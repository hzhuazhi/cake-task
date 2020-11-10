package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.interest.InterestModel;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
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
 * @Description task:利益者收益数据
 * @Author yoko
 * @Date 2020/11/10 18:42
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskInterestProfit {


    private final static Logger log = LoggerFactory.getLogger(TaskInterestProfit.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: task：利益者的收益数据的金额累加
     * <p>
     *     每30每秒运行一次
     *     1.查询未跑的利益者的收益流水信息
     *     2.给利益者收益金额进行累加
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 30000) // 每30秒执行
    public void profit() throws Exception{
//        log.info("----------------------------------TaskInterestProfit.profit()----start");

        // 获取未跑的卡商扣款流水信息，并且订单状态不是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,0,null);
        List<InterestProfitModel> synchroList = ComponentUtil.taskInterestProfitService.getDataList(statusQuery);
        for (InterestProfitModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_INTEREST_PROFIT, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;
                    // 组装利益者收益累加
                    InterestModel interestUpdate = TaskMethod.assembleInterestByChanagerMoney(data.getInterestId(), 1, data.getProfit());
                    // 锁住此利益者
                    String lockKey_interest = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY_PROFIT, data.getMerchantId());
                    boolean flagLock_interest = ComponentUtil.redisIdService.lock(lockKey_interest);
                    if (flagLock_interest){
                        int num = ComponentUtil.interestService.updateAddOrSubtractMoney(interestUpdate);
                        if (num > 0){
                            flag = true;
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_interest);
                    }
                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskInterestProfitService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskInterestProfit.profit()----end");
            }catch (Exception e){
                log.error(String.format("this TaskInterestProfit.profit() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskInterestProfitService.updateStatus(statusModel);
            }
        }
    }

}
