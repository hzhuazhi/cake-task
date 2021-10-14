package com.cake.task.master.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;


/**
 * @Description 公共方法类
 * @Author yoko
 * @Date 2020/1/7 20:25
 * @Version 1.0
 */
public class HodgepodgeMethod {
    private static Logger log = LoggerFactory.getLogger(HodgepodgeMethod.class);



    /**
     * @Description: 换算杉德代付拉单的订单金额
     * <p>
     *     不足12位则订单金额前面补0
     * </p>
     * @param orderMoney - 代付拉单订单金额
     * @return
     * @Author: yoko
     * @Date 2021/10/13 16:02
     */
    public static String sandPayOrderMoney(String orderMoney){
        String str = "";
        BigDecimal amt  = new BigDecimal (orderMoney);
        Double tmp = amt.multiply(new BigDecimal(100)).doubleValue();
        str = String.format("%012d", Math.abs(tmp.intValue()));
        return str;
    }


    /**
     * @Description: 换算杉德余额
     * <p>
     *     杉德余额属于12位，需要把余额变更成正常金额
     * </p>
     * @param balance - 余额
     * @return
     * @Author: yoko
     * @Date 2021/10/13 16:02
     */
    public static String sandPayBalance(String balance){
        String str = "";
        BigDecimal  amt  = new BigDecimal (balance);
        Double tmp = amt.divide(new BigDecimal(100)).doubleValue();
        str = tmp.toString();
        return str;
    }



    public static void main(String [] args) throws Exception{
    }




    

}
