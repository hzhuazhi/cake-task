package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 变更金额纪录的Dao层
 * @Author yoko
 * @Date 2020/11/14 14:20
 * @Version 1.0
 */
@Mapper
public interface ChangeMoneyMapper<T> extends BaseDao<T> {
}
