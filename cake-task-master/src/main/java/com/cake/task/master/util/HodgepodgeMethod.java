package com.cake.task.master.util;
import com.cake.task.master.core.common.utils.DateUtil;
import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel;
import com.cake.task.master.core.model.merchant.MerchantChannelModel;
import com.cake.task.master.core.model.merchant.MerchantModel;
import com.cake.task.master.core.model.merchant.MerchantServiceChargeModel;
import com.cake.task.master.core.model.order.OrderOutLimitModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.order.OrderOutPrepareModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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



    /**
     * @Description: 组装查询卡商与渠道关联关系
     * @param id - 主键ID
     * @param merchantId - 卡商ID
     * @param merchantSiteId - 卡站点ID
     * @param channelId - 渠道ID
     * @param useStatus - 使用状态
     * @return com.hz.cake.master.core.model.merchant.MerchantChannelModel
     * @author yoko
     * @date 2020/10/30 14:04
     */
    public static MerchantChannelModel assembleMerchantChannelQuery(long id, long merchantId, long merchantSiteId, long channelId, int useStatus){
        MerchantChannelModel resBean = new MerchantChannelModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (merchantSiteId > 0){
            resBean.setMerchantSiteId(merchantSiteId);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询卡商信息的方法
     * @param id - 主键ID
     * @param merchantType - 卡商类型：1我方卡商，2第三方卡商
     * @param operateType - 卡商运营类型/运营性质：1代收，2代付
     * @param idList - 卡商主键ID集合
     * @param orderMoney - 订单金额
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.hz.cake.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/10/30 14:40
     */
    public static MerchantModel assembleMerchantQuery(long id, int merchantType, int operateType, List<Long> idList, String orderMoney, int useStatus){
        MerchantModel resBean = new MerchantModel();
        if (!StringUtils.isBlank(orderMoney)){
            BigDecimal bd = new BigDecimal(orderMoney);
            resBean.setMoney(bd);
        }
        if (id > 0){
            resBean.setId(id);
        }
        if (merchantType > 0){
            resBean.setMerchantType(merchantType);
        }
        if (operateType > 0){
            resBean.setOperateType(operateType);
        }
        if (idList != null && idList.size() > 0){
            resBean.setIdList(idList);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 获取上一次使用过的代付ID
     * <p>
     *     从缓存中获取上次给出码的代付ID
     *     数据来由：每次给出之后，把代付ID进行纪录
     * </p>
     * @param resourceType - 代付资源类型：1杉德支付，2金服支付
     * @return
     * @author yoko
     * @date 2020/5/21 15:38
     */
    public static long getMaxReplacePayRedis(int resourceType) throws Exception{
        String strKeyCache = CachedKeyUtils.getCacheKey(CacheKey.REPLACE_PAY, resourceType);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (!StringUtils.isBlank(strCache)) {
            return Long.parseLong(strCache);
        }else{
            return 0;
        }
    }


    /**
     * @Description: 组装查询代付的查询条件
     * @param merchantList - 卡商集合
     * @param orderMoney - 订单金额
     * @param resourceType - 代付资源类型：1杉德支付，2金服支付
     * @return com.hz.cake.master.core.model.replacepay.ReplacePayModel
     * @author yoko
     * @date 2021/6/21 11:05
     */
    public static ReplacePayModel assembleReplacePayQuery(List<MerchantModel> merchantList, String orderMoney, int resourceType){
        ReplacePayModel resBean = new ReplacePayModel();
        List<Long> merchantIdList = merchantList.stream().map(MerchantModel::getId).collect(Collectors.toList());
        resBean.setMerchantIdList(merchantIdList);
        BigDecimal bd = new BigDecimal(orderMoney);
        resBean.setMoney(bd);
        if (resourceType > 0){
            resBean.setResourceType(resourceType);
        }
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        return resBean;
    }


    /**
     * @Description: 代付集合排序
     * <p>
     *     排序方式：小于上次给过的代付ID放在集合的后面，大于上次给过的代付ID放集合的前面
     * </p>
     * @param replacePayList - 代付集合
     * @param maxreplacePayRuleId - 上次给出的代付ID
     * @return java.util.List<ReplacePayModel>
     * @author yoko
     * @date 2020/10/10 11:49
     */
    public static List<ReplacePayModel> sortReplacePayList(List<ReplacePayModel> replacePayList, long maxreplacePayRuleId){
        if (maxreplacePayRuleId > 0){
            List<ReplacePayModel> resList = new ArrayList<>();
            List<ReplacePayModel> noList = new ArrayList<>();// 没有给出过出码的代付集合
            List<ReplacePayModel> yesList = new ArrayList<>();// 有给出过出码的代付集合
            for (ReplacePayModel replacePayModel : replacePayList){
                if (replacePayModel.getId() > maxreplacePayRuleId){
                    noList.add(replacePayModel);
                }else {
                    yesList.add(replacePayModel);
                }
            }
            if (noList != null && noList.size() > 0){
                resList.addAll(noList);
            }
            if (yesList != null && yesList.size() > 0){
                resList.addAll(yesList);
            }
            return resList;
        }else {
            return replacePayList;
        }
    }




    /**
     * @Description: 组装更新代付订单信息
     * @param orderNo - 代付订单号
     * @param replacePayModel - 第三方资源信息表
     * @param merchantList - 卡商信息集合
     * @param serviceCharge - 手续费
     * @param outStatus - 代付订单出码状态:1初始化（我方处理默认初始化），2出码成功（第三方反馈结果），3出码失败
     * @param orderStatus - 订单状态：1初始化，2超时，3质疑，4成功
     * @param failInfo - 失败缘由
     * @return com.cake.task.master.core.model.order.OrderOutModel
     * @Author: yoko
     * @Date 2021/10/27 16:57
     */
    public static OrderOutModel assembleOrderOutUpdate(String orderNo, ReplacePayModel replacePayModel,
                                                    List<MerchantModel> merchantList, String serviceCharge, int outStatus,
                                                    int orderStatus, String failInfo){
        OrderOutModel resBean = new OrderOutModel();
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }else {
            return null;
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }

        if (!StringUtils.isBlank(serviceCharge)){
            resBean.setServiceCharge(serviceCharge);
        }

        if (replacePayModel != null && replacePayModel.getId() != null){
            // 三方代付信息
            resBean.setReplacePayId(replacePayModel.getId());
            resBean.setReplacePayName(replacePayModel.getAlias());


            // 卡商信息
            MerchantModel merchantModel = null;
            if (merchantList != null && merchantList.size() > 0){
                for (MerchantModel merchantData : merchantList){
                    if (merchantData.getId() == replacePayModel.getMerchantId()){
                        merchantModel = merchantData;
                    }
                }
                if (!StringUtils.isBlank(merchantModel.getAcName())){
                    resBean.setMerchantName(merchantModel.getAcName());
                }
                resBean.setMerchantId(merchantModel.getId());
            }
        }

        if (!StringUtils.isBlank(failInfo)){
            resBean.setFailInfo(failInfo);
        }
        return resBean;

    }

    /**
    * @Description: 组装根据代付订单号查询订单信息
    * @param orderNo - 代付订单号
    * @author: yoko
    * @date: 2021/11/30 15:28
    * @version 1.0.0
    */
    public static OrderOutModel assembleOrderOutQuery(String orderNo){
        OrderOutModel resBean = new OrderOutModel();
        resBean.setOrderNo(orderNo);
        return resBean;
    }


    /**
     * @Description: 判断时间是否是在当前时间范围内
     * @param openTimeSlot
     * @return
     * @author yoko
     * @date 2020/9/23 14:15
     */
    public static boolean checkOpenTimeSlot(String openTimeSlot){
        boolean flag = false;
        String[] strArr = openTimeSlot.split("#");
        for (String str : strArr){
            String[] str_ = str.split("-");
            boolean flag_ = DateUtil.isBelong(str_[0], str_[1]);
            if (flag_){
                return true;
            }
        }
        return flag;
    }


    /**
     * @Description: 组装变更卡商余额的方法
     * @param id - 卡商主键ID
     * @param type - 余额加减：1加，2减
     * @param money - 变更的金额值
     * @return com.hz.cake.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/10/30 19:57
     */
    public static MerchantModel assembleMerchantByChanagerBalance(long id, int type, String money){
        MerchantModel resBean = new MerchantModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (type > 0){
            if (type == 1){
                resBean.setAddBalance("1");
            }else if (type == 2){
                resBean.setSubtractBalance("1");
            }
        }
        if (!StringUtils.isBlank(money)){
            resBean.setOrderMoney(money);
        }
        return resBean;
    }


    /**
     * @Description: 组装添加流水或查询流水的方法
     * @param id - 主键ID
     * @param merchantId - 卡商ID
     * @param orderNo - 订单号
     * @param orderType - 订单类型：1代收，2代付
     * @param money - 订单金额
     * @param orderStatus - 订单状态：1初始化，2超时/失败，3有质疑，4成功，5表示订单超时且操作状态属于初始化的
     * @param delayTime - 延迟运行时间：当订单属于超时状态：则系统时间需要大于此时间才能进行逻辑操作
     * @param lockTime - 锁定时间
     * @param type - 操作类型：1查询，2添加数据
     * @return com.hz.cake.master.core.model.merchant.MerchantBalanceDeductModel
     * @author yoko
     * @date 2020/10/30 20:21
     */
    public static MerchantBalanceDeductModel assembleMerchantBalanceDeduct(long id, long merchantId, String orderNo, int orderType, String money, int orderStatus,
                                                                           String delayTime, String lockTime, int type){
        MerchantBalanceDeductModel resBean = new MerchantBalanceDeductModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(money)){
            resBean.setMoney(money);
        }
        if (!StringUtils.isBlank(delayTime)){
            resBean.setDelayTime(delayTime);
        }
        if (!StringUtils.isBlank(lockTime)){
            resBean.setLockTime(lockTime);
        }
        if (type > 0){
            if (type == 2){
                resBean.setCurday(DateUtil.getDayNumber(new Date()));
                resBean.setCurhour(DateUtil.getHour(new Date()));
                resBean.setCurminute(DateUtil.getCurminute(new Date()));
            }
        }
        return resBean;
    }


    /**
     * @Description: 组装查询卡商绑定渠道的手续费的方法
     * @param id - 主键ID
     * @param merchantId - 卡商ID
     * @param channelId - 渠道ID
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.hz.cake.master.core.model.merchant.MerchantServiceChargeModel
     * @author yoko
     * @date 2020/11/26 15:04
     */
    public static MerchantServiceChargeModel assembleMerchantServiceChargeQuery(long id, long merchantId, long channelId, int useStatus){
        MerchantServiceChargeModel resBean = new MerchantServiceChargeModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }



    public static void main(String [] args) throws Exception{
    }




    

}
