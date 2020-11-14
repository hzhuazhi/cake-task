package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskChangeMoneyMapper;
import com.cake.task.master.core.model.change.ChangeMoneyModel;
import com.cake.task.master.core.service.task.TaskChangeMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:变更金额纪录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/14 14:40
 * @Version 1.0
 */
@Service
public class TaskChangeMoneyServiceImpl<T> extends BaseServiceImpl<T> implements TaskChangeMoneyService<T> {
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
    private TaskChangeMoneyMapper taskChangeMoneyMapper;

    public BaseDao<T> getDao() {
        return taskChangeMoneyMapper;
    }

    @Override
    public List<ChangeMoneyModel> getDataList(Object obj) {
        return taskChangeMoneyMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskChangeMoneyMapper.updateStatus(obj);
    }
}
