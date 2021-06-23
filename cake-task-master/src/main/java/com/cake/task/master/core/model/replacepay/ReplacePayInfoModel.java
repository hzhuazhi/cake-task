package com.cake.task.master.core.model.replacepay;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @Description 代付信息：这个表记录付款信息的实体属性Bean
 * @Author yoko
 * @Date 2021/6/16 11:19
 * @Version 1.0
 */
public class ReplacePayInfoModel extends BasePage implements Serializable {
    private static final long   serialVersionUID = 3203223201106L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 归属代付资源ID：对应表tb_fr_replace_pay的主键ID
     */
    private Long replacePayId;

    /**
     * 订单号：对应表tb_fr_order里面的订单号：后续补充进来的
     */
    private String orderNo;

    /**
     * 收款金额
     */
    private String money;

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

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
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
}
