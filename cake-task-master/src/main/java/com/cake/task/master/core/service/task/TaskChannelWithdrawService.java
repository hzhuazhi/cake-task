package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;

import java.util.List;

/**
 * @Description task:商户的提现记录的Service层
 * @Author yoko
 * @Date 2020/11/19 14:19
 * @Version 1.0
 */
public interface TaskChannelWithdrawService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的商户的提现记录信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ChannelWithdrawModel> getDataList(Object obj);

    /**
     * @Description: 更新商户的提现记录信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

    /**
     * @Description: 更新平台的提现状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/19 19:02
     */
    public int updatePlatformWithdrawStatus(ChannelWithdrawModel model);
}
