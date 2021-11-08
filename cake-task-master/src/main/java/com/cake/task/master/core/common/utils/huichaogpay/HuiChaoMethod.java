package com.cake.task.master.core.common.utils.huichaogpay;

import com.cake.task.master.core.common.utils.huichaogpay.model.request.QueryBalanceRequest;
import com.cake.task.master.core.common.utils.huichaogpay.model.request.TransferList;
import com.cake.task.master.core.common.utils.huichaogpay.model.request.TransferQueryRequest;
import com.cake.task.master.core.common.utils.huichaogpay.model.request.TransferRequest;
import com.thoughtworks.xstream.XStream;
import org.apache.logging.log4j.util.Base64Util;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.StringReader;

/**
 * @author yoko
 * @desc
 * @create 2021-11-05 16:38
 **/
public class HuiChaoMethod {


    /**
    * @Description: 转账信息组装xml数据
    * @param dto - 要转账的信息
     *@param key - 秘钥
    * @return:
    * @author: yoko
    * @date: 2021/11/5 17:43
    * @version 1.0.0
    */
    public static String transferXML(TransferRequest dto, String key) throws Exception {
        String res = null;
        try {
            String strContent = "transId=" + dto.getTransferList().getTransId() + "&accountNumber=" + dto.getAccountNumber()
                    + "&cardNo=" + dto.getTransferList().getCardNo() + "&amount=" + dto.getTransferList().getAmount();
            String secureCode = SecurityUtil.sign( key, strContent);
            TransferList transferList = dto.getTransferList();
            transferList.setSecureCode(secureCode);
//            System.out.println("secureCode:" + secureCode);
            res = makeTransferXML(dto);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 拼接成Xml明文
     */
    private static String makeTransferXML(TransferRequest dto) {
        XStream xStream = new XStream();
        xStream.alias("root", TransferRequest.class);
        String transData = xStream.toXML(dto);
        transData = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + transData;
        //Xml明文进行Base64编码
//        System.out.println("XML格式:" + transData);
        transData = Base64Util.encode(transData);
//        System.out.println("transData:" + transData);
        return transData;
    }


    /**
     * @Description: 汇潮-查单-组装xml字符串数据
     * @param dto - 查单信息
     * @param key - 秘钥
     * @return: java.lang.String
     * @author: yoko
     * @date: 2021/11/5 19:15
     * @version 1.0.0
     */
    public static String transferQueryXML(TransferQueryRequest dto, String key) throws Exception {
        String res = null;
        try {
            //merchantNumber&requestTime
            String strContent = dto.getMerchantNumber() + "&" + dto.getRequestTime();
            String sign = SecurityUtil.sign( key, strContent);
            dto.setSign(sign);
            System.out.println("sign:" + sign);
            res = makeTransferQueryXML(dto);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 拼接成Xml明文-查单
     */
    private static String makeTransferQueryXML(TransferQueryRequest dto) {
        XStream xStream = new XStream();
        xStream.alias("root", TransferQueryRequest.class);
        String requestDomain = xStream.toXML(dto);
        requestDomain = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + requestDomain;
        //Xml明文进行Base64编码
//        System.out.println("XML格式:" + requestDomain);
        requestDomain = Base64Util.encode(requestDomain);
//        System.out.println("requestDomain:" + requestDomain);
        return requestDomain;
    }



    /**
     * @Description: 汇潮-查余额-组装xml字符串数据
     * @param dto - 查余额的信息
     * @param key - 秘钥
     * @return: java.lang.String
     * @author: yoko
     * @date: 2021/11/5 20:57
     * @version 1.0.0
     */
    public static String queryBalanceXML(QueryBalanceRequest dto, String key) throws Exception {
        String res = null;
        try {
            //MerNo+RequestTime
            String strContent = dto.getMerNo() + dto.getRequestTime();
            String signInfo = SecurityUtil.sign( key, strContent);
            dto.setSignInfo(signInfo);
            res = makeQueryBalanceXML(dto);
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 拼接成Xml明文 - 汇潮-查余额
     */
    private static String makeQueryBalanceXML(QueryBalanceRequest dto) {
        XStream xStream = new XStream();
        xStream.alias("root", QueryBalanceRequest.class);
        String requestDomain = xStream.toXML(dto);
        requestDomain = "<?xml version=\"1.0\" encoding=\"utf-8\"?>" + requestDomain;
        //Xml明文进行Base64编码
        requestDomain = Base64Util.encode(requestDomain);
        return requestDomain;
    }



    /**
     * @Description: 将xml转换成实体Bean
     * @param clazz - 要转换成的实体Bean
     * @param xmlStr - xml字符串
     * @return: T
     * @author: yoko
     * @date: 2021/11/5 21:13
     * @version 1.0.0
     */
    public static <T> T convertXmlStrToObjectww(Class<T> clazz, String xmlStr) {
        try {
            XStream xStream = new XStream();
            XStream.setupDefaultSecurity(xStream);
            xStream.allowTypesByRegExp(new String[] { ".*" });
            xStream.processAnnotations(clazz);
            return (T) xStream.fromXML(xmlStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将String类型的xml转换成对象
     */
    public static Object convertXmlStrToObject(Class clazz, String xmlStr) {
        Object xmlObject = null;
        try {
            JAXBContext context = JAXBContext.newInstance(clazz);
            // 进行将Xml转成对象的核心接口
            Unmarshaller unmarshaller = context.createUnmarshaller();
            StringReader sr = new StringReader(xmlStr);
            xmlObject = unmarshaller.unmarshal(sr);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xmlObject;
    }

}
