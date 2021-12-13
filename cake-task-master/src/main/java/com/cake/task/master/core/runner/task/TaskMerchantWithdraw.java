package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.StringUtil;
import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.merchant.MerchantWithdrawModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description task:卡商的提现记录
 * @Author yoko
 * @Date 2021/1/11 15:50
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskMerchantWithdraw {

    private final static Logger log = LoggerFactory.getLogger(TaskMerchantWithdraw.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: 执行卡商的提现记录
     * <p>
     *     每10每秒运行一次
     *     1.查询未跑的提现信息，并且订单状态不是初始化。
     *     2.更新卡商余额，可提现收益。
     *     3.更新提现记录的提现订单状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
//    @Scheduled(fixedDelay = 10000) // 每秒执行
    public void handle() throws Exception{
//        log.info("----------------------------------TaskMerchantWithdraw.handle()----start");

        // 获取未跑的提现记录，并且订单状态不是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,1,null);
        List<MerchantWithdrawModel> synchroList = ComponentUtil.taskMerchantWithdrawService.getDataList(statusQuery);
        for (MerchantWithdrawModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MERCHANT_WITHDRAW_RUN, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;

                    // 查询卡商余额以及收益
                    MerchantModel merchantQuery = TaskMethod.assembleMerchantQuery(data.getMerchantId(), null,0,0, 0, null);
                    MerchantModel merchantModel = (MerchantModel)ComponentUtil.merchantService.findByObject(merchantQuery);
                    if (merchantModel == null || merchantModel.getId() == null || merchantModel.getId() <= 0){
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"根据卡商ID查询卡商信息为空!");
                        // 更新状态
                        ComponentUtil.taskMerchantWithdrawService.updateStatus(statusModel);
                        continue;
                    }

                    // check校验要提现的金额是否小于等于卡商的余额
                    if (!StringUtils.isBlank(merchantModel.getBalance())){
                        boolean flag_balance = StringUtil.getBigDecimalSubtract(merchantModel.getBalance(), data.getMoney());
                        if (!flag_balance){
                            // 失败
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"提现金额大于余额!");
                            // 更新状态
                            ComponentUtil.taskMerchantWithdrawService.updateStatus(statusModel);
                            continue;
                        }
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"卡商余额为空!");
                        // 更新状态
                        ComponentUtil.taskMerchantWithdrawService.updateStatus(statusModel);
                        continue;
                    }


                    // check校验要提现的金额是否小于等于卡商的收益
                    if (!StringUtils.isBlank(merchantModel.getProfit())){
                        boolean flag_profit = StringUtil.getBigDecimalSubtract(merchantModel.getProfit(), data.getMoney());
                        if (!flag_profit){
                            // 失败
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"提现金额大于收益!");
                            // 更新状态
                            ComponentUtil.taskMerchantWithdrawService.updateStatus(statusModel);
                            continue;
                        }
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"卡商收益为空!");
                        // 更新状态
                        ComponentUtil.taskMerchantWithdrawService.updateStatus(statusModel);
                        continue;
                    }


                    // 余额变更
                    // 锁住此卡商-余额
                    String lockKey_balance = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY, data.getMerchantId());
                    boolean flagLock_balance = ComponentUtil.redisIdService.lock(lockKey_balance);

                    // 收益变更
                    // 锁住此卡商-收益
                    String lockKey_profit = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY_PROFIT, data.getMerchantId());
                    boolean flagLock_profit = ComponentUtil.redisIdService.lock(lockKey_profit);
                    if (flagLock_balance && flagLock_profit){
                        // 卡商提现：余额要扣减
                        MerchantModel merchantUpdateBalance = TaskMethod.assembleMerchantByChanagerMoney(data.getMerchantId(), 2, data.getMoney());

                        // 卡商提现：收益要扣减
                        MerchantModel merchantUpdateProfit = TaskMethod.assembleMerchantByChanagerMoney(data.getMerchantId(), 2, data.getMoney());

                        // 组装卡商提现，订单更新状态
                        MerchantWithdrawModel merchantWithdrawUpdate = TaskMethod.assembleMerchantWithdrawOrderStatusUpdate(data.getId(), 4);

                        flag = ComponentUtil.taskMerchantWithdrawService.handleMerchantWithdraw(merchantUpdateBalance, merchantUpdateProfit, merchantWithdrawUpdate);

                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_balance);
                        ComponentUtil.redisIdService.delLock(lockKey_profit);

                    }


                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskMerchantWithdrawService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskMerchantWithdraw.handle()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMerchantWithdraw.handle() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskMerchantWithdrawService.updateStatus(statusModel);
            }
        }
    }
}
