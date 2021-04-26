package com.cake.task.master.core.service.impl;
import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.BankMapper;
import com.cake.task.master.core.model.bank.BankModel;
import com.cake.task.master.core.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Description 银行的Service层的实现层
 * @Author yoko
 * @Date 2020/5/18 19:08
 * @Version 1.0
 */
@Service
public class BankServiceImpl<T> extends BaseServiceImpl<T> implements BankService<T> {
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
    private BankMapper bankMapper;

    public BaseDao<T> getDao() {
        return bankMapper;
    }


    @Override
    public List<BankModel> getBankAndStrategy(BankModel model) {
        return bankMapper.getBankAndStrategy(model);
    }

    @Override
    public int countUseNum(BankModel model) {
        return bankMapper.countUseNum(model);
    }

    @Override
    public int countCanUseNum(BankModel model) {
        return bankMapper.countCanUseNum(model);
    }

    @Override
    public long getMaxBankIdByUse(BankModel model) {
        return bankMapper.getMaxBankIdByUse(model);
    }

    @Override
    public BankModel getNextBankByNotUse(BankModel model) {
        return bankMapper.getNextBankByNotUse(model);
    }

    @Override
    public BankModel getMinBankByNotUse(BankModel model) {
        return bankMapper.getMinBankByNotUse(model);
    }

    @Override
    public List<BankModel> getBankListByOrderId(BankModel model) {
        return bankMapper.getBankListByOrderId(model);
    }

    @Override
    public List<BankModel> getBankListByDayMoney(BankModel model) {
        return bankMapper.getBankListByDayMoney(model);
    }
}
