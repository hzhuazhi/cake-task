package com.cake.task.master.core.service;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.withdraw.WithdrawModel;

/**
 * @Description 提现记录的Service层
 * @Author yoko
 * @Date 2020/11/19 17:12
 * @Version 1.0
 */
public interface WithdrawService<T> extends BaseService<T> {

    /**
     * @Description: 根据条件查询下发的金额总和
     * @param model
     * @return
     * @author yoko
     * @date 2020/12/2 16:28
     */
    public String sumMoney(WithdrawModel model);
}
