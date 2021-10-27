package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskOrderOutPrepareMapper;
import com.cake.task.master.core.model.order.OrderOutPrepareModel;
import com.cake.task.master.core.service.task.TaskOrderOutPrepareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:代付预备请求的Service层的实现层
 * @Author yoko
 * @Date 2021/6/23 10:11
 * @Version 1.0
 */
@Service
public class TaskOrderOutPrepareServiceImpl<T> extends BaseServiceImpl<T> implements TaskOrderOutPrepareService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskOrderOutPrepareMapper taskOrderOutPrepareMapper;



    public BaseDao<T> getDao() {
        return taskOrderOutPrepareMapper;
    }

    @Override
    public List<OrderOutPrepareModel> getDataList(Object obj) {
        return taskOrderOutPrepareMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskOrderOutPrepareMapper.updateStatus(obj);
    }
}
