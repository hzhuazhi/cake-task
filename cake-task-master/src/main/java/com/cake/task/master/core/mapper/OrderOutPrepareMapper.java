package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 代付预备请求的Dao层
 * @Author yoko
 * @Date 2020/10/29 17:45
 * @Version 1.0
 */
@Mapper
public interface OrderOutPrepareMapper<T> extends BaseDao<T> {
}
