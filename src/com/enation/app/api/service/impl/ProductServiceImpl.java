package com.enation.app.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.api.dao.ThemeProductDao;
import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.StaticProperty;
import com.enation.app.api.model.Theme;
import com.enation.app.api.model.ThemeContent;
import com.enation.app.api.model.ThemeTag;
import com.enation.app.api.service.ProductService;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.shop.component.gallery.model.GoodsGallery;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class ProductServiceImpl extends BaseSupport implements ProductService{

	@Resource
	private IStoreGoodsManager storeGoodsManager;
	@Resource
	private ThemeProductDao themeProductDao;
	
	public Page getProductList(int pageNo,int pageSize,Map<String,Object> map) {
		String sql ="select g.* from es_goods g where g.market_enable=1 ";
		//Page page = storeGoodsManager.b2b2cGoodsList(pageNo,pageSize,map);
		int disabled=(map.get("disabled")==null)?0:(int)map.get("disabled");
		if(disabled != 10){
			sql = sql + "  and g.disabled="+disabled;
		}
		String keyword=(String) ((map.get("namekeyword")==null)||("null").equals(map.get("namekeyword"))?"":map.get("namekeyword"));
		if(!StringUtil.isEmpty(keyword)){
			sql = sql + "  and ((g.name like '%"+keyword+"%') or ( g.sn like '%"+keyword+"%'))";
		}
		Long time=(long) (map.get("time")==null?0L:(Long)map.get("time"));
		if(time!=0){
			sql = sql + " and g.startTime <"+time+" and g.endTime >"+time;
		}
		String orderStr = (String) ((map.get("order")==null)?"g.startTime":map.get("order"));
		sql = sql +" order by "+orderStr+" desc";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize);
	}

	@Override
	public void updateProduct(int productId, Map<String, Object> map) throws Exception {
		this.daoSupport.update("es_goods", map, " goods_id = "+productId);
	}
	/**
	 * app主题列表接口
	 * @param pageNo
	 * @param pageSize
	 * @param map
	 * @return
	 */
	public Page getThemeProductsAPP(int pageNo, int pageSize, Map<String, String> map) {
		String sql="select at.id as id,at.contentStyle as contentStyle,at.image as image,at.title as title,at.details as details,eatt.image as tagImage "
				+ " from es_api_theme at "
				+ " left join es_api_theme_tag eatt on eatt.id = at.theme_tag_id "
				+ " where at.status = '1' and at.startTime < ?";
		if(map!=null){
			if(map.containsKey("indexStatus")){
				sql = sql +" and at.indexStatus = 1 ";
			}
		}
		sql = sql +" order by at.startTime desc";
		List<Map<String,Object>> themeList = this.daoSupport.queryForListPage(sql, pageNo, pageSize,new Date().getTime());
		String countSql = "SELECT COUNT(*) from es_api_theme where status =1 and startTime < ?";
		int totalCount = this.daoSupport.queryForInt(countSql,new Date().getTime());
		return new Page(0, totalCount, pageSize, themeList);
	}
	/**
	 * 后台主题列表
	 * @param pageNo
	 * @param pageSize
	 * @param map    
	 * @return
	 */
	public Page getThemeProducts(int pageNo, int pageSize, Map<String, Object> map) {
		String sql="select (select count(*) from wh_api_action waa where waa.type = 2 and waa.data_id = at.id and waa.status = 0 ) as loveThemeCount,"
				+ " at.id as id,at.love_count as loveCount,at.minorImage as minorImage,at.contentStyle as contentStyle,at.image as image,at.title as title,at.details as details,at.indexStatus as indexStatus,at.findStatus as findStatus,at.recommendStatus as recommendStatus,at.loginClickCount as loginClickCount,at.clickCount as clickCount,at.create_time as createTime,at.startTime as startTime "
				+ " from es_api_theme at "
				+ " where at.status = '1' ";
		if(map!=null){
			if(map.containsKey("startTime")){
				sql = sql +" and at.startTime > "+ (long)map.get("startTime");
			}
			if(map.containsKey("endTime")){
				sql = sql +" and at.startTime < "+ (long)map.get("endTime");
			}
			if(map.containsKey("indexStatus")){
				sql = sql +" and at.indexStatus = 1 and at.startTime < "+new Date().getTime();
			}
			if(map.containsKey("findStatus")){
				sql = sql +" and at.findStatus = 1 and at.startTime < "+new Date().getTime();
			}
			if(map.containsKey("bannerStatus")){
				sql = sql +" and at.bannerStatus = "+Long.parseLong((String)map.get("bannerStatus"));
			}
			if(map.containsKey("recommendStatus")){
				sql = sql +" and at.recommendStatus = 1 and at.startTime < "+new Date().getTime();
			}
			if(map.containsKey("themeId")){
				sql = sql +" and at.id != "+map.get("themeId");
			}
			if(map.containsKey("keywords")&&!"".equals(map.get("keywords"))){
				sql = sql +" and at.title like '%"+map.get("keywords")+"%' ";
			}
			if(map.containsKey("contentStyle")){
				sql = sql +" and at.contentStyle = '"+(String)map.get("contentStyle")+"'";
			}
			if(map.containsKey("typeId")){
				int typeId = Integer.parseInt((String)map.get("typeId"));
				if(typeId==0){
					sql = sql +" and at.id in (select eatt.themeid from es_api_theme_themetag eatt where eatt.themetagkeyid = "+typeId +" )";
				}else{
					sql = sql +" and at.id in (select eatt.themeid from es_api_theme_themetag eatt where eatt.themetagvalueid = "+typeId +" )";
				}
			}
		}
		sql = sql+" order by at.startTime desc";
		List<Map> themeList = this.daoSupport.queryForListPage(sql, pageNo, pageSize);
		String themeIdsStr = "";
		List<ThemeProduct> themeProducts = new ArrayList<ThemeProduct>();
		for(Map resObj:themeList){
			int themeid = (Integer)resObj.get("id");
			if("".equals(themeIdsStr)){
				themeIdsStr = themeIdsStr+themeid;
			}else{
				themeIdsStr = themeIdsStr+","+themeid;
			}
			ThemeProduct tps = new ThemeProduct();
			Theme thm = new Theme();
			thm.setId(themeid);
			thm.setTitle((String)resObj.get("title"));
			thm.setDetails((String)resObj.get("details"));
			thm.setImage((String)resObj.get("image"));
			thm.setMinorImage((String)resObj.get("minorImage"));
			thm.setIndexStatus((int)resObj.get("indexStatus"));
			thm.setFindStatus((int)resObj.get("findStatus"));
			thm.setRecommendStatus((int)resObj.get("recommendStatus"));
			thm.setClickCount((int)resObj.get("clickCount"));
			thm.setLoginClickCount((int)resObj.get("loginClickCount"));
			thm.setCreate_time((Long)resObj.get("createTime"));
			thm.setContentStyle((String)resObj.get("contentStyle"));
			thm.setLove_count((int)resObj.get("loveCount"));
			thm.setRealClickCount((long)resObj.get("loveThemeCount"));
			thm.setStartTime((Long)resObj.get("startTime"));
			tps.setTheme(thm);
			themeProducts.add(tps);
		}
		if(themeIdsStr!=""){
			String themetagSql = "select themeid as tid, eatt.name as tagname, eatt.image as tagImage,att.themetagkeyid as keyid,att.themetagvalueid as valueid "
					+ " from es_api_theme_themetag att "
					+ " left join es_api_theme_tag eatt on eatt.id = att.themetagvalueid "
					+ " where att.themeid in ( "+themeIdsStr+" ) ";
			List<Map> themetags = this.daoSupport.queryForList(themetagSql);
			for(Map obj:themetags){
				int themeid = (Integer)obj.get("tid");
				int keyid = (Integer)obj.get("keyid");
				int valueid = (Integer)obj.get("valueid");
				String tagImage = (String)obj.get("tagImage");
				for(ThemeProduct tp:themeProducts){
					if(tp.getTheme().getId()==themeid){
						if(tp.getTheme().getTagsImage()==null){
							tp.getTheme().setTagsImage(StaticProperty.currentUrl+tagImage);
							Map<Integer,Integer> themetagList = new HashMap<Integer,Integer>();
							themetagList.put(keyid, valueid);
							tp.getTheme().setThemetagList(themetagList);
						}else{
							tp.getTheme().setTagsImage(StaticProperty.currentUrl+tp.getTheme().getTagsImage()+","+tagImage);
							tp.getTheme().getThemetagList().put(keyid, valueid);
						}
						break;
					}
				}
			}
		}
		String countSql = "SELECT COUNT(*) from es_api_theme where status =1 ";
		if(map.containsKey("startTime")){
			countSql = countSql +" and startTime > "+ (long)map.get("startTime");
		}
		if(map.containsKey("endTime")){
			countSql = countSql +" and startTime < "+ (long)map.get("endTime");
		}
		if(map.containsKey("contentStyle")){
			countSql = countSql +" and contentStyle = '"+(String)map.get("contentStyle")+"'";
		}
		int totalCount = this.daoSupport.queryForInt(countSql);
		return new Page(pageNo, totalCount, pageSize, themeProducts);
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	public void saveProductJoinTheme(int good_id, int theme_id, int position,String status,String type) {
		if(status!=null&&"-1".equals(status)){
			themeProductDao.themeDelProduct(good_id, theme_id);
		}else{
			themeProductDao.productJoinTheme(good_id, theme_id, position,type);
		}
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	public Theme saveTheme(Theme theme,List<Map<String,Object>> contentMaps) {
		theme.setCreate_time(new Date().getTime());
		theme.setProduct_count(0);
		theme.setStatus("1");
		theme.setCreate_time(new Date().getTime());
		theme.setIndexStatus(-1);
		theme.setFindStatus(-1);
		theme.setRecommendStatus(1);
		theme.setClickCount(0);
		theme.setLove_count(0);
		theme.setRealClickCount(0);
		this.daoSupport.insert("es_api_theme", theme);
		theme.setId(this.daoSupport.getLastId("es_api_theme"));
		//保存主题内容
		for(Map<String,Object> map :contentMaps){
			map.put("theme_id", theme.getId());
			this.daoSupport.insert("es_api_theme_content", map);
		}
		return theme;
	}

	@Transactional(propagation = Propagation.REQUIRED) 
	public void loveTheme(int memberId, int themeId) {
		themeProductDao.loveTheme(memberId, themeId);
	}

	@Override
	public ThemeProduct catchThemeDetails(int themeId) {
		return themeProductDao.catchThemeDetails(themeId);
	}

	@Override
	public Theme updateTheme(int themeId,Map<String,Object> maps) {
		return themeProductDao.updateTheme(themeId,maps);
	}

	@Override
	public void deleTheme(int tid) {
		this.daoSupport.execute("delete from es_api_theme_product where theme_id = ?", tid);//删除主题的商品
		this.daoSupport.execute("delete from es_api_theme_themetag where themeid = ?", tid);//删除主题的分类属性
		this.daoSupport.execute("delete from es_api_theme where id = ?", tid);//删除主题
		this.daoSupport.execute("delete from es_api_theme_content where  theme_id = ?", tid);//删除主题内容
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ThemeTag> getThemeTagList() {
		String sql="select * from es_api_theme_tag where status =1 order by create_time desc";
		List<ThemeTag> tts = this.daoSupport.queryForList(sql, ThemeTag.class);
		List<ThemeTag> themeTagList = new ArrayList<ThemeTag>();
		for(ThemeTag tt:tts){
			if(tt.getCategory()==0){
				themeTagList.add(tt);
			}
		}
		for(ThemeTag tt:themeTagList){
			List<ThemeTag> cthemeTagList = new ArrayList<ThemeTag>();
			for(ThemeTag tta:tts){
				if(tta.getCategory()==tt.getId()){
					cthemeTagList.add(tta);
				}
			}
			tt.setChildrenThemeTag(cthemeTagList);
		}
		return themeTagList;
	}

	@Override
	public void addThemeTag(ThemeTag themeTag) {
		this.daoSupport.insert("es_api_theme_tag", themeTag);
		
	}

	@Override
	public List<ThemeTag> getParentThemeTagList() {
		String sql = "select * from es_api_theme_tag where status =1 and category =0";
		return this.daoSupport.queryForList(sql, ThemeTag.class);
	}

	@Override
	public List<ThemeTag> getChildrenThemeTagList() {
		String sql = "select * from es_api_theme_tag where status =1 and category !=0";
		return this.daoSupport.queryForList(sql, ThemeTag.class);
	}

	@Override
	public void deleThemeTag(Long ttId) {
		this.daoSupport.execute("delete from es_api_theme_tag where id = ?", ttId);
	}

	@Override
	public void updateThemeTag(int themeId, int keyId, int valueId) {
		String sql="select count(*) from es_api_theme_themetag where themeid = ? and themetagkeyid = ?";
		int resint = this.daoSupport.queryForInt(sql, themeId,keyId);
		if(resint==0){
			Map<String, Object> themetag = new HashMap<String,Object>();
			themetag.put("themeid", themeId);
			themetag.put("themetagkeyid", keyId);
			themetag.put("themetagvalueid", valueId);
			themetag.put("create_time", new Date().getTime());
			this.daoSupport.insert("es_api_theme_themetag", themetag);
		}else{
			if(valueId==0){
				this.daoSupport.execute("delete from es_api_theme_themetag where themeid = ? and themetagkeyid = ?", themeId,keyId);//
			}else{
				Map<String, Object> themetag = new HashMap<String,Object>();
				themetag.put("themetagvalueid", valueId);
				themetag.put("create_time", new Date().getTime());
				this.daoSupport.update("es_api_theme_themetag", themetag, "themeid = "+themeId +" and themetagkeyid =" + keyId);
			}
		}
		String tagSql = "select eatt.name as tagname from es_api_theme_tag eatt where eatt.id = ? and eatt.status = 1";
		List<Map<String, Object>> restag = this.daoSupport.queryForList(tagSql, keyId);
		if(restag!=null&&restag.size()>0){
			String tagname = (String)restag.get(0).get("tagname");
			if("身型".equals(tagname)||"肤质".equals(tagname)){
				Map<String,Object> tagupdate= new HashMap<String,Object>();
				tagupdate.put("theme_tag_id", valueId);
				this.daoSupport.update("es_api_theme", tagupdate, "id = "+themeId);
			}
		}
	}

	@Override
	public Page getThemeJsonList(int parseInt, int pageSize, Map<String, String> map) {
		String keyword=(String) (map.get("namekeyword")==null?"":map.get("namekeyword"));
		String sql = "select * from es_api_theme where status =1 ";
		if(!StringUtil.isEmpty(keyword)){
			sql = sql+" and title like '%"+keyword+"%' ";
		}
		sql = sql +" order by id desc ";
		return this.daoSupport.queryForPage(sql, parseInt, pageSize);
	}

	@Override
	public Theme getThemeDetails(int themeId,int memberId) throws Exception {
		String sql ="select eat.* ";
		if(memberId!=0){
			sql = sql +",(select count(*) from wh_api_action waa where waa.status != -1 and waa.type = 2 and waa.member_id = "+memberId+" and waa.data_id = "+themeId+") as isLove";
		}
		sql = sql + " from es_api_theme eat where eat.id = ? and eat.status = '1' ";
		Theme theme = (Theme) this.baseDaoSupport.queryForObject(sql, Theme.class, themeId);
		if(theme == null){
			throw new Exception("主题id错误!");
		}
		String contentSql="select eatc.*,eg.name as productName,eg.original as productImage,eg.brief as productBrief,eg.productOrigin as productOrigin,eatc.status as status,eg.price as productPrice,eg.mktprice as productMkPrice,eg.intro as intro,eg.url as url ";
		if(memberId!=0){
			contentSql = contentSql +",(select count(*) from wh_api_action waa where waa.status != -1 and waa.type = 3 and member_id = "+memberId+" and data_id = eg.goods_id) as isCollect";
		}
		contentSql = contentSql + ", eb.name as productBrandName, egc.image as productCategoryImage "
				+ " from es_api_theme_content eatc "
				+ " left join es_goods eg on (eatc.goods_id!=0 and eatc.goods_id = eg.goods_id)"
				+ " left join es_brand eb on (eatc.goods_id!=0 and eb.brand_id = eg.brand_id)"
				+ " left join es_goods_cat egc on (eatc.goods_id!=0 and egc.cat_id = eg.cat_id)"
				+ " where eatc.status != '-1' and theme_id = ?";
		List<ThemeContent> themeContent = this.baseDaoSupport.queryForList(contentSql, ThemeContent.class, themeId);
		theme.setThemeContent(themeContent);
		
		if(memberId!=0){
			String clickSql="update es_api_theme eat set eat.loginClickCount = eat.loginClickCount+1 where eat.id = ?";
			this.daoSupport.execute(clickSql, theme.getId());
		}else{
			String clickSql="update es_api_theme eat set eat.clickCount = eat.clickCount+1 where eat.id = ?";
			this.daoSupport.execute(clickSql, theme.getId());
		}
		return theme;
	}

	@Override
	public ThemeTag getThemeTagById(String typeId) throws Exception {
		if(typeId==null||"".equals(typeId)){
			return null;
		}
		String sql = "select eatt.* from es_api_theme_tag eatt where eatt.id = ?";
		return (ThemeTag) this.daoSupport.queryForObject(sql, ThemeTag.class, typeId);
	}

	@Override
	public void addProduct(Map<String, Object> map) throws Exception {
		int catId = (int)map.get("cat_id");
		String sql ="select cat_id as catId,type_id as typeId from es_goods_cat egc where egc.cat_id = ?";
		Map<String,Object> resObj = this.daoSupport.queryForMap(sql, catId);
		map.put("type_id", (int)resObj.get("typeId"));
		map.put("view_count", 0);
		map.put("create_time", new Date().getTime());
		map.put("last_modify", new Date().getTime());
		map.put("goods_type", "normal");
		map.put("market_enable", 0);
		map.put("store_name", "WeMan我们");
		map.put("store_id", 1);
		map.put("disabled", 0);
		this.daoSupport.insert("es_goods", map);
	}

	@Override
	public void updateThemeTag(ThemeTag tt) throws Exception {
		this.daoSupport.update("es_api_theme_tag", tt, "id = "+tt.getId());
	}

	@Override
	public Page userThemeCount(Long startTime, Long endTime,Long userStartTime, Long userEndTime, int dataId, String type,Page page) {
		String sql ="select em.uname as username,em.regtime as regtime,sum(eauv.viewCount) as viewCount,eat.title as title from es_api_user_view eauv "
				+ " left join es_api_theme eat on eat.id = eauv.dataId "
				+ " left join es_member em on em.member_id = eauv.viewUserId "
				+ " where 1=1 ";
		if(type.indexOf("ClickBanner")>0){
			sql = sql + " and eauv.type like '%loginClickBanner-index%'";
		}else{
			sql = sql + " and eauv.type = ?" ;
		}
		sql = sql + " and eauv.viewUserId is not null and eauv.dataId = ? and eauv.create_time > ? and eauv.create_time <= ?"
				+ " and em.regtime > ? and em.regtime < ? group by eauv.viewUserId ";
		//Page resPage = this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), type,dataId,startTime,endTime,userStartTime,userEndTime);
		List<Map<String,Object>> userlist = null;
		if(type.indexOf("ClickBanner")>0){
			userlist = this.daoSupport.queryForListPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), dataId,startTime,endTime,userStartTime,userEndTime);
		}else{
			userlist = this.daoSupport.queryForListPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), type,dataId,startTime,endTime,userStartTime,userEndTime);
		}
		String sql2 ="select eauv.id from es_api_user_view eauv "
				+ " left join es_member em on em.member_id = eauv.viewUserId "
				+ " where eauv.type = ? and eauv.viewUserId is not null and eauv.dataId = ? and eauv.create_time > ? and eauv.create_time <= ?"
				+ " and em.regtime > ? and em.regtime < ? group by eauv.viewUserId ";
		List<Map<String,Object>> userlists = this.daoSupport.queryForList(sql2, type,dataId,startTime,endTime,userStartTime,userEndTime);
//		String countSql = "SELECT COUNT(*) from es_api_user_view eauv "
//				+ " left join es_member em on em.member_id = eauv.viewUserId "
//				+ " where eauv.type = ? and eauv.viewUserId is not null and eauv.dataId = ? and eauv.create_time > ? and eauv.create_time <= ?"
//				+ " and em.regtime > ? and em.regtime < ? group by eauv.viewUserId";
//		int totalCount = this.daoSupport.queryForInt(countSql, type,dataId,startTime,endTime,userStartTime,userEndTime);
		return new Page((int)page.getCurrentPageNo()*page.getPageSize(), userlists.size(), page.getPageSize(), userlist);
	}

	@Override
	public Page noUserThemeCount(Long startTime, Long endTime, int dataId, String type,Page page) {
//		String sql ="select count(*) as viewCount,eat.title as title from es_api_user_view eauv "
//				+ " left join es_api_theme eat on eat.id = eauv.dataId "
//				+ " where eauv.type = ? and eauv.viewUserId is null and "
//				+ " eauv.dataId = ? and eauv.create_time > ? and eauv.create_time < ? ";
//		List<Map<String,Object>> userlist = this.daoSupport.queryForListPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), type,dataId,startTime,endTime);
		String sql = "select count(*) as viewCount from es_api_user_view eauv where 1=1 ";
		if(type.indexOf("ClickBanner")>0){
			sql = sql + " and eauv.type like '%nologinClickBanner-index%'";
		}else{
			sql = sql + " and eauv.type = ?" ;
		}
		sql = sql + " and eauv.dataId = ? and eauv.create_time > ? and eauv.create_time <= ? group by eauv.viewUserId ";
		List<Map<String,Object>> userlists = null;
		if(type.indexOf("ClickBanner")>0){
			userlists = this.daoSupport.queryForList(sql,dataId,startTime,endTime);
		}else{
			userlists = this.daoSupport.queryForList(sql, type,dataId,startTime,endTime);
		}
		String countSql = "SELECT COUNT(*) from es_api_user_view eauv "
				+ " left join es_member em on em.member_id = eauv.viewUserId "
				+ " where eauv.type = ? and eauv.viewUserId is not null and eauv.dataId = ? and eauv.create_time > ? and eauv.create_time <= ?";
		int totalCount = this.daoSupport.queryForInt(countSql, type,dataId,startTime,endTime);
		return new Page(0, totalCount, page.getPageSize(), userlists);
		//return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), type,dataId,startTime,endTime);
	}
	
	/**
	 * b2c的获取商品详情
	 * @param productId
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject getProductDetails(int productId, JSONObject jsonObject) throws Exception {
		String sql ="select eg.name as prodcutName, eg.intro as productIntro,eg.price as productPrice,eg.mktprice as productMktPrice,eg.specs as productSpecs,eg.buy_count as productBuyCount,eg.view_count as prodcutViewCount,eg.original as productImage,eg.enable_store as productCount,"
				+ " eb.name as brandName,eb.logo as brandImage,egc.name as catName,egc.image as catImage "
				+ " from es_goods eg left join es_brand eb on eb.brand_id = eg.brand_id left join es_goods_cat egc on egc.cat_id = eg.cat_id "
				+ " where eg.goods_id = ? and eg.productOrigin = 'weman' and eg.disabled = 0 and eg.market_enable = 1 ";
		List<Map<String,Object>> pmapList = this.daoSupport.queryForList(sql, productId);
		if(pmapList!=null&&pmapList.size()>0){
			Map<String,Object> pmap = pmapList.get(0);
			String updateUserViewSql = "update es_goods eg set eg.view_count = eg.view_count+1 where eg.goods_id = ?";
			this.daoSupport.execute(updateUserViewSql, productId);
			List<Map<String,Object>> result = this.baseDaoSupport.queryForList("select gg.original as pimage from goods_gallery gg where gg.goods_id = ?", productId);
			if(result!=null&&result.size()>0){
				String images = "";
				for(Map<String,Object> map :result){
					images = images+map.get("pimage")+",";
				}
				images=images.substring(0, images.lastIndexOf(","));
				jsonObject.put("productImages", images);
			}
			List<Map<String,Object>> list= this.daoSupport.queryForList("select * from es_product_store where goodsid=?", productId);
			Map<Integer,Integer> gcount = new HashMap<Integer,Integer>();
			if(list!=null){
				for(Map<String,Object> map:list){
					gcount.put((int)map.get("productid"), (int)map.get("enable_store"));
				}
			}
			jsonObject.put("productId", productId);
			jsonObject.put("productName", pmap.get("productName"));
			jsonObject.put("productIntro", pmap.get("productIntro"));
			jsonObject.put("productBuyCount", pmap.get("productBuyCount"));
			jsonObject.put("prodcutViewCount", pmap.get("prodcutViewCount"));
			jsonObject.put("productImage", pmap.get("productImage"));
			jsonObject.put("productCount", pmap.get("productCount"));
			JSONArray productSpecs  = JSONArray.fromObject((String)pmap.get("productSpecs"));
			JSONArray productSpecJa = new JSONArray();
			for(int i=0;i<productSpecs.size();i++){
				JSONObject productSpec = JSONObject.fromObject(productSpecs.get(i));
				JSONObject spec = new JSONObject();
				spec.put("entityId", productSpec.get("product_id"));
				spec.put("specPrice", productSpec.get("price"));
				spec.put("specSn", productSpec.get("sn"));
				spec.put("specWeight", productSpec.get("weight"));
				spec.put("specStr", productSpec.get("specs"));
				spec.put("specId", productSpec.get("specsvIdJson"));
				spec.put("specCount", gcount.get(productSpec.get("product_id")));
				productSpecJa.add(spec);
			}
			jsonObject.put("productSpecs", productSpecJa);
			jsonObject.put("productPrice", pmap.get("productPrice"));
			jsonObject.put("productMktPrice", pmap.get("productMktPrice"));
			jsonObject.put("productBrandName", pmap.get("brandName"));
			jsonObject.put("productBrandImage", pmap.get("brandImage"));
			jsonObject.put("productCatName", pmap.get("catName"));
			jsonObject.put("productCatImage", pmap.get("catImage"));
		}
		return jsonObject;
	}

	@Override
	public void updateThemeContent(int tcid, Map<String, Object> map) throws Exception {
		this.daoSupport.update("es_api_theme_content", map, "id = "+tcid);
		
	}

	@Override
	public void deleThemeContent(int tcid) throws Exception {
		this.daoSupport.execute("delete from es_api_theme_content where id = ?", tcid);//删除
	}

	@Override
	public Map<String,Object> getProductDetails(int pid) throws Exception {
		String sql = "select * from es_goods eg where eg.goods_id = ?";
		return this.daoSupport.queryForMap(sql,pid);
	}

	@Override
	public Map<String, Object> getAdminIndexDetails(Long startTime, Long endTime) throws Exception {
		Map<String,Object> resMap = new HashMap<String,Object>();
		String themeCount = "select sum(viewCount) from es_api_user_view eauv where eauv.type = 'clickTheme' ";
		themeCount = themeCount +" and create_time > "+startTime+" and create_time < "+endTime;
		int totalThemeCount = this.baseDaoSupport.queryForInt(themeCount);
		resMap.put("themeCount", totalThemeCount);
		String themeCount2 = "select sum(viewCount) as themeCount from es_api_user_view eauv where eauv.type = 'clickTheme' and viewUserId is null";
		themeCount2 = themeCount2 +" and create_time > "+startTime+" and create_time < "+endTime;
		int themeCountNoLogin =  this.baseDaoSupport.queryForInt(themeCount2);
		resMap.put("themeCountNoLogin",themeCountNoLogin);
		resMap.put("themeCountLogin", totalThemeCount-themeCountNoLogin);
		String themeLoveCount = "select count(*) from wh_api_action waa where waa.type = 2 and waa.status != -1";
		themeLoveCount = themeLoveCount +" and create_time > "+startTime+" and create_time < "+endTime;
		resMap.put("themeCountLove", this.baseDaoSupport.queryForInt(themeLoveCount));
		String articleViewCount = "select sum(viewCount) from es_api_user_view eauv where eauv.type = 'clickArticle' ";
		articleViewCount = articleViewCount +" and create_time > "+startTime+" and create_time < "+endTime;
		resMap.put("articleCount", this.daoSupport.queryForInt(articleViewCount));
		String articleLoveCount = "select count(*) from wh_api_action waa where waa.type = 4 and waa.status != -1";
		articleLoveCount = articleLoveCount +" and create_time > "+startTime+" and create_time < "+endTime;
		resMap.put("articleCountLove", this.daoSupport.queryForInt(articleLoveCount));
		String articleCommentCount = "select count(*) from wh_api_comment wac where wac.status != -1";
		articleCommentCount = articleCommentCount +" and create_time > "+startTime+" and create_time < "+endTime;
		resMap.put("articleCountComment", this.daoSupport.queryForInt(articleCommentCount));
		String addUserCount = "select count(*) from es_member em where 1=1 ";
		addUserCount = addUserCount +" and regtime > "+startTime/1000+" and regtime < "+endTime/1000;
		resMap.put("addUserCount", this.daoSupport.queryForInt(addUserCount));
		return resMap;
	}

	@Override
	public List<Map<String, Object>> getBrowseThemeList() throws Exception {
		String sql ="select * from es_api_theme eat where eat.startTime > ? and eat.startTime < ?";
		return this.baseDaoSupport.queryForList(sql, new Date().getTime()-1000*60*60*2,new Date().getTime()+1000*60*60*24*2);
	}

	/**
	 * 首页接口   version2.0
	 * @param pageNo
	 * @param i
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public Page getThemeProductsAPPVersion2(int pageNo, int pageSize, Map<String, String> map) throws Exception {
		String sql="select at.id as id,at.contentStyle as contentStyle,at.image as image "
				+ " from es_api_theme at "
				+ " where at.status = '1' and at.startTime < ? "
				+ " and exists (select 1 from es_api_theme_content eatc where eatc.theme_id = at.id and eatc.type = 'product' and eatc.isIndexShow = 1 ) ";
		if(map!=null){
			if(map.containsKey("indexStatus")){
				sql = sql +" and at.indexStatus = 1 ";
			}
			if(map.containsKey("contentStyle")){
				sql = sql +" and at.contentStyle = '" + map.get("contentStyle")+"'";
			}
			if(map.containsKey("typeId")){
				int typeId = Integer.parseInt((String)map.get("typeId"));
				if(typeId==0){
					sql = sql +" and at.id in (select eatt.themeid from es_api_theme_themetag eatt where eatt.themetagkeyid = "+typeId +" )";
				}else{
					sql = sql +" and at.id in (select eatt.themeid from es_api_theme_themetag eatt where eatt.themetagvalueid = "+typeId +" )";
				}
			}
		}
		sql = sql +" order by at.startTime desc";
		List<Map<String,Object>> themeList = this.daoSupport.queryForListPage(sql, pageNo, pageSize,new Date().getTime());
		String countSql = "SELECT COUNT(*) from es_api_theme where status =1 and startTime < ? and contentStyle = '" + map.get("contentStyle")+"' and indexStatus = 1 ";
		int totalCount = this.daoSupport.queryForInt(countSql,new Date().getTime());
		return new Page(0, totalCount, pageSize, themeList);
	}

	
	public List<Map<String,Object>> getThemeContentByThemeIds(List<Integer> themeIds){
		String sqlContent = "select eac.theme_id as tid,eg.productOrigin as productOrigin,eg.brief as pbrief,eg.url as purl,eg.name as pname,eg.original as pimage,eg.price as pprice,eg.goods_id as pid "
				+ " from es_api_theme_content eac  "
				+ " left join es_goods eg on eg.goods_id = eac.goods_id where 1=1 "
				+ " and eac.type = 'product' and eac.isIndexShow = 1  ";
		if(themeIds.size()>0){
			String ids = "";
			for(int themeIda :themeIds){
				if(ids.equals("")){
					sqlContent = sqlContent +" and (eac.theme_id = "+themeIda;
					ids = "1";
				}else{
					sqlContent = sqlContent +" or eac.theme_id = "+themeIda;
				}
			}
			sqlContent = sqlContent +") ";
		}
		sqlContent = sqlContent + " order by position desc";
		List<Map<String,Object>> themeContentList = this.daoSupport.queryForList(sqlContent);
		return themeContentList;
	}
	
	@Override
	public Page getUserActionProduct(String pageNo, int i, Map<String, Object> maps) throws Exception {
		String sql ="select * from es_goods at where 1=1 ";
		if(maps!=null){
			if(maps.containsKey("startTime")){
				sql = sql +" and at.startTime > "+ (long)maps.get("startTime");
			}
			if(maps.containsKey("endTime")){
				sql = sql +" and at.startTime < "+ (long)maps.get("endTime");
			}
			if(maps.containsKey("keywords")&&!"".equals(maps.get("keywords"))){
				sql = sql +" and at.name like '%"+maps.get("keywords")+"%' ";
			}
			if(maps.containsKey("orderBy")){
				sql = sql +" order by at."+maps.get("orderBy")+" desc ";
			}else{
				sql = sql +" order by at.startTime desc ";
			}
		}
		return this.daoSupport.queryForPage(sql, Integer.parseInt(pageNo), 10);
	}


	

}
