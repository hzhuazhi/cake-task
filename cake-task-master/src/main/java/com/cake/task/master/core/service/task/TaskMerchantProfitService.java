package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;

import java.util.List;

/**
 * @Description task:卡商的收益数据的Service层
 * @Author yoko
 * @Date 2020/11/10 15:52
 * @Version 1.0
 */
public interface TaskMerchantProfitService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的卡商的收益信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<MerchantProfitModel> getDataList(Object obj);

    /**
     * @Description: 更新卡商的收益信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
