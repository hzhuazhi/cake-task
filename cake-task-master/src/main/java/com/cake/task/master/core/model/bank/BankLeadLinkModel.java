package com.cake.task.master.core.model.bank;

import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 主卡与设备卡关联关系的实体属性Bean
 * @Author yoko
 * @Date 2021/2/26 10:53
 * @Version 1.0
 */
public class BankLeadLinkModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203253201121L;

    public BankLeadLinkModel(){

    }

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 名称/别名
     */
    private String alias;

    /**
     * 主卡/原始卡ID：对应表tb_fr_bank_lead的主键ID
     */
    private Long bankLeadId;

    /**
     * 银行卡ID：对应表tb_fr_bank的主键ID
     */
    private Long bankId;

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

    public Long getBankLeadId() {
        return bankLeadId;
    }

    public void setBankLeadId(Long bankLeadId) {
        this.bankLeadId = bankLeadId;
    }

    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
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
}
