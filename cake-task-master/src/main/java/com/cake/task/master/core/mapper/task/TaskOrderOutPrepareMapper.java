package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.order.OrderOutPrepareModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:代付预备请求的Dao层
 * @Author yoko
 * @Date 2021/6/23 10:08
 * @Version 1.0
 */
@Mapper
public interface TaskOrderOutPrepareMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询需要运算代付预备请求的数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<OrderOutPrepareModel> getDataList(Object obj);

    /**
     * @Description: 更新运算代付预备请求的数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
