package com.cake.task.master.core.common.utils.zhongbangpay.model.response;

/**
 * @author yoko
 * @desc 众邦转账返回的实体Bean
 * @create 2022-07-04 15:39
 **/
public class TransferResponse {

    private String app_id;// 应用ID
    private String trx_id;// 交易流水号/订单号
    private String service;// 服务名：接口名称，transfer
    private String sign;// 签名值
    private String return_code;// 返回码：正常返回SUCCESS
    private String return_message;// 返回信息
    private String balance;// 账户余额(元)
    private String freeze_amt;// 账户冻结金额(元)

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

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_message() {
        return return_message;
    }

    public void setReturn_message(String return_message) {
        this.return_message = return_message;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getFreeze_amt() {
        return freeze_amt;
    }

    public void setFreeze_amt(String freeze_amt) {
        this.freeze_amt = freeze_amt;
    }
}
