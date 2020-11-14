package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.InterestMapper;
import com.cake.task.master.core.model.interest.InterestModel;
import com.cake.task.master.core.service.InterestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 利益分配者的Service层的实现层
 * @Author yoko
 * @Date 2020/11/9 17:28
 * @Version 1.0
 */
@Service
public class InterestServiceImpl <T> extends BaseServiceImpl<T> implements InterestService<T> {
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
    private InterestMapper interestMapper;

    public BaseDao<T> getDao() {
        return interestMapper;
    }

    @Override
    public int updateUseStatus(InterestModel model) {
        return interestMapper.updateUseStatus(model);
    }

    @Override
    public int updateBalance(InterestModel model) {
        return interestMapper.updateBalance(model);
    }

    @Override
    public int updateMoney(InterestModel model) {
        return interestMapper.updateMoney(model);
    }

    @Override
    public int updateDeductMoney(InterestModel model) {
        return interestMapper.updateDeductMoney(model);
    }

    @Override
    public int updateAddOrSubtractMoney(InterestModel model) {
        return interestMapper.updateAddOrSubtractMoney(model);
    }

    @Override
    public int updateAddOrSubtractBalance(InterestModel model) {
        return interestMapper.updateAddOrSubtractBalance(model);
    }
}
