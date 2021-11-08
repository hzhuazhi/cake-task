package com.cake.task.master.core.common.utils.huichaogpay.model.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author yoko
 * @desc 余额-查询-实体属性Bean
 * @create 2021-11-08 12:25
 **/
@XStreamAlias("CheckBalanceResponse")
public class CheckBalanceResponse {
//    public BalanceData CheckBalanceResponse;// 账户余额详情

    @XStreamAlias("respCode")
    public String respCode;// 状态码-0000：请求成功，其它表示请求失败

    @XStreamAlias("respMsg")
    public String respMsg;// 查询状态描述

    @XStreamAlias("availableBalance")
    public String availableBalance;// 可用余额

    @XStreamAlias("unSettleBalance")
    public String unSettleBalance;// 未结算余额

    @XStreamAlias("requestTime")
    public String requestTime;// 请求时间 格式：yyyyMMdd HHmmss
    public CheckBalanceResponse(){

    }

    public String getRespCode() {
        return respCode;
    }

    public void setRespCode(String respCode) {
        this.respCode = respCode;
    }

    public String getRespMsg() {
        return respMsg;
    }

    public void setRespMsg(String respMsg) {
        this.respMsg = respMsg;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public String getUnSettleBalance() {
        return unSettleBalance;
    }

    public void setUnSettleBalance(String unSettleBalance) {
        this.unSettleBalance = unSettleBalance;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
