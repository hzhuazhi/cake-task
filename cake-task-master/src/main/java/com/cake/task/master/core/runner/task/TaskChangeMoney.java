package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.change.ChangeMoneyModel;
import com.cake.task.master.core.model.interest.InterestModel;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
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
 * @Description task:变更金额纪录
 * @Author yoko
 * @Date 2020/11/14 17:26
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskChangeMoney {


    private final static Logger log = LoggerFactory.getLogger(TaskChangeMoney.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: task：金额变更
     * <p>
     *     每10每秒运行一次
     *     1.查询未跑的金额变更流水信息
     *     2.判断变更金额是卡商还是中转站还是利益者，进行实际金额变更
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 10000) // 每10秒执行
    public void runStatus() throws Exception{
//        log.info("----------------------------------TaskChangeMoney.runStatus()----start");

        // 获取未跑的变更金额流水信息
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,0,null);
        List<ChangeMoneyModel> synchroList = ComponentUtil.taskChangeMoneyService.getDataList(statusQuery);
        for (ChangeMoneyModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_CHANGE_MONEY, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;

                    int type = 0;
                    if (data.getChangeType() == 1){
                        // 加金额
                        type = 1;
                    }else if (data.getChangeType() == 2){
                        // 减金额
                        type = 2;
                    }

                    if (data.getAscriptionType() == 1){
                        // 卡商金额变更
                        // 组装卡商金额变更
                        MerchantModel merchantUpdate = TaskMethod.assembleMerchantByChanagerMoney(data.getAscriptionId(), type, data.getMoney());
                        if (data.getChangeField() == 1){
                            // 余额变更
                            // 锁住此卡商
                            String lockKey_merchant = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY, data.getAscriptionId());
                            boolean flagLock_merchant = ComponentUtil.redisIdService.lock(lockKey_merchant);
                            if (flagLock_merchant){
                                int num = ComponentUtil.merchantService.updateAddOrSubtractBalance(merchantUpdate);
                                if (num > 0){
                                    flag = true;
                                }
                                // 解锁
                                ComponentUtil.redisIdService.delLock(lockKey_merchant);
                            }
                        }else if (data.getChangeField() == 2){
                            // 收益变更
                            // 锁住此卡商
                            String lockKey_merchant = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY_PROFIT, data.getAscriptionId());
                            boolean flagLock_merchant = ComponentUtil.redisIdService.lock(lockKey_merchant);
                            if (flagLock_merchant){
                                int num = ComponentUtil.merchantService.updateAddOrSubtractProfit(merchantUpdate);
                                if (num > 0){
                                    flag = true;
                                }
                                // 解锁
                                ComponentUtil.redisIdService.delLock(lockKey_merchant);
                            }

                        }

                    }else if (data.getAscriptionType() == 2){
                        // 中转站金额变更
                        flag = true;
                    }else if (data.getAscriptionType() == 3){
                        // 利益人金额变更

                        // 组装利益者收益变更
                        InterestModel interestUpdate = TaskMethod.assembleInterestByChanagerMoney(data.getAscriptionId(), type, data.getMoney());
                        // 锁住此利益者
                        String lockKey_interest = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY_PROFIT, data.getAscriptionId());
                        boolean flagLock_interest = ComponentUtil.redisIdService.lock(lockKey_interest);
                        if (flagLock_interest){
                            int num = ComponentUtil.interestService.updateAddOrSubtractBalance(interestUpdate);
                            if (num > 0){
                                flag = true;
                            }
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey_interest);
                        }

                    }
                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskChangeMoneyService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskChangeMoney.runStatus()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChangeMoney.runStatus() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskChangeMoneyService.updateStatus(statusModel);
            }
        }
    }
    
}
