package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.jinfupay.JinFuApi;
import com.cake.task.master.core.common.utils.jinfupay.model.JinFuPayResponse;
import com.cake.task.master.core.common.utils.sandpay.method.MerBalanceQuery;
import com.cake.task.master.core.common.utils.sandpay.model.AgentPayResponse;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
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
 * @Description task：代付信息
 * @Author yoko
 * @Date 2021/6/22 14:03
 * @Version 1.0
 */

@Component
@EnableScheduling
public class TaskReplacePay {

    private final static Logger log = LoggerFactory.getLogger(TaskReplacePay.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: task：代付信息，检测杉德代付账户的余额
     * <p>
     *     每1分钟运行一次
     *     1.查询代付信息表。
     *     2.查询衫德此代付账户的余额。
     *     3.更新本地此代付的余额。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 30000) // 每半分钟执行
//    @Scheduled(fixedDelay = 6000) // 每分钟执行
    public void sandBalanceQuery() throws Exception{
//        log.info("----------------------------------TaskReplacePay.sandBalanceQuery()----start");

        // 获取代付信息
        ReplacePayModel replacePayQuery = TaskMethod.assembleReplacePayQuery(0,0,1,2,1);
        List<ReplacePayModel> synchroList = ComponentUtil.replacePayService.findByCondition(replacePayQuery);
        for (ReplacePayModel data : synchroList){
            try{
                // 调用衫德查询余额
                AgentPayResponse sandResponse = MerBalanceQuery.sandBalanceQuery(data);
                if (sandResponse != null){
                    // 更新代付账户余额
                    ReplacePayModel replacePayModel = TaskMethod.assembleReplacePayUpdateBalance(data.getId(), sandResponse.balance, sandResponse.creditAmt);
                    ComponentUtil.replacePayService.updateBalance(replacePayModel);
                }

//                log.info("----------------------------------TaskReplacePay.sandBalanceQuery()----end");
            }catch (Exception e){
                log.error(String.format("this TaskReplacePay.sandBalanceQuery() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
            }
        }
    }



    /**
     * @Description: task：代付信息，检测金服代付账户的余额
     * <p>
     *     每1分钟运行一次
     *     1.查询代付信息表。
     *     2.查询金服此代付账户的余额。
     *     3.更新本地此代付的余额。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 30000) // 每半分钟执行
//    @Scheduled(fixedDelay = 6000) // 每分钟执行
    public void jinFuBalanceQuery() throws Exception{
//        log.info("----------------------------------TaskReplacePay.jinFuBalanceQuery()----start");

        // 获取代付信息
        ReplacePayModel replacePayQuery = TaskMethod.assembleReplacePayQuery(0,0,2,2,1);
        List<ReplacePayModel> synchroList = ComponentUtil.replacePayService.findByCondition(replacePayQuery);
        for (ReplacePayModel data : synchroList){
            try{
                // 调用金服查询余额
                JinFuPayResponse jinFuPayResponse = JinFuApi.jinFuQueryBalance(data);
                if (jinFuPayResponse != null && jinFuPayResponse.code == 0){
                    // 更新代付账户余额
                    ReplacePayModel replacePayModel = TaskMethod.assembleReplacePayUpdateBalanceByJinFu(data.getId(), jinFuPayResponse);
                    if (replacePayModel != null){
                        ComponentUtil.replacePayService.updateBalance(replacePayModel);
                    }
                }

//                log.info("----------------------------------TaskReplacePay.jinFuBalanceQuery()----end");
            }catch (Exception e){
                log.error(String.format("this TaskReplacePay.jinFuBalanceQuery() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
            }
        }
    }

}
