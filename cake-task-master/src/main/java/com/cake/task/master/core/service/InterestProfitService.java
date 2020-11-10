package com.cake.task.master.core.service;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.interest.InterestProfitModel;

import java.util.List;

/**
 * @Description 利益者收益数据的Service层
 * @Author yoko
 * @Date 2020/11/9 17:27
 * @Version 1.0
 */
public interface InterestProfitService<T> extends BaseService<T> {

    /**
     * @Description: 批量添加收益者的收益信息
     * @param list - 收益者的收益信息集合
     * @return
     * @author yoko
     * @date 2020/11/10 14:31
     */
    public int addBatchInterestProfit(List<InterestProfitModel> list);
}
