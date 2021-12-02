package com.cake.task.master.core.common.utils.constant;

/**
 * @author df
 * @Description:redis的key
 * @create 2019-05-22 15:43
 **/
public interface CacheKey {

    /**
     * 策略数据
     */
    String STRATEGY = "-1";

    /**
     * 短信的类型定位策略
     */
    String SHORT_MSG_STRATEGY = "-2";

    /**
     * 商户：商户的秘钥
     */
    String CHANNEL_SECRETKEY = "-3";

    /**
     * 锁：在筛选银行卡，需要先锁住此银行卡
     * 高并发避免出现问题
     */
    String LOCK_BANK = "-4";

    /**
     * 派单给银行卡时，要派单的金额，存入redis缓存
     * task:如果订单成功，则需要删除此task
     */
    String BANK_ORDER_MONEY = "-5";

    /**
     * 微信转卡日收款金额
     * task:需要检测收款限制
     */
    String WX_IN_DAY_MONEY = "-6";

    /**
     * 微信转卡月收款金额
     * task:需要检测收款限制
     */
    String WX_IN_MONTH_MONEY = "-7";

    /**
     * 微信转卡日收款次数
     * task:需要检测收款限制
     */
    String WX_IN_DAY_NUM = "-8";

    /**
     * 支付宝转卡日收款金额
     * task:需要检测收款限制
     */
    String ZFB_IN_DAY_MONEY = "-9";

    /**
     * 支付宝转卡月收款金额
     * task:需要检测收款限制
     */
    String ZFB_IN_MONTH_MONEY = "-10";

    /**
     * 支付宝转卡日收款次数
     * task:需要检测收款限制
     */
    String ZFB_IN_DAY_NUM = "-11";

    /**
     * 卡转卡日收款金额
     * task:需要检测收款限制
     */
    String CARD_IN_DAY_MONEY = "-12";

    /**
     * 卡转卡月收款金额
     * task:需要检测收款限制
     */
    String CARD_IN_MONTH_MONEY = "-13";

    /**
     * 卡转卡日收款次数
     * task:需要检测收款限制
     */
    String CARD_IN_DAY_NUM = "-14";

    /**
     * 短代API
     */
    String SHORT_CHAIN = "-15";

    /**
     * 短信的类型定位策略-多个
     * <p>
     *     根据类型的>=来查询
     * </p>
     */
    String SHORT_MSG_STRATEGY_TWO = "-16";

    /**
     * 锁定：锁住卡商账号的主键ID，在更新卡商金额的时候
     */
    String LOCK_MERCHANT_MONEY = "-17";

    /**
     * 给出绑定银行卡的ID纪录
     */
    String BANK_BINDING_TYPE = "-18";

    /**
     * 给出绑定卡商的ID纪录
     */
    String MERCHANT_BINDING_TYPE = "-19";

    /**
     * 锁：在筛选商户，需要先锁住此商户
     * 高并发避免出现问题
     */
    String LOCK_MERCHANT = "-20";

    /**
     * LOCK-卡商的账户ID
     * 用于更新字段least_money
     */
    String LOCK_MERCHANT_ID = "-21";

    /**
     * 锁定：锁住卡商账号的主键ID，在更新卡商收益金额金额的时候
     * <p>
     *     更新卡商字段profit的时候
     * </p>
     */
    String LOCK_MERCHANT_MONEY_PROFIT = "-22";

    /**
     * 商户：商户的ID
     */
    String CHANNEL_ID = "-23";

    /**
     * 转账用户的IP
     */
    String TRANSFER_USER_IP = "-24";

    /**
     * 给出代付的ID纪录
     */
    String REPLACE_PAY = "-25";







    /**
     * 代付日付款金额
     * task:需要检测付款限制
     */
    String OUT_DAY_MONEY = "-26";

    /**
     * 代付月付款金额
     * task:需要检测付款限制
     */
    String OUT_MONTH_MONEY = "-27";

    /**
     * 代付日付款次数
     * task:需要检测收款限制
     */
    String OUT_DAY_NUM = "-28";


    /**
     * 锁：在筛选代付时，需要先锁住此代付
     * 高并发避免出现问题
     */
    String LOCK_REPLACE_PAY = "-29";


    /**
     * 卡商绑定渠道的手续费
     */
    String MERCHANT_SERVICE_CHARGE = "-30";



}
