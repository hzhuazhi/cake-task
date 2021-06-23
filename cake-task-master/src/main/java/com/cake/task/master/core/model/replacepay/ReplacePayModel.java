package com.cake.task.master.core.model.replacepay;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 代付资源的实体属性Bean
 * @Author yoko
 * @Date 2021/6/8 17:01
 * @Version 1.0
 */
public class ReplacePayModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203623201121L;

    public ReplacePayModel(){

    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 别名
     */
    private String alias;

    /**
     * 公司名称
     */
    private String companyName;

    /**
     *公司地址
     */
    private String companyAds;

    /**
     * 银行卡归属类型：对应表tb_fr_bank_type的主键ID
     */
    private Long bankTypeId;

    /**
     * 归属卡商ID：对应表tb_fr_merchant的主键ID，并且角色是卡商
     */
    private Long merchantId;

    /**
     * 银行卡归属卡站点ID：对应表tb_fr_merchant_site的主键ID，并且角色是卡站点
     */
    private Long merchantSiteId;


    /**
     * 银行名称/归属开户行
     */
    private String bankName;

    /**
     * 银行卡账号/银行卡号
     */
    private String bankCard;

    /**
     * 银行支行/支行名称
     */
    private String subbranchName;

    /**
     * 开户名
     */
    private String accountName;

    /**
     * 余额：银行卡现有余额
     */
    private String balance;


    /**
     * 可用余额
     */
    private String useBalance;


    /**
     * 出码接口地址：请求的地址
     */
    private String outInterfaceAds;


    /**
     * 回调地址：这个回调接口地址可能是我方的地址（被动接收），也有可能是对方的地址（主动请求）
     */
    private String inInterfaceAds;


    /**
     * 查询余额接口地址
     */
    private String balanceInterfaceAds;

    /**
     * 商户ID、商户号
     */
    private String businessNum;

    /**
     * 平台商户ID、平台商户号
     */
    private String platformBusinessNum;

    /**
     * 账户属性 ： 0-对私   1-对公
     */
    private String accountAttribute;

    /**
     * 账号类型 ： 3-公司账户  4-银行卡
     */
    private String accountType;

    /**
     * 公司负责人电话
     */
    private String telephoneNum;

    /**
     * 公钥
     */
    private String publicKey;

    /**
     * 公钥路径
     */
    private String publicKeyPath;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 私钥路径
     */
    private String privateKeyPath;

    /**
     * 衫德秘钥
     */
    private String sandKey;

    /**
     * 衫德秘钥路径
     */
    private String sandKeyPath;

    /**
     * 版本号
     */
    private String versionNum;

    /**
     * 付款类型/产品ID：1付款对私，2付款对公
     */
    private Integer payType;

    /**
     * 获取订单结果类型：1被动接收数据，2主动查询
     */
    private Integer gainDataType;

    /**
     * 主动获取订单结果的间隔时间类型：1任意时间都可查询，2固定时间，3集合某时间间隔（5分钟，8分钟....）
     */
    private Integer gainDataTimeType;

    /**
     * 主动获取订单结果的具体间隔时间（单位秒）：当gain_data_time_type=1时这里无需填值，等于2时这里可以填任意正整数，等于3时这里填（300#800#...多个以#隔开）
     */
    private String gainDataTime;

    /**
     * 数据说明：检测被限制的原因:task跑日月总限制，如果被限制，连续给出订单失败会填充被限制的原因
     */
    private String dataExplain;

    /**
     * 是否测试通过：1未通过，2通过；收到银行卡短信，并且解析短信模板配置正确
     */
    private Integer isOk;

    /**
     * 备注
     */
    private String remark;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    private Integer useStatus;

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
     * 代付策略的主键ID
     */
    private Long replacePayStrategyId;

    /**
     * 归属代付资源ID：对应表tb_fr_replace_pay的主键ID
     */
    private Long replacePayId;


    /**
     * 优先级：就是填入数字，数字越小的优先级越靠前
     */
    private Integer priority;

    /**
     * 放量时间段：支持多个时间段，以#号分割
     */
    private String openTimeSlot;

    /**
     *日付款限量金额
     */
    private String outDayMoney;

    /**
     * 月付款限量金额
     */
    private String outMonthMoney;

    /**
     * 日付款限量次数
     */
    private Integer outDayNum;

    /**
     * 每日已经代付的成功金额
     */
    private String dayMoney;

    /**
     * 日期
     */
    private Integer curday;


    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * 卡商集合
     */
    private List<Long> merchantIdList;


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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyAds() {
        return companyAds;
    }

    public void setCompanyAds(String companyAds) {
        this.companyAds = companyAds;
    }

    public Long getBankTypeId() {
        return bankTypeId;
    }

    public void setBankTypeId(Long bankTypeId) {
        this.bankTypeId = bankTypeId;
    }

    public Long getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(Long merchantId) {
        this.merchantId = merchantId;
    }

    public Long getMerchantSiteId() {
        return merchantSiteId;
    }

    public void setMerchantSiteId(Long merchantSiteId) {
        this.merchantSiteId = merchantSiteId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankCard() {
        return bankCard;
    }

    public void setBankCard(String bankCard) {
        this.bankCard = bankCard;
    }

    public String getSubbranchName() {
        return subbranchName;
    }

    public void setSubbranchName(String subbranchName) {
        this.subbranchName = subbranchName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getUseBalance() {
        return useBalance;
    }

    public void setUseBalance(String useBalance) {
        this.useBalance = useBalance;
    }

    public String getOutInterfaceAds() {
        return outInterfaceAds;
    }

    public void setOutInterfaceAds(String outInterfaceAds) {
        this.outInterfaceAds = outInterfaceAds;
    }

    public String getInInterfaceAds() {
        return inInterfaceAds;
    }

    public void setInInterfaceAds(String inInterfaceAds) {
        this.inInterfaceAds = inInterfaceAds;
    }

    public String getBalanceInterfaceAds() {
        return balanceInterfaceAds;
    }

    public void setBalanceInterfaceAds(String balanceInterfaceAds) {
        this.balanceInterfaceAds = balanceInterfaceAds;
    }

    public String getBusinessNum() {
        return businessNum;
    }

    public void setBusinessNum(String businessNum) {
        this.businessNum = businessNum;
    }

    public String getPlatformBusinessNum() {
        return platformBusinessNum;
    }

    public void setPlatformBusinessNum(String platformBusinessNum) {
        this.platformBusinessNum = platformBusinessNum;
    }

    public String getAccountAttribute() {
        return accountAttribute;
    }

    public void setAccountAttribute(String accountAttribute) {
        this.accountAttribute = accountAttribute;
    }

    public String getTelephoneNum() {
        return telephoneNum;
    }

    public void setTelephoneNum(String telephoneNum) {
        this.telephoneNum = telephoneNum;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getPublicKeyPath() {
        return publicKeyPath;
    }

    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public String getPrivateKeyPath() {
        return privateKeyPath;
    }

    public void setPrivateKeyPath(String privateKeyPath) {
        this.privateKeyPath = privateKeyPath;
    }

    public String getSandKey() {
        return sandKey;
    }

    public void setSandKey(String sandKey) {
        this.sandKey = sandKey;
    }

    public String getSandKeyPath() {
        return sandKeyPath;
    }

    public void setSandKeyPath(String sandKeyPath) {
        this.sandKeyPath = sandKeyPath;
    }

    public String getVersionNum() {
        return versionNum;
    }

    public void setVersionNum(String versionNum) {
        this.versionNum = versionNum;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getGainDataType() {
        return gainDataType;
    }

    public void setGainDataType(Integer gainDataType) {
        this.gainDataType = gainDataType;
    }

    public Integer getGainDataTimeType() {
        return gainDataTimeType;
    }

    public void setGainDataTimeType(Integer gainDataTimeType) {
        this.gainDataTimeType = gainDataTimeType;
    }

    public String getGainDataTime() {
        return gainDataTime;
    }

    public void setGainDataTime(String gainDataTime) {
        this.gainDataTime = gainDataTime;
    }

    public String getDataExplain() {
        return dataExplain;
    }

    public void setDataExplain(String dataExplain) {
        this.dataExplain = dataExplain;
    }

    public Integer getIsOk() {
        return isOk;
    }

    public void setIsOk(Integer isOk) {
        this.isOk = isOk;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getUseStatus() {
        return useStatus;
    }

    public void setUseStatus(Integer useStatus) {
        this.useStatus = useStatus;
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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }


    public Long getReplacePayStrategyId() {
        return replacePayStrategyId;
    }

    public void setReplacePayStrategyId(Long replacePayStrategyId) {
        this.replacePayStrategyId = replacePayStrategyId;
    }

    public Long getReplacePayId() {
        return replacePayId;
    }

    public void setReplacePayId(Long replacePayId) {
        this.replacePayId = replacePayId;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getOpenTimeSlot() {
        return openTimeSlot;
    }

    public void setOpenTimeSlot(String openTimeSlot) {
        this.openTimeSlot = openTimeSlot;
    }

    public String getOutDayMoney() {
        return outDayMoney;
    }

    public void setOutDayMoney(String outDayMoney) {
        this.outDayMoney = outDayMoney;
    }

    public String getOutMonthMoney() {
        return outMonthMoney;
    }

    public void setOutMonthMoney(String outMonthMoney) {
        this.outMonthMoney = outMonthMoney;
    }

    public Integer getOutDayNum() {
        return outDayNum;
    }

    public void setOutDayNum(Integer outDayNum) {
        this.outDayNum = outDayNum;
    }

    public String getDayMoney() {
        return dayMoney;
    }

    public void setDayMoney(String dayMoney) {
        this.dayMoney = dayMoney;
    }

    public Integer getCurday() {
        return curday;
    }

    public void setCurday(Integer curday) {
        this.curday = curday;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public List<Long> getMerchantIdList() {
        return merchantIdList;
    }

    public void setMerchantIdList(List<Long> merchantIdList) {
        this.merchantIdList = merchantIdList;
    }
}
