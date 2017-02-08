package com.enation.app.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enation.app.api.service.PersionService;
import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class PersionServiceImpl extends BaseSupport implements PersionService{

	@Override
	public JSONObject getUserActionCount(JSONObject obj, int member_id) {
		//赞数
		String loveCount = "select sum(waa.love_count) as loveCount from wh_api_action waa "
				+ " left join wh_api_article war on war.id = waa.data_id "
				+ " left join es_member em on em.member_id = war.member_id "
				+ " where waa.type = 4 and em.member_id = ?";
		obj.put("loveCount", String.valueOf(this.daoSupport.queryForInt(loveCount, member_id)));
		//关注数
		String attentionCount = "select count(*) as count from wh_api_action waa where member_id = ? and type= 1 and waa.status != -1";
		obj.put("attentionCount", String.valueOf(this.daoSupport.queryForInt(attentionCount, member_id)));
		//粉丝数
		String fansCount = "select count(*) as count from wh_api_action waa where data_id = ? and type= 1 and waa.status != -1";
		obj.put("fansCount", String.valueOf(this.daoSupport.queryForInt(fansCount, member_id)));
		//印象数组
		String impressCount = "select content as content, count(*) as count from wh_api_action waa where type = 1 and data_id = ? group by content order by count desc";
		List<Map<String,Object>> re = (List<Map<String,Object>>)this.daoSupport.queryForList(impressCount, member_id);
		if(re!=null&&re.size()>0){
			JSONArray ja = new JSONArray ();
			for(Map<String,Object> map:re){
				if(map.get("content")!=null){
					JSONObject impressObj = new JSONObject();
					impressObj.put("content", map.get("content"));
					impressObj.put("count", String.valueOf((Long)map.get("count")));
					ja.add(impressObj);
				}
			}
			obj.put("impress", ja);
		}
		//发文数
		String showCount="select count(*) from wh_api_article waa where member_id = ? and status != -1";
		obj.put("showCount", String.valueOf(this.daoSupport.queryForInt(showCount, member_id)));
		//收藏主题数
		String collectTheme = "select count(*) from wh_api_action waa where waa.type = 2 and waa.member_id = ? and waa.status != -1";
		obj.put("collectTheme", String.valueOf(this.daoSupport.queryForInt(collectTheme, member_id)));
		//收藏商品数
		String collectProduct = "select count(*) from wh_api_action waa where waa.type = 3 and waa.member_id = ? and waa.status != -1";
		obj.put("collectProduct", String.valueOf(this.daoSupport.queryForInt(collectProduct, member_id)));
		
		return obj;
	}

	@Override
	public void updatePersionDetails(Member member) {
		this.daoSupport.update("es_member", member, "member_id = "+member.getMember_id());
	}

	@Override
	public Page myFriendList(int member_id,int userId, int type, Page page) {
		if(type==1){//我的关注
			String sql = " select em.member_id as userId,em.face as photo,em.uname as username,"
					+ " (select 1 from wh_api_action waab where waab.type = 1 and waab.status != -1 and waab.member_id = waa.data_id and waab.data_id = "+member_id+") as isFan,"
					+ " (select count(*) from wh_api_action waac where waac.type = 1 and waac.status != -1 and waac.data_id = waa.data_id) as fanCount,"
					+ " (select count(*) from wh_api_article waat where waat.status != -1 and waat.member_id = waa.data_id) as articleCount"
					+ " from wh_api_action waa "
					+ " left join es_member em on em.member_id = waa.data_id "
					+ " where waa.type = 1 and waa.member_id = ? and waa.status != -1 order by waa.create_time desc";
			return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), userId);
		}else if(type==2){//我的粉丝
			String sql = " select em.member_id as userId,em.face as photo,em.uname as username,"
					+ " (select 1 from wh_api_action waab where waab.type = 1 and waab.status != -1 and waab.member_id = "+member_id+" and waab.data_id = waa.member_id) as isFan,"
					+ " (select count(*) from wh_api_action waac where waac.type = 1 and waac.status != -1 and waac.data_id = waa.member_id) as fanCount,"
					+ " (select count(*) from wh_api_article waat where waat.status != -1 and waat.member_id = waa.member_id) as articleCount"
					+ " from wh_api_action waa "
					+ " left join es_member em on em.member_id = waa.member_id "
					+ " where waa.type = 1 and waa.data_id = ? and waa.status != -1 order by waa.create_time desc";
			return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), userId);
		}
		return null;
	}

	@Override
	public Page fetchUserActionList(int member_id, int type, Page page) {
		if(type==1){//1是发文列表
			String sql ="select waa.id as articleId,waa.image as articleImage from wh_api_article waa where waa.status != -1 and waa.member_id = ? order by waa.create_time desc";
			return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), member_id);
		}else if(type==2){//收藏主题列表
			String sql ="select eat.id as themeId,eat.title as themeTitle,eat.contentStyle as contentStyle,eat.details as themeDetails,eat.minorImage as themeImage from wh_api_action waa "
					+ " left join es_api_theme eat on eat.id = waa.data_id "
					+ " where exists(select 1 from es_api_theme et where waa.data_id = et.id) and waa.type = 2 and waa.status != -1 and waa.member_id = ? order by waa.create_time desc";
			return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), member_id);
		}else if(type==3){//收藏商品列表
			String sql ="select eg.goods_id as goods_id,eg.isShowMKPrice as isShowMKPrice,eg.brief as brief,eg.name as name,eg.original as original,eg.price as price,eg.mktprice as mktprice,eg.url as url,eg.productOrigin as productOrigin from wh_api_action waa "
					+ " left join es_goods eg on eg.goods_id = waa.data_id "
					+ " where exists(select 1 from es_goods eeg where eeg.goods_id = waa.data_id) and waa.type = 3 and waa.status != -1 and waa.member_id = ? order by waa.create_time desc";
			return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), member_id);
		}
		return null;
	}

	@Override
	public void saveFAQ(Map<String, Object> map) throws Exception {
		this.daoSupport.insert("wh_api_faq", map);
	}

	@Override
	public void fecthMessageCount(JSONObject jsonObject, int member_id) {
		if(member_id ==0 ){
			jsonObject.put("totalCount", "0");
			jsonObject.put("loveCount", "0");
			jsonObject.put("commentCount", "0");
			jsonObject.put("addFriendCount", "0");
		}else{
			//被赞数
			String loveCountSql = "select count(*) from wh_api_action waa "
					+ "LEFT JOIN wh_api_article wa on wa.id = waa.data_id where wa.member_id = ? and waa.type= 4 and waa.status = 0 and waa.member_id != ?";
			int loveCount = this.daoSupport.queryForInt(loveCountSql, member_id,member_id);
			jsonObject.put("loveCount", String.valueOf(loveCount));
			//评论数
			String commentCountSql = "select count(*) from wh_api_comment wac "
					+ "LEFT JOIN wh_api_article waa on waa.id = wac.article_id where (waa.member_id = ? or find_in_set(?,wac.userIds)>0 ) and waa.status != -1 and wac.status != -1 and wac.isRead = 0 and wac.member_id != ?";
			int commentCount = this.daoSupport.queryForInt(commentCountSql, member_id,member_id,member_id);
			jsonObject.put("commentCount", String.valueOf(commentCount));
			//加好友数
			String addSql = "select count(*) from wh_api_action waa where waa.data_id = ? and waa.type= 1 and waa.status = 0";
			int addFriendCount = this.daoSupport.queryForInt(addSql, member_id);
			jsonObject.put("addFriendCount", String.valueOf(addFriendCount));
			//系统通知数
			String xtsql ="select count(*) from es_api_push_message eapm  where eapm.member_id = ? and eapm.status = 0";
			int xtCount = this.daoSupport.queryForInt(xtsql, member_id);
			jsonObject.put("xtCount", String.valueOf(xtCount));
			
			int totalCount = addFriendCount+commentCount+loveCount+xtCount;
			jsonObject.put("totalCount", String.valueOf(totalCount));
		}
		
	}

	@Override
	public Page fecthMessageList(int type, int member_id,Page page) throws Exception {
		String sql= "";
		if(type==1){//被加关注列表1
			sql ="select waa.id as zzid,waa.content as content,em.member_id as memberId,em.face as photo,em.uname as username,waa.create_time as createTime "
					+ " from wh_api_action waa left join es_member em on em.member_id = waa.member_id "
					+ " where waa.data_id = ? and waa.type = 1 and waa.status != -1 order by waa.create_time desc";
			Page respage = this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), member_id);
			String zzid = "";
			for(Map<String,Object> map:(List<Map<String,Object>>)respage.getResult()){
				zzid = zzid +map.get("zzid")+",";
			}
			if(!zzid.equals("")){
				zzid = zzid.substring(0,zzid.lastIndexOf(","));
			}else{
				zzid = "0";
			}
			String updateSql = "update wh_api_action waa set waa.status = 1 where waa.id in ("+zzid+")";//标记为已读
			this.daoSupport.execute(updateSql);
			return respage;
		}else if(type==2){//被评论列表2
			sql ="select wac.id as zzid ,em.member_id as memberId,em.face as photo,em.uname as username,wac.create_time as createTime,waa.image as articleImage,waa.id as articleId "
					+ " from wh_api_comment wac left join wh_api_article waa on waa.id = wac.article_id "
					+ " left join es_member em on em.member_id = wac.member_id "
					+ " where (waa.member_id = ? or find_in_set(?,wac.userIds)>0 ) and waa.status != -1 and wac.status != -1 and wac.member_id != ? order by wac.create_time desc";
			Page respage = this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), member_id,member_id,member_id);
			String zzid = "";
			for(Map<String,Object> map:(List<Map<String,Object>>)respage.getResult()){
				zzid = zzid +map.get("zzid")+",";
			}
			if(!zzid.equals("")){
				zzid = zzid.substring(0,zzid.lastIndexOf(","));
			}else{
				zzid = "0";
			}
			String updateSql = "update wh_api_comment wac set wac.isRead = 1 where wac.id in ("+zzid+") or (wac.member_id =? )";//标记为已读
			this.daoSupport.execute(updateSql,member_id);
			return respage;
		}else if(type==3){//被赞类别3
			sql ="select waa.id as zzid,em.member_id as memberId,em.face as photo,em.uname as username,waa.create_time as createTime,waa.love_count as loveCount,wa.image as articleImage,wa.id as articleId "
					+ " from wh_api_action waa left join es_member em on em.member_id = waa.member_id "
					+ " left join wh_api_article wa on wa.id = waa.data_id "
					+ " where waa.type = 4 and waa.status != -1 and wa.member_id = ? and waa.member_id != ? order by waa.create_time desc";
			Page respage = this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), member_id,member_id);
			String zzid = "";
			for(Map<String,Object> map:(List<Map<String,Object>>)respage.getResult()){
				zzid = zzid +map.get("zzid")+",";
			}
			if(!zzid.equals("")){
				zzid = zzid.substring(0,zzid.lastIndexOf(","));
			}else{
				zzid = "0";
			}
			String updateSql = "update wh_api_action waa set waa.status = 1 where waa.id in ("+zzid+") or (waa.member_id =? and waa.type = 4)";//标记为已读
			this.daoSupport.execute(updateSql,member_id);
			return respage;
		}else if(type==4){//通知4 
			sql ="select eapm.id as zzid,eapm.type as type,eapm.content as content,eapm.data_id as dataId,eapm.create_time as createTime, "
					+ " eat.title as ttitle,eat.image as timage,eat.details as tdetails ,"
					+ " eg.name as pname,eg.brief as pbrief,eg.price as pprice,eg.mktprice as pmkprice,eg.original as pimage,eg.productOrigin as productOrigin "
					+ " from es_api_push_message eapm "
					+ " left join es_api_theme eat on (eat.id = eapm.data_id and locate('theme',eapm.type)>0) "
					+ " left join es_goods eg on (eg.goods_id = eapm.data_id and eapm.type='product')  "
					+ " where eapm.member_id = ? and eapm.status != -1 order by eapm.create_time desc";
			Page respage = this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), member_id);
			String zzid = "";
			for(Map<String,Object> map:(List<Map<String,Object>>)respage.getResult()){
				zzid = zzid +map.get("zzid")+",";
			}
			if(!zzid.equals("")){
				zzid = zzid.substring(0,zzid.lastIndexOf(","));
			}else{
				zzid = "0";
			}
			String updateSql = "update es_api_push_message eapm set eapm.status = 1 where eapm.id in ("+zzid+")";//标记为已读
			this.daoSupport.execute(updateSql);
			return respage;
		}else{
			return new Page();
		}
	}
 
	@Override
	public boolean getIsFriend(int member_id, String userId) throws Exception {
		String sql = "select count(*) from wh_api_action waa where waa.type = 1 and waa.member_id = ? and waa.data_id = ? and waa.status != -1";
		int count = this.baseDaoSupport.queryForInt(sql, member_id,userId);
		if(count>0){
			return true;
		}
		return false;
	}

	/*  保存用户的动作  1.loginClickTheme 2.nologinClickTheme  
	 * 3.loginClickProduct 4.nologinClickProduct
	 * 5.loginClickBanner 6.nologinClickBanner
	 * 7.loginClickArticle 8.nologinClickArticle
	 */
	@Override
	public void saveUserAction(String member_id, String type, int data_id) throws Exception {
		Map<String,Object> userView = new HashMap<String,Object>();
		userView.put("dataId", data_id);
		userView.put("type", type);
		userView.put("status", "1");
		userView.put("viewCount", "1");
		userView.put("viewUserId", member_id);
		userView.put("create_time", new Date().getTime());
		this.daoSupport.insert("es_api_user_view", userView);
	}

}
