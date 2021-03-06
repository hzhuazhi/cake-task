package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.merchant.MerchantWithdrawModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:卡商的提现记录的Dao层
 * @Author yoko
 * @Date 2021/1/11 11:25
 * @Version 1.0
 */
@Mapper
public interface TaskMerchantWithdrawMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的卡商的提现记录信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<MerchantWithdrawModel> getDataList(Object obj);

    /**
     * @Description: 更新卡商的提现记录信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
