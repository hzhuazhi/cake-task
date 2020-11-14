package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.change.ChangeMoneyModel;

import java.util.List;

/**
 * @Description task:变更金额纪录的Service层
 * @Author yoko
 * @Date 2020/11/14 14:39
 * @Version 1.0
 */
public interface TaskChangeMoneyService<T> extends BaseService<T> {

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
