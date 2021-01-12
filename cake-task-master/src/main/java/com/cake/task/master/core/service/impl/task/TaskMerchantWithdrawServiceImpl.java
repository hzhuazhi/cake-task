package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.exception.ServiceException;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.MerchantMapper;
import com.cake.task.master.core.mapper.MerchantWithdrawMapper;
import com.cake.task.master.core.mapper.task.TaskMerchantWithdrawMapper;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantWithdrawModel;
import com.cake.task.master.core.service.task.TaskMerchantWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description task:卡商的提现记录的实现层
 * @Author yoko
 * @Date 2021/1/11 11:23
 * @Version 1.0
 */
@Service
public class TaskMerchantWithdrawServiceImpl <T> extends BaseServiceImpl<T> implements TaskMerchantWithdrawService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskMerchantWithdrawMapper taskMerchantWithdrawMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private MerchantWithdrawMapper merchantWithdrawMapper;


    public BaseDao<T> getDao() {
        return taskMerchantWithdrawMapper;
    }

    @Override
    public List<MerchantWithdrawModel> getDataList(Object obj) {
        return taskMerchantWithdrawMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskMerchantWithdrawMapper.updateStatus(obj);
    }


    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleMerchantWithdraw(MerchantModel merchantUpdateBalance, MerchantModel merchantUpdateProfit, MerchantWithdrawModel merchantWithdrawUpdate) throws Exception {
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        try {

            num1 = merchantMapper.updateAddOrSubtractBalance(merchantUpdateBalance);
            num2 = merchantMapper.updateAddOrSubtractProfit(merchantUpdateProfit);
            num3 = merchantWithdrawMapper.updateOrderStatus(merchantWithdrawUpdate);

            if (num1> 0 && num2 > 0 && num3 > 0){
                return true;
            }else {
                throw new ServiceException("handleMerchantWithdraw", "三个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("handleMerchantWithdraw", "执行时,出现未抓取到的错误");
        }
    }
}
