package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.order.OrderOutModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 代付订单的Dao层
 * @Author yoko
 * @Date 2020/10/29 17:45
 * @Version 1.0
 */
@Mapper
public interface OrderOutMapper<T> extends BaseDao<T> {
    
    
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
     * @Description: 更新预请求时后代付订单的信息
     * @param model - 代付订单信息
     * @author: yoko
     * @date: 2021/12/2 19:45
     * @version 1.0.0
     */
    public int updateOrderOutByPrepare(OrderOutModel model);
}
