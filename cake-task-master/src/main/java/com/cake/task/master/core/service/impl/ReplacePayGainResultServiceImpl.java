package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.ReplacePayGainResultMapper;
import com.cake.task.master.core.service.ReplacePayGainResultService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 第三方代付主动拉取结果返回的订单结果的Service层的实现层
 * @Author yoko
 * @Date 2021/6/16 17:14
 * @Version 1.0
 */
@Service
public class ReplacePayGainResultServiceImpl<T> extends BaseServiceImpl<T> implements ReplacePayGainResultService<T> {

    private static Logger log = LoggerFactory.getLogger(ReplacePayGainResultServiceImpl.class);

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
    private ReplacePayGainResultMapper replacePayGainResultMapper;



    public BaseDao<T> getDao() {
        return replacePayGainResultMapper;
    }
}
