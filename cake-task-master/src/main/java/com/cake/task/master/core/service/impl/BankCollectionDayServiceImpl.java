package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.BankCollectionDayMapper;
import com.cake.task.master.core.service.BankCollectionDayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 银行卡收款信息_日期分割的Service的实现层
 * @Author yoko
 * @Date 2021/4/21 14:52
 * @Version 1.0
 */
@Service
public class BankCollectionDayServiceImpl<T> extends BaseServiceImpl<T> implements BankCollectionDayService<T> {
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
    private BankCollectionDayMapper bankCollectionDayMapper;

    public BaseDao<T> getDao() {
        return bankCollectionDayMapper;
    }
}
