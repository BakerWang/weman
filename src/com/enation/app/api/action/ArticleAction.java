package com.enation.app.api.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.action.admin.form.ArticleModel;
import com.enation.app.api.dto.ArticleComment;
import com.enation.app.api.model.PhoneBanner;
import com.enation.app.api.service.ArticleService;
import com.enation.app.api.service.BannerService;
import com.enation.framework.database.Page;
import com.enation.framework.util.FileUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
public class ArticleAction extends BaseAction{

	private static final long serialVersionUID = 1L;

	@Resource
	private ArticleService articleService;
	@Resource
	private BannerService bannerService;
	
	private java.io.File originalCover;//发文封面原图
	private String originalCoverFileName;//发文封面原图 图片名字
	private java.io.File cutCover;//发文封面截图
	private String cutCoverFileName;//图片名字
	
	private AdminSearchForm adminSearchForm = new AdminSearchForm();
	
	
	public void saveUserView(){
		try{
			int memberId = this.getMemberId(paramObject.getString("accessToken"));
			String articleId = paramObject.has("articleId")?paramObject.getString("articleId"):null;
			Map<String,Object> userView = new HashMap<String,Object>();
			userView.put("status", "1");
			userView.put("viewCount", "1");
			userView.put("create_time", new Date().getTime());
			if(memberId==0){
				String clientId = paramObject.has("clientId")?paramObject.getString("clientId"):null;
				userView.put("viewUserId", Integer.parseInt(clientId));
				userView.put("dataId", Integer.parseInt(articleId));
				userView.put("type", "nologinClickArticle");
			}else{
				userView.put("viewUserId", memberId);
				userView.put("dataId", Integer.parseInt(articleId));
				userView.put("type", "loginClickArticle");
			}
			articleService.saveUserView(userView);
			jsonObject.put("result", "success");
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
			this.logger.error("用户日志失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	/**
	 * 添加用户动作
	 *  type: 1是关注好友 2是收藏主题  3是收藏商品  4是赞(dataId为社交发文id) 5是关注后给好友的印象
	 */
	public void userAction(){
		try{
			int commentType = Integer.parseInt(paramObject.getString("actionType"));
			int memberId = this.getMemberId(paramObject.getString("accessToken"));
			int dataId = Integer.parseInt(paramObject.getString("dataId"));
			Map<String,Object> actionMap = new HashMap<String,Object>();
			actionMap.put("type", commentType);
			actionMap.put("member_id", memberId);
			actionMap.put("data_id", dataId);
			if(paramObject.has("content")){
				actionMap.put("content", paramObject.get("content"));
			}
			if(commentType==4){
				actionMap.put("love_count", paramObject.has("loveCount")?Integer.parseInt((String)paramObject.get("loveCount")):0);
			}else{
				actionMap.put("love_count", 0);
			}
			articleService.saveUserAction(actionMap);
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
			this.logger.error("添加用户动作失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 取消用户动作
	 *  type: 1是关注好友 2是收藏主题  3是收藏商品  4是赞(赞社交发文详情) 5是关注后给好友的印象
	 */
	public void delUserAction(){
		try{
			int commentType = Integer.parseInt(paramObject.getString("actionType"));
			int memberId = this.getMemberId(paramObject.getString("accessToken"));
			int dataId = Integer.parseInt(paramObject.getString("dataId"));
			if(commentType==1||commentType==2||commentType==3){
				Map<String,Object> actionMap = new HashMap<String,Object>();
				actionMap.put("type", commentType);
				actionMap.put("member_id", memberId);
				actionMap.put("data_id", dataId);
				articleService.updateUserActioin(actionMap);
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
			this.logger.error("取消用户动作失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 删除发文评论
	 */
	public void deleteArticleComment(){
		try{
			int commentId = Integer.parseInt(paramObject.getString("commentId"));
			Map<String,Object> commentMap = new HashMap<String,Object>();
			commentMap.put("status", "-1");
			articleService.updateArticleComment(commentId, commentMap);
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
			this.logger.error("删除发文评论失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 删除发文
	 */
	public void deleteArticle(){
		try{
			int commentId = Integer.parseInt(paramObject.getString("articleId"));
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("status", -1);
			articleService.updateArticleStatus(commentId, map);
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
			this.logger.error("删除发文失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 创建发文评论
	 */
	public void createArticleComment(){
		try{
			Map<String,Object> commentMap = new HashMap<String,Object>();
			commentMap.put("article_id", Integer.parseInt(paramObject.getString("articleId")));
			commentMap.put("member_id", this.getMemberId(paramObject.getString("accessToken")));
			String userIds = paramObject.has("userIds")?(String)paramObject.getString("userIds"):null;
			if(userIds!=null){//评论艾特的用户id  用，隔开的
				commentMap.put("userIds", userIds);
				commentMap.put("userNames", paramObject.getString("userNames"));
			}
			commentMap.put("content", paramObject.get("content"));
			commentMap.put("status", paramObject.get("status"));//0是公开 1是私密评论
			commentMap.put("isRead", 0);
			articleService.saveArticleComment(commentMap);
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
			this.logger.error("评论发文失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 获取发文评论列表
	 */
	public void articleCommentList(){
		try{
			Integer pageSize = Integer.parseInt(paramObject.getString("page"));
			Integer articleId = Integer.parseInt(paramObject.getString("articleId"));
			int cmemberId = this.getMemberId(paramObject.getString("accessToken"));
			page.setPageSize(10);
			page.setCurrentPageNo(pageSize);
			Page resPage = articleService.getCommentsByArticleId(articleId,page,cmemberId);
			JSONArray commentJa = new JSONArray();
		    List<ArticleComment> acs = (List<ArticleComment>)resPage.getResult();
		    jsonObject.put("totalPageCount", resPage.getTotalPageCount());
			for(ArticleComment ac:acs){
				JSONObject commentObj = new JSONObject();
				commentObj.put("commentId", String.valueOf(ac.getId()));
				commentObj.put("userIds", String.valueOf(ac.getUserIds()));
				commentObj.put("userNames", String.valueOf(ac.getUserNames()));
				commentObj.put("commentContent", ac.getContent());
				commentObj.put("commentCreatTime", com.enation.framework.util.DateUtil.getShowDate(new Date(ac.getCreate_time())));
				commentObj.put("userId", String.valueOf(ac.getUserId()));
				commentObj.put("userPhoto", this.getImageUrl(ac.getPhoto()));
				commentObj.put("username", String.valueOf(ac.getUsername()));
				if(ac.getStatus()==1){
					commentObj.put("isOpen", "no");
				}else{
					commentObj.put("isOpen", "yes");
				}
				if(cmemberId==ac.getUserId()){
					commentObj.put("canDel", "yes");
				}else{
					commentObj.put("canDel", "no");
				}
				commentJa.add(commentObj);
			}
			jsonObject.put("commentData", commentJa);
			List<PhoneBanner> phoneBanners = bannerService.getCurrentBanners("社交banner");
			if(phoneBanners!=null&&phoneBanners.size()>0){
				JSONArray bannerJarray = new JSONArray();
				for(PhoneBanner pb:phoneBanners){
					JSONObject bannerObj = new JSONObject();
					bannerObj.put("bannerType", String.valueOf(pb.getType()));
					bannerObj.put("bannerId", String.valueOf(pb.getId()));
					bannerObj.put("bannerImage", this.getImageUrl(pb.getImage()));
					bannerObj.put("bannerData", pb.getDetails());
					bannerJarray.add(bannerObj);
				}
				jsonObject.put("bannerList", bannerJarray);
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
			this.logger.error("获取发文列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 发文详情
	 */
	public void articleDetails(){
		try{
			String accessToken = paramObject.has("accessToken")?paramObject.getString("accessToken"):null;
			int cmemberId = this.getMemberId(accessToken);
			int articleId = Integer.parseInt((String)paramObject.get("articleId"));
			if(cmemberId!=0){
				ArticleModel mapObj = articleService.getArtilceDetails(articleId,cmemberId);
				JSONObject obj = new JSONObject();
				if(cmemberId==mapObj.getUserId()){
					obj.put("canDel", "yes");
				}else{
					obj.put("canDel", "no");
				}
				obj.put("userId", String.valueOf(mapObj.getUserId()));
				obj.put("userName", mapObj.getUserName());
				obj.put("userPhoto", this.getImageUrl(mapObj.getUserPhoto()));
				String userAttr = this.getAge(mapObj.getUserAge())+"|"+mapObj.getUserHeight()+"|"+mapObj.getUserWeight();
				obj.put("userAttr", userAttr);
				if(!paramObject.has("accessToken")){//判断是否关注
					obj.put("isFollow", "no");
				}else{
					int articleUserId = (int)mapObj.getUserId();
					if(cmemberId==articleUserId){
						obj.put("isFollow", "yes");
					}else{
						List<Integer> dataIds = articleService.getFollowsByMemberId(cmemberId, 1);
						if(dataIds.contains(articleUserId)){
							obj.put("isFollow", "yes");
						}else{
							obj.put("isFollow", "no");
						}
					}
				}
				obj.put("articleId", String.valueOf((int)mapObj.getId()));
				obj.put("articleImage", this.getImageUrl(mapObj.getImage()));
				obj.put("articleTags", mapObj.getTagStr());
				obj.put("articleCreateTime", com.enation.framework.util.DateUtil.getShowDate(new Date((Long)mapObj.getCreateTime())));
				obj.put("articleContent", mapObj.getContent());
				//obj.put("articleCategoryName", String.valueOf(mapObj.getCategoryName()));
				obj.put("articleCategoryImage", String.valueOf(mapObj.getCategoryImage()));
				obj.put("articleBrandName", String.valueOf(mapObj.getBrandName()));
				//obj.put("articleBrandLogo", String.valueOf(mapObj.getBrandLogo()));
				obj.put("articleLoveCount", String.valueOf((int)mapObj.getLoveCount()));
				obj.put("articleComment", String.valueOf((int)mapObj.getCommentCount()));
				obj.put("articleViewCount", String.valueOf((int)mapObj.getViewCount()));
				jsonObject.put("data", obj);
				if(mapObj.getLoveUserList()!=null&&mapObj.getLoveUserList().size()>0){
					JSONArray loveArray = new JSONArray();
					for(Map<String,Object> map :(List<Map<String,Object>>)mapObj.getLoveUserList()){
						JSONObject lobj = new JSONObject();
						lobj.put("userId", String.valueOf((int)map.get("member_id")));
						lobj.put("userphoto", this.getImageUrl((String)map.get("photo")));
						loveArray.add(lobj);
					}
					jsonObject.put("loveUsers", loveArray);
				}
			}
			
			Map<String,Object> userView = new HashMap<String,Object>();
			userView.put("status", "1");
			userView.put("viewCount", "1");
			userView.put("create_time", new Date().getTime());
			if(cmemberId==0){
				String clientId = paramObject.has("clientId")?paramObject.getString("clientId"):null;
				if(clientId==null){
					jsonObject.put("result", "FAILED");
					jsonObject.put("reason", "没有传clientId");
					return;
				}else{
					userView.put("viewUserId", Integer.parseInt(clientId));
				}
				userView.put("dataId", articleId);
				userView.put("type", "nologinClickArticle");
			}else{
				userView.put("viewUserId", cmemberId);
				userView.put("dataId", articleId);
				userView.put("type", "loginClickArticle");
			}
			articleService.saveUserView(userView);
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
			this.logger.error("获取发文详情失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	
	/**
	 * 发文列表首页
	 */
	public void articleList(){
		try{
			page.setCurrentPageNo(Integer.parseInt((String)paramObject.get("page")));
			String accessToken = paramObject.has("accessToken")?(String)paramObject.get("accessToken"):null;
			if(accessToken!=null){
				int cmemberId = this.getMemberId(paramObject.getString("accessToken"));
				adminSearchForm.setUserId(cmemberId);
				String type = paramObject.has("type")?(String)paramObject.get("type"):null;
				if(type!=null&&"friends".equals(type)){
					adminSearchForm.setStatus(110);//登录后的情况   关注
				}else{
					adminSearchForm.setStatus(120);//登录后的情况   全部  
				}
			}else{
				adminSearchForm.setStatus(100);//未登录的情况下  
			}
			page.setPageSize(10);
			Page resPage = articleService.getArticleList(adminSearchForm, page);
			JSONArray jarray = new JSONArray();
			for(ArticleModel mapObj:(List<ArticleModel>)resPage.getResult()){
				JSONObject obj = new JSONObject();
				obj.put("userId", String.valueOf(mapObj.getUserId()));
				obj.put("userName", mapObj.getUserName());
				obj.put("userPhoto", this.getImageUrl(mapObj.getUserPhoto()));
				String userAttr = this.getAge(mapObj.getUserAge())+"|"+mapObj.getUserHeight()+"|"+mapObj.getUserWeight();
				obj.put("userAttr", userAttr);
				if(!paramObject.has("accessToken")){//判断是否关注
					obj.put("isFollow", "no");
				}else{
					int cmemberId = this.getMemberId(paramObject.getString("accessToken"));
					int articleUserId = (int)mapObj.getUserId();
					if(cmemberId==articleUserId){
						obj.put("isFollow", "yes");
					}else{
						String status = mapObj.getIsFriend();
						if(status!=null){
							obj.put("isFollow", "yes");
						}else{
							obj.put("isFollow", "no");
						}
					}
				}
				obj.put("articleId", String.valueOf((int)mapObj.getId()));
				obj.put("articleImage", this.getImageUrl(mapObj.getImage()));
				obj.put("articleTags", mapObj.getTagStr());
				obj.put("articleCreateTime", com.enation.framework.util.DateUtil.getShowDate(new Date((Long)mapObj.getCreateTime())));
				obj.put("articleContent", mapObj.getContent());
				//obj.put("articleCategoryName", String.valueOf(mapObj.getCategoryName()));
				obj.put("articleCategoryImage", String.valueOf(mapObj.getCategoryImage()));
				obj.put("articleBrandName", String.valueOf(mapObj.getBrandName()));
				//obj.put("articleBrandLogo", String.valueOf(mapObj.getBrandLogo()));
				obj.put("articleLoveCount", String.valueOf((int)mapObj.getLoveCount()));
				obj.put("articleComment", String.valueOf((int)mapObj.getCommentCount()));
				obj.put("articleViewCount", String.valueOf((int)mapObj.getViewCount()));
				jarray.add(obj);
			}
			if(page.getCurrentPageNo()==1){
				List<PhoneBanner> phoneBanners = bannerService.getCurrentBanners("社交banner");
				if(phoneBanners!=null&&phoneBanners.size()>0){
					JSONArray bannerJarray = new JSONArray();
					for(PhoneBanner pb:phoneBanners){
						JSONObject bannerObj = new JSONObject();
						bannerObj.put("bannerType", pb.getType());
						bannerObj.put("bannerId", String.valueOf(pb.getId()));
						bannerObj.put("bannerImage", this.getImageUrl(pb.getImage()));
						bannerObj.put("bannerData", pb.getDetails());
						if(pb.getThemeContentStyle()!=null&&!pb.getThemeContentStyle().equals("0")){
							bannerObj.put("contentStyle", pb.getThemeContentStyle());
						}
						bannerJarray.add(bannerObj);
					}
					jsonObject.put("bannerList", bannerJarray);
				}
			}
			jsonObject.put("totalPageCount", resPage.getTotalPageCount());
			jsonObject.put("data", jarray);
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
			this.logger.error("获取发文列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	
	/**
	 * 保存社交发文
	 */
	public void saveArticle(){
		try{
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("content", paramObject.getString("content"));
			map.put("good_cat_id", paramObject.getString("good_cat_id"));
			map.put("member_id", this.getMemberId(paramObject.getString("accessToken")));
			map.put("good_type_name", paramObject.getString("good_type_name"));
			map.put("good_cat_name", paramObject.getString("good_cat_name"));
			if(cutCover!=null){
				//封面的原图
				if (FileUtil.isAllowUp(cutCoverFileName)) {
					String saveName = uploadImage(cutCover,cutCoverFileName, "articleImage");
					saveName = resizeImage(saveName,500);
					map.put("image", saveName);
				}
				if (FileUtil.isAllowUp(originalCoverFileName)) {
					//String saveName = uploadImage(originalCover,originalCoverFileName, "articleImage");
					//map.put("orImage", saveName);
				}
			}else{
				map.put("image", "attachment/articleImage/articleNullImage.png");
				map.put("orImage", "attachment/articleImage/articleOrNullImage.png");
			}
			String tagStr = paramObject.getString("tagStr");
			if(tagStr!=null&&!"".equals(tagStr)){
				map.put("tagStr", tagStr);
			}
			articleService.saveArticle(map);
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
			this.logger.error("发文失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}

	/**
	 * 发文之前访问类别和品牌接口
	 */
	public void beginArticleCat(){
		try{
			List<Map<String,Object>> catbrands = articleService.beginArticle(1);
			JSONArray dataArray = new JSONArray();
			List<Integer> data = new ArrayList<Integer>();
			for(Map<String,Object> map:catbrands){
				int parentId = (int)map.get("cparentid");
				int pcid = (int)map.get("cid");
				if(parentId==0&&!data.contains(pcid)){
					data.add(pcid);
					JSONObject objJson = new JSONObject();
					objJson.put("catId", String.valueOf(pcid));
					objJson.put("catName", (String)map.get("cname"));
					JSONArray objArray = new JSONArray();
					int cid = (int)map.get("cid");
					List<Integer> cdata = new ArrayList<Integer>();
					for(Map<String,Object> cmap:catbrands){
						int cparentId = (int)cmap.get("cparentid");
						int objcatid = (int)cmap.get("cid");
						if(cparentId==cid&&!cdata.contains(objcatid)){
							cdata.add(objcatid);
							JSONObject obj = new JSONObject();
							obj.put("catId", String.valueOf(objcatid));
							obj.put("catName", (String)cmap.get("cname"));
							obj.put("catImage", this.getImageUrl((String)cmap.get("cimage")));
							JSONArray brandArray = new JSONArray();
							for(Map<String,Object> bmap:catbrands){
								int brandcatid = (int)bmap.get("cid");
								if(brandcatid==objcatid){
									JSONObject brandObj = new JSONObject();
									brandObj.put("id", String.valueOf((int)bmap.get("bid")));
									brandObj.put("image", this.getImageUrl((String)bmap.get("blogo")));
									brandObj.put("name", (String)bmap.get("bname"));
									brandArray.add(brandObj);
								}
							}
							obj.put("brandData", brandArray);
							objArray.add(obj);
						}
					}
					objJson.put("children", objArray);
					dataArray.add(objJson);
				}
			}
			jsonObject.put("data", dataArray);
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
			this.logger.error("获取发文前类别和品牌失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 搜索品牌和搜索发文标签 
	 * 参数type：1是搜索品牌    2是搜索标签
	 */
	public void beginArticleSearch(){
		try{
			String type = (paramObject.has("type")?(String)paramObject.get("type"):"1");
			JSONArray ja = new JSONArray();
			if("1".equals(type)){//1是搜索品牌    2是搜索标签
				String searchStr = paramObject.getString("searchStr");
				List<Map<String,String>> brands = articleService.searchBrand(searchStr);
				for(Map<String,String> brand:brands){
						JSONObject obj = new JSONObject();
						obj.put("id", brand.get("id"));
						obj.put("content", brand.get("content"));
						if(brand.get("logo")==null||"".equals(brand.get("logo"))){
							obj.put("image", this.getImageUrl("brandDefault.png"));
						}else{
							obj.put("image", this.getImageUrl(brand.get("logo")));
						}
						ja.add(obj);
				}
			}else{
				String searchStr = paramObject.getString("searchStr");
				List<String> tags = articleService.searchTag(searchStr);
				for(String str:tags){
					JSONObject obj = new JSONObject();
					obj.put("content", str);
					ja.add(obj);
				}
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
			this.logger.error("搜索发文标签和发文品牌失败",e);
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
