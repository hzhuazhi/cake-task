package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.ChannelWithdrawMapper;
import com.cake.task.master.core.service.ChannelWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 商户的提现记录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/11 19:15
 * @Version 1.0
 */
@Service
public class ChannelWithdrawServiceImpl <T> extends BaseServiceImpl<T> implements ChannelWithdrawService<T> {
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
    private ChannelWithdrawMapper channelWithdrawMapper;

    public BaseDao<T> getDao() {
        return channelWithdrawMapper;
    }
}
