package com.cake.task.master.core.service;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;

/**
 * @Description 商户的提现记录的Service层
 * @Author yoko
 * @Date 2020/11/11 19:14
 * @Version 1.0
 */
public interface ChannelWithdrawService<T> extends BaseService<T> {

    /**
     * @Description: 更新渠道提现记录的订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/19 20:07
     */
    public int updateOrderStatus(ChannelWithdrawModel model);
}
