package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:商户的提现记录的Dao层
 * @Author yoko
 * @Date 2020/11/19 11:46
 * @Version 1.0
 */
@Mapper
public interface TaskChannelWithdrawMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的商户的提现记录信息
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ChannelWithdrawModel> getDataList(Object obj);

    /**
     * @Description: 更新商户的提现记录信息数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

    /**
     * @Description: 更新平台的提现状态
     * @param model
     * @return
     * @author yoko
     * @date 2020/11/19 19:02
    */
    public int updatePlatformWithdrawStatus(ChannelWithdrawModel model);
}
