package com.cake.task.master.core.runner.task;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.HttpSendUtils;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.order.OrderModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description task：代付订单表
 * @Author yoko
 * @Date 2020/11/1 21:25
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskOrderOut {

    private final static Logger log = LoggerFactory.getLogger(TaskOrderOut.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: task：执行代付订单的数据同步
     * <p>
     *     每1每秒运行一次
     *     1.查询出已处理的代付订单状态不是初始化的订单数据数据。
     *     2.根据同步地址进行数据同步。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 1000) // 每秒执行
    public void orderNotify() throws Exception{
//        log.info("----------------------------------TaskOrderOut.orderNotify()----start");

        // 获取已成功的订单数据，并且为同步给下游的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 1, 0,1,0,null);
        List<OrderOutModel> synchroList = ComponentUtil.taskOrderOutService.getDataList(statusQuery);
        for (OrderOutModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_OUT_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    // 进行数据同步

//                    String sendData = "total_amount=" + data.getOrderMoney() + "&" + "out_trade_no=" + data.getOutTradeNo() + "&" + "trade_status=" + 1
//                            + "&" + "trade_no=" + data.getOrderNo() + "&" + "trade_time=" + data.getCreateTime();
                    int trade_status = 2;
                    if (data.getOrderStatus() == 2){
                        trade_status = 2;
                    }else if (data.getOrderStatus() == 4){
                        trade_status = 1;
                    }

                    String picture_ads = "";
                    if (!StringUtils.isBlank(data.getPictureAds())){
                        picture_ads = data.getPictureAds();
                    }
                    String fail_info = "";
                    if (!StringUtils.isBlank(data.getFailInfo())){
                        fail_info = data.getFailInfo();
                    }

                    Map<String, Object> sendMap = new HashMap<>();
                    sendMap.put("out_trade_no", data.getOutTradeNo());
                    sendMap.put("trade_status", trade_status);
                    sendMap.put("trade_no", data.getOrderNo());
                    sendMap.put("picture_ads", picture_ads);
                    sendMap.put("fail_info", fail_info);
                    sendMap.put("trade_time", data.getCreateTime());

                    String sendUrl = "";
                    if (!StringUtils.isBlank(data.getNotifyUrl())){
                        sendUrl = data.getNotifyUrl();
                    }else {
                        sendUrl = ComponentUtil.loadConstant.defaultNotifyUrlOut;
                    }
//                    sendUrl = "http://localhost:8085/pay/data/fine";
//                    String resp = HttpSendUtils.sendGet(sendUrl + "?" + URLEncoder.encode(sendData,"UTF-8"), null, null);
//                    String resp = HttpSendUtils.sendGet(sendUrl + "?" + sendData, null, null);
                    String resp = HttpSendUtils.sendPostAppJson(sendUrl , JSON.toJSONString(sendMap));
                    if (resp.indexOf("ok") > -1){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskOrderOutService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskOrderOut.orderNotify()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrderOut.orderNotify() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                ComponentUtil.taskOrderOutService.updateStatus(statusModel);
            }
        }
    }


}
