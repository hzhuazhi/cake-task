package com.cake.task.master.core.service;


import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.order.OrderOutModel;

import java.util.List;

/**
 * @Description 代付订单的Service层
 * @Author yoko
 * @Date 2020/10/29 17:46
 * @Version 1.0
 */
public interface OrderOutService<T> extends BaseService<T> {


    /**
     * @Description: 更新代付订单的订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2021/6/23 14:37
     */
    public int updateOrderStatusBySand(OrderOutModel model);


    /**
     * @Description: 计算订单数量
     * @param model
     * @return
     * @author yoko
     * @date 2021/6/23 16:53
     */
    public int countOrder(OrderOutModel model);

    /**
     * @Description: 计算订单金额
     * @param model
     * @return
     * @author yoko
     * @date 2021/6/23 16:54
     */
    public String sumOrderMoney(OrderOutModel model);
}
