package com.cake.task.master.core.model.replacepay;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 代付放量策略的实体属性Bean
 * @Author yoko
 * @Date 2021/6/15 18:12
 * @Version 1.0
 */
public class ReplacePayStrategyModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203623201129L;

    public ReplacePayStrategyModel(){

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
     * 别名
     */
    private String alias;

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

    public Long getReplacePayId() {
        return replacePayId;
    }

    public void setReplacePayId(Long replacePayId) {
        this.replacePayId = replacePayId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
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
