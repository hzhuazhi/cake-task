package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 利益者收益数据的Dao层
 * @Author yoko
 * @Date 2020/11/9 17:23
 * @Version 1.0
 */
@Mapper
public interface InterestProfitMapper<T> extends BaseDao<T> {

    /**
     * @Description: 批量添加收益者的收益信息
     * @param list - 收益者的收益信息集合
     * @return
     * @author yoko
     * @date 2020/11/10 14:31
    */
    public int addBatchInterestProfit(List<InterestProfitModel> list);
}
