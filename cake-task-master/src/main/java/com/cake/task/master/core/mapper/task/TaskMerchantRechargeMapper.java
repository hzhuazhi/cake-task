package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.merchant.MerchantRechargeModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:卡商充值记录的Dao层
 * @Author yoko
 * @Date 2020/9/15 20:07
 * @Version 1.0
 */
@Mapper
public interface TaskMerchantRechargeMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的卡商充值信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<MerchantRechargeModel> getDataList(Object obj);

    /**
     * @Description: 更新卡商充值信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
