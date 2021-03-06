package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.WithdrawMapper;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
import com.cake.task.master.core.service.WithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 提现记录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/19 17:14
 * @Version 1.0
 */
@Service
public class WithdrawServiceImpl <T> extends BaseServiceImpl<T> implements WithdrawService<T> {
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
    private WithdrawMapper withdrawMapper;

    public BaseDao<T> getDao() {
        return withdrawMapper;
    }

    @Override
    public String sumMoney(WithdrawModel model) {
        return withdrawMapper.sumMoney(model);
    }
}
