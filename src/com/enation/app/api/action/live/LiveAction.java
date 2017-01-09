package com.enation.app.api.action.live;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import com.enation.app.api.action.BaseAction;
import com.enation.app.api.action.live.qiniu.LiveUtils;
import com.enation.app.api.action.live.qiniu.Stream;
import com.enation.app.api.action.live.rongyun.LiveRYUtil;
import com.enation.app.api.service.BannerService;
import com.enation.app.api.service.LiveService;
import com.enation.app.api.service.PersionService;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.service.impl.MemberManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
public class LiveAction extends BaseAction {

	private static final long serialVersionUID = 4701424070826462281L;
	
	@Resource
	private LiveService liveService;

	@Resource
	private BannerService bannerService;
	
	@Resource
	private PersionService persionService;
	
	@Resource
	private MemberManager memberService;
	
	/**
	 * 获取直播详情分享数据
	 */
	public void getLiveShare(){
		try{
			String name = paramObject.getString("liveName");
			Map<String,Object> liveDetails = liveService.getLiveDetails(name,false);
			Map<String,Object> bannerDetails = bannerService.getBannerDetailsByTitle(name);
			if(bannerDetails!=null){
				jsonObject.put("image", this.getImageUrl((String)bannerDetails.get("image")));
				jsonObject.put("title", (String)bannerDetails.get("title"));
			}
			jsonObject.put("details", (String)liveDetails.get("details"));
			String realPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			String url = realPath+"/apiAdmin/ShareAction_getLiveDetails.do?liveName="+name;
			jsonObject.put("url", url);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
			logger.error(e, e);
			this.logger.error("调用" + methodStr + "方法失败");
			this.logger.error("参数:" + requestStr);
			this.logger.error("获取直播分享详情失败！", e);
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
	 * 获取直播伪造用户列表
	 */
	public void fecthLiveUserList(){
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("liveName", paramObject.getString("liveName"));
			List<Map<String,Object>> resmap = liveService.getLiveUserList(map);
			if(resmap!=null){
				String path = request.getContextPath();
				String bpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
				JSONArray ja = new JSONArray();
				for(Map<String,Object> obj :resmap){
					JSONObject userObj = new JSONObject();
					userObj.put("userid", obj.get("userid"));
					userObj.put("username", obj.get("username"));
					userObj.put("userphoto", bpath+"/statics/"+(String)obj.get("userphoto"));
					ja.add(userObj);
				}
				jsonObject.put("liveUserList", ja);
			}
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取填充直播伪造用户列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 获取直播推流信息
	 */
	public void getLivePublishDetails() {
		try {
			String accessToken = paramObject.has("accessToken")?paramObject.getString("accessToken"):"e3b43d31e09f8f2b24335d063cb6114c";
			Member member = this.getMemberDetails(accessToken);
			String name = paramObject.getString("name");
			if(name==null||"".equals(name)){
				name = "yimei0001";
			}
			JSONObject credentialsObj = new JSONObject();
			credentialsObj.put("accessKey", LiveUtils.ACCESS_KEY);
			credentialsObj.put("secretKey", LiveUtils.SECRET_KEY);
			jsonObject.put("credentials", credentialsObj);
			Map<String,Object> liveDetails = liveService.getLiveDetails(name,true);
			String publishKey = null;
			String hub = null;
			String publishUrl = null;
			String playUrl = null;
			String title = null;
			if(liveDetails==null||liveDetails.size()==0){
				Stream streamObj = LiveUtils.createStream(name);
				try {
					publishKey = streamObj.getPublishKey();
				} catch (Exception e) {
					jsonObject.put("result", "FAILED");
					jsonObject.put("reason", "直播名字已存在");
					return;
				}
				hub = streamObj.getHubName();
				publishUrl = streamObj.rtmpPublishUrl();
				playUrl = streamObj.rtmpLiveUrls().get(Stream.ORIGIN);
				title = streamObj.getTitle();
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("title", name);
				map.put("publishKey", publishKey);
				map.put("hub", hub);
				map.put("disabled", 1);
				map.put("startTime", new Date().getTime()/1000);
				map.put("createTime", new Date().getTime()/1000);
				map.put("publish", publishUrl);
				map.put("isSave", -1);
				map.put("play", playUrl);
				map.put("playHls", streamObj.hlsLiveUrls().get(Stream.ORIGIN));
				map.put("title", title);
				map.put("streamId", streamObj.getStreamId());
				map.put("playback", streamObj.getPlaybackHlshost());
				map.put("memberId", member.getMember_id());
				map.put("details", "直播人比较懒，还没有来得及添加介绍！");
				liveService.saveLiveDetails(map);
			}else{
				publishKey = (String)liveDetails.get("publishKey");
				hub = (String)liveDetails.get("hub");
				publishUrl = (String)liveDetails.get("publish");
				playUrl = (String)liveDetails.get("play");
				title = (String)liveDetails.get("title");
			}
			if(member!=null){
				String tokenStr = LiveRYUtil.getRongYunToken(String.valueOf(member.getMember_id()),member.getUname(),member.getFace());
				jsonObject.put("rongyunToken", tokenStr);
			}
			jsonObject.put("publishKey", publishKey);
			jsonObject.put("hub", hub);
			jsonObject.put("disabled", false);
			jsonObject.put("publishUrl", publishUrl);
			jsonObject.put("playUrl", playUrl);
			jsonObject.put("chatId", title);
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
			Map<String,Object> liveDetails = liveService.getLiveDetails(name,false);
			Member member = this.getMemberDetails(paramObject.getString("accessToken"));
			String userphoto = this.getImageUrl(member.getFace());
			int memberId = member.getMember_id();
			String tokenStr = LiveRYUtil.getRongYunToken(String.valueOf(memberId),member.getUname(),userphoto);
			if(liveDetails!=null&&liveDetails.size()>0){
				jsonObject.put("rongyunToken", tokenStr);
				jsonObject.put("publishUrl", liveDetails.get("publish"));
				jsonObject.put("playUrl", liveDetails.get("play"));
				jsonObject.put("chatId", liveDetails.get("title"));
				jsonObject.put("liveName", name);
				if(persionService.getIsFriend(memberId,String.valueOf((int)liveDetails.get("memberId")))){
					jsonObject.put("isFriend", "yes");
				}else{
					jsonObject.put("isFriend", "no");
				}
				jsonObject.put("viewUserId", memberId);
				int liveUserId = (int)liveDetails.get("memberId");
				Member liveMember = memberService.get(liveUserId);
				jsonObject.put("userid", liveMember.getMember_id());
				jsonObject.put("username", liveMember.getUname());
				jsonObject.put("userphoto", this.getImageUrl(liveMember.getFace()));
				jsonObject.put("liveDetails", liveDetails.get("details"));
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
