package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.ReplacePayInfoMapper;
import com.cake.task.master.core.service.ReplacePayInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 代付信息：这个表记录付款信息的Service层的实现层
 * @Author yoko
 * @Date 2021/6/16 11:21
 * @Version 1.0
 */
@Service
public class ReplacePayInfoServiceImpl<T> extends BaseServiceImpl<T> implements ReplacePayInfoService<T> {

    private static Logger log = LoggerFactory.getLogger(ReplacePayInfoServiceImpl.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    public long TWO_HOUR = 2;

    @Autowired
    private ReplacePayInfoMapper replacePayInfoMapper;



    public BaseDao<T> getDao() {
        return replacePayInfoMapper;
    }
}
