package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.common.utils.DateUtil;
import com.cake.task.master.core.common.utils.StringUtil;
import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.mapper.ReplacePayStrategyMapper;
import com.cake.task.master.core.model.replacepay.ReplacePayStrategyModel;
import com.cake.task.master.core.service.ReplacePayStrategyService;
import com.cake.task.master.util.ComponentUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @Description 代付放量策略的Service层的实现层
 * @Author yoko
 * @Date 2021/6/15 18:19
 * @Version 1.0
 */
@Service
public class ReplacePayStrategyServiceImpl<T> extends BaseServiceImpl<T> implements ReplacePayStrategyService<T> {

    private static Logger log = LoggerFactory.getLogger(ReplacePayStrategyServiceImpl.class);

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
    private ReplacePayStrategyMapper replacePayStrategyMapper;



    public BaseDao<T> getDao() {
        return replacePayStrategyMapper;
    }

    @Override
    public void replacePayStrategyLimit(ReplacePayStrategyModel replacePayStrategyModel, int dayNum, String dayMoney, String monthMoney) {
        String dayMoney_redis = getRedisDataByKey(CacheKey.OUT_DAY_MONEY, replacePayStrategyModel.getId());
        if (StringUtils.isBlank(dayMoney_redis)){
            if (!StringUtils.isBlank(replacePayStrategyModel.getOutDayMoney())){
                // 判断日金额是否超过规定金额
                boolean flag = StringUtil.getBigDecimalSubtract(replacePayStrategyModel.getOutDayMoney(), dayMoney);
                if (!flag){
                    // 超过限量额度，redis存储
                    // 缓存设置：失效时间当日的凌晨零点
                    long time = DateUtil.getTomorrowMinute();
                    String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.OUT_DAY_MONEY, replacePayStrategyModel.getId());
                    ComponentUtil.redisService.set(strKeyCache, dayMoney, time);
                }
            }
        }
        String monthMoney_redis = getRedisDataByKey(CacheKey.OUT_MONTH_MONEY, replacePayStrategyModel.getId());
        if (StringUtils.isBlank(monthMoney_redis)){
            if (!StringUtils.isBlank(replacePayStrategyModel.getOutMonthMoney())){
                // 判断月金额是否超过规定金额
                boolean flag = StringUtil.getBigDecimalSubtract(replacePayStrategyModel.getOutMonthMoney(), monthMoney);
                if (!flag){
                    // 超过限量额度，redis存储
                    // 缓存设置：失效时间当前时间到月底的天数
                    int time = DateUtil.differByEndMonth();
                    String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.OUT_MONTH_MONEY, replacePayStrategyModel.getId());
                    ComponentUtil.redisService.set(strKeyCache, monthMoney, time, TimeUnit.DAYS);
                }
            }
        }
        String dayNum_redis = getRedisDataByKey(CacheKey.OUT_DAY_NUM, replacePayStrategyModel.getId());
        if (StringUtils.isBlank(dayNum_redis)){
            if (replacePayStrategyModel.getOutDayNum() != null && replacePayStrategyModel.getOutDayNum() > 0){
                // 判断日日数是否超过规定次数
                if (dayNum >= replacePayStrategyModel.getOutDayNum()){
                    // 缓存设置：失效时间当日的凌晨零点
                    long time = DateUtil.getTomorrowMinute();
                    String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.OUT_DAY_NUM, replacePayStrategyModel.getId());
                    ComponentUtil.redisService.set(strKeyCache, String.valueOf(dayNum), time);
                }
            }
        }
    }



    /**
     * @Description: 组装缓存key查询缓存中存在的数据
     * @param cacheKey - 缓存的类型key
     * @param obj - 数据的ID
     * @return
     * @author yoko
     * @date 2020/5/20 14:59
     */
    public String getRedisDataByKey(String cacheKey, Object obj){
        String str = null;
        String strKeyCache = CachedKeyUtils.getCacheKey(cacheKey, obj);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            return str;
        }else{
            str = strCache;
            return str;
        }
    }

}
