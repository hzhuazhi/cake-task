package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.task.TaskMerchantWithdrawMapper;
import com.cake.task.master.core.model.merchant.MerchantWithdrawModel;
import com.cake.task.master.core.service.task.TaskMerchantWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description task:卡商的提现记录的实现层
 * @Author yoko
 * @Date 2021/1/11 11:23
 * @Version 1.0
 */
@Service
public class TaskMerchantWithdrawServiceImpl <T> extends BaseServiceImpl<T> implements TaskMerchantWithdrawService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMerchantWithdrawMapper taskMerchantWithdrawMapper;


    public BaseDao<T> getDao() {
        return taskMerchantWithdrawMapper;
    }

    @Override
    public List<MerchantWithdrawModel> getDataList(Object obj) {
        return taskMerchantWithdrawMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskMerchantWithdrawMapper.updateStatus(obj);
    }
}
