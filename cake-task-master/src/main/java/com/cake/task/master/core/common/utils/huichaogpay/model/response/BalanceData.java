package com.cake.task.master.core.common.utils.huichaogpay.model.response;

/**
 * @author yoko
 * @desc 账户余额详情
 * @create 2021-11-08 12:26
 **/
public class BalanceData {
    public String respCode;// 状态码-0000：请求成功，其它表示请求失败
    public String respMsg;// 查询状态描述
    public String availableBalance;// 可用余额
    public String unSettleBalance;// 未结算余额
    public String requestTime;// 请求时间 格式：yyyyMMdd HHmmss
    public BalanceData(){

    }


}
