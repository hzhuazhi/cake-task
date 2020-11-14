package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.ChangeMoneyMapper;
import com.cake.task.master.core.service.ChangeMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 变更金额纪录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/14 14:39
 * @Version 1.0
 */
@Service
public class ChangeMoneyServiceImpl<T> extends BaseServiceImpl<T> implements ChangeMoneyService<T> {
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
    private ChangeMoneyMapper changeMoneyMapper;

    public BaseDao<T> getDao() {
        return changeMoneyMapper;
    }
}
