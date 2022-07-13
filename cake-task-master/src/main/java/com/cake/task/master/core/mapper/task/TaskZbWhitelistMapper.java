package com.cake.task.master.core.mapper.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.model.zhongbang.ZbWhitelistModel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Description task:众邦白名单的Dao层
 * @Author yoko
 * @Date 2020/9/14 21:57
 * @Version 1.0
 */
@Mapper
public interface TaskZbWhitelistMapper<T> extends BaseDao<T> {

    /**
     * @Description: 查询未跑的白名单数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/3 13:53
     */
    public List<ZbWhitelistModel> getDataList(Object obj);

    /**
     * @Description: 更新白名单数据的状态
     * @param obj
     * @return
     * @author yoko
     * @date 2020/1/11 16:30
     */
    public int updateStatus(Object obj);

    /**
     * @Description: 获取要同步给下游的白名单数据
     * @param obj
     * @return
     * @author yoko
     * @date 2020/6/8 17:38
     */
    public List<ZbWhitelistModel> getWhitelistNotifyList(Object obj);
}
