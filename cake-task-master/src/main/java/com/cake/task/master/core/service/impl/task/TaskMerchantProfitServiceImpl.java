package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskMerchantProfitMapper;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.service.task.TaskMerchantProfitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:卡商的收益数据的Service层的实现层
 * @Author yoko
 * @Date 2020/11/10 15:55
 * @Version 1.0
 */
@Service
public class TaskMerchantProfitServiceImpl<T> extends BaseServiceImpl<T> implements TaskMerchantProfitService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMerchantProfitMapper taskMerchantProfitMapper;


    public BaseDao<T> getDao() {
        return taskMerchantProfitMapper;
    }

    @Override
    public List<MerchantProfitModel> getDataList(Object obj) {
        return taskMerchantProfitMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskMerchantProfitMapper.updateStatus(obj);
    }
}
