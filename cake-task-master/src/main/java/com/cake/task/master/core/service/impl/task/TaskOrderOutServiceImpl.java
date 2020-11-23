package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.exception.ServiceException;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.InterestProfitMapper;
import com.cake.task.master.core.mapper.MerchantBalanceDeductMapper;
import com.cake.task.master.core.mapper.MerchantProfitMapper;
import com.cake.task.master.core.mapper.task.TaskOrderOutMapper;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.service.task.TaskOrderOutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description task：代付订单表的Service层的实现层
 * @Author yoko
 * @Date 2020/11/1 21:28
 * @Version 1.0
 */
@Service
public class TaskOrderOutServiceImpl<T> extends BaseServiceImpl<T> implements TaskOrderOutService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskOrderOutMapper taskOrderOutMapper;

    @Autowired
    private MerchantBalanceDeductMapper merchantBalanceDeductMapper;

    @Autowired
    private MerchantProfitMapper merchantProfitMapper;

    @Autowired
    private InterestProfitMapper interestProfitMapper;



    public BaseDao<T> getDao() {
        return taskOrderOutMapper;
    }

    @Override
    public List<OrderOutModel> getDataList(Object obj) {
        return taskOrderOutMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskOrderOutMapper.updateStatus(obj);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleSuccessOrderOut(MerchantBalanceDeductModel merchantBalanceDeductUpdate, MerchantProfitModel merchantProfitModel, List<InterestProfitModel> interestProfitList) throws Exception {
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        try {
            num1 = merchantBalanceDeductMapper.updateOrderStatusByOrderNo(merchantBalanceDeductUpdate);
            if (merchantProfitModel == null){
                num2 = 1;
            }else {
                num2 = merchantProfitMapper.add(merchantProfitModel);
            }
            if (interestProfitList == null || interestProfitList.size() <= 0){
                if (num1> 0 && num2 > 0){
                    return true;
                }else {
                    throw new ServiceException("handleSuccessOrderOut", "二个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
                }
            }else {
                num3 = interestProfitMapper.addBatchInterestProfit(interestProfitList);
                if (num3 != interestProfitList.size()){
                    // 说明批量查询的数据影响条数与集合的数据条数不一
                    num3 = 0;
                }
                if (num1> 0 && num2 > 0 && num3 > 0){
                    return true;
                }else {
                    throw new ServiceException("handleSuccessOrderOut", "三个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("handleSuccessOrderOut", "执行时,出现未抓取到的错误");
        }




    }
}
