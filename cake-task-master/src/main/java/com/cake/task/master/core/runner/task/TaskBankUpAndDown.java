package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.bank.BankModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.order.OrderModel;
import com.cake.task.master.core.model.strategy.StrategyModel;
import com.cake.task.master.core.model.task.base.StatusModel;
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
 * @Description task：银行卡检测自动上线，下线
 * @Author yoko
 * @Date 2021/3/30 11:19
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskBankUpAndDown {


    private final static Logger log = LoggerFactory.getLogger(TaskBankUpAndDown.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 检测银行卡，上线
     * <p>
     *     每4秒运行一次
     *     1.检测银行卡自动上下线开关是否是打开的。
     *     2.查询当前时间最大允许上线的银行卡数量。
     *     3.如果当前银行卡数量小于最大允许的银行卡数量，则针对这个卡商查询出目前上线的最的上线的银行卡ID
     *     4.查询此卡商目前上线的最大银行卡ID之后继续查询比此银行卡ID更大的银行ID，判断此卡是否可以上线，如果可以上线则修改银行卡的状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 4000) // 每4秒执行
    public void bankUp() throws Exception{
//        log.info("----------------------------------TaskBankUpAndDown.bankUp()----start");
        // 策略：获取银行卡自动上下线银行卡的开关
        StrategyModel strategyBankUpAndDownSwitchQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_UP_AND_DOWN_SWITCH.getStgType());
        StrategyModel strategyBankUpAndDownSwitchModel = ComponentUtil.strategyService.getStrategyModel(strategyBankUpAndDownSwitchQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        int bankUpAndDownSwitch = strategyBankUpAndDownSwitchModel.getStgNumValue();

        // 策略：获取自动上线卡的上线数量
        StrategyModel strategyBankUpNumQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_UP_NUM.getStgType());
        StrategyModel strategyBankUpNumhModel = ComponentUtil.strategyService.getStrategyModel(strategyBankUpNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        // 获取当前配置的可上线卡的数量
        int bankUpNum = TaskMethod.getBankUpNum(strategyBankUpNumhModel);

        if (bankUpAndDownSwitch == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 获取卡商集合：获取代付的卡商集合
            MerchantModel merchantQuery = TaskMethod.assembleMerchantQuery(0, null,0, 1,1, null);
            List<MerchantModel> merchantList = ComponentUtil.merchantService.findByCondition(merchantQuery);
            for (MerchantModel merchantModel : merchantList){
                boolean flag = false;// 是否需要进行上线卡：true表示需要上线卡，false表示无需上线卡
                // 根据卡商ID，查询卡商此时有多少张卡处于上线状态
                BankModel bankUseNumQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                        null,null,null,2,1,0,0);
                int useNum = ComponentUtil.bankService.countUseNum(bankUseNumQuery);
                if (useNum < bankUpNum){
                    // 表示目前正在上线使用的卡数量少于现在要求的上线卡的数量：则需要添加上线卡
                    flag = true;
                }

                if (flag){
                    // 计算目前有多少张卡可以上线
                    BankModel bankCanUseNumQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                            null,null,null,2,2,4,0);
                    int canUseNum = ComponentUtil.bankService.countCanUseNum(bankCanUseNumQuery);
                    if (canUseNum > 0){
                        // 表示有可以上线的卡正在候着
                        flag = true;
                    }else {
                        flag = false;
                    }
                }

                long nexBankId = 0;// 下一个银行卡ID
                if (flag){
                    // 获取正在使用的最大银行卡ID
                    BankModel bankCanUseNumQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                            null,null,null,2,1,4, nexBankId);
                    nexBankId = ComponentUtil.bankService.getMaxBankIdByUse(bankCanUseNumQuery);
                }

                if (flag){
                    BankModel upBankModel = null;
                    if (nexBankId > 0){
                        // 表示有上线的卡

                        // 获取下一个未上线但是可以上线的银行信息
                        BankModel nextBankByNotUseQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                                null,null,null,2,2,4, nexBankId);
                        upBankModel = ComponentUtil.bankService.getNextBankByNotUse(nextBankByNotUseQuery);
                    }

                    if (upBankModel == null || upBankModel.getId() == null || upBankModel.getId() <= 0){
                        // 如果要上线的卡等于空代表：正在使用的银行卡ID已经属于最大银行卡ID了，需要进行下一轮循环了，所以直接获取最小银行卡ID的信息

                        // 获取最小的未上线但是可以上线的银行信息
                        BankModel minBankByNotUseQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                                null,null,null,2,2,4, 0);
                        upBankModel = ComponentUtil.bankService.getMinBankByNotUse(minBankByNotUseQuery);
                    }

                    if (upBankModel != null && upBankModel.getId() != null && upBankModel.getId() > 0){
                        // 锁住这个银行卡ID
                        String lockKey = CachedKeyUtils.getCacheKey(TkCacheKey.LOCK_BANK_UPDATE_USE, upBankModel.getId());
                        boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                        if (flagLock){
                            // 银行卡上线：更新银行卡使用状态
                            BankModel upModel = TaskMethod.assembleBankAllUpdate(upBankModel.getId(), 0,0,null,1,0,null,null);
                            ComponentUtil.bankService.update(upModel);
                        }
                        // 解锁
                        ComponentUtil.redisIdService.delLock(lockKey);
                    }
                }


            }

        }

    }
}
