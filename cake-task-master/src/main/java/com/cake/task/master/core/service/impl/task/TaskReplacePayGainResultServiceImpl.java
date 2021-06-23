package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskReplacePayGainResultMapper;
import com.cake.task.master.core.model.replacepay.ReplacePayGainResultModel;
import com.cake.task.master.core.service.task.TaskReplacePayGainResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:第三方代付主动拉取结果返回的订单结果的Service层的实现层
 * @Author yoko
 * @Date 2021/6/23 10:11
 * @Version 1.0
 */
@Service
public class TaskReplacePayGainResultServiceImpl <T> extends BaseServiceImpl<T> implements TaskReplacePayGainResultService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskReplacePayGainResultMapper taskReplacePayGainResultMapper;



    public BaseDao<T> getDao() {
        return taskReplacePayGainResultMapper;
    }

    @Override
    public List<ReplacePayGainResultModel> getDataList(Object obj) {
        return taskReplacePayGainResultMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskReplacePayGainResultMapper.updateStatus(obj);
    }
}
