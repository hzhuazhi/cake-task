package com.cake.task.master.core.model.withdraw;

import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 提现记录的实体属性Bean
 * @Author yoko
 * @Date 2020/11/19 16:33
 * @Version 1.0
 */
public class WithdrawModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223501106L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 订单号/各个环节提现的订单号
     */
    private String orderNo;

    /**
     * 中转站订单号
     */
    private String outTradeNo;

    /**
     * 提现金额/提现金额
     */
    private String orderMoney;

    /**
     * 提现手续费：2块，5块
     */
    private String withdrawServiceCharge;

    /**
     * 订单状态：1初始化，2超时，3质疑，4成功
     */
    private Integer orderStatus;

    /**
     * 提现订单类型：1利益者提现，2卡商提现，3渠道提现
     */
    private Integer withdrawType;

    /**
     * 渠道主键ID
     */
    private Long channelId;

    /**
     * 渠道类型：1代收，2大包，3代付
     */
    private Integer channelType;


    /**
     * 指派由谁进行转账给提现人：1卡商，2中转站，3平台
     */
    private Integer outType;

    /**
     * 指派给的卡商ID：对应表tb_fr_merchant的主键ID；假如指派out_type=1，则此字段不允许为空
     */
    private Long merchantId;

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
     * 充值记录银行卡转账图片凭证
     */
    private String pictureAds;

    /**
     * 审核状态：1初始化，2审核收款失败，3审核收款成功
     */
    private Integer checkStatus;

    /**
     * 审核失败缘由，审核失败的原因
     */
    private String checkInfo;

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

    private Integer curdayStart;
    private Integer curdayEnd;


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

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getWithdrawServiceCharge() {
        return withdrawServiceCharge;
    }

    public void setWithdrawServiceCharge(String withdrawServiceCharge) {
        this.withdrawServiceCharge = withdrawServiceCharge;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getWithdrawType() {
        return withdrawType;
    }

    public void setWithdrawType(Integer withdrawType) {
        this.withdrawType = withdrawType;
    }

    public Integer getOutType() {
        return outType;
    }

    public void setOutType(Integer outType) {
        this.outType = outType;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
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

    public String getPictureAds() {
        return pictureAds;
    }

    public void setPictureAds(String pictureAds) {
        this.pictureAds = pictureAds;
    }

    public Integer getCheckStatus() {
        return checkStatus;
    }

    public void setCheckStatus(Integer checkStatus) {
        this.checkStatus = checkStatus;
    }

    public String getCheckInfo() {
        return checkInfo;
    }

    public void setCheckInfo(String checkInfo) {
        this.checkInfo = checkInfo;
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

    public Integer getCurdayStart() {
        return curdayStart;
    }

    public void setCurdayStart(Integer curdayStart) {
        this.curdayStart = curdayStart;
    }

    public Integer getCurdayEnd() {
        return curdayEnd;
    }

    public void setCurdayEnd(Integer curdayEnd) {
        this.curdayEnd = curdayEnd;
    }

    public Long getChannelId() {
        return channelId;
    }

    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }

    public Integer getChannelType() {
        return channelType;
    }

    public void setChannelType(Integer channelType) {
        this.channelType = channelType;
    }
}
