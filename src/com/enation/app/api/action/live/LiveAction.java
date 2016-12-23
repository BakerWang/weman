package com.enation.app.api.action.live;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import com.enation.app.api.action.BaseAction;
import com.enation.app.api.action.live.qiniu.LiveUtils;
import com.enation.app.api.action.live.qiniu.Stream;
import com.enation.app.api.action.live.rongyun.LiveRYUtil;
import com.enation.app.api.service.LiveService;
import com.enation.app.base.core.model.Member;

import net.sf.json.JSONObject;

@Scope("prototype")
public class LiveAction extends BaseAction {

	private static final long serialVersionUID = 4701424070826462281L;
	
	@Resource
	private LiveService liveService;

	/**
	 * 获取直播推流信息
	 */
	public void getLivePublishDetails() {
		try {
			String name = paramObject.getString("name");
			if(name==null||"".equals(name)){
				name = "yimei0001";
			}
			JSONObject credentialsObj = new JSONObject();
			credentialsObj.put("accessKey", LiveUtils.ACCESS_KEY);
			credentialsObj.put("secretKey", LiveUtils.SECRET_KEY);
			jsonObject.put("credentials", credentialsObj);
			Map<String,Object> liveDetails = liveService.getLiveDetails(name);
			String publishKey = null;
			String hub = null;
			String publishUrl = null;
			String playUrl = null;
			if(liveDetails==null||liveDetails.size()==0){
				Stream streamObj = LiveUtils.createStream(name);
				publishKey = streamObj.getPublishKey();
				hub = streamObj.getHubName();
				publishUrl = streamObj.rtmpPublishUrl();
				playUrl = streamObj.rtmpLiveUrls().get(Stream.ORIGIN);
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("title", name);
				map.put("publishKey", publishKey);
				map.put("hub", hub);
				map.put("disabled", 1);
				map.put("startTime", new Date().getTime());
				map.put("createTime", new Date().getTime());
				map.put("publish", publishUrl);
				map.put("isSave", -1);
				map.put("play", playUrl);
				map.put("title", streamObj.getTitle());
				map.put("streamId", streamObj.getStreamId());
				map.put("playback", streamObj.getPlaybackHlshost());
				liveService.saveLiveDetails(map);
			}else{
				publishKey = (String)liveDetails.get("publishKey");
				hub = (String)liveDetails.get("hub");
				publishUrl = (String)liveDetails.get("publish");
				playUrl = (String)liveDetails.get("play");
			}
			jsonObject.put("publishKey", publishKey);
			jsonObject.put("hub", hub);
			jsonObject.put("disabled", false);
			jsonObject.put("publishUrl", publishUrl);
			jsonObject.put("playUrl", playUrl);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			logger.error(e, e);
			this.logger.error("调用" + methodStr + "方法失败");
			this.logger.error("参数:" + requestStr);
			this.logger.error("获取录播信息失败", e);
			this.logger.error(e, e);
			if ("success".equalsIgnoreCase(jsonObject.getString("result"))) {
				jsonObject.put("result", "FAILED");
			}
			if (!jsonObject.containsKey("reason")) {
				jsonObject.put("reason", "系统错误！");
			}
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}

	
	/**
	 * 获取直播拉流信息
	 */
	public void getLivePlayDetails() {
		try {
			String name = paramObject.getString("name");
			Map<String,Object> liveDetails = liveService.getLiveDetails(name);
			Member member = this.getMemberDetails(paramObject.getString("accessToken"));
			String userphoto = this.getImageUrl(member.getFace());
			String tokenStr = LiveRYUtil.getRongYunToken(member.getMember_id().toString(),member.getUname(),userphoto);
			if(liveDetails!=null&&liveDetails.size()>0){
				jsonObject.put("rongyunToken", tokenStr);
				jsonObject.put("publishUrl", liveDetails.get("publish"));
				jsonObject.put("playUrl", liveDetails.get("play"));
				jsonObject.put("userid", member.getMember_id());
				jsonObject.put("username", member.getUname());
				jsonObject.put("userphoto", userphoto);
			}else{
				jsonObject.put("result", "noPublish");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			logger.error(e, e);
			this.logger.error("调用" + methodStr + "方法失败");
			this.logger.error("参数:" + requestStr);
			this.logger.error("获取播放信息失败", e);
			this.logger.error(e, e);
			if ("success".equalsIgnoreCase(jsonObject.getString("result"))) {
				jsonObject.put("result", "FAILED");
			}
			if (!jsonObject.containsKey("reason")) {
				jsonObject.put("reason", "系统错误！");
			}
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 获取用户的聊天室token
	 */
	public void getUserToken(){
		try {
			Member member = this.getMemberDetails(paramObject.getString("accessToken"));
			if(member == null){
				jsonObject.put("result", "failed");
				jsonObject.put("reason", "未登录");
			}else{
				String tokenStr = LiveRYUtil.getRongYunToken(member.getMember_id().toString(),member.getUname(),member.getFace());
				if(tokenStr==null){
					jsonObject.put("rongyunToken", tokenStr);
					jsonObject.put("result", "failed");
					jsonObject.put("reason", "获取用户token失败!");
				}else{
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			logger.error(e, e);
			this.logger.error("调用" + methodStr + "方法失败");
			this.logger.error("参数:" + requestStr);
			this.logger.error("获取播放信息失败", e);
			this.logger.error(e, e);
			if ("success".equalsIgnoreCase(jsonObject.getString("result"))) {
				jsonObject.put("result", "FAILED");
			}
			if (!jsonObject.containsKey("reason")) {
				jsonObject.put("reason", "系统错误！");
			}
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
}
