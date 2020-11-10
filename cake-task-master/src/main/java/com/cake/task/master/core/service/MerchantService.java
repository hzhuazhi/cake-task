package com.cake.task.master.core.service;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantModel;

/**
 * @Description 卡商扩充数据的Service层
 * @Author yoko
 * @Date 2020/9/8 17:06
 * @Version 1.0
 */
public interface MerchantService<T> extends BaseService<T> {
    /**
     * @Description: 卡商扩充数据的使用状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/5 19:21
     */
    public int updateUseStatus(MerchantModel model);

    /**
     * @Description: 更新卡商的余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/15 20:36
     */
    public int updateBalance(MerchantModel model);

    /**
     * @Description: 更新用户的总金额以及余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/21 18:47
     */
    public int updateMoney(MerchantModel model);

    /**
     * @Description: 扣除卡商的已跑量金额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/23 21:51
     */
    public int updateDeductMoney(MerchantModel model);

    /**
     * @Description: 保证金，预付款金额更新
     * <p>
     *     累加预付款金额
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/24 19:53
    */
    public int updateLeastMoney(MerchantModel model);
}