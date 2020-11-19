package com.cake.task.master.core.service.impl.task;

import com.cake.task.master.core.common.dao.BaseDao;
import com.cake.task.master.core.common.exception.ServiceException;
import com.cake.task.master.core.common.service.impl.BaseServiceImpl;
import com.cake.task.master.core.mapper.ChannelWithdrawMapper;
import com.cake.task.master.core.mapper.MerchantMapper;
import com.cake.task.master.core.mapper.MerchantProfitMapper;
import com.cake.task.master.core.mapper.task.TaskWithdrawMapper;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantProfitModel;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
import com.cake.task.master.core.service.task.TaskWithdrawService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Description task:提现记录的Service层的实现层
 * @Author yoko
 * @Date 2020/11/19 17:14
 * @Version 1.0
 */
@Service
public class TaskWithdrawServiceImpl<T> extends BaseServiceImpl<T> implements TaskWithdrawService<T> {
    /**
     * 5分钟.
     */
    public long FIVE_MIN = 300;

    public long TWO_HOUR = 2;

    @Autowired
    private TaskWithdrawMapper taskWithdrawMapper;

    @Autowired
    private MerchantProfitMapper merchantProfitMapper;

    @Autowired
    private MerchantMapper merchantMapper;

    @Autowired
    private ChannelWithdrawMapper channelWithdrawMapper;


    public BaseDao<T> getDao() {
        return taskWithdrawMapper;
    }

    @Override
    public List<WithdrawModel> getDataList(Object obj) {
        return taskWithdrawMapper.getDataList(obj);
    }

    @Override
    public int updateStatus(Object obj) {
        return taskWithdrawMapper.updateStatus(obj);
    }

    @Transactional(rollbackFor=Exception.class)
    @Override
    public boolean handleChannelWithdraw(MerchantProfitModel merchantProfitAdd, MerchantModel merchantUpdate, ChannelWithdrawModel channelWithdrawUpdate) throws Exception {
        int num1 = 0;
        int num2 = 0;
        int num3 = 0;
        try {

            if (merchantProfitAdd == null){
                num1 = 1;
            }else {
                num1 = merchantProfitMapper.add(merchantProfitAdd);
            }
            if (merchantUpdate == null){
                num2 = 1;
            }else {
                num2 = merchantMapper.updateAddOrSubtractBalance(merchantUpdate);
            }
            num3 = channelWithdrawMapper.updateOrderStatus(channelWithdrawUpdate);

            if (num1> 0 && num2 > 0 && num3 > 0){
                return true;
            }else {
                throw new ServiceException("handleChannelWithdraw", "三个执行更新SQL其中有一个或者多个响应行为0");
//                throw new RuntimeException();
            }
        }catch (Exception e){
            e.printStackTrace();
            throw new ServiceException("handleChannelWithdraw", "执行时,出现未抓取到的错误");
        }
    }
}
