package com.cake.task.master.core.runner.task;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.common.utils.jinfupay.JinFuApi;
import com.cake.task.master.core.common.utils.jinfupay.model.JinFuPayResponse;
import com.cake.task.master.core.common.utils.jinfupay.model.ResultResponse;
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
     * @Description: task：执行代付订单查询订单状态-杉德
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
    public void orderStatusQuerySandPay() throws Exception{
//        log.info("----------------------------------TaskReplacePayGain.orderStatusQuerySandPay()----start");

        // 获取未跑的代付订单，并且订单状态不是初始化的
        ReplacePayGainModel replacePayGainQuery = TaskMethod.assembleReplacePayGainQuery(limitNum,1,0,0,0,1,"1");
        List<ReplacePayGainModel> synchroList = ComponentUtil.taskReplacePayGainService.getDataList(replacePayGainQuery);
        for (ReplacePayGainModel data : synchroList){
            ReplacePayGainModel replacePayGainUpdate = null;
            ReplacePayGainModel replacePayGainTime = null;
            String dataExplain = "";
            try{
                boolean flag_up = false;
                ReplacePayGainResultModel replacePayGainResultAdd = null;
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
                                if (!sandResponse.resultFlag.equals("2")){
                                    // 添加数据到：第三方代付主动拉取结果返回的订单结果表中
                                    flag_up = true;
                                    replacePayGainResultAdd = TaskMethod.assembleReplacePayGainResultAdd(data, sandResponse);
                                }

                            }else {
                                // 失败
                                dataExplain = "查询衫德订单订单状态时，返回空状态码";
                                replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                            }
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
                    int num = ComponentUtil.taskReplacePayGainService.updateStatus(replacePayGainUpdate);
                    if (num > 0 && flag_up){
                        // 正式添加第三方结果
                        ComponentUtil.replacePayGainResultService.add(replacePayGainResultAdd);
                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskReplacePayGain.orderStatusQuerySandPay()----end");
            }catch (Exception e){
                log.error(String.format("this TaskReplacePayGain.orderStatusQuerySandPay() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, null, null, "异常失败try!");
                ComponentUtil.taskReplacePayGainService.updateStatus(replacePayGainUpdate);
            }
        }
    }




    /**
     * @Description: task：执行代付订单查询订单状态-金服
     * <p>
     *     每5每秒运行一次
     *     1.查询代付未跑的代付订单信息。
     *     2.调用金服查询获取订单状态。
     *     3.更新代付订单的订单状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 5000) // 每秒执行
    public void orderStatusQueryJinFu() throws Exception{
//        log.info("----------------------------------TaskReplacePayGain.orderStatusQueryJinFu()----start");

        // 获取未跑的代付订单，并且订单状态不是初始化的
        ReplacePayGainModel replacePayGainQuery = TaskMethod.assembleReplacePayGainQuery(limitNum,1,0,0,0,2,"1");
        List<ReplacePayGainModel> synchroList = ComponentUtil.taskReplacePayGainService.getDataList(replacePayGainQuery);
        for (ReplacePayGainModel data : synchroList){
            ReplacePayGainModel replacePayGainUpdate = null;
            ReplacePayGainModel replacePayGainTime = null;
            String dataExplain = "";
            try{
                boolean flag_up = false;
                ReplacePayGainResultModel replacePayGainResultAdd = null;
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_REPLACE_PAY_GAIN, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    ReplacePayModel replacePayQuery = TaskMethod.assembleReplacePayByIdQuery(data.getReplacePayId());
                    ReplacePayModel replacePayModel = ComponentUtil.replacePayService.getReplacePayModel(replacePayQuery,0);
                    if (replacePayModel != null){
                        // 调用金服，获取订单状态
                        JinFuPayResponse jinFuPayResponse = JinFuApi.jinFuQueryOrder(replacePayModel, data.getOrderNo());
                        if (jinFuPayResponse != null && jinFuPayResponse.code == 0){
                            // 判断订单状态
                            ResultResponse resultResponse = jinFuPayResponse.result;
                            if (resultResponse != null){
                                if (!StringUtils.isBlank(resultResponse.status)){
                                    if (resultResponse.status.equals("SUCCESS")){
                                        // 成功：这里表示单子真正成功
                                        replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 3,0, null, null, dataExplain);
                                    }else if (resultResponse.status.equals("FAIL")){
                                        // 成功：这里表示单子是失败的
                                        replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 3,0, null, null, dataExplain);
                                    }else if (resultResponse.status.equals("DEALING")){
                                        // 失败：银行结果处理中，拉取的状态的状态则定为失败
                                        dataExplain = "查询金服订单订单状态时，返回处理中(等银行返回明确结果)";
                                        replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                        replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                                    }else if (resultResponse.status.equals("REFUND")){
                                        // 成功：这里表示单子是退票的，退票需要人为处理
                                        // 退票现在的处理机制：1先把订单拉取结果反馈的拉取结果表，并且拉取结果表进行运算把代付订单表的状态修改成质疑状态
                                        replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 3,0, null, null, dataExplain);
                                    }
                                    if (!resultResponse.status.equals("DEALING")){
                                        // 添加数据到：第三方代付主动拉取结果返回的订单结果表中
                                        flag_up = true;
                                        replacePayGainResultAdd = TaskMethod.assembleReplacePayGainResultByJinFuAdd(data, resultResponse);
                                    }

                                }else {
                                    // 失败
                                    dataExplain = "查询金服订单订单状态时，返回空状态码";
                                    replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                    replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                                }


                            }else {
                                // 失败
                                dataExplain = "查询金服订单订单状态时，返回空订单信息";
                                replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                            }

                        }else{
                            // 失败
                            dataExplain = "查询金服订单订单状态时，返回失败码";
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
                    int num = ComponentUtil.taskReplacePayGainService.updateStatus(replacePayGainUpdate);
                    if (num > 0 && flag_up){
                        // 正式添加第三方结果数据
                        ComponentUtil.replacePayGainResultService.add(replacePayGainResultAdd);
                    }
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskReplacePayGain.orderStatusQueryJinFu()----end");
            }catch (Exception e){
                log.error(String.format("this TaskReplacePayGain.orderStatusQueryJinFu() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, null, null, "异常失败try!");
                ComponentUtil.taskReplacePayGainService.updateStatus(replacePayGainUpdate);
            }
        }
    }

}
