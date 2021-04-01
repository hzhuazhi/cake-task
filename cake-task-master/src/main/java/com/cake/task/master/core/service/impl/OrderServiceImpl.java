package com.cake.task.master.core.service.impl;
import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.OrderMapper;
import com.cake.task.master.core.model.order.OrderModel;
import com.cake.task.master.core.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 任务订单的Service层的实现层
 * @Author yoko
 * @Date 2020/5/21 19:35
 * @Version 1.0
 */
@Service
public class OrderServiceImpl<T> extends BaseServiceImpl<T> implements OrderService<T> {

    private static Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    public long TWO_HOUR = 2;

    @Autowired
    private OrderMapper orderMapper;



    public BaseDao<T> getDao() {
        return orderMapper;
    }


    @Override
    public int countOrder(OrderModel model) {
        return orderMapper.countOrder(model);
    }

    @Override
    public String sumOrderMoney(OrderModel model) {
        return orderMapper.sumOrderMoney(model);
    }

    @Override
    public int countSucOrderNum(OrderModel model) {
        return orderMapper.countSucOrderNum(model);
    }

    @Override
    public List<OrderModel> getOrderByLimitList(OrderModel model) {
        return orderMapper.getOrderByLimitList(model);
    }
}
