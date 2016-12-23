package com.enation.app.api.action.live.rongyun;

import java.io.Reader;

import com.enation.app.api.action.live.rongyun.models.TokenReslut;
import com.enation.app.base.core.model.Member;

import net.sf.json.JSONObject;

public class LiveRYUtil {

	private static String appKey = "8brlm7uf8pdn3";//替换成您的appkey
	private static String appSecret = "9sg4yYSctU";//替换成匹配上面key的secret
	
	public static void main(String[] args) {
		try {
			String appKey = "8brlm7uf8pdn3";//替换成您的appkey
			String appSecret = "9sg4yYSctU";//替换成匹配上面key的secret
			
			Reader reader = null ;
			RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
					
			
			System.out.println("************************User********************");
			// 获取 Token 方法 
			TokenReslut userGetTokenResult = rongCloud.user.getToken("100000001", "username", "http://www.rongcloud.cn/images/logo.png");
			System.out.println("getToken:  " + userGetTokenResult.toString());
			System.out.println(JSONObject.fromObject(userGetTokenResult).getString("token"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getRongYunToken(String userid,String username,String userphoto) throws Exception{
		RongCloud rongCloud = RongCloud.getInstance(appKey, appSecret);
		// 获取 Token 方法  member.getMember_id().toString(), member.getUname(), member.getFace()
		TokenReslut userGetTokenResult = rongCloud.user.getToken(userid,username,userphoto);
		return JSONObject.fromObject(userGetTokenResult).getString("token");
	}
	
	
	
}
