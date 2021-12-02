package com.cake.task.master.core.service;


import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;

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




    /**
     * @Description: 筛选代付-杉德
     * @param replacePayList - 代付集合
     * @param merchantList - 卡商集合
     * @param orderOutModel - 订单信息
     * @return
     * @author yoko
     * @date 2021/6/21 11:35
     */
    public ReplacePayModel screenReplacePay(List<ReplacePayModel> replacePayList, List<MerchantModel> merchantList, OrderOutModel orderOutModel) throws Exception;


    /**
     * @Description: 筛选代付-金服
     * @param replacePayList - 代付集合
     * @param merchantList - 卡商集合
     * @param orderOutModel - 订单信息
     * @return
     * @author yoko
     * @date 2021/6/21 11:35
     */
    public ReplacePayModel screenReplacePayJinFu(List<ReplacePayModel> replacePayList, List<MerchantModel> merchantList, OrderOutModel orderOutModel) throws Exception;


    /**
     * @Description: 筛选代付-汇潮
     * @param replacePayList - 代付集合
     * @param merchantList - 卡商集合
     * @param orderOutModel - 订单信息
     * @return
     * @author yoko
     * @date 2021/6/21 11:35
     */
    public ReplacePayModel screenReplacePayHuiChao(List<ReplacePayModel> replacePayList, List<MerchantModel> merchantList, OrderOutModel orderOutModel) throws Exception;



}
