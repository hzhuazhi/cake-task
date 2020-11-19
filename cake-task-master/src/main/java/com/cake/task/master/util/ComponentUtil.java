package com.cake.task.master.util;

import com.cake.task.master.core.common.redis.RedisIdService;
import com.cake.task.master.core.common.redis.RedisService;
import com.cake.task.master.core.common.utils.constant.LoadConstant;
import com.cake.task.master.core.service.*;
import com.cake.task.master.core.service.task.*;

/**
 * 工具类
 */
public class ComponentUtil {
    public static RedisIdService redisIdService;
    public static RedisService redisService;
    public static LoadConstant loadConstant;
    public static StrategyService strategyService;


    public static MobileCardService mobileCardService;
    public static MobileCardShortMsgService mobileCardShortMsgService;
    public static ShortMsgStrategyService shortMsgStrategyService;
    public static ShortMsgArrearsService shortMsgArrearsService;
    public static BankTypeService bankTypeService;
    public static BankService bankService;
    public static BankCollectionService bankCollectionService;
    public static BankStrategyService bankStrategyService;
    public static BankShortMsgStrategyService bankShortMsgStrategyService;
    public static BankShortMsgService bankShortMsgService;
    public static ChannelService channelService;
    public static ChannelBankService channelBankService;
    public static MerchantService merchantService;
    public static MerchantRechargeService merchantRechargeService;
    public static OrderService orderService;
    public static OrderReplenishService orderReplenishService;
    public static StatisticsClickPayService statisticsClickPayService;
    public static ShortChainService shortChainService;
    public static IssueService issueService;
    public static OrderOutService orderOutService;
    public static MobileCardHeartbeatService mobileCardHeartbeatService;
    public static MerchantChannelService merchantChannelService;
    public static MerchantBalanceDeductService merchantBalanceDeductService;
    public static InterestService interestService;
    public static InterestMerchantService interestMerchantService;
    public static InterestProfitService interestProfitService;
    public static MerchantProfitService merchantProfitService;
    public static ChannelWithdrawService channelWithdrawService;
    public static ChangeMoneyService changeMoneyService;




    public static TaskMobileCardShortMsgService taskMobileCardShortMsgService;
    public static TaskBankShortMsgService taskBankShortMsgService;
    public static TaskOrderService taskOrderService;
    public static TaskMonitorService taskMonitorService;
    public static TaskOrderReplenishService taskOrderReplenishService;
    public static TaskShortMsgArrearsService taskShortMsgArrearsService;
    public static TaskMerchantRechargeService taskMerchantRechargeService;
    public static TaskIssueService taskIssueService;
    public static TaskOrderOutService taskOrderOutService;
    public static TaskMerchantBalanceDeductService taskMerchantBalanceDeductService;
    public static TaskMerchantProfitService taskMerchantProfitService;
    public static TaskInterestProfitService taskInterestProfitService;
    public static TaskChangeMoneyService taskChangeMoneyService;
    public static TaskChannelWithdrawService taskChannelWithdrawService;

}
