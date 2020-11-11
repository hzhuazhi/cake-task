package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 商户的提现记录的实体属性Bean
 * @Author yoko
 * @Date 2020/11/11 18:50
 * @Version 1.0
 */
@Mapper
public interface ChannelWithdrawMapper<T> extends BaseDao<T> {
}
