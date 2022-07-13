package com.cake.task.master.core.runner.task.zhongbang;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.HttpSendUtils;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.common.utils.zhongbangpay.ZhongBangApi;
import com.cake.task.master.core.common.utils.zhongbangpay.model.response.TransferResponse;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.core.model.zhongbang.ZbWhitelistModel;
import com.cake.task.master.util.ComponentUtil;
import com.cake.task.master.util.HodgepodgeMethod;
import com.cake.task.master.util.TaskMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yoko
 * @desc task:众邦白名单
 * @create 2022-07-12 14:08
 **/
@Component
@EnableScheduling
public class TaskZbWhitelist {

    private final static Logger log = LoggerFactory.getLogger(TaskZbWhitelist.class);

    @Value("${task.limit.num}")
    private int limitNum;



    /**
     * 10分钟
     */
    public long TEN_MIN = 10;


    /**
     * @Description: 众邦-白名单添加-主动查询上游
     * <p>
     *     每秒运行一次
     *     1.查询白名单属于失败的数据
     *     2.主动向众邦发起查询
     *     3.更新白名单的状态
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 120000) // 每2分钟执行
    public void activeQuery() throws Exception{
//        log.info("----------------------------------TaskZbWhitelist.activeQuery()----start");

        // 获取众邦代付信息
        ReplacePayModel replacePayQuery = TaskMethod.assembleReplacePayQuery(0,0,4,2,1);
        ReplacePayModel replacePayModel = (ReplacePayModel)ComponentUtil.replacePayService.findByObject(replacePayQuery);
        // 获取白名单添加失败的未主动查询的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 1, 0, 0, 0, 0,0,2,null);
        List<ZbWhitelistModel> synchroList = ComponentUtil.taskZbWhitelistService.getDataList(statusQuery);
        for (ZbWhitelistModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ZHONG_BANG_WHITELIST_QUERY, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;

                    // 主动查询众邦白名单结果
                    TransferResponse zbWhitelistResponse = ZhongBangApi.queryWhitelist(replacePayModel, data);
                    boolean check_flag = HodgepodgeMethod.checkZhongBangWhitelistRst(zbWhitelistResponse);
                    if (check_flag){
                        // 添加成
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 3, 0, 0, 0,3,null);
                    }else {
                        // 添加失败
                        String err_code = "";
                        String err_info = "";
                        String err_data = HodgepodgeMethod.getZhongBangWhitelistErrorInfo(zbWhitelistResponse);
                        String [] err_data_fg = null;
                        if (!StringUtils.isBlank(err_data)){
                            err_data_fg = err_data.split("#");
                            if (!StringUtils.isBlank(err_data_fg[0])){
                                err_code = err_data_fg[0];
                            }
                            if (!StringUtils.isBlank(err_data_fg[1])){
                                err_info = err_data_fg[1];
                            }
                        }
                        statusModel = TaskMethod.assembleTaskUpdateStatusByZhongBangWhitelist(data.getId(), 2, 0, 0, err_code, err_info);
                    }

                    // 更新状态
                    ComponentUtil.taskZbWhitelistService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }
//                log.info("----------------------------------TaskOrder.handleInvalid()----end");
            }catch (Exception e){
                log.error(String.format("this TaskZbWhitelist.activeQuery() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为必填项没数据
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 2, 0,0, 0,0,"异常失败try!");
                ComponentUtil.taskZbWhitelistService.updateStatus(statusModel);
            }
        }
    }



    /**
     * @Description: task：执行白名单结果数据同步
     * <p>
     *     每1每秒运行一次
     *     1.查询出失败，成功的白名单数据。（这里失败次数超过5次的也查出来）
     *     2.根据同步地址进行数据同步。
     * </p>
     * @author yoko
     * @date 2019/12/6 20:25
     */
    @Scheduled(fixedDelay = 60000) // 每10秒执行
    public void whitelistNotify() throws Exception{
//        log.info("----------------------------------TaskZbWhitelist.whitelistNotify()----start");

        // 获取已成功的订单数据，并且为同步给下游的数据
        StatusModel statusQuery = TaskMethod.assembleTaskStatusQuery(limitNum, 0, 0, 0, 0, 0,1,0,null);
        List<ZbWhitelistModel> synchroList = ComponentUtil.taskZbWhitelistService.getWhitelistNotifyList(statusQuery);
        for (ZbWhitelistModel data : synchroList){
            try{
                // 锁住这个数据流水
                String lockKey = CachedKeyUtils.getCacheKeyTask(TkCacheKey.LOCK_ZHONG_BANG_WHITELIST_NOTIFY, data.getId());
                boolean flagLock = ComponentUtil.redisIdService.lock(lockKey);
                if (flagLock){
                    StatusModel statusModel = null;
                    // 进行数据同步

                    Map<String, Object> sendMap = new HashMap<>();
                    sendMap.put("user_serial_no", data.getUserSerialNo());
                    sendMap.put("order_status", data.getOrderStatus());
                    sendMap.put("res_code", data.getResCode());
                    sendMap.put("res_explain", data.getResExplain());
                    sendMap.put("trade_time", data.getCreateTime());

                    String sendUrl = "";
                    if (!StringUtils.isBlank(data.getNotifyUrl())){
                        sendUrl = data.getNotifyUrl();
                    }
                    String resp = HttpSendUtils.sendPostAppJson(sendUrl , JSON.toJSONString(sendMap));
                    if (resp.indexOf("ok") > -1){
                        // 成功
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 3,0,null);
                    }else {
                        statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                    }


                    // 更新状态
                    ComponentUtil.taskZbWhitelistService.updateStatus(statusModel);
                    // 解锁
                    ComponentUtil.redisIdService.delLock(lockKey);
                }

//                log.info("----------------------------------TaskZbWhitelist.whitelistNotify()----end");
            }catch (Exception e){
                log.error(String.format("this TaskZbWhitelist.whitelistNotify() is error , the dataId=%s !", data.getId()));
                e.printStackTrace();
                // 更新此次task的状态：更新成失败：因为ERROR
                StatusModel statusModel = TaskMethod.assembleTaskUpdateStatus(data.getId(), 0, 0, 0, 2,0,null);
                ComponentUtil.taskZbWhitelistService.updateStatus(statusModel);
            }
        }
    }

}
