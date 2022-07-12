package com.cake.task.master.core.model.zhongbang;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @author yoko
 * @desc 众邦白名单实体Bean
 * @create 2022-07-05 17:51
 **/
public class ZbWhitelistModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 120322320115621L;

    public ZbWhitelistModel(){

    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 流水号：便于主动查询添加白名单时使用的
     */
    private String trxId;


    /**
     * 白名单流水序号
     */
    private String serialNo;

    /**
     * 用户的白名单流水序号（渠道的）
     */
    private String userSerialNo;


    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 身份证号
     */
    private String cardNum;

    /**
     * 身份证正面照片URL
     */
    private String cardImgFront;

    /**
     * 身份证反面照片URL
     */
    private String cardImgBack;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 银行卡号
     */
    private String bankCard;

    /**
     * 添加白名单的状态：1初始化，2成功，3失败
     */
    private Integer orderStatus;

    /**
     * 查询白名单的状态：1初始化，2成功，3失败
     */
    private Integer queryStatus;

    /**
     * 同步的接口地址:我方的同步地址
     */
    private String notifyUrl;

    /**
     * 添加白名单返回的状态码
     */
    private String resCode;


    /**
     * 添加白名单返回的说明
     */
    private String resExplain;

    /**
     * 签名值：去重复判断，是否重复提交
     */
    private String sign;

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
     *运行状态：：0初始化，1锁定，2计算失败，3计算成功
     */
    private Integer handleStatus;

    /**
     * 运行次数
     */
    private Integer handleNum;

    /**
     *发送次数
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTrxId() {
        return trxId;
    }

    public void setTrxId(String trxId) {
        this.trxId = trxId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getUserSerialNo() {
        return userSerialNo;
    }

    public void setUserSerialNo(String userSerialNo) {
        this.userSerialNo = userSerialNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardNum() {
        return cardNum;
    }

    public void setCardNum(String cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardImgFront() {
        return cardImgFront;
    }

    public void setCardImgFront(String cardImgFront) {
        this.cardImgFront = cardImgFront;
    }

    public String getCardImgBack() {
        return cardImgBack;
    }

    public void setCardImgBack(String cardImgBack) {
        this.cardImgBack = cardImgBack;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(Integer queryStatus) {
        this.queryStatus = queryStatus;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getResExplain() {
        return resExplain;
    }

    public void setResExplain(String resExplain) {
        this.resExplain = resExplain;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
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

    public Integer getHandleStatus() {
        return handleStatus;
    }

    public void setHandleStatus(Integer handleStatus) {
        this.handleStatus = handleStatus;
    }

    public Integer getHandleNum() {
        return handleNum;
    }

    public void setHandleNum(Integer handleNum) {
        this.handleNum = handleNum;
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

}
