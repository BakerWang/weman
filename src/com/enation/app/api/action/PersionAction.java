package com.enation.app.api.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.service.PersionService;
import com.enation.app.api.service.ProductService;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.service.impl.MemberManager;
import com.enation.framework.database.Page;
import com.enation.framework.util.FileUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


@Scope("prototype")
public class PersionAction extends BaseAction{

	private static final long serialVersionUID = -455457747547259900L;
	
	@Resource
	private MemberManager memberService;
	@Resource
	private PersionService persionService;
	@Resource
	private ProductService productService;
	
	/**
	 * 获取通知4     被赞列表3   被评论列表2   被加关注列表1
	 */
	public void fecthMessageList(){
		try {
			int member_id = this.getMemberId(paramObject.getString("accessToken"));
			int type = Integer.parseInt(paramObject.getString("type"));
			int pageNo = Integer.parseInt(paramObject.getString("page"));
			page.setCurrentPageNo(pageNo);
			page.setPageSize(10);
			Page resPage = persionService.fecthMessageList(type, member_id,page);//我里面的总消息说（赞数，评论数，关注数，系统通知数）
			JSONArray ja = new JSONArray();
			for(Map<String,Object> map:(List<Map<String,Object>>)resPage.getResult()){
				JSONObject obj = new JSONObject();
				if(type==1){//被关注列表1
					obj.put("userId", String.valueOf((int)map.get("memberId")));
					obj.put("userphoto", this.getImageUrl((String)map.get("photo")));
					obj.put("username", (String)map.get("username"));
					obj.put("createTime", com.enation.framework.util.DateUtil.getShowDate(new Date((Long)map.get("createTime"))));
					String content = (String)map.get("content");
					if(content!=null&&!"".equals(content)){
						obj.put("content", "关注了我     他觉得我很"+content+"!");
					}else{
						obj.put("content", "关注了我");
					}
				}else if(type==2){//评论列表2
					obj.put("userId", String.valueOf((int)map.get("memberId")));
					obj.put("userphoto", this.getImageUrl((String)map.get("photo")));
					obj.put("username", (String)map.get("username"));
					obj.put("createTime", com.enation.framework.util.DateUtil.getShowDate(new Date((Long)map.get("createTime"))));
					obj.put("image", this.getImageUrl((String)map.get("articleImage")));
					obj.put("articleId", String.valueOf((int)map.get("articleId")));
					obj.put("content", "评论了我的SHOW");
				}else if(type==3){//被赞列表3
					obj.put("userId", String.valueOf((int)map.get("memberId")));
					obj.put("userphoto", this.getImageUrl((String)map.get("photo")));
					obj.put("username", (String)map.get("username"));
					obj.put("articleId", String.valueOf((int)map.get("articleId")));
					obj.put("articleImage", this.getImageUrl((String)map.get("articleImage")));
					obj.put("createTime", com.enation.framework.util.DateUtil.getShowDate(new Date((Long)map.get("createTime"))));
					obj.put("loveCount", String.valueOf((int)map.get("loveCount")));
					obj.put("content", "已赞了我的SHOW");
				}else if(type==4){//通知4
					String xttype = (String)map.get("type");
					String content =(String)map.get("content");
					String image = this.getImageUrl("123.png");
					String date = com.enation.framework.util.DateUtil.getShowDate(new Date((Long)map.get("createTime")));
					obj.put("type", xttype);
					obj.put("content", content);
					obj.put("image", image);
					obj.put("createTime", date);
					if("theme".equals(xttype)){
						obj.put("themeId", map.get("dataId"));
						obj.put("themeTitle", (String)map.get("ttitle"));
						obj.put("themeImage", this.getImageUrl((String)map.get("timage")));
						obj.put("themeDetails", (String)map.get("tdetails"));
					}else if("product".equals(xttype)){
						obj.put("productId", map.get("dataId"));
						obj.put("productName", (String)map.get("pname"));
						obj.put("productImage", this.getImageUrl((String)map.get("pimage")));
						obj.put("productPrice", String.valueOf((double)map.get("pprice")));
						obj.put("productMKPrice", String.valueOf((double)map.get("pmkprice")));
						obj.put("productBrief", (String)map.get("pbrief"));
					}else if("web".equals(xttype)){
						obj.put("url", map.get("dataId"));
					}
				}
				ja.add(obj);
			}
			jsonObject.put("data", ja);
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
			this.logger.error("获取个人主页失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	/**
	 * 获取消息的数量  （赞数，评论数，关注数，系统通知数）
	 */
	public void fecthMessageCount(){
		try {
			int member_id = this.getMemberId(paramObject.getString("accessToken"));
			persionService.fecthMessageCount(jsonObject, member_id);//我里面的总消息说（赞数，评论数，关注数，系统通知数）
			Map<String,String> map = new HashMap<String,String>();
			map.put("recommendStatus", "1");
			Page page =	productService.getThemeProducts(1, 5 , map);
			List<ThemeProduct> tps = (List<ThemeProduct>) page.getResult();
			JSONArray jsonArray = new JSONArray();
			for(ThemeProduct tp:tps){
				JSONObject theme = new JSONObject();
				theme.put("themeId", String.valueOf(tp.getTheme().getId()));
				theme.put("themeImage", this.getImageUrl(tp.getTheme().getImage()));
				theme.put("themeTitle", tp.getTheme().getTitle());
				theme.put("themeDetails", tp.getTheme().getDetails());
				theme.put("tags", tp.getTheme().getTagsImage());
				jsonArray.add(theme);
			}
			jsonObject.put("themeData", jsonArray);
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
			this.logger.error("获取个人主页失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	/**
	 * 获取个人主页和个人信息  自己的和别人的（有没有userId）
	 */
	public void getPersionDetails(){
		try {
			int member_id = this.getMemberId(paramObject.getString("accessToken"));
			Member member = null;
			String userId = paramObject.has("userId")?(String)paramObject.get("userId"):null;
			if(userId!=null){
				if(persionService.getIsFriend(member_id,userId)){
					jsonObject.put("isFriend", "yes");
				}else{
					jsonObject.put("isFriend", "no");
				}
				member_id = Integer.parseInt(userId);
				member = memberService.get(member_id);
			}else{
				member = memberService.get(member_id);
			}
			jsonObject.put("background", this.getImageUrl("attachment/allDefaultImage/persionDefault.png"));
			jsonObject.put("userId", String.valueOf(member.getMember_id()));
			jsonObject.put("photo", this.getImageUrl(member.getFace()));
			jsonObject.put("username", member.getUname());
			jsonObject.put("sex", String.valueOf(member.getSex()));
			jsonObject.put("birthday", this.getAge(member.getBirthday()));
			jsonObject.put("height", String.valueOf(member.getHeight()));
			jsonObject.put("weight", String.valueOf(member.getWeight()));
			jsonObject.put("birthdayNumber", String.valueOf(member.getBirthday()));
			jsonObject.put("address", member.getAddress());
			jsonObject.put("signature", member.getRemark());
			persionService.getUserActionCount(jsonObject, member_id);//添加赞数，关注数，粉丝数，印象数 ，show数，收藏主题数，收藏商品数
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
			this.logger.error("获取个人主页失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	private java.io.File cutCover;//头像截图
	private String cutCoverFileName;//头像截图   文件的名字
	
	/**
	 * 更改用户信息
	 */
	public void updatePersionDetails(){
		try {
			int member_id = this.getMemberId(paramObject.getString("accessToken"));
			Member member = memberService.get(member_id);
			if(paramObject.has("username")){
				String username = paramObject.getString("username");
				member.setUname(username);
			}
			if(paramObject.has("sex")){
				int sex = Integer.parseInt(paramObject.getString("sex"));
				member.setSex(sex);
			}
			if(paramObject.has("birthday")){
				long birthday = new SimpleDateFormat("yyyy-MM-dd").parse(paramObject.getString("birthday")).getTime();
				member.setBirthday(birthday);
			}
			if(paramObject.has("height")){
				int height = Integer.parseInt(paramObject.getString("height"));
				member.setHeight(height);
			}
			if(paramObject.has("weight")){
				int weight = Integer.parseInt(paramObject.getString("weight"));
				member.setWeight(weight);
			}
			if(paramObject.has("address")){
				String address = paramObject.getString("address");
				member.setAddress(address);
			}
			if(paramObject.has("signature")){
				String signature = paramObject.getString("signature");
				member.setRemark(signature);
			}
			if(cutCover!=null){
				//封面的原图
				if (FileUtil.isAllowUp(cutCoverFileName)) {
					String saveName = uploadImage(cutCover, cutCoverFileName, "photoImage");
					member.setFace(saveName);
				}
			}
			persionService.updatePersionDetails(member);
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
			this.logger.error("更新个人主页失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 意见反馈
	 */
	public void sendFAQ(){
		try{
			String content = paramObject.getString("content");
			String image1 = paramObject.has("image1")?paramObject.getString("image1"):null;
			String image2 = paramObject.has("image2")?paramObject.getString("image2"):null;
			String image3 = paramObject.has("image3")?paramObject.getString("image3"):null;
			String image4 = paramObject.has("image4")?paramObject.getString("image4"):null;
			int memberId = this.getMemberId("");
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("content", content);
			map.put("member_id", memberId);
			map.put("status", 1);
			map.put("create_time", new Date().getTime());
			if(image1!=null){
				String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+image1;
				if(new File(imagePath).exists()){
					File oldFile = new File(imagePath);
					String newFilePath = imagePath.replace("temp", "faqImage");
					FileUtil.createFile(oldFile, newFilePath);
					FileUtil.delete(imagePath);
					image1 = image1.replace("temp", "faqImage");
					map.put("image1", image1);
				}
			}
			if(image2!=null){
				String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+image2;
				if(new File(imagePath).exists()){
					File oldFile = new File(imagePath);
					String newFilePath = imagePath.replace("temp", "faqImage");
					FileUtil.createFile(oldFile, newFilePath);
					FileUtil.delete(imagePath);
					image2 = image2.replace("temp", "faqImage");
					map.put("image2", image2);
				}
			}
			if(image3!=null){
				String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+image3;
				if(new File(imagePath).exists()){
					File oldFile = new File(imagePath);
					String newFilePath = imagePath.replace("temp", "faqImage");
					FileUtil.createFile(oldFile, newFilePath);
					FileUtil.delete(imagePath);
					image3 = image3.replace("temp", "faqImage");
					map.put("image3", image3);
				}
			}
			if(image4!=null){
				String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+image4;
				if(new File(imagePath).exists()){
					File oldFile = new File(imagePath);
					String newFilePath = imagePath.replace("temp", "faqImage");
					FileUtil.createFile(oldFile, newFilePath);
					FileUtil.delete(imagePath);
					image4 = image4.replace("temp", "faqImage");
					map.put("image4", image4);
				}
			}
			persionService.saveFAQ(map);
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
			this.logger.error("意见反馈错误", e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * type 1是我的关注 2是我的粉丝
	 * 获取我的关注或者我的粉丝
	 */
	public void myFriendList(){
		try {
			int userId = Integer.parseInt(paramObject.getString("userId"));
			int type = Integer.parseInt(paramObject.getString("type"));//1是我的关注   2是我的粉丝
			int pageNo = Integer.parseInt(paramObject.getString("page"));
			int member_id = this.getMemberId(paramObject.getString("accessToken"));
			page.setCurrentPageNo(pageNo);
			page.setPageSize(10);
			Page respage = persionService.myFriendList(member_id,userId,type,page);
			List<Map<String,Object>> resList= (List<Map<String, Object>>) respage.getResult();
			JSONArray ja = new JSONArray();
			for(Map<String,Object> map:resList){
				JSONObject obj = new JSONObject();
				obj.put("userId", String.valueOf(map.get("userId")));
				obj.put("userPhoto", this.getImageUrl((String)map.get("photo")));
				obj.put("username", map.get("username"));
				if(map.get("isFan")!=null){//0是没有相互关注   1是相互关注
					obj.put("isTogetherFan", map.get("isFan"));
				}else{
					if((int)map.get("userId")==member_id){
						obj.put("isTogetherFan", "1");
					}else{
						obj.put("isTogetherFan", "0");
					}
				}
				obj.put("userFanCount", String.valueOf(map.get("fanCount")));
				obj.put("userArticle", String.valueOf(map.get("articleCount")));
				ja.add(obj);
			}
			jsonObject.put("totalPageCount", respage.getTotalPageCount());
			jsonObject.put("data", ja);
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
			this.logger.error("获取我的关注和我的粉丝失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * type 1.发文列表  2是收藏主题列表  3是收藏商品列表
	 */
	public void fetchActionList(){
		try {
			int member_id = this.getMemberId(paramObject.getString("accessToken"));
			int type = Integer.parseInt(paramObject.getString("type"));//type 1.发文列表  2是收藏主题列表  3是收藏商品列表
			int pageNo = Integer.parseInt(paramObject.getString("page"));
			String userId = paramObject.has("userId")?(String)paramObject.get("userId"):null;
			if(userId!=null){
				member_id = Integer.parseInt(userId);
			}
			page.setCurrentPageNo(pageNo);
			page.setPageSize(10);
			Page respage = persionService.fetchUserActionList(member_id,type,page);
			List<Map<String,Object>> resList= (List<Map<String, Object>>) respage.getResult();
			JSONArray ja = new JSONArray();
			for(Map<String,Object> map:resList){
				JSONObject obj = new JSONObject();
				if(type==1){
					obj.put("articleId", String.valueOf(map.get("articleId")));
					obj.put("articleImage", this.getImageUrl((String)map.get("articleImage")));
				}else if(type==2){
					obj.put("themeId", String.valueOf(map.get("themeId")));
					obj.put("themeImage", this.getImageUrl((String)map.get("themeImage")));
					obj.put("themeTitle", map.get("themeTitle"));
					obj.put("themeDetails", map.get("themeDetails"));
				}else if(type==3){
					obj.put("pid", String.valueOf(map.get("goods_id")));
					obj.put("pname", map.get("name"));
					obj.put("pimage", this.getImageUrl((String)map.get("original")));
					obj.put("pprice", String.valueOf(map.get("price")));
					obj.put("pmktprice", String.valueOf(map.get("mktprice")));
					obj.put("ptitle", map.get("brief"));
					obj.put("purl", String.valueOf(map.get("url")));
					if(map.get("isShowMKPrice")!=null&&(int)map.get("isShowMKPrice")==-1){
						obj.put("isShowMKPrice", "no");
					}else{
						obj.put("isShowMKPrice", "yes");
					}
				}
				ja.add(obj);
			}
			jsonObject.put("totalPageCount", respage.getTotalPageCount());
			jsonObject.put("data", ja);
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
			this.logger.error("获取show,收藏商品,收藏主题失败",e);
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
			int member_id = this.getMemberId(paramObject.getString("accessToken"));
			String newPassword = paramObject.getString("newPassword");
			Member member = memberService.get(member_id);
			if(member==null){
				jsonObject.put("result", "FAILED");
				jsonObject.put("reason", "系统错误！");
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
	
	
	
	public java.io.File getCutCover() {
		return cutCover;
	}
	public void setCutCover(java.io.File cutCover) {
		this.cutCover = cutCover;
	}
	public String getCutCoverFileName() {
		return cutCoverFileName;
	}
	public void setCutCoverFileName(String cutCoverFileName) {
		this.cutCoverFileName = cutCoverFileName;
	}

}
