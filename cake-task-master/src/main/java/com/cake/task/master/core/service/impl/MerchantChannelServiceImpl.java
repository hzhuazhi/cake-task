package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.MerchantChannelMapper;
import com.cake.task.master.core.service.MerchantChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 卡商与渠道/商户的关联关系：代付时卡商与渠道的绑定关系的Service层实现层
 * @Author yoko
 * @Date 2020/10/29 16:21
 * @Version 1.0
 */
@Service
public class MerchantChannelServiceImpl<T> extends BaseServiceImpl<T> implements MerchantChannelService<T> {
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
    private MerchantChannelMapper merchantChannelMapper;

    public BaseDao<T> getDao() {
        return merchantChannelMapper;
    }
}
