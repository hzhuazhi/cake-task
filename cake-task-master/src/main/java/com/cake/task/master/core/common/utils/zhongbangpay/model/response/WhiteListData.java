package com.cake.task.master.core.common.utils.zhongbangpay.model.response;

/**
 * @author yoko
 * @desc 白名单查询返回的详情
 * @create 2022-07-11 17:03
 **/
public class WhiteListData {

    public WhiteListData(){

    }

    private String serial_no;// 白名单序号
    private String real_name;// 用户姓名
    private String cert_no;// 身份证号
    private String mobile_no;// 手机号
    private String bank_account_no;// 银行卡号
    private String add_status;// 添加状态 WAIT_ADD:待添加； ADD_PROCESSING：处 理中； ADD_SUCCESS：添加 成功; ADD_FAIL:添加失败
    private String description;// 说明

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
}
