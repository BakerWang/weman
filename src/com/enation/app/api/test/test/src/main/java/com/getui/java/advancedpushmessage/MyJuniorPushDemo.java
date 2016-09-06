package com.enation.app.api.test.test.src.main.java.com.getui.java.advancedpushmessage;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.SingleMessage;
import com.gexin.rp.sdk.base.payload.APNPayload;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.APNTemplate;
public class MyJuniorPushDemo {
	//采用"Java SDK 快速入门"， "第二步 获取访问凭证 "中获得的应用配置，用户可以自行替换
    private static String appId = "oEhmlJKXY16aEIHiCwLW36";
    private static String appKey = "PodxYeQr5375P8kDzsXVB3";
    private static String masterSecret = "08dXmK1lNN6HjRQ7cGjKP9";
    static String devicetoken = "d418abdd798a70a5252010dbff325222400636db901a51eb4fce73d8ce44345a";//iOS设备唯一标识，由苹果那边生成
    private static String clientId = "54c707552f3d42b6531abbf93cee5fd0";//个推的唯一标示
    static String url = "http://sdk.open.api.igexin.com/apiex.htm";
       public static void apnpush() throws Exception {
              IGtPush push = new IGtPush(url, appKey, masterSecret);  
              APNTemplate t = new APNTemplate();
              APNPayload apnpayload = new APNPayload();
              apnpayload.setSound("");
              //apn高级推送
              APNPayload.DictionaryAlertMsg alertMsg = new APNPayload.DictionaryAlertMsg();
              ////通知文本消息标题
              alertMsg.setTitle("aaaaaa");
              //通知文本消息字符串
              alertMsg.setBody("1232");
              //对于标题指定执行按钮所使用的Localizable.strings,仅支持IOS8.2以上版本
              alertMsg.setTitleLocKey("WeMan我们");
              //指定执行按钮所使用的Localizable.strings
              alertMsg.setActionLocKey("ddddd");
              apnpayload.setAlertMsg(alertMsg);

              t.setAPNInfo(apnpayload);
              SingleMessage sm = new SingleMessage();
              sm.setData(t);
              IPushResult ret0 = push.pushAPNMessageToSingle(appId, devicetoken, sm);
              System.out.println(ret0.getResponse());

       }

       public static void main(String[] args) throws Exception {
              apnpush();
       }
}