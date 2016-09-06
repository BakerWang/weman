package com.enation.app.api.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.enation.app.api.dao.ThemeProductDao;
import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.Theme;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
@Repository
public class ThemeProductDaoImpl extends BaseSupport implements ThemeProductDao{

	@Override
	public void productJoinTheme(int good_id, int theme_id, int position,String type) {
		Map<String,Object> pvalues = new HashMap<String,Object>();
		pvalues.put("goods_id", good_id);
		pvalues.put("theme_id", theme_id);
		pvalues.put("product_position", position);
		pvalues.put("create_time", new Date().getTime());
		pvalues.put("type", type);
		this.daoSupport.insert("es_api_theme_product", pvalues);
		this.daoSupport.execute("update es_api_theme at set product_count= (product_count+1) where at.id = ? ", theme_id);
	}
	
	public void themeDelProduct(int good_id, int theme_id){
		this.daoSupport.execute("delete from es_api_theme_product where theme_id = ? and goods_id = ?", theme_id,good_id);
		this.daoSupport.execute("update es_api_theme at set product_count= (product_count-1) where at.id = ? ", theme_id);
	}

	@Override
	public void loveTheme(int member_id, int theme_id) {
		Map<String,Object> pvalues = new HashMap<String,Object>();
		pvalues.put("member_id", member_id);
		pvalues.put("theme_id", theme_id);
		pvalues.put("create_time", new Date().getTime());
		this.daoSupport.insert("es_api_theme_love", pvalues);
		this.daoSupport.execute("update es_api_theme at set love_count= (love_count+1) where at.id = ? ", theme_id);
	}
	
	@Override
	public Page themeProductList(int pageNo,int pageSize,Map<String,String> map) {
		String sql="select at.id as id,at.title as title,at.image as image,at.details details,at.love_count as lc,at.create_time as createTime,at.status as status,at.product_count as pcount,"
				+ "gds.goods_id as gid,gds.name as gname,gds.price as gprice,gds.small as gimage,gds.brief as gbrief "
				+ "from es_api_theme at "
				+ "left join es_api_theme_product atp on atp.theme_id=at.id "
				+ "left join es_goods gds on atp.goods_id=gds.goods_id where 1=1 ";
		if(map!=null&&map.size()>0){
			String status = map.get("status");
			if(status!=null&&("1".equals(status)||"-1".equals(status))){
				sql = sql+" and at.status = "+status;
			}
			String keywords = map.get("keywords");
			if(keywords!=null&&!"".equals(keywords)){
				sql = sql+" and (at.title like '%"+keywords+"%')";
			}
		}
		sql = sql+" order by at.create_time desc, atp.product_position asc";
		List<Map> themeList = this.daoSupport.queryForListPage(sql, pageNo, pageSize);
		List<ThemeProduct> themeProducts = new ArrayList<ThemeProduct>();
		int cthemeid = 0;
		ThemeProduct tps = null;
		String themeIdsStr = "";
		for(Map resObj:themeList){
			Goods goods = null;
			Integer gid = (Integer)resObj.get("gid");
			if(gid!=null&&gid!=0){
				goods = new Goods();
				goods.setGoods_id((Integer)resObj.get("gid"));
				goods.setName((String)resObj.get("gname"));
				goods.setPrice((Double)resObj.get("gprice"));
				goods.setSmall((String)resObj.get("gimage"));
				goods.setIntro((String)resObj.get("gbrief"));
			}
			int themeid = (Integer)resObj.get("id");
			if("".equals(themeIdsStr)){
				themeIdsStr = themeIdsStr+themeid;
			}else{
				themeIdsStr = themeIdsStr+","+themeid;
			}
			if(themeid != cthemeid){
				tps = new ThemeProduct();
				Theme thm = new Theme();
				thm.setId(themeid);
				thm.setImage((String)resObj.get("image"));
				thm.setLove_count((Integer)resObj.get("lc"));
				thm.setDetails((String)resObj.get("details"));
				thm.setTitle((String)resObj.get("title"));
				thm.setCreate_time((Long)resObj.get("createTime"));
				thm.setStatus((String)resObj.get("status"));
				thm.setProduct_count((Integer)resObj.get("pcount"));
				tps.setTheme(thm);
				List<Goods> goodss = new ArrayList<Goods>();
				if(goods!=null){
					goodss.add(goods);
				}
				tps.setGoodList(goodss);
				themeProducts.add(tps);
			}else{
				tps.getGoodList().add(goods);
			}
			cthemeid = themeid;
		}
		String countSql = "SELECT COUNT(*) from es_api_theme";
		int totalCount = this.daoSupport.queryForInt(countSql);
		String themetagSql = "select themeid as tid, themetagkeyid as akey,themetagvalueid as bvalue "
				+ "from es_api_theme_themetag att where att.themeid in ( "+themeIdsStr+" ) ";
		List<Map> themetags = this.daoSupport.queryForList(themetagSql);
		for(Map obj:themetags){
			int themeid = (Integer)obj.get("tid");
			int keyid = (Integer)obj.get("akey");
			int valueid = (Integer)obj.get("bvalue");
			for(ThemeProduct tp:themeProducts){
				if(tp.getTheme().getId()==themeid){
					if(tp.getTheme().getThemetagList()==null||tp.getTheme().getThemetagList().size()==0){
						Map<Integer,Integer> ttl = new HashMap<Integer,Integer>();
						ttl.put(keyid, valueid);
						tp.getTheme().setThemetagList(ttl);
					}else{
						tp.getTheme().getThemetagList().put(keyid, valueid);
					}
					System.out.println(keyid+"||"+valueid);
					break;
				}
			}
		}
		return new Page(0, totalCount, pageSize, themeProducts);
	}

	@Override
	public ThemeProduct catchThemeDetails(int themeId) {
		String sql="select at.id as id,at.title as title,at.image as image,at.details details,at.love_count as lc,at.create_time as createTime, atp.type as atype,"
				+ "gds.goods_id as gid,gds.name as gname,gds.price as gprice,gds.small as gimage,gds.brief as gbrief,gds.view_count as gviewcount "
				+ "from es_api_theme at "
				+ "left join es_api_theme_product atp on atp.theme_id=at.id "
				+ "left join es_goods gds on atp.goods_id=gds.goods_id  "
				+ "where at.id = ?";
		
		List<Map> themeList = this.daoSupport.queryForList(sql, themeId);
		ThemeProduct tps = null;
		List<Goods> goodss = new ArrayList<Goods>();
		List<Goods> detailsGoodss = new ArrayList<Goods>();
		int index = 0;
		for(Map resObj:themeList){
			Goods goods = null;
			Integer gid = (Integer)resObj.get("gid");
			if(gid!=null&&gid!=0){
				goods = new Goods();
				goods.setGoods_id(gid);
				goods.setName((String)resObj.get("gname"));
				goods.setPrice((Double)resObj.get("gprice"));
				goods.setSmall((String)resObj.get("gimage"));
				goods.setIntro((String)resObj.get("gbrief"));
				goods.setView_count((Integer)resObj.get("gviewcount"));
				int atype = (Integer)resObj.get("atype");
				if(atype==1){
					goodss.add(goods);
				}else{
					detailsGoodss.add(goods);
				}
			}
			int themeid = (Integer)resObj.get("id");
			if(index==0){
				tps = new ThemeProduct();
				Theme thm = new Theme();
				thm.setId(themeid);
				thm.setImage((String)resObj.get("image"));
				thm.setLove_count((Integer)resObj.get("lc"));
				thm.setDetails((String)resObj.get("details"));
				thm.setTitle((String)resObj.get("title"));
				thm.setCreate_time((Long)resObj.get("createTime"));
				tps.setTheme(thm);
			}
		}
		tps.setGoodList(goodss);
		tps.setDetailsGoodList(detailsGoodss);
		return tps;
	}

	@Override
	public Theme updateTheme(int themeId,Map<String,Object> maps) {
		this.daoSupport.update("es_api_theme", maps, "id = "+themeId);
		return null;
	}

}
