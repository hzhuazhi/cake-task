package com.cake.task.master.core.mapper;
import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.bank.BankModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description 银行的Dao层
 * @Author yoko
 * @Date 2020/5/18 19:04
 * @Version 1.0
 */
@Mapper
public interface BankMapper<T> extends BaseDao<T> {

    /**
     * @Description: 获取银行卡以及银行卡的放量策略数据
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/12 16:24
    */
    public List<BankModel> getBankAndStrategy(BankModel model);

    /**
     * @Description: 计算有多少张卡处于上线状态
     * 两个查询值：卡商ID，使用状态为正在使用的
     * @param model - 银行基本信息
     * @return
     * @author yoko
     * @date 2021/3/30 17:11
    */
    public int countUseNum(BankModel model);

    /**
     * @Description: 计算有多少长卡可以上线的
     * <p>
     *     计算已经换卡完毕，但是未上线的卡的数量
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2021/3/30 17:25
    */
    public int countCanUseNum(BankModel model);

    /**
     * @Description: 获取正在使用的最大银行卡ID
     * <p>
     *     查询条件：卡商ID，使用状态
     * </p>
     * @param model - 银行信息
     * @return
     * @author yoko
     * @date 2021/3/30 17:13
    */
    public long getMaxBankIdByUse(BankModel model);

    /**
     * @Description: 获取下一个未上线但是可以上线的银行信息
     * <p>
     *     根据卡商目前上线的银行最大ID，查询比这个银行ID大一点的银行信息；
     *     俗称下一张要上线的银行卡
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2021/3/30 17:27
    */
    public BankModel getNextBankByNotUse(BankModel model);


    /**
     * @Description: 获取最小的未上线但是可以上线的银行信息
     * <p>
     *     因为目前最大的银行ID都已经处于上线状态，需要轮询一回合，所以查询最小的银行卡ID
     * </p>
     * @param model
     * @return
     * @author yoko
     * @date 2021/3/30 17:27
     */
    public BankModel getMinBankByNotUse(BankModel model);


}
