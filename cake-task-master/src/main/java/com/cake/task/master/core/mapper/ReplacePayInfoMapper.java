package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 代付信息：这个表记录付款信息的Dao层
 * @Author yoko
 * @Date 2021/6/16 11:20
 * @Version 1.0
 */
@Mapper
public interface ReplacePayInfoMapper<T> extends BaseDao<T> {
}
