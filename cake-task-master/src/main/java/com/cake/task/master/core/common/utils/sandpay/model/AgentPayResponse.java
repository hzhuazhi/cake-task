package com.cake.task.master.core.common.utils.sandpay.model;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 衫德支付-代付返回的实体属性Bean
 * @Author yoko
 * @Date 2021/6/10 16:15
 * @Version 1.0
 */
public class AgentPayResponse extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201105L;

    public AgentPayResponse(){

    }

    public String respCode;// 响应码

    public String respDesc;// 响应描述

    public String tranTime;// 交易时间

    public String orderCode;// 订单号

    public String origRespCode;// 原交易响应码

    public String origRespDesc;// 原交易响应描述

    public String resultFlag;// 结果状态 0-  成功,1-  失败,2-  处理中(等银行返回明确结果)

    public String sandSerial;// 杉德系统流水号

    public String tranDate;// 交易日期

    public String tranFee;// 手续费:付款成功才返回，单位为分

    public String extraFee;// 额外手续费:配资付款成功才返回，单位为分

    public String holidayFee;// 节假日手续费:配资付款成功才返回,单位为分

    public String reqReserved;// 请求方保留域

    public String exchangeRefundStatus;// 退汇标识:1表示有退汇

    public String exchangeRefundReason;// 退汇描述:示例;20210507/账号户名不符

    public String balance;// 余额

    public String creditAmt;// 可用额度

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespDesc() {
        return respDesc;
    }

    public void setRespDesc(String respDesc) {
        this.respDesc = respDesc;
    }

    public String getTranTime() {
        return tranTime;
    }

    public void setTranTime(String tranTime) {
        this.tranTime = tranTime;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrigRespCode() {
        return origRespCode;
    }

    public void setOrigRespCode(String origRespCode) {
        this.origRespCode = origRespCode;
    }

    public String getOrigRespDesc() {
        return origRespDesc;
    }

    public void setOrigRespDesc(String origRespDesc) {
        this.origRespDesc = origRespDesc;
    }

    public String getResultFlag() {
        return resultFlag;
    }

    public void setResultFlag(String resultFlag) {
        this.resultFlag = resultFlag;
    }

    public String getSandSerial() {
        return sandSerial;
    }

    public void setSandSerial(String sandSerial) {
        this.sandSerial = sandSerial;
    }

    public String getTranDate() {
        return tranDate;
    }

    public void setTranDate(String tranDate) {
        this.tranDate = tranDate;
    }

    public String getTranFee() {
        return tranFee;
    }

    public void setTranFee(String tranFee) {
        this.tranFee = tranFee;
    }

    public String getExtraFee() {
        return extraFee;
    }

    public void setExtraFee(String extraFee) {
        this.extraFee = extraFee;
    }

    public String getHolidayFee() {
        return holidayFee;
    }

    public void setHolidayFee(String holidayFee) {
        this.holidayFee = holidayFee;
    }

    public String getReqReserved() {
        return reqReserved;
    }

    public void setReqReserved(String reqReserved) {
        this.reqReserved = reqReserved;
    }

    public String getExchangeRefundStatus() {
        return exchangeRefundStatus;
    }

    public void setExchangeRefundStatus(String exchangeRefundStatus) {
        this.exchangeRefundStatus = exchangeRefundStatus;
    }

    public String getExchangeRefundReason() {
        return exchangeRefundReason;
    }

    public void setExchangeRefundReason(String exchangeRefundReason) {
        this.exchangeRefundReason = exchangeRefundReason;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCreditAmt() {
        return creditAmt;
    }

    public void setCreditAmt(String creditAmt) {
        this.creditAmt = creditAmt;
    }
}
