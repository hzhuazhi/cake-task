package com.cake.task.master.core.common.utils.jinfupay.model;


import com.cake.task.master.core.protocol.page.BasePage;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 金服支付响应的实体属性Bean
 * @Author: yoko
 * @Date: $
 * @Version: 1.0
 **/
public class JinFuPayResponse extends BasePage implements Serializable {
    private static final long   serialVersionUID = 1203223201105L;

    public JinFuPayResponse(){

    }

    public Integer code; //状态码：0成功，非0失败
    public Integer count; //数量
    public String message; //信息
    public String msg; // 提示信息
    public ResultResponse result;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ResultResponse getResult() {
        return result;
    }

    public void setResult(ResultResponse result) {
        this.result = result;
    }
}
