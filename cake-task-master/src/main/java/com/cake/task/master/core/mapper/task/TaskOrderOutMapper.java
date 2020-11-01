package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.order.OrderOutModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task：代付订单表的Dao层
 * @Author yoko
 * @Date 2020/11/1 21:26
 * @Version 1.0
 */
@Mapper
public interface TaskOrderOutMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的代付订单信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<OrderOutModel> getDataList(Object obj);

    /**
     * @Description: 更新代付订单信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
