package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.order.OrderOutModel;

import java.util.List;

/**
 * @Description task：代付订单表的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 21:27
 * @Version 1.0
 */
public interface TaskOrderOutService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的代付订单信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<OrderOutModel> getDataList(Object obj);

    /**
     * @Description: 更新代付订单信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

    /**
     * @Description: 处理代付订单的逻辑
     * <p>
     *     1.更新卡商扣款流水订单状态。
     *     2.添加卡商收益信息。
     *     3.批量添加利益者收益信息。
     * </p>
     * @param merchantBalanceDeductUpdate - 卡商扣款流水的订单信息
     * @param merchantProfitModel - 卡商收益信息
     * @param interestProfitList - 利益者收益信息集合
     * @return
     * @author yoko
     * @date 2020/11/10 19:28
    */
    public boolean handleSuccessOrderOut(MerchantBalanceDeductModel merchantBalanceDeductUpdate, MerchantProfitModel merchantProfitModel, List<InterestProfitModel> interestProfitList) throws Exception;
}
