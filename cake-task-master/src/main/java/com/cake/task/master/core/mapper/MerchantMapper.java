package com.cake.task.master.core.mapper;
import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.merchant.MerchantModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 卡商扩充数据的Dao层
 * @Author yoko
 * @Date 2020/9/8 17:05
 * @Version 1.0
 */
@Mapper
public interface MerchantMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新卡商的使用状态
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


    /**
     * @Description: 更新卡商的收益
     * <p>
     *     卡商收益累加则更新total_profit、profit这两个字段；
     *     卡商金额扣减则更新profit字段
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/10 16:59
    */
    public int updateProfit(MerchantModel model);

    /**
     * @Description: 加减卡商余额
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/14 15:01
    */
    public int updateAddOrSubtractBalance(MerchantModel model);

    /**
     * @Description: 加减卡商收益
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/14 15:01
     */
    public int updateAddOrSubtractProfit(MerchantModel model);

    /**
     * @Description: 根据渠道与银行卡的关联关系查询卡商信息
     * @param model
     * @return
     * @author yoko
     * @date 2020/12/2 15:47
    */
    public List<MerchantModel> getMerchantByChannelBank(MerchantModel model);


    /**
     * @Description: 更新卡商的余额-根据代付订单
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/15 20:36
     */
    public int updateBalanceByOrderOut(MerchantModel model);
}
