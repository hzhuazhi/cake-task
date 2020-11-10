package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskMerchantBalanceDeductMapper;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.service.task.TaskMerchantBalanceDeductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:卡商扣减余额流水的Service层的实现层
 * @Author yoko
 * @Date 2020/11/10 15:55
 * @Version 1.0
 */
@Service
public class TaskMerchantBalanceDeductServiceImpl<T> extends BaseServiceImpl<T> implements TaskMerchantBalanceDeductService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMerchantBalanceDeductMapper taskMerchantBalanceDeductMapper;


    public BaseDao<T> getDao() {
        return taskMerchantBalanceDeductMapper;
    }

    @Override
    public List<MerchantBalanceDeductModel> getDataList(Object obj) {
        return taskMerchantBalanceDeductMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskMerchantBalanceDeductMapper.updateStatus(obj);
    }
}
