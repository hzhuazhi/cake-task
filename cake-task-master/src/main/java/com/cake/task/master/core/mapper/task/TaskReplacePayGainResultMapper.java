package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.replacepay.ReplacePayGainResultModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:第三方代付主动拉取结果返回的订单结果的Dao层
 * @Author yoko
 * @Date 2021/6/23 10:08
 * @Version 1.0
 */
@Mapper
public interface TaskReplacePayGainResultMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询需要运算第三方代付订单结果的数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ReplacePayGainResultModel> getDataList(Object obj);

    /**
     * @Description: 更新运算第三方代付订单结果的数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);
}
