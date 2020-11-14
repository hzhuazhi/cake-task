package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.change.ChangeMoneyModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:变更金额纪录的Dao层
 * @Author yoko
 * @Date 2020/11/14 14:23
 * @Version 1.0
 */
@Mapper
public interface TaskChangeMoneyMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的变更金额纪录信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ChangeMoneyModel> getDataList(Object obj);

    /**
     * @Description: 更新变更金额纪录信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
