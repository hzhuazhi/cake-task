package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.DateUtil;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.merchant.MerchantChannelModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantServiceChargeModel;
import com.cake.task.master.core.model.order.OrderOutLimitModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.order.OrderOutPrepareModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import com.cake.task.master.core.model.strategy.StrategyModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.HodgepodgeMethod;
import com.cake.task.master.util.TaskMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName:
 * @Description: task：代付预备请求
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
@Component
@EnableScheduling
public class TaskOrderOutPrepare {
    private final static Logger log = LoggerFactory.getLogger(TaskOrderOutPrepare.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: 从代付预备中进行调用三方代付接口
     * <p>
     *     每秒运行一次
     *     1.查询未跑的订单：根据策略是否需要审核的条件，生成订单延迟请求的时间，以及未运行订单不能超过1天时间的未跑订单数据。
     *     2.根据代付类型：杉德，金服来进行三方API接口调用。
     *     3.判断第三方代付API反馈的结果进行逻辑处理，失败的如黑名单错误，则添加黑名单数据，订单的拉单状态等信息更新添加到代付表中。
     *     4.更新代付预备表的运算状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 20000) // 每20秒执行
    public void sendThreeApi() throws Exception{
//        log.info("----------------------------------TaskOrderOutPrepare.sendThreeApi()----start");

        // 策略数据：代付出码规则
        int replacePayRule = 0; // 代付出码规则:1从ID从小到大，2金额从小到大
        StrategyModel strategyReplacePayRuleQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.REPLACE_PAY_RULE.getStgType());
        StrategyModel strategyReplacePayRuleModel = ComponentUtil.strategyService.getStrategyModel(strategyReplacePayRuleQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        replacePayRule = strategyReplacePayRuleModel.getStgNumValue();

        // 策略：代付预付款运算延迟时间
        StrategyModel strategyReplacePayDelayTimeNumQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.REPLACE_PAY_DELAY_TIME_NUM.getStgType());
        StrategyModel strategyReplacePayDelayTimeNumModel = ComponentUtil.strategyService.getStrategyModel(strategyReplacePayDelayTimeNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        int replacePayDelayTimeNum = strategyReplacePayDelayTimeNumModel.getStgNumValue();

        // 策略：代付预付款运算时是否先审核
        int replacePayCheck = 0; // 代付预付款运算时是否先审核：1不用审核就进行运算，2需要审核完毕才进行运算
        StrategyModel strategyReplacePayCheckQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.REPLACE_PAY_CHECK.getStgType());
        StrategyModel strategyReplacePayCheckModel = ComponentUtil.strategyService.getStrategyModel(strategyReplacePayCheckQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        replacePayCheck = strategyReplacePayCheckModel.getStgNumValue();
        int dataType = 0;
        if (replacePayCheck == 2){
            dataType = 4; // 需要审核成功
        }


        // 策略数据：代付黑名单校验规则
        int replacePayBlackListRule = 0; // 代付黑名单校验规则：1根据姓名校验，2根据银行卡卡号校验，3根据姓名+银行卡校验
        StrategyModel strategyReplacePayBlackListRuleQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.REPLACE_PAY_BLACK_LIST_RULE.getStgType());
        StrategyModel strategyReplacePayBlackListRuleModel = ComponentUtil.strategyService.getStrategyModel(strategyReplacePayBlackListRuleQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        replacePayBlackListRule = strategyReplacePayBlackListRuleModel.getStgNumValue();



        // 获取未跑的代付预备数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 1, dataType, 0, 0,0,0,String.valueOf(replacePayDelayTimeNum));
        List<OrderOutPrepareModel> synchroList = ComponentUtil.taskOrderOutPrepareService.getDataList(statusQuery);
        for (OrderOutPrepareModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ORDER_OUT_PREPARE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    OrderOutModel orderOutUpdate = null;
                    String failInfo = ""; // 失败缘由

                    // 优先check校验黑名单
                    OrderOutLimitModel orderOutLimitQuery = null;
                    if (replacePayBlackListRule == 1){
                        orderOutLimitQuery = HodgepodgeMethod.assembleOrderOutLimitQuery(data.getInAccountName(), null);
                    }else if (replacePayBlackListRule == 2){
                        orderOutLimitQuery = HodgepodgeMethod.assembleOrderOutLimitQuery(null, data.getInBankCard());
                    }else if (replacePayBlackListRule == 3){
                        orderOutLimitQuery = HodgepodgeMethod.assembleOrderOutLimitQuery(data.getInAccountName(), data.getInBankCard());
                    }

                    // check校验黑名单
                    OrderOutLimitModel orderOutLimitModel = (OrderOutLimitModel)ComponentUtil.orderOutLimitService.findByObject(orderOutLimitQuery);
                    if (orderOutLimitModel != null && orderOutLimitModel.getId() != null){
                        // 属于黑名单
                        // task数据更新成运算成功，代付订单的拉单状态修改成拉单失败

                        // 成功：因为task运算是成功的
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                        // 组装拉单失败的代付订单信息
                        failInfo = "黑名单限制,拉单失败!";
                        orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), null, null, null, 3,
                                2, failInfo);
                    }else{
                        // 不属于黑名单
                        int resourceType = data.getResourceType();// 代付资源类型：1杉德支付，2金服支付，3汇潮支付
                        boolean flag_check = true;// 所有check校验的状态

                        // 根据渠道获取关联的卡商
                        MerchantChannelModel merchantChannelQuery = HodgepodgeMethod.assembleMerchantChannelQuery(0,0,0, data.getChannelId(), 1);
                        List<MerchantChannelModel> merchantChannelList = ComponentUtil.merchantChannelService.findByCondition(merchantChannelQuery);
                        if (merchantChannelList == null || merchantChannelList.size() <= 0){
                            // 成功：因为task运算是成功的
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                            // 组装拉单失败的代付订单信息
                            failInfo = "代付订单时,商户与渠道关联关系数据为空!";
                            orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), null, null, null, 3,
                                    2, failInfo);

                            flag_check = false;
                        }

                        // 获取卡商的集合ID
                        List<Long> merchantIdList = null;
                        if (flag_check){
                            if (merchantChannelList != null && merchantChannelList.size() > 0){
                                merchantIdList = merchantChannelList.stream().map(MerchantChannelModel::getMerchantId).collect(Collectors.toList());
                            }
                        }

                        List<MerchantModel> merchantList = null;
                        if (flag_check){
                            // 根据渠道与卡商的关联关系中的卡商ID获取卡商集合
                            MerchantModel merchantModel = HodgepodgeMethod.assembleMerchantQuery(0, 0, 2, merchantIdList, data.getOrderMoney(), 1);
                            merchantList = ComponentUtil.merchantService.findByCondition(merchantModel);
                            if (merchantList == null || merchantList.size() <= 0){
                                // 成功：因为task运算是成功的
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                // 组装拉单失败的代付订单信息
                                failInfo = "代付订单时,卡商数据为空!";
                                orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), null, null, null, 3,
                                        2, failInfo);

                                flag_check = false;
                            }
                        }

                        long maxReplacePayId = 0;// 上次最大的代付ID
                        if (flag_check){
                            // 获取上次给出的代付ID
                            maxReplacePayId = HodgepodgeMethod.getMaxReplacePayRedis(resourceType);
                        }

                        List<ReplacePayModel> replacePayList = null;
                        if (flag_check){
                            // 查询代付集合数据
                            ReplacePayModel replacePayModel = HodgepodgeMethod.assembleReplacePayQuery(merchantList, data.getOrderMoney(), resourceType);
                            replacePayList = ComponentUtil.replacePayService.getReplacePayList(replacePayModel);
                            if (replacePayList == null || replacePayList.size() <= 0){
                                // 成功：因为task运算是成功的
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                // 组装拉单失败的代付订单信息
                                failInfo = "代付订单时,代付数据为空!";
                                orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), null, null, null, 3,
                                        2, failInfo);

                                flag_check = false;
                            }
                        }

                        // 查询代付订单信息
                        OrderOutModel orderOutQuery = HodgepodgeMethod.assembleOrderOutQuery(data.getOrderNo());
                        OrderOutModel orderOutModel = (OrderOutModel)ComponentUtil.orderOutService.findByObject(orderOutQuery);
                        if (orderOutModel == null){
                            // 成功：因为task运算是成功的
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,"根据订单号查询代付订单信息的数据为空!");
                            flag_check = false;
                        }


                        // 代付集合进行排序
                        List<ReplacePayModel> sortList = null;
                        if (flag_check){
                            if (replacePayRule == 1){
                                // 1从ID从小到大，
                                sortList = HodgepodgeMethod.sortReplacePayList(replacePayList, maxReplacePayId);
                            }else if (replacePayRule == 2){
                                // 2金额从小到大
                                sortList = replacePayList;
                                sortList.sort((x, y) -> Double.compare(Double.parseDouble(x.getDayMoney()), Double.parseDouble(y.getDayMoney())));
                            }
                        }

                        if (flag_check){
                            if (data.getResourceType() == 1){
                                // 杉德代付
                                // 组装请求衫德的订单信息
                                orderOutModel.setTradeTime(DateUtil.getNowLongTime());
                                // 筛选可用的代付
                                ReplacePayModel replacePayData = ComponentUtil.orderOutService.screenReplacePay(sortList, merchantList, orderOutModel);
                                if (replacePayData != null && replacePayData.getId() != null && replacePayData.getId() > 0){
                                    // 获取卡商绑定渠道的手续费
                                    MerchantServiceChargeModel merchantServiceChargeQuery = HodgepodgeMethod.assembleMerchantServiceChargeQuery(0, replacePayData.getMerchantId(), data.getChannelId(),1);
                                    MerchantServiceChargeModel merchantServiceChargeModel = ComponentUtil.merchantServiceChargeService.getMerchantServiceChargeModel(merchantServiceChargeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);

                                    // 成功：因为task运算是成功的
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                    // 组装拉单成功的代付订单信息
                                    orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), replacePayData, merchantList, merchantServiceChargeModel.getServiceCharge(), 2,
                                            1, null);
                                }else {
                                    // 成功：因为task运算是成功的
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                    // 组装拉单失败的代付订单信息
                                    failInfo = "拉单杉德失败!";
                                    orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), null, null, null, 3,
                                            2, failInfo);
                                }
                            }else if (data.getResourceType() == 2){
                                // 金服代付
                                // 组装请求金服的订单信息
                                orderOutModel.setTradeTime(DateUtil.getNowLongTime());
                                // 筛选可用的代付
                                ReplacePayModel replacePayData = ComponentUtil.orderOutService.screenReplacePayJinFu(sortList, merchantList, orderOutModel);
                                if (replacePayData != null && replacePayData.getId() != null && replacePayData.getId() > 0){
                                    // 获取卡商绑定渠道的手续费
                                    MerchantServiceChargeModel merchantServiceChargeQuery = HodgepodgeMethod.assembleMerchantServiceChargeQuery(0, replacePayData.getMerchantId(), data.getChannelId(),1);
                                    MerchantServiceChargeModel merchantServiceChargeModel = ComponentUtil.merchantServiceChargeService.getMerchantServiceChargeModel(merchantServiceChargeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);

                                    // 成功：因为task运算是成功的
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                    // 组装拉单成功的代付订单信息
                                    orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), replacePayData, merchantList, merchantServiceChargeModel.getServiceCharge(), 2,
                                            1, null);
                                }else {
                                    // 成功：因为task运算是成功的
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                    // 组装拉单失败的代付订单信息
                                    failInfo = "拉单金服失败!";
                                    orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), null, null, null, 3,
                                            2, failInfo);
                                }
                            }else if(data.getResourceType() == 3){
                                // 汇潮支付
                                // 组装请求汇潮的订单信息
                                orderOutModel.setTradeTime(DateUtil.getNowLongTime());
                                // 筛选可用的代付
                                ReplacePayModel replacePayData = ComponentUtil.orderOutService.screenReplacePayHuiChao(sortList, merchantList, orderOutModel);
                                if (replacePayData != null && replacePayData.getId() != null && replacePayData.getId() > 0){
                                    // 获取卡商绑定渠道的手续费
                                    MerchantServiceChargeModel merchantServiceChargeQuery = HodgepodgeMethod.assembleMerchantServiceChargeQuery(0, replacePayData.getMerchantId(), data.getChannelId(),1);
                                    MerchantServiceChargeModel merchantServiceChargeModel = ComponentUtil.merchantServiceChargeService.getMerchantServiceChargeModel(merchantServiceChargeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);

                                    // 成功：因为task运算是成功的
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                    // 组装拉单成功的代付订单信息
                                    orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), replacePayData, merchantList, merchantServiceChargeModel.getServiceCharge(), 2,
                                            1, null);
                                }else {
                                    // 成功：因为task运算是成功的
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 3, 0, 0,0,null);

                                    // 组装拉单失败的代付订单信息
                                    failInfo = "拉单汇潮失败!";
                                    orderOutUpdate = HodgepodgeMethod.assembleOrderOutUpdate(data.getOrderNo(), null, null, null, 3,
                                            2, failInfo);
                                }
                            }
                        }

                    }




//                    OrderOutModel orderOutUpdate = TaskMethod.assembleOrderOutUpdateBySand(data);
//                    int num = ComponentUtil.orderOutService.updateOrderStatusBySand(orderOutUpdate);
//                    if (num > 0){
//                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
//                    }else {
//                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"更新订单状态失败!");
//                    }

                    // 更新状态
                    ComponentUtil.taskReplacePayGainResultService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskOrderOutPrepare.sendThreeApi()----end");
            }catch (Exception e){
                log.error(String.format("this TaskOrderOutPrepare.sendThreeApi() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskReplacePayGainResultService.updateStatus(statusModel);
            }
        }
    }
}
