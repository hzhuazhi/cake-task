package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.TaskMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description task:提现记录
 * @Author yoko
 * @Date 2020/11/19 19:22
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskWithdraw {

    private final static Logger log = LoggerFactory.getLogger(TaskWithdraw.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: 把执行提现的结果同步给各个角色的提现记录
     * <p>
     *     每10每秒运行一次
     *     1.查询未跑的提现信息，并且订单状态不是初始化。
     *     2.更新各个角色的提现记录的提现订单状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 10000) // 每秒执行
    public void send() throws Exception{
//        log.info("----------------------------------TaskWithdraw.send()----start");

        // 获取未跑的提现记录，并且订单状态不是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 1, 0,1,0,null);
        List<WithdrawModel> synchroList = ComponentUtil.taskWithdrawService.getDataList(statusQuery);
        for (WithdrawModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_WITHDRAW_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;

                    if (data.getWithdrawType() == 1){
                        // 利益者提现
                        // #后续需求出来，补充业务逻辑
                    }else if (data.getWithdrawType() == 2){
                        // 卡商提现
                    }else if (data.getWithdrawType() == 3){
                        // 渠道提现
                        // 判断指派类型
                        if (data.getOutType() == 1){
                            // 指派给卡商进行下发的

                            // 判断此次下发，卡商是否可以得到手续费的收益：2块，5块
                            MerchantProfitModel merchantProfitAdd = TaskMethod.assembleMerchantProfitByWithdraw(data);
                            MerchantModel merchantUpdate = null;
                            if (data.getOrderStatus() == 4){
                                // 因为提现订单属于成功的，则此卡商代收收到的钱是在余额中的，由于下发成功，则余额要减此次下发的金额
                                merchantUpdate = TaskMethod.assembleMerchantByChanagerMoney(data.getMerchantId(), 2, data.getOrderMoney());
                            }

                            boolean flag_lock = false;
                            String lockKey_merchant = "";
                            // 组装更新渠道提现记录的订单状态
                            ChannelWithdrawModel channelWithdrawUpdate = TaskMethod.assembleChannelWithdrawOrderStatusUpdate(data);
                            if (merchantUpdate != null){
                                // 余额变更
                                // 锁住此卡商
                                lockKey_merchant = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY, data.getMerchantId());
                                boolean flagLock_merchant = ComponentUtil.redisIdService.lock(lockKey_merchant);
                                if (flagLock_merchant){
                                    flag_lock = true;
                                }
                            }
                            if (flag_lock){
                                // 只有卡商被锁住才能执行事务的逻辑
                                flag = ComponentUtil.taskWithdrawService.handleChannelWithdraw(merchantProfitAdd, merchantUpdate, channelWithdrawUpdate);
                                // 解锁
                                ComponentUtil.redisIdService.delLock(lockKey_merchant);
                            }



                        }else if (data.getOutType() == 2){
                            // 指派给中转站进行下发的
                        }else if (data.getOutType() == 3){
                            // 指派给平台进行下发的
                        }

                    }

                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskWithdrawService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskWithdraw.send()----end");
            }catch (Exception e){
                log.error(String.format("this TaskWithdraw.send() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,"异常失败try!");
                ComponentUtil.taskWithdrawService.updateStatus(statusModel);
            }
        }
    }
}
