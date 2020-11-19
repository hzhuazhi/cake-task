package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 商户的提现记录的实体属性Bean
 * @Author yoko
 * @Date 2020/11/11 18:50
 * @Version 1.0
 */
@Mapper
public interface ChannelWithdrawMapper<T> extends BaseDao<T> {
    
    /**
     * @Description: 更新渠道提现记录的订单状态
     * @param model
     * @return 
     * @author yoko
     * @date 2020/11/19 20:07 
    */
    public int updateOrderStatus(ChannelWithdrawModel model);
    
}
