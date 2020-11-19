package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
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

    /**
     * @Description: 处理提现记录的逻辑
     * <p>
     *     1.添加卡商下发的收益记录，添加的数据是2块，5块这种。
     *     2.更新卡商的余额。
     *     3.更新渠道提现记录的订单状态。
     * </p>
     * @param merchantProfitAdd - 卡商收益信息
     * @param merchantUpdate - 要更新的卡商余额
     * @param channelWithdrawUpdate - 要更新的渠道提现订单状态
     * @return
     * @author yoko
     * @date 2020/11/10 19:28
     */
    public boolean handleChannelWithdraw(MerchantProfitModel merchantProfitAdd, MerchantModel merchantUpdate, ChannelWithdrawModel channelWithdrawUpdate) throws Exception;
}
