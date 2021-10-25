package com.cake.task.master.core.runner;

import com.cake.task.master.core.service.*;
import com.cake.task.master.core.common.redis.RedisIdService;
import com.cake.task.master.core.common.redis.RedisService;
import com.cake.task.master.core.common.utils.constant.LoadConstant;
import com.cake.task.master.core.service.task.*;
import com.cake.task.master.util.ComponentUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;


@Component
@Order(0)
public class AutowireRunner implements ApplicationRunner {
    private final static Logger log = LoggerFactory.getLogger(AutowireRunner.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;


    Thread runThread = null;

    @Autowired
    private RedisIdService redisIdService;
    @Autowired
    private RedisService redisService;
    @Resource
    private LoadConstant loadConstant;
    @Autowired
    private StrategyService strategyService;




    @Autowired
    private MobileCardService mobileCardService;

    @Autowired
    private MobileCardShortMsgService mobileCardShortMsgService;

    @Autowired
    private ShortMsgStrategyService shortMsgStrategyService;

    @Autowired
    private ShortMsgArrearsService shortMsgArrearsService;

    @Autowired
    private BankTypeService bankTypeService;

    @Autowired
    private BankService bankService;

    @Autowired
    private BankCollectionService bankCollectionService;

    @Autowired
    private BankStrategyService bankStrategyService;

    @Autowired
    private BankShortMsgStrategyService bankShortMsgStrategyService;

    @Autowired
    private BankShortMsgService bankShortMsgService;

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelBankService channelBankService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantRechargeService merchantRechargeService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderReplenishService orderReplenishService;

    @Autowired
    private StatisticsClickPayService statisticsClickPayService;

    @Autowired
    private ShortChainService shortChainService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private OrderOutService orderOutService;

    @Autowired
    private MobileCardHeartbeatService mobileCardHeartbeatService;

    @Autowired
    private MerchantChannelService merchantChannelService;

    @Autowired
    private MerchantBalanceDeductService merchantBalanceDeductService;

    @Autowired
    private InterestService interestService;

    @Autowired
    private InterestMerchantService interestMerchantService;

    @Autowired
    private InterestProfitService interestProfitService;

    @Autowired
    private MerchantProfitService merchantProfitService;

    @Autowired
    private ChannelWithdrawService channelWithdrawService;

    @Autowired
    private ChangeMoneyService changeMoneyService;

    @Autowired
    private WithdrawService withdrawService;

    @Autowired
    private MonitorBankService monitorBankService;

    @Autowired
    private MerchantWithdrawService merchantWithdrawService;

    @Autowired
    private BankLeadService bankLeadService;

    @Autowired
    private BankLeadLinkService bankLeadLinkService;

    @Autowired
    private BankLeadCollectionService bankLeadCollectionService;

    @Autowired
    private BankCollectionDayService bankCollectionDayService;

    @Autowired
    private ReplacePayService replacePayService;

    @Autowired
    private ReplacePayStrategyService replacePayStrategyService;

    @Autowired
    private ReplacePayInfoService replacePayInfoService;

    @Autowired
    private ReplacePayGainService replacePayGainService;

    @Autowired
    private ReplacePayGainResultService replacePayGainResultService;

    @Autowired
    private OrderOutPrepareService orderOutPrepareService;

    @Autowired
    private OrderOutLimitService orderOutLimitService;








    @Autowired
    private TaskMobileCardShortMsgService taskMobileCardShortMsgService;

    @Autowired
    private TaskBankShortMsgService taskBankShortMsgService;

    @Autowired
    private TaskOrderService taskOrderService;

    @Autowired
    private TaskMonitorService taskMonitorService;

    @Autowired
    private TaskOrderReplenishService taskOrderReplenishService;

    @Autowired
    private TaskShortMsgArrearsService taskShortMsgArrearsService;

    @Autowired
    private TaskMerchantRechargeService taskMerchantRechargeService;

    @Autowired
    private TaskIssueService taskIssueService;

    @Autowired
    private TaskOrderOutService taskOrderOutService;

    @Autowired
    private TaskMerchantBalanceDeductService taskMerchantBalanceDeductService;

    @Autowired
    private TaskMerchantProfitService taskMerchantProfitService;

    @Autowired
    private TaskInterestProfitService taskInterestProfitService;

    @Autowired
    private TaskChangeMoneyService taskChangeMoneyService;

    @Autowired
    private TaskChannelWithdrawService taskChannelWithdrawService;

    @Autowired
    private TaskWithdrawService taskWithdrawService;

    @Autowired
    private TaskMerchantWithdrawService taskMerchantWithdrawService;

    @Autowired
    private TaskReplacePayGainService taskReplacePayGainService;

    @Autowired
    private TaskReplacePayGainResultService taskReplacePayGainResultService;









    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("AutowireRunner ...");
        ComponentUtil.redisIdService = redisIdService;
        ComponentUtil.redisService = redisService;
        ComponentUtil.loadConstant = loadConstant;
        ComponentUtil.strategyService = strategyService;



        ComponentUtil.mobileCardService = mobileCardService;
        ComponentUtil.mobileCardShortMsgService = mobileCardShortMsgService;
        ComponentUtil.shortMsgStrategyService = shortMsgStrategyService;
        ComponentUtil.shortMsgArrearsService = shortMsgArrearsService;
        ComponentUtil.bankTypeService = bankTypeService;
        ComponentUtil.bankService = bankService;
        ComponentUtil.bankCollectionService = bankCollectionService;
        ComponentUtil.bankStrategyService = bankStrategyService;
        ComponentUtil.bankShortMsgStrategyService = bankShortMsgStrategyService;
        ComponentUtil.bankShortMsgService = bankShortMsgService;
        ComponentUtil.channelService = channelService;
        ComponentUtil.channelBankService = channelBankService;
        ComponentUtil.merchantService = merchantService;
        ComponentUtil.merchantRechargeService = merchantRechargeService;
        ComponentUtil.orderService = orderService;
        ComponentUtil.orderReplenishService = orderReplenishService;
        ComponentUtil.statisticsClickPayService = statisticsClickPayService;
        ComponentUtil.shortChainService = shortChainService;
        ComponentUtil.issueService = issueService;
        ComponentUtil.orderOutService = orderOutService;
        ComponentUtil.mobileCardHeartbeatService = mobileCardHeartbeatService;
        ComponentUtil.merchantChannelService = merchantChannelService;
        ComponentUtil.merchantBalanceDeductService = merchantBalanceDeductService;
        ComponentUtil.interestService = interestService;
        ComponentUtil.interestMerchantService = interestMerchantService;
        ComponentUtil.interestProfitService = interestProfitService;
        ComponentUtil.merchantProfitService = merchantProfitService;
        ComponentUtil.channelWithdrawService = channelWithdrawService;
        ComponentUtil.changeMoneyService = changeMoneyService;
        ComponentUtil.withdrawService = withdrawService;
        ComponentUtil.monitorBankService = monitorBankService;
        ComponentUtil.merchantWithdrawService = merchantWithdrawService;
        ComponentUtil.bankLeadService = bankLeadService;
        ComponentUtil.bankLeadLinkService = bankLeadLinkService;
        ComponentUtil.bankLeadCollectionService = bankLeadCollectionService;
        ComponentUtil.bankCollectionDayService = bankCollectionDayService;
        ComponentUtil.replacePayService = replacePayService;
        ComponentUtil.replacePayStrategyService = replacePayStrategyService;
        ComponentUtil.replacePayInfoService = replacePayInfoService;
        ComponentUtil.replacePayGainService = replacePayGainService;
        ComponentUtil.replacePayGainResultService = replacePayGainResultService;
        ComponentUtil.orderOutPrepareService = orderOutPrepareService;
        ComponentUtil.orderOutLimitService = orderOutLimitService;




        ComponentUtil.taskMobileCardShortMsgService = taskMobileCardShortMsgService;
        ComponentUtil.taskBankShortMsgService = taskBankShortMsgService;
        ComponentUtil.taskOrderService = taskOrderService;
        ComponentUtil.taskMonitorService = taskMonitorService;
        ComponentUtil.taskOrderReplenishService = taskOrderReplenishService;
        ComponentUtil.taskShortMsgArrearsService = taskShortMsgArrearsService;
        ComponentUtil.taskMerchantRechargeService = taskMerchantRechargeService;
        ComponentUtil.taskIssueService = taskIssueService;
        ComponentUtil.taskOrderOutService = taskOrderOutService;
        ComponentUtil.taskMerchantBalanceDeductService = taskMerchantBalanceDeductService;
        ComponentUtil.taskMerchantProfitService = taskMerchantProfitService;
        ComponentUtil.taskInterestProfitService = taskInterestProfitService;
        ComponentUtil.taskChangeMoneyService = taskChangeMoneyService;
        ComponentUtil.taskChannelWithdrawService = taskChannelWithdrawService;
        ComponentUtil.taskWithdrawService = taskWithdrawService;
        ComponentUtil.taskMerchantWithdrawService = taskMerchantWithdrawService;
        ComponentUtil.taskReplacePayGainService = taskReplacePayGainService;
        ComponentUtil.taskReplacePayGainResultService = taskReplacePayGainResultService;


        runThread = new RunThread();
        runThread.start();






    }

    /**
     * @author df
     * @Description: TODO(模拟请求)
     * <p>1.随机获取当日金额的任务</p>
     * <p>2.获取代码信息</p>
     * @create 20:21 2019/1/29
     **/
    class RunThread extends Thread{
        @Override
        public void run() {
            log.info("启动啦............");
        }
    }




}
