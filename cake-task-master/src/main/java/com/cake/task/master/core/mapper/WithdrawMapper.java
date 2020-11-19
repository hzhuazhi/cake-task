package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 提现记录的Dao层
 * @Author yoko
 * @Date 2020/11/19 17:09
 * @Version 1.0
 */
@Mapper
public interface WithdrawMapper<T> extends BaseDao<T> {
}
