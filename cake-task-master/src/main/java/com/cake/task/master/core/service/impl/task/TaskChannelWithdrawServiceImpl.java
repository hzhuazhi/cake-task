package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskChannelWithdrawMapper;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.service.task.TaskChannelWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:商户的提现记录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/19 14:20
 * @Version 1.0
 */
@Service
public class TaskChannelWithdrawServiceImpl<T> extends BaseServiceImpl<T> implements TaskChannelWithdrawService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskChannelWithdrawMapper taskChannelWithdrawMapper;


    public BaseDao<T> getDao() {
        return taskChannelWithdrawMapper;
    }

    @Override
    public List<ChannelWithdrawModel> getDataList(Object obj) {
        return taskChannelWithdrawMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskChannelWithdrawMapper.updateStatus(obj);
    }

    @Override
    public int updatePlatformWithdrawStatus(ChannelWithdrawModel model) {
        return taskChannelWithdrawMapper.updatePlatformWithdrawStatus(model);
    }
}
