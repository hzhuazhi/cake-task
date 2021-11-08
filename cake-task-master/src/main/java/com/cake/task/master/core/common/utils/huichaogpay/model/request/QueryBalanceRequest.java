package com.cake.task.master.core.common.utils.huichaogpay.model.request;

/**
 * Created by yinfan on 2019-10-16.
 */
public class QueryBalanceRequest {
    //MerNo RequestTime signType SignInfo
    private String MerNo;
    private String RequestTime;
    private String signType;
    private String SignInfo;

    public String getMerNo() {
        return MerNo;
    }

    public void setMerNo(String merNo) {
        MerNo = merNo;
    }

    public String getRequestTime() {
        return RequestTime;
    }

    public void setRequestTime(String requestTime) {
        RequestTime = requestTime;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getSignInfo() {
        return SignInfo;
    }

    public void setSignInfo(String signInfo) {
        SignInfo = signInfo;
    }
}
