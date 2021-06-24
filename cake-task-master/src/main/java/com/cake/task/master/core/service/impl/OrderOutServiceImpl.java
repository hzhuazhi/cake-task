package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.OrderOutMapper;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.service.OrderOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description 代付订单的Service层的实现层
 * @Author yoko
 * @Date 2020/10/29 17:47
 * @Version 1.0
 */
@Service
public class OrderOutServiceImpl<T> extends BaseServiceImpl<T> implements OrderOutService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 3分钟
     */
    public long THREE_MIN = 180;

    /**
     * 11分钟.
     */
    public long ELEVEN_MIN = 660;

    public long TWO_HOUR = 2;


    @Autowired
    private OrderOutMapper orderOutMapper;



    public BaseDao<T> getDao() {
        return orderOutMapper;
    }


    @Override
    public int updateOrderStatusBySand(OrderOutModel model) {
        return orderOutMapper.updateOrderStatusBySand(model);
    }

    @Override
    public int countOrder(OrderOutModel model) {
        return orderOutMapper.countOrder(model);
    }

    @Override
    public String sumOrderMoney(OrderOutModel model) {
        return orderOutMapper.sumOrderMoney(model);
    }
}
