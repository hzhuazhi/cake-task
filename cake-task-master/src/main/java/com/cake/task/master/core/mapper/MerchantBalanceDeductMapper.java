package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商扣减余额流水的Dao层
 * @Author yoko
 * @Date 2020/10/30 16:36
 * @Version 1.0
 */
@Mapper
public interface MerchantBalanceDeductMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据订单号更新卡商扣款流水订单的订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/9 15:11
    */
    public int updateOrderStatusByOrderNo(MerchantBalanceDeductModel model);
}
