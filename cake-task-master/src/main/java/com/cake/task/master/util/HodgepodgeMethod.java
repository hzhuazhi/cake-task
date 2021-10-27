package com.cake.task.master.util;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.order.OrderOutLimitModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.order.OrderOutPrepareModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;


/**
 * @Description 公共方法类
 * @Author yoko
 * @Date 2020/1/7 20:25
 * @Version 1.0
 */
public class HodgepodgeMethod {
    private static Logger log = LoggerFactory.getLogger(HodgepodgeMethod.class);



    /**
     * @Description: 换算杉德代付拉单的订单金额
     * <p>
     *     不足12位则订单金额前面补0
     * </p>
     * @param orderMoney - 代付拉单订单金额
     * @return
     * @Author: yoko
     * @Date 2021/10/13 16:02
     */
    public static String sandPayOrderMoney(String orderMoney){
        String str = "";
        BigDecimal amt  = new BigDecimal (orderMoney);
        Double tmp = amt.multiply(new BigDecimal(100)).doubleValue();
        str = String.format("%012d", Math.abs(tmp.intValue()));
        return str;
    }


    /**
     * @Description: 换算杉德余额
     * <p>
     *     杉德余额属于12位，需要把余额变更成正常金额
     * </p>
     * @param balance - 余额
     * @return
     * @Author: yoko
     * @Date 2021/10/13 16:02
     */
    public static String sandPayBalance(String balance){
        String str = "";
        BigDecimal  amt  = new BigDecimal (balance);
        Double tmp = amt.divide(new BigDecimal(100)).doubleValue();
        str = tmp.toString();
        return str;
    }


    /**
     * @Description: 组装代付查询黑名单的查询条件
     * @param accountName - 开户人姓名
     * @param bankCard - 开户人卡号
     * @return com.hz.cake.master.core.model.order.OrderOutLimitModel
     * @Author: yoko
     * @Date 2021/10/25 15:22
     */
    public static OrderOutLimitModel assembleOrderOutLimitQuery(String accountName, String bankCard){
        OrderOutLimitModel resBean = new OrderOutLimitModel();
        if (!StringUtils.isBlank(accountName)){
            resBean.setAccountName(accountName);
        }
        if (!StringUtils.isBlank(bankCard)){
            resBean.setBankCard(bankCard);
        }
        return resBean;
    }


    /**
     * @Description: 组装添加代付订单信息
     * @param orderOutPrepareModel - 预备代付订单信息
     * @param replacePayModel - 第三方资源信息表
     * @param merchantList - 卡商信息集合
     * @param serviceCharge - 手续费
     * @param outStatus - 代付订单出码状态:1初始化（我方处理默认初始化），2出码成功（第三方反馈结果），3出码失败
     * @param tradeTime - 交易时间时间戳
     * @param invalidTime - 订单失效时间
     * @param failInfo - 失败缘由
     * @return com.cake.task.master.core.model.order.OrderOutModel
     * @Author: yoko
     * @Date 2021/10/27 16:57
     */
    public static OrderOutModel assembleOrderOutAdd(OrderOutPrepareModel orderOutPrepareModel, ReplacePayModel replacePayModel,
                                                    List<MerchantModel> merchantList, String serviceCharge, int outStatus,String tradeTime,
                                                    String invalidTime, String failInfo){
        OrderOutModel resBean = new OrderOutModel();
        if (!StringUtils.isBlank(orderOutPrepareModel.getOrderNo())){
            resBean.setOrderNo(orderOutPrepareModel.getOrderNo());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getOrderMoney())){
            resBean.setOrderMoney(orderOutPrepareModel.getOrderMoney());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getOutTradeNo())){
            resBean.setOutTradeNo(orderOutPrepareModel.getOutTradeNo());
        }else {
            return null;
        }
        resBean.setOrderType(orderOutPrepareModel.getOrderType());
        if (!StringUtils.isBlank(serviceCharge)){
            resBean.setServiceCharge(serviceCharge);
        }
        resBean.setHandleType(orderOutPrepareModel.getHandleType());
        resBean.setOutStatus(outStatus);
        if (!StringUtils.isBlank(invalidTime)){
            resBean.setInvalidTime(invalidTime);
        }

        if (!StringUtils.isBlank(orderOutPrepareModel.getInBankCard())){
            resBean.setInBankCard(orderOutPrepareModel.getInBankCard());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getInBankName())){
            resBean.setInBankName(orderOutPrepareModel.getInBankName());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getInAccountName())){
            resBean.setInAccountName(orderOutPrepareModel.getInAccountName());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getInBankSubbranch())){
            resBean.setInBankSubbranch(orderOutPrepareModel.getInBankSubbranch());
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getInBankProvince())){
            resBean.setInBankProvince(orderOutPrepareModel.getInBankProvince());
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getInBankCity())){
            resBean.setInBankCity(orderOutPrepareModel.getInBankCity());
        }

        if (replacePayModel != null && replacePayModel.getId() != null){
            // 三方代付信息
            resBean.setReplacePayId(replacePayModel.getId());
            resBean.setReplacePayName(replacePayModel.getAlias());
            resBean.setResourceType(replacePayModel.getResourceType());


            // 卡商信息
            MerchantModel merchantModel = null;
            if (merchantList != null && merchantList.size() > 0){
                for (MerchantModel merchantData : merchantList){
                    if (merchantData.getId() == replacePayModel.getMerchantId()){
                        merchantModel = merchantData;
                    }
                }

//                resBean.setOrderType(merchantModel.getPayType());
                resBean.setMerchantId(merchantModel.getId());
                if (!StringUtils.isBlank(merchantModel.getAcName())){
                    resBean.setMerchantName(merchantModel.getAcName());
                }
            }
        }

        resBean.setChannelId(orderOutPrepareModel.getChannelId());
        if (!StringUtils.isBlank(orderOutPrepareModel.getChannelName())){
            resBean.setChannelName(orderOutPrepareModel.getChannelName());
        }
        if (!StringUtils.isBlank(tradeTime)){
            resBean.setTradeTime(String.valueOf(tradeTime));
        }
        if (!StringUtils.isBlank(failInfo)){
            resBean.setFailInfo(failInfo);
        }
        if (!StringUtils.isBlank(orderOutPrepareModel.getNotifyUrl())){
            resBean.setNotifyUrl(orderOutPrepareModel.getNotifyUrl());
        }

        resBean.setCurday(orderOutPrepareModel.getCurday());
        resBean.setCurhour(orderOutPrepareModel.getCurhour());
        resBean.setCurminute(orderOutPrepareModel.getCurminute());

        return resBean;

    }



    public static void main(String [] args) throws Exception{
    }




    

}
