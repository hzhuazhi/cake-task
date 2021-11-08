package com.cake.task.master.core.common.utils.huichaogpay.model.request;

/**
 * Created by yinfan on 2019-10-16.
 */
public class TransferQueryRequest {
    // merchantNumber mertransferID signType sign queryTimeBegin queryTimeEnd requestTime
    private String merchantNumber;
    private String mertransferID;
    private String signType;
    private String sign;
    private String queryTimeBegin;
    private String queryTimeEnd;
    private String requestTime;

    public String getMerchantNumber() {
        return merchantNumber;
    }

    public void setMerchantNumber(String merchantNumber) {
        this.merchantNumber = merchantNumber;
    }

    public String getMertransferID() {
        return mertransferID;
    }

    public void setMertransferID(String mertransferID) {
        this.mertransferID = mertransferID;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getQueryTimeBegin() {
        return queryTimeBegin;
    }

    public void setQueryTimeBegin(String queryTimeBegin) {
        this.queryTimeBegin = queryTimeBegin;
    }

    public String getQueryTimeEnd() {
        return queryTimeEnd;
    }

    public void setQueryTimeEnd(String queryTimeEnd) {
        this.queryTimeEnd = queryTimeEnd;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }
}
