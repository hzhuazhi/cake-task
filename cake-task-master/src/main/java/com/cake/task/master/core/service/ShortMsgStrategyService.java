package com.cake.task.master.core.service;

import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.shortmsg.ShortMsgStrategyModel;

import java.util.List;

/**
 * @Description 短信的类型定位策略的Service层
 * @Author yoko
 * @Date 2020/9/11 14:57
 * @Version 1.0
 */
public interface ShortMsgStrategyService<T> extends BaseService<T> {

    /**
     * @Description: 短信的类型定位策略的数据
     * @param model - 查询条件
     * @param isCache - 是否通过缓存查询：0需要通过缓存查询，1直接查询数据库
     * @return
     * @author yoko
     * @date 2019/11/21 19:26
     */
    public List<ShortMsgStrategyModel> getShortMsgStrategyList(ShortMsgStrategyModel model, int isCache) throws Exception;
}
