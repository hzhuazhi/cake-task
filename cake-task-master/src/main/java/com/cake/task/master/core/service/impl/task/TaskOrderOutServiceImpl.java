package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskOrderOutMapper;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.service.task.TaskOrderOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task：代付订单表的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 21:28
 * @Version 1.0
 */
@Service
public class TaskOrderOutServiceImpl<T> extends BaseServiceImpl<T> implements TaskOrderOutService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskOrderOutMapper taskOrderOutMapper;



    public BaseDao<T> getDao() {
        return taskOrderOutMapper;
    }

    @Override
    public List<OrderOutModel> getDataList(Object obj) {
        return taskOrderOutMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskOrderOutMapper.updateStatus(obj);
    }
}
