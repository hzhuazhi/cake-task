package com.cake.task.master.core.mapper;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.issue.IssueModel;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description 下发的Dao层
 * @Author yoko
 * @Date 2020/9/23 11:43
 * @Version 1.0
 */
@Mapper
public interface IssueMapper<T> extends BaseDao<T> {

    /**
     * @Description: 更新是否分配完毕归属
     * @param model
     * @return
     * @author yoko
     * @date 2020/9/24 21:51
    */
    public int updateDistribution(IssueModel model);
}
