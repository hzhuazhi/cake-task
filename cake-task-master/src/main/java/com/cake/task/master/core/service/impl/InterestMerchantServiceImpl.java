package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.InterestMerchantMapper;
import com.cake.task.master.core.service.InterestMerchantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 利益分配者与卡商绑定的Service层的实现层
 * @Author yoko
 * @Date 2020/11/9 17:28
 * @Version 1.0
 */
@Service
public class InterestMerchantServiceImpl<T> extends BaseServiceImpl<T> implements InterestMerchantService<T> {
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
    private InterestMerchantMapper interestMerchantMapper;

    public BaseDao<T> getDao() {
        return interestMerchantMapper;
    }
}
