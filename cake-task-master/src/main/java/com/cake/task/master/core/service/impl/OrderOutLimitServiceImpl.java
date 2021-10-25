package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.OrderOutLimitMapper;
import com.cake.task.master.core.service.OrderOutLimitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description 代付黑名单的Service层的实现层
 * @Author yoko
 * @Date 2020/10/29 17:47
 * @Version 1.0
 */
@Service
public class OrderOutLimitServiceImpl<T> extends BaseServiceImpl<T> implements OrderOutLimitService<T> {
    private static Logger log = LoggerFactory.getLogger(OrderOutLimitServiceImpl.class);

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
    private OrderOutLimitMapper orderOutLimitMapper;


    public BaseDao<T> getDao() {
        return orderOutLimitMapper;
    }






}
