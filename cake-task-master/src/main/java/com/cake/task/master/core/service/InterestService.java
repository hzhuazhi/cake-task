package com.cake.task.master.core.service;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.interest.InterestModel;

/**
 * @Description 利益分配者的Service层
 * @Author yoko
 * @Date 2020/11/9 17:26
 * @Version 1.0
 */
public interface InterestService<T> extends BaseService<T> {

    /**
     * @Description: 更新卡商的使用状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/5 19:21
     */
    public int updateUseStatus(InterestModel model);

    /**
     * @Description: 更新利益分配者的余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/15 20:36
     */
    public int updateBalance(InterestModel model);

    /**
     * @Description: 更新利益分配者的总金额以及余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/21 18:47
     */
    public int updateMoney(InterestModel model);

    /**
     * @Description: 扣除利益分配者的提现金额金额
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/23 21:51
     */
    public int updateDeductMoney(InterestModel model);
}
