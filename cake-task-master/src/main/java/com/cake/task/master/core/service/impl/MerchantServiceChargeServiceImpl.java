package com.cake.task.master.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.mapper.MerchantServiceChargeMapper;
import com.cake.task.master.core.model.merchant.MerchantServiceChargeModel;
import com.cake.task.master.core.service.MerchantServiceChargeService;
import com.cake.task.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description 卡商绑定渠道的手续费的Service层的实现层
 * @Author yoko
 * @Date 2020/11/26 13:21
 * @Version 1.0
 */
@Service
public class MerchantServiceChargeServiceImpl<T> extends BaseServiceImpl<T> implements MerchantServiceChargeService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 3分钟
     */
    public long THREE_MIN = 180;

    /**
     * 11分钟.
     */
    public long ELEVEN_MIN = 660;

    public long TWO_HOUR = 2;



    @Autowired
    private MerchantServiceChargeMapper merchantServiceChargeMapper;

    public BaseDao<T> getDao() {
        return merchantServiceChargeMapper;
    }


    @Override
    public MerchantServiceChargeModel getMerchantServiceChargeModel(MerchantServiceChargeModel model, int isCache) throws Exception {
        MerchantServiceChargeModel dataModel = null;
        if (isCache == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.MERCHANT_SERVICE_CHARGE, model.getMerchantId(), model.getChannelId());
            String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
            if (!StringUtils.isBlank(strCache)) {
                // 从缓存里面获取数据
                dataModel = JSON.parseObject(strCache, MerchantServiceChargeModel.class);
            } else {
                //查询数据库
                dataModel = (MerchantServiceChargeModel) merchantServiceChargeMapper.findByObject(model);
                if (dataModel != null && dataModel.getId() != ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO) {
                    // 把数据存入缓存
                    ComponentUtil.redisService.set(strKeyCache, JSON.toJSONString(dataModel, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), FIVE_MIN);
                }
            }
        }else {
            // 直接查数据库
            // 查询数据库
            dataModel = (MerchantServiceChargeModel) merchantServiceChargeMapper.findByObject(model);
        }
        return dataModel;
    }
}
