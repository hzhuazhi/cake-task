package com.cake.task.master.core.common.utils.huichaogpay;

import com.cake.task.master.core.common.utils.HttpUtil;
import com.cake.task.master.core.common.utils.huichaogpay.model.request.QueryBalanceRequest;
import com.cake.task.master.core.common.utils.huichaogpay.model.request.TransferList;
import com.cake.task.master.core.common.utils.huichaogpay.model.request.TransferQueryRequest;
import com.cake.task.master.core.common.utils.huichaogpay.model.request.TransferRequest;
import com.cake.task.master.core.common.utils.huichaogpay.model.response.CheckBalanceResponse;
import com.cake.task.master.core.common.utils.huichaogpay.model.response.TransferQueryResponse;
import com.cake.task.master.core.common.utils.huichaogpay.model.response.TransferResponse;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yoko
 * @desc 汇潮支付-api
 * @create 2021-11-05 16:30
 **/
public class HuiChaoApi {
    private static Logger log = LoggerFactory.getLogger(HuiChaoApi.class);


    /**
     * @Description: 汇潮-拉单
     * @param replacePayModel - 汇潮的基本信息
     * @param orderOutModel - 代付订单信息
     * @return: void
     * @author: yoko
     * @date: 2021/11/5 16:36
     * @version 1.0.0
     */
    public static TransferResponse transferFixed(ReplacePayModel replacePayModel, OrderOutModel orderOutModel) throws Exception{
        TransferRequest dto = new TransferRequest();
        TransferList list = new TransferList();
        dto.setAccountNumber(replacePayModel.getBusinessNum());//麻袋数字账号-商户号
        dto.setNotifyURL(replacePayModel.getInInterfaceAds());// 订单异步通知(回调)地址  外网能正常访问的都可以
        dto.setTt("0");// 加急 普通 结果差异不大，不建议选加急
        dto.setSignType("RSA");//加密方式
        dto.setTransferList(list);
        list.setTransId(orderOutModel.getOrderNo());// 代付请求流水号
        list.setBankCode(orderOutModel.getInBankName());// 尽量传全称 不要传XXXXXX股份有限公司，中国工商银行 工商银行这种都可以，不要传中国工商银行股份有限公司。*广发银行*是特例，只能传这个。
        list.setProvice("北京市");//省市建议 固定传北京市
        list.setCity("北京市");//省市建议 固定传北京市
        list.setBranchName("");//支行 非必传
        list.setAccountName(orderOutModel.getInAccountName());// 银行卡预留姓名
        list.setCardNo(orderOutModel.getInBankCard());//银行卡号
        list.setIdNo("");//身份证号
        list.setPhone("");//银行卡预留手机号
        list.setAmount(orderOutModel.getOrderMoney());//最低1元 金额的单位都是元。
        list.setRemark("备注");//备注信息

        // 获取数据组装成的xml字符串
        String xml = HuiChaoMethod.transferXML(dto, replacePayModel.getPublicKey());
        Map<String, String> sendDataMap = new HashMap<>();
        sendDataMap.put("transData", xml);
        String resData = HttpUtil.doPost(replacePayModel.getOutInterfaceAds(), sendDataMap);
        log.info("base64.transferFixed.resData:" + resData);
        resData = new String(Base64.decodeBase64(resData), "UTF-8");
        log.info("transferFixed----------------resData:" + resData);

        TransferResponse result = null;
        if (!StringUtils.isBlank(resData)){
            // 将xml字符串转换成实体Bean
            result = HuiChaoMethod.convertXmlStrToObjectww(TransferResponse.class, resData);
            return result;
        }else {
            return null;
        }
    }


    /**
     * @Description: 汇潮-查单
     * @param replacePayModel - 汇潮基本信息
     * @param orderNo - 订单号
     * @return: void
     * @author: yoko
     * @date: 2021/11/5 20:50
     * @version 1.0.0
     */
    public static TransferQueryResponse transferQueryFixed(ReplacePayModel replacePayModel, String orderNo) throws Exception {
        TransferQueryRequest dto = new TransferQueryRequest();
        dto.setMerchantNumber(replacePayModel.getBusinessNum());//麻袋数字账号-商户号
        dto.setMertransferID(orderNo);//代付请求流水号
        dto.setSignType("RSA");//
        dto.setQueryTimeBegin("");//格式：yyyy-MM-dd HH:mm:ss 一般只查单笔订单，所以 时间不需要传
        dto.setQueryTimeEnd("");//格式：yyyy-MM-dd HH:mm:ss  一般只查单笔订单，所以 时间不需要传
        dto.setRequestTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));//必传   格式：yyyyMMddhhmmss

        // 获取数据组装成的xml字符串
        String xml = HuiChaoMethod.transferQueryXML(dto, replacePayModel.getPublicKey());
        Map<String, String> sendDataMap = new HashMap<>();
        sendDataMap.put("requestDomain", xml);
        String resData = HttpUtil.doPost(replacePayModel.getInInterfaceAds(), sendDataMap);
        log.info("base64.transferQueryFixed.resData:" + resData);
        resData = new String(Base64.decodeBase64(resData), "UTF-8");
        log.info("transferQueryFixed----------------resData:" + resData);
//        resData = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><yemadai><code>0000</code><transfer><mertransferID>HC202111301234231</mertransferID><amount>1.00</amount><state>00</state><date>2021-11-05 18:37:38</date><memo>成功</memo></transfer></yemadai>";
        TransferQueryResponse result = null;
        if (!StringUtils.isBlank(resData)){
            // 将xml字符串转换成实体Bean
            result = HuiChaoMethod.convertXmlStrToObjectww(TransferQueryResponse.class, resData);
            return result;
        }else {
            return null;
        }
    }

    /**
    * @Description: 汇潮-查余额
    * @param replacePayModel - 汇潮基本信息
    * @return:
    * @author: yoko
    * @date: 2021/11/5 21:03
    * @version 1.0.0
    */
    public static CheckBalanceResponse queryBalance(ReplacePayModel replacePayModel) throws Exception {
        QueryBalanceRequest dto = new QueryBalanceRequest();
        dto.setMerNo(replacePayModel.getBusinessNum());
        dto.setRequestTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));
        dto.setSignType("RSA");
        // 获取数据组装成的xml字符串
        String xml = HuiChaoMethod.queryBalanceXML(dto, replacePayModel.getPublicKey());
        Map<String, String> sendDataMap = new HashMap<>();
        sendDataMap.put("requestDomain", xml);
        String resData = HttpUtil.doPost(replacePayModel.getBalanceInterfaceAds(), sendDataMap);
        log.info("base64.queryBalance.resData:" + resData);
//        resData = new String(Base64.decodeBase64(resData), "UTF-8");
//        log.info("queryBalance----------------resData:" + resData);
        CheckBalanceResponse result = null;
        if (!StringUtils.isBlank(resData)){
            // 将xml字符串转换成实体Bean
            result = HuiChaoMethod.convertXmlStrToObjectww(CheckBalanceResponse.class, resData);
            return result;
        }else {
            return null;
        }
    }



    public static void main(String [] args) throws Exception{
        ReplacePayModel replacePayModel = new ReplacePayModel();
        replacePayModel.setBusinessNum("15165511");
//        replacePayModel.setInInterfaceAds("http://www.baidu.com");
        replacePayModel.setInInterfaceAds("https://gwapi.yemadai.com/transfer/transferQueryFixed");
        replacePayModel.setOutInterfaceAds("https://gwapi.yemadai.com/transfer/transferFixed");
        replacePayModel.setBalanceInterfaceAds("https://gwapi.yemadai.com/checkBalance");
        replacePayModel.setPublicKey("MIICdQIBADANB111gkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIO00l3ZGn8HF9wRnhutpXv707TBJFWhFMsWSFOkVTUl9nBS5v8sVsVwMFYgmE2RrRrYAnLtSgH2da+IZCPHwaI3BpsUN2HvAv7bZhngCczEikR7I9YHxtKjR8QIWQW9aDfQlcPmMNvNSCcPlScxSqmx5ZspPhSNUSCUiNS+56O3AgMBAAECgYA8FBTN+IXMoiixG1w7Fffh2ZrV3jC72tHIXi658MFpkBqdXEPA7LHcOHPkJdQzthr3nsdnM3TJ9mnym03KwIlDziYvrkf8hwSNSE5Ov9bAz38IlqgSHvBfYjnmOZ5GlvmF2kYXq5yUGpb08iUFpux4FwsAX2ePbW1EUGzbeqofQQJBAPVmJtalVWnfc8QwO1AqiAzLSXDNemeVLZ2h5taFQrbVjJulYbfwGeXro9TWSF66bfV5yRUyvFT0yyE9fqFMytcCQQCJZVoB0ZCILZpGTbWK4k2G+rV9609YjJEonAVcVMZYmZBPqmEEwvk2c5g+MmZR9WrQDCgIhMhDNodtrxDByLIhAkBrclG5E+UlGSCgGxotTKILMAs059MbfXbemR/wd6KxWSlakPwLRIaiZB10uGoxh+FTZQKFVJSfghtar4k9aNk7AkBEA3Hs2IqNftWR28H0gFYUbWMOdD+Q+/SXf7R/ok+VaF8xsIvaSZIITGyezWAtTimT15CxttlTvFiKCVFoPCtBAkActYlolXcT6rf2ZRNbjKf7XZDhJZxfQHYiXztYDPjjB5FSDtIyjIaNHSbCbrk1qWWkSDDka+iXN4VAR7R16rEg");


        OrderOutModel orderOutModel = new OrderOutModel();
        orderOutModel.setOrderNo(String.valueOf(System.currentTimeMillis()));
        log.info("orderNo:" + orderOutModel.getOrderNo());
        orderOutModel.setInBankName("中国银行");
        orderOutModel.setInAccountName("张三");
        orderOutModel.setInBankCard("kh123456789");
        orderOutModel.setOrderMoney("500.00");



        transferFixed(replacePayModel, orderOutModel);

//        transferQueryFixed(replacePayModel,"1636108657064");
//        queryBalance(replacePayModel);
    }

}
