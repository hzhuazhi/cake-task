package com.cake.task.master.core.common.enums;

/**
 * @Description TODO
 * @Author long
 * @Date 2020/1/2 17:32
 * @Version 1.0
 */
public enum ENUM_ERROR {

    SERVER_ERRER("A00001", "手机号码已经注册!", "注册模块、手机号码已经注册!"),
    SERVER_OK("0", "请求正常！", "请求正常!"),
    INVALID_USER("-1", "无效的请求！", "无效的请求!"),

    /***********************************************
     * 登录、注册、忘记密码类的业务
     **********************************************/
    A00001("A00001", "手机号码已经注册!", "注册模块、手机号码已经注册!"),
    A00002("A00002", "无效的邀请码!", "注册模块、无效的邀请码!"),
    A00003("A00003", "发送验证码已达到上线!", "注册模块、发送验证码已达到上线!"),
    A00004("A00004", "发送短信失败!", "注册模块、发送短信失败!"),
    A00005("A00005", "二次密码输入不一致,请仔细核对后再进行提交!", "提交注册信息、二次密码输入不一致!"),
    A00006("A00006", "无效的用户信息!", "提交注册信息、数据异常!"),
    A00007("A00007", "验证码错误!", "验证码错误!"),
    A00008("A00008", "验证码已过期!", "登录模块、发送短信失败!"),
    A00009("A00009", "手机号未注册!", "登录模块、手机号未注册!"),
    A00010("A00010", "手机号或者密码错误!", "手机号或者密码错误!"),
    A00011("A00011", "手机号码不存在!", "忘记密码、手机号码不存在!"),
    A00012("A00012", "用户token已经超时!", "忘记密码、用户token已经超时!"),
    A00013("A00013", "无效的请求信息!", "无效的请求信息!"),
    A00014("A00014", "请求已过期!", "请求已过期!"),
    A00015("A00015", "修改密码失败，请重试!", "修改密码失败，请重试!"),
    A00016("A00016", "用户数据异常，请联系管理员!", "用户数据异常，请联系管理员!"),
    A00017("A00017", "设置密码和当前密码一致，请输入新的密码!", "设置密码和当前密码一致，请输入新的密码!"),
    A00018("A00018", "手机号格是不正确!", "手机号格是不正确!"),

    P00001("P00001", "支付宝个数，已经达到最大值!", "支付宝个数，已经达到最大值!"),
    P00002("P00002", "支付宝信息不正确!", "支付宝信息不正确!"),
    P00003("P00003", "该用户金额不够，不能进行提现!", "该用户金额不够，不能进行提现!"),
    P00004("P00004", "该用户订单锁定中，请重试", "该用户订单锁定中，请重试!"),
    P00005("P00005", "提现金额不符合要求", "提现金额不符合要求!"),
    P00006("P00006", "用户订单信息在处理，请稍后重试", "用户订单信息在处理，请稍后重试!"),

    P00007("P00007", "用户信息和绑定的支付宝信息不一致,不能进行修改", "用户信息和绑定的支付宝信息不一致,不能进行修改!"),

    P00008("P00008", "支付密码生成Key已过期", "支付密码生成Key已过期!"),
    P00009("P00009", "用户信息和生成Key对应的用户不一致", "用户信息和生成Key对应的用户不一致!"),
    P00010("P00010", "支付密码错误，请重新输入", "支付密码错误，请重新输入!"),
    P00011("P00011", "该用户已经达到当日提现最大次数", "该用户已经达到当日提现最大次数!"),


    R00001("P00001", "该用户不是永久VIP不能领取该任务", "该用户不是永久VIP不能领取该任务!"),
    R00002("P00002", "该用户没有需要领取的信息", "该用户没有需要领取的信息!"),
    ;
    /**
     * 错误码
     */
    private String eCode;
    /**
     * 给客户端看的错误信息
     */
    private String eDesc;
    /**
     * 插入数据库的错误信息
     */
    private String dbDesc;

    private ENUM_ERROR(String eCode, String eDesc, String dbDesc) {
        this.eCode = eCode;
        this.eDesc = eDesc;
        this.dbDesc =dbDesc;
    }

    public String geteCode() {
        return eCode;
    }

    public void seteCode(String eCode) {
        this.eCode = eCode;
    }

    public String geteDesc() {
        return eDesc;
    }

    public void seteDesc(String eDesc) {
        this.eDesc = eDesc;
    }

    public String getDbDesc() {
        return dbDesc;
    }

    public void setDbDesc(String dbDesc) {
        this.dbDesc = dbDesc;
    }
}