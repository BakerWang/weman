package com.enation.app.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.api.action.admin.form.ArticleModel;
import com.enation.app.api.action.admin.form.ArticleTagModel;
import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.dto.ArticleComment;
import com.enation.app.api.dto.BeginArticleCat;
import com.enation.app.api.dto.BeginArticleType;
import com.enation.app.api.model.DeviceToken;
import com.enation.app.api.model.StaticProperty;
import com.enation.app.api.pushMessage.PushMessage;
import com.enation.app.api.service.ArticleService;
import com.enation.app.shop.core.model.Article;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

import edu.emory.mathcs.backport.java.util.Arrays;

@Service
public class ArticleServiceImpl extends BaseSupport implements ArticleService{

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveArticle(Map<String, Object> dtoMap) throws Exception {
		dtoMap.put("create_time", new Date().getTime());
		dtoMap.put("view_count", 0);
		dtoMap.put("love_count", 0);
		dtoMap.put("comment_count", 0);
		dtoMap.put("status", 1);//未通过审核
		String good_type_name = (String) dtoMap.get("good_type_name");
		String[] good_type_names = good_type_name.split(",");
		String good_type_id = "";
		for(int i=0;i<good_type_names.length;i++){
			String typeSql = "select brand_id from es_brand eb where eb.name = ?";
			List<Map<String,Object>> blist = this.daoSupport.queryForList(typeSql,good_type_names[i].trim());
			if(blist==null||blist.size()==0){
				Map<String,Object> brandMap = new HashMap<String,Object>();
				brandMap.put("name", good_type_names[i].trim());
				brandMap.put("logo", "brandNullImage.png");
				brandMap.put("disabled", -1);//-1是用户创建的
				this.daoSupport.insert("es_brand", brandMap);
				good_type_id = good_type_id+this.daoSupport.getLastId("wh_api_tag")+",";
			}else{
				good_type_id = good_type_id+(int)blist.get(0).get("brand_id")+",";
			}
		}
		if(!good_type_id.equals("")&&good_type_id.length()>0){
			good_type_id = good_type_id.substring(0,good_type_id.length()-1);
		}
		dtoMap.put("good_type_id", good_type_id);
		this.daoSupport.insert("wh_api_article", dtoMap);
		int articleId = this.daoSupport.getLastId("wh_api_article");
		if(dtoMap.containsKey("tagStr")){
			String tagStr = (String) dtoMap.get("tagStr");
			String[] tagArray = tagStr.split(",");
			for(String str:tagArray){
				Map<String,Object> atagMap = new HashMap<String,Object>();
				String sql = "select id from wh_api_tag where content = ?";
				List<Map<String,Object>> rlist = this.daoSupport.queryForList(sql, str);
				int tagId = 0;
				if(rlist==null||rlist.size()==0){
					Map<String,Object> tagMap = new HashMap<String,Object>();
					tagMap.put("content", str);
					tagMap.put("status", 1);
					tagMap.put("create_time", new Date().getTime());
					tagMap.put("member_id", dtoMap.get("member_id"));
					this.daoSupport.insert("wh_api_tag", tagMap);
					tagId = this.daoSupport.getLastId("wh_api_tag");
				}else{
					tagId = (int)rlist.get(0).get("id");
				}
				atagMap.put("tag_id", tagId);
				atagMap.put("article_id", articleId);
				atagMap.put("create_time", new Date().getTime());
				this.baseDaoSupport.insert("wh_api_article_tag", atagMap);
			}
		}
	}


	@Override
	public List<Map<String,Object>> beginArticle(int list_show) throws Exception {
		String tagSql = "select gc.cat_id as cid,gc.image as cimage,gc.name as cname,gc.parent_id as cparentid,bb.brand_id as bid,bb.name as bname,bb.logo as blogo "
				+ "from es_goods_cat gc "
				+ "LEFT JOIN es_type_brand tb on tb.type_id = gc.type_id "
				+ "left JOIN es_brand bb on bb.brand_id = tb.brand_id "
				+ "where 1=1 ";
		if(list_show!=0){
			tagSql = tagSql + " and gc.list_show = " + list_show;
		}
		tagSql = tagSql + " order by gc.cat_order";
		List<Map<String,Object>> catbrands = this.daoSupport.queryForList(tagSql);
		return catbrands;
	}

	@Override
	public List<String> searchTag(String searchStr) throws Exception {
		String sql = "select * from wh_api_tag at where 1=1 and status = 1 ";
		if(searchStr!=null&&!"".equals(searchStr)){
			sql = sql+" and at.content like '%"+searchStr+"%' ";
		}
		sql = sql +" order by id desc ";
		Page page = this.daoSupport.queryForPage(sql, 1, 5);
		List<Map> tags = (List<Map>) page.getResult();
		List<String> tagArray = new ArrayList<String>();
		for(Map map:tags){
			String content = (String)map.get("content");
			tagArray.add(content);
		}
		return tagArray;
	}
	
	@Override
	public List<Map<String, String>> searchBrand(String searchStr) throws Exception {
		String sql = "select * from es_brand eb where 1=1 ";
		if(searchStr!=null&&!"".equals(searchStr)){
			sql = sql+" and eb.name like '%"+searchStr+"%' ";
		}
		sql = sql +" order by brand_id desc ";
		Page page = this.daoSupport.queryForPage(sql, 1, 5);
		List<Map<String,Object>> tags = (List<Map<String,Object>>) page.getResult();
		List<Map<String, String>> brands = new ArrayList<Map<String,String>>();
		for(Map<String,Object> map:tags){
			Map<String,String> brand = new HashMap<String,String>();
			int bid = (int)map.get("brand_id");
			brand.put("id", String.valueOf(bid));
			brand.put("content", (String)map.get("name"));
			brand.put("logo", (String)map.get("logo"));
			brands.add(brand);
		}
		return brands;
	}
	
	
	
	
	
	
	
	/** 后台部分  */
	
	@Override
	public Page getArticleList(AdminSearchForm adminSearchForm, Page page) throws Exception {
		String sql = "aselect waa.id as id,waa.tagStr as tagStr,waa.content as content,waa.good_cat_name as categoryName,waa.good_cat_name as brandName,waa.good_type_id as brandIds,waa.good_cat_id as categoryIds,em.uname as userName,em.face as userPhoto,em.member_id as userId,em.birthday as userAge,em.height as userHeight,em.weight as userWeight, waa.status as status, "
				+ " waa.image as image,waa.orImage as orImage,waa.love_count as loveCount,waa.comment_count as commentCount,waa.view_count as viewCount,waa.create_time as createTime ";
				
		if(adminSearchForm!=null&&(adminSearchForm.getStatus()==110||adminSearchForm.getStatus()==120)){
			sql = sql + ",(select '1' from wh_api_action waction  where waction.type = 1 and waction.member_id = "+adminSearchForm.getUserId()+" and waction.data_id = waa.member_id and waction.status != -1) as isFriend";
		}
		sql = sql + " from wh_api_article waa "
				  + " left join es_member em on em.member_id = waa.member_id "
				  + " where 1=1 ";
		if(adminSearchForm!=null){
			if(adminSearchForm.getTitle()!=null&&!"".equals(adminSearchForm.getTitle())){
				String title = java.net.URLDecoder.decode(adminSearchForm.getTitle(), "UTF-8");
				sql = sql+" and waa.content like '%"+title+"%'";
			}
			if(adminSearchForm.getStatus()==100){// 未登录的情况      除陌生人未通过审核的   
				sql = sql+" and waa.status = 1 ";
			}else if(adminSearchForm.getStatus()==110){//登录的情况   只看关注   (关注人的全部) 
				sql = sql+" and waa.status != -1 and (waa.member_id ="+adminSearchForm.getUserId()+" or waa.member_id in (select waac.data_id from wh_api_action waac where waac.type = 1 and waac.status != -1 and waac.member_id = "+adminSearchForm.getUserId()+") )";
			}else if(adminSearchForm.getStatus()==120){//登录的情况   全部    (除陌生人未通过审核的 )  1是通过的  0 是未通过的  -1 删除的 
				//看到关注的通过和未通过
				sql = sql+" and ((waa.status != -1 and "
							 + "(waa.member_id ="+adminSearchForm.getUserId()+" "
							 		+ "or waa.member_id in (select waac.data_id from wh_api_action waac where waac.type = 1 and waac.status != -1 and waac.member_id = "+adminSearchForm.getUserId()+")"
			 				 + " ))"
						+ " or (waa.status = 1 and waa.member_id not in (select waac.data_id from wh_api_action waac where waac.type = 1 and waac.status != -1 and waac.member_id = "+adminSearchForm.getUserId()+") ))";
				//看到未关注的通过
			}else if(adminSearchForm.getStatus()!=111){//if(adminSearchForm.getStatus()!=0)
				sql = sql+" and waa.status = "+adminSearchForm.getStatus();
			}
		}
		sql = sql+" order by id desc";
		Page pg = this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(),  ArticleModel.class);
		String categoryIds = "";
		for(ArticleModel am:(List<ArticleModel>)pg.getResult()){
			categoryIds = categoryIds+am.getCategoryIds()+",";
		}
		if(categoryIds!=null&&!categoryIds.equals("")){
			categoryIds = categoryIds.substring(0,categoryIds.length()-1);
			String bsql = "select egc.cat_id as cid ,egc.name as cname,egc.image as cimage from es_goods_cat egc where egc.cat_id in ("+categoryIds+")";
			List<Map<Object,Object>> catList = this.baseDaoSupport.queryForList(bsql);
			Map<Integer,String> catIdLogoMap = new HashMap<Integer,String>();
			for(Map<Object,Object> mm:catList){
				int catId = (int)(mm.get("cid"));
				String catLogo = "";
				if(mm.get("cimage")!=null){
					catLogo = String.valueOf(mm.get("cimage"));
				}
				catIdLogoMap.put(catId, catLogo);
			}
			for(ArticleModel am:(List<ArticleModel>)pg.getResult()){
				String[] catId = am.getCategoryIds().split(",");
				String catImages = "";
				for(String b:catId){
					if(catIdLogoMap.containsKey(Integer.parseInt(b))){
						String image = StaticProperty.currentUrl+catIdLogoMap.get(Integer.parseInt(b));
						if(catIdLogoMap.get(Integer.parseInt(b))==null||"".equals(catIdLogoMap.get(Integer.parseInt(b)))){
							image = StaticProperty.currentUrl+"attachment/allDefaultImage/defaultCateImage.png";
						}
						catImages = catImages+image+",";
					}
				}
				if(!catImages.equals("")){
					catImages = catImages.substring(0,catImages.length()-1);
					am.setCategoryImage(catImages);
				}
			}
		}
		return pg;
	}

	@Override
	public void updateArticleStatus(int articleId, Map<String,Object> map) throws Exception {
		this.daoSupport.update("wh_api_article", map, "id ="+articleId);
	}

	
	@Override
	public void saveArticleTag(Map<String, Object> tagMap) throws Exception {
		tagMap.put("create_time", new Date().getTime());
		tagMap.put("status","1");
		this.daoSupport.insert("wh_api_tag", tagMap);
	}

	@Override
	public Page getArticleTagList(AdminSearchForm adminSearchForm, Page page) throws Exception {
		String sql = "select wat.id as id,wat.type as type,wat.content as content,wat.create_time as createTime,wat.status as status,em.uname as username,"
				+ " (select count(*) from wh_api_article_tag waat where wat.id =waat.tag_id) as articleCount "
				+ " from wh_api_tag wat "
				+ " left join es_member em on em.member_id = wat.member_id"
				+ " where 1=1";
		if(adminSearchForm!=null){
			if(adminSearchForm.getTitle()!=null&&!"".equals(adminSearchForm.getTitle())){
				sql = sql+" and waa.content like '%"+adminSearchForm.getTitle()+"%'";
			}
			if(adminSearchForm.getType()!=null&&!"".equals(adminSearchForm.getType())){
				sql = sql+" and waa.status = "+adminSearchForm.getType();
			}
		}
		sql = sql+" order by id desc";
		Page pg =  this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), ArticleTagModel.class);
		return pg;
	}

	@Override
	public void updateArticleTagStatus(int articleTagId, int status) throws Exception {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("status", status);
		this.daoSupport.update("wh_api_tag", map, "id ="+articleTagId);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	@Override
	public void saveArticleComment(Map<String, Object> commentMap) throws Exception {
		commentMap.put("create_time", new Date().getTime());
		this.daoSupport.insert("wh_api_comment", commentMap);
		int articleId = (int)commentMap.get("article_id");
		String article = "select id , comment_count as commentCount from wh_api_article waa where waa.id = ?";
		ArticleModel am = (ArticleModel) this.daoSupport.queryForObject(article, ArticleModel.class, articleId);
		if(am!=null){
			Map<String,Object> amap = new HashMap<String,Object>();
			amap.put("comment_count", am.getCommentCount()+1);
			this.daoSupport.update("wh_api_article", amap, " id = "+articleId);
			//--------  推送  发文评论    -----------
			Map<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("type", "comment");
			if(commentMap.get("userIds")==null){
				String usersql = "select ead.* from es_api_devicetoken ead "
						+ " left join wh_api_article waa on waa.member_id = ead.member_id "
						+ " where waa.id = ?";
				DeviceToken userMessage = (DeviceToken) this.baseDaoSupport.queryForObject(usersql, DeviceToken.class, am.getId());
				int member_id = (int)commentMap.get("member_id");
				if(userMessage!=null&&userMessage.getMember_id()!=member_id){
					String msql = "select em.uname as username from es_member em where em.member_id = ?";
					List<Map<String,Object>> map = this.baseDaoSupport.queryForList(msql, member_id);
					if(map!=null&&map.size()>0){
						String pushMessage = map.get(0).get("username")+ "评论我的SHOW";
						PushMessage.pushSingleUser(pushMessage, dataMap, userMessage);
					}
				}
			}else{
				String[] userIds = ((String)commentMap.get("userIds")).split(",");
				for(String userid:userIds){
					String usersql = "select ead.* from es_api_devicetoken ead "
							+ " where ead.member_id = ?";
					DeviceToken userMessage = (DeviceToken) this.baseDaoSupport.queryForObject(usersql, DeviceToken.class, Integer.parseInt(userid));
					int member_id = (int)commentMap.get("member_id");
					if(userMessage!=null&&userMessage.getMember_id()!=member_id){
						String msql = "select em.uname as username from es_member em where em.member_id = ?";
						List<Map<String,Object>> map = this.baseDaoSupport.queryForList(msql, member_id);
						if(map!=null&&map.size()>0){
							String pushMessage = map.get(0).get("username")+ "回复了我的评论";
							PushMessage.pushSingleUser(pushMessage, dataMap, userMessage);
						}
					}
				}
			}
		}
	}

	@Override
	public Page getArticleComments(AdminSearchForm adminSearchForm,Page page) throws Exception {
		String sql = "select wac.id as id,wac.userIds as userIds,wac.content as content,wac.status as status,wac.create_time as create_time, em.uname as username,em.face as photo,em.member_id as userId,waa.content as articleContent from wh_api_comment wac "
				+ " left join wh_api_article waa on waa.id = wac.article_id "
				+ " left join es_member em on wac.member_id = em.member_id where 1=1 ";
		if(adminSearchForm!=null){
			if(adminSearchForm.getTitle()!=null&&!"".equals(adminSearchForm.getTitle())){
				sql = sql+" and wac.content like '%"+adminSearchForm.getTitle()+"%'";
			}
			if(adminSearchForm.getType()!=null&&!"".equals(adminSearchForm.getType())){
				sql = sql+" and wac.status = "+adminSearchForm.getType();
			}
		}
		return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(),ArticleComment.class);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateArticleComment(int commentId, Map<String, Object> commentMap) throws Exception {
		this.daoSupport.update("wh_api_comment", commentMap, "id = "+commentId);
		String article = "select article_id as aid from wh_api_comment wac where wac.id = ?";
		List<Map<String,Object>> am =  this.daoSupport.queryForList(article, commentId);
		if(am!=null&&am.size()>0){
			String updateSql = "update wh_api_article waa set waa.comment_count = waa.comment_count-1  where waa.id = ?";
			this.daoSupport.execute(updateSql, (int)am.get(0).get("aid"));
		}
	}

	@Override
	public Page getCommentsByArticleId(int articleId, Page page,int memberId) throws Exception {
		if(memberId==0){
			String sql = "select wac.id as id,wac.userIds as userIds,wac.userNames as userNames,wac.content as content,wac.status as status,wac.create_time as create_time, em.uname as username,em.member_id as userId,em.face as photo "
					+ " from wh_api_comment wac "
					+ " left join es_member em on wac.member_id = em.member_id "
					+ " where wac.article_id = ? and wac.status != -1 order by id desc";
			return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(),ArticleComment.class,articleId);
		}
		String asql ="select waa.member_id as userId from wh_api_article waa where waa.id = ? ";
		Map<String,Object> map = this.baseDaoSupport.queryForMap(asql, articleId);
		if(map==null){
			return new Page();
		}else{
			int articleUserId = (int)map.get("userId");
			String sql = "select wac.id as id,wac.userIds as userIds,wac.userNames as userNames,wac.content as content,wac.status as status,wac.create_time as create_time, em.uname as username,em.member_id as userId,em.face as photo "
					+ " from wh_api_comment wac "
					+ " left join es_member em on wac.member_id = em.member_id "
					+ " where wac.article_id = ? and (status = 0 or (status = 1 and "+memberId+"="+articleUserId+" and wac.userIds is null) or (status = 1 and wac.userIds is not null and wac.userIds like '%"+memberId+"%') or (status = 1 and "+memberId+"=wac.member_id) ) order by id desc";
			return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(),ArticleComment.class,articleId);
		}
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveUserAction(Map<String, Object> actionMap) throws Exception {
		if(actionMap.containsKey("type")&&(int)actionMap.get("type")==4){//赞类型的修改
			boolean isSendMessage = true;
			int memberId = (int)actionMap.get("member_id");
			int dataId =(int)actionMap.get("data_id");
			String sql = "select waa.love_count as loveCount,waa.create_time as createTime from wh_api_action waa where member_id = ? and data_id = ? and type = ?";
			List<Map<String,Object>> isLoveMap = this.daoSupport.queryForList(sql, memberId,dataId,4);
//			int count = this.daoSupport.queryForInt(sql, memberId,dataId,4);
			if(isLoveMap!=null&&isLoveMap.size()>0){
				long createTime = (Long)isLoveMap.get(0).get("createTime");
				if(new Date().getTime()-createTime<1000*60*1){
					isSendMessage = false;
				}
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("create_time", new Date().getTime());
				actionMap.put("status", 0);
				map.put("love_count", (int)(isLoveMap.get(0).get("loveCount"))+(int)(actionMap.get("love_count")));
				this.daoSupport.update("wh_api_action", map , "member_id = "+memberId+" and data_id = "+dataId);
			}else{
				actionMap.put("status", 0);
				actionMap.put("create_time",new Date().getTime());
				this.daoSupport.insert("wh_api_action", actionMap);
			}
			String article = "select id , love_count as loveCount from wh_api_article waa where waa.id = ?";
			ArticleModel am = (ArticleModel) this.daoSupport.queryForObject(article, ArticleModel.class, dataId);
			if(am!=null){
				Map<String,Object> amap = new HashMap<String,Object>();
				amap.put("love_count", am.getLoveCount()+(int)actionMap.get("love_count"));
				this.daoSupport.update("wh_api_article", amap, " id = "+dataId);
				//--------  推送  赞发文    -----------
				if(isSendMessage){
					Map<String,String> dataMap = new HashMap<String,String>();
					dataMap.put("type", "love");
					String usersql = "select ead.* from es_api_devicetoken ead "
							+ " left join wh_api_article waa on waa.member_id = ead.member_id "
							+ " where waa.id = ?";
					DeviceToken userMessage = (DeviceToken) this.baseDaoSupport.queryForObject(usersql, DeviceToken.class, dataId);
					int member_id = (int)actionMap.get("member_id");
					if(userMessage!=null&&userMessage.getMember_id()!=member_id){
						String msql = "select em.uname as username from es_member em where em.member_id = ?";
						List<Map<String,Object>> map = this.baseDaoSupport.queryForList(msql, member_id);
						if(map!=null&&map.size()>0){
							String pushMessage = map.get(0).get("username")+"赞了我的SHOW";
							PushMessage.pushSingleUser(pushMessage, dataMap, userMessage);
						}
					}
				}
			}
		}else{
			int type = (int)actionMap.get("type");
			int memberId = (int)actionMap.get("member_id");
			int dataId =(int)actionMap.get("data_id");
			if(type==5){
				type=1;
			}
			String sql = "select count(*) from wh_api_action waa where member_id = ? and data_id = ? and type = ?";
			int count = this.daoSupport.queryForInt(sql, memberId,dataId,type);
			if(count!=0&&count>0){
				Map<String,Object> map = new HashMap<String,Object>();
				map.put("status", 0);
				map.put("content", actionMap.get("content"));
				map.put("create_time",new Date().getTime());
				this.daoSupport.update("wh_api_action", map , "member_id = "+memberId+" and data_id = "+dataId+" and type = "+type);
			}else{
				actionMap.put("status", 0);
				actionMap.put("create_time",new Date().getTime());
				this.daoSupport.insert("wh_api_action", actionMap);
			}
			if(type==2){
				String addCollectSql = "update es_api_theme eat set eat.love_count = eat.love_count+1  where eat.id = ?";
				this.daoSupport.execute(addCollectSql, dataId);
			}else if(type==1&&(int)actionMap.get("type")!=5){
				//--------  推送   添加关注    -----------
				Map<String,String> dataMap = new HashMap<String,String>();
				dataMap.put("type", "friend");
				String usersql = "select ead.* from es_api_devicetoken ead "
						+ " where ead.member_id = ? ";
				DeviceToken userMessage = (DeviceToken) this.baseDaoSupport.queryForObject(usersql, DeviceToken.class, dataId);
				if(userMessage!=null&&dataId!=memberId){
					String msql = "select em.uname as username from es_member em where em.member_id = ?";
					List<Map<String,Object>> map = this.baseDaoSupport.queryForList(msql, memberId);
					if(map!=null&&map.size()>0){
						String pushMessage = map.get(0).get("username")+"关注了我";
						PushMessage.pushSingleUser(pushMessage, dataMap, userMessage);
					}
				}
			}
		}
		
	}

	@Override
	public Page getUserActionList(AdminSearchForm adminSearchForm, Page page) throws Exception {
		String sql = "select waa.*,(select uname from es_member where member_id = waa.member_id) as username,"
				+ " CASE waa.type WHEN 1 THEN (select uname from es_member where member_id = waa.data_id) "
				+ " WHEN 2 THEN (select title from es_api_theme where id = waa.data_id) "
				+ " WHEN 3 THEN (select name from es_goods where goods_id = waa.data_id) "
				+ " WHEN 4 THEN (select content from wh_api_article where id = waa.data_id) "
				+ " ELSE '空值' END as dataName"
				+ " from wh_api_action waa ";
		if(adminSearchForm!=null){
			if(adminSearchForm.getUsername()!=null&&!"".equals(adminSearchForm.getUsername())){
				sql = sql +" left join es_member em on em.member_id = waa.member_id ";
			}
			sql = sql +" where status =1 ";
			if(adminSearchForm.getUsername()!=null&&!"".equals(adminSearchForm.getUsername())){
				sql = sql +" and em.uname = '"+adminSearchForm.getUsername()+"' ";
			}
			if(adminSearchForm.getTitle()!=null&&!"".equals(adminSearchForm.getTitle())){
				sql = sql+" and waa.type ="+adminSearchForm.getTitle();
			}
			if(adminSearchForm.getType()!=null&&!"".equals(adminSearchForm.getType())){
				sql = sql+" and waa.status = "+adminSearchForm.getType();
			}
		}
		sql = sql +" order by id desc ";
		return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize());
	}

	@Override
	public void updateUserAction(int userActionId, Map<String, Object> userActionMap) throws Exception {
		this.daoSupport.update("wh_api_action", userActionMap, "id = "+userActionId);
	}


	@Override
	public List<Integer> getFollowsByMemberId(int cmemberId, int type) throws Exception {
		String sql = "select * from wh_api_action waa where status =1 and member_id = ? and type = ?";
		List<Map<String,Object>> resMaps = this.daoSupport.queryForList(sql, cmemberId,type);
		List<Integer> dataList = new ArrayList<Integer>();
		for(Map<String,Object> map:resMaps){
			dataList.add((int)map.get("data_id"));
		}
		return dataList;
	}


	@Override
	public ArticleModel getArtilceDetails(int articleId,int memberId) throws Exception {
		String sql = "select waa.id as id,waa.tagStr as tagStr,waa.content as content,em.uname as userName,em.face as userPhoto,em.member_id as userId, waa.status as status,em.birthday as userAge,em.height as userHeight,em.weight as userWeight, "
				+ " waa.image as image,waa.orImage as orImage,waa.love_count as loveCount,waa.comment_count as commentCount,waa.view_count as viewCount,waa.create_time as createTime,"
				+ " waa.good_cat_name as brandName,waa.good_cat_id as categoryIds "
				+ " from wh_api_article waa "
				+ " left join es_member em on em.member_id = waa.member_id "
				+ " where waa.id = ?";
		ArticleModel am = (ArticleModel) this.daoSupport.queryForObject(sql, ArticleModel.class, articleId);
		String categoryIds = am.getCategoryIds();
		if(categoryIds!=null&&!categoryIds.equals("")){
			String bsql = "select egc.cat_id as cid ,egc.name as cname,egc.image as cimage from es_goods_cat egc where egc.cat_id in ("+categoryIds+")";
			List<Map<String,Object>> catList = this.baseDaoSupport.queryForList(bsql);
			Map<Integer,String> catIdLogoMap = new HashMap<Integer,String>();
			for(Map<String,Object> mm:catList){
				int catId = (int)(mm.get("cid"));
				String catLogo = "";
				if(mm.get("cimage")!=null){
					catLogo = String.valueOf(mm.get("cimage"));
				}
				catIdLogoMap.put(catId, catLogo);
			}
			String[] catId = am.getCategoryIds().split(",");
			String catImages = "";
			for(String b:catId){
				if(catIdLogoMap.containsKey(Integer.parseInt(b))){
					catImages = catImages+StaticProperty.currentUrl+catIdLogoMap.get(Integer.parseInt(b))+",";
				}
			}
			if(!catImages.equals("")){
				catImages = catImages.substring(0,catImages.length()-1);
				am.setCategoryImage(catImages);
			}
		}
		if(am!=null){
			String loveUserSql ="select em.member_id as member_id,em.face as photo from wh_api_action waa "
					+ " left join es_member em on em.member_id = waa.member_id "
					+ " where waa.type = 4 and waa.data_id = ? order by love_count desc ";
			Page lovepage = this.daoSupport.queryForPage(loveUserSql, 1, 8, am.getId());
			am.setLoveUserList((List<Map<String,Object>>)lovepage.getResult());
			Map<String,Object> commentMap = new HashMap<String,Object>();
			commentMap.put("view_count", am.getViewCount()+1);
			this.daoSupport.update("wh_api_article", commentMap, " id = "+am.getId());
//			if(memberId!=0){
//				String isExistsSql ="select count(*) from es_api_user_view eauv where eauv.type = 'clickArticle' and eauv.dataId = ? and eauv.status = 1 and eauv.viewUserId = ?";
//				int count = this.daoSupport.queryForInt(isExistsSql, am.getId(),memberId);
//				if(count>0){
//					String updateUserViewSql = "update es_api_user_view eauv set eauv.viewCount = eauv.viewCount+1 where eauv.type = 'clickArticle' and eauv.dataId = ? and eauv.status = 1 and eauv.viewUserId = ?";
//					this.daoSupport.execute(updateUserViewSql, am.getId(),memberId);
//				}else{
//					Map<String,Object> userView = new HashMap<String,Object>();
//					userView.put("viewUserId", memberId);
//					userView.put("dataId", am.getId());
//					userView.put("type", "clickArticle");
//					userView.put("status", "1");
//					userView.put("viewCount", "1");
//					userView.put("create_time", new Date().getTime());
//					this.daoSupport.insert("es_api_user_view", userView);
//				}
//			}
		}
		return am;
	}


	@Override
	public void updateArticleTagType(int articleTagId, int type) {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("type", type);//type：1是hot -1不是
		this.daoSupport.update("wh_api_tag", map, "id ="+articleTagId);
	}


	@Override
	public Page getArticleTagList(Page page) throws Exception {
		String sql = "select wat.id as id,wat.content as ccontent "
				+ " from wh_api_tag wat "
				+ " where status = 1 and type = 1 ";
		sql = sql+" order by id desc";
		Page pg =  this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize());
		return pg;
	}


	@Override
	public Page getUserActionList(int articleId,Page page) {
		String sql ="select em.face as photo,em.member_id as member_id from wh_api_action waa "
				+ " left join es_member em on em.member_id = waa.member_id "
				+ " where waa.id = ? and type = 4 order by love_count desc";
		return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), articleId);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateUserActioin(Map<String, Object> actionMap) {
		Map<String,Object> updateMap = new HashMap<String,Object>();
		updateMap.put("status", -1);
		this.daoSupport.update("wh_api_action", updateMap, actionMap);
		int type = (Integer)actionMap.get("type");
		if(type==2){
			int themeId = (Integer)actionMap.get("data_id");
			String addCollectSql = "update es_api_theme eat set eat.love_count = eat.love_count-1  where eat.id = ?";
			this.daoSupport.execute(addCollectSql, themeId);
		}
	}


	@Override
	public void saveUserView(Map<String, Object> userView) throws Exception {
		this.daoSupport.insert("es_api_user_view", userView);
	}

	
}
