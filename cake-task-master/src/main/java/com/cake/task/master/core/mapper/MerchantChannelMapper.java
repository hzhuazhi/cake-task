package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商与渠道/商户的关联关系：代付时卡商与渠道的绑定关系的Dao层
 * @Author yoko
 * @Date 2020/10/29 16:11
 * @Version 1.0
 */
@Mapper
public interface MerchantChannelMapper<T> extends BaseDao<T> {
}
