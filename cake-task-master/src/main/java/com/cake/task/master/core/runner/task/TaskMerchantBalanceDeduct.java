package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.interest.InterestMerchantModel;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.task.base.StatusModel;
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
 * @Description task:卡商扣减余额流水
 * @Author yoko
 * @Date 2020/11/10 16:21
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskMerchantBalanceDeduct {

    private final static Logger log = LoggerFactory.getLogger(TaskMerchantBalanceDeduct.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: task：卡商扣款流水业务处理
     * <p>
     *     每10每秒运行一次
     *     1.查询未跑的扣款流水订单状态大于1的扣款流水信息
     *     2.如果订单号是失败状态，则需要给把此订单金额返还给卡商。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 10000) // 每10秒执行
    public void orderStatus() throws Exception{
//        log.info("----------------------------------TaskMerchantBalanceDeduct.orderStatus()----start");

        // 获取未跑的卡商扣款流水信息，并且订单状态不是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 1, 0,0,0,null);
        List<MerchantBalanceDeductModel> synchroList = ComponentUtil.taskMerchantBalanceDeductService.getDataList(statusQuery);
        for (MerchantBalanceDeductModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MERCHANT_BALANCE_DEDUCT, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;
                    if (data.getOrderStatus() == 2){
                        // 订单状态属于失败状态，则需要把订单金额返还给卡商

                        // 组装卡商余额的返还
                        MerchantModel merchantUpdate = TaskMethod.assembleMerchantUpdateMoney(data.getMerchantId(), data.getMoney());
                        // 锁住此卡商
                        String lockKey_merchant = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY, data.getMerchantId());
                        boolean flagLock_merchant = ComponentUtil.redisIdService.lock(lockKey_merchant);
                        if (flagLock_merchant){
                            int num = ComponentUtil.merchantService.updateBalance(merchantUpdate);
                            if (num > 0){
                                flag = true;
                            }
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey_merchant);
                        }
                    }else if (data.getOrderStatus() == 4){
                        // 订单是成功状态：只需要修改此条流水的运行状态
                        flag = true;
                    }
                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskMerchantBalanceDeductService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskMerchantBalanceDeduct.orderStatus()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMerchantBalanceDeduct.orderStatus() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskMerchantBalanceDeductService.updateStatus(statusModel);
            }
        }
    }
    
}
