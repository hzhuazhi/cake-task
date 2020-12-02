package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 提现记录的Dao层
 * @Author yoko
 * @Date 2020/11/19 17:09
 * @Version 1.0
 */
@Mapper
public interface WithdrawMapper<T> extends BaseDao<T> {

    /**
     * @Description: 根据条件查询下发的金额总和
     * @param model
     * @return
     * @author yoko
     * @date 2020/12/2 16:28
    */
    public String sumMoney(WithdrawModel model);
}
