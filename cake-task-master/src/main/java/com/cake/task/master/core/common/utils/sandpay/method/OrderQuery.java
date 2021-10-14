/**
 * Copyright : http://www.sandpay.com.cn , 2011-2014
 * Project : sandpay-dsf-demo
 * $Id$
 * $Revision$
 * Last Changed by pxl at 2018-4-25 下午5:52:46
 * $URL$
 * 
 * Change Log
 * Author      Change Date    Comments
 *-------------------------------------------------------------
 * pxl         2018-4-25        Initailized
 */
package com.cake.task.master.core.common.utils.sandpay.method;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cake.task.master.core.common.utils.BeanUtils;
import com.cake.task.master.core.common.utils.sandpay.CertUtil;
import com.cake.task.master.core.common.utils.sandpay.SDKConfig;
import com.cake.task.master.core.common.utils.sandpay.model.AgentPayResponse;
import com.cake.task.master.core.model.replacepay.ReplacePayGainModel;
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 产品：杉德代收付产品<br>
 * 交易：订单查询<br>
 * 日期： 2021-01<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class OrderQuery {
	
	public static Logger logger = LoggerFactory.getLogger(OrderQuery.class);
	
	
	JSONObject request = new JSONObject();
	/** 
	*  组织请求报文          
	*/
	private void setRequest() {
		
		request.put("version", DemoBase.version);                     // 版本号  
		request.put("productId", DemoBase.PRODUCTID_AGENTPAY_TOC);    // 产品ID  
		request.put("tranTime", "20210119092720");           	      // 查询订单的交易时间
		request.put("orderCode", "202101190927208");                  // 要查询的订单号  
		request.put("extend", "");                                    // 扩展域
		
	}


	/**
	 *  组织请求报文
	 */
	private void setRequest(ReplacePayGainModel replacePayGainModel) {

		request.put("version", DemoBase.version);                     // 版本号
		request.put("productId", DemoBase.PRODUCTID_AGENTPAY_TOC);    // 产品ID
		request.put("tranTime", replacePayGainModel.getTradeTime());           	      // 查询订单的交易时间
		request.put("orderCode", replacePayGainModel.getOrderNo());                  // 要查询的订单号
		request.put("extend", "");                                    // 扩展域

	}


	/**
	 * @Description: 衫德代付-查询订单状态
	 * @param replacePayModel - 代付资源信息
	 * @param replacePayGainModel - 第三方代付主动拉取结果的信息
	 * @return com.cake.task.master.core.common.utils.sandpay.model.AgentPayResponse
	 * @author yoko
	 * @date 2021/6/22 19:31
	 */
	public static AgentPayResponse sandOrderQuery(ReplacePayModel replacePayModel, ReplacePayGainModel replacePayGainModel) throws Exception{
		OrderQuery demo = new OrderQuery();
		String reqAddr = replacePayModel.getInInterfaceAds();   //接口报文规范中获取

		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init(replacePayModel.getPublicKeyPath(), replacePayModel.getPrivateKeyPath(), replacePayModel.getSandKey());
		//设置报文
		demo.setRequest(replacePayGainModel);

		String merId = replacePayModel.getBusinessNum(); 			//商户ID
		String plMid = replacePayModel.getPlatformBusinessNum();		//平台商户ID

		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.ORDER_QUERY, merId, plMid);

		if(resp!=null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");
			logger.info("处理状态：["+resp.getString("resultFlag")+"]");
			logger.info("响应描述：["+resp.getString("respDesc")+"]");

			if (resp.getString("respCode").equals("0000")){
//				AgentPayResponse result = BeanUtils.copy(resp, AgentPayResponse.class);
				AgentPayResponse result = JSON.parseObject(resp.toJSONString(), AgentPayResponse.class);
				return result;
			}else {
				return null;
			}
		}else {
			logger.error("服务器请求异常！！！");
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		
		OrderQuery demo = new OrderQuery();
		String reqAddr="/queryOrder";   //接口报文规范中获取
		
		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init(SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		//设置报文
		demo.setRequest();
		
		String merId = SDKConfig.getConfig().getMid(); 			//商户ID
		String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID
		
		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.ORDER_QUERY, merId, plMid);
		
		if(resp!=null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");	
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("处理状态：["+resp.getString("resultFlag")+"]");
			
			System.out.println("响应码：["+resp.getString("respCode")+"]");
			System.out.println("响应描述：["+resp.getString("respDesc")+"]");
			System.out.println("处理状态：["+resp.getString("resultFlag")+"]");
		}else {
			logger.error("服务器请求异常！！！");	
		}
	}

	
}
