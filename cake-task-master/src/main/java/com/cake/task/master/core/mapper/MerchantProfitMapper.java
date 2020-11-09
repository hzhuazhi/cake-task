package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 卡商的收益数据的Dao层
 * @Author yoko
 * @Date 2020/11/9 17:23
 * @Version 1.0
 */
@Mapper
public interface MerchantProfitMapper<T> extends BaseDao<T> {
}
