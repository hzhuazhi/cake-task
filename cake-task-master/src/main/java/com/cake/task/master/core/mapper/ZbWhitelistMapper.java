package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author yoko
 * @desc 众邦白名单的Dao层
 * @create 2022-07-07 13:37
 **/
@Mapper
public interface ZbWhitelistMapper<T> extends BaseDao<T> {
}
