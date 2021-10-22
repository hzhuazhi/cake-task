package com.cake.task.master.core.common.utils.jinfupay.model;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 金服支付响应的第二层result的实体属性Bean
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class ResultResponse extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223271105L;

    public ResultResponse(){

    }

    public String order_id;// 上游订单号
    public String pay_fund_order_id;// 上游资金流水号
    public String pay_date;// 支付时间
    public String arrival_time_end;// 到达时间
    public String trans_amount;// 交易金额
    public String status;// 交易状态：SUCCESS  成功，FAIL  失败，DEALING  处理中，REFUND   退票


    public String balance;// 余额
    public String quota;// 充值的总金额
    public String use_amount;// 已使用完的金额

    public String getPay_date() {
        return pay_date;
    }

    public void setPay_date(String pay_date) {
        this.pay_date = pay_date;
    }

    public String getArrival_time_end() {
        return arrival_time_end;
    }

    public void setArrival_time_end(String arrival_time_end) {
        this.arrival_time_end = arrival_time_end;
    }

    public String getTrans_amount() {
        return trans_amount;
    }

    public void setTrans_amount(String trans_amount) {
        this.trans_amount = trans_amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getQuota() {
        return quota;
    }

    public void setQuota(String quota) {
        this.quota = quota;
    }

    public String getUse_amount() {
        return use_amount;
    }

    public void setUse_amount(String use_amount) {
        this.use_amount = use_amount;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPay_fund_order_id() {
        return pay_fund_order_id;
    }

    public void setPay_fund_order_id(String pay_fund_order_id) {
        this.pay_fund_order_id = pay_fund_order_id;
    }
}
