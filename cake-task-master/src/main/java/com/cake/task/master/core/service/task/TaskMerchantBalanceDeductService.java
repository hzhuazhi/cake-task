package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;

import java.util.List;

/**
 * @Description task:卡商扣减余额流水的Service层
 * @Author yoko
 * @Date 2020/11/10 15:52
 * @Version 1.0
 */
public interface TaskMerchantBalanceDeductService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的卡商扣款流水信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<MerchantBalanceDeductModel> getDataList(Object obj);

    /**
     * @Description: 更新卡商扣款流水信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
