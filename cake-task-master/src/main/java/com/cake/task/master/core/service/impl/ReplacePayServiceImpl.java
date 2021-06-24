package com.cake.task.master.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.constant.TkCacheKey;
import com.cake.task.master.core.mapper.ReplacePayMapper;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import com.cake.task.master.core.service.ReplacePayService;
import com.cake.task.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description 代付资源的Service层的实现层
 * @Author yoko
 * @Date 2021/6/9 14:03
 * @Version 1.0
 */
@Service
public class ReplacePayServiceImpl<T> extends BaseServiceImpl<T> implements ReplacePayService<T> {

    private static Logger log = LoggerFactory.getLogger(ReplacePayServiceImpl.class);

    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    /**
     * 15分钟.
     */
    public long FIFTEEN_MIN = 900;

    public long TWO_HOUR = 2;

    @Autowired
    private ReplacePayMapper replacePayMapper;



    public BaseDao<T> getDao() {
        return replacePayMapper;
    }

    @Override
    public List<ReplacePayModel> getReplacePayList(ReplacePayModel model) {
        return replacePayMapper.getReplacePayList(model);
    }

    @Override
    public int updateBalance(ReplacePayModel model) {
        return replacePayMapper.updateBalance(model);
    }


    @Override
    public ReplacePayModel getReplacePayModel(ReplacePayModel model, int isCache) throws Exception {
        ReplacePayModel dataModel = null;
        if (isCache == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO){
            String strKeyCache = CachedKeyUtils.getCacheKey(TkCacheKey.REPLACE_PAY, model.getId());
            String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
            if (!StringUtils.isBlank(strCache)) {
                // 从缓存里面获取数据
                dataModel = JSON.parseObject(strCache, ReplacePayModel.class);
            } else {
                //查询数据库
                dataModel = (ReplacePayModel) replacePayMapper.findByObject(model);
                if (dataModel != null && dataModel.getId() != ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ZERO) {
                    // 把数据存入缓存
                    ComponentUtil.redisService.set(strKeyCache, JSON.toJSONString(dataModel, SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty), FIVE_MIN);
                }
            }
        }else {
            // 直接查数据库
            // 查询数据库
            dataModel = (ReplacePayModel) replacePayMapper.findByObject(model);
        }
        return dataModel;
    }
}
