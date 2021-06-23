package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 第三方代付主动拉取结果返回的订单结果的Dao层
 * @Author yoko
 * @Date 2021/6/16 17:12
 * @Version 1.0
 */
@Mapper
public interface ReplacePayGainResultMapper<T> extends BaseDao<T> {
}
