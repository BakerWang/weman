package com.enation.app.api.pushMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.enation.app.api.model.DeviceToken;
import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
import com.gexin.rp.sdk.template.LinkTemplate;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.gexin.rp.sdk.template.TransmissionTemplate;

public class BasePushMessage {

	private static String appId = "oEhmlJKXY16aEIHiCwLW36";
    private static String appKey = "PodxYeQr5375P8kDzsXVB3";
    private static String masterSecret = "08dXmK1lNN6HjRQ7cGjKP9";
    private static String host = "http://sdk.open.api.igexin.com/apiex.htm";
	
    
   
    /**
     * 在线所有IOS用户的推送
     * @param clientId
     * @param pushContent
     * @throws Exception
     */
    public static void onlinePushAppMessage(String pushContentStr,List<DeviceToken> userDeviceToken) throws Exception{
    	 // 配置返回每个用户返回用户状态，可选
        System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        // 通知透传模板
        TransmissionTemplate template = transmissionTemplateDemo(pushContentStr); //透传消息模板
        ListMessage message = new ListMessage();
        message.setData(template);
        // 设置消息离线，并设置离线时间
        message.setOffline(true);
        // 离线有效时间，单位为毫秒，可选
        message.setOfflineExpireTime(24 * 1000 * 3600);
        // 配置推送目标
        List<Target> targets = new ArrayList<Target>();
        if(userDeviceToken==null){
        	return ;
        }
        for(DeviceToken dt:userDeviceToken){
        	Target target = new Target();
        	target.setAppId(appId);
        	target.setClientId(dt.getClientId());
        	targets.add(target);
        }
        // taskId用于在推送时去查找对应的message
        String taskId = push.getContentId(message);
        IPushResult ret = push.pushMessageToList(taskId, targets);
    }
    
    /**
     * 不在线所有IOS用户的推送
     * @param clientId
     * @param pushContent
     * @throws Exception
     */
    public static void offlinePushAppMessage(String pushContentStr,List<DeviceToken> userDeviceToken,Map<String,String> pushContent) throws Exception{
    	IGtPush push = new IGtPush(host, appKey, masterSecret);

        APNTemplate t = new APNTemplate();
        APNPayload apnpayload = new APNPayload();
        apnpayload.setSound("");
        APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
        //alertMsg.setTitle("aaaaaa");//通知文本消息标题
        //通知文本消息字符串
        if(pushContentStr.getBytes("UTF-8").length>140){
        	pushContentStr = pushContentStr.substring(0,40)+"...";
        }
        alertMsg.setBody(pushContentStr);
        //对于标题指定执行按钮所使用的Localizable.strings,仅支持IOS8.2以上版本
        alertMsg.setTitleLocKey("WeMan");
        //指定执行按钮所使用的Localizable.strings
        alertMsg.setActionLocKey("WeMan");
        if(pushContent!=null){
			for(String key:pushContent.keySet()){
				apnpayload.addCustomMsg(key, pushContent.get(key));
			}
	    }
        apnpayload.setAlertMsg(alertMsg);

        t.setAPNInfo(apnpayload);  
        ListMessage message = new ListMessage();
        message.setData(t);
        String contentId = push.getAPNContentId(appId, message);
        List<String> dtl = new ArrayList<String>();
        if(userDeviceToken==null){
        	return ;
        }
        for(DeviceToken dt:userDeviceToken){
        	dtl.add(dt.getDeviceToken());
        }
        System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true"); //显示每个用户的用户状态，false:不显示，true：显示 
        IPushResult ret = push.pushAPNMessageToList(appId, contentId, dtl);
    }
    
    /**
     * 在线的单独用户推送
     * @param clientId
     * @param pushContent
     * @throws Exception
     */
    public static void onlinePushSingleMessage(String clientId,String pushContent) throws Exception{
    	 IGtPush push = new IGtPush(host, appKey, masterSecret);
    	 	 TransmissionTemplate template = transmissionTemplateDemo(pushContent); //透传消息模板
    	 //	 LinkTemplate template = linkTemplateDemo(pushContent); //打开网页模板
    	// NotificationTemplate template = notificationTemplateDemo(pushContent); //打开应用模板
         SingleMessage message = new SingleMessage();
         message.setOffline(false);
         // 离线有效时间，单位为毫秒，可选
         //message.setOfflineExpireTime(24 * 3600 * 1000);
         message.setData(template);
         // 可选，1为wifi，0为不限制网络环境。根据手机处于的网络情况，决定是否下发
         message.setPushNetWorkType(0); 
         Target target = new Target();
         target.setAppId(appId);
         target.setClientId(clientId);
         //target.setAlias(Alias);
         push.pushMessageToSingle(message, target);
    }
    
    /**
     * 不在线的单独用户推送
     * @param clientId
     * @param pushContent
     * @throws Exception
     */
    public static void offlinePushSingleMessage(String content,String clientId,Map<String,String> pushContent) throws Exception{
    	  IGtPush push = new IGtPush(host, appKey, masterSecret);  
          APNTemplate t = new APNTemplate();
          APNPayload apnpayload = new APNPayload();
          //apnpayload.setSound("");
          //apn高级推送
          APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
          ////通知文本消息标题
          //alertMsg.setTitle("aaaaaa");
          //通知文本消息字符串
          if(content.getBytes("UTF-8").length>140){
				content = content.substring(0,40)+"...";
		  }
          alertMsg.setBody(content);
          //对于标题指定执行按钮所使用的Localizable.strings,仅支持IOS8.2以上版本
          alertMsg.setTitleLocKey("WeMan");
          //指定执行按钮所使用的Localizable.strings
          alertMsg.setActionLocKey("WeMan");
          apnpayload.setAlertMsg(alertMsg);
          
          if(pushContent!=null){
				for(String key:pushContent.keySet()){
					apnpayload.addCustomMsg(key, pushContent.get(key));
				}
		  }
          t.setAPNInfo(apnpayload);
          SingleMessage sm = new SingleMessage();
          sm.setData(t);
          push.pushAPNMessageToSingle(appId, clientId, sm);
    }
    
    
	/**
	 * 透传消息模板
	 * @param pushContent
	 * @return
	 */
	public static TransmissionTemplate transmissionTemplateDemo(String pushContent) {
	    TransmissionTemplate template = new TransmissionTemplate();
	    template.setAppId(appId);
	    template.setAppkey(appKey);
	    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionType(2);
	    template.setTransmissionContent(pushContent);
	    // 设置定时展示时间
	    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	    return template;
	}
	
	
	
	/**
	 * 点击通知打开应用模板
	 * @param appId
	 * @param appKey
	 * @return
	 */
	public static LinkTemplate linkTemplateDemo(String pushContent) {
	    LinkTemplate template = new LinkTemplate();
	    // 设置APPID与APPKEY
	    template.setAppId(appId);
	    template.setAppkey(appKey);
	    // 设置通知栏标题与内容
	    template.setTitle("我是标题");
	    template.setText("我是通知栏内容");
	    // 配置通知栏图标
	    template.setLogo("icon.png");
	    // 配置通知栏网络图标
	    template.setLogoUrl("");
	    // 设置通知是否响铃，震动，或者可清除
	    template.setIsRing(true);
	    template.setIsVibrate(true);
	    template.setIsClearable(true);
	    // 设置打开的网址地址
	    template.setUrl("http://www.baidu.com");
	    // 设置定时展示时间
	    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	    return template;
	}
	
	/**
	 * 点击通知打开网页模板
	 * @param appId
	 * @param appkey
	 * @return
	 */
	public static NotificationTemplate notificationTemplateDemo(String pushContent) {
	    NotificationTemplate template = new NotificationTemplate();
	    // 设置APPID与APPKEY
	    template.setAppId(appId);
	    template.setAppkey(appKey);
	    // 设置通知栏标题与内容
	    template.setTitle("我是通知栏标题");
	    template.setText("我是通知栏内容");
	    // 配置通知栏图标
	    template.setLogo("icon.png");
	    // 配置通知栏网络图标
	    template.setLogoUrl("");
	    // 设置通知是否响铃，震动，或者可清除
	    template.setIsRing(true);
	    template.setIsVibrate(true);
	    template.setIsClearable(true);
	    // 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
	    template.setTransmissionType(1);
	    template.setTransmissionContent(pushContent);
	    // 设置定时展示时间
	    // template.setDuration("2015-01-16 11:40:00", "2015-01-16 12:24:00");
	    return template;
	}
	
	 /**
     * 获取个推的长连接是否在线  
     * @param clientId
     * @return  online offline
     */
    public static String getUserStatus(String clientId) {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        IQueryResult abc = push.getClientIdStatus(appId, clientId);
        String result = (String) abc.getResponse().get("result");
        return result.toLowerCase();
    }
	
}
