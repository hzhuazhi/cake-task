package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskReplacePayGainMapper;
import com.cake.task.master.core.model.replacepay.ReplacePayGainModel;
import com.cake.task.master.core.service.task.TaskReplacePayGainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:第三方代付主动拉取结果的Service层的实现层
 * @Author yoko
 * @Date 2021/6/22 17:13
 * @Version 1.0
 */
@Service
public class TaskReplacePayGainServiceImpl <T> extends BaseServiceImpl<T> implements TaskReplacePayGainService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskReplacePayGainMapper taskReplacePayGainMapper;



    public BaseDao<T> getDao() {
        return taskReplacePayGainMapper;
    }

    @Override
    public List<ReplacePayGainModel> getDataList(Object obj) {
        return taskReplacePayGainMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskReplacePayGainMapper.updateStatus(obj);
    }
}
