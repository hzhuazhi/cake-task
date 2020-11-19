package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.withdraw.WithdrawModel;

import java.util.List;

/**
 * @Description task:提现记录的Service层
 * @Author yoko
 * @Date 2020/11/19 17:12
 * @Version 1.0
 */
public interface TaskWithdrawService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的提现记录信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<WithdrawModel> getDataList(Object obj);

    /**
     * @Description: 更新提现记录数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
