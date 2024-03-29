package com.cake.task.master.core.runner.task;

import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.model.bank.BankShortMsgModel;
import com.cake.task.master.core.model.mobilecard.MobileCardModel;
import com.cake.task.master.core.model.mobilecard.MobileCardShortMsgModel;
import com.cake.task.master.core.model.shortmsg.ShortMsgArrearsModel;
import com.cake.task.master.core.model.shortmsg.ShortMsgStrategyModel;
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

import java.util.List;

/**
 * @Description task:手机卡所有短信内容数据
 * @Author yoko
 * @Date 2020/9/13 21:37
 * @Version 1.0
 */
@Component
@EnableScheduling
public class TaskMobileCardShortMsg {

    private final static Logger log = LoggerFactory.getLogger(TaskMobileCardShortMsg.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;

    /**
     * @Description: 解析所有手机短信信息
     * <p>
     *     每秒运行一次
     *     1.拆解短信的类型：1初始化，2其它短信，3欠费短信，4银行短信
     *     2.更新类型值
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行 hhyoko
    public void analysisShortMsg() throws Exception{
//        log.info("----------------------------------TaskMobileCardShortMsg.analysisShortMsg()----start");
        // 获取短信的类型定位策略
        ShortMsgStrategyModel shortMsgStrategyModel = TaskMethod.assembleShortMsgStrategyByTypeQuery(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO);
        List<ShortMsgStrategyModel> shortMsgStrategyList = ComponentUtil.shortMsgStrategyService.getShortMsgStrategyList(shortMsgStrategyModel, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);


        // 获取手机短信数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 1, 0, 0,0,0,null);
        List<MobileCardShortMsgModel> synchroList = ComponentUtil.taskMobileCardShortMsgService.getDataList(statusQuery);
        for (MobileCardShortMsgModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MOBILE_CARD_SHORT_MSG, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    int type = 0;// 筛选之后的最终类型
                    type = TaskMethod.screenMobileCardShortMsgType(data, shortMsgStrategyList);
                    if (type == 1){
                        // 其它短信
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 2, 0,0,null);
                    }else if (type == 2){
                        // 欠费短信
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 3, 0,0,null);
                    }else if (type == 3){
                        // 银行短信
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 4, 0,0,null);
                    }
                    if (!StringUtils.isBlank(data.getPhoneNum())){
                        MobileCardModel mobileCardQuery = TaskMethod.assembleMobileCardQuery(0, data.getPhoneNum(), 0,0,0);
                        MobileCardModel mobileCardModel = ComponentUtil.mobileCardService.getMobileCard(mobileCardQuery, ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO);
                        if (mobileCardModel != null && mobileCardModel.getId() != null && mobileCardModel.getId() > 0){
                            // 更新原始数据的手机ID
                            MobileCardShortMsgModel mobileCardShortMsgUpdate = TaskMethod.assembleMobileCardShortMsgUpdateMobileCardId(data.getId(), mobileCardModel.getId());
                            ComponentUtil.mobileCardShortMsgService.update(mobileCardShortMsgUpdate);
                        }
                    }
                    // 更新状态
                    ComponentUtil.taskMobileCardShortMsgService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }


//                log.info("----------------------------------TaskMobileCardShortMsg.analysisShortMsg()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMobileCardShortMsg.analysisShortMsg() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0,2, 0,0,"异常失败try!");
                ComponentUtil.taskMobileCardShortMsgService.updateStatus(statusModel);
            }
        }

    }




    /**
     * @Description: 处理要进行运算的数据
     * <p>
     *     每秒运行一次
     *     1.查询所有要进行运算的数据
     *     2.根据数据不同类型机型不同运算
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
//    @Scheduled(fixedDelay = 1000) // 每1秒执行 hhyoko
    public void handle() throws Exception{
//        log.info("----------------------------------TaskMobileCardShortMsg.handle()----start");
        // 获取手机短信数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0,1,0,0,0,null);
        List<MobileCardShortMsgModel> synchroList = ComponentUtil.taskMobileCardShortMsgService.getDataList(statusQuery);
        for (MobileCardShortMsgModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_MOBILE_CARD_SHORT_MSG_HANDLE, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    if (data.getDataType() == 2){
                        // 其它类型短信
                        // 直接更新成成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                    }else if (data.getDataType() == 3){
                        // 欠费短信
                        if (data.getMobileCardId() != null && data.getMobileCardId() > 0){
                            // 组装添加欠费信息
                            ShortMsgArrearsModel shortMsgArrearsModel = TaskMethod.assembleShortMsgArrearsAdd(data);
                            if (shortMsgArrearsModel != null){
                                // 正式添加欠费短信
                                int num = ComponentUtil.shortMsgArrearsService.add(shortMsgArrearsModel);
                                if (num > 0){
                                    // 直接更新成成功
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                                }else {
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"添加数据响应行为0");
                                }
                            }else {
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"必要字段数据为空");
                            }
                        }else {
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"手机卡的ID为空");
                        }
                    }else if (data.getDataType() == 4){
                        // 银行短信
                        if (data.getMobileCardId() != null && data.getMobileCardId() > 0){
                            // 组装添加银行信息
                            BankShortMsgModel bankShortMsgModel = TaskMethod.assembleBankShortMsgAdd(data);
                            if (bankShortMsgModel != null){
                                // 正式添加银行短信
                                int num = ComponentUtil.bankShortMsgService.add(bankShortMsgModel);
                                if (num > 0){
                                    // 直接更新成成功
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,0,null);
                                }else {
                                    statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"添加数据响应行为0");
                                }
                            }else {
                                statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"必要字段数据为空");
                            }
                        }else {
                            statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0, 0, 0,0,"手机卡的ID为空");
                        }
                    }
                    // 更新状态
                    ComponentUtil.taskMobileCardShortMsgService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }


//                log.info("----------------------------------TaskMobileCardShortMsg.handle()----end");
            }catch (Exception e){
                log.error(String.format("this TaskMobileCardShortMsg.handle() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskMobileCardShortMsgService.updateStatus(statusModel);
            }
        }

    }



}
