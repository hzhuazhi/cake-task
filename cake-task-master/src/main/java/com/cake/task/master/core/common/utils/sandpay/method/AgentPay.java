/**
 * Copyright : http://www.sandpay.com.cn , 2011-2014
 * Project : sandpay-dsf-demo
 * $Id$
 * $Revision$
 * Last Changed by pxl at 2018-4-25 上午11:13:56
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * pxl         2018-4-25        Initailized
 */
package com.cake.task.master.core.common.utils.sandpay.method;

import com.alibaba.fastjson.JSONObject;
import com.cake.task.master.core.common.utils.BeanUtils;
import com.cake.task.master.core.common.utils.sandpay.CertUtil;
import com.cake.task.master.core.common.utils.sandpay.SDKConfig;
import com.cake.task.master.core.common.utils.sandpay.model.AgentPayResponse;
import com.cake.task.master.core.model.order.OrderOutModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 产品：杉德代收付产品<br>
 * 交易：代付接口<br>
 * 日期： 2021-01<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class AgentPay {
	
	public static Logger logger = LoggerFactory.getLogger(AgentPay.class);
	
	
	JSONObject request = new JSONObject();
	
	
	/** 
	* 组织请求报文     
	*/
	private void setRequest() {
		
		request.put("version", DemoBase.version);								//版本号      
		request.put("productId", DemoBase.PRODUCTID_AGENTPAY_TOC);              //产品ID     
		request.put("tranTime", DemoBase.getCurrentTime());                     //交易时间     
		request.put("orderCode", DemoBase.getOrderCode());                      //订单号      
		request.put("timeOut", DemoBase.getNextDayTime());                      //订单超时时间   
		request.put("tranAmt", "000000000001");                                 //金额       
		request.put("currencyCode", DemoBase.CURRENCY_CODE);                    //币种       
		request.put("accAttr", "0");                                            //账户属性     0-对私   1-对公
		request.put("accType", "4");                                            //账号类型      3-公司账户  4-银行卡
		request.put("accNo", "6228480038125000000");                            //收款人账户号   
		request.put("accName", "施祺");                                       		//收款人账户名   
		request.put("provNo", "");                                              //收款人开户省份编码
		request.put("cityNo", "");                                              //收款人开会城市编码
		request.put("bankName", "");                                            //收款账户开户行名称
		request.put("bankType", "");                                			//收款人账户联行号 
		request.put("remark", "消费");                                          	//摘要       
		request.put("payMode", ""); 											//付款模式
		request.put("channelType", "");                                         //渠道类型   
		request.put("extendParams", "");										//业务扩展参数
		request.put("reqReserved", "");                                         //请求方保留域  
		request.put("extend", "");                                              //扩展域
		request.put("phone", "");												//手机号
	}



	/**
	 * 组织请求报文
	 */
	private void setRequest(ReplacePayModel replacePayModel, OrderOutModel orderOutModel) {

		request.put("version", DemoBase.version);								//版本号
		request.put("productId", DemoBase.PRODUCTID_AGENTPAY_TOC);              //产品ID
		request.put("tranTime", orderOutModel.getTradeTime());                     //交易时间
		request.put("orderCode", orderOutModel.getOrderNo());                      //订单号
		request.put("timeOut", "30");                      //订单超时时间
		request.put("tranAmt", orderOutModel.getOrderMoney());                                 //金额
		request.put("currencyCode", DemoBase.CURRENCY_CODE);                    //币种
		request.put("accAttr", replacePayModel.getAccountAttribute());                                            //账户属性     0-对私   1-对公
		request.put("accType", replacePayModel.getAccountType());                                            //账号类型      3-公司账户  4-银行卡
		request.put("accNo", orderOutModel.getInBankCard());                            //收款人账户号
		request.put("accName", orderOutModel.getInAccountName());                                       		//收款人账户名
		request.put("provNo", "");                                              //收款人开户省份编码
		request.put("cityNo", "");                                              //收款人开会城市编码
		request.put("bankName", "");                                            //收款账户开户行名称
		request.put("bankType", "");                                			//收款人账户联行号
		request.put("remark", "消费");                                          	//摘要
		request.put("payMode", ""); 											//付款模式
		request.put("channelType", "");                                         //渠道类型
		request.put("extendParams", "");										//业务扩展参数
		request.put("reqReserved", "");                                         //请求方保留域
		request.put("extend", "");                                              //扩展域
		request.put("phone", "");												//手机号
	}


	/**
	 * @Description: 衫德支付-代付请求
	 * @param replacePayModel
	 * @param orderOutModel
	 * @return
	 * @author yoko
	 * @date 2021/6/11 15:28
	*/
	public static AgentPayResponse sandAgentPay(ReplacePayModel replacePayModel, OrderOutModel orderOutModel) throws Exception{
		AgentPay demo = new AgentPay();
		String reqAddr = replacePayModel.getOutInterfaceAds();   //接口报文规范中获取

		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init(replacePayModel.getPublicKeyPath(), replacePayModel.getPrivateKeyPath(), replacePayModel.getSandKey());
		//设置报文
		demo.setRequest(replacePayModel, orderOutModel);

		String merId = replacePayModel.getBusinessNum(); 			//商户ID
		String plMid = replacePayModel.getPlatformBusinessNum();		//平台商户ID

		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.AGENT_PAY, merId, plMid);

		if(resp != null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("处理状态：["+resp.getString("resultFlag")+"]");
			if (resp.getString("respCode").equals("0000")){
				AgentPayResponse result = BeanUtils.copy(resp, AgentPayResponse.class);
				return result;
			}else {
				return null;
			}
		}else {
			logger.error("服务器请求异常！！！");
			System.out.println("服务器请求异常！！！");
			return null;
		}
	}




	public static void main(String[] args) throws Exception {
		AgentPay demo = new AgentPay();
		String reqAddr="/agentpay";   //接口报文规范中获取
		
		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init(SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		//设置报文
		demo.setRequest();
		
		String merId = SDKConfig.getConfig().getMid(); 			//商户ID
		String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID

		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.AGENT_PAY, merId, plMid);

		if(resp!=null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");	
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("处理状态：["+resp.getString("resultFlag")+"]");
			
			System.out.println("响应码：["+resp.getString("respCode")+"]");
			System.out.println("响应描述：["+resp.getString("respDesc")+"]");
			System.out.println("处理状态：["+resp.getString("resultFlag")+"]");
		}else {
			logger.error("服务器请求异常！！！");	
			System.out.println("服务器请求异常！！！");
		}	

	}
	
}
