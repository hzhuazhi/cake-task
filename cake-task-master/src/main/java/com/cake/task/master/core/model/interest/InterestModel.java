package com.cake.task.master.core.model.interest;

import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Description 利益分配者的实体属性Bean
 * @Author yoko
 * @Date 2020/11/9 16:10
 * @Version 1.0
 */
public class InterestModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223401101L;


    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 账号
     */
    private String accountNum;

    /**
     * 登录密码
     */
    private String passWd;

    /**
     * 提款密码
     */
    private String withdrawPassWd;

    /**
     * 所属角色ID
     */
    private Long roleId;

    /**
     * 账号名称
     */
    private String acName;

    /**
     * 账号联系人
     */
    private String acContacts;

    /**
     * 联系电话
     */
    private String acPhone;

    /**
     * 账号类型
     */
    private Integer acType;

    /**
     * 总额：总共跑量的金额
     */
    private String totalMoney;

    /**
     * 余额：跑量的金额累加，渠道提现的金额扣减
     */
    private String balance;

    /**
     * 锁定金额
     */
    private String lockMoney;

    /**
     * 总收益金额
     */
    private String totalProfit;

    /**
     * 备注
     */
    private String remark;

    /**
     * 使用状态:1初始化有效正常使用，2无效暂停使用
     */
    private Integer useStatus;

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

    /**
     * 金额
     */
    private BigDecimal money;

    /**
     * ID集合-SQL
     */
    private List<Long> idList;

    /**
     * 余额加
     */
    private String addBalance;

    /**
     * 余额减
     */
    private String subtractBalance;

    /**
     * 要进行更改的金额
     */
    private String orderMoney;


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

    public String getAccountNum() {
        return accountNum;
    }

    public void setAccountNum(String accountNum) {
        this.accountNum = accountNum;
    }

    public String getPassWd() {
        return passWd;
    }

    public void setPassWd(String passWd) {
        this.passWd = passWd;
    }

    public String getWithdrawPassWd() {
        return withdrawPassWd;
    }

    public void setWithdrawPassWd(String withdrawPassWd) {
        this.withdrawPassWd = withdrawPassWd;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getAcName() {
        return acName;
    }

    public void setAcName(String acName) {
        this.acName = acName;
    }

    public String getAcContacts() {
        return acContacts;
    }

    public void setAcContacts(String acContacts) {
        this.acContacts = acContacts;
    }

    public String getAcPhone() {
        return acPhone;
    }

    public void setAcPhone(String acPhone) {
        this.acPhone = acPhone;
    }

    public Integer getAcType() {
        return acType;
    }

    public void setAcType(Integer acType) {
        this.acType = acType;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getLockMoney() {
        return lockMoney;
    }

    public void setLockMoney(String lockMoney) {
        this.lockMoney = lockMoney;
    }

    public String getTotalProfit() {
        return totalProfit;
    }

    public void setTotalProfit(String totalProfit) {
        this.totalProfit = totalProfit;
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

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public List<Long> getIdList() {
        return idList;
    }

    public void setIdList(List<Long> idList) {
        this.idList = idList;
    }

    public String getAddBalance() {
        return addBalance;
    }

    public void setAddBalance(String addBalance) {
        this.addBalance = addBalance;
    }

    public String getSubtractBalance() {
        return subtractBalance;
    }

    public void setSubtractBalance(String subtractBalance) {
        this.subtractBalance = subtractBalance;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }
}
