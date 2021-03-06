package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 主卡：原始卡卡信息的Dao层
 * @Author yoko
 * @Date 2021/2/26 11:04
 * @Version 1.0
 */
@Mapper
public interface BankLeadMapper<T> extends BaseDao<T> {
}
