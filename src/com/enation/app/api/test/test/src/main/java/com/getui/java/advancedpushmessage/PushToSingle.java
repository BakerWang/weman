package com.enation.app.api.test.test.src.main.java.com.getui.java.advancedpushmessage;

import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.http.IGtPush;

public class PushToSingle {
   //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
	 private static String appId = "SDf4Q9pSd38mLktu2XuR36";
	    private static String appKey = "PsufBWfwv599y4ckXz1Hr8";
	    private static String masterSecret = "RJInoKLyDJ8QYQxnNpbb29";

	    static String CID = "eddbf2cd79f5f22da6ef554bcbe6bf8f";
    static String host = "http://sdk.open.api.igexin.com/apiex.htm";

    public static void main(String[] args) throws Exception {
       IGtPush push = new IGtPush(host, appKey, masterSecret);

       SingleMessage message = new SingleMessage();
       //判断客户端是否wifi环境下推送。2为仅在4G/3G/2G环境下推送，1为仅在wifi环境下推送，0为不限制网络环境。
       message.setPushNetWorkType(1); 

   }
}