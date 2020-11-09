package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.MerchantBalanceDeductMapper;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.service.MerchantBalanceDeductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 卡商扣减余额流水的Service层的实现层
 * @Author yoko
 * @Date 2020/10/30 16:41
 * @Version 1.0
 */
@Service
public class MerchantBalanceDeductServiceImpl<T> extends BaseServiceImpl<T> implements MerchantBalanceDeductService<T> {
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
    private MerchantBalanceDeductMapper merchantBalanceDeductMapper;

    public BaseDao<T> getDao() {
        return merchantBalanceDeductMapper;
    }

    @Override
    public int updateOrderStatusByOrderNo(MerchantBalanceDeductModel model) {
        return merchantBalanceDeductMapper.updateOrderStatusByOrderNo(model);
    }
}
