package com.cake.task.master.core.common.utils.zhongbangpay.model.response;

import java.util.List;

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


    private String trx_send_tm;// 交易发送银行的时间
    private String trx_tm;// 交易时间：银行记账时间 “YYYY-MM-DD HH24:MI:SS”
    private String bank_trx_id;// 银行流水号：银行记账流水号
    private String trx_amt;// 交易金额(元)
    private String charge_amt;// 收费金额:服务费金额(元)
    private String status;// 交易状态: SUCCESS:成功 FAIL:失败 PROCESSING:处理中
    private String status_desc;// 状态描述: 订单交易状态说明


    private String serial_no;// 白名单序号
    private String real_name;// 用户姓名
    private String cert_no;// 身份证号
    private String mobile_no;// 手机号
    private String bank_account_no;// 银行卡号
    private String add_status;// 添加状态 WAIT_ADD:待添加； ADD_PROCESSING：处 理中； ADD_SUCCESS：添加 成功; ADD_FAIL:添加失败
    private String description;// 说明


    private String result_code;// 返回状态码
    private String result_message;// 返回提示数据

    private List<WhiteListData> whiteList_data;



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

    public String getTrx_send_tm() {
        return trx_send_tm;
    }

    public void setTrx_send_tm(String trx_send_tm) {
        this.trx_send_tm = trx_send_tm;
    }

    public String getTrx_tm() {
        return trx_tm;
    }

    public void setTrx_tm(String trx_tm) {
        this.trx_tm = trx_tm;
    }

    public String getBank_trx_id() {
        return bank_trx_id;
    }

    public void setBank_trx_id(String bank_trx_id) {
        this.bank_trx_id = bank_trx_id;
    }

    public String getTrx_amt() {
        return trx_amt;
    }

    public void setTrx_amt(String trx_amt) {
        this.trx_amt = trx_amt;
    }

    public String getCharge_amt() {
        return charge_amt;
    }

    public void setCharge_amt(String charge_amt) {
        this.charge_amt = charge_amt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus_desc() {
        return status_desc;
    }

    public void setStatus_desc(String status_desc) {
        this.status_desc = status_desc;
    }

    public String getSerial_no() {
        return serial_no;
    }

    public void setSerial_no(String serial_no) {
        this.serial_no = serial_no;
    }

    public String getReal_name() {
        return real_name;
    }

    public void setReal_name(String real_name) {
        this.real_name = real_name;
    }

    public String getCert_no() {
        return cert_no;
    }

    public void setCert_no(String cert_no) {
        this.cert_no = cert_no;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getBank_account_no() {
        return bank_account_no;
    }

    public void setBank_account_no(String bank_account_no) {
        this.bank_account_no = bank_account_no;
    }

    public String getAdd_status() {
        return add_status;
    }

    public void setAdd_status(String add_status) {
        this.add_status = add_status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResult_code() {
        return result_code;
    }

    public void setResult_code(String result_code) {
        this.result_code = result_code;
    }

    public String getResult_message() {
        return result_message;
    }

    public void setResult_message(String result_message) {
        this.result_message = result_message;
    }

    public List<WhiteListData> getWhiteList_data() {
        return whiteList_data;
    }

    public void setWhiteList_data(List<WhiteListData> whiteList_data) {
        this.whiteList_data = whiteList_data;
    }
}
