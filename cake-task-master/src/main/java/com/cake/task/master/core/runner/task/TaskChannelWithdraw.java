package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.channel.ChannelModel;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Description task:商户的提现记录
 * @Author yoko
 * @Date 2020/11/19 11:46
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskChannelWithdraw {


    private final static Logger log = LoggerFactory.getLogger(TaskChannelWithdraw.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;



    /**
     * @Description: task：把数据录入到公共的提现记录表中
     * <p>
     *     每10每秒运行一次
     *     1.查询未跑的渠道提现信息。
     *     2.把提现信息录入添加到汇总的提现记录表中。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
//    @Scheduled(fixedDelay = 10000) // 每秒执行 hhyoko
    public void addWithdraw() throws Exception{
//        log.info("----------------------------------TaskChannelWithdraw.addWithdraw()----start");

        // 获取未跑的渠道提现记录，并且订单状态是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,1,null);
        List<ChannelWithdrawModel> synchroList = ComponentUtil.taskChannelWithdrawService.getDataList(statusQuery);
        for (ChannelWithdrawModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_CHANNEL_WITHDRAW_RUN, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;
                    long channelId = 0;// 平台上报的数据，没有渠道ID，所以这里赋值渠道ID
                    if (data.getChannelId() == null || data.getChannelId() <= 0){
                        if (!StringUtils.isBlank(data.getSecretKey())){
                            ChannelModel channelQuery = TaskMethod.assembleChannelQuery(0, data.getSecretKey(), 0, 0 , 0);
                            ChannelModel channelModel = (ChannelModel)ComponentUtil.channelService.findByObject(channelQuery);
                            if (channelModel != null && channelModel.getId() != null){
                                channelId = channelModel.getId();
                                // 修改此条数据的渠道ID，进行数据填充
                                ChannelWithdrawModel channelWithdrawUpdate = TaskMethod.assembleChannelWithdrawUpdateChannel(data.getId(), channelId);
                                ComponentUtil.channelWithdrawService.update(channelWithdrawUpdate);
                                // 赋值给提现汇总的纪录的渠道ID
                                data.setChannelId(channelId);
                                if (!StringUtils.isBlank(channelModel.getAlias())){
                                    data.setChannelName(channelModel.getAlias());
                                }
                            }
                        }
                    }else{
                        // 渠道ID不为空，虽然代码冗余，因为写的时间太长了，所以直接做if判断加冗余代码
                        ChannelModel channelQuery = TaskMethod.assembleChannelQuery(data.getChannelId(), null, 0, 0 , 0);
                        ChannelModel channelModel = (ChannelModel)ComponentUtil.channelService.findByObject(channelQuery);
                        if (channelModel != null && channelModel.getId() != null){
                            if (!StringUtils.isBlank(channelModel.getAlias())){
                                data.setChannelName(channelModel.getAlias());
                            }
                        }
                    }
                    // 组装添加数据录入到提现汇总表中
                    WithdrawModel withdrawModel = TaskMethod.assembleWithdrawByChannel(data, 3);
                    int num = ComponentUtil.withdrawService.add(withdrawModel);
                    if (num > 0){
                        flag = true;
                    }

                    if (flag){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else {
                        // 失败
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskChannelWithdrawService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskChannelWithdraw.addWithdraw()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChannelWithdraw.addWithdraw() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"异常失败try!");
                ComponentUtil.taskChannelWithdrawService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: 渠道提现的结果的逻辑处理
     * <p>
     *     每10每秒运行一次
     *     1.查询未跑的渠道提现信息并且订单状态不是初始化状态的纪录数据。
     *     2.判断提现信息的数据来源。
     *     3.如果来源是平台：则把提现状态结果同步给平台。
     *     <p>
     *         后续如果需求确定：数据来源是蛋糕，在继续补充蛋糕的逻辑。
     *     </p>
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(cron = "1 * * * * ?")
//    @Scheduled(fixedDelay = 10000) // 每秒执行 hhyoko
    public void orderStatus() throws Exception{
//        log.info("----------------------------------TaskChannelWithdraw.orderStatus()----start");

        // 获取未跑的渠道提现记录，并且订单状态不是初始化的
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 1, 0,1,0,null);
        List<ChannelWithdrawModel> synchroList = ComponentUtil.taskChannelWithdrawService.getDataList(statusQuery);
        for (ChannelWithdrawModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_CHANNEL_WITHDRAW_SEND, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    boolean flag = false;
                    if (data.getDataType() == 1){
                        // 来源平台，需要把提现的结果同步给平台
                        int withdrawStatus = 0;// 提现状态:1提现中，2提现失败，3提现成功
                        if (data.getOrderStatus() == 4){
                            withdrawStatus = 3;
                        }else {
                            withdrawStatus = 2;
                        }
                        ChannelWithdrawModel channelWithdrawUpdate = TaskMethod.assembleUpdateWithdrawStatus(data, withdrawStatus);
                        int num = ComponentUtil.taskChannelWithdrawService.updatePlatformWithdrawStatus(channelWithdrawUpdate);
                        if (num > 0){
                            flag = true;
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
                    ComponentUtil.taskChannelWithdrawService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskChannelWithdraw.orderStatus()----end");
            }catch (Exception e){
                log.error(String.format("this TaskChannelWithdraw.orderStatus() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,"异常失败try!");
                ComponentUtil.taskChannelWithdrawService.updateStatus(statusModel);
            }
        }
    }


}
