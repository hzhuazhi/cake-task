package com.cake.task.master.core.service.impl;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.exception.ServiceException;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.OrderOutMapper;
import com.cake.task.master.core.mapper.OrderOutPrepareMapper;
import com.cake.task.master.core.mapper.ReplacePayGainMapper;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayGainModel;
import com.cake.task.master.core.service.OrderOutPrepareService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * @Description 代付预备请求的Service层的实现层
 * @Author yoko
 * @Date 2020/10/29 17:47
 * @Version 1.0
 */
@Service
public class OrderOutPrepareServiceImpl<T> extends BaseServiceImpl<T> implements OrderOutPrepareService<T> {
    private static Logger log = LoggerFactory.getLogger(OrderOutPrepareServiceImpl.class);

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
    private OrderOutPrepareMapper orderOutPrepareMapper;

    @Autowired
    private ReplacePayGainMapper replacePayGainMapper;

    @Autowired
    private OrderOutMapper orderOutMapper;



    public BaseDao<T> getDao() {
        return orderOutPrepareMapper;
    }



    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleOrderOutPrepare(ReplacePayGainModel replacePayGainAdd, OrderOutModel orderOutUpdate) throws Exception {
        int num1 = 0;
        int num2 = 0;
        try {

            if (replacePayGainAdd == null){
                num1 = 1;
            }else {
                num1 = replacePayGainMapper.add(replacePayGainAdd);
            }
            num2 = orderOutMapper.updateOrderOutByPrepare(orderOutUpdate);

            if (num1> 0 && num2 > 0){
                return true;
            }else {
                throw new ServiceException("handleOrderOutPrepare", "二个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("handleOrderOutPrepare", "执行时,出现未抓取到的错误");
        }
    }
}
