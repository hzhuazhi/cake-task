package com.cake.task.master.core.service;


import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;

/**
 * @Description 卡商扣减余额流水的Service层
 * @Author yoko
 * @Date 2020/10/30 16:40
 * @Version 1.0
 */
public interface MerchantBalanceDeductService<T> extends BaseService<T> {

    /**
     * @Description: 根据订单号更新卡商扣款流水订单的订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/9 15:11
     */
    public int updateOrderStatusByOrderNo(MerchantBalanceDeductModel model);
}
