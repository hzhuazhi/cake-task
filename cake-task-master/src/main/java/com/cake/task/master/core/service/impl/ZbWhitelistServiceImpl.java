package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.ZbWhitelistMapper;
import com.cake.task.master.core.service.ZbWhitelistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description 众邦白名单的Service层的实现层
 * @Author yoko
 * @Date 2020/5/18 19:08
 * @Version 1.0
 */
@Service
public class ZbWhitelistServiceImpl<T> extends BaseServiceImpl<T> implements ZbWhitelistService<T> {
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
    private ZbWhitelistMapper zbWhitelistMapper;

    public BaseDao<T> getDao() {
        return zbWhitelistMapper;
    }


}
