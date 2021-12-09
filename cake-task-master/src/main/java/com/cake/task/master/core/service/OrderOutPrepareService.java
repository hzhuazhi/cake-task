package com.cake.task.master.core.service;


import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayGainModel;

/**
 * @Description 代付预备请求的Service层
 * @Author yoko
 * @Date 2020/10/29 17:46
 * @Version 1.0
 */
public interface OrderOutPrepareService<T> extends BaseService<T> {


    /**
     * @Description: 处理代付预备请求之后的事物逻辑
     * <p>
     *     1.添加第三方代付主动拉取结果的数据
     *     2.更新代付订单的拉单信息。
     * </p>
     * @param replacePayGainAdd - 第三方代付主动拉取结果信息
     * @param orderOutUpdate - 要更新的代付订单信息
     * @return
     * @author yoko
     * @date 2020/11/10 19:28
     */
    public boolean handleOrderOutPrepare(ReplacePayGainModel replacePayGainAdd, OrderOutModel orderOutUpdate) throws Exception;

}
