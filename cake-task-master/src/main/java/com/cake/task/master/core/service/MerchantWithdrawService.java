package com.cake.task.master.core.service;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantWithdrawModel;

/**
 * @Description 卡商的提现记录的Service层
 * @Author yoko
 * @Date 2021/1/11 11:23
 * @Version 1.0
 */
public interface MerchantWithdrawService<T> extends BaseService<T> {

    /**
     * @Description: 更新卡商提现记录的订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/19 20:07
     */
    public int updateOrderStatus(MerchantWithdrawModel model);
}
