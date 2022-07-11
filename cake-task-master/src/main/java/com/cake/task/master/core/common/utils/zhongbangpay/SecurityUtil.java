package com.cake.task.master.core.common.utils.zhongbangpay;

import com.alibaba.fastjson.JSONObject;
import com.cake.task.master.core.common.utils.BeanUtils;
import com.cake.task.master.core.common.utils.zhongbangpay.model.response.TransferResponse;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.concurrent.TimeUnit;

/**
 * @Description TODO
 * @Author long
 * @Date 2022/6/30 11:40
 * @Version 1.0
 */
public class SecurityUtil {
    private static Logger log = LoggerFactory.getLogger(SecurityUtil.class);
    /**
     * 加密算法RSA
     **/
    private static final String APP_ID = "20220623e00000016422";
    /**
     * 加密算法RSA
     **/
    private static final String RSA_ALGORITHM = "RSA";
    /**
     * 签名算法
     **/
    private static final String SIGNATURE_ALGORITHM = "SHA256WithRSA";
    /**
     * 字符串编码
     **/
    private static final String CHARSET = "UTF-8";
    /**
     * API平台公钥
     **/
    private static final String apiPublicKey = "1-gBzmBvxcW7wINRLMKT_wxQ4mweYbk3jsEBE5Lx2FNRje5Haw3BYOdZ6OxQ1HtRwYF-3-3-1EOMHmqGmL7zCSD-3-3-Ny5tu7wIDAQAB";
    /**
     * 商户私钥
     **/
    private static final String privateKey = "121/ivWpFKzT3VV5X2YyE4DxiiPM4MdjTj+12+14SOpj//z8dGHhNt5/1212/FPaH4H5NW/Fag5gKNW/121/d+29CyYj2+121/SK/1212+Gn9uFvgcnv/121+112+hmknF239Tl+121/121/s/ECM0NrfK4rARwswpsCv9UmmiDwFZ+121+Z6OVdtwM8cLsem68TWYXrn750qtS0jcWyHFHzfRcdSedxi0N9bAhoM8l+z+DPRCifj+AHa5u2/PhZ1okqzNu6XQTzqpMHiBmUr1g8zkSwKBgQC79is1NocCKDLwAn8S5a/W4LY5A5IK3HEOTLM4fZjm9xqR8N7eULecot6swTK/y1U6jG33odJRD5CtA440Q/hHKUo+5sy6yf62yBHesyZI80rIYk33m8SHmWo0PdxRHsQlzMEDQd+8yPncb4imsxqQj+KBbLe+g1myfMoAdlLM5QKBgAxjC8XnDOaTer8fKxKmssMRyNMvwtx30aEnCAXkmPRyEM85bRTgdetMgNzgpdZQ+i6BnOgKoRE3jWuPSMaCFsc27kDuJ8U74mbb5dRfLURK9mxX/9U9yIJ4HujNXOCyqxgD3FEKkUCTMQAdIxTyAXr+YXHDYWaqjRjBmXmePPQ1AoGAYKto6YNTyG44VGxUQrnSx+bmkUge0msx0jLscf9WpmMsTSbe2OQWqv6xG5R5r166RNR4skWNz5b7x5/ugT778E1yHAvD+8VrtUIMvsbQx/Ao5Ap7IwAAAeWLU7cFGsuCAhbepfg5Q/TxXMgYNfQjk3oM+peZoOgZN80qbzZru2kCgYEAvd5MUGUufhCgIPhugEwZr+YqoP3aOdU13Ji2rPgMsit/nEeXNyNs6ltsDuVfQOM3bZTRUWIf3xVSlc40ELbLljXki1FXxWo9oGyntjPsMeTk/SyZ/dZQg2UqaNR5z8bqlMraqCpaU0eTIosquUcmqnOZwBsglMK6R8jg5G1oLwI=";
    /**
     * 请求地址
     **/
    private static final String TEST_SERVICE_URL = "https://eais.yijieb.com/service/gateway.html";
    /**
     * 时间格式
     **/
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");


    /**
     * 转换为代签名字符串
     *
     * @param stringStringMap
     * @return
     */
    public static String doStringToSign(Map<String, String> stringStringMap) {
        String waitToSignStr = null;
        Map<String, String> sortedMap = new TreeMap<String, String>(stringStringMap);
        if (sortedMap.containsKey("sign")) {
            sortedMap.remove("sign");
        }
        StringBuilder stringToSign = new StringBuilder();
        if (sortedMap.size() > 0) {
            for (Map.Entry<String, String> entry : sortedMap.entrySet()) {
                if (entry.getValue() != null) {
                    stringToSign.append(entry.getKey()).append("=").append(String.valueOf(entry.getValue())).append("&");
                }
            }
            stringToSign.deleteCharAt(stringToSign.length() - 1);
            waitToSignStr = stringToSign.toString();
        }
        return waitToSignStr;
    }

    /**
     * RSA签名
     *
     * @param content 待签名数据
     * @return 签名值
     */
    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decodeBase64(privateKey));

            KeyFactory keyf = KeyFactory.getInstance(RSA_ALGORITHM);
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

            signature.initSign(priKey);
            signature.update(content.getBytes(CHARSET));

            byte[] signed = signature.sign();

            return Base64.encodeBase64URLSafeString(signed);
        } catch (Exception e) {
            throw new RuntimeException("签名发生异常", e);
        }
    }

    /**
     * RSA 验签
     *
     * @param content 待签名数据
     * @param sign    签名值
     * @return 布尔值
     */
    public static boolean verify(String content, String sign) {
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(RSA_ALGORITHM);
            byte[] encodedKey = Base64.decodeBase64(apiPublicKey);
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

            Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);

            signature.initVerify(pubKey);
            signature.update(content.getBytes(CHARSET));

            boolean bverify = signature.verify(Base64.decodeBase64(sign));
            return bverify;

        } catch (Exception e) {
            throw new RuntimeException("RSA 验签失败", e);
        }
    }

    /**
     * 发送请求
     *
     * @param url
     * @param paramsMap
     */
    public static TransferResponse sendPost(String url, Map<String, String> paramsMap) {
        OkHttpClient client = new OkHttpClient();
        client.newBuilder().connectTimeout(60, TimeUnit.SECONDS).writeTimeout(120, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS).followRedirects(false);
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : paramsMap.keySet()) {
            //填充表单信息
            builder.add(key, paramsMap.get(key));
        }
        Request request = new Request.Builder().url(url).post(builder.build()).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
//                String strRsp = "{\"trx_id\":\"1504652571153793024\",\"freeze_amt\":0.00,\"result_message\":\"成功\",\"balance\":36000.55,\"service\":\"account.balance.query\",\"return_message\":\"成功\",\"sign\":\"grOnXaB2ASLTr7E7NHMWkjF/yD9l3Ikk3CmM46Bxo2HjVGuWmBhStajS1uPjnO04Eg/1qwmadB7Fd6VorY33GWf/4B9GshqdVrMAm97LkBo/94nzJWQLBr5bUs9AnyUP/939MFy6N2WNCfoaEeKzuB/Nl8V74Hijgkfy3CZIfGaJVvOWysWlwAfsIH98+y36hB/1wicbys9LKUd3QjwnVwf4FimFzr/puwP1HsPz12NAU72HgNjwpyXq0iGtnmnKEC1Yo9h5jSGzBlmhvUsIQDUEeGWfVBDtm8S1sMXrYv+fXCu1GGqblcrWtGH/HxfBzQp3hhHrGQ25bXYC2ysJgw==\",\"signType\":\"RSA\",\"result_code\":\"SUCCESS\",\"version\":\"1.0\",\"return_code\":\"SUCCESS\",\"app_id\":\"20220308e00000003539\"}";
                String strRsp = response.body().string();
                TransferResponse transferResponse = JSONObject.parseObject(strRsp, TransferResponse.class);
                if (transferResponse != null){
                    return transferResponse;
                }else {
                    return null;
                }
            } else {
                throw new IOException("Unexpected code " + response.message());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
