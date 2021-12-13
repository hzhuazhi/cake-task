package com.cake.task.master.core.runner;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.DateUtil;
import com.cake.task.master.core.common.utils.HttpSendUtils;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.interest.InterestMerchantModel;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayInfoModel;
import com.cake.task.master.core.model.replacepay.ReplacePayStrategyModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description 线程：代付订单
 * @Author yoko
 * @Date 2020/11/1 21:25
 * @Version 1.0
 */
@Component
//@Order(1)
public class OrderOutRunner implements ApplicationRunner {
    private final static Logger log = LoggerFactory.getLogger(OrderOutRunner.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    Thread runStatusThread = null;

    Thread runNotifyThread = null;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("OrderOutRunner...RUN");

        runStatusThread = new RunStatusThread();
        runStatusThread.start();

        runNotifyThread = new RunNotifyThread();
        runNotifyThread.start();
    }



    /**
     * @Description: task：执行代付订单的订单状态不是初始化状态的逻辑处理
     * <p>
     *     每5每秒运行一次
     *     1.查询代付订单订单状态不是初始化的订单信息。
     *     2.更新订单对应的卡商的扣款流水的订单状态。
     *     3.如果订单号是成功状态，则需要给各个利益者进行收益分配。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    class RunStatusThread extends Thread{
//        log.info("----------------------------------OrderOutRunner.orderStatus()----start");

        @Override
        public void run() {
            while (1 == 1) {
                try {
                    // 2分钟
                    Thread.sleep(1000 * 60 * 2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // start.....
                //        log.info("----------------------------------OrderOutRunner.orderStatus()----start");
                int curday = 0;
                int curdayStart =0;
                int curdayEnd = 0;
                try {
                    curday = DateUtil.getDayNumber(new Date());// 当天
                    curdayStart = DateUtil.getMinMonthDate();// 月初
                    curdayEnd = DateUtil.getMaxMonthDate();// 月末
                }catch (Exception e){
                    e.printStackTrace();
                }

                // 获取未跑的代付订单，并且订单状态不是初始化的
                StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 1, 0,0,0,null);
                List<OrderOutModel> synchroList = ComponentUtil.taskOrderOutService.getDataList(statusQuery);
                for (OrderOutModel data : synchroList){
                    try{
                        // 锁住这个数据流水
                        String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_OUT, data.getId());
                        boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                        if (flagLock){
                            StatusModel statusModel = null;
                            boolean flag = false;
                            if (data.getOrderStatus() == 2){
                                if (data.getOutStatus() <= 2){
                                    // 这里是拉单成功的，卡商才会有扣款流水，如果拉单失败，则没有扣款流水。
                                    // 更新卡商的扣款流水订单状态
                                    MerchantBalanceDeductModel merchantBalanceDeductUpdate = TaskMethod.assembleMerchantBalanceDeductUpdateByOrderNo(data.getOrderNo(), data.getOrderStatus());
                                    int num = ComponentUtil.merchantBalanceDeductService.updateOrderStatusByOrderNo(merchantBalanceDeductUpdate);
                                    if (num > 0){
                                        flag = true;
                                    }
                                }else{
                                    flag = true;
                                }

                            }else if (data.getOrderStatus() == 4){
                                // A.更新卡商的扣款流水订单状态
                                // B.更新各自利益者的收益：卡商与利益者的利益信息

                                // 组装要更新的卡商的扣款流水订单状态
                                MerchantBalanceDeductModel merchantBalanceDeductUpdate = TaskMethod.assembleMerchantBalanceDeductUpdateByOrderNo(data.getOrderNo(), data.getOrderStatus());
                                // 组装卡商的利益
                                MerchantProfitModel merchantProfitModel = TaskMethod.assembleMerchantProfitByOrderOutAdd(data, 2);
                                // 查询卡商对应的利益者的关联关系以及利益者的收益比例
                                InterestMerchantModel interestMerchantQuery = TaskMethod.assembleInterestMerchantQuery(0,0,data.getMerchantId(),1);
                                List<InterestMerchantModel> interestMerchantList = ComponentUtil.interestMerchantService.findByCondition(interestMerchantQuery);
                                // 组装利益者的利益
                                List<InterestProfitModel> interestProfitList = TaskMethod.assembleInterestProfitListByOrderOut(interestMerchantList, data, 2);
                                // 组装代付成功数据
                                ReplacePayInfoModel replacePayInfoModel = TaskMethod.assembleReplacePayInfoAdd(0, data.getReplacePayId(), data.getOrderNo(), data.getOrderMoney());


                                // 执行事务-正式处理逻辑
                                flag = ComponentUtil.taskOrderOutService.handleSuccessOrderOut(merchantBalanceDeductUpdate, merchantProfitModel, interestProfitList, replacePayInfoModel);
                            }

                            if (flag){
                                // 成功
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);

                                // 计算第三方的放量限制
                                if (data.getReplacePayId() != null && data.getReplacePayId() > 0){
                                    // 获取此代付的放量策略
                                    ReplacePayStrategyModel replacePayStrategyQuery = TaskMethod.assembleReplacePayStrategyQuery(0, data.getReplacePayId(), 1);
                                    ReplacePayStrategyModel replacePayStrategyModel = (ReplacePayStrategyModel)ComponentUtil.replacePayStrategyService.findByObject(replacePayStrategyQuery);
                                    if (replacePayStrategyModel != null && replacePayStrategyModel.getId() != null && replacePayStrategyModel.getId() > 0){
                                        // 获取日代付成功的次数
                                        OrderOutModel orderByDayNumQuery = TaskMethod.assembleOrderOutByLimitQuery(data.getReplacePayId(), 0, 4, 0,curday, 0, 0);
                                        int dayNum = ComponentUtil.orderOutService.countOrder(orderByDayNumQuery);

                                        // 获取日代付成功的金额
                                        OrderOutModel orderByDayMoneyQuery = TaskMethod.assembleOrderOutByLimitQuery(data.getReplacePayId(), 0, 4, 0,curday, 0, 0);
                                        String dayMoney = ComponentUtil.orderOutService.sumOrderMoney(orderByDayMoneyQuery);

                                        // 获取月代付成功的金额
                                        OrderOutModel orderByMonthMoneyQuery = TaskMethod.assembleOrderOutByLimitQuery(data.getReplacePayId(), 0, 4,0, 0, curdayStart, curdayEnd);
                                        String monthMoney = ComponentUtil.orderOutService.sumOrderMoney(orderByMonthMoneyQuery);

                                        // check代付的放量限制：如果超过策略的放量，则会存redis缓存
                                        ComponentUtil.replacePayStrategyService.replacePayStrategyLimit(replacePayStrategyModel, dayNum, dayMoney, monthMoney);
                                    }


                                }
                            }else {
                                // 失败
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,null);
                            }


                            // 更新状态
                            ComponentUtil.taskOrderOutService.updateStatus(statusModel);
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);
                        }

//                log.info("----------------------------------OrderOutRunner.orderStatus()----end");
                    }catch (Exception e){
                        log.error(String.format("this OrderOutRunner.orderStatus() is error , the dataId=%s !", data.getId()));
                        e.printStackTrace();
                        // 更新此次task的状态：更新成失败：因为ERROR
                        StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                        ComponentUtil.taskOrderOutService.updateStatus(statusModel);
                    }
                }






                // end.....
            }
        }

    }




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
    class RunNotifyThread extends Thread{
        @Override
        public void run() {
            while (1==1) {
                try {
                    // 9秒钟
                    Thread.sleep(9000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

//                try{
//                    if (1==1){
//                        throw new ServiceException("1","11");
//                    }
//                }catch (Exception e){
//                    e.printStackTrace();
//                }



                // 获取已成功的订单数据，并且为同步给下游的数据
                StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 1, 0,1,0,null);
                List<OrderOutModel> synchroList = ComponentUtil.taskOrderOutService.getDataList(statusQuery);
                for (OrderOutModel data : synchroList){
                    if (data.getOrderStatus() == 3){
                        // 有质疑的订单不同步：并且直接修改成失败
                        StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,"有质疑的订单!");
                        // 更新状态
                        ComponentUtil.taskOrderOutService.updateStatus(statusModel);
                        continue;
                    }
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

//                log.info("----------------------------------OrderOutRunner.orderNotify()----end");
                    }catch (Exception e){
                        log.error(String.format("this OrderOutRunner.orderNotify() is error , the dataId=%s !", data.getId()));
                        e.printStackTrace();
                        // 更新此次task的状态：更新成失败：因为ERROR
                        StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                        ComponentUtil.taskOrderOutService.updateStatus(statusModel);
                    }
                }

            }
        }
    }



}
