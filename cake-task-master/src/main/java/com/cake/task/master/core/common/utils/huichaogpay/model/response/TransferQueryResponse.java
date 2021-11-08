package com.cake.task.master.core.common.utils.huichaogpay.model.response;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * @author yoko
 * @desc 查单-结果-实体Bean
 * @create 2021-11-08 11:35
 **/
@XStreamAlias("yemadai")
public class TransferQueryResponse {
    @XStreamAlias("code")
    public String code;// 查询状态码 0000表示请求成功,其它则表示请求失败

    @XStreamAlias("transfer")
    public TransferQuery transfer;// 代付的订单详情
    public TransferQueryResponse(){

    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public TransferQuery getTransfer() {
        return transfer;
    }

    public void setTransfer(TransferQuery transfer) {
        this.transfer = transfer;
    }
}
