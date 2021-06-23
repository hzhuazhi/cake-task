package com.cake.task.master.core.service;


import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;

import java.util.List;

/**
 * @Description 代付资源的Service层
 * @Author yoko
 * @Date 2021/6/9 14:03
 * @Version 1.0
 */
public interface ReplacePayService<T> extends BaseService<T> {


    /**
     * @Description: 获取代付的出码集合数据
     * @param model
     * @return
     * @author yoko
     * @date 2021/6/18 17:00
     */
    public List<ReplacePayModel> getReplacePayList(ReplacePayModel model);

    /**
     * @Description: 更新代付账户的余额
     * @param model
     * @return
     * @author yoko
     * @date 2021/6/22 16:57
     */
    public int updateBalance(ReplacePayModel model);


    /**
     * @Description: 根据条件查询代付数据
     * @param model - 查询条件
     * @param isCache - 是否通过缓存查询：0需要通过缓存查询，1直接查询数据库
     * @return
     * @author yoko
     * @date 2019/11/21 19:26
     */
    public ReplacePayModel getReplacePayModel(ReplacePayModel model, int isCache) throws Exception;
}
