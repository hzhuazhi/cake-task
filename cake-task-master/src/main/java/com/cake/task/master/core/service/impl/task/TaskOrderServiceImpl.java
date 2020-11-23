package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.exception.ServiceException;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.BankCollectionMapper;
import com.cake.task.master.core.mapper.InterestProfitMapper;
import com.cake.task.master.core.mapper.MerchantMapper;
import com.cake.task.master.core.mapper.MerchantProfitMapper;
import com.cake.task.master.core.mapper.task.TaskOrderMapper;
import com.cake.task.master.core.model.bank.BankCollectionModel;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.order.OrderModel;
import com.cake.task.master.core.service.task.TaskOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description task:订单的Service层的实现层
 * @Author yoko
 * @Date 2020/9/14 22:15
 * @Version 1.0
 */
@Service
public class TaskOrderServiceImpl<T> extends BaseServiceImpl<T> implements TaskOrderService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskOrderMapper taskOrderMapper;

    @Autowired
    private BankCollectionMapper bankCollectionMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantProfitMapper merchantProfitMapper;

    @Autowired
    private InterestProfitMapper interestProfitMapper;



    public BaseDao<T> getDao() {
        return taskOrderMapper;
    }

    @Override
    public List<OrderModel> getDataList(Object obj) {
        return taskOrderMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskOrderMapper.updateStatus(obj);
    }

    @Override
    public List<OrderModel> getOrderNotifyList(Object obj) {
        return taskOrderMapper.getOrderNotifyList(obj);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleSuccessOrder(BankCollectionModel bankCollectionModel, MerchantModel merchantUpdateMoney, MerchantProfitModel merchantProfitModel, List<InterestProfitModel> interestProfitList) throws Exception {
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        int num4 = 0;
        try {
            num1 = bankCollectionMapper.add(bankCollectionModel);
            num2 = merchantMapper.updateMoney(merchantUpdateMoney);
            if (merchantProfitModel == null){
                num3 = 1;
            }else {
                num3 = merchantProfitMapper.add(merchantProfitModel);
            }
            if (interestProfitList == null || interestProfitList.size() <= 0){
                if (num1> 0 && num2 > 0 && num3 > 0){
                    return true;
                }else {
                    throw new ServiceException("handleSuccessOrder", "三个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
                }
            }else {
                num4 = interestProfitMapper.addBatchInterestProfit(interestProfitList);
                if (num4 != interestProfitList.size()){
                    // 说明批量查询的数据影响条数与集合的数据条数不一
                    num4 = 0;
                }
                if (num1> 0 && num2 > 0 && num3 > 0 && num4 > 0){
                    return true;
                }else {
                    throw new ServiceException("handleSuccessOrderOut", "四个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
                }
            }

        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("handleSuccessOrder", "执行时,出现未抓取到的错误");
        }

    }
}
