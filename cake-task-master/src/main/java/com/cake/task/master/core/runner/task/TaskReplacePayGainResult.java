package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayGainResultModel;
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
 * @Description task:第三方代付主动拉取结果返回的订单结果
 * @Author yoko
 * @Date 2021/6/23 10:07
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskReplacePayGainResult {

    private final static Logger log = LoggerFactory.getLogger(TaskReplacePayGainResult.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: 处理第三方订单
     * <p>
     *     每秒运行一次
     *     1.查询未跑的订单。
     *     2.更新代付订单表的订单状态(成功/失败)。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 5000) // 每1秒执行
    public void success() throws Exception{
//        log.info("----------------------------------TaskReplacePayGainResult.success()----start");

        // 获取未跑的第三方成功订单
//        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,0,null);
//        List<ReplacePayGainResultModel> synchroList = ComponentUtil.taskReplacePayGainResultService.getDataList(statusQuery);
//        for (ReplacePayGainResultModel data : synchroList){
//            try{
//                // 锁住这个数据流水
//                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_REPLACE_PAY_GAIN_RESULT, data.getId());
//                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
//                if (flagLock){
//                    StatusModel statusModel = null;
//
//                    OrderOutModel orderOutUpdate = TaskMethod.assembleOrderOutUpdateBySand(data);
//                    int num = ComponentUtil.orderOutService.updateOrderStatusBySand(orderOutUpdate);
//                    if (num > 0){
//                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
//                    }else {
//                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"更新订单状态失败!");
//                    }
//
//                    // 更新状态
//                    ComponentUtil.taskReplacePayGainResultService.updateStatus(statusModel);
//                    // 解锁
//                    ComponentUtil.redisIdService.delLock(lockKey);
//                }
////                log.info("----------------------------------TaskReplacePayGainResult.success()----end");
//            }catch (Exception e){
//                log.error(String.format("this TaskReplacePayGainResult.success() is error , the dataId=%s !", data.getId()));
//                e.printStackTrace();
//                // 更新此次task的状态：更新成失败：因为必填项没数据
//                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
//                ComponentUtil.taskReplacePayGainResultService.updateStatus(statusModel);
//            }
//        }
    }


}
