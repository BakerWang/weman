package com.enation.app.api.action.live.qiniu;

import java.util.Date;
import java.util.List;

import com.enation.app.api.action.live.qiniu.Hub;
import com.enation.app.api.action.live.qiniu.Stream;
import com.enation.app.api.action.live.qiniu.Stream.SaveAsResponse;
import com.enation.app.api.action.live.qiniu.Stream.Status;
import com.enation.app.api.action.live.qiniu.Stream.StreamList;

import net.sf.json.JSONObject;

public class LiveUtils {

	public static String ACCESS_KEY = "b7Zkl_Ld9nHfP2v3K2GhJ3mVoE_u9KRz79yjjyDa";
	public static String SECRET_KEY = "wC3N9ZvE4MjaaVVvwk2HVGCzDvO4x9GGoLyzV9vR";
	private static String HUB_NAME = "test-02";
	private static Hub hub = null;

	static {
		Credentials credentials = new Credentials(ACCESS_KEY, SECRET_KEY); // Credentials
																			// Object
		hub = new Hub(credentials, HUB_NAME);
	}

	
	public static void main(String[] args) {
		try {
			//System.out.println(createStream("100010").toJsonString());
			//System.out.println(getStream("z1.test-02.mjchen134").toJsonString());
			//z1.test-02.123456||1483069478||1483077595
			saveLive("z1.test-02.123456",1483069478,1483077595);
			//String str=""
			//System.out.println(JSONObject.fromObject(response.toString()).get(""));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 创建流
	public static Stream createStream(String streamName) throws Exception {
		// String title = null; // optional, auto-generated as default
		// String publishKey = null; // optional, auto-generated as default
		// String publishSecurity = null; // optional, can be "dynamic" or
		// "static", "dynamic" as default
		// Stream stream = null;
		try {
			
			Stream stream = hub.createStream(streamName, null, "static");
			return stream;
			/*
			 * { "id":"z1.test-hub.55d97350eb6f92638c00000a",
			 * "createdAt":"2015-08-22T04:54:13.539Z",
			 * "updatedAt":"2015-08-22T04:54:13.539Z",
			 * "title":"55d97350eb6f92638c00000a", "hub":"test-hub",
			 * "disabled":false, "publishKey":"ca11e07f094c3a6e",25039fa6d6cc1611
			 * "publishSecurity":"dynamic", "hosts":{ "publish":{
			 * "rtmp":"ey636h.publish.z1.pili.qiniup.com" }, "live":{
			 * "hdl":"ey636h.live1-hdl.z1.pili.qiniucdn.com",
			 * "hls":"ey636h.live1-hls.z1.pili.qiniucdn.com",
			 * "rtmp":"ey636h.live1-rtmp.z1.pili.qiniucdn.com" }, "playback":{
			 * "hls":"ey636h.playback1.z1.pili.qiniucdn.com" } } }
			 */
		} catch (PiliException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 得到流
	public static Stream getStream(String streamName) throws Exception {
		try {
			Stream stream = hub.getStream(streamName);
			return stream;
			/*
			 * { "id":"z1.test-hub.55d80075e3ba5723280000d2",
			 * "createdAt":"2015-08-22T04:54:13.539Z",
			 * "updatedAt":"2015-08-22T04:54:13.539Z",
			 * "title":"55d80075e3ba5723280000d2", "hub":"test-hub",
			 * "disabled":false, "publishKey":"ca11e07f094c3a6e",
			 * "publishSecurity":"dynamic", "hosts":{ "publish":{
			 * "rtmp":"ey636h.publish.z1.pili.qiniup.com" }, "live":{
			 * "hdl":"ey636h.live1-hdl.z1.pili.qiniucdn.com",
			 * "hls":"ey636h.live1-hls.z1.pili.qiniucdn.com",
			 * "rtmp":"ey636h.live1-rtmp.z1.pili.qiniucdn.com" }, "playback":{
			 * "hls":"ey636h.playback1.z1.pili.qiniucdn.com" } } }
			 */
		} catch (PiliException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 列出所有流
	public static List<Stream> getStreamList() throws Exception {
		try {
			String status = null; // optional, can only be "connected"
			String marker = null; // optional
			long limit = 0; // optional
			String titlePrefix = null; // optional

			StreamList streamList = hub.listStreams(status, marker, limit, titlePrefix);
			List<Stream> list = streamList.getStreams();
			for (Stream s : list) {
				// access the stream
			}
			return list;
			/*
			 * marker:10 isEnd:false stream object
			 */
		} catch (PiliException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 查询直播状态
	public static String getStreamStatus(String streamName) throws Exception {
		try {
			Stream stream = getStream(streamName);
			Status status = stream.status();
			return status.toString();
			/*
			 * { "addr":"222.73.202.226:2572", "startFrom":
			 * "2015-09-10T05:58:10.289+08:00", "status":"disconnected",
			 * "bytesPerSecond":0, "framesPerSecond":{ "audio":0, "video":0,
			 * "data":0 } }
			 */
		} catch (PiliException e) {
			e.printStackTrace();
		}
		return null;
	}

	// 保存直播数据
	public static String saveLive(String streamName,long startTime,long endTime) throws Exception {
		String saveAsFormat = "mp4"; // required
		String saveAsName = "weman" + new Date().getTime() + "." + saveAsFormat; // required
		long saveAsStart = startTime; // required, in second, unix timestamp
		long saveAsEnd = endTime; // required, in second, unix timestamp
		String saveAsNotifyUrl = null; // optional
		try {
			Stream stream = getStream(streamName);
			SaveAsResponse response = stream.saveAs(saveAsName, saveAsFormat, saveAsStart, saveAsEnd, saveAsNotifyUrl,
					null);
			System.out.println(response.toString());
			return response.toString();
			/*
			 * { "url":
			 * "http://ey636h.vod1.z1.pili.qiniucdn.com/recordings/z1.test-hub.55d81a72e3ba5723280000ec/videoName.m3u8",
			 * "targetUrl":
			 * "http://ey636h.vod1.z1.pili.qiniucdn.com/recordings/z1.test-hub.55d81a72e3ba5723280000ec/videoName.mp4",
			 * "persistentId":"z1.55d81c6c7823de5a49ad77b3" }
			 */
		} catch (PiliException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	// RTMP推流地址
	public static String getRTMPPublishUrl(String streamName) throws Exception {
		Stream stream = getStream(streamName);
		String originUrl = stream.rtmpLiveUrls().get(Stream.ORIGIN);
		// rtmp://ey636h.live1-rtmp.z1.pili.qiniucdn.com/test-hub/55d8113ee3ba5723280000dc
		return originUrl;
	}

	// RTMP直播地址
	public static String getRTMPPlayURL(String streamName) throws Exception {
		try {
			Stream stream = getStream(streamName);
			String publishUrl = stream.rtmpPublishUrl();
			// rtmp://ey636h.publish.z1.pili.qiniup.com/test-hub/55d810aae3ba5723280000db?nonce=1440223404&token=hIVJje0ZOX9hp7yPIvGBmJ_6Qxo=
			return publishUrl;
		} catch (PiliException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public static String getUserToken(Integer member_id, String uname, String face) {
		
		return null;
	}
}
