package com.enation.app.api.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;

import com.enation.app.api.service.InitUserService;
import com.enation.app.api.util.Utils;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.service.impl.MemberManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;

import net.sf.json.JSONObject;
@Scope("prototype")
public class InitUserAction extends BaseAction{

	private static final long serialVersionUID = -5710249725067303583L;
	
	@Resource
	private InitUserService initUserService;
	@Resource
	private MemberManager memberService;
	
	/**
	 * 获取网站初始化信息    包含最新版本号
	 */
	public void catchAppInitMessage(){
		try{
			Map<String,Object> map = initUserService.catchAppInitMessage(clientType);//clientType为iphone或者android
			if(map!=null){
				String version = (String)map.get("version");
				jsonObject.put("lasterVersion", version);
				if(map.containsKey("hotTags")){
					jsonObject.put("hotTags", map.get("hotTags"));
				}
				String impress = "MAN,COOL,FASHION";
				jsonObject.put("impress", impress);
				String path = request.getContextPath();
				String bpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
				jsonObject.put("faqUrl", bpath+"/api/faq.html");//常见问题
				jsonObject.put("aboutUrl", bpath+"/api/question.html");//关于WeMan
				jsonObject.put("bodyUrl", bpath+"/api/body.html");//身型说明
				jsonObject.put("skinUrl", bpath+"/api/skin.html");//肤质说明
				jsonObject.put("loginBackgroundImage", this.getImageUrl("attachment/allDefaultImage/loginBackgroundImage.png"));//登录背景图
				jsonObject.put("startImage", this.getImageUrl("attachment/allDefaultImage/startImageD.gif"));//启动图
				jsonObject.put("eulaUrl", bpath+"/api/eula.html");
				jsonObject.put("fontColor", "c2c2c2");
				jsonObject.put("fontColor2", "ff6251");
				jsonObject.put("index", "首页");
				jsonObject.put("indexImage", this.getImageUrl("attachment/allDefaultImage/shouyehui.png"));
				jsonObject.put("indexImage2", this.getImageUrl("attachment/allDefaultImage/shouyehong.png"));
				jsonObject.put("find", "发现");
				jsonObject.put("findImage", this.getImageUrl("attachment/allDefaultImage/faxianhui.png"));
				jsonObject.put("findImage2", this.getImageUrl("attachment/allDefaultImage/faxianhong.png"));
				jsonObject.put("show", "SHOW");
				jsonObject.put("showImage", this.getImageUrl("attachment/allDefaultImage/SHOWhui.png"));
				jsonObject.put("showImage2", this.getImageUrl("attachment/allDefaultImage/SHOWhong.png"));
				jsonObject.put("me", "我");
				jsonObject.put("meImage", this.getImageUrl("attachment/allDefaultImage/wohui.png"));
				jsonObject.put("meImage2", this.getImageUrl("attachment/allDefaultImage/wohong.png"));
			}else{
				jsonObject.put("result", "FAILED");
				jsonObject.put("reason", "系统错误！");
			}
		}catch(Exception e){
			logger.error(e);
			logger.error(e,e);
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取版本失败", e);
			this.logger.error(e,e);
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
		}finally{
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	/**
	 * 绑定clientId，个推的唯一标示
	 */
	public void accessClientId(){
		try{
			String deviceToken = (String) paramObject.get("deviceToken");
			String clientId = (String) paramObject.get("clientId");
			String loginType = (String) paramObject.get("clientType");
			String accessToken = paramObject.has("accessToken")?(String)paramObject.get("accessToken"):null;
			int cmemberId = 0;
			if(accessToken!=null){
				cmemberId = this.getMemberId(paramObject.getString("accessToken"));
			}
			if(loginType==null){
				loginType="iphone";
			}
			initUserService.bindClientId(deviceToken,clientId,loginType,cmemberId);
		} catch (Exception e) {
			e.printStackTrace();
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("绑定clientId失败！", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 修改密码
	 */
	public void verfityPassword(){
		try{
			String phoneNumber = paramObject.getString("phoneNumber");
			String newPassword = paramObject.getString("newPassword");
			Member member = memberService.getMemberByMobile(phoneNumber.trim());
			if(member==null){
				jsonObject.put("result", "FAILED");
				jsonObject.put("reason", "此手机号未注册！");
				return;
			}else{
				memberService.updatePassword(member.getMember_id(), newPassword);
				jsonObject.put("result", "SUCCESS");
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("登录失败", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	/**
	 * 发送验证码
	 * type:1是注册  2是忘记密码
	 */
	public void sendPhoneMsg(){
		try{
			int type = Integer.parseInt(paramObject.getString("type"));//1是注册  2是忘记密码
			String phoneNumber = paramObject.getString("phoneNumber");
			if(!Utils.isTelphone(phoneNumber)){
				jsonObject.put("result", "FAILED");
				jsonObject.put("reason", "请输入正确的手机号！");
			}else{
				if(type==1 && memberService.getMemberByMobile(phoneNumber.trim())!=null){
					jsonObject.put("result", "FAILED");
					jsonObject.put("reason", "该手机号已被注册！");
					return;
				}
				if(type==2 && memberService.getMemberByMobile(phoneNumber.trim())==null){
					jsonObject.put("result", "FAILED");
					jsonObject.put("reason", "该手机号未注册！");
					return;
				}
				String code = Utils.sendCode(phoneNumber,type);
				//发送验证码
				if(code!=null){
					jsonObject.put("verificatString",code);
				}else{
					jsonObject.put("result", "FAILED");
					jsonObject.put("reason", "系统错误！");
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("登录失败", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 测试用的接口   
	 * 获取用户的accessToken
	 */
	public void fecthAccessToken(){
		try{
			String username = paramObject.getString("username");
			String accessToken = initUserService.getAccessTokenByUname(username);
			jsonObject.put("accessToken", accessToken);
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("result", "FAILED");
			jsonObject.put("reason", "系统错误！");
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("登录失败", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	private java.io.File originalCover;//头像原图
	private java.io.File cutCover;//头像截图
	
	private String originalCoverFileName;//文件的名字
	private String cutCoverFileName;//文件的名字
	
	/**
	 * 用户注册
	 */
	public void register(){
		try {
			if(paramObject.has("username")&&paramObject.has("deviceToken")){
				String mobile = paramObject.has("username")?(String) paramObject.get("mobile"):null;
				String username = (String) paramObject.get("username");
				String password = paramObject.has("username")?(String) paramObject.get("password"):null;
				String height = (String) paramObject.get("height");
				String weight = (String) paramObject.get("weight");
				String birthday =(String) paramObject.get("birthday");
				String deviceToken = (String) paramObject.get("deviceToken");
				String loginType = (String) paramObject.get("clientType");
				String sex = (String) paramObject.get("sex");
				String registerip = request.getRemoteAddr();
				Member member = new Member();
				member.setUname(username);
				member.setPassword(password);
				member.setMobile(mobile);
				member.setRegisterip(registerip);
				member.setSex(Integer.parseInt(sex));//0是女  1是男
				member.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday).getTime());
				member.setHeight(Integer.parseInt(height));
				member.setWeight(Integer.parseInt(weight));
				if(cutCover!=null){
					//封面的原图
					if (FileUtil.isAllowUp(cutCoverFileName)) {
						//String path = UploadUtil.upload(cutCover,cutCoverFileName, "photoImage");
						String saveName = uploadImage(cutCover, cutCoverFileName, "photoImage");
						member.setFace(saveName);
					}
					if(originalCover!=null){
						if (FileUtil.isAllowUp(originalCoverFileName)) {
							String saveName = uploadImage(originalCover, originalCoverFileName, "photoImage");
							member.setOrFace(saveName);
						}
					}
				}else{
					member.setFace("attachment/photoImage/photoNullImage.png");
					member.setOrFace("attachment/photoImage/photoOrNullImage.png");
				}
				String bindType =  paramObject.has("bindType")?(String)paramObject.get("bindType"):null;
				String bindNum = paramObject.has("bindNum")?(String)paramObject.getString("bindNum"):null;
				if(loginType==null){
					loginType="iphone";
				}
				String clientId = paramObject.has("clientId")?paramObject.getString("clientId"):null;
				int resInt = initUserService.register(member,loginType,deviceToken,bindType,bindNum,clientId);
				if(resInt==0){
					jsonObject.put("result", "FAILED");
					jsonObject.put("reason", "此手机号或用户名已被注册!");
				}else{
					String accessToken = initUserService.getAccessTokenByUname(member.getUname());
					jsonObject.put("accessToken", accessToken);
				}
			}else{
				jsonObject.put("result", "FAILED");
				jsonObject.put("reason", "参数错误！");
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
			this.logger.error("注册失败", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	
	/**
	 * 用户登录
	 */
	public void login(){
		try{
			String account = paramObject.getString("account");
			String password = paramObject.getString("password");
			String deviceToken = (String) paramObject.get("deviceToken");
			String loginType = (String) paramObject.get("clientType");
			String clientId = paramObject.has("clientId")?paramObject.getString("clientId"):null;
			if(loginType==null){
				loginType="iphone";
			}
			Map<String,Object> map = initUserService.login(account,password,deviceToken,loginType,clientId);
			if(map==null){
				jsonObject.put("result", "FAILED");
				jsonObject.put("reason", "用户名或密码失败！");
			}else{
				baseLoginMsg(map,jsonObject);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("登录失败", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	/**
	 * 验证代登陆时是否绑定
	 */
	public void verifyBindAccount(){
		try{
			String bnum = paramObject.getString("bindNum");//代登陆的微信凭证
			String type= paramObject.getString("bindType");//type 1.weixin 2.qq 3.sina
			Map<String,Object> map = initUserService.login_verifybind(bnum,type);
			if(map==null){
				jsonObject.put("result", "no");
			}else{
				baseLoginMsg(map,jsonObject);
			}
		}catch(Exception e){
			e.printStackTrace();
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("验证代登陆失败", e);
			this.logger.error(e,e);
		}finally{
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 分享url
	 * type：1.theme 2.article
	 */
	public void shareUrl(){
		try{
			String type = paramObject.getString("type");
			int dataId = Integer.parseInt(paramObject.getString("data_id"));
			Map<String,Object> resmap = initUserService.shareUrl(type,dataId);
			String realPath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			if("theme".equals(type)){
				String url = realPath+"/apiAdmin/ShareAction_getDataDetails.do?type=theme&dataId="+String.valueOf((int)resmap.get("id"));
				jsonObject.put("title", resmap.get("title"));
				jsonObject.put("viewUrl", url);
			}else{
				String url = realPath+"/apiAdmin/ShareAction_getDataDetails.do?type=article&dataId="+String.valueOf((int)resmap.get("id"));
				jsonObject.put("title", "发文者的SHOW");
				jsonObject.put("viewUrl", url);
			}
			jsonObject.put("image", this.getImageUrl((String)resmap.get("image")));
			jsonObject.put("details", (String)resmap.get("details"));
			jsonObject.put("dataId", String.valueOf((int)resmap.get("id")));
		} catch (Exception e) {
			e.printStackTrace();
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("分享url错误", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}


	
	public java.io.File getOriginalCover() {
		return originalCover;
	}

	public void setOriginalCover(java.io.File originalCover) {
		this.originalCover = originalCover;
	}

	public java.io.File getCutCover() {
		return cutCover;
	}

	public void setCutCover(java.io.File cutCover) {
		this.cutCover = cutCover;
	}

	public String getOriginalCoverFileName() {
		return originalCoverFileName;
	}

	public void setOriginalCoverFileName(String originalCoverFileName) {
		this.originalCoverFileName = originalCoverFileName;
	}

	public String getCutCoverFileName() {
		return cutCoverFileName;
	}

	public void setCutCoverFileName(String cutCoverFileName) {
		this.cutCoverFileName = cutCoverFileName;
	}

	
}
