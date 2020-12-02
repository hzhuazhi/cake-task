package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.strategy.StrategyModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
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
 * @Description task:提现记录
 * @Author yoko
 * @Date 2020/11/19 19:22
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskWithdraw {

    private final static Logger log = LoggerFactory.getLogger(TaskWithdraw.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;




    /**
     * @Description: 把执行提现信息进行分配
     * <p>
     *     每5秒运行一次
     *     1.查询未分配的下发信息。
     *     2.根据各自提现类型进行分配。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 5000) // 每秒执行
    public void issueDistribution() throws Exception{
//        log.info("----------------------------------TaskWithdraw.issueDistribution()----start");

        int issueDistribution = 0;// 下发分配类型：1手动分配，2自动分配
        // 策略：检测银行卡连续给出失败次数
        StrategyModel strategyQuery = TaskMethod.assembleStrategyQuery(ServerConstant.StrategyEnum.ISSUE_DISTRIBUTION.getStgType());
        StrategyModel strategyModel = ComponentUtil.strategyService.getStrategyModel(strategyQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
        issueDistribution = strategyModel.getStgNumValue();
        // 获取未分配提现记录
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 1, 0, 0, 0,0,0,null);
        List<WithdrawModel> synchroList = ComponentUtil.taskWithdrawService.getDataList(statusQuery);
        for (WithdrawModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_WITHDRAW_ISSUE_DISTRIBUTION, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;
                    if (issueDistribution == 1){
                        // 手动分配 ：这里无需做任何操作，直接把workType=3就行
                        flag = true;

                    }else if (issueDistribution == 2){
                        // 自动分配

                        // 判断提现类型
                        if (data.getWithdrawType() == 1){
                            // 利益者提现： #目前没有业务逻辑处理
                            flag = true;
                        }else if (data.getWithdrawType() == 2){
                            // 卡商提现： #目前没有业务逻辑处理
                            flag = true;
                        }else if (data.getWithdrawType() == 3){
                            // 渠道提现
                            // 判断渠道类型
                            if (data.getChannelType() == null || data.getChannelType() == 0){
                                // 表示属于平台的利益者提现的（平台提现分两种：渠道提现，利益者提现）
                                // 这里目前不做自动分配处理
                                flag = true;
                            }else if (data.getChannelType() == 1){
                                // 渠道：代收

                                // 查询与之关联的卡商信息
                                MerchantModel merchantQuery = TaskMethod.assembleMerchantByChannelQuery(data.getChannelId());
                                List<MerchantModel> merchantList = ComponentUtil.merchantService.getMerchantByChannelBank(merchantQuery);
                                if (merchantList == null || merchantList.size() <= 0){
                                    flag = false;
                                }else {
                                    // 正式进行分配
                                    long merchantId = 0;// 卡商ID：只要此卡商与此提现的渠道有关联关系，并且渠道提现的金额小于卡商的余额，则分配这个卡商
                                    for (MerchantModel merchantModel : merchantList){
                                        // 查询提现记录中已分配给的卡商，但是没实际操作下发的卡商的金额
                                        WithdrawModel withdrawQuery = TaskMethod.assembleWithdrawQuery(0,null,null,1,0,0,0,
                                                1,merchantModel.getId(),0,0,0);
                                        String withdrawMoney = ComponentUtil.withdrawService.sumMoney(withdrawQuery);
                                        boolean flag_money = TaskMethod.checkMerchantMoney(merchantModel.getBalance(), data.getOrderMoney(), withdrawMoney);
                                        if (flag_money){
                                            merchantId = merchantModel.getId();
                                            break;
                                        }
                                    }

                                    if (merchantId > 0){
                                        // 更新提现的分配信息
                                        WithdrawModel withdrawUpdate = TaskMethod.assembleWithdrawQuery(data.getId(),null,null,0,0,0,0,
                                                1, merchantId,0,0,0);
                                        int num = ComponentUtil.withdrawService.update(withdrawUpdate);
                                        if (num > 0){
                                            flag = true;
                                        }
                                    }
                                }

                            }else if (data.getChannelType() == 2){
                                // 渠道：大包
                                // 这里目前不做自动分配处理
                                flag = true;
                            }else if (data.getChannelType() == 3){
                                // 渠道：代付
                                // 这里目前不做自动分配处理
                                flag = true;
                            }
                        }
                    }



                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 3, 0, 0,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, 0,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskWithdrawService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskWithdraw.issueDistribution()----end");
            }catch (Exception e){
                log.error(String.format("this TaskWithdraw.send() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 2, 0, 0,0,"异常失败try!");
                ComponentUtil.taskWithdrawService.updateStatus(statusModel);
            }
        }
    }




    /**
     * @Description: 把执行提现的结果同步给各个角色的提现记录
     * <p>
     *     每10每秒运行一次
     *     1.查询未跑的提现信息，并且订单状态不是初始化。
     *     2.更新各个角色的提现记录的提现订单状态。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
    @Scheduled(fixedDelay = 10000) // 每秒执行
    public void send() throws Exception{
//        log.info("----------------------------------TaskWithdraw.send()----start");

        // 获取未跑的提现记录，并且订单状态不是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 1, 0,1,0,null);
        List<WithdrawModel> synchroList = ComponentUtil.taskWithdrawService.getDataList(statusQuery);
        for (WithdrawModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_WITHDRAW_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;

                    if (data.getWithdrawType() == 1){
                        // 利益者提现
                        // #后续需求出来，补充业务逻辑
                    }else if (data.getWithdrawType() == 2){
                        // 卡商提现
                    }else if (data.getWithdrawType() == 3){
                        // 渠道提现
                        // 判断指派类型
                        if (data.getOutType() == 1){
                            // 指派给卡商进行下发的

                            // 判断此次下发，卡商是否可以得到手续费的收益：2块，5块
                            MerchantProfitModel merchantProfitAdd = TaskMethod.assembleMerchantProfitByWithdraw(data);
                            MerchantModel merchantUpdate = null;
                            if (data.getOrderStatus() == 4){
                                // 因为提现订单属于成功的，则此卡商代收收到的钱是在余额中的，由于下发成功，则余额要减此次下发的金额
                                merchantUpdate = TaskMethod.assembleMerchantByChanagerMoney(data.getMerchantId(), 2, data.getOrderMoney());
                            }

                            boolean flag_lock = false;
                            int tab = 0;// 标记：等于1表示需要更新卡商余额
                            String lockKey_merchant = "";
                            // 组装更新渠道提现记录的订单状态
                            ChannelWithdrawModel channelWithdrawUpdate = TaskMethod.assembleChannelWithdrawOrderStatusUpdate(data);
                            if (merchantUpdate != null){
                                tab = 1;
                                // 余额变更
                                // 锁住此卡商
                                lockKey_merchant = CachedKeyUtils.getCacheKeyTask(CacheKey.LOCK_MERCHANT_MONEY, data.getMerchantId());
                                boolean flagLock_merchant = ComponentUtil.redisIdService.lock(lockKey_merchant);
                                if (flagLock_merchant){
                                    flag_lock = true;
                                }
                            }
                            if (tab == 1){
                                // 表示需要更新卡商余额
                                if (flag_lock){
                                    // 只有卡商被锁住才能执行事务的逻辑：因为tab=1表示要更新卡商余额，所以一定要锁住
                                    flag = ComponentUtil.taskWithdrawService.handleChannelWithdraw(merchantProfitAdd, merchantUpdate, channelWithdrawUpdate);
                                    // 解锁
                                    ComponentUtil.redisIdService.delLock(lockKey_merchant);
                                }
                            }else {
                                // 这里表示不用更新卡商的余额：判断能进行到这里是表示卡商给渠道下发的提现是失败的，所以才进入这里
                                flag = ComponentUtil.taskWithdrawService.handleChannelWithdraw(merchantProfitAdd, merchantUpdate, channelWithdrawUpdate);
                            }




                        }else if (data.getOutType() == 2){
                            // 指派给中转站进行下发的
                        }else if (data.getOutType() == 3){
                            // 指派给平台进行下发的
                        }

                    }

                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskWithdrawService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskWithdraw.send()----end");
            }catch (Exception e){
                log.error(String.format("this TaskWithdraw.send() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,"异常失败try!");
                ComponentUtil.taskWithdrawService.updateStatus(statusModel);
            }
        }
    }
}
