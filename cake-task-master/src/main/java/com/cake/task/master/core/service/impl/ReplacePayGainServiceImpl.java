package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.ReplacePayGainMapper;
import com.cake.task.master.core.service.ReplacePayGainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 第三方代付主动拉取结果的Service层的实现层
 * @Author yoko
 * @Date 2021/6/16 16:13
 * @Version 1.0
 */
@Service
public class ReplacePayGainServiceImpl<T> extends BaseServiceImpl<T> implements ReplacePayGainService<T> {

    private static Logger log = LoggerFactory.getLogger(ReplacePayGainServiceImpl.class);

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
    private ReplacePayGainMapper replacePayGainMapper;



    public BaseDao<T> getDao() {
        return replacePayGainMapper;
    }
}
