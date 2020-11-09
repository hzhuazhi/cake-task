package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 利益分配者与卡商绑定的Dao层
 * @Author yoko
 * @Date 2020/11/9 17:23
 * @Version 1.0
 */
@Mapper
public interface InterestMerchantMapper<T> extends BaseDao<T> {
}
