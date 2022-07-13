package com.cake.task.master.core.service.task;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.zhongbang.ZbWhitelistModel;

import java.util.List;

/**
 * @Description task:众邦白名单的Service层
 * @Author yoko
 * @Date 2020/9/14 22:14
 * @Version 1.0
 */
public interface TaskZbWhitelistService<T> extends BaseService<T> {

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
