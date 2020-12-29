package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.MonitorBankMapper;
import com.cake.task.master.core.service.MonitorBankService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 监控银行卡的Service层
 * @Author yoko
 * @Date 2020/12/29 16:02
 * @Version 1.0
 */
@Service
public class MonitorBankServiceImpl<T> extends BaseServiceImpl<T> implements MonitorBankService<T> {

    private final static Logger log = LoggerFactory.getLogger(MonitorBankServiceImpl.class);

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
    private MonitorBankMapper monitorBankMapper;

    public BaseDao<T> getDao() {
        return monitorBankMapper;
    }
}
