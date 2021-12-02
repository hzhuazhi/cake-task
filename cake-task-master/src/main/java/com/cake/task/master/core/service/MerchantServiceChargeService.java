package com.cake.task.master.core.service;


import com.cake.task.master.core.common.service.BaseService;
import com.cake.task.master.core.model.merchant.MerchantServiceChargeModel;

/**
 * @Description 卡商绑定渠道的手续费的Service层
 * @Author yoko
 * @Date 2020/11/26 13:20
 * @Version 1.0
 */
public interface MerchantServiceChargeService<T> extends BaseService<T> {
    /**
     * @Description: 根据条件查询卡商绑定渠道的手续费数据
     * @param model - 查询条件
     * @param isCache - 是否通过缓存查询：0需要通过缓存查询，1直接查询数据库
     * @return
     * @author yoko
     * @date 2019/11/21 19:26
     */
    public MerchantServiceChargeModel getMerchantServiceChargeModel(MerchantServiceChargeModel model, int isCache) throws Exception;
}
