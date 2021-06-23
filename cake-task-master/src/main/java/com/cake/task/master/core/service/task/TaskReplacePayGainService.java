package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.replacepay.ReplacePayGainModel;

import java.util.List;

/**
 * @Description task:第三方代付主动拉取结果的Service层
 * @Author yoko
 * @Date 2021/6/22 17:12
 * @Version 1.0
 */
public interface TaskReplacePayGainService<T> extends BaseService<T> {

    /**
     * @Description: 查询需要获取订单结果的代付订单数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ReplacePayGainModel> getDataList(Object obj);

    /**
     * @Description: 更新查询代付结果的数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
