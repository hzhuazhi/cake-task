package com.cake.task.master.core.common.utils.zhongbangpay;

import com.alibaba.fastjson.JSON;
import com.cake.task.master.core.common.utils.zhongbangpay.model.response.TransferResponse;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @author yoko
 * @desc 众邦API
 * @create 2022-07-05 9:58
 **/
public class ZhongBangApi {
    private static Logger log = LoggerFactory.getLogger(ZhongBangApi.class);



    /**
     * @Description: 众邦-拉单
     * @param replacePayModel - 众邦的基本信息
     * @param orderOutModel - 代付订单信息
     * @return: void
     * @author: yoko
     * @date: 2021/11/5 16:36
     * @version 1.0.0
     */
    public static TransferResponse transfer(ReplacePayModel replacePayModel, OrderOutModel orderOutModel, String payee_bank_id) throws Exception{
        // 组装数据
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_id", replacePayModel.getBusinessNum());// 应用ID
        paramMap.put("trx_id", orderOutModel.getOrderNo());// 交易流水号
        paramMap.put("service", "transfer");// 接口名称:transfer
        paramMap.put("trx_amt", orderOutModel.getOrderMoney());// 交易金额（元）
        paramMap.put("payee_acct_id", orderOutModel.getInBankCard());// 收款人的银行卡
        paramMap.put("payee_nm", orderOutModel.getInAccountName());// 收款人名称
        paramMap.put("payee_bank_id", payee_bank_id);// 收款银行行号：参照支持银行附件
        paramMap.put("payee_bank_nm", orderOutModel.getInBankName());// 收款行银行名称
        //待签名字符串
        String waitSign = SecurityUtil.doStringToSign(paramMap);
        log.info("-----ZhongBangApi.transfer().waitSign:" + waitSign);
        //计算签名
        String sign = SecurityUtil.sign(waitSign, replacePayModel.getPrivateKey());
        paramMap.put("sign", sign);// 收款行银行名称
        TransferResponse transferResponse = SecurityUtil.sendPost(replacePayModel.getInInterfaceAds(), paramMap);
        log.info("-----ZhongBangApi.transfer().transferResponse:" + JSON.toJSONString(transferResponse));
        return transferResponse;
    }



    /**
     * @Description: 众邦-查询余额
     * @param replacePayModel - 众邦的基本信息
     * @return: void
     * @author: yoko
     * @date: 2021/11/5 16:36
     * @version 1.0.0
     */
    public static TransferResponse queryBalance(ReplacePayModel replacePayModel) throws Exception{
        // 组装数据
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_id", replacePayModel.getBusinessNum());// 应用ID
        paramMap.put("trx_id", String.valueOf(System.currentTimeMillis()));// 交易流水号
        paramMap.put("service", "account.balance.query");// 接口名称:account.balance.query
        //待签名字符串
        String waitSign = SecurityUtil.doStringToSign(paramMap);
        log.info("-----ZhongBangApi.queryBalance().waitSign:" + waitSign);
        //计算签名
        String sign = SecurityUtil.sign(waitSign, replacePayModel.getPrivateKey());
        paramMap.put("sign", sign);// 收款行银行名称
        TransferResponse transferResponse = SecurityUtil.sendPost(replacePayModel.getInInterfaceAds(), paramMap);
        log.info("-----ZhongBangApi.queryBalance().transferResponse:" + JSON.toJSONString(transferResponse));
        return transferResponse;
    }



    /**
     * @Description: 众邦-加白名单
     * @param replacePayModel - 众邦的基本信息
     * @param orderOutModel - 代付订单信息
     * @return: void
     * @author: yoko
     * @date: 2021/11/5 16:36
     * @version 1.0.0
     */
    public static TransferResponse addWhitelist(ReplacePayModel replacePayModel, OrderOutModel orderOutModel) throws Exception{
        // 组装数据
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("app_id", replacePayModel.getBusinessNum());// 应用ID
        paramMap.put("trx_id", String.valueOf(System.currentTimeMillis()));// 交易流水号
        paramMap.put("service", "transfer.whitelist.add");// 接口名称:transfer.whitelist.add
        //待签名字符串
        String waitSign = SecurityUtil.doStringToSign(paramMap);
        log.info("-----ZhongBangApi.addWhitelist().waitSign:" + waitSign);
        //计算签名
        String sign = SecurityUtil.sign(waitSign, replacePayModel.getPrivateKey());
        paramMap.put("sign", sign);// 收款行银行名称
        TransferResponse transferResponse = SecurityUtil.sendPost(replacePayModel.getInInterfaceAds(), paramMap);
        log.info("-----ZhongBangApi.addWhitelist().transferResponse:" + JSON.toJSONString(transferResponse));
        return transferResponse;
    }


    public static void main(String [] args) throws Exception{
        ReplacePayModel replacePayModel = new ReplacePayModel();
        replacePayModel.setBusinessNum("121214121");
        replacePayModel.setPrivateKey("121/ivWpFKzT3VV5X2YyE4DxiiPM4MdjTj+12+14SOpj//z8dGHhNt5/1212/FPaH4H5NW/Fag5gKNW/121/d+29CyYj2+121/SK/1212+Gn9uFvgcnv/121+112+hmknF239Tl+121/121/s/ECM0NrfK4rARwswpsCv9UmmiDwFZ+121+Z6OVdtwM8cLsem68TWYXrn750qtS0jcWyHFHzfRcdSedxi0N9bAhoM8l+z+DPRCifj+AHa5u2/PhZ1okqzNu6XQTzqpMHiBmUr1g8zkSwKBgQC79is1NocCKDLwAn8S5a/W4LY5A5IK3HEOTLM4fZjm9xqR8N7eULecot6swTK/y1U6jG33odJRD5CtA440Q/hHKUo+5sy6yf62yBHesyZI80rIYk33m8SHmWo0PdxRHsQlzMEDQd+8yPncb4imsxqQj+KBbLe+g1myfMoAdlLM5QKBgAxjC8XnDOaTer8fKxKmssMRyNMvwtx30aEnCAXkmPRyEM85bRTgdetMgNzgpdZQ+i6BnOgKoRE3jWuPSMaCFsc27kDuJ8U74mbb5dRfLURK9mxX/9U9yIJ4HujNXOCyqxgD3FEKkUCTMQAdIxTyAXr+YXHDYWaqjRjBmXmePPQ1AoGAYKto6YNTyG44VGxUQrnSx+bmkUge0msx0jLscf9WpmMsTSbe2OQWqv6xG5R5r166RNR4skWNz5b7x5/ugT778E1yHAvD+8VrtUIMvsbQx/Ao5Ap7IwAAAeWLU7cFGsuCAhbepfg5Q/TxXMgYNfQjk3oM+peZoOgZN80qbzZru2kCgYEAvd5MUGUufhCgIPhugEwZr+YqoP3aOdU13Ji2rPgMsit/nEeXNyNs6ltsDuVfQOM3bZTRUWIf3xVSlc40ELbLljXki1FXxWo9oGyntjPsMeTk/SyZ/dZQg2UqaNR5z8bqlMraqCpaU0eTIosquUcmqnOZwBsglMK6R8jg5G1oLwI=");
        replacePayModel.setInInterfaceAds("https://eais.yijieb.com/service/gateway.html");
        queryBalance(replacePayModel);
    }



}
