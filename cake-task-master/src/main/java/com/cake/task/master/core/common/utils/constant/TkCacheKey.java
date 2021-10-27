package com.cake.task.master.core.common.utils.constant;

/**
 * @Description task任务的redis的key
 * @Author yoko
 * @Date 2020/6/3 14:50
 * @Version 1.0
 */
public interface TkCacheKey {

    /**
     * LOCK-手机短信类容的task的redis
     */
    String LOCK_MOBILE_CARD_SHORT_MSG = "-1";

    /**
     * 手机卡的信息
     */
    String MOBILE_CARD_PHONE_NUM = "-2";

    /**
     * LOCK-手机短信类容的task的redis
     * <p>
     *     正式处理逻辑运算
     * </p>
     */
    String LOCK_MOBILE_CARD_SHORT_MSG_HANDLE = "-3";

    /**
     * LOCK-银行卡短信
     */
    String LOCK_BANK_SHORT_MSG = "-4";

    /**
     * LOCK-银行卡短信已经扩充完毕的数据
     */
    String LOCK_BANK_SHORT_MSG_WORK_TYPE = "-5";

    /**
     * LOCK-失效订单
     */
    String LOCK_ORDER_INVALID = "-6";

    /**
     * LOCK-成功订单
     */
    String LOCK_ORDER_SUCCESS = "-7";

    /**
     * LOCK-同步订单
     */
    String LOCK_ORDER_SEND = "-8";

    /**
     * LOCK-监控：监控银行卡
     */
    String LOCK_MONITOR_BANK = "-9";

    /**
     * LOCK-订单补单
     */
    String LOCK_ORDER_REPLENISH = "-10";

    /**
     * LOCK-手机欠费短信
     */
    String LOCK_SHORT_MSG_ARREARS = "-11";

    /**
     * LOCK-卡商充值
     */
    String LOCK_MERCHANT_RECHARGE = "-12";

    /**
     * LOCK-下发-分配操作
     */
    String LOCK_ISSUE_DISTRIBUTION = "-13";

    /**
     * LOCK-下发-是否归集完毕操作
     */
    String LOCK_ISSUE_COMPLETE = "-14";

    /**
     * LOCK-下发-同步下发的成功数据
     */
    String LOCK_ISSUE_SEND = "-15";

    /**
     * LOCK-卡商充值-订单类型：平台发起订单
     */
    String LOCK_MERCHANT_RECHARGE_BALANCE = "-16";

    /**
     * LOCK-卡商充值-订单类型：下发订单
     * <p>
     *     只更新金额
     * </p>
     */
    String LOCK_MERCHANT_RECHARGE_MONEY = "-17";

    /**
     * LOCK-卡商充值-订单类型：下发订单
     * <p>
     *     只同步下发
     * </p>
     */
    String LOCK_MERCHANT_RECHARGE_ISSUE = "-18";

    /**
     * LOCK-卡商充值-订单类型：下发订单- 更新操作状态
     * <p>
     *     把操作状态属于初始化状态的更新成废弃状态
     * </p>
     */
    String LOCK_MERCHANT_RECHARGE_OPERATE = "-19";

    /**
     * LOCK-卡商充值-订单类型：下发订单- 更新卡商跑量金额（累加金额）
     */
    String LOCK_MERCHANT_RECHARGE_INVALID = "-20";

    /**
     * LOCK-同步代付订单
     */
    String LOCK_ORDER_OUT_SEND = "-21";

    /**
     * LOCK-代付订单不是初始化的订单处理
     */
    String LOCK_ORDER_OUT = "-22";

    /**
     * LOCK-卡商扣款流水订单状态不是初始化的订单处理
     */
    String LOCK_MERCHANT_BALANCE_DEDUCT = "-23";

    /**
     * LOCK-卡商收益流水的处理
     */
    String LOCK_MERCHANT_PROFIT = "-24";

    /**
     * LOCK-利益者收益流水的处理
     */
    String LOCK_INTEREST_PROFIT = "-25";

    /**
     * LOCK-变更金额流水的处理
     */
    String LOCK_CHANGE_MONEY = "-26";

    /**
     * LOCK-渠道提现流水的处理
     */
    String LOCK_CHANNEL_WITHDRAW_RUN = "-27";


    /**
     * LOCK-渠道提现流水的处理-数据同步
     */
    String LOCK_CHANNEL_WITHDRAW_SEND = "-28";

    /**
     * LOCK-汇总提现记录流水的处理-数据同步
     */
    String LOCK_WITHDRAW_SEND = "-29";

    /**
     * LOCK-汇总提现记录流水的处理-分配
     */
    String LOCK_WITHDRAW_ISSUE_DISTRIBUTION = "-30";

    /**
     * LOCK-卡商提现流水的处理
     */
    String LOCK_MERCHANT_WITHDRAW_RUN = "-31";

    /**
     * LOCK-银行卡在更新使用状态时进行锁
     */
    String LOCK_BANK_UPDATE_USE = "-32";

    /**
     * LOCK-第三方代付主动拉取结果的数据处理
     */
    String LOCK_REPLACE_PAY_GAIN = "-33";

    /**
     * LOCK-第三方代付主动拉取结果返回的订单结果
     */
    String LOCK_REPLACE_PAY_GAIN_RESULT = "-34";

    /**
     * 代付数据
     */
    String REPLACE_PAY = "-35";

    /**
     * LOCK-代付预备
     */
    String LOCK_ORDER_OUT_PREPARE = "-36";



}
