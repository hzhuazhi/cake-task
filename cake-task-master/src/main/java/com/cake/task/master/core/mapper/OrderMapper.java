package com.cake.task.master.core.mapper;
import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.order.OrderModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


/**
 * @Description 任务订单的Dao层
 * @Author yoko
 * @Date 2020/5/21 19:32
 * @Version 1.0
 */
@Mapper
public interface OrderMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据订单号查询订单状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/6/8 20:00
     */
    public int getOrderStatus(OrderModel model);

    /**
     * @Description: 根据条件查询给出订单的次数
     * <p>
     *     目前用到：银行卡成功的次数（根据银行卡、支付类型、日期计算）
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/15 10:39
    */
    public int countOrder(OrderModel model);

    /**
     * @Description: 根据条件查询成功金额
     * <p>
     *     目前用到：银行卡的成功金额（根据银行卡、支付类型、日期计算）
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/15 10:41
    */
    public String sumOrderMoney(OrderModel model);

    /**
     * @Description: 查询订单的成功数量
     * @param model - 订单信息
     * @return
     * @author yoko
     * @date 2021/3/31 19:17
    */
    public int countSucOrderNum(OrderModel model);

    /**
     * @Description: 根据条件获取订单信息
     * <p>
     *     这里规定只查询几条订单（有限制），
     *     并且按照时间降序排序
     * </p>
     * @param model - 订单信息
     * @return
     * @author yoko
     * @date 2021/3/31 19:49
    */
    public List<OrderModel> getOrderByLimitList(OrderModel model);

}
