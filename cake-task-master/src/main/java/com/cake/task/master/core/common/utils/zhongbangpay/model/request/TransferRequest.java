package com.cake.task.master.core.common.utils.zhongbangpay.model.request;

import java.util.List;

/**
 * @author yoko
 * @desc 众邦转账实体属性Bean
 * @create 2022-07-04 15:14
 **/
public class TransferRequest {

    private String app_id;// 应用ID
    private String trx_id;// 交易流水号/订单号
    private String service;// 服务名：接口名称，transfer
    private String trx_amt;// 交易金额，交易金额（元）
    private String payee_acct_id;// 收款人账号：收款人的银行卡
    private String payee_nm;// 收款人名称/收款人
    private String payee_bank_id;// 收款人银行行号：1021000999 96
    private String payee_bank_nm;// 收款人名称：中国工商银行
    private String desc;// 交易描述
    private String sign;// 签名值

    private List<WhitelistRequest> list;// 白名单列表

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getTrx_id() {
        return trx_id;
    }

    public void setTrx_id(String trx_id) {
        this.trx_id = trx_id;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getTrx_amt() {
        return trx_amt;
    }

    public void setTrx_amt(String trx_amt) {
        this.trx_amt = trx_amt;
    }

    public String getPayee_acct_id() {
        return payee_acct_id;
    }

    public void setPayee_acct_id(String payee_acct_id) {
        this.payee_acct_id = payee_acct_id;
    }

    public String getPayee_nm() {
        return payee_nm;
    }

    public void setPayee_nm(String payee_nm) {
        this.payee_nm = payee_nm;
    }

    public String getPayee_bank_id() {
        return payee_bank_id;
    }

    public void setPayee_bank_id(String payee_bank_id) {
        this.payee_bank_id = payee_bank_id;
    }

    public String getPayee_bank_nm() {
        return payee_bank_nm;
    }

    public void setPayee_bank_nm(String payee_bank_nm) {
        this.payee_bank_nm = payee_bank_nm;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }


    public List<WhitelistRequest> getList() {
        return list;
    }

    public void setList(List<WhitelistRequest> list) {
        this.list = list;
    }
}
