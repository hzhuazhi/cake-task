package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.DateUtil;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.bank.BankModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.order.OrderModel;
import com.cake.task.master.core.model.strategy.StrategyModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.TaskMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
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
//    @Scheduled(fixedDelay = 4000) // 每4秒执行 hhyoko
    public void bankUp() throws Exception{
//        log.info("----------------------------------TaskBankUpAndDown.bankUp()----start");
        int curday = DateUtil.getDayNumber(new Date());
        String suffix = String.valueOf(curday);
        // 策略：获取自动上下线银行卡开关
        StrategyModel strategyBankUpAndDownSwitchQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_UP_AND_DOWN_SWITCH.getStgType());
        StrategyModel strategyBankUpAndDownSwitchModel = ComponentUtil.strategyService.getStrategyModel(strategyBankUpAndDownSwitchQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        int bankUpAndDownSwitch = strategyBankUpAndDownSwitchModel.getStgNumValue();

        // 策略：获取自动上线卡的上线数量
        StrategyModel strategyBankUpNumQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_UP_NUM.getStgType());
        StrategyModel strategyBankUpNumModel = ComponentUtil.strategyService.getStrategyModel(strategyBankUpNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        // 获取当前配置的可上线卡的数量
        int bankUpNum = TaskMethod.getBankUpNum(strategyBankUpNumModel);


        // 策略：获取自动上线卡的规则
        StrategyModel strategyBankUpRuleQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_UP_RULE.getStgType());
        StrategyModel strategyBankUpRuleModel = ComponentUtil.strategyService.getStrategyModel(strategyBankUpRuleQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        int bankUpRule = strategyBankUpRuleModel.getStgNumValue();// 自动上线卡的规则：int值等于1则按照银行卡ID从小到大，int值等于2则按照金额最小的优先上线

        if (bankUpAndDownSwitch == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 获取卡商集合：获取代付的卡商集合
            MerchantModel merchantQuery = TaskMethod.assembleMerchantQuery(0, null,0, 1,1, null);
            List<MerchantModel> merchantList = ComponentUtil.merchantService.findByCondition(merchantQuery);
            for (MerchantModel merchantModel : merchantList){
                boolean flag = false;// 是否需要进行上线卡：true表示需要上线卡，false表示无需上线卡
                // 根据卡商ID，查询卡商此时有多少张卡处于上线状态
                BankModel bankUseNumQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                        null,null,null,2,1,4,0, suffix);
                int useNum = ComponentUtil.bankService.countUseNum(bankUseNumQuery);
                if (useNum < bankUpNum){
                    // 表示目前正在上线使用的卡数量少于现在要求的上线卡的数量：则需要添加上线卡
                    flag = true;
                }

                if (flag){
                    // 计算目前有多少张卡可以上线
                    BankModel bankCanUseNumQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                            null,null,null,2,2,4,0, suffix);
                    int canUseNum = ComponentUtil.bankService.countCanUseNum(bankCanUseNumQuery);
                    if (canUseNum > 0){
                        // 表示有可以上线的卡正在候着
                        flag = true;
                    }else {
                        flag = false;
                    }
                }


                BankModel upBankModel = null;// 要更新上线的的卡信息

                List<BankModel> sortList = null;// 排序的银行卡集合：按照银行卡ID从小到大、按照成功金额从小到大
                if (flag){
                    // 组装查询可以上线的银行卡的查询条件
                    BankModel bankCanUseQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                            null,null,null,2,2,4,0, suffix);
                    if (bankUpRule == 1){
                        // 根据银行卡ID从小到大上线
                        long maxBankId = 0;// 正在使用的最大银行卡ID
                        // 查询可用的银行卡信息集合-根据银行卡ID升序排列
                        List<BankModel> bankList = ComponentUtil.bankService.getBankListByOrderId(bankCanUseQuery);
                        if (bankList != null && bankList.size() > 0){
                            // 获取正在使用的最大银行卡ID
                            BankModel bankCanUseMaxQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                                    null,null,null,2,1,4, 0, suffix);
                            maxBankId = ComponentUtil.bankService.getMaxBankIdByUse(bankCanUseMaxQuery);
                            sortList = TaskMethod.sortBankList(bankList, maxBankId);
                        }

                    }else if (bankUpRule == 2){
                        // 根据银行卡收款金额最小的优先上线
                        sortList = ComponentUtil.bankService.getBankListByDayMoney(bankCanUseQuery);
                    }

                    if (sortList != null && sortList.size() > 0){
                        // check筛选可用的银行卡
                        for (BankModel checkBankModel : sortList){
                            boolean checkBank = TaskMethod.checkBankLimit(checkBankModel, 2);
                            if (checkBank){
                                upBankModel = new BankModel();
                                upBankModel = checkBankModel;
                                break;
                            }
                        }
                    }
                }

                if (flag){
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


                /*long nexBankId = 0;// 下一个银行卡ID
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
                }*/
            }

        }

        //        log.info("----------------------------------TaskBankUpAndDown.bankUp()----start");

    }





    /**
     * @Description: 检测银行卡，下线线
     * <p>
     *     每9秒运行一次
     *     1.检测银行卡自动上下线开关是否是打开的。
     *     2.检查卡商目前是否有可上线的卡：如果没有可上线的卡，则无需下线卡。
     *     3.循环上线的卡，check是否订单已经到达策略配置的成功订单的数量。
     *     4.
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 9000) // 每9秒执行 hhyoko
    public void bankDown() throws Exception{
//        log.info("----------------------------------TaskBankUpAndDown.bankDown()----start");

        int curday = DateUtil.getDayNumber(new Date());
        String suffix = String.valueOf(curday);

        // 策略：获取自动上下线银行卡开关
        StrategyModel strategyBankUpAndDownSwitchQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_UP_AND_DOWN_SWITCH.getStgType());
        StrategyModel strategyBankUpAndDownSwitchModel = ComponentUtil.strategyService.getStrategyModel(strategyBankUpAndDownSwitchQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        int bankUpAndDownSwitch = strategyBankUpAndDownSwitchModel.getStgNumValue();

        // 策略：自动下线卡成功订单数
        StrategyModel strategyBankDownBySucNumQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_DOWN_BY_SUC_NUM.getStgType());
        StrategyModel strategyBankDownBySucNumModel = ComponentUtil.strategyService.getStrategyModel(strategyBankDownBySucNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        // 自动下线卡成功订单的数量
        int bankDownBySucNum = strategyBankDownBySucNumModel.getStgNumValue();


        // 策略：自动下线卡不计渠道失败
        StrategyModel strategyBankDownByNotChannelQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_DOWN_BY_NOT_CHANNEL.getStgType());
        StrategyModel strategyBankDownByNotChannelModel = ComponentUtil.strategyService.getStrategyModel(strategyBankDownByNotChannelQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        // 换算出不计渠道失败的渠道
        List<Long> channelIdList = TaskMethod.getChannelByBankDownByNotChannel(strategyBankDownByNotChannelModel.getStgBigValue());// 不在计算失败的渠道ID集合



        // 策略：自动下线卡失败订单数
        StrategyModel strategyBankDownByFailNumQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_DOWN_BY_FAIL_NUM.getStgType());
        StrategyModel strategyBankDownByFailNumModel = ComponentUtil.strategyService.getStrategyModel(strategyBankDownByFailNumQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        // 自动下线卡失败订单的数
        int bankDownByFailNum = strategyBankDownByFailNumModel.getStgNumValue();

        // 策略：自动下线卡延迟分钟后展现
        StrategyModel strategyBankDownShowTimeQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.BANK_DOWN_SHOW_TIME.getStgType());
        StrategyModel strategyBankDownShowTimeModel = ComponentUtil.strategyService.getStrategyModel(strategyBankDownShowTimeQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        // 自动下线卡延迟分钟后展现
        int bankDownShowTime = strategyBankDownShowTimeModel.getStgNumValue();

        if (bankUpAndDownSwitch == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
            // 获取卡商集合：获取代付的卡商集合
            MerchantModel merchantQuery = TaskMethod.assembleMerchantQuery(0, null,0, 1,1, null);
            List<MerchantModel> merchantList = ComponentUtil.merchantService.findByCondition(merchantQuery);
            for (MerchantModel merchantModel : merchantList){
                boolean flag = false;// 是否需要进行下线卡：true表示需要下线卡，false表示无需下线卡
                // 计算目前有多少张卡可以上线
                BankModel bankCanUseNumQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                        null,null,null,2,2,4,0, suffix);
                int canUseNum = ComponentUtil.bankService.countCanUseNum(bankCanUseNumQuery);
                if (canUseNum > 0){
                    // 表示有可以上线的卡正在候着
                    flag = true;
                }else {
                    flag = false;
                }

                List<BankModel> bankList = null;
                if (flag){
                    // 查询已上线的所有卡
                    BankModel bankQuery = TaskMethod.assembleBankQuery(0, 0, 0, merchantModel.getId(),
                            null,null,null,2,1,4,0, suffix);
                    bankList = ComponentUtil.bankService.findByCondition(bankQuery);
                    if (bankList == null || bankList.size() == 0){
                        // 这里表示没有已上线的卡，所以不存在下线
                        flag = false;
                    }
                }

                if (flag){
                    // 循环遍历银行卡，check银行卡的成功订单数超过策略设定的成功数了
                    for (BankModel bankData : bankList){
                        String checkChange = "";
                        int changeStatus = 0;
                        // 查询银行卡成功订单数量
                        OrderModel sucNumOrderQuery = TaskMethod.assembleOrderByDownBankQuery(bankData.getBankCard(), 2,4,null,0, null);
                        int sumNum = ComponentUtil.orderService.countSucOrderNum(sucNumOrderQuery);
                        if (sumNum >= bankDownBySucNum){
                            // 表示已经超过策略中的成功订单数量了：需要下线此卡
                            changeStatus = 2;
                            checkChange = "《请重新换卡》 "+ "，检测时间：" + DateUtil.getNowPlusTime() + "，银行卡号：" + bankData.getBankCard() + "，已成功订单数：" + sumNum + "，已经超过设定允许成功订单数：" + bankDownBySucNum;
                            flag = true;
                        }else {
                            // 查询在下线检测时间范围内的订单集合-无订单条数限制
                            OrderModel orderQuery = TaskMethod.assembleOrderByDownBankQuery(bankData.getBankCard(), 2,0,"1", 0, channelIdList);
                            List<OrderModel> orderList = ComponentUtil.orderService.findByCondition(orderQuery);
                            if (orderList != null && orderList.size() >0 && orderList.size() >= bankDownByFailNum){
                                flag = TaskMethod.checkFailOrder(orderList, 0);
                                if (flag){
                                    // 表示新卡，并且已经连续超过策略中的失败订单数量了：需要下线此卡
                                    changeStatus = 3;
                                    checkChange = "《该卡为新卡,请确定卡号是否正确》"+"，检测时间：" + DateUtil.getNowPlusTime() + "，银行卡号：" + bankData.getBankCard() +
                                            "，连续失败订单数：" + orderList.size() + "，已经超过设定允许失败订单数：" + bankDownByFailNum;
                                }
                            }else {
                                // 在最上层因为只要有待上线的卡flag=true了，所以这里需要更改成false
                                flag = false;
                            }

                            if (!flag){
                                // 这里注意：因为新卡连续监测没有达到要下线的要求，所以要继续check

                                // 查询在下线检测时间范围内的订单集合-订单条数限制
                                OrderModel orderByDownTimeQuery = TaskMethod.assembleOrderByDownBankQuery(bankData.getBankCard(), 2,0,"1", bankDownByFailNum, channelIdList);
                                List<OrderModel> orderLimitList = ComponentUtil.orderService.getOrderByLimitList(orderByDownTimeQuery);
                                if (orderList != null && orderList.size() >0 && orderList.size() >= bankDownByFailNum){
                                    flag = TaskMethod.checkFailOrder(orderLimitList, bankDownByFailNum);
                                    if (flag){
                                        // 表示已经连续超过策略中的失败订单数量了：需要下线此卡
                                        changeStatus = 2;
                                        checkChange = "《请重新换卡》" + "，检测时间：" + DateUtil.getNowPlusTime() + "，银行卡号：" + bankData.getBankCard() + "，连续失败订单数：" + bankDownByFailNum + "，已经超过设定允许失败订单数：" + bankDownByFailNum;
                                    }
                                }
                            }

                        }

                        if (!flag){
                            // check银行的日限、月限
                            boolean checkBank = TaskMethod.checkBankLimit(bankData, 2);
                            if (!checkBank){
                                // 表示已经超过放量策略中的日收款额度，余额收款额度，日收款次数的限制了：需要下线此卡
                                changeStatus = 2;
                                checkChange = "《请重新换卡，该卡已超过放量额度策略：日额度、月额度、日收款次数中的一项》 "+ "，检测时间：" + DateUtil.getNowPlusTime() + "，银行卡号：" + bankData.getBankCard();
                                flag = true;
                            }
                        }



                        if (flag){
                            // 锁住这个银行卡ID
                            String lockKey = CachedKeyUtils.getCacheKey(TkCacheKey.LOCK_BANK_UPDATE_USE, bankData.getId());
                            boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                            if (flagLock){
                                // 银行卡下线：更新银行卡使用状态、换卡状态
                                String changeTime = DateUtil.addDateMinute(bankDownShowTime);
                                BankModel upModel = TaskMethod.assembleBankAllUpdate(bankData.getId(), 0,0,null,2, changeStatus, changeTime, checkChange);
                                ComponentUtil.bankService.update(upModel);
                            }
                            // 解锁
                            ComponentUtil.redisIdService.delLock(lockKey);

                        }
                    }
                }

            }

        }

        //        log.info("----------------------------------TaskBankUpAndDown.bankDown()----start");

    }


}
