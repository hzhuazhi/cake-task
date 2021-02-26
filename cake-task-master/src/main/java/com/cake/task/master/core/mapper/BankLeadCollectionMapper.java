package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 主卡收款信息的Dao层
 * @Author yoko
 * @Date 2021/2/26 11:05
 * @Version 1.0
 */
@Mapper
public interface BankLeadCollectionMapper<T> extends BaseDao<T> {
}
