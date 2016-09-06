package com.enation.app.api.test.test.src.main.java.com.getui.java.query;

import com.gexin.rp.sdk.base.IQueryResult;
import com.gexin.rp.sdk.http.IGtPush;

public class GetUserStatus {
	 private static String appId = "oEhmlJKXY16aEIHiCwLW36";
	    private static String appKey = "PodxYeQr5375P8kDzsXVB3";
	    private static String masterSecret = "08dXmK1lNN6HjRQ7cGjKP9";
	static String CID = "54c707552f3d42b6531abbf93cee5fd0";

    static String host = "http://sdk.open.api.igexin.com/apiex.htm";
    public static void main(String[] args) throws Exception {
//        IGtPush push = new IGtPush(host, appKey, masterSecret);
//        push.connect();
        getUserStatus();
    }

    public static void getUserStatus() {
        IGtPush push = new IGtPush(host, appKey, masterSecret);
        IQueryResult abc = push.getClientIdStatus(appId, CID);
        System.out.println(abc.getResponse());
    }
}