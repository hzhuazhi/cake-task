package com.cake.task.master.core.common.utils.huichaogpay.model.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author yoko
 * @desc 查单-结果-订单详情-实体Bean
 * @create 2021-11-08 11:37
 **/
public class TransferQuery {
    @XStreamAlias("mertransferID")
    public String mertransferID;// 下发订单号:商户下发订单号

    @XStreamAlias("amount")
    public String amount;// 订单金额

    @XStreamAlias("state")
    public String state;// 状态码- 成功：00，失败：11，处理中：22

    @XStreamAlias("date")
    public String date;// 订单时间 格式：yyyy-MM-dd HH:mm:ss

    @XStreamAlias("memo")
    public String memo;// 备注
    public TransferQuery(){

    }

    public String getMertransferID() {
        return mertransferID;
    }

    public void setMertransferID(String mertransferID) {
        this.mertransferID = mertransferID;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }
}
