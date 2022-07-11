package com.cake.task.master.core.common.utils.zhongbangpay.model.request;

/**
 * @author yoko
 * @desc 白名单的实体Bean
 * @create 2022-07-04 15:57
 **/
public class WhitelistRequest {

    private String serial_no;// 白名单序号
    private String real_name;// 用户姓名
    private String cert_no;// 身份证号
    private String cert_img_front;// 身份证正面照片：身份证正面照片URL
    private String cert_img_back;// 身份证反面照片：身份证反面照片URL
    private String mobile_no;// 手机号
    private String bank_account_no;// 银行卡号


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

    public String getCert_img_front() {
        return cert_img_front;
    }

    public void setCert_img_front(String cert_img_front) {
        this.cert_img_front = cert_img_front;
    }

    public String getCert_img_back() {
        return cert_img_back;
    }

    public void setCert_img_back(String cert_img_back) {
        this.cert_img_back = cert_img_back;
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
}
