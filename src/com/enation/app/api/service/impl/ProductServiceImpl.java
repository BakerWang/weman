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
	
	public Page getProductList(int pageNo,int pageSize,Map<String,String> map) {
		String sql ="select g.* from es_goods g where g.disabled=0 and g.market_enable=1 order by g.create_time desc";
		//Page page = storeGoodsManager.b2b2cGoodsList(pageNo,pageSize,map);
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
		String sql="select at.id as id,at.image as image,at.title as title,at.details as details,eatt.image as tagImage "
				+ " from es_api_theme at "
				+ " left join es_api_theme_tag eatt on eatt.id = at.theme_tag_id "
				+ " where at.status = '1' ";
		if(map!=null){
			if(map.containsKey("indexStatus")){
				sql = sql +" and at.indexStatus = 1 ";
			}
		}
		sql = sql +" order by at.id desc";
		List<Map<String,Object>> themeList = this.daoSupport.queryForListPage(sql, pageNo, pageSize);
		String countSql = "SELECT COUNT(*) from es_api_theme where status =1 ";
		int totalCount = this.daoSupport.queryForInt(countSql);
		return new Page(0, totalCount, pageSize, themeList);
	}
	/**
	 * 后台主题列表
	 * @param pageNo
	 * @param pageSize
	 * @param map    
	 * @return
	 */
	public Page getThemeProducts(int pageNo, int pageSize, Map<String, String> map) {
		String sql="select at.id as id,at.minorImage as minorImage,at.image as image,at.title as title,at.details as details,at.indexStatus as indexStatus,at.findStatus as findStatus,at.recommendStatus as recommendStatus,at.loginClickCount as loginClickCount,at.clickCount as clickCount,at.create_time as createTime "
				+ " from es_api_theme at "
				+ " where at.status = '1' ";
		if(map!=null){
			if(map.containsKey("indexStatus")){
				sql = sql +" and at.indexStatus = 1 ";
			}
			if(map.containsKey("findStatus")){
				sql = sql +" and at.findStatus = 1 ";
			}
			if(map.containsKey("recommendStatus")){
				sql = sql +" and at.recommendStatus = 1 ";
			}
			if(map.containsKey("themeId")){
				sql = sql +" and at.id != "+map.get("themeId");
			}
			if(map.containsKey("keywords")){
				sql = sql +" and at.title like '%"+map.get("keywords")+"%' ";
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
		sql = sql+" order by at.id desc";
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
		int totalCount = this.daoSupport.queryForInt(countSql);
		
		return new Page(0, totalCount, pageSize, themeProducts);
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
		theme.setLove_count(0);
		theme.setProduct_count(0);
		theme.setStatus("1");
		theme.setCreate_time(new Date().getTime());
		theme.setIndexStatus(-1);
		theme.setFindStatus(-1);
		theme.setRecommendStatus(1);
		theme.setClickCount(0);
		theme.setLove_count(0);
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
		String sql="select * from es_api_theme_tag where status =1 ";
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
			Map<String, Object> themetag = new HashMap<String,Object>();
			themetag.put("themetagvalueid", valueId);
			themetag.put("create_time", new Date().getTime());
			this.daoSupport.update("es_api_theme_themetag", themetag, "themeid = "+themeId +" and themetagkeyid =" + keyId);
		}
		String tagSql = "select eatt.name as tagname from es_api_theme_tag eatt where eatt.id = ? and eatt.status = 1";
		List<Map<String, Object>> restag = this.daoSupport.queryForList(tagSql, keyId);
		if(restag!=null&&restag.size()>0){
			String tagname = (String)restag.get(0).get("tagname");
			if("身型".equals(tagname)){
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
		String contentSql="select eatc.*,eg.name as productName,eg.original as productImage,eatc.status as status,eg.price as productPrice,eg.mktprice as productMkPrice,eg.intro as intro,eg.url as url ";
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
			String isExistsSql ="select count(*) from es_api_user_view eauv where eauv.type = 'clickTheme' and eauv.dataId = ? and eauv.status = 1 and eauv.viewUserId = ?";
			int count = this.daoSupport.queryForInt(isExistsSql, theme.getId(),memberId);
			if(count>0){
				String updateUserViewSql = "update es_api_user_view eauv set eauv.viewCount = eauv.viewCount+1 where eauv.type = 'clickTheme' and eauv.dataId = ? and eauv.status = 1 and eauv.viewUserId = ?";
				this.daoSupport.execute(updateUserViewSql, theme.getId(),memberId);
			}else{
				Map<String,Object> userView = new HashMap<String,Object>();
				userView.put("viewUserId", memberId);
				userView.put("dataId", theme.getId());
				userView.put("type", "clickTheme");
				userView.put("status", "1");
				userView.put("viewCount", "1");
				userView.put("create_time", new Date().getTime());
				this.daoSupport.insert("es_api_user_view", userView);
			}
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
		map.put("market_enable", 1);
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
	public Page userThemeCount(Long startTime, Long endTime, int dataId, String type,Page page) {
		String sql ="select em.uname as username,eauv.viewCount as viewCount,eauv.dataId as dataId from es_api_user_view eauv "
				+ " left join es_member em on em.member_id = eauv.viewUserId "
				+ " where eauv.type = ? and eauv.dataId = ? and eauv.create_time >= ? and eauv.create_time <= ?";
		return this.daoSupport.queryForPage(sql, (int)page.getCurrentPageNo(), page.getPageSize(), type,dataId,startTime,endTime);
	}


	

}
