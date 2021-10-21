package com.cake.task.master.core.model.replacepay;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 第三方代付主动拉取结果的实体属性Bean
 * @Author yoko
 * @Date 2021/6/16 15:04
 * @Version 1.0
 */
public class ReplacePayGainModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 3203223201133L;

    public ReplacePayGainModel(){

    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 归属代付资源ID：对应表tb_fr_replace_pay的主键ID
     */
    private Long replacePayId;

    /**
     * 代付资源类型：1杉德支付，2金服支付
     */
    private Integer resourceType;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 订单金额
     */
    private String orderMoney;

    /**
     * 交易时间：时间戳20210607102058
     */
    private Long tradeTime;

    /**
     * 下次主动获取结果的时间：当系统时间大于等于此时间才会主动去获取数据
     */
    private String nextTime;

    /**
     * 当前主动获取订单结果的具体间隔时间：针对tb_fr_replace_pay表里面字段gain_data_time进行运算next_time的具体值
     */
    private String nowGainDataTime;

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
     * 发送次数
     */
    private Integer sendNum;

    /**
     * 发送状态：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer sendStatus;

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

    /**
     * 查询条件-限制条数
     */
    private Integer limitNum;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getReplacePayId() {
        return replacePayId;
    }

    public void setReplacePayId(Long replacePayId) {
        this.replacePayId = replacePayId;
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

    public Long getTradeTime() {
        return tradeTime;
    }

    public void setTradeTime(Long tradeTime) {
        this.tradeTime = tradeTime;
    }

    public String getNextTime() {
        return nextTime;
    }

    public void setNextTime(String nextTime) {
        this.nextTime = nextTime;
    }

    public String getNowGainDataTime() {
        return nowGainDataTime;
    }

    public void setNowGainDataTime(String nowGainDataTime) {
        this.nowGainDataTime = nowGainDataTime;
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

    public Integer getLimitNum() {
        return limitNum;
    }

    public void setLimitNum(Integer limitNum) {
        this.limitNum = limitNum;
    }

    public Integer getResourceType() {
        return resourceType;
    }

    public void setResourceType(Integer resourceType) {
        this.resourceType = resourceType;
    }
}
