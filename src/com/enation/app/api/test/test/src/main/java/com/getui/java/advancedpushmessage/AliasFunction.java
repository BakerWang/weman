package com.enation.app.api.test.test.src.main.java.com.getui.java.advancedpushmessage;

import com.gexin.rp.sdk.base.IAliasResult;
import com.gexin.rp.sdk.http.IGtPush;

public class AliasFunction {
   //采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
	 private static String appId = "SDf4Q9pSd38mLktu2XuR36";
	    private static String appKey = "PsufBWfwv599y4ckXz1Hr8";
	    private static String masterSecret = "RJInoKLyDJ8QYQxnNpbb29";

	    static String CID = "eddbf2cd79f5f22da6ef554bcbe6bf8f";
	static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    static String Alias = "aliastest";
	
	public static void main(String[] args) throws Exception {
	    IGtPush push = new IGtPush(host, appKey, masterSecret);
	
	    IAliasResult bindSCid = push.bindAlias(appId, Alias, CID);
	    System.out.println("绑定结果：" + bindSCid.getResult() + "错误码:" + bindSCid.getErrorMsg());
	
	}
}