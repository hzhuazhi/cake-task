package com.cake.task.master.core.runner;

import com.cake.task.master.core.common.exception.ServiceException;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.common.utils.huichaogpay.HuiChaoApi;
import com.cake.task.master.core.common.utils.huichaogpay.model.response.TransferQuery;
import com.cake.task.master.core.common.utils.huichaogpay.model.response.TransferQueryResponse;
import com.cake.task.master.core.model.replacepay.ReplacePayGainModel;
import com.cake.task.master.core.model.replacepay.ReplacePayGainResultModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author yoko
 * @desc 线程：第三方代付主动拉取结果
 * @create 2021-12-10 5:41
 **/
@Component
public class ReplacePayGainRunner implements ApplicationRunner {
    private final static Logger log = LoggerFactory.getLogger(ReplacePayGainRunner.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;

    Thread runHuiChaoQueryStatus = null;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("ReplacePayGainRunner...RUN");

        runHuiChaoQueryStatus = new RunHuiChaoQueryStatus();
        runHuiChaoQueryStatus.start();


    }


    /**
     * @Description: task：执行代付订单查询订单状态-汇潮
     * <p>
     *     每5每秒运行一次
     *     1.查询代付未跑的代付订单信息。
     *     2.调用汇潮查询获取订单状态。
     *     3.更新代付订单的订单状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    class RunHuiChaoQueryStatus extends Thread{
        @Override
        public void run() {
            while (1==1) {
                try {
                    // 9秒钟
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                try {
//                    if (1==1){
//                        throw new ServiceException("asd","asdg");
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }


                //        log.info("----------------------------------ReplacePayGainRunner.RunHuiChaoQueryStatus()----start");

                // 获取未跑的代付订单，并且订单状态不是初始化的
                ReplacePayGainModel replacePayGainQuery = TaskMethod.assembleReplacePayGainQuery(limitNum,1,0,0,0,3,"1");
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
                                // 调用汇潮，获取订单状态
                                TransferQueryResponse transferQueryResponse = HuiChaoApi.transferQueryFixed(replacePayModel, data.getOrderNo());
                                if (transferQueryResponse != null && transferQueryResponse.code.equals("0000")){
                                    // 判断订单状态
                                    TransferQuery resultResponse = transferQueryResponse.transfer;
                                    if (resultResponse != null){
                                        if (!StringUtils.isBlank(resultResponse.state)){
                                            if (resultResponse.state.equals("00")){
                                                // 成功：这里表示单子真正成功
                                                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 3,0, null, null, dataExplain);
                                            }else if (resultResponse.state.equals("11")){
                                                // 成功：这里表示单子是失败的
                                                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 3,0, null, null, dataExplain);
                                            }else if (resultResponse.state.equals("22")){
                                                // 失败：银行结果处理中，拉取的状态的状态则定为失败
                                                dataExplain = "查询汇潮订单订单状态时，返回处理中(等银行返回明确结果)";
                                                replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                                replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                                            }
                                            if (!resultResponse.state.equals("22")){
                                                // 添加数据到：第三方代付主动拉取结果返回的订单结果表中
                                                flag_up = true;
                                                replacePayGainResultAdd = TaskMethod.assembleReplacePayGainResultByHuiChaoAdd(data, resultResponse);
                                            }

                                        }else {
                                            // 失败
                                            dataExplain = "查询汇潮订单订单状态时，返回空状态码";
                                            replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                            replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                                        }


                                    }else {
                                        // 失败
                                        dataExplain = "查询汇潮订单订单状态时，返回空订单信息";
                                        replacePayGainTime = TaskMethod.assembleReplacePayGainTime(replacePayModel, data.getNowGainDataTime());
                                        replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, replacePayGainTime.getNextTime(), replacePayGainTime.getNowGainDataTime(), dataExplain);
                                    }

                                }else{
                                    // 失败
                                    dataExplain = "查询汇潮订单订单状态时，返回失败码";
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

//                log.info("----------------------------------ReplacePayGainRunner.RunHuiChaoQueryStatus()----end");
                    }catch (Exception e){
                        log.error(String.format("this ReplacePayGainRunner.RunHuiChaoQueryStatus() is error , the dataId=%s !", data.getId()));
                        e.printStackTrace();
                        // 更新此次task的状态：更新成失败：因为ERROR
                        replacePayGainUpdate = TaskMethod.assembleReplacePayGainUpdate(data.getId(), 2,0, null, null, "异常失败try!");
                        ComponentUtil.taskReplacePayGainService.updateStatus(replacePayGainUpdate);
                    }
                }

            }
        }
    }
}
