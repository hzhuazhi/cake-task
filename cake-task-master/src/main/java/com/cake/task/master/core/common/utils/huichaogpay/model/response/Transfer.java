package com.cake.task.master.core.common.utils.huichaogpay.model.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author yoko
 * @desc 拉单-结果-订单详情-实体Bean
 * @create 2021-11-08 11:22
 **/
//@XStreamAlias("transferList")
public class Transfer {
    @XStreamAlias("resCode")
    public String resCode;// 0000：请求成功 / 请求成功，其它表示失败

    @XStreamAlias("transId")
    public String transId;// 商户订单号

    @XStreamAlias("accountName")
    public String accountName;// 开户人

    @XStreamAlias("cardNo")
    public String cardNo;// 银行卡

    @XStreamAlias("amount")
    public String amount;// 订单金额

    @XStreamAlias("remark")
    public String remark;// 备注

    @XStreamAlias("secureCode")
    public String secureCode;// 签名信息

    public Transfer(){

    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSecureCode() {
        return secureCode;
    }

    public void setSecureCode(String secureCode) {
        this.secureCode = secureCode;
    }
}
