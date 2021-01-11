package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.merchant.MerchantWithdrawModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商的提现记录的Dao层
 * @Author yoko
 * @Date 2021/1/11 11:24
 * @Version 1.0
 */
@Mapper
public interface MerchantWithdrawMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新卡商提现记录的订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/19 20:07
     */
    public int updateOrderStatus(MerchantWithdrawModel model);
}
