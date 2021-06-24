package com.cake.task.master.core.service;


import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.replacepay.ReplacePayStrategyModel;

/**
 * @Description 代付放量策略的Service层
 * @Author yoko
 * @Date 2021/6/15 18:18
 * @Version 1.0
 */
public interface ReplacePayStrategyService<T> extends BaseService<T> {

    /**
     * @Description: 银行卡限制
     * <p>
     *     check代付账户是否超过放量限制，如果超过放量限制，则进行redis缓存
     * </p>
     * @param replacePayStrategyModel - 代付放量策略
     * @param dayNum - 日成功次数
     * @param dayMoney - 日成功金额
     * @param monthMoney - 月成功金额
     * @return void
     * @author yoko
     * @date 2020/9/15 11:37
     */
    public void replacePayStrategyLimit(ReplacePayStrategyModel replacePayStrategyModel, int dayNum, String dayMoney, String monthMoney);
}
