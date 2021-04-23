package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 银行卡收款信息_日期分割的Dao层
 * @Author yoko
 * @Date 2021/4/21 14:50
 * @Version 1.0
 */
@Mapper
public interface BankCollectionDayMapper<T> extends BaseDao<T> {
}
