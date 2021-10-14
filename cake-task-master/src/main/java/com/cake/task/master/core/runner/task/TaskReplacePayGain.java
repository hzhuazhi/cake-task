package com.cake.task.master.core.runner.task;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.common.utils.sandpay.method.OrderQuery;
import com.cake.task.master.core.common.utils.sandpay.model.AgentPayResponse;
import com.cake.task.master.core.model.replacepay.ReplacePayGainModel;
import com.cake.task.master.core.model.replacepay.ReplacePayGainResultModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
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
 * @Description task:第三方代付主动拉取结果
 * @Author yoko
 * @Date 2021/6/22 17:29
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskReplacePayGain {

    private final static Logger log = LoggerFactory.getLogger(TaskReplacePayGain.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: task：执行代付订单查询订单状态
     * <p>
     *     每5每秒运行一次
     *     1.查询代付未跑的代付订单信息。
     *     2.调用衫德查询获取订单状态。
     *     3.更新代付订单的订单状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 5000) // 每秒执行
    public void orderStatusQuery() throws Exception{
//        log.info("----------------------------------TaskReplacePayGain.orderStatusQuery()----start");

        // 获取未跑的代付订单，并且订单状态不是初始化的
        ReplacePayGainModel replacePayGainQuery = TaskMethod.assembleReplacePayGainQuery(limitNum,1,0,0,0,"1");
        List<ReplacePayGainModel> synchroList = ComponentUtil.taskReplacePayGainService.getDataList(replacePayGainQuery);
        for (ReplacePayGainModel data : synchroList){
            ReplacePayGainModel replacePayGainUpdate = null;
            ReplacePayGainModel replacePayGainTime = null;
            String dataExplain = "";
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_REPLACE_PAY_GAIN, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    ReplacePayModel replacePayQuery = TaskMethod.assembleReplacePayByIdQuery(data.getReplacePayId());
                    ReplacePayModel replacePayModel = ComponentUtil.replacePayService.getReplacePayModel(replacePayQuery,0);
                    if (replacePayModel != null){
                        // 调用衫德，获取订单状态
                        AgentPayResponse sandResponse = OrderQuery.sandOrderQuery(replacePayModel, data);
                        if (sandResponse != null){
                            log.info("TaskReplacePayGain.orderStatusQuery()...sandResponse:"+ JSON.toJSONString(sandResponse));

                            // 判断订单状态
                            if (!StringUtils.isBlank(sandResponse.resultFlag)){
                                if (sandResponse.resultFlag.equals("2")){
                                    // 银行结果处理中，拉取的状态的状态则定为失败
                                    dataExplain = "查询衫德订单订单状态时，返回处理中(等银行返回明确结果)";
                                    replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                    replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                                }else{
                                    // 成功
                                    replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 3,0, null, null, dataExplain);
                                }
                            }else {
                                // 失败
                                dataExplain = "查询衫德订单订单状态时，返回空状态码";
                                replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                            }

                            // 添加数据到：第三方代付主动拉取结果返回的订单结果表中
                            ReplacePayGainResultModel replacePayGainResultAdd = TaskMethod.assembleReplacePayGainResultAdd(data, sandResponse);
                            ComponentUtil.replacePayGainResultService.add(replacePayGainResultAdd);

                        }else{
                            // 失败
                            dataExplain = "查询衫德订单订单状态时，返回失败码";
                            replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                            replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                        }
                    }else {
                        // 失败
                        dataExplain = "根据代付ID未查询到代付资源表的信息";
                        replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                        replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                    }


                    // 更新状态
                    ComponentUtil.taskReplacePayGainService.updateStatus(replacePayGainUpdate);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskReplacePayGain.orderStatusQuery()----end");
            }catch (Exception e){
                log.error(String.format("this TaskReplacePayGain.orderStatusQuery() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, null, null, "异常失败try!");
                ComponentUtil.taskReplacePayGainService.updateStatus(replacePayGainUpdate);
            }
        }
    }



}
