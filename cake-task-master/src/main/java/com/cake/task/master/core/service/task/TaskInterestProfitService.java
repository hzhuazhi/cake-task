package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.interest.InterestProfitModel;

import java.util.List;

/**
 * @Description task:利益者收益数据的Service层
 * @Author yoko
 * @Date 2020/11/10 15:52
 * @Version 1.0
 */
public interface TaskInterestProfitService<T> extends BaseService<T> {

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
