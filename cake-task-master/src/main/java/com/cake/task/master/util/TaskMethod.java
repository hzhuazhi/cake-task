package com.cake.task.master.util;
import com.cake.task.master.core.common.utils.DateUtil;
import com.cake.task.master.core.common.utils.StringUtil;
import com.cake.task.master.core.common.utils.constant.CacheKey;
import com.cake.task.master.core.common.utils.constant.CachedKeyUtils;
import com.cake.task.master.core.common.utils.constant.ServerConstant;
import com.cake.task.master.core.common.utils.sandpay.model.AgentPayResponse;
import com.cake.task.master.core.model.bank.*;
import com.cake.task.master.core.model.channel.ChannelBankModel;
import com.cake.task.master.core.model.channel.ChannelModel;
import com.cake.task.master.core.model.channel.ChannelWithdrawModel;
import com.cake.task.master.core.model.interest.InterestMerchantModel;
import com.cake.task.master.core.model.interest.InterestModel;
import com.cake.task.master.core.model.interest.InterestProfitModel;
import com.cake.task.master.core.model.issue.IssueModel;
import com.cake.task.master.core.model.merchant.*;
import com.cake.task.master.core.model.mobilecard.MobileCardModel;
import com.cake.task.master.core.model.mobilecard.MobileCardShortMsgModel;
import com.cake.task.master.core.model.order.OrderModel;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.*;
import com.cake.task.master.core.model.shortmsg.ShortMsgArrearsModel;
import com.cake.task.master.core.model.shortmsg.ShortMsgStrategyModel;
import com.cake.task.master.core.model.strategy.StrategyModel;
import com.cake.task.master.core.model.task.base.StatusModel;
import com.cake.task.master.core.model.withdraw.WithdrawModel;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @Description 定时任务的公共类
 * @Author yoko
 * @Date 2020/1/11 16:20
 * @Version 1.0
 */
public class TaskMethod {
    private static Logger log = LoggerFactory.getLogger(TaskMethod.class);



    /**
     * @Description: 组装查询定时任务的查询条件
     * @param limitNum - 多少条数据
     * @param runType - 运行类型
     * @param workType - 运算类型
     * @param dataType - 数据类型
     * @param greaterThan - 大于
     * @param lessThan - 小于
     * @param sendType - 发送类型
     * @param orderStatus - 订单状态
     * @param invalidTime - 失效时间
     * @return
     * @author yoko
     * @date 2020/1/11 16:23
     */
    public static StatusModel assembleTaskStatusQuery(int limitNum, int runType, int workType, int dataType, int greaterThan, int lessThan, int sendType, int orderStatus, String invalidTime){
        StatusModel resBean = new StatusModel();
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (dataType > 0){
            resBean.setDataType(dataType);
        }
        if (greaterThan > 0){
            resBean.setGreaterThan(greaterThan);
        }
        if (lessThan > 0){
            resBean.setLessThan(lessThan);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(invalidTime)){
            resBean.setInvalidTime(invalidTime);
        }
        resBean.setLimitNum(limitNum);
        return resBean;
    }

    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @param workType - 补充数据的类型：1初始化，2补充数据失败，3补充数据成功
     * @param dataType - 数据类型
     * @param sendStatus - 发送状态：0初始化，1锁定，2计算失败，3计算成功
     * @param orderStatus - 订单状态
     * @param info - 解析说明
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static StatusModel assembleTaskUpdateStatus(long id, int runStatus, int workType, int dataType,int sendStatus,int orderStatus, String info){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        if (runStatus > 0){
            resBean.setRunStatus(runStatus);
            if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (dataType > 0){
            resBean.setDataType(dataType);
        }
        if (sendStatus > 0){
            resBean.setSendStatus(sendStatus);
            if (sendStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        return resBean;
    }


    /**
     * @Description: 根据短信解析类型查询
     * @param shortMsgType
     * @return
     * @author yoko
     * @date 2020/9/13 22:07
    */
    public static ShortMsgStrategyModel assembleShortMsgStrategyByTypeQuery(int shortMsgType){
        ShortMsgStrategyModel resBean = new ShortMsgStrategyModel();
        resBean.setShortMsgTypeStr(shortMsgType);
        return resBean;
    }



    /**
     * @Description: 归类短信归属类型
     * <p>
     *     1其它短信，2欠费短信，3银行短信
     * </p>
     * @param mobileCardShortMsgModel - 短信信息
     * @param shortMsgStrategyList - 策略：检查手机短信类型的规则
     * @return
     * @author yoko
     * @date 2020/6/3 16:23
     */
    public static int screenMobileCardShortMsgType(MobileCardShortMsgModel mobileCardShortMsgModel, List<ShortMsgStrategyModel> shortMsgStrategyList){
        int type = 0;// 筛选之后的最终类型-短信内容的类型：1其它短信，2欠费短信，3银行短信
        for (ShortMsgStrategyModel shortMsgStrategyModel : shortMsgStrategyList){
            int keyType = shortMsgStrategyModel.getShortMsgType();// 策略：短信定义的类型
            String[] keyArray = shortMsgStrategyModel.getKeyValue().split("#");// 策略：短信分类的关键字
            int keyNum = shortMsgStrategyModel.getKeyNum();// 短信分类需要满足几个关键字的符合
            int countKeyNum = 0;// 计算已经满足了几个关键字的符合
            // 循环关键字匹配
            if (keyType == 1){
                // 只需要匹配一个关键字：欠费短信
                countKeyNum  = countAccordWithKey(mobileCardShortMsgModel.getSmsContent(), keyArray);// 具体筛选
                if (countKeyNum >= keyNum){
                    type = 2;// 欠费短息
                    break;
                }
            }else if(keyType == 2){
                // 银行短信
                // 需要匹配：银行短信
                countKeyNum  = countAccordWithKey(mobileCardShortMsgModel.getSmsContent(), keyArray);// 具体筛选
                if (countKeyNum >= keyNum){
                    type = 3;// 属于银行短信
                    break;
                }
            }
        }
        if (type <= 0){
            // 其它短信
            type = 1;
        }
        return type;
    }


    /**
     * @Description: 计算满足了几个关键字
     * @param content - 短信类容
     * @param keyArray - 关键字
     * @return
     * @author yoko
     * @date 2020/6/3 15:38
     */
    public static int countAccordWithKey(String content, String[] keyArray){
        int count = 0;// 计算已经满足了几个关键字的符合
        for (String str : keyArray){
            if (content.indexOf(str) > -1){
                count ++;
            }
        }
        return count;
    }

    /**
     * @Description: 组装查询手机号的查询方法
     * @param id - 主键ID
     * @param phoneNum - 手机号
     * @param isArrears - 是否欠费：1未欠费，2欠费
     * @param heartbeatStatus - 心跳状态：1初始化异常，2正常
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.hz.fruit.master.core.model.mobilecard.MobileCardModel
     * @author yoko
     * @date 2020/9/12 14:53
     */
    public static MobileCardModel assembleMobileCardQuery(long id, String phoneNum, int isArrears, int heartbeatStatus, int useStatus){
        MobileCardModel resBean = new MobileCardModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(phoneNum)){
            resBean.setPhoneNum(phoneNum);
        }
        if (isArrears > 0){
            resBean.setIsArrears(isArrears);
        }
        if (heartbeatStatus > 0){
            resBean.setHeartbeatStatus(heartbeatStatus);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: 更新所有短信的手机ID
     * @param id - 短信的主键ID
     * @param mobileCardId - 手机卡的主键ID
     * @return
     * @author yoko
     * @date 2020/9/14 14:09
    */
    public static MobileCardShortMsgModel assembleMobileCardShortMsgUpdateMobileCardId(long id, long mobileCardId){
        MobileCardShortMsgModel resBean = new MobileCardShortMsgModel();
        resBean.setId(id);
        resBean.setMobileCardId(mobileCardId);
        return resBean;
    }


    /**
     * @Description: 组装添加欠费短信
     * @param mobileCardShortMsgModel
     * @return
     * @author yoko
     * @date 2020/9/14 15:03
    */
    public static ShortMsgArrearsModel assembleShortMsgArrearsAdd(MobileCardShortMsgModel mobileCardShortMsgModel){
        ShortMsgArrearsModel resBean = new ShortMsgArrearsModel();
        if (mobileCardShortMsgModel.getMobileCardId() != null && mobileCardShortMsgModel.getMobileCardId() > 0){
            resBean.setMobileCardId(mobileCardShortMsgModel.getMobileCardId());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getPhoneNum())){
            resBean.setPhoneNum(mobileCardShortMsgModel.getPhoneNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsNum())){
            resBean.setSmsNum(mobileCardShortMsgModel.getSmsNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsContent())){
            resBean.setSmsContent(mobileCardShortMsgModel.getSmsContent());
        }else {
            return null;
        }

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }

    /**
     * @Description: 组装添加银行短信
     * @param mobileCardShortMsgModel
     * @return
     * @author yoko
     * @date 2020/9/14 15:03
     */
    public static BankShortMsgModel assembleBankShortMsgAdd(MobileCardShortMsgModel mobileCardShortMsgModel){
        BankShortMsgModel resBean = new BankShortMsgModel();
        if (mobileCardShortMsgModel.getMobileCardId() != null && mobileCardShortMsgModel.getMobileCardId() > 0){
            resBean.setMobileCardId(mobileCardShortMsgModel.getMobileCardId());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getPhoneNum())){
            resBean.setPhoneNum(mobileCardShortMsgModel.getPhoneNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsNum())){
            resBean.setSmsNum(mobileCardShortMsgModel.getSmsNum());
        }else {
            return null;
        }
        if (!StringUtils.isBlank(mobileCardShortMsgModel.getSmsContent())){
            resBean.setSmsContent(mobileCardShortMsgModel.getSmsContent());
        }else {
            return null;
        }

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装查询银行短信解析的策略数据
     * @param id - 主键ID
     * @param bankTypeId - 银行类型ID
     * @param smsNum - 短信来源端口号
     * @return BankShortMsgStrategyModel
     * @author yoko
     * @date 2020/9/14 17:12
     */
    public static BankShortMsgStrategyModel assembleBankShortMsgStrategyQuery(long id, long bankTypeId, String smsNum){
        BankShortMsgStrategyModel resBean = new BankShortMsgStrategyModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (bankTypeId > 0){
            resBean.setBankTypeId(bankTypeId);
        }
        if (!StringUtils.isBlank(smsNum)){
            resBean.setSmsNum(smsNum);
        }
        return resBean;
    }

    /**
     * @Description: 组装查询银行卡的查询条件
     * @param id - 主键ID
     * @param mobileCardId - 手机卡ID
     * @param bankTypeId - 银行类型ID
     * @param merchantId - 卡商ID
     * @param bankCard - 银行卡卡号
     * @param smsNum - 短信来源端口号
     * @param lastNum - 银行卡尾号
     * @param isOk - 是否测试通过：1未通过，2通过；收到银行卡短信，并且解析短信模板配置正确
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @param changeStatus - 换卡状态：1初始化，2待换卡（成功10多单后、或者后面订单连续几单未成功），3驳回（上线连续几单都未成功），4换卡完毕
     * @param nextBankId - 自动上线下银行卡使用：下一个银行卡ID
     * @param suffix - 表的下标位
     * @return BankModel
     * @author yoko
     * @date 2020/9/14 17:19
     */
    public static BankModel assembleBankQuery(long id, long mobileCardId, long bankTypeId, long merchantId, String bankCard, String smsNum, String lastNum,
                                              int isOk, int useStatus, int changeStatus, long nextBankId, String suffix){
        BankModel resBean = new BankModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (mobileCardId > 0){
            resBean.setMobileCardId(mobileCardId);
        }
        if (bankTypeId > 0){
            resBean.setBankTypeId(bankTypeId);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (!StringUtils.isBlank(bankCard)){
            resBean.setBankCard(bankCard);
        }
        if (!StringUtils.isBlank(smsNum)){
            resBean.setSmsNum(smsNum);
        }
        if (!StringUtils.isBlank(lastNum)){
            resBean.setLastNum(lastNum);
        }
        if (isOk > 0){
            resBean.setIsOk(isOk);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        if (changeStatus > 0){
            resBean.setChangeStatus(changeStatus);
        }
        if (nextBankId > 0){
            resBean.setNextBankId(nextBankId);
        }
        if (!StringUtils.isBlank(suffix)){
            resBean.setSuffix(suffix);
        }

        return resBean;
    }


    /**
     * @Description: 截取短信内容中的金额
     * <p>
     *     截取银行收款短信的收款金额；
     *     判断最终截取的字符串是否是金额
     * </p>
     * @param bankShortMsgStrategyList - 截取银行短信金额的数据
     * @param smsContent - 银行短信
     * @return java.lang.String
     * @author yoko
     * @date 2020/9/14 17:48
     */
    public static String getBankMoney(List<BankShortMsgStrategyModel> bankShortMsgStrategyList, String smsContent){
        String str = null;
        int startIndex = 0;
        int endIndex = 0;
        for (BankShortMsgStrategyModel bankShortMsgStrategyModel : bankShortMsgStrategyList){
            startIndex = getIndexOfByStr(smsContent, bankShortMsgStrategyModel.getStartMoney());
            if (startIndex > 0){
                startIndex = startIndex + bankShortMsgStrategyModel.getStartMoney().length();
            }else {
                continue;
            }

//            endIndex = getIndexOfByStr(smsContent, bankShortMsgStrategyModel.getEndMoney());
            endIndex = getIndexOfByStrByIndex(smsContent, bankShortMsgStrategyModel.getEndMoney(), startIndex);
            if (endIndex > 0){
            }else {
                continue;
            }

            if (startIndex <= 0 || endIndex <= 0){
                continue;
            }

            String money = smsContent.substring(startIndex, endIndex).replaceAll(",","");
            if (StringUtils.isBlank(money)){
                continue;
            }

            // 判断是否是金额
            boolean flag = StringUtil.isNumberByMoney(money);
            if (flag){
                str = money;
            }
        }
        return str;
    }

    /**
     * @Description: 获取key在内容中的下标位
     * @param content - 类容
     * @param key - 匹配的关键字
     * @return
     * @author yoko
     * @date 2020/6/4 11:18
     */
    public static int getIndexOfByStr(String content, String key){
        if (content.indexOf(key) > -1){
            return content.indexOf(key);
        }else {
            return 0;
        }
    }


    /**
     * @Description: 获取key在内容中的下标位
     * <p>
     *     从字符串的某一个下标位开始
     * </p>
     * @param content - 类容
     * @param key - 匹配的关键字
     * @return
     * @author yoko
     * @date 2020/6/4 11:18
     */
    public static int getIndexOfByStrByIndex(String content, String key, int index){
        String str = content.substring(index, content.length());
        if (str.indexOf(key) > -1){
            return str.indexOf(key) + index;
        }else {
            return 0;
        }
    }



    /**
     * @Description: 组装查询策略数据条件的方法
     * @return com.pf.play.rule.core.model.strategy.StrategyModel
     * @author yoko
     * @date 2020/5/19 17:12
     */
    public static StrategyModel assembleStrategyQuery(int stgType){
        StrategyModel resBean = new StrategyModel();
        resBean.setStgType(stgType);
        return resBean;
    }



//    /**
//     * @Description: 解析短信获取银行卡
//     * <p>
//     *     解析短信，根据短信的尾号匹配银行卡的尾号；
//     *     如果可以匹配到银行卡的尾号，则返回
//     * </p>
//     * @param bankList - 银行卡集合
//     * @param smsContent - 短信内容
//     * @param lastNumKey - 尾号开始位的关键字
//     * @return long
//     * @author yoko
//     * @date 2020/9/14 19:13
//     */
//    public static BankModel getBankIdBySmsContent(List<BankModel> bankList, String smsContent, String lastNumKey){
//        String [] lastNumKeyArr = lastNumKey.split("#");
//        for (BankModel bankModel : bankList){
//            for (String str : lastNumKeyArr){
//                int start = 0;
//                int end = 0;
//                if (smsContent.indexOf(str) > -1){
//                    start = smsContent.indexOf(str) + str.length();
//                    end = start + bankModel.getLastNum().length();
//                    // 从短信内容中截取银行卡尾号
//                    String sms_lastNum = smsContent.substring(start, end);
//                    if (!StringUtils.isBlank(sms_lastNum)){
//                        if (sms_lastNum.equals(bankModel.getLastNum())){
//                            return bankModel;
//                        }
//                    }
//                }
//            }
//        }
//        return null;
//    }



    /**
     * @Description: 解析短信获取银行卡
     * <p>
     *     解析短信，根据短信的尾号匹配银行卡的尾号；
     *     如果可以匹配到银行卡的尾号，则返回
     * </p>
     * @param bankList - 银行卡集合
     * @param smsContent - 短信内容
     * @param lastNumKey - 尾号开始位的关键字
     * @return long
     * @author yoko
     * @date 2020/9/14 19:13
     */
    public static BankModel getBankIdBySmsContent(List<BankModel> bankList, String smsContent, String lastNumKey){
        String [] lastNumKeyArr = lastNumKey.split("#");
        for (BankModel bankModel : bankList){
            for (String str : lastNumKeyArr){
                int start = 0;
                int end = 0;
                if (smsContent.indexOf(str) > -1){
                    String [] laseNumArr = bankModel.getLastNum().split("#");
                    for (String laseNumStr : laseNumArr){
                        start = smsContent.indexOf(str) + str.length();
                        end = start + laseNumStr.length();
                        // 从短信内容中截取银行卡尾号
                        String sms_lastNum = smsContent.substring(start, end);
                        if (!StringUtils.isBlank(sms_lastNum)){
                            if (sms_lastNum.equals(laseNumStr)){
                                return bankModel;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * @Description: TODO
     * @param id - 主键ID
     * @param orderNo - 订单号
     * @param bankId - 银行卡主键ID
     * @param bankTypeId - 银行卡类型
     * @param money - 收款金额
     * @param lastNum - 银行卡尾号
     * @param transferUser - 付款人姓名
     * @return BankShortMsgStrategyModel
     * @author yoko
     * @date 2020/9/14 19:59
     */
    public static BankShortMsgModel assembleBankShortMsgUpdate(long id, String orderNo, long bankId, long bankTypeId, String money, String lastNum, String transferUser){
        BankShortMsgModel resBean = new BankShortMsgModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (bankTypeId > 0){
            resBean.setBankTypeId(bankTypeId);
        }
        if (!StringUtils.isBlank(money)){
            if (money.indexOf(".") <= -1){
                money = money + ".00";
            }
            resBean.setMoney(money);
        }
        if (!StringUtils.isBlank(lastNum)){
            resBean.setLastNum(lastNum);
        }
        if (!StringUtils.isBlank(transferUser)){
            resBean.setTransferUser(transferUser);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询订单信息的查询条件
     * @param id - 订单主键ID
     * @param bankId - 银行卡ID
     * @param orderNo - 订单号
     * @param orderType - 订单类型
     * @param orderMoney - 订单金额
     * @param distributionMoney - 实际派发金额
     * @param orderStatus - 订单状态
     * @param orderStatusStr - 订单大于等于状态
     * @param replenishType - 补单类型
     * @param startTime - 创建时间的开始时间
     * @param endTime - 创建时间的结束时间
     * @return OrderModel
     * @author yoko
     * @date 2020/9/14 20:54
     */
    public static OrderModel assembleOrderQuery(long id, long bankId, String orderNo, int orderType, String orderMoney, String distributionMoney, int orderStatus,
                                                String orderStatusStr, int replenishType, String startTime, String endTime, String transferUser){
        OrderModel resBean = new OrderModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(orderMoney)){
            resBean.setOrderMoney(orderMoney);
        }
        if (!StringUtils.isBlank(distributionMoney)){
            resBean.setDistributionMoney(distributionMoney);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(orderStatusStr)){
            resBean.setOrderStatusStr(orderStatusStr);
        }
        if (replenishType > 0){
            resBean.setReplenishType(replenishType);
        }
        if (!StringUtils.isBlank(startTime) && !StringUtils.isBlank(endTime)){
            resBean.setStartTime(startTime);
            resBean.setEndTime(endTime);
        }
        if (!StringUtils.isBlank(transferUser)){
            resBean.setTransferUser(transferUser);
        }
        return resBean;
    }

    /**
     * @Description: 把订单集合的订单号汇聚成一个字符串
     * @param orderList
     * @return
     * @author yoko
     * @date 2020/9/14 21:21
    */
    public static String getOrderNoStr(List<OrderModel> orderList){
        String str = "";
        for (OrderModel orderModel : orderList){
            str += orderModel.getOrderNo() + ",";
        }
        return str;
    }

    /**
     * @Description: 更新订单号状态
     * @param id - 主键ID
     * @param orderStatus - 订单号状态
     * @return OrderModel
     * @author yoko
     * @date 2020/9/14 21:26
     */
    public static OrderModel assembleOrderUpdateStatus(long id, int orderStatus){
        OrderModel resBean = new OrderModel();
        resBean.setId(id);
        resBean.setOrderStatus(orderStatus);
        return resBean;
    }


    /**
     * @Description: 组装查询银行放量策略的查询条件
     * @param id - 主键ID
     * @param bankId - 银行卡ID
     * @param useStatus - 使用状态
     * @return BankStrategyModel
     * @author yoko
     * @date 2020/9/15 10:29
     */
    public static BankStrategyModel assembleBankStrategyQuery(long id, long bankId, int useStatus){
        BankStrategyModel resBean = new BankStrategyModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询订单信息来限制银行卡的查询条件
     * @param bankId - 银行卡ID
     * @param orderType - 支付类型
     * @param orderStatus - 订单状态
     * @param curday - 创建日期
     * @param curdayStart - 开始日期
     * @param curdayEnd - 结束日期
     * @return OrderModel
     * @author yoko
     * @date 2020/9/15 10:58
     */
    public static OrderModel assembleOrderByLimitQuery(long bankId, int orderType, int orderStatus, int curday, int curdayStart, int curdayEnd){
        OrderModel resBean = new OrderModel();
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (curday > 0){
            resBean.setCurday(curday);
        }
        if (curdayStart > 0){
            resBean.setCurdayStart(curdayStart);
        }
        if (curdayEnd > 0){
            resBean.setCurdayEnd(curdayEnd);
        }
        return resBean;
    }

    /**
     * @Description: 组装监控要查询订单的查询条件
     * @param bankId - 银行卡ID
     * @param orderType - 订单的支付类型
     * @param orderStatusStr - 订单状态
     * @param curday - 创建日期
     * @param limitNum - 查询的条数
     * @return OrderModel
     * @author yoko
     * @date 2020/9/15 16:36
     */
    public static OrderModel assembleOrderByMonitorQuery(long bankId, int orderType, String orderStatusStr, int curday, int limitNum){
        OrderModel resBean = new OrderModel();
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(orderStatusStr)){
            resBean.setOrderStatusStr(orderStatusStr);
        }
        if (curday > 0){
            resBean.setCurday(curday);
        }
        if (limitNum > 0){
            resBean.setLimitNum(limitNum);
        }
        return resBean;
    }


    /**
     * @Description: 更新银行卡信息
     * @param id - 主键ID
     * @param checkStatus - 检测状态：1初始化正常，2不正常
     * @param isArrears - 归属手机卡是否欠费：1未欠费，2欠费
     * @param dataExplain - 数据说明：检测被限制的原因:task跑日月总限制，如果被限制，连续给出订单失败会填充被限制的原因
     * @return BankModel
     * @author yoko
     * @date 2020/9/15 16:57
     */
    public static BankModel assembleBankUpdate(long id, int checkStatus,int isArrears, String dataExplain){
        BankModel resBean = new BankModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (isArrears > 0){
            resBean.setIsArrears(isArrears);
        }
        if (!StringUtils.isBlank(dataExplain)){
            resBean.setDataExplain(dataExplain);
        }
        return resBean;
    }

    /**
     * @Description: 组装添加银行收款纪录
     * @param bankId - 银行卡主键ID
     * @param orderNo - 订单号
     * @param money - 订单金额
     * @return BankCollectionModel
     * @author yoko
     * @date 2020/9/15 17:25
     */
    public static BankCollectionModel assembleBankCollectionAdd(long bankId, String orderNo, String money){
        BankCollectionModel resBean = new BankCollectionModel();
        resBean.setBankId(bankId);
        resBean.setOrderNo(orderNo);
        resBean.setMoney(money);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }

    /**
     * @Description: 更新订单状态以及补单类型
     * @param id - 主键ID
     * @param orderStatus - 订单状态
     * @param replenishType - 是否是补单：1初始化不是补单，2是补单
     * @return OrderModel
     * @author yoko
     * @date 2020/9/14 21:26
     */
    public static OrderModel assembleOrderUpdateStatusAndReplenish(long id, int orderStatus, int replenishType){
        OrderModel resBean = new OrderModel();
        resBean.setId(id);
        resBean.setOrderStatus(orderStatus);
        resBean.setReplenishType(replenishType);
        return resBean;
    }

    /**
     * @Description: 跟新手机卡欠费状态
     * @param id - 主键ID
     * @param isArrears - 是否欠费：1未欠费，2欠费
     * @return MobileCardModel
     * @author yoko
     * @date 2020/9/15 19:26
     */
    public static MobileCardModel assembleMobileCardUpdateArrears(long id, int isArrears){
        MobileCardModel resBean = new MobileCardModel();
        resBean.setId(id);
        resBean.setIsArrears(isArrears);
        return resBean;
    }

    /**
     * @Description: 手机欠费更新要涉及的银行卡
     * @param id - 主键ID
     * @param involveBank - 更新涉及到的银行卡
     * @return ShortMsgArrearsModel
     * @author yoko
     * @date 2020/9/15 19:35
     */
    public static ShortMsgArrearsModel assembleShortMsgArrearsUpdateBank(long id, String involveBank){
        ShortMsgArrearsModel resBean = new ShortMsgArrearsModel();
        resBean.setId(id);
        resBean.setInvolveBank(involveBank);
        return resBean;
    }

    /**
     * @Description: 银行卡ID组合成字符串
     * @param bankList
     * @return
     * @author yoko
     * @date 2020/9/15 19:41
    */
    public static String assembleInvolveBank(List<BankModel> bankList){
        String str = "";
        if (bankList != null && bankList.size() > 0){
            for (BankModel bankModel : bankList){
                str += bankModel.getId() + ",";
            }
        }
        return str;
    }

    /**
     * @Description: 组装查询卡商的扩展信息
     * @param id - 主键ID
     * @param useStatus - 使用状态
     * @return MerchantModel
     * @author yoko
     * @date 2020/9/15 20:28
     */
    public static MerchantModel assembleMerchantQuery(long id, int useStatus){
        MerchantModel resBean = new MerchantModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: 组装更新卡商的余额的方法
     * @param id - 主键ID
     * @param orderMoney - 订单金额
     * @return MerchantModel
     * @author yoko
     * @date 2020/9/15 20:41
     */
    public static MerchantModel assembleMerchantUpdateBalance(long id, String orderMoney){
        MerchantModel resBean = new MerchantModel();
        resBean.setId(id);
        BigDecimal bd = new BigDecimal(orderMoney);
        resBean.setMoney(bd);
        return resBean;
    }

    /**
     * @Description: 组装更新卡商的金额的方法
     * @param orderMoney - 订单金额
     * @return MerchantModel
     * @author yoko
     * @date 2020/9/15 20:41
     */
    public static MerchantModel assembleMerchantUpdateMoney(long id, String orderMoney){
        MerchantModel resBean = new MerchantModel();
        resBean.setId(id);
        log.info("");
        BigDecimal bd = new BigDecimal(orderMoney);
        resBean.setMoney(bd);
        return resBean;
    }


    /**
     * @Description: 组装查询下发的查询条件
     * @param limitNum
     * @param runType
     * @param sendType
     * @param orderStatus - 订单状态：1初始化，2超时/失败/审核驳回，3成功
     * @param ascriptionType - 订单分配归属类型：1归属卡商，2归属平台
     * @param isDistribution - 是否已分配完毕归属：1初始化/未分配，2已分配
     * @param isComplete - 是否已归集完毕：1初始化/未归集完毕，2已归集完毕；此状态：是归属类型属于平台方，平台方需要向卡商发布充值订单，发布完毕，如果卡商都已经充值完毕到我方卡，则修改此状态，修改成归集完毕的状态
     * @param checkStatus - 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @param whereCheckStatus - SQL查询条件 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @return com.hz.fruit.master.core.model.issue.IssueModel
     * @author yoko
     * @date 2020/9/23 15:03
     */
    public static IssueModel assembleIssueQuery(int limitNum, int runType, int sendType, int orderStatus, int ascriptionType, int isDistribution, int isComplete, int checkStatus, int whereCheckStatus){
        IssueModel resBean = new IssueModel();
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (ascriptionType > 0){
            resBean.setAscriptionType(ascriptionType);
        }
        if (isDistribution > 0){
            resBean.setIsDistribution(isDistribution);
        }
        if (isComplete > 0){
            resBean.setIsComplete(isComplete);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (whereCheckStatus > 0){
            resBean.setWhereCheckStatus(whereCheckStatus);
        }
        resBean.setLimitNum(limitNum);
        return resBean;
    }


    /**
     * @Description: 组装查询卡商充值的信息
     * @param id - 主键ID
     * @param merchantId - 归属的账号ID：对应表tb_fr_merchant的主键ID，并且角色类型是卡商
     * @param orderNo - 订单号
     * @param orderType - 订单类型：1预付款订单，2平台发起订单，3下发订单
     * @param issueOrderNo - 下发表的订单号：对应表tb_fr_issue的order_no；也可以把它称之为关联订单号
     * @param orderStatus - 订单状态：1初始化，2超时/失败/审核驳回，3成功
     * @param operateStatus - 操作状态：1初始化，2系统放弃，3手动放弃，4锁定
     * @param isSynchro - 是否需要数据同步：1不需要同步，2需要同步
     * @param checkStatus - 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @param checkInfo - 审核失败缘由，审核失败的原因
    * @param invalidTime
     * @return com.hz.fruit.master.core.model.merchant.MerchantRechargeModel
     * @author yoko
     * @date 2020/9/23 17:16
     */
    public static MerchantRechargeModel assembleMerchantRechargeQuery(long id, long merchantId, String orderNo, int orderType, String issueOrderNo,
                                                                      int orderStatus, int operateStatus,
                                                                      int isSynchro, int checkStatus, String checkInfo, String invalidTime, String operateStatusStr){
        MerchantRechargeModel resBean = new MerchantRechargeModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(issueOrderNo)){
            resBean.setIssueOrderNo(issueOrderNo);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (operateStatus > 0){
            resBean.setOperateStatus(operateStatus);
        }
        if (isSynchro > 0){
            resBean.setIsSynchro(isSynchro);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (!StringUtils.isBlank(checkInfo)){
            resBean.setCheckInfo(checkInfo);
        }
        if (!StringUtils.isBlank(invalidTime)){
            resBean.setInvalidTime(invalidTime);
        }
        if (!StringUtils.isBlank(operateStatusStr)){
            resBean.setOperateStatusStr(operateStatusStr);
        }
        return resBean;
    }

    /**
     * @Description: 组装查询卡商扩充数据的查询方法
     * @param id - 主键ID
     * @param money - 金额
     * @param merchantType - 卡商类型：1我方卡商，2第三方卡商
     * @param operateType - 卡商运营类型/运营性质：1代收，2代付
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @param merchantIdList - 卡商账号ID集合
     * @return MerchantModel
     * @author yoko
     * @date 2020/9/23 20:30
     */
    public static MerchantModel assembleMerchantQuery(long id, String money, int merchantType, int operateType, int useStatus, List<Long> merchantIdList){
        MerchantModel resBean = new MerchantModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(money)){
            BigDecimal bd = new BigDecimal(money);
            resBean.setMoney(bd);
        }
        if (merchantType > 0){
            resBean.setMerchantType(merchantType);
        }
        if (operateType > 0){
            resBean.setOperateType(operateType);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        if (merchantIdList != null && merchantIdList.size() > 0){
            resBean.setIdList(merchantIdList);
        }
        return resBean;
    }

    /**
     * @Description: 组装查询下发的更新方法
     * @param id - 主键ID
     * @param orderNo - 订单号
     * @param outTradeNo - 支付平台订单号：下游上报的订单号
     * @param orderStatus - 订单状态：1初始化，2超时/失败/审核驳回，3成功
     * @param pictureAds - 转账成功图片凭证
     * @param myBankInfo - 我方银行卡信息备注：假如归属类型：我方/平台，填写我方银行卡的信息
     * @param ascriptionType - 订单分配归属类型：1归属卡商，2归属平台
     * @param isDistribution - 是否已分配完毕归属：1初始化/未分配，2已分配
     * @param isComplete - 是否已归集完毕：1初始化/未归集完毕，2已归集完毕；此状态：是归属类型属于平台方，平台方需要向卡商发布充值订单，发布完毕，如果卡商都已经充值完毕到我方卡，则修改此状态，修改成归集完毕的状态
     * @param checkStatus - 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @param checkInfo -  审核失败缘由，审核失败的原因
     * @param dataExplain - 数据说明：做解说用的
     * @param whereCheckStatus - SQL查询条件 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @return com.hz.fruit.master.core.model.issue.IssueModel
     * @author yoko
     * @date 2020/9/23 15:03
     */
    public static IssueModel assembleIssueUpdate(long id, String orderNo, String outTradeNo, int orderStatus, String pictureAds,
                                                 String myBankInfo, int ascriptionType, int isDistribution, int isComplete, int checkStatus, String checkInfo,
                                                 String dataExplain, int whereCheckStatus){
        IssueModel resBean = new IssueModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (!StringUtils.isBlank(outTradeNo)){
            resBean.setOutTradeNo(outTradeNo);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(pictureAds)){
            resBean.setPictureAds(pictureAds);
        }
        if (!StringUtils.isBlank(myBankInfo)){
            resBean.setMyBankInfo(myBankInfo);
        }
        if (ascriptionType > 0){
            resBean.setAscriptionType(ascriptionType);
        }
        if (isDistribution > 0){
            resBean.setIsDistribution(isDistribution);
        }
        if (isComplete > 0){
            resBean.setIsComplete(isComplete);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (!StringUtils.isBlank(checkInfo)){
            resBean.setCheckInfo(checkInfo);
        }
        if (!StringUtils.isBlank(dataExplain)){
            resBean.setDataExplain(dataExplain);
        }
        if (whereCheckStatus > 0){
            resBean.setWhereCheckStatus(whereCheckStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装添加卡商充值的信息
     * @param merchantId - 归属的账号ID：对应表tb_fr_merchant的主键ID，并且角色类型是卡商
     * @param orderNo - 订单号
     * @param orderType - 订单类型：1预付款订单，2平台发起订单，3下发订单
     * @param issueOrderNo - 下发表的订单号：对应表tb_fr_issue的order_no；也可以把它称之为关联订单号
     * @param orderMoney - 订单金额
     * @param bankName - 银行名称
     * @param bankCard - 银行卡卡号
     * @param accountName - 银行开户人
     * @param isSynchro - 是否需要数据同步：1不需要同步，2需要同步
     * @param invalidTime - 系统运行自动放弃的时间：订单分配完毕之后，订单类型是：下发分配订单，如果卡商在超过这个时间没有进行放弃或者锁定这样的操作，则自动修改成放弃。
    * @param invalidTime
     * @return com.hz.fruit.master.core.model.merchant.MerchantRechargeModel
     * @author yoko
     * @date 2020/9/23 17:16
     */
    public static MerchantRechargeModel assembleMerchantRechargeAdd(long merchantId, String orderNo, int orderType, String issueOrderNo,
                                                                    String orderMoney, String bankName, String bankCard, String accountName, int isSynchro,
                                                                    String invalidTime){
        MerchantRechargeModel resBean = new MerchantRechargeModel();
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (!StringUtils.isBlank(issueOrderNo)){
            resBean.setIssueOrderNo(issueOrderNo);
        }
        if (!StringUtils.isBlank(orderMoney)){
            resBean.setOrderMoney(orderMoney);
        }
        if (!StringUtils.isBlank(bankName)){
            resBean.setBankName(bankName);
        }
        if (!StringUtils.isBlank(bankCard)){
            resBean.setBankCard(bankCard);
        }
        if (!StringUtils.isBlank(accountName)){
            resBean.setAccountName(accountName);
        }
        if (isSynchro > 0){
            resBean.setIsSynchro(isSynchro);
        }
        if (!StringUtils.isBlank(invalidTime)){
            resBean.setInvalidTime(invalidTime);
        }
        return resBean;
    }


    /**
     * @Description: 组装更改运行状态的数据
     * @param id - 主键ID
     * @param runStatus - 运行计算状态：：0初始化，1锁定，2计算失败，3计算成功
     * @param sendStatus - 发送状态：0初始化，1锁定，2计算失败，3计算成功
     * @param orderStatus - 订单状态
     * @return StatusModel
     * @author yoko
     * @date 2019/12/10 10:42
     */
    public static IssueModel assembleIssueUpdateStatus(long id, int runStatus, int sendStatus,int orderStatus){
        IssueModel resBean = new IssueModel();
        resBean.setId(id);
        if (runStatus > 0){
            resBean.setRunStatus(runStatus);
            if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (sendStatus > 0){
            resBean.setSendStatus(sendStatus);
            if (sendStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装task查询卡商充值的信息
     * @param limitNum - 查询的条数
     * @param runType - 运行类型
     * @param sendType - 发送类型
     * @param orderType - 订单类型：1预付款订单，2平台发起订单，3下发订单
     * @param orderStatus - 订单状态：1初始化，2超时/失败/审核驳回，3成功
     * @param operateStatus - 操作状态：1初始化，2系统放弃，3手动放弃，4锁定
     * @param isSynchro - 是否需要数据同步：1不需要同步，2需要同步
     * @param checkStatus - 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @param invalidTimeStr - 超时时间
     * @return com.hz.fruit.master.core.model.merchant.MerchantRechargeModel
     * @author yoko
     * @date 2020/9/23 17:16
     */
    public static MerchantRechargeModel assembleMerchantRechargeByTaskQuery(int limitNum, int runType, int sendType, int orderType, int orderStatus, int operateStatus,
                                                                      String operateStatusStr, int isSynchro, int checkStatus,  String invalidTimeStr){
        MerchantRechargeModel resBean = new MerchantRechargeModel();
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (operateStatus > 0){
            resBean.setOperateStatus(operateStatus);
        }
        if (!StringUtils.isBlank(operateStatusStr)){
            resBean.setOperateStatusStr(operateStatusStr);
        }
        if (isSynchro > 0){
            resBean.setIsSynchro(isSynchro);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (!StringUtils.isBlank(invalidTimeStr)){
            resBean.setInvalidTimeStr(invalidTimeStr);
        }
        resBean.setLimitNum(limitNum);
        return resBean;
    }


    /**
     * @Description: 根据订单号组装查询方法
     * @param orderNo - 订单号
     * @return
     * @author yoko
     * @date 2020/9/24 20:56
    */
    public static IssueModel assembleIssueByOrderQuery(String orderNo){
        IssueModel resBean = new IssueModel();
        resBean.setOrderNo(orderNo);
        return resBean;
    }


    /**
     * @Description: 组装更新卡商充值订单的操作状态
     * @param id - 主键ID
     * @param operateStatus - 要更新的操作状态
     * @param whereOperateStatus - 更新的where条件
     * @return MerchantRechargeModel
     * @author yoko
     * @date 2020/9/24 22:04
     */
    public static MerchantRechargeModel assembleMerchantRechargeUpdateOperate(long id, int operateStatus, int whereOperateStatus){
        MerchantRechargeModel resBean = new MerchantRechargeModel();
        resBean.setId(id);
        resBean.setOperateStatus(operateStatus);
        resBean.setWhereOperateStatus(whereOperateStatus);
        return resBean;
    }


    /**
     * @Description: 组装更新分配状态的方法
     * @param id - 主键ID
     * @param isDistribution - 是否已分配完毕归属：1初始化/未分配，2已分配
     * @param whereIsDistribution - 更新是否分配完毕的更新where条件
     * @return IssueModel
     * @author yoko
     * @date 2020/9/24 22:08
     */
    public static IssueModel assembleIssueUpdateDistribution(long id, int isDistribution, int whereIsDistribution){
        IssueModel resBean = new IssueModel();
        resBean.setId(id);
        resBean.setIsDistribution(isDistribution);
        resBean.setWhereIsDistribution(whereIsDistribution);
        return resBean;
    }


    /**
     * @Description: 组装查询执行操作状态放弃的方法
     * @param limitNum - 执行条数
     * @param operateStatusStart - 操作状态-开始
     * @param operateStatusEnd - 操作状态-结束
     * @return MerchantRechargeModel
     * @author yoko
     * @date 2020/9/25 10:05
     */
    public static MerchantRechargeModel assembleMerchantRechargeTaskByInvalidQuery(int limitNum, int operateStatusStart, int operateStatusEnd){
        MerchantRechargeModel resBean = new MerchantRechargeModel();
        resBean.setLimitNum(limitNum);
        resBean.setOperateStatusStart(operateStatusStart);
        resBean.setOperateStatusEnd(operateStatusEnd);

        resBean.setInvalidStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
        resBean.setInvalidNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        return resBean;
    }

    /**
     * @Description: 组装更新失效运算的方法
     * @param id
     * @param invalidStatus
     * @param info
     * @return StatusModel
     * @author yoko
     * @date 2020/9/25 11:07
     */
    public static StatusModel assembleStatusUpdateInvalid(long id, int invalidStatus, String info){
        StatusModel resBean = new StatusModel();
        resBean.setId(id);
        if (invalidStatus > 0){
            resBean.setInvalidStatus(invalidStatus);
            if (invalidStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setInvalidNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (!StringUtils.isBlank(info)){
            resBean.setInfo(info);
        }
        return resBean;
    }


    /**
     * @Description: 组装根据订单号更新卡商扣款流水的订单的状态的方法
     * @param orderNo - 订单号
     * @param orderStatus - 订单状态
     * @return com.cake.task.master.core.model.merchant.MerchantBalanceDeductModel
     * @author yoko
     * @date 2020/11/9 15:18
     */
    public static MerchantBalanceDeductModel assembleMerchantBalanceDeductUpdateByOrderNo(String orderNo, int orderStatus){
        MerchantBalanceDeductModel resBean = new MerchantBalanceDeductModel();
        resBean.setOrderNo(orderNo);
        resBean.setOrderStatus(orderStatus);
        return resBean;
    }


    /**
     * @Description: 组装代付订单的卡商的收益信息
     * @param orderOutModel - 代付订单信息
     * @param orderType - 订单类型：1代收订单，2代付订单，3其它角色提现订单
     * @return com.cake.task.master.core.model.merchant.MerchantProfitModel
     * @author yoko
     * @date 2020/11/10 13:50
     */
    public static MerchantProfitModel assembleMerchantProfitByOrderOutAdd(OrderOutModel orderOutModel, int orderType){
        if (!StringUtils.isBlank(orderOutModel.getServiceCharge())){
            if (!orderOutModel.getServiceCharge().equals("0")){
                MerchantProfitModel resBean = new MerchantProfitModel();
                resBean.setOrderNo(orderOutModel.getOrderNo());
                resBean.setOrderType(orderType);
                resBean.setOrderMoney(orderOutModel.getOrderMoney());
//        resBean.setDistributionMoney("1");
                resBean.setServiceCharge(orderOutModel.getServiceCharge());
//        resBean.setReplenishType(1);
                resBean.setProfitRatio(orderOutModel.getServiceCharge());
                String profit = StringUtil.getMultiplyMantissa(orderOutModel.getOrderMoney(), orderOutModel.getServiceCharge(), 4);
                resBean.setProfit(profit);
                resBean.setMerchantId(orderOutModel.getMerchantId());
                resBean.setCurday(DateUtil.getDayNumber(new Date()));
                resBean.setCurhour(DateUtil.getHour(new Date()));
                resBean.setCurminute(DateUtil.getCurminute(new Date()));
                return resBean;
            }else {
                return null;
            }
        }else {
            return null;
        }
    }

    /**
     * @Description: 组装利益分配者与卡商绑定的查询方法
     * @param id - 主键ID
     * @param interestId - 利益者ID
     * @param merchantId - 卡商ID
     * @param useStatus -  使用状态
     * @return com.cake.task.master.core.model.interest.InterestMerchantModel
     * @author yoko
     * @date 2020/11/10 13:54
     */
    public static InterestMerchantModel assembleInterestMerchantQuery(long id, long interestId, long merchantId, int useStatus){
        InterestMerchantModel resBean = new InterestMerchantModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (interestId > 0){
            resBean.setInterestId(interestId);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: 根据代付订单组装利益者的利益信息
     * @param interestMerchantList - 利益分配者与卡商绑定
     * @param orderOutModel - 代付订单信息
     * @param orderType - 订单类型：1代收订单，2代付订单
     * @return java.util.List<com.cake.task.master.core.model.interest.InterestProfitModel>
     * @author yoko
     * @date 2020/11/10 14:04
     */
    public static List<InterestProfitModel> assembleInterestProfitListByOrderOut(List<InterestMerchantModel> interestMerchantList,
                                                                                 OrderOutModel orderOutModel, int orderType){
        List<InterestProfitModel> resList = new ArrayList<>();
        if (interestMerchantList == null || interestMerchantList.size() <= 0){
            return null;
        }
        for (InterestMerchantModel interestMerchantModel : interestMerchantList){
            InterestProfitModel interestProfitModel = new InterestProfitModel();
            interestProfitModel.setOrderNo(orderOutModel.getOrderNo());
            interestProfitModel.setOrderType(orderType);
            interestProfitModel.setOrderMoney(orderOutModel.getOrderMoney());
            interestProfitModel.setDistributionMoney("");
            interestProfitModel.setServiceCharge(orderOutModel.getServiceCharge());
            interestProfitModel.setReplenishType(1);
            interestProfitModel.setProfitRatio(interestMerchantModel.getServiceCharge());
            String profit = StringUtil.getMultiplyMantissa(orderOutModel.getOrderMoney(), interestMerchantModel.getServiceCharge(), 4);
            interestProfitModel.setProfit(profit);
            interestProfitModel.setInterestId(interestMerchantModel.getInterestId());
            interestProfitModel.setMerchantId(orderOutModel.getMerchantId());
            interestProfitModel.setCurday(DateUtil.getDayNumber(new Date()));
            interestProfitModel.setCurhour(DateUtil.getHour(new Date()));
            interestProfitModel.setCurminute(DateUtil.getCurminute(new Date()));
            resList.add(interestProfitModel);
        }
        return resList;

    }


    /**
     * @Description: 组装变更卡商余额/收益的方法
     * @param id - 卡商主键ID
     * @param type - 余额加减：1加，2减
     * @param money - 变更的金额值
     * @return com.hz.cake.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/10/30 19:57
     */
    public static MerchantModel assembleMerchantByChanagerMoney(long id, int type, String money){
        MerchantModel resBean = new MerchantModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (type > 0){
            if (type == 1){
                resBean.setAddBalance("1");
            }else if (type == 2){
                resBean.setSubtractBalance("1");
            }
        }
        if (!StringUtils.isBlank(money)){
            BigDecimal bd = new BigDecimal(money);
            resBean.setMoney(bd);
        }
        return resBean;
    }


    /**
     * @Description: 组装变更利益者余额/收益的方法
     * @param id - 利益者主键ID
     * @param type - 余额加减：1加，2减
     * @param money - 变更的金额值
     * @return com.hz.cake.master.core.model.merchant.MerchantModel
     * @author yoko
     * @date 2020/10/30 19:57
     */
    public static InterestModel assembleInterestByChanagerMoney(long id, int type, String money){
        InterestModel resBean = new InterestModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (type > 0){
            if (type == 1){
                resBean.setAddBalance("1");
            }else if (type == 2){
                resBean.setSubtractBalance("1");
            }
            log.info("");
        }
        if (!StringUtils.isBlank(money)){
            BigDecimal bd = new BigDecimal(money);
            resBean.setMoney(bd);
        }
        return resBean;
    }


    /**
     * @Description: 组装代收订单的卡商的收益信息
     * <p>
     *     当订单的手续费收益为空，则卡商的收益没有
     * </p>
     * @param orderModel - 代收订单信息
     * @param orderType - 订单类型：1代收订单，2代付订单，3其它角色提现订单
     * @return com.cake.task.master.core.model.merchant.MerchantProfitModel
     * @author yoko
     * @date 2020/11/10 13:50
     */
    public static MerchantProfitModel assembleMerchantProfitByOrderAdd(OrderModel orderModel, int orderType){
        if (!StringUtils.isBlank(orderModel.getServiceCharge())){
            if (!orderModel.getServiceCharge().equals("0")){
                MerchantProfitModel resBean = new MerchantProfitModel();
                resBean.setOrderNo(orderModel.getOrderNo());
                resBean.setOrderType(orderType);
                resBean.setOrderMoney(orderModel.getOrderMoney());
                resBean.setDistributionMoney(orderModel.getDistributionMoney());
                resBean.setServiceCharge(orderModel.getServiceCharge());
                resBean.setReplenishType(orderModel.getReplenishType());
                resBean.setProfitRatio(orderModel.getServiceCharge());
                String profit = StringUtil.getMultiplyMantissa(orderModel.getOrderMoney(), orderModel.getServiceCharge(), 4);
                resBean.setProfit(profit);
                resBean.setMerchantId(orderModel.getMerchantId());
                resBean.setCurday(DateUtil.getDayNumber(new Date()));
                resBean.setCurhour(DateUtil.getHour(new Date()));
                resBean.setCurminute(DateUtil.getCurminute(new Date()));
                return resBean;
            }else{
                return null;
            }
        }else {
            return null;
        }

    }


    /**
     * @Description: 根据代收订单组装利益者的利益信息
     * @param interestMerchantList - 利益分配者与卡商绑定
     * @param orderModel - 代收订单信息
     * @param orderType - 订单类型：1代收订单，2代付订单
     * @return java.util.List<com.cake.task.master.core.model.interest.InterestProfitModel>
     * @author yoko
     * @date 2020/11/10 14:04
     */
    public static List<InterestProfitModel> assembleInterestProfitListByOrder(List<InterestMerchantModel> interestMerchantList,
                                                                                 OrderModel orderModel, int orderType){
        List<InterestProfitModel> resList = new ArrayList<>();
        if (interestMerchantList == null || interestMerchantList.size() <= 0){
            return null;
        }
        for (InterestMerchantModel interestMerchantModel : interestMerchantList){
            InterestProfitModel interestProfitModel = new InterestProfitModel();
            interestProfitModel.setOrderNo(orderModel.getOrderNo());
            interestProfitModel.setOrderType(orderType);
            interestProfitModel.setOrderMoney(orderModel.getOrderMoney());
            interestProfitModel.setDistributionMoney(orderModel.getDistributionMoney());
            interestProfitModel.setServiceCharge(orderModel.getServiceCharge());
            interestProfitModel.setReplenishType(1);
            interestProfitModel.setProfitRatio(interestMerchantModel.getServiceCharge());
            String profit = StringUtil.getMultiplyMantissa(orderModel.getOrderMoney(), interestMerchantModel.getServiceCharge(), 4);
            interestProfitModel.setProfit(profit);
            interestProfitModel.setInterestId(interestMerchantModel.getInterestId());
            interestProfitModel.setMerchantId(orderModel.getMerchantId());
            interestProfitModel.setCurday(DateUtil.getDayNumber(new Date()));
            interestProfitModel.setCurhour(DateUtil.getHour(new Date()));
            interestProfitModel.setCurminute(DateUtil.getCurminute(new Date()));
            resList.add(interestProfitModel);
        }
        return resList;

    }

    /**
     * @Description: 组装添加汇总到提现记录的数据
     * @param channelWithdrawModel - 渠道提现记录
     * @param withdrawType - 提现订单类型：1利益者提现，2卡商提现，3渠道提现
     * @return com.cake.task.master.core.model.withdraw.WithdrawModel
     * @author yoko
     * @date 2020/11/19 17:37
     */
    public static WithdrawModel assembleWithdrawByChannel(ChannelWithdrawModel channelWithdrawModel, int withdrawType){
        WithdrawModel resBean = new WithdrawModel();
        resBean.setOrderNo(channelWithdrawModel.getOrderNo());
        resBean.setOrderMoney(channelWithdrawModel.getMoney());
        resBean.setWithdrawServiceCharge(channelWithdrawModel.getWithdrawServiceCharge());
        resBean.setWithdrawType(withdrawType);
        if (channelWithdrawModel.getChannelId() != null && channelWithdrawModel.getChannelId() > 0){
            resBean.setChannelId(channelWithdrawModel.getChannelId());
        }
        if (!StringUtils.isBlank(channelWithdrawModel.getChannelName())){
            resBean.setChannelName(channelWithdrawModel.getChannelName());
        }
        if (channelWithdrawModel.getChannelType() != null && channelWithdrawModel.getChannelType() > 0){
            resBean.setChannelType(channelWithdrawModel.getChannelType());
        }
        resBean.setInBankCard(channelWithdrawModel.getInBankCard());
        resBean.setInBankName(channelWithdrawModel.getInBankName());
        resBean.setInAccountName(channelWithdrawModel.getInAccountName());
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装更新平台提现订单的状态
     * @param channelWithdrawModel - 渠道提现记录
     * @param withdrawStatus - 提现状态:1提现中，2提现失败，3提现成功
     * @return com.cake.task.master.core.model.channel.ChannelWithdrawModel
     * @author yoko
     * @date 2020/11/19 19:11
     */
    public static ChannelWithdrawModel assembleUpdateWithdrawStatus(ChannelWithdrawModel channelWithdrawModel, int withdrawStatus){
        ChannelWithdrawModel resBean = new ChannelWithdrawModel();
        resBean.setOrderNo(channelWithdrawModel.getOutTradeNo());
        resBean.setWithdrawStatus(withdrawStatus);
        if (!StringUtils.isBlank(channelWithdrawModel.getPictureAds())){
            resBean.setPictureAds(channelWithdrawModel.getPictureAds());
        }
        return resBean;
    }


    /**
     * @Description: 判断组装卡商的收益数据
     * @param withdrawModel - 提现记录
     * @return
     * @author yoko
     * @date 2020/11/19 19:50
    */
    public static MerchantProfitModel assembleMerchantProfitByWithdraw(WithdrawModel withdrawModel){
        if (withdrawModel.getOrderStatus() != 4){
            // 订单状态不是成功状态
            return null;
        }
        if (StringUtils.isBlank(withdrawModel.getWithdrawServiceCharge())){
            // 手续费的值是空的
            return null;
        }

        MerchantProfitModel resBean = new MerchantProfitModel();
        resBean.setOrderNo(withdrawModel.getOrderNo());
        resBean.setOrderType(3);
        resBean.setOrderMoney(withdrawModel.getOrderMoney());
        resBean.setDistributionMoney(withdrawModel.getOrderMoney());
        resBean.setServiceCharge(withdrawModel.getWithdrawServiceCharge());
        resBean.setReplenishType(1);
        resBean.setProfitRatio("");
        resBean.setProfit(withdrawModel.getWithdrawServiceCharge());
        resBean.setMerchantId(withdrawModel.getMerchantId());
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装渠道提现的订单状态更改
     * @param withdrawModel - 汇总的提现记录
     * @return com.cake.task.master.core.model.channel.ChannelWithdrawModel
     * @author yoko
     * @date 2020/11/19 20:05
     */
    public static ChannelWithdrawModel assembleChannelWithdrawOrderStatusUpdate(WithdrawModel withdrawModel){
        ChannelWithdrawModel resBean = new ChannelWithdrawModel();
        resBean.setOrderNo(withdrawModel.getOrderNo());
        resBean.setOrderStatus(withdrawModel.getOrderStatus());
        if (!StringUtils.isBlank(withdrawModel.getOutBankName())){
            resBean.setOutBankName(withdrawModel.getOutBankName());
        }
        if (!StringUtils.isBlank(withdrawModel.getOutBankCard())){
            resBean.setOutBankCard(withdrawModel.getOutBankCard());
        }
        if (!StringUtils.isBlank(withdrawModel.getOutAccountName())){
            resBean.setOutAccountName(withdrawModel.getOutAccountName());
        }
        if (!StringUtils.isBlank(withdrawModel.getPictureAds())){
            resBean.setPictureAds(withdrawModel.getPictureAds());
        }
        return resBean;
    }


    /**
     * @Description: 组装查询渠道的查询方法
     * @param id - 主键ID
     * @param secretKey - 渠道秘钥
     * @param bankBindingType - 银行卡绑定类型：1无需绑定银行卡，2需要绑定银行卡
     * @param channelType - 渠道类型：1代收，2大包，3代付
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.cake.task.master.core.model.channel.ChannelModel
     * @author yoko
     * @date 2020/12/1 17:41
     */
    public static ChannelModel assembleChannelQuery(long id, String secretKey, int bankBindingType, int channelType, int useStatus){
        ChannelModel resBean = new ChannelModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(secretKey)){
            resBean.setSecretKey(secretKey);
        }
        if (bankBindingType > 0){
            resBean.setBankBindingType(bankBindingType);
        }
        if (channelType > 0){
            resBean.setChannelType(channelType);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }

    /**
     * @Description: 组装填充渠道主键ID
     * @param id - 主键ID
     * @param channelId - 渠道ID
     * @return com.cake.task.master.core.model.channel.ChannelWithdrawModel
     * @author yoko
     * @date 2020/12/1 17:47
     */
    public static ChannelWithdrawModel assembleChannelWithdrawUpdateChannel(long id, long channelId){
        ChannelWithdrawModel resBean = new ChannelWithdrawModel();
        resBean.setId(id);
        resBean.setChannelId(channelId);
        return resBean;
    }

    /**
     * @Description: 组装根据渠道ID与银行卡绑定关系查询卡商信息
     * @param channelId - 渠道ID
     * @return MerchantModel
     * @author yoko
     * @date 2020/9/15 20:28
     */
    public static MerchantModel assembleMerchantByChannelQuery(long channelId){
        MerchantModel resBean = new MerchantModel();
        resBean.setChannelId(channelId);
        return resBean;
    }


    /**
     * @Description: 组装查询提现汇总的查询条件的方法
     * @param id - 主键ID
     * @param orderNo - 订单号
     * @param outTradeNo - 第三方订单号
     * @param orderStatus - 订单状态：1初始化，2超时，3质疑，4成功
     * @param withdrawType - 提现订单类型：1利益者提现，2卡商提现，3渠道提现
     * @param channelId - 渠道主键ID
     * @param channelType - 渠道类型：0初始化，1代收，2大包，3代付；订单类型是渠道提现时，这里做自动分派是需要用到
     * @param outType - 指派由谁进行转账给提现人：1卡商，2中转站，3平台
     * @param merchantId - 指派给的卡商ID：对应表tb_fr_merchant的主键ID；假如指派out_type=1，则此字段不允许为空
     * @param checkStatus - 审核状态：1初始化，2审核收款失败，3审核收款成功
     * @param workType - 补充数据的类型：1初始化，2补充数据失败（其它原因等..），3补充数据成功；这里是派单状态
     * @param curday - 创建日期
     * @return com.cake.task.master.core.model.withdraw.WithdrawModel
     * @author yoko
     * @date 2020/12/2 16:33
     */
    public static WithdrawModel assembleWithdrawQuery(long id, String orderNo, String outTradeNo, int orderStatus, int withdrawType, long channelId, int channelType,
                                                      int outType, long merchantId, int checkStatus, int workType, int curday){
        WithdrawModel resBean = new WithdrawModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (!StringUtils.isBlank(orderNo)){
            resBean.setOrderNo(orderNo);
        }
        if (!StringUtils.isBlank(outTradeNo)){
            resBean.setOutTradeNo(outTradeNo);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (withdrawType > 0){
            resBean.setWithdrawType(withdrawType);
        }
        if (channelId > 0){
           resBean.setChannelId(channelId);
        }
        if (channelType > 0){
            resBean.setChannelType(channelType);
        }
        if (outType > 0){
            resBean.setOutType(outType);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (workType > 0){
            resBean.setWorkType(workType);
        }
        if (curday > 0){
            resBean.setCurday(curday);
        }
        return resBean;
    }


    /**
     * @Description: check卡商是否有足够的余额来分配此提现订单
     * <p>
     *     这里有一个计算公式：要进行分配的提现金额 + 卡商已经分配但是未处理的提现金额 < 卡商现有余额
     * </p>
     * @param balance - 余额
     * @param orderMoney - 要分配的下发的金额
     * @param withdrawMoney - 已经分配完毕但是还未操作的提现下发金额
     * @return boolean
     * @author yoko
     * @date 2020/12/2 16:47
     */
    public static boolean checkMerchantMoney(String balance, String orderMoney, String withdrawMoney){
        if (StringUtils.isBlank(balance)){
            return false;
        }
        String addMoney = StringUtil.getBigDecimalAdd(orderMoney, withdrawMoney);
        return StringUtil.getBigDecimalSubtract(balance, addMoney);
    }


    /**
     * @Description: 组装查询渠道与银行卡绑定关系的查询方法
     * @param id - 主键ID
     * @param channelId - 渠道ID
     * @param bankId - 银行卡ID
     * @param useStatus - 使用状态
     * @return com.cake.task.master.core.model.channel.ChannelBankModel
     * @author yoko
     * @date 2020/12/10 14:48
     */
    public static ChannelBankModel assembleChannelBankQuery(long id, long channelId, long bankId, int useStatus){
        ChannelBankModel resBean = new ChannelBankModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;

    }



    /**
     * @Description: 截取短信内容中的余额
     * <p>
     *     1.判断短信内容中是否有余额的关键字。
     *     2.有余额关键字，那就根于余额开始结束来截取银行卡目前的余额值
     * </p>
     * @param balanceKey - 策略截取银行短信余额的数据
     * @param matching - 匹配规则值如下：余额%【中国银行】#余额%元#余额%。#余额为%元
     * @param smsContent - 银行短信
     * @return java.lang.String
     * @author yoko
     * @date 2020/9/14 17:48
     */
    public static String getBankBalance(String balanceKey, String matching, String smsContent){
        String str = null;
        int startIndex = 0;
        int endIndex = 0;
        String [] balanceKeyArr = balanceKey.split("#");

        boolean flag_balance = false; // 是否有关于余额的关键字:true表示有，false表示无
        for (String balanceKeyStr : balanceKeyArr){
            if (smsContent.indexOf(balanceKeyStr) > -1){
                flag_balance = true;
                break;
            }
        }

        if (flag_balance){
            // 计算余额的值

            // 获取截取方法
            String [] matchingArr = matching.split("#");
            for (String matchingKey : matchingArr){
                String [] matchingKeyArr = matchingKey.split("%");
                String startKey = matchingKeyArr[0];
                String endKey = matchingKeyArr[1];
                startIndex = getIndexOfByStr(smsContent, startKey);
                if (startIndex > 0){
                    startIndex = startIndex + startKey.length();
                }else {
                    continue;
                }

                endIndex = getIndexOfByStrByIndex(smsContent, endKey, startIndex);
                if (endIndex > 0){
                }else {
                    continue;
                }

                if (startIndex <= 0 || endIndex <= 0){
                    continue;
                }

                String money = smsContent.substring(startIndex, endIndex).replaceAll(",","");
                if (StringUtils.isBlank(money)){
                    continue;
                }

                // 判断是否是金额
                boolean flag = StringUtil.isNumberByMoney(money);
                if (flag){
                    str = money;
                }
            }

        }

        return str;
    }


    /**
     * @Description: 组装更新银行卡余额的方法
     * @param id - 主键ID
     * @param balance - 银行卡余额
     * @return BankModel
     * @author yoko
     * @date 2020/9/14 17:19
     */
    public static BankModel assembleBankUpdate(long id, String balance){
        BankModel resBean = new BankModel();
        if (id > 0){
            resBean.setId(id);
        }else {
            return null;
        }
        if (!StringUtils.isBlank(balance)){
            resBean.setBalance(balance);
        }else {
            return null;
        }
        return resBean;
    }




    /**
     * @Description: 组装卡商提现的订单状态更改
     * @param id - 主键ID
     * @param orderStatus - 订单状态：1初始化，2超时，3质疑，4成功
     * @return com.cake.task.master.core.model.channel.ChannelWithdrawModel
     * @author yoko
     * @date 2020/11/19 20:05
     */
    public static MerchantWithdrawModel assembleMerchantWithdrawOrderStatusUpdate(long id, int orderStatus){
        MerchantWithdrawModel resBean = new MerchantWithdrawModel();
        resBean.setId(id);
        resBean.setOrderStatus(orderStatus);
        return resBean;
    }


    /**
     * @Description: 组装主卡与设备卡关联关系的查询方法
     * @param id - 主键ID
     * @param bankLeadId - 主卡ID
     * @param bankId - 银行卡ID
     * @param useStatus -  使用状态
     * @return com.cake.task.master.core.model.interest.InterestMerchantModel
     * @author yoko
     * @date 2020/11/10 13:54
     */
    public static BankLeadLinkModel assembleBankLeadLinkQuery(long id, long bankLeadId, long bankId, int useStatus){
        BankLeadLinkModel resBean = new BankLeadLinkModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (bankLeadId > 0){
            resBean.setBankLeadId(bankLeadId);
        }
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装主卡与设备卡关联关系的查询方法
     * @param id - 主键ID
     * @param id - 主卡ID
     * @return com.cake.task.master.core.model.interest.InterestMerchantModel
     * @author yoko
     * @date 2020/11/10 13:54
     */
    public static BankLeadModel assembleBankLeadByIdQuery(long id){
        BankLeadModel resBean = new BankLeadModel();
        if (id > 0){
            resBean.setId(id);
        }else{
            return null;
        }
        return resBean;
    }

    /**
     * @Description: 组装添加主卡收款纪录
     * @param leadBankId - 主卡的主键ID
     * @param bankId - 银行卡主键ID
     * @param orderNo - 订单号
     * @param money - 订单金额
     * @return BankCollectionModel
     * @author yoko
     * @date 2020/9/15 17:25
     */
    public static BankLeadCollectionModel assembleBankLeadCollectionAdd(long leadBankId, long bankId, String orderNo, String money){
        BankLeadCollectionModel resBean = new BankLeadCollectionModel();
        resBean.setLeadBankId(leadBankId);
        resBean.setBankId(bankId);
        resBean.setOrderNo(orderNo);
        resBean.setMoney(money);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 获取目前自动上线银行卡的数量
     * <p>
     *     1.根据时间判断当前时间是否有匹配的上线的数量，如果有当前匹配的时间的数量就直接给出。
     *     2.如果没有匹配的上线数量，则使用默认的数量
     * </p>
     * @param strategyBankUpNumhModel
     * @return
     * @author yoko
     * @date 2020/9/23 14:15
     */
    public static int getBankUpNum(StrategyModel strategyBankUpNumhModel){
        int bankNum = 0;
        // 拆解时间段
        String [] strArr = null;
        if (!StringUtils.isBlank(strategyBankUpNumhModel.getStgBigValue())){
            strArr = strategyBankUpNumhModel.getStgBigValue().split("#");
        }

        // 拆解上线数量
        String[] bankNumArr = null;
        if (!StringUtils.isBlank(strategyBankUpNumhModel.getStgValue())){
            bankNumArr = strategyBankUpNumhModel.getStgValue().split("#");
        }
        if (strArr != null && bankNumArr != null){
            if (strArr.length == bankNumArr.length){
                for (int i = 0; i <= strArr.length; i++){
                    String[] str_ = strArr[i].split("-");
                    boolean flag_ = DateUtil.isBelong(str_[0], str_[1]);
                    if (flag_){
                        bankNum = Integer.parseInt(bankNumArr[i]);
                        break;
                    }
                }
            }
        }
        if (bankNum <= 0){
            // 表示在这个时间段没有配置上线的数量，则使用默认的上线数量
            bankNum = strategyBankUpNumhModel.getStgNumValue();
        }
        return bankNum;
    }


    /**
     * @Description: 更新银行卡信息
     * @param id - 主键ID
     * @param checkStatus - 检测状态：1初始化正常，2不正常
     * @param isArrears - 归属手机卡是否欠费：1未欠费，2欠费
     * @param dataExplain - 数据说明：检测被限制的原因:task跑日月总限制，如果被限制，连续给出订单失败会填充被限制的原因
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @param changeStatus - 换卡状态：1初始化，2待换卡（成功10多单后、或者后面订单连续几单未成功），3驳回（上线连续几单都未成功），4换卡完毕
     * @param changeTime - 换卡时间：比如卡在2021-03-29 18:29:47下线，但是挂了有订单不允许立即让人换卡，在此时间延迟几分钟在换卡
     * @param checkChange - 检测换卡：检测卡的原因说明，比如连续失败，等。。
     * @return BankModel
     * @author yoko
     * @date 2020/9/15 16:57
     */
    public static BankModel assembleBankAllUpdate(long id, int checkStatus,int isArrears, String dataExplain,
                                                  int useStatus, int changeStatus, String changeTime, String checkChange){
        BankModel resBean = new BankModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (checkStatus > 0){
            resBean.setCheckStatus(checkStatus);
        }
        if (isArrears > 0){
            resBean.setIsArrears(isArrears);
        }
        if (!StringUtils.isBlank(dataExplain)){
            resBean.setDataExplain(dataExplain);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        if (changeStatus > 0){
            resBean.setChangeStatus(changeStatus);
        }
        if (!StringUtils.isBlank(changeTime)){
            resBean.setChangeTime(changeTime);
        }
        if (!StringUtils.isBlank(checkChange)){
            resBean.setCheckChange(checkChange);
        }
        return resBean;
    }



    /**
     * @Description: 组装查询订单信息的查询条件-银行卡自动下线
     * @param bankCard - 银行卡卡号
     * @param orderType - 订单类型：1微信转卡，2支付宝转卡，3卡转卡
     * @param orderStatus - 订单状态：1初始化，2超时/失败，3有质疑，4成功
     * @param downTime - 自动下线检测时间：银行卡自动下线使用此时间去抓取订单进行自动下线判断
     * @param limitNum - 查询数据的条数
     * @param notInChannelIdList - 不包含的渠道集合
     * @return OrderModel
     * @author yoko
     * @date 2020/9/14 20:54
     */
    public static OrderModel assembleOrderByDownBankQuery(String bankCard, int orderType, int orderStatus, String downTime, int limitNum, List<Long> notInChannelIdList){
        OrderModel resBean = new OrderModel();
        if (!StringUtils.isBlank(bankCard)){
            resBean.setBankCard(bankCard);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (!StringUtils.isBlank(downTime)){
            resBean.setDownTime(downTime);
        }
        if (limitNum > 0){
            resBean.setLimitNum(limitNum);
        }
        if (notInChannelIdList != null && notInChannelIdList.size() > 0){
            resBean.setNotInChannelIdList(notInChannelIdList);
        }
        return resBean;
    }


    /**
     * @Description: check校验订单信息是否达到连续失败次数
     * <p>
     *     如果订单连续达到失败次数，则返回true表示此卡需要下线了。
     *     如果返回false表示银行卡无需下线
     * </p>
     * @param orderList - 订单信息
     * @param bankDownByFailNum - 连续失败次数
     * @return boolean
     * @author yoko
     * @date 2021/3/31 20:41
     */
    public static boolean checkFailOrder(List<OrderModel> orderList, int bankDownByFailNum){
        boolean flag = false;
        int checkNum = 0;
        if (orderList != null && orderList.size() > 0){
            if (bankDownByFailNum > 0){
                if (orderList.size() == bankDownByFailNum){
                    for (OrderModel dataModel : orderList){
                        if (dataModel.getOrderStatus() == 4){
                            break;
                        }else{
                            checkNum ++;
                        }
                    }
                }
            }else{
                for (OrderModel dataModel : orderList){
                    if (dataModel.getOrderStatus() == 4){
                        break;
                    }else{
                        checkNum ++;
                    }
                }
            }
        }
        if (bankDownByFailNum > 0){
            if (checkNum != 0 && checkNum >= bankDownByFailNum) {
                flag = true;
            }
        }else{
            if (checkNum != 0 && checkNum >= orderList.size()) {
                flag = true;
            }
        }

        return flag;
    }


    /**
     * @Description: 组装查询卡商与渠道关联关系的方法
     * @param id - 主键ID
     * @param merchantId - 卡商ID
     * @param channelId - 渠道ID
     * @param linkType - 关联类型：1代收，2代付
     * @param useStatus - 使用状态：1正常，2暂停
     * @return com.cake.task.master.core.model.merchant.MerchantChannelModel
     * @author yoko
     * @date 2021/4/1 14:44
     */
    public static MerchantChannelModel assembleMerchantChannelQuery(long id, long merchantId, long channelId, int linkType, int useStatus){
        MerchantChannelModel resBean = new MerchantChannelModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (channelId > 0){
            resBean.setChannelId(channelId);
        }
        if (linkType > 0){
            resBean.setLinkType(linkType);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 拆解策略里面的渠道ID值
     * @param strategyData - 策略值：1:渠道1#2:渠道2
     * @return java.util.List<java.lang.Long>
     * @author yoko
     * @date 2021/4/8 11:04
     */
    public static List<Long> getChannelByBankDownByNotChannel(String strategyData){
        List<Long> resList = new ArrayList<Long>();
        if (!StringUtils.isBlank(strategyData)){
            String [] channelStrArr = strategyData.split("#");
            for (String channelStr : channelStrArr){
                String [] channelArr = channelStr.split(":");
                if (channelArr != null && channelArr.length != 0){
                    resList.add(Long.parseLong(channelArr[0]));
                }
            }

        }else {
            return null;
        }
        return resList;
    }

    /**
     * @Description: 拆解出黑名单名字
     * @param blacklistName - 黑名单名字：张三#李四#王五#轩六
     * @return
     * @author yoko
     * @date 2021/4/8 16:16
    */
    public  static List<String> getBlacklistNameList(String blacklistName){
        List<String> resList = new ArrayList<>();
        if (!StringUtils.isBlank(blacklistName)){
            String [] strArr = blacklistName.split("#");
            if (strArr != null && strArr.length > 0){
                for (String str : strArr){
                    resList.add(str);
                }
            }else {
                return null;
            }
        }else {
            return null;
        }
        return resList;
    }


    /**
     * @Description: check短信内容是否有包含黑名单的名字
     * <p>
     *     如果短信内容包含黑名单的名字则返回true
     * </p>
     * @param smsContent - 收款短信内容
     * @param blacklistNameList - 黑名单名字集合
     * @return boolean
     * @author yoko
     * @date 2021/4/8 16:32
     */
    public static boolean checkBlacklistName(String smsContent, List<String> blacklistNameList){
        if (!StringUtils.isBlank(smsContent)){
            if (blacklistNameList != null && blacklistNameList.size() > 0){
                for (String str : blacklistNameList){
                    if (smsContent.indexOf(str) > -1){
                        return true;
                    }
                }
            }else {
                return false;
            }
        }else {
            return false;
        }
        return false;
    }




    /**
     * @Description: 解析短信获取银行卡-原始卡
     * <p>
     *     解析短信，根据短信的尾号匹配银行卡原始卡的尾号；
     *     如果可以匹配到银行原始卡的尾号，则返回
     * </p>
     * @param bankList - 银行卡集合
     * @param smsContent - 短信内容
     * @param lastNumKey - 尾号开始位的关键字
     * @return long
     * @author yoko
     * @date 2020/9/14 19:13
     */
    public static BankModel getBankIdBySmsContentAndleadBankCard(List<BankModel> bankList, String smsContent, String lastNumKey){
        String [] lastNumKeyArr = lastNumKey.split("#");
        for (BankModel bankModel : bankList){
            for (String str : lastNumKeyArr){
                int start = 0;
                int end = 0;
                if (smsContent.indexOf(str) > -1){
                    if (!StringUtils.isBlank(bankModel.getLeadBankCard())){
                        String leadLastNum = bankModel.getLeadBankCard().substring(bankModel.getLeadBankCard().length()- 4);// 获取原始卡的尾号
                        start = smsContent.indexOf(str) + str.length();
                        end = start + leadLastNum.length();
                        // 从短信内容中截取银行卡尾号
                        String sms_lastNum = smsContent.substring(start, end);
                        if (!StringUtils.isBlank(sms_lastNum)){
                            if (sms_lastNum.equals(leadLastNum)){
                                return bankModel;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * @Description: 银行卡收款信息_日期分割的数据
     * @param leadBankId - 原始银行卡ID
     * @param bankId - 银行卡ID
     * @param orderNo - 订单号
     * @param money - 成功金额
     * @return com.cake.task.master.core.model.bank.BankCollectionDayModel
     * @author yoko
     * @date 2021/4/21 16:28
     */
    public static BankCollectionDayModel assembleBankCollectionDayAdd(long leadBankId, long bankId, String orderNo, String money){
        BankCollectionDayModel resBean = new BankCollectionDayModel();
        int curday = DateUtil.getDayNumber(new Date());
        if (leadBankId > 0){
            resBean.setLeadBankId(leadBankId);
        }
        resBean.setBankId(bankId);
        resBean.setOrderNo(orderNo);
        resBean.setMoney(money);
        resBean.setCurday(curday);
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        resBean.setSuffix(String.valueOf(curday));
        return resBean;
    }



    /**
     * @Description: 银行卡集合排序
     * <p>
     *     排序方式：小于正在使用的最大银行卡ID放在集合的后面，大于正在使用的最大银行卡ID放集合的前面
     * </p>
     * @param bankList - 银行卡集合
     * @param maxBankId - 正在使用的最大银行卡ID
     * @return java.util.List<BankModel>
     * @author yoko
     * @date 2020/10/10 11:49
     */
    public static List<BankModel> sortBankList(List<BankModel> bankList, long maxBankId){
        if (maxBankId > 0){
            List<BankModel> resList = new ArrayList<>();
            List<BankModel> noList = new ArrayList<>();// 没有给出过出码的银行集合
            List<BankModel> yesList = new ArrayList<>();// 有给出过出码的银行集合
            for (BankModel bankModel : bankList){
                if (bankModel.getId() > maxBankId){
                    noList.add(bankModel);
                }else {
                    yesList.add(bankModel);
                }
            }
            if (noList != null && noList.size() > 0){
                resList.addAll(noList);
            }
            if (yesList != null && yesList.size() > 0){
                resList.addAll(yesList);
            }
            return resList;
        }else {
            return bankList;
        }
    }



    /**
     * @Description: check校验银行卡限量
     * @param bankModel - 银行卡信息
     * @param payType - 支付类型：1微信转卡，2支付宝转卡，3卡转卡
     * @return
     * @author yoko
     * @date 2020/9/12 21:19
     */
    public static boolean checkBankLimit(BankModel bankModel, int payType){
        boolean flag = true;
        if (payType == 1){
            String dayMoney = getRedisDataByKey(CacheKey.WX_IN_DAY_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(dayMoney)){
                return false;
            }
            String monthMoney = getRedisDataByKey(CacheKey.WX_IN_MONTH_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(monthMoney)){
                return false;
            }
            String dayNum = getRedisDataByKey(CacheKey.WX_IN_DAY_NUM, bankModel.getId());
            if (!StringUtils.isBlank(dayNum)){
                return false;
            }
        }else if (payType == 2){
            String dayMoney = getRedisDataByKey(CacheKey.ZFB_IN_DAY_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(dayMoney)){
                return false;
            }
            String monthMoney = getRedisDataByKey(CacheKey.ZFB_IN_MONTH_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(monthMoney)){
                return false;
            }
            String dayNum = getRedisDataByKey(CacheKey.ZFB_IN_DAY_NUM, bankModel.getId());
            if (!StringUtils.isBlank(dayNum)){
                return false;
            }
        }else if (payType == 3){
            String dayMoney = getRedisDataByKey(CacheKey.CARD_IN_DAY_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(dayMoney)){
                return false;
            }
            String monthMoney = getRedisDataByKey(CacheKey.CARD_IN_MONTH_MONEY, bankModel.getId());
            if (!StringUtils.isBlank(monthMoney)){
                return false;
            }
            String dayNum = getRedisDataByKey(CacheKey.CARD_IN_DAY_NUM, bankModel.getId());
            if (!StringUtils.isBlank(dayNum)){
                return false;
            }
        }
        return flag;
    }

    /**
     * @Description: 组装缓存key查询缓存中存在的数据
     * @param cacheKey - 缓存的类型key
     * @param obj - 数据的ID
     * @return
     * @author yoko
     * @date 2020/5/20 14:59
     */
    public static String getRedisDataByKey(String cacheKey, Object obj){
        String str = null;
        String strKeyCache = CachedKeyUtils.getCacheKey(cacheKey, obj);
        String strCache = (String) ComponentUtil.redisService.get(strKeyCache);
        if (StringUtils.isBlank(strCache)){
            return str;
        }else{
            str = strCache;
            return str;
        }
    }

    /**
     * @Description: 付款人姓名截取规则：#表示分割截取规则，根据#进行组装数据
     * @param strategyData
     * @return
     * @author yoko
     * @date 2021/5/8 14:08
    */
    public static List<String> getTransferUserRuleListByStrategy(String strategyData){
        List<String> resList = new ArrayList<>();
        if (!StringUtils.isBlank(strategyData)){
            String[] strArr = strategyData.split("#");
            for(String str : strArr){
                resList.add(str);
            }
        }else {
            return null;
        }
        return resList;
    }

    /**
     * @Description: 从短信内容中获取付款人名字
     * @param transferUserRuleList - 截取付款人的开始以及截至规则
     * @param smsContent - 短信内容
     * @return java.lang.String
     * @author yoko
     * @date 2021/5/8 14:52
     */
    public static String getTransferUserBySms(List<String> transferUserRuleList, String smsContent){
        String str = null;
        int startIndex = 0;
        int endIndex = 0;
        if (transferUserRuleList != null && transferUserRuleList.size() > 0){
            for (String strRule : transferUserRuleList){
                String [] strRuleArr = strRule.split("@");
                startIndex = getIndexOfByStr(smsContent, strRuleArr[0]);
                if (startIndex > 0){
                    startIndex = startIndex + strRuleArr[0].length();
                }else {
                    continue;
                }

//            endIndex = getIndexOfByStr(smsContent, bankShortMsgStrategyModel.getEndMoney());
                endIndex = getIndexOfByStrByIndex(smsContent, strRuleArr[1], startIndex);
                if (endIndex > 0){
                }else {
                    continue;
                }

                if (startIndex <= 0 || endIndex <= 0){
                    continue;
                }

                String transferUser = smsContent.substring(startIndex, endIndex);
                if (!StringUtils.isBlank(transferUser)){
                    str = transferUser;
                }else {
                    continue;
                }
            }
        }else{
            return null;
        }

        return str;
    }


    /**
     * @Description: 组装查询代付的查询条件
     * @param id - 代付主键ID
     * @param merchantId - 卡商ID
     * @param isOk - 是否测试通过：1未通过，2通过；收到银行卡短信，并且解析短信模板配置正确
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.cake.task.master.core.model.replacepay.ReplacePayModel
     * @author yoko
     * @date 2021/6/22 16:45
     */
    public static ReplacePayModel assembleReplacePayQuery(long id, long merchantId, int isOk, int useStatus){
        ReplacePayModel resBean = new ReplacePayModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (merchantId > 0){
            resBean.setMerchantId(merchantId);
        }
        if (isOk > 0){
            resBean.setIsOk(isOk);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装更新代付余额的方法
     * @param id - 主键ID
     * @param balance - 余额
     * @param useBalance - 可用余额
     * @return com.cake.task.master.core.model.replacepay.ReplacePayModel
     * @author yoko
     * @date 2021/6/22 16:54
     */
    public static ReplacePayModel assembleReplacePayUpdateBalance(long id, String balance, String useBalance){
        ReplacePayModel resBean = new ReplacePayModel();
        resBean.setId(id);
        if (!StringUtils.isBlank(balance)){
            resBean.setBalance(balance);
        }
        if (!StringUtils.isBlank(useBalance)){
            resBean.setUseBalance(useBalance);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询第三方代付主动拉取结果的查询数据
     * @param limitNum - 查询条数
     * @param runType - 运算类型
     * @param sendType - 发送类型
     * @param id - 主键ID
     * @param replacePayId - 代付ID
     * @param nextTime - 下次查询时间
     * @return com.cake.task.master.core.model.replacepay.ReplacePayGainModel
     * @author yoko
     * @date 2021/6/22 18:13
     */
    public static ReplacePayGainModel assembleReplacePayGainQuery(int limitNum, int runType, int sendType, long id, long replacePayId, String nextTime){
        ReplacePayGainModel resBean = new ReplacePayGainModel();
        if (limitNum > 0){
            resBean.setLimitNum(limitNum);
        }
        if (runType > 0){
            resBean.setRunStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        if (id > 0){
            resBean.setId(id);
        }
        if (replacePayId > 0){
            resBean.setReplacePayId(replacePayId);
        }
        if (!StringUtils.isBlank(nextTime)){
            resBean.setNextTime(nextTime);
        }
        if (sendType > 0){
            resBean.setSendStatus(ServerConstant.PUBLIC_CONSTANT.RUN_STATUS_THREE);
            resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.RUN_NUM_FIVE);
        }
        return resBean;

    }

    /**
     * @Description: 组装查询代付信息的查询条件
     * @param id - 主键ID
     * @return
     * @author yoko
     * @date 2021/6/22 18:36
    */
    public static ReplacePayModel assembleReplacePayByIdQuery(long id){
        ReplacePayModel resBean = new ReplacePayModel();
        resBean.setId(id);
        return resBean;
    }


    /**
     * @Description: 组装更新第三方代付主动拉取结果的方法
     * @param id - 主键ID
     * @param runStatus - 运行状态
     * @param sendStatus - 发送状态
     * @param nextTime - 下次查询时间
     * @param nowGainDataTime - 当前主动获取订单的间隔时间
     * @param dataExplain - 解析说明
     * @return com.cake.task.master.core.model.replacepay.ReplacePayGainModel
     * @author yoko
     * @date 2021/6/22 18:42
     */
    public static ReplacePayGainModel assembleReplacePayGainUpdate(long id, int runStatus, int sendStatus, String nextTime, String nowGainDataTime, String dataExplain){
        ReplacePayGainModel resBean = new ReplacePayGainModel();
        resBean.setId(id);
        if (runStatus > 0){
            resBean.setRunStatus(runStatus);
            if (runStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setRunNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (sendStatus > 0){
            resBean.setSendStatus(sendStatus);
            if (sendStatus == ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_TWO){
                // 表示失败：失败则需要运行次数加一
                resBean.setSendNum(ServerConstant.PUBLIC_CONSTANT.SIZE_VALUE_ONE);
            }
        }
        if (!StringUtils.isBlank(nextTime)){
            resBean.setNextTime(nextTime);
        }
        if (!StringUtils.isBlank(nowGainDataTime)){
            resBean.setNowGainDataTime(nowGainDataTime);
        }
        if (!StringUtils.isBlank(dataExplain)){
            resBean.setDataExplain(dataExplain);
        }
        return resBean;
    }

    /**
     * @Description: 组装第三方代付主动拉取结果的下次查询时间
     * @param replacePayModel - 代付信息
     * @param nowGainDataTime - 第三方代付主动拉取结果的当前时间间隔
     * @return com.cake.task.master.core.model.replacepay.ReplacePayGainModel
     * @author yoko
     * @date 2021/6/22 19:16
     */
    public static ReplacePayGainModel assembleReplacePayGainTime(ReplacePayModel replacePayModel, String nowGainDataTime){
        ReplacePayGainModel resBean = new ReplacePayGainModel();
        if (replacePayModel.getGainDataTimeType() == 1){
            // 主动获取订单结果的间隔时间类型：1任意时间都可查询，2固定时间，3集合某时间间隔（5分钟，8分钟....）
            // 主动获取订单结果的间隔时间类型：1任意时间都可查询
            String next_time = DateUtil.addDateMinute(1);
            resBean.setNextTime(next_time);
            resBean.setNowGainDataTime("1");
        }else if (replacePayModel.getGainDataTimeType() == 2){
            String next_time = DateUtil.addDateMinute(Integer.parseInt(replacePayModel.getGainDataTime()));
            resBean.setNextTime(next_time);
            resBean.setNowGainDataTime("1");
        }else if (replacePayModel.getGainDataTimeType() == 3){
//            List<String> strList = new ArrayList<>();
            String next_time = "";
            String nowGainDataTime_up = "";
            String [] strArr = replacePayModel.getGainDataTime().split("#");
            if (nowGainDataTime.equals("1")){
                next_time = DateUtil.addDateMinute(Integer.parseInt(strArr[0]));
                nowGainDataTime_up = strArr[0];
            }else{
                int strArrSize = strArr.length;
                int num = 0;
                for (String str : strArr){
                    num ++;
                    if (str.equals(nowGainDataTime)){
                        break;
                    }
                }
                if (strArrSize != num){
                    next_time = DateUtil.addDateMinute(Integer.parseInt(strArr[num + 1]));
                    nowGainDataTime_up = strArr[num + 1];
                }else{
                    next_time = DateUtil.addDateMinute(1);
                    nowGainDataTime_up = "-1";
                }

            }
            resBean.setNextTime(next_time);
            resBean.setNowGainDataTime(nowGainDataTime_up);
        }
        return resBean;
    }


    /**
     * @Description: 组装第三方代付主动拉取结果返回的订单结果的方法
     * @param replacePayGainModel - 第三方代付主动拉取结果的信息
     * @param agentPayResponse -  第三方返回的订单信息
     * @return com.cake.task.master.core.model.replacepay.ReplacePayGainResultModel
     * @author yoko
     * @date 2021/6/22 19:53
     */
    public static ReplacePayGainResultModel assembleReplacePayGainResultAdd(ReplacePayGainModel replacePayGainModel, AgentPayResponse agentPayResponse){
        ReplacePayGainResultModel resBean = new ReplacePayGainResultModel();
        resBean.setReplacePayId(replacePayGainModel.getReplacePayId());
        resBean.setOrderNo(replacePayGainModel.getOrderNo());
        resBean.setTradeTime(replacePayGainModel.getTradeTime());
        resBean.setSupplierTradeNo(agentPayResponse.sandSerial);
        resBean.setTranFee(agentPayResponse.tranFee);
        resBean.setTradeStatus(4);
        resBean.setExtraFee(agentPayResponse.extraFee);
        resBean.setHolidayFee(agentPayResponse.holidayFee);

        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }



    /**
     * @Description: 组装更新代付订单状态的信息
     * @param replacePayGainResultModel - 第三方代付主动拉取结果返回的订单结果
     * @param orderStatus - 订单状态：1初始化，2超时，3质疑，4成功
     * @return com.cake.task.master.core.model.order.OrderOutModel
     * @author yoko
     * @date 2021/6/23 15:19
     */
    public static OrderOutModel assembleOrderOutUpdateBySand(ReplacePayGainResultModel replacePayGainResultModel, int orderStatus){
        OrderOutModel resBean = new OrderOutModel();
        resBean.setOrderNo(replacePayGainResultModel.getOrderNo());
        resBean.setOrderStatus(orderStatus);
        if (!StringUtils.isBlank(replacePayGainResultModel.getSupplierTradeNo())){
            resBean.setSupplierTradeNo(replacePayGainResultModel.getSupplierTradeNo());
        }
        if (!StringUtils.isBlank(replacePayGainResultModel.getTranFee())){
            resBean.setSupplierServiceCharge(replacePayGainResultModel.getTranFee());
        }
        return resBean;
    }



    /**
     * @Description: 代付成功信息的数据组装
     * @param bankId - 银行卡ID
     * @param replacePayId - 代付ID
     * @param orderNo - 订单号
     * @param money - 成功金额
     * @return com.cake.task.master.core.model.bank.BankCollectionDayModel
     * @author yoko
     * @date 2021/4/21 16:28
     */
    public static ReplacePayInfoModel assembleReplacePayInfoAdd(long bankId, long replacePayId, String orderNo, String money){
        ReplacePayInfoModel resBean = new ReplacePayInfoModel();
        if (bankId > 0){
            resBean.setBankId(bankId);
        }
        if (replacePayId > 0){
            resBean.setReplacePayId(replacePayId);
        }
        resBean.setOrderNo(orderNo);
        resBean.setMoney(money);
        resBean.setCurday(DateUtil.getDayNumber(new Date()));
        resBean.setCurhour(DateUtil.getHour(new Date()));
        resBean.setCurminute(DateUtil.getCurminute(new Date()));
        return resBean;
    }


    /**
     * @Description: 组装代付放量策略的查询条件
     * @param id - 代付策略的主键ID
     * @param replacePayId - 代付资源ID
     * @param useStatus - 使用状态:1初始化有效正常使用，2无效暂停使用
     * @return com.cake.task.master.core.model.replacepay.ReplacePayStrategyModel
     * @author yoko
     * @date 2021/6/23 16:48
     */
    public static ReplacePayStrategyModel assembleReplacePayStrategyQuery(long id, long replacePayId, int useStatus){
        ReplacePayStrategyModel resBean = new ReplacePayStrategyModel();
        if (id > 0){
            resBean.setId(id);
        }
        if (replacePayId > 0){
            resBean.setReplacePayId(replacePayId);
        }
        if (useStatus > 0){
            resBean.setUseStatus(useStatus);
        }
        return resBean;
    }


    /**
     * @Description: 组装查询代付订单信息来限制代付的查询条件
     * @param replacePayId - 代付ID
     * @param orderType - 订单类型：1手动转账，2API转账
     * @param orderStatus - 订单状态
     * @param handleType - 订单处理类型：1我方处理，2第三方处理
     * @param curday - 创建日期
     * @param curdayStart - 开始日期
     * @param curdayEnd - 结束日期
     * @return OrderModel
     * @author yoko
     * @date 2020/9/15 10:58
     */
    public static OrderOutModel assembleOrderOutByLimitQuery(long replacePayId, int orderType, int orderStatus, int handleType, int curday, int curdayStart, int curdayEnd){
        OrderOutModel resBean = new OrderOutModel();
        if (replacePayId > 0){
            resBean.setReplacePayId(replacePayId);
        }
        if (orderType > 0){
            resBean.setOrderType(orderType);
        }
        if (orderStatus > 0){
            resBean.setOrderStatus(orderStatus);
        }
        if (handleType > 0){
            resBean.setHandleType(handleType);
        }
        if (curday > 0){
            resBean.setCurday(curday);
        }
        if (curdayStart > 0){
            resBean.setCurdayStart(curdayStart);
        }
        if (curdayEnd > 0){
            resBean.setCurdayEnd(curdayEnd);
        }
        return resBean;
    }





    public static void main(String []args){
        List<BankShortMsgStrategyModel> bankShortMsgStrategyList = new ArrayList<>();
        BankShortMsgStrategyModel bankShortMsgStrategyModel1 = new BankShortMsgStrategyModel();
        bankShortMsgStrategyModel1.setStartMoney("支付宝)");
        bankShortMsgStrategyModel1.setEndMoney("元");

        BankShortMsgStrategyModel bankShortMsgStrategyModel2 = new BankShortMsgStrategyModel();
        bankShortMsgStrategyModel2.setStartMoney("收入(手机转账)");
        bankShortMsgStrategyModel2.setEndMoney("元");

        BankShortMsgStrategyModel bankShortMsgStrategyModel3 = new BankShortMsgStrategyModel();
        bankShortMsgStrategyModel3.setStartMoney("收入(转账)");
        bankShortMsgStrategyModel3.setEndMoney("元");

        BankShortMsgStrategyModel bankShortMsgStrategyModel4 = new BankShortMsgStrategyModel();
        bankShortMsgStrategyModel4.setStartMoney("收入(他行汇入)");
        bankShortMsgStrategyModel4.setEndMoney("元");

        BankShortMsgStrategyModel bankShortMsgStrategyModel5 = new BankShortMsgStrategyModel();
        bankShortMsgStrategyModel5.setStartMoney("收入(冲正)");
        bankShortMsgStrategyModel5.setEndMoney("元");

        bankShortMsgStrategyList.add(bankShortMsgStrategyModel1);
        bankShortMsgStrategyList.add(bankShortMsgStrategyModel2);
        bankShortMsgStrategyList.add(bankShortMsgStrategyModel3);
        bankShortMsgStrategyList.add(bankShortMsgStrategyModel4);
        bankShortMsgStrategyList.add(bankShortMsgStrategyModel5);
        String smsContent = "您尾号2666卡10月3日01:00元快捷支付收入(肖爱林支付宝转账支付宝)300元，余额13,814元。【工商银行】";
        String money = getBankMoney(bankShortMsgStrategyList, smsContent);
        System.out.println("money:" + money);


        String balanceKey = "余额";
        String matching = "余额%【中国银行】#余额%元#余额%。#余额为%元";
        String smsContent1 = "【中国农业银行】支付宝（中国）网络技术有限公司于10月04日02:30向您尾号2476账户完成代付交易人民币300.00，余额300.18。";
        String str = getBankBalance(balanceKey, matching, smsContent1);
        System.out.println("str:" + str);
        if (!StringUtils.isBlank(str)){
            System.out.println("哈哈");
        }

        boolean flag = StringUtil.getBigDecimalSubtract("101","100.01");
        System.out.println("flag:" + flag);
        String strategyData = "1:渠道1#2:渠道2#4:渠道4";
        List<Long> idList = TaskMethod.getChannelByBankDownByNotChannel(strategyData);
        for (int i=0; i< idList.size(); i++){
            System.out.println("idList-data:" + idList.get(i));
        }

        String strategyData1 = "张三#李四#王五#轩六";
//        String strategyData1 = null;
        List<String> strList = TaskMethod.getBlacklistNameList(strategyData1);
        for (int i=0; i< strList.size(); i++){
            System.out.println("strList-data:" + strList.get(i));
        }
        String smsContent2 = "您的借记卡账户张三_2，于10月01日网上支付收入人民币20.01元,交易后余额为20.00元.";
        List<String> blacklistNameList = new ArrayList<>();
        blacklistNameList.add("张三");
        blacklistNameList.add("李四");
        blacklistNameList.add("王五");
        boolean flag1 = TaskMethod.checkBlacklistName(smsContent2, blacklistNameList);
        System.out.println("flag1:" + flag1);

        String lastStr = "1234";
        System.out.println("lastStr:" + lastStr.substring(lastStr.length()- 4));

        String str2 = "1对方户名：@，#aasdas@121";
        List<String> strList1 = getTransferUserRuleListByStrategy(str2);
        System.out.println("strList1:" + strList1.size());
        if (strList1 != null && strList1.size() > 0){
            for (String str3 : strList1){
                System.out.println("str3:" + str3);
            }
        }

        String smsData = "您尾号1335卡4月30日18:37手机银行支出(跨行汇款)1,700元，余额279.33元，对方户名：李学辉，对方账户尾号：1376";
        String transferUser = getTransferUserBySms(strList1, smsData);
        System.out.println("transferUser:" + transferUser);

    }

}
