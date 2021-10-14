/**
 * Copyright : http://www.sandpay.com.cn , 2011-2014
 * Project : sandpay-dsf-demo
 * $Id$
 * $Revision$
 * Last Changed by pxl at 2018-4-25 下午7:24:10
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
import com.cake.task.master.core.model.replacepay.ReplacePayModel;
import com.cake.task.master.util.HodgepodgeMethod;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 产品：杉德代收付产品<br>
 * 交易：商户余额查询<br>
 * 日期： 2021-01<br>
 * 版本： 1.0.0 
 * 说明：以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己需要，按照技术文档编写。该代码仅供参考。<br>
 */
public class MerBalanceQuery {

	public static Logger logger = LoggerFactory.getLogger(MerBalanceQuery.class);

	JSONObject request = new JSONObject();
	
	/** 
	*    组织请求报文      
	*/
	void setRequest() {
		request.put("version", DemoBase.version);
		request.put("productId", DemoBase.PRODUCTID_AGENTPAY_TOC);
		request.put("tranTime", DemoBase.getCurrentTime());
		request.put("orderCode", DemoBase.getOrderCode());
		request.put("channelType", "");
		request.put("extend", "");
	}


	/**
	 * @Description: 衫德账户余额查询
	 * @param replacePayModel - 代付信息
	 * @return
	 * @author yoko
	 * @date 2021/6/22 14:28
	*/
	public static AgentPayResponse sandBalanceQuery(ReplacePayModel replacePayModel) throws Exception{
		MerBalanceQuery demo = new MerBalanceQuery();
		String reqAddr = replacePayModel.getBalanceInterfaceAds();   //接口报文规范中获取

		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init(replacePayModel.getPublicKeyPath(), replacePayModel.getPrivateKeyPath(), replacePayModel.getSandKey());
		//设置报文
		demo.setRequest();

		String merId = replacePayModel.getBusinessNum(); 			//商户ID
		String plMid = replacePayModel.getPlatformBusinessNum();		//平台商户ID

		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.MER_BALANCE_QUERY, merId, plMid);

		if(resp != null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("余额：["+resp.getString("balance")+"]");
			if (resp.getString("respCode").equals("0000")){
//				AgentPayResponse result = BeanUtils.copy(resp, AgentPayResponse.class);
				AgentPayResponse result = JSON.parseObject(resp.toJSONString(), AgentPayResponse.class);
				if (!StringUtils.isBlank(result.getBalance())){
					result.setBalance(HodgepodgeMethod.sandPayBalance(result.getBalance()));
				}
				if (!StringUtils.isBlank(result.getCreditAmt())){
					result.setCreditAmt(HodgepodgeMethod.sandPayBalance(result.getCreditAmt()));
				}
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
		
		MerBalanceQuery demo = new MerBalanceQuery();
		String reqAddr="/queryBalance";   //接口报文规范中获取
		
		//加载配置文件
		SDKConfig.getConfig().loadPropertiesFromSrc();
		//加载证书
		CertUtil.init(SDKConfig.getConfig().getSandCertPath(), SDKConfig.getConfig().getSignCertPath(), SDKConfig.getConfig().getSignCertPwd());
		//设置报文
		demo.setRequest();
		
		String merId = SDKConfig.getConfig().getMid(); 			//商户ID
		String plMid = SDKConfig.getConfig().getPlMid();		//平台商户ID
		
		JSONObject resp = DemoBase.requestServer(demo.request, reqAddr, DemoBase.MER_BALANCE_QUERY, merId, plMid);
		
		if(resp!=null) {
			logger.info("响应码：["+resp.getString("respCode")+"]");	
			logger.info("响应描述：["+resp.getString("respDesc")+"]");
			logger.info("余额：["+resp.getString("balance")+"]");
			
			System.out.println("响应码：["+resp.getString("respCode")+"]");
			System.out.println("响应描述：["+resp.getString("respDesc")+"]");
			System.out.println("余额：["+resp.getString("balance")+"]");
		}else {
			logger.error("服务器请求异常！！！");	
		}
	}


}
