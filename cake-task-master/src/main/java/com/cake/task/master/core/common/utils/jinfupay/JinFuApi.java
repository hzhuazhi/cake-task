package com.cake.task.master.core.common.utils.jinfupay;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.HttpUtil;
import com.cake.task.master.core.common.utils.jinfupay.model.JinFuPayResponse;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName:
 * @Description: 金服支付API
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class JinFuApi {
    private static Logger log = LoggerFactory.getLogger(JinFuApi.class);


    /**
     * @Description: 金服代付-拉单
     * @param replacePayModel - 金服代付的基本信息
     * @param orderOutModel - 代付订单信息
     * @return
     * @Author: yoko
     * @Date 2021/10/21 16:47
    */
    public static JinFuPayResponse jinFuPay(ReplacePayModel replacePayModel, OrderOutModel orderOutModel) throws Exception{
        Map<String ,String> sendDataMap = new HashMap<>();
        sendDataMap.put("order_no", orderOutModel.getOrderNo());
        sendDataMap.put("amount", orderOutModel.getOrderMoney());
        sendDataMap.put("bankCrad", orderOutModel.getInBankCard());
        sendDataMap.put("cardholderName", orderOutModel.getInAccountName());
        sendDataMap.put("companyCode", replacePayModel.getBusinessNum());
        sendDataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        String mySign = SecurityUtil.sign(replacePayModel.getPublicKey(), SecurityUtil.map2str(sendDataMap));
        log.info("--------JinFuApi.jinFuPay--------mySign:" + mySign);
        sendDataMap.put("sign", mySign);
        String parameter = JSON.toJSONString(sendDataMap);
        log.info("JinFuApi.jinFuPay-----------parameter:" + parameter);
        String resData = HttpUtil.doPostJson(replacePayModel.getOutInterfaceAds(), parameter);
        log.info("----JinFuApi.jinFuPay----resData:" + resData);
//        resData = "{\"code\":0,\"count\":0,\"data\":null,\"message\":null,\"msg\":\"请求成功!\",\"result\":null,\"status\":0}";
        JinFuPayResponse result = JSON.parseObject(resData, JinFuPayResponse.class);
        return result;
    }


    /**
     * @Description: 金服代付-查单
     * @param replacePayModel - 金服代付的基本信息
     * @param orderNo - 代付订单号
     * @return
     * @Author: yoko
     * @Date 2021/10/21 16:47
     */
    public static JinFuPayResponse jinFuQueryOrder(ReplacePayModel replacePayModel, String orderNo) throws Exception{
        Map<String ,String> sendDataMap = new HashMap<>();
        sendDataMap.put("order_no", orderNo);
        sendDataMap.put("companyCode", replacePayModel.getBusinessNum());
        sendDataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        String mySign = SecurityUtil.sign(replacePayModel.getPublicKey(), SecurityUtil.map2str(sendDataMap));
        sendDataMap.put("sign", mySign);
        String parameter = JSON.toJSONString(sendDataMap);
        String resData = HttpUtil.doPostJson(replacePayModel.getInInterfaceAds(), parameter);
        log.info("----JinFuApi.jinFuQueryOrder----resData:" + resData);
        JinFuPayResponse result = JSON.parseObject(resData, JinFuPayResponse.class);
        return result;
    }


    /**
     * @Description: 金服代付-查余额
     * @param replacePayModel - 金服代付的基本信息
     * @return
     * @Author: yoko
     * @Date 2021/10/21 16:47
     */
    public static JinFuPayResponse jinFuQueryBalance(ReplacePayModel replacePayModel) throws Exception{
        Map<String ,String> sendDataMap = new HashMap<>();
        sendDataMap.put("companyCode", replacePayModel.getBusinessNum());
        sendDataMap.put("timestamp", String.valueOf(System.currentTimeMillis()));
        String mySign = SecurityUtil.sign(replacePayModel.getPublicKey(), SecurityUtil.map2str(sendDataMap));
        sendDataMap.put("sign", mySign);
        String parameter = JSON.toJSONString(sendDataMap);
        String resData = HttpUtil.doPostJson(replacePayModel.getInInterfaceAds(), parameter);
        log.info("----JinFuApi.jinFuQueryBalance----resData:" + resData);
        JinFuPayResponse result = JSON.parseObject(resData, JinFuPayResponse.class);
        return result;
    }

}
