package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.bank.BankCollectionModel;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.order.OrderModel;

import java.util.List;

/**
 * @Description task:订单的Service层
 * @Author yoko
 * @Date 2020/9/14 22:14
 * @Version 1.0
 */
public interface TaskOrderService<T> extends BaseService<T> {

    /**
     * @Description: 查询未跑的订单信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<OrderModel> getDataList(Object obj);

    /**
     * @Description: 更新订单信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

    /**
     * @Description: 获取要同步给下游的订单数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/8 17:38
     */
    public List<OrderModel> getOrderNotifyList(Object obj);


    /**
     * @Description: 处理代收订单的逻辑
     * <p>
     *     1.添加银行卡成功收款信息。
     *     2.更新卡商余额信息。
     *     3.添加卡商收益信息。
     *     4.批量添加利益者收益信息。
     * </p>
     * @param bankCollectionModel - 银行卡成功收款信息
     * @param merchantUpdateMoney - 要更新的卡商余额信息
     * @param merchantProfitModel - 卡商收益信息
     * @param interestProfitList - 利益者收益信息集合
     * @return
     * @author yoko
     * @date 2020/11/10 19:28
     */
    public boolean handleSuccessOrder(BankCollectionModel bankCollectionModel, MerchantModel merchantUpdateMoney, MerchantProfitModel merchantProfitModel, List<InterestProfitModel> interestProfitList) throws Exception;
}
