package com.enation.app.api.pushMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.enation.app.api.model.DeviceToken;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class PushMessage {

    
    public static void main(String[] args) {
    	//System.out.println(getUserStatus("54c707552f3d42b6531abbf93cee5fd0"));
    	String content="我是推送！！";
    	Map<String,String>dataMap = new HashMap<String,String>();
    	dataMap.put("type", "web");
    	dataMap.put("data", "http://www.baidu.com");
    	DeviceToken dt = new DeviceToken();
    	dt.setToken("63922e4bada09dfe0dbe0fd0d6b6939bd90c67fe0e96928719c217fe014e4e80");
    	dt.setTokenType("iphone");
    	dt.setClientId("2f42fe2a573a5acf7dc4f326b53ec775");
//    	dt.setToken("404f9f535df2b917a27b4b7f4fa24586cae3909a4acb5f071b697f42f9085230");
//    	dt.setTokenType("iphone");
//    	dt.setClientId("54c707552f3d42b6531abbf93cee5fd0");
    	DeviceToken dt2 = new DeviceToken();
    	dt2.setToken("f389512ed34ecddd93e9b741cc47f7e8348e162d40466e18c812034bb079972d");
    	dt2.setTokenType("iphone");
    	dt2.setClientId("6a4fb9eaaa98824d3272b148ecd60632");
//    	DeviceToken dt3 = new DeviceToken();
//    	dt3.setToken("b468835d5120cf710c5f2018f93e0b154f9874039f3e8d8b178caa4d5a65fdf8");
//    	dt3.setTokenType("iphone");
//    	dt3.setClientId("b59329e6715b3fce79bbc761fab488f9");
    	List<DeviceToken> dts = new ArrayList<DeviceToken>();
    	dts.add(dt);
    	dts.add(dt2);
    	//dts.add(dt3);
    	try {
			//pushSingleUser(content, dataMap, dt);
    		pushUserList("大家好", dataMap, dts);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    
    /**
     * 推送用户列表
     * @param content
     * @param dataMap
     * @param userMessage
     * @throws Exception
     */
    public static void pushUserList(String content,Map<String,String> dataMap,List<DeviceToken> userMessages) throws Exception{
    	List<DeviceToken> onlineUser = new ArrayList<DeviceToken>();
    	List<DeviceToken> offlineUser = new ArrayList<DeviceToken>();
    	if(userMessages==null){return;};
    	for(DeviceToken dt:userMessages){
    		String status = BasePushMessage.getUserStatus(dt.getClientId());
    		if(status.equals("online")){
    			onlineUser.add(dt);
    		}else{
    			offlineUser.add(dt);
    		}
    	}
    	if(onlineUser.size()>0){
    		JSONObject dataObj = new JSONObject();
    		for(String str:dataMap.keySet()){
    			dataObj.put(str, dataMap.get(str));
    		}
    		BasePushMessage.onlinePushAppMessage(dataObj.toString(), onlineUser);
    	}
    	if(offlineUser.size()>0){
    		BasePushMessage.offlinePushAppMessage(content, offlineUser, dataMap);
    	}
    }	
    
    /**
     * 推送单个用户（IOS分为个推和APNS）
     * @param content
     * @param dataMap
     * @param userMessage
     * @throws Exception
     */
    public static void pushSingleUser(String content,Map<String,String> dataMap,DeviceToken userMessage) throws Exception{
    	if(userMessage.getTokenType().equals("iphone")){
    		if(dataMap!=null){
    			if(BasePushMessage.getUserStatus(userMessage.getClientId()).equals("online")){
    				JSONObject dataObj = new JSONObject();
    				for(String str:dataMap.keySet()){
    					dataObj.put(str, dataMap.get(str));
    				}
    				BasePushMessage.onlinePushSingleMessage(userMessage.getClientId(), dataObj.toString());
    			}else{
    				BasePushMessage.offlinePushSingleMessage(content, userMessage.getToken(), dataMap);
    			}
    		}
    	}
    }
    
}
