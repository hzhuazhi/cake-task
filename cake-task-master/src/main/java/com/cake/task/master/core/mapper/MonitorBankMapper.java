package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 监控银行卡的Dao层
 * @Author yoko
 * @Date 2020/12/29 16:00
 * @Version 1.0
 */
@Mapper
public interface MonitorBankMapper<T> extends BaseDao<T> {
}
