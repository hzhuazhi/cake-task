package com.cake.task.master.core.model.order;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 代付预备请求的的实体属性Bean
 * @Author yoko
 * @Date 2020/10/29 17:33
 * @Version 1.0
 */
public class OrderOutPrepareModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201105L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private String orderMoney;

    /**
     * 第三方订单号
     */
    private String outTradeNo;

    /**
     * 订单状态：1初始化，2超时，3质疑，4成功
     */
    private Integer orderStatus;

    /**
     * 订单类型：1手动转账，2API转账
     */
    private Integer orderType;

    /**
     * 手续费
     */
    private String serviceCharge;

    /**
     * 订单处理类型：1我方处理，2第三方处理
     */
    private Integer handleType;

    /**
     * 代付订单出码状态:1初始化（我方处理默认初始化），2出码成功（第三方反馈结果），3出码失败
     */
    private Integer outStatus;

    /**
     * 失效时间：审核失效时间，超过多久没有点击审核通过则默认审核通过
     */
    private String invalidTime;

    /**
     * 收款银行卡账号/银行卡号
     */
    private String inBankCard;

    /**
     * 收款银行名称/归属开户行
     */
    private String inBankName;

    /**
     * 收款开户名
     */
    private String inAccountName;

    /**
     * 收款银行开户支行
     */
    private String inBankSubbranch;

    /**
     * 收款银行开户省份
     */
    private String inBankProvince;

    /**
     * 收款银行开户城市
     */
    private String inBankCity;

    /**
     * 付款银行名称/归属开户行
     */
    private String outBankName;

    /**
     * 付款银行卡账号/银行卡号
     */
    private String outBankCard;

    /**
     * 付款开户名
     */
    private String outAccountName;

    /**
     * 归属卡商的账号ID：对应表tb_fr_merchant的主键ID，并且角色类型是卡商
     */
    private Long merchantId;

    /**
     * 银行归属卡商名称
     */
    private String merchantName;

    /**
     * 银行卡归属卡站点ID：对应表tb_fr_merchant_site的主键ID，并且角色是卡站点
     */
    private Long merchantSiteId;

    /**
     * 卡站点名称
     */
    private String merchantSiteName;

    /**
     * 请求的商户ID：对应表tb_fr_channel的主键ID
     */
    private Long channelId;

    /**
     * 商户名称
     */
    private String channelName;

    /**
     * 归属代付资源ID：对应表tb_fr_replace_pay的主键ID
     */
    private Long replacePayId;

    /**
     * 代付名称
     */
    private String replacePayName;

    /**
     * 代付资源类型：1杉德支付，2金服支付
     */
    private Integer resourceType;

    /**
     *上游订单号：供应商订单号、对接放的订单号
     */
    private String supplierTradeNo;

    /**
     * 交易时间时间戳
     */
    private String tradeTime;

    /**
     * 上游手续费
     */
    private String supplierServiceCharge;

    /**
     * 失败缘由：卡商转账失败缘由说明
     */
    private String failInfo;

    /**
     * 同步的接口地址:我方的同步地址
     */
    private String notifyUrl;

    /**
     * 审核状态(审核成功才进行API请求)：1初始化，2审核收款失败，3系统自动审核，4审核收款成功
     */
    private Integer checkStatus;

    /**
     * 补充数据的类型：1初始化，2补充数据失败（其它原因等..），3补充数据成功
     */
    private Integer workType;

    /**
     * 数据说明：做解说用的
     */
    private String dataExplain;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建日期：存的日期格式20160530
     */
    private Integer curday;

    /**
     * 创建所属小时：24小时制
     */
    private Integer curhour;

    /**
     * 创建所属分钟：60分钟制
     */
    private Integer curminute;

    /**
     *运行计算次数
     */
    private Integer runNum;

    /**
     * 运行计算状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer runStatus;

    /**
     *发送次数
     */
    private Integer sendNum;

    /**
     * 发送状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer sendStatus;

    /**
     * 创建人ID
     */
    private Long createUserId;

    /**
     * 创建人归属角色ID
     */
    private Long createRoleId;

    /**
     * 更新人ID
     */
    private Long updateUserId;

    /**
     * 更新人归属角色ID
     */
    private Long updateRoleId;

    /**
     * 创建时间
     */
    private String createTime;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否有效：0有效，1无效/删除
     */
    private Integer yn;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public Integer getHandleType() {
        return handleType;
    }

    public void setHandleType(Integer handleType) {
        this.handleType = handleType;
    }

    public Integer getOutStatus() {
        return outStatus;
    }

    public void setOutStatus(Integer outStatus) {
        this.outStatus = outStatus;
    }

    public String getInvalidTime() {
        return invalidTime;
    }

    public void setInvalidTime(String invalidTime) {
        this.invalidTime = invalidTime;
    }

    public String getInBankCard() {
        return inBankCard;
    }

    public void setInBankCard(String inBankCard) {
        this.inBankCard = inBankCard;
    }

    public String getInBankName() {
        return inBankName;
    }

    public void setInBankName(String inBankName) {
        this.inBankName = inBankName;
    }

    public String getInAccountName() {
        return inAccountName;
    }

    public void setInAccountName(String inAccountName) {
        this.inAccountName = inAccountName;
    }

    public String getInBankSubbranch() {
        return inBankSubbranch;
    }

    public void setInBankSubbranch(String inBankSubbranch) {
        this.inBankSubbranch = inBankSubbranch;
    }

    public String getInBankProvince() {
        return inBankProvince;
    }

    public void setInBankProvince(String inBankProvince) {
        this.inBankProvince = inBankProvince;
    }

    public String getInBankCity() {
        return inBankCity;
    }

    public void setInBankCity(String inBankCity) {
        this.inBankCity = inBankCity;
    }

    public String getOutBankName() {
        return outBankName;
    }

    public void setOutBankName(String outBankName) {
        this.outBankName = outBankName;
    }

    public String getOutBankCard() {
        return outBankCard;
    }

    public void setOutBankCard(String outBankCard) {
        this.outBankCard = outBankCard;
    }

    public String getOutAccountName() {
        return outAccountName;
    }

    public void setOutAccountName(String outAccountName) {
        this.outAccountName = outAccountName;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public Long getMerchantSiteId() {
        return merchantSiteId;
    }

    public void setMerchantSiteId(Long merchantSiteId) {
        this.merchantSiteId = merchantSiteId;
    }

    public String getMerchantSiteName() {
        return merchantSiteName;
    }

    public void setMerchantSiteName(String merchantSiteName) {
        this.merchantSiteName = merchantSiteName;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public Long getReplacePayId() {
        return replacePayId;
    }

    public void setReplacePayId(Long replacePayId) {
        this.replacePayId = replacePayId;
    }

    public String getReplacePayName() {
        return replacePayName;
    }

    public void setReplacePayName(String replacePayName) {
        this.replacePayName = replacePayName;
    }

    public String getSupplierTradeNo() {
        return supplierTradeNo;
    }

    public void setSupplierTradeNo(String supplierTradeNo) {
        this.supplierTradeNo = supplierTradeNo;
    }

    public String getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(String tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getSupplierServiceCharge() {
        return supplierServiceCharge;
    }

    public void setSupplierServiceCharge(String supplierServiceCharge) {
        this.supplierServiceCharge = supplierServiceCharge;
    }

    public String getFailInfo() {
        return failInfo;
    }

    public void setFailInfo(String failInfo) {
        this.failInfo = failInfo;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public Integer getCurhour() {
        return curhour;
    }

    public void setCurhour(Integer curhour) {
        this.curhour = curhour;
    }

    public Integer getCurminute() {
        return curminute;
    }

    public void setCurminute(Integer curminute) {
        this.curminute = curminute;
    }

    public Integer getRunNum() {
        return runNum;
    }

    public void setRunNum(Integer runNum) {
        this.runNum = runNum;
    }

    public Integer getRunStatus() {
        return runStatus;
    }

    public void setRunStatus(Integer runStatus) {
        this.runStatus = runStatus;
    }

    public Integer getSendNum() {
        return sendNum;
    }

    public void setSendNum(Integer sendNum) {
        this.sendNum = sendNum;
    }

    public Integer getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(Integer sendStatus) {
        this.sendStatus = sendStatus;
    }

    public Long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(Long createUserId) {
        this.createUserId = createUserId;
    }

    public Long getCreateRoleId() {
        return createRoleId;
    }

    public void setCreateRoleId(Long createRoleId) {
        this.createRoleId = createRoleId;
    }

    public Long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(Long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public Long getUpdateRoleId() {
        return updateRoleId;
    }

    public void setUpdateRoleId(Long updateRoleId) {
        this.updateRoleId = updateRoleId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getYn() {
        return yn;
    }

    public void setYn(Integer yn) {
        this.yn = yn;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }
}
