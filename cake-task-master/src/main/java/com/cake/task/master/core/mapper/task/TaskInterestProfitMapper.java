package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:利益者收益数据的Dao层
 * @Author yoko
 * @Date 2020/11/10 15:39
 * @Version 1.0
 */
@Mapper
public interface TaskInterestProfitMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的利益者收益信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<InterestProfitModel> getDataList(Object obj);

    /**
     * @Description: 更新利益者收益信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
