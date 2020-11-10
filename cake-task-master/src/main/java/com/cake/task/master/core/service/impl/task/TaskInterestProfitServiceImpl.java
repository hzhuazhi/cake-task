package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskInterestProfitMapper;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.service.task.TaskInterestProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:利益者收益数据的Service层的实现层
 * @Author yoko
 * @Date 2020/11/10 15:55
 * @Version 1.0
 */
@Service
public class TaskInterestProfitServiceImpl<T> extends BaseServiceImpl<T> implements TaskInterestProfitService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskInterestProfitMapper taskInterestProfitMapper;


    public BaseDao<T> getDao() {
        return taskInterestProfitMapper;
    }

    @Override
    public List<InterestProfitModel> getDataList(Object obj) {
        return taskInterestProfitMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskInterestProfitMapper.updateStatus(obj);
    }
}
