package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.BankLeadLinkMapper;
import com.cake.task.master.core.service.BankLeadLinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 主卡与设备卡关联关系的Service层的实现层
 * @Author yoko
 * @Date 2021/2/26 11:09
 * @Version 1.0
 */
@Service
public class BankLeadLinkServiceImpl<T> extends BaseServiceImpl<T> implements BankLeadLinkService<T> {
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
    private BankLeadLinkMapper bankLeadLinkMapper;

    public BaseDao<T> getDao() {
        return bankLeadLinkMapper;
    }
}
