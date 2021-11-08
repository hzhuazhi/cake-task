package com.cake.task.master.core.common.utils.huichaogpay.model.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;


/**
 * @author yoko
 * @desc 拉单-结果-实体Bean
 * @create 2021-11-05 21:25
 **/
@XStreamAlias("yemadai")
public class TransferResponse {
    @XStreamAlias("errCode")
    public String errCode;// 错误码 0000：请求成功，其他：请求格式有误

    @XStreamAlias("transferList")
    public Transfer transferList;// 拉单的订单信息

    public TransferResponse(){

    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public Transfer getTransferList() {
        return transferList;
    }

    public void setTransferList(Transfer transferList) {
        this.transferList = transferList;
    }
}
