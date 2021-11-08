package com.cake.task.master.core.common.utils.huichaogpay.model.request;

/**
 * Created by yinfan on 2019-10-16.
 */
public class TransferRequest {
    //accountNumber  transId notifyURL tt signType bankCode provice city branchName accountName cardNo idNo phone amount remark secureCode
    private String accountNumber;
    private String notifyURL;
    private String tt;
    private String signType;
    private TransferList transferList;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getNotifyURL() {
        return notifyURL;
    }

    public void setNotifyURL(String notifyURL) {
        this.notifyURL = notifyURL;
    }

    public String getTt() {
        return tt;
    }

    public void setTt(String tt) {
        this.tt = tt;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public TransferList getTransferList() {
        return transferList;
    }

    public void setTransferList(TransferList transferList) {
        this.transferList = transferList;
    }
}
