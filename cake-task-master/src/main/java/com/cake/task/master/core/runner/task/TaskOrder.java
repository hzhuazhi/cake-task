package com.cake.task.master.core.runner.task;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.DateUtil;
import com.cake.task.master.core.common.utils.HttpSendUtils;
import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.bank.*;
import com.cake.task.master.core.model.interest.InterestMerchantModel;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.order.OrderModel;
import com.cake.task.master.core.model.strategy.StrategyModel;
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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description task:订单
 * @Author yoko
 * @Date 2020/9/14 21:56
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskOrder {

    private final static Logger log = LoggerFactory.getLogger(TaskOrder.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 处理时效订单
     * <p>
     *     每秒运行一次
     *     1.查询订单属于初始化状态并且失效时间已经小于当前系统时间的订单
     *     2.更新订单状态：更新成失效状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 1000) // 每1秒执行
    public void handleInvalid() throws Exception{
//        log.info("----------------------------------TaskOrder.handleInvalid()----start");
        // 获取已失效订单数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 0, 0,0,1,"1");
        List<OrderModel> synchroList = ComponentUtil.taskOrderService.getDataList(statusQuery);
        for (OrderModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_INVALID, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 4, 0,2,null);
                    // 更新状态
                    ComponentUtil.taskOrderService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskOrder.handleInvalid()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrder.handleInvalid() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskOrderService.updateStatus(statusModel);
            }
        }
    }


    /**
     * @Description: 处理成功订单
     * <p>
     *     每秒运行一次
     *     1.查询成功订单。
     *     2.释放redis缓存：银行卡的金额(补单类型的订单不进行redis释放)
     *     3.计算银行卡的限制
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 1000) // 每1秒执行
    public void success() throws Exception{
//        log.info("----------------------------------TaskOrder.success()----start");
        int curday = DateUtil.getDayNumber(new Date());// 当天
        int curdayStart = DateUtil.getMinMonthDate();// 月初
        int curdayEnd = DateUtil.getMaxMonthDate();// 月末


        // 策略数据：派单是否锁金额
        int isLockMoney = 0;// 派单是否锁金额：1锁金额，2不锁金额
        StrategyModel strategyIsLockMoneyQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.IS_LOCK_MONEY.getStgType());
        StrategyModel strategyIsLockMoneyModel = ComponentUtil.strategyService.getStrategyModel(strategyIsLockMoneyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        isLockMoney = strategyIsLockMoneyModel.getStgNumValue();


        // 获取已成功订单数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,4,null);
        List<OrderModel> synchroList = ComponentUtil.taskOrderService.getDataList(statusQuery);
        for (OrderModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_SUCCESS, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 获取此银行卡的放量策略
                    BankStrategyModel bankStrategyQuery = TaskMethod.assembleBankStrategyQuery(0, data.getBankId(), 1);
                    BankStrategyModel bankStrategyModel = (BankStrategyModel)ComponentUtil.bankStrategyService.findByObject(bankStrategyQuery);
                    if (bankStrategyModel != null && bankStrategyModel.getId() != null && bankStrategyModel.getId() > 0){
                        // 计算银行卡的放量限制

                        // 获取日成功给出的次数
                        OrderModel orderByDayNumQuery = TaskMethod.assembleOrderByLimitQuery(data.getBankId(), data.getOrderType(), 4, curday, 0, 0);
                        int dayNum = ComponentUtil.orderService.countOrder(orderByDayNumQuery);

                        // 获取日成功给出的金额
                        OrderModel orderByDayMoneyQuery = TaskMethod.assembleOrderByLimitQuery(data.getBankId(), data.getOrderType(), 4, curday, 0, 0);
                        String dayMoney = ComponentUtil.orderService.sumOrderMoney(orderByDayMoneyQuery);

                        // 获取月成功给出的金额
                        OrderModel orderByMonthMoneyQuery = TaskMethod.assembleOrderByLimitQuery(data.getBankId(), data.getOrderType(), 4, 0, curdayStart, curdayEnd);
                        String monthMoney = ComponentUtil.orderService.sumOrderMoney(orderByMonthMoneyQuery);

                        // check银行卡的放量限制：如果超过策略的放量，则会存redis缓存
                        ComponentUtil.bankStrategyService.bankStrategyLimit(bankStrategyModel, data.getOrderType(), dayNum, dayMoney, monthMoney);
                    }


                    // 组装银行收款信息
                    BankCollectionModel bankCollectionAdd = TaskMethod.assembleBankCollectionAdd(data.getBankId(), data.getOrderNo(), data.getOrderMoney());

                    // 组装卡商金额更新
                    MerchantModel merchantUpdate = TaskMethod.assembleMerchantUpdateMoney(data.getMerchantId(), data.getOrderMoney());

                    // 组装卡商的利益
                    MerchantProfitModel merchantProfitModel = TaskMethod.assembleMerchantProfitByOrderAdd(data, 1);

                    // 查询卡商对应的利益者的关联关系以及利益者的收益比例
                    InterestMerchantModel interestMerchantQuery = TaskMethod.assembleInterestMerchantQuery(0,0,data.getMerchantId(),1);
                    List<InterestMerchantModel> interestMerchantList = ComponentUtil.interestMerchantService.findByCondition(interestMerchantQuery);

                    // 组装利益者的利益
                    List<InterestProfitModel> interestProfitList = TaskMethod.assembleInterestProfitListByOrder(interestMerchantList, data, 1);

                    // 查询此卡是否有主卡
                    BankLeadLinkModel bankLeadLinkQuery = TaskMethod.assembleBankLeadLinkQuery(0,0, data.getBankId(), 1);
                    BankLeadLinkModel bankLeadLinkModel = (BankLeadLinkModel)ComponentUtil.bankLeadLinkService.findByObject(bankLeadLinkQuery);

                    // 获取主卡信息
                    BankLeadModel bankLeadModel = null;
                    if (bankLeadLinkModel != null && bankLeadLinkModel.getId() != null && bankLeadLinkModel.getId() > 0){
                        BankLeadModel bankLeadQuery = TaskMethod.assembleBankLeadByIdQuery(bankLeadLinkModel.getBankLeadId());
                        bankLeadModel = (BankLeadModel)ComponentUtil.bankLeadService.findByObject(bankLeadQuery);
                    }

                    // 添加主卡的收款纪录
                    long leadBankId = 0;
                    BankLeadCollectionModel bankLeadCollectionAdd = null;
                    if (bankLeadModel != null && bankLeadModel.getId() != null && bankLeadModel.getId() > 0){
                        bankLeadCollectionAdd = TaskMethod.assembleBankLeadCollectionAdd(bankLeadModel.getId(), data.getBankId(), data.getOrderNo(), data.getOrderMoney());
                        leadBankId = bankLeadModel.getId();
                    }

                    // 添加银行卡收款信息_日期分割
                    BankCollectionDayModel bankCollectionDayAdd = TaskMethod.assembleBankCollectionDayAdd(leadBankId, data.getBankId(), data.getOrderNo(), data.getOrderMoney());

                    // 锁住这个卡商
                    String lockKey_merchantId = CachedKeyUtils.getCacheKey(CacheKey.LOCK_MERCHANT_MONEY, data.getMerchantId());
                    boolean flagLock_merchantId = ComponentUtil.redisIdService.lock(lockKey_merchantId);
                    if (flagLock_merchantId){
                        // 执行订单成功的逻辑
                        boolean flag_handle = ComponentUtil.taskOrderService.handleSuccessOrder(bankCollectionAdd, merchantUpdate, merchantProfitModel, interestProfitList, bankLeadCollectionAdd, bankCollectionDayAdd);
                        if (flag_handle){

                            // 判断是否是补单，不是补单则需要释放银行卡的挂单金额
                            if (data.getReplenishType() == 1){
                                // 派单是否锁金额，1锁金额，所以需要删除redis
                                if (isLockMoney == 1){
                                    // 删除redis：删除银行卡此金额的挂单
                                    String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.BANK_ORDER_MONEY, data.getBankId(), data.getDistributionMoney());
                                    ComponentUtil.redisService.remove(strKeyCache);
                                }
                            }

                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                        }else {
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"事务处理出错!");
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey_merchantId);
                    }else{
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"卡商被其它任务锁住!");
                    }



                    // 更新状态
                    ComponentUtil.taskOrderService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskOrder.success()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrder.success() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskOrderService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: task：执行派单成功订单的数据同步
     * <p>
     *     每1每秒运行一次
     *     1.查询出已处理的派单成功的订单数据数据。
     *     2.根据同步地址进行数据同步。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 1000) // 每秒执行
    public void orderNotify() throws Exception{
//        log.info("----------------------------------TaskOrder.orderNotify()----start");

        // 获取已成功的订单数据，并且为同步给下游的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 0, 0,1,0,null);
        List<OrderModel> synchroList = ComponentUtil.taskOrderService.getOrderNotifyList(statusQuery);
        for (OrderModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    // 进行数据同步

//                    String sendData = "total_amount=" + data.getOrderMoney() + "&" + "out_trade_no=" + data.getOutTradeNo() + "&" + "trade_status=" + 1
//                            + "&" + "trade_no=" + data.getOrderNo() + "&" + "trade_time=" + data.getCreateTime();
                    Map<String, Object> sendMap = new HashMap<>();
                    sendMap.put("total_amount", data.getOrderMoney());
                    sendMap.put("pay_amount", data.getOrderMoney());
                    sendMap.put("out_trade_no", data.getOutTradeNo());
                    sendMap.put("trade_status", 1);
                    sendMap.put("trade_no", data.getOrderNo());
                    sendMap.put("trade_time", data.getCreateTime());

                    String sendUrl = "";
                    if (!StringUtils.isBlank(data.getNotifyUrl())){
                        sendUrl = data.getNotifyUrl();
                    }else {
                        sendUrl = ComponentUtil.loadConstant.defaultNotifyUrlIn;
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
                    ComponentUtil.taskOrderService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskOrder.orderNotify()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrder.orderNotify() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                ComponentUtil.taskOrderService.updateStatus(statusModel);
            }
        }
    }

}
