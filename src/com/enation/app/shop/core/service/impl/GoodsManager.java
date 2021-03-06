package com.enation.app.shop.core.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.core.model.Cat;
import com.enation.app.shop.core.model.Goods;
import com.enation.app.shop.core.model.GoodsStores;
import com.enation.app.shop.core.model.support.GoodsEditDTO;
import com.enation.app.shop.core.plugin.goods.GoodsDataFilterBundle;
import com.enation.app.shop.core.plugin.goods.GoodsPluginBundle;
import com.enation.app.shop.core.service.IDepotMonitorManager;
import com.enation.app.shop.core.service.IGoodsCatManager;
import com.enation.app.shop.core.service.IGoodsManager;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IMemberPriceManager;
import com.enation.app.shop.core.service.IPackageProductManager;
import com.enation.app.shop.core.service.ITagManager;
import com.enation.app.shop.core.service.SnDuplicateException;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * Goods业务管理
 * 
 * @author kingapex 2010-1-13下午12:07:07
 */
@SuppressWarnings({ "rawtypes", "unchecked" })
public class GoodsManager extends BaseSupport implements IGoodsManager {
	private ITagManager tagManager;
	private GoodsPluginBundle goodsPluginBundle;
	private IPackageProductManager packageProductManager;
	private IGoodsCatManager goodsCatManager;
	private IMemberPriceManager memberPriceManager;
	private IMemberLvManager memberLvManager;
	private IDepotMonitorManager depotMonitorManager;
	private GoodsDataFilterBundle goodsDataFilterBundle;
	
	// private IDaoSupport<Goods> daoSupport;
	/**
	 * 添加商品，同时激发各种事件
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(Goods goods) {
		try {
			Map goodsMap = po2Map(goods);
			
			// 触发商品添加前事件
			goodsPluginBundle.onBeforeAdd(goodsMap);
			goodsMap.put("disabled", 0);
			goodsMap.put("create_time", DateUtil.getDateline());
			goodsMap.put("view_count", 0);
			goodsMap.put("buy_count", 0);
			goodsMap.put("last_modify", DateUtil.getDateline());
			goodsMap.put("store", 0);
		/*	
			if ("0".equals((String) goodsMap.get("have_spec"))) {              //添加商品时，默认商品库存是0，但是这是在没有规格的情况下。  如有问题，请高手优先修改这里，  whj-2015-05-21
				goodsMap.put("store", 0);
			}
		*/	
			this.baseDaoSupport.insert("goods", goodsMap);
			Integer goods_id = this.baseDaoSupport.getLastId("goods");
			goods.setGoods_id(goods_id);
			goodsMap.put("goods_id", goods_id);
			goodsPluginBundle.onAfterAdd(goodsMap);
		} catch (RuntimeException e) {
			if (e instanceof SnDuplicateException) {
				throw e;
			}
			e.printStackTrace();
		}
	}


	/**
	 * 修改商品同时激发各种事件
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void edit(Goods goods) {
		try {
			if (logger.isDebugEnabled()) {
				logger.debug("开始保存商品数据...");
			}
			Map goodsMap = this.po2Map(goods);
			 
			this.goodsPluginBundle.onBeforeEdit(goodsMap);
			this.baseDaoSupport.update("goods", goodsMap,
					"goods_id=" + goods.getGoods_id());
			String sql = "select * from es_goods where goods_id=?";
			
			goodsMap = this.daoSupport.queryForMap(sql, goods.getGoods_id());
			
			this.goodsPluginBundle.onAfterEdit(goodsMap);
			if (logger.isDebugEnabled()) {
				logger.debug("保存商品数据完成.");
			}
		} catch (RuntimeException e) {
			if (e instanceof SnDuplicateException) {
				throw e;
			}
			e.printStackTrace();
		}
	}

	/**
	 * 得到修改商品时的数据
	 * 
	 * @param goods_id
	 * @return
	 */
	
	public GoodsEditDTO getGoodsEditData(Integer goods_id) {
		GoodsEditDTO editDTO = new GoodsEditDTO();
		String sql = "select * from goods where goods_id=?";
		Map goods = this.baseDaoSupport.queryForMap(sql, goods_id);

		String intro = (String) goods.get("intro");
		if (intro != null) {
			intro = UploadUtil.replacePath(intro);
			goods.put("intro", intro);
		}

		Map<Integer, String> htmlMap = goodsPluginBundle.onFillEditInputData(goods);

		editDTO.setGoods(goods);
		editDTO.setHtmlMap(htmlMap);

		return editDTO;
	}

	/**
	 * 读取一个商品的详细<br/>
	 * 处理由库中读取的默认图片和所有图片路径:<br>
	 * 如果是以本地文件形式存储，则将前缀替换为静态资源服务器地址。
	 */

	
	public Map get(Integer goods_id) {
		String sql = "select g.*,b.name as brand_name from "
				+ this.getTableName("goods") + " g left join "
				+ this.getTableName("brand") + " b on g.brand_id=b.brand_id ";
		sql += "  where goods_id=?";
 
		Map goods = this.daoSupport.queryForMap(sql, goods_id);

		/**
		 * ====================== 对商品图片的处理 ======================
		 */
 
		String small = (String) goods.get("small");
		if (small != null) {
			small = UploadUtil.replacePath(small);
			goods.put("small", small);
		}
		String big = (String) goods.get("big");
		if (big != null) {
			big = UploadUtil.replacePath(big);
			goods.put("big", big);
		}
		 
 
		return goods;
	}

	public void getNavdata(Map goods) {
		// lzf 2011-08-29 add,lzy modified 2011-10-04
		int catid = (Integer) goods.get("cat_id");
		List list = goodsCatManager.getNavpath(catid);
		goods.put("navdata", list);
		// lzf add end
	}	

	private String getListSql(int disabled) {
		String selectSql = this.goodsPluginBundle.onGetSelector();
		String fromSql = this.goodsPluginBundle.onGetFrom();

		String sql = "select g.*,b.name as brand_name ,t.name as type_name,c.name as cat_name "
				+ selectSql
				+ " from "
				+ this.getTableName("goods")
				+ " g left join "
				+ this.getTableName("goods_cat")
				+ " c on g.cat_id=c.cat_id left join "
				+ this.getTableName("brand")
				+ " b on g.brand_id = b.brand_id and b.disabled=0 left join "
				+ this.getTableName("goods_type")
				+ " t on g.type_id =t.type_id "
				+ fromSql
				+ " where g.goods_type = 'normal' and g.disabled=" + disabled;
	
		return sql;
	}

	/**
	 * 取得捆绑商品列表
	 * 
	 * @param disabled
	 * @return
	 */
	private String getBindListSql(int disabled) {
		String sql = "select g.*,b.name as brand_name ,t.name as type_name,c.name as cat_name from "
				+ this.getTableName("goods")
				+ " g left join "
				+ this.getTableName("goods_cat")
				+ " c on g.cat_id=c.cat_id left join "
				+ this.getTableName("brand")
				+ " b on g.brand_id = b.brand_id left join "
				+ this.getTableName("goods_type")
				+ " t on g.type_id =t.type_id"
				+ " where g.goods_type = 'bind' and g.disabled=" + disabled;
		return sql;
	}	

	/**
	 * 后台搜索商品
	 * 
	 * @param params
	 *            通过map的方式传递搜索参数
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page searchBindGoods(String name, String sn, String order, int page,
			int pageSize) {

		String sql = getBindListSql(0);

		if (order == null) {
			order = "goods_id desc";
		}

		if (name != null && !name.equals("")) {
			sql += "  and g.name like '%" + name + "%'";
		}

		if (sn != null && !sn.equals("")) {
			sql += "   and g.sn = '" + sn + "'";
		}

		sql += " order by g." + order;
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);

		List<Map> list = (List<Map>) (webpage.getResult());

		for (Map map : list) {
			List productList = packageProductManager.list(Integer.valueOf(map
					.get("goods_id").toString()));
			productList = productList == null ? new ArrayList() : productList;
			map.put("productList", productList);
		}

		return webpage;
	}

	/**
	 * 读取商品回收站列表
	 * 
	 * @param name
	 * @param sn
	 * @param order
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public Page pageTrash(String name, String sn, String order, int page,
			int pageSize) {

		String sql = getListSql(1);
		if (order == null) {
			order = "goods_id desc";
		}

		if (name != null && !name.equals("")) {
			sql += "  and g.name like '%" + name + "%'";
		}

		if (sn != null && !sn.equals("")) {
			sql += "   and g.sn = '" + sn + "'";
		}

		sql += " order by g." + order;

		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);

		return webpage;
	}

	
	
	/***
	 * 库存余量提醒分页列表
	 * 
	 * @param warnTotal
	 *            总报警数
	 * @param page
	 * @param pageSize
	 */
	public List<GoodsStores> storeWarnGoods(int warnTotal, int page, int pageSize) {
		// String sql =
		// " where g.market_enable = 1 and g.goods_type = 'normal' and g.disabled= 0 order by g.goods_id desc ";
		String select_sql = "select gc.name as gc_name,b.name as b_name,g.cat_id,g.goods_id,g.name,g.sn,g.price,g.last_modify,g.market_enable,s.sumstore ";
		String left_sql = " left join " + this.getTableName("goods") + " g  on s.goodsid = g.goods_id  left join " + this.getTableName("goods_cat") + " gc on gc.cat_id = g.cat_id left join " + this.getTableName("brand") + " b on b.brand_id = g.brand_id ";
		List<GoodsStores> list = new ArrayList<GoodsStores>();

		String sql_2 = select_sql
				+ " from  (select ss.* from (select goodsid,productid,sum(store) sumstore from " + this.getTableName("product_store") + "  group by goodsid,productid   ) ss "+
				"  left join " + this.getTableName("warn_num") + " wn on wn.goods_id = ss.goodsid  where ss.sumstore <=  (case when (wn.warn_num is not null or wn.warn_num <> 0) then wn.warn_num else ?  end )  ) s  "
				+ left_sql;
		List<GoodsStores> list_2 = this.daoSupport.queryForList(sql_2, new RowMapper() {
					@Override
					public Object mapRow(ResultSet rs, int arg1)
							throws SQLException {
						GoodsStores gs = new GoodsStores();
						gs.setGoods_id(rs.getInt("goods_id"));
						gs.setName(rs.getString("name"));
						gs.setSn(rs.getString("sn"));
						gs.setRealstore(rs.getInt("sumstore"));
						gs.setPrice(rs.getDouble("price"));
						gs.setLast_modify(rs.getLong("last_modify"));
						gs.setBrandname(rs.getString("b_name"));
						gs.setCatname(rs.getString("gc_name"));
						gs.setMarket_enable(rs.getInt("market_enable"));
						gs.setCat_id(rs.getInt("cat_id"));
						return gs;
					}
				}, warnTotal);
		list.addAll(list_2);// 普通商品		

		return list;
	}

	/**
	 * 批量将商品放入回收站
	 * 
	 * @param ids
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void delete(Integer[] ids) {
		if (ids == null)
			return;

		for (Integer id : ids) {
			this.tagManager.saveRels(id, null);
		}
		String id_str = StringUtil.arrayToString(ids, ",");
		String sql = "update  goods set disabled=1  where goods_id in ("
				+ id_str + ")";

		this.baseDaoSupport.execute(sql);
	}

	/**
	 * 还原
	 * 
	 * @param ids
	 */
	public void revert(Integer[] ids) {
		if (ids == null)
			return;
		String id_str = StringUtil.arrayToString(ids, ",");
		String sql = "update  goods set disabled=0  where goods_id in ("
				+ id_str + ")";
		this.baseDaoSupport.execute(sql);
	}

	/**
	 * 清除
	 * 
	 * @param ids
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void clean(Integer[] ids) {
		if (ids == null)
			return;
		for (Integer id : ids) {
			this.tagManager.saveRels(id, null);
		}
		this.goodsPluginBundle.onGoodsDelete(ids);
		String id_str = StringUtil.arrayToString(ids, ",");
		String sql = "delete  from goods  where goods_id in (" + id_str + ")";
		this.baseDaoSupport.execute(sql);
	}

	public List list(Integer[] ids) {
		if (ids == null || ids.length == 0)
			return new ArrayList();
		String idstr = StringUtil.arrayToString(ids, ",");
		String sql = "select * from goods where goods_id in(" + idstr + ")";
		return this.baseDaoSupport.queryForList(sql);
	}

	public GoodsPluginBundle getGoodsPluginBundle() {
		return goodsPluginBundle;
	}

	public void setGoodsPluginBundle(GoodsPluginBundle goodsPluginBundle) {
		this.goodsPluginBundle = goodsPluginBundle;
	}

	/**
	 * 将po对象中有属性和值转换成map
	 * 
	 * @param po
	 * @return
	 */
	protected Map po2Map(Object po) {
		Map poMap = new HashMap();
		Map map = new HashMap();
		try {
			map = BeanUtils.describe(po);
		} catch (Exception ex) {
		}
		Object[] keyArray = map.keySet().toArray();
		for (int i = 0; i < keyArray.length; i++) {
			String str = keyArray[i].toString();
			if (str != null && !str.equals("class")) {
				if (map.get(str) != null) {
					poMap.put(str, map.get(str));
				}
			}
		}
		return poMap;
	}

	public IPackageProductManager getPackageProductManager() {
		return packageProductManager;
	}

	public void setPackageProductManager(
			IPackageProductManager packageProductManager) {
		this.packageProductManager = packageProductManager;
	}

	public Goods getGoods(Integer goods_id) {
		Goods goods = (Goods) this.baseDaoSupport.queryForObject(
				"select * from goods where goods_id=?", Goods.class, goods_id);
		return goods;
	}

	public IGoodsCatManager getGoodsCatManager() {
		return goodsCatManager;
	}

	public void setGoodsCatManager(IGoodsCatManager goodsCatManager) {
		this.goodsCatManager = goodsCatManager;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public void batchEdit() {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String[] ids = request.getParameterValues("goodsidArray");
		String[] names = request.getParameterValues("name");
		String[] prices = request.getParameterValues("price");
		String[] cats = request.getParameterValues("catidArray");
		String[] market_enable = request.getParameterValues("market_enables");
		String[] store = request.getParameterValues("store");
		String[] sord = request.getParameterValues("sord");

		String sql = "";

		for (int i = 0; i < ids.length; i++) {
			sql = "";
			if (names != null && names.length > 0) {
				if (!StringUtil.isEmpty(names[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " name='" + names[i] + "'";
				}
			}

			if (prices != null && prices.length > 0) {
				if (!StringUtil.isEmpty(prices[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " price=" + prices[i];
				}
			}
			if (cats != null && cats.length > 0) {
				if (!StringUtil.isEmpty(cats[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " cat_id=" + cats[i];
				}
			}
			if (store != null && store.length > 0) {
				if (!StringUtil.isEmpty(store[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " store=" + store[i];
				}
			}
			if (market_enable != null && market_enable.length > 0) {
				if (!StringUtil.isEmpty(market_enable[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " market_enable=" + market_enable[i];
				}
			}
			if (sord != null && sord.length > 0) {
				if (!StringUtil.isEmpty(sord[i])) {
					if (!sql.equals(""))
						sql += ",";
					sql += " sord=" + sord[i];
				}
			}
			sql = "update  goods set " + sql + " where goods_id=?";
			this.baseDaoSupport.execute(sql, ids[i]);

		}
	}

	public Map census() {
		// 计算上架商品总数
		String sql = "select count(0) from goods ";
		int allcount = this.baseDaoSupport.queryForInt(sql);
				
		// 计算上架商品总数
		sql = "select count(0) from goods where market_enable=1 and  disabled = 0";
		int salecount = this.baseDaoSupport.queryForInt(sql);

		// 计算下架商品总数
		sql = "select count(0) from goods where market_enable=0 and  disabled = 0";
		int unsalecount = this.baseDaoSupport.queryForInt(sql);

		// 计算回收站总数
		sql = "select count(0) from goods where   disabled = 1";
		int disabledcount = this.baseDaoSupport.queryForInt(sql);

		// 读取商品评论数
		sql = "select count(0) from comments where   for_comment_id is null  and commenttype='goods' and object_type='discuss'";
		int discusscount = this.baseDaoSupport.queryForInt(sql);

		// 读取商品评论数
		sql = "select count(0) from comments where for_comment_id is null  and  commenttype='goods' and object_type='ask'";
		int askcount = this.baseDaoSupport.queryForInt(sql);

		Map<String, Integer> map = new HashMap<String, Integer>(2);
		map.put("salecount", salecount);
		map.put("unsalecount", unsalecount);
		map.put("disabledcount", disabledcount);
		map.put("allcount", allcount);
		map.put("discuss", discusscount);
		map.put("ask", askcount);
		return map;
	}
 
	/**
	 * 获取某个分类的推荐商品
	 */
	public List getRecommentList(int goods_id, int cat_id, int brand_id, int num) {
		 //原美睛网代码，去掉
		return null;
	}

	public List list() {
		String sql = "select * from goods where disabled = 0";
		return this.baseDaoSupport.queryForList(sql);
	}

	public IMemberPriceManager getMemberPriceManager() {
		return memberPriceManager;
	}

	public ITagManager getTagManager() {
		return tagManager;
	}

	public void setTagManager(ITagManager tagManager) {
		this.tagManager = tagManager;
	}

	public void setMemberPriceManager(IMemberPriceManager memberPriceManager) {
		this.memberPriceManager = memberPriceManager;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	@Override
	public void updateField(String filedname, Object value, Integer goodsid) {
		this.baseDaoSupport.execute("update goods set " + filedname + "=? where goods_id=?", value, goodsid);
	}

	@Override
	public Goods getGoodBySn(String goodSn) {
		Goods goods = (Goods) this.baseDaoSupport.queryForObject("select * from goods where sn=?", Goods.class, goodSn);
		return goods;
	}

	public IDepotMonitorManager getDepotMonitorManager() {
		return depotMonitorManager;
	}

	public void setDepotMonitorManager(IDepotMonitorManager depotMonitorManager) {
		this.depotMonitorManager = depotMonitorManager;
	}
	
	@Override
	public List listByCat(Integer catid) {
		String sql = getListSql(0);
		if (catid.intValue() != 0) {
			Cat cat = this.goodsCatManager.getById(catid);
			sql += " and  g.cat_id in(";
			sql += "select c.cat_id from " + this.getTableName("goods_cat")
					+ " c where c.cat_path like '" + cat.getCat_path()
					+ "%')  ";
		}
		return this.daoSupport.queryForList(sql);
	}

	@Override
	public List listByTag(Integer[] tagid) {
		String sql = getListSql(0);
		if (tagid != null && tagid.length > 0) {
			String tagidstr = StringUtil.arrayToString(tagid, ",");
			sql += " and g.goods_id in(select rel_id from "
					+ this.getTableName("tag_rel") + " where tag_id in("
					+ tagidstr + "))";
		}
		return this.daoSupport.queryForList(sql);
	}

	@Override
	public void incViewCount(Integer goods_id) {
		this.baseDaoSupport.execute("update goods set view_count = view_count + 1 where goods_id = ?", goods_id);
	}
	
	
	public List listGoods(String catid,String tagid,String goodsnum){
		int num = 10;
		if(!StringUtil.isEmpty(goodsnum)){
			num = Integer.valueOf(goodsnum);
		}
		
		StringBuffer sql = new StringBuffer();
		if(!StringUtil.isEmpty(tagid)){
		sql.append("select g.* from " + this.getTableName("tag_rel") + " r LEFT JOIN " + this.getTableName("goods") + " g ON g.goods_id=r.rel_id where g.disabled=0 and g.market_enable=1");
		
		if(! StringUtil.isEmpty(catid) ){
			Cat cat  = this.goodsCatManager.getById(Integer.valueOf(catid));
			if(cat!=null){
				String cat_path  = cat.getCat_path();
				if (cat_path != null) {
					sql.append( " and  g.cat_id in(" ) ;
					sql.append("select c.cat_id from " + this.getTableName("goods_cat") + " ");
					sql.append(" c where c.cat_path like '" + cat_path + "%')");
				}
			}
		}
		sql.append(" AND r.tag_id="+tagid+"");
		sql.append(" order by r.ordernum desc");
		}else{
			sql.append("select * from " + this.getTableName("goods") + " where disabled=0 and market_enable=1");
			
			if(! StringUtil.isEmpty(catid) ){
				Cat cat  = this.goodsCatManager.getById(Integer.valueOf(catid));
				if(cat!=null){
					String cat_path  = cat.getCat_path();
					if (cat_path != null) {
						sql.append( " and  cat_id in(" ) ;
						sql.append("select c.cat_id from " + this.getTableName("goods_cat") + " ");
						sql.append(" c where c.cat_path like '" + cat_path + "%')");
					}
				}
			}
			sql.append(" order by goods_id desc");
		}
		//System.out.println(sql.toString());
		List list = this.daoSupport.queryForListPage(sql.toString(), 1,num);
		this.goodsDataFilterBundle.filterGoodsData(list);
		return list;
	}

	public GoodsDataFilterBundle getGoodsDataFilterBundle() {
		return goodsDataFilterBundle;
	}

	public void setGoodsDataFilterBundle(GoodsDataFilterBundle goodsDataFilterBundle) {
		this.goodsDataFilterBundle = goodsDataFilterBundle;
	}

	@Override
	public List goodsBuyer(int goods_id, int pageSize) {
		String sql = "select distinct m.* from es_order o left join es_member m " +
				"on o.member_id=m.member_id where order_id in (select order_id from es_order_items " +
				"where goods_id=?)";
		Page page = this.daoSupport.queryForPage(sql, 1, pageSize, goods_id);
		
		return (List)page.getResult();
	}

	
	@Override
	public Page searchGoods(Map goodsMap, int page, int pageSize, String other,String sort,String order) {
		String sql = creatTempSql(goodsMap, other);
		StringBuffer _sql = new StringBuffer(sql);
		this.goodsPluginBundle.onSearchFilter(_sql);
		_sql.append(" order by "+sort+" "+order);
		Page webpage = this.daoSupport.queryForPage(_sql.toString(), page,pageSize);
		return webpage;
	}

	@Override
	public List searchGoods(Map goodsMap) {
		String sql = creatTempSql(goodsMap, null);
		return this.daoSupport.queryForList(sql,Goods.class);
	}
	
	
	private String creatTempSql(Map goodsMap,String other){
		
		other = other==null?"":other;
		String sql = getListSql(0);
		Integer brandid = (Integer) goodsMap.get("brandid");
		Integer catid = (Integer) goodsMap.get("catid");
		String name = (String) goodsMap.get("name");
		String sn = (String) goodsMap.get("sn");
		Integer[]tagid = (Integer[]) goodsMap.get("tagid");
		Integer stype = (Integer) goodsMap.get("stype");
		String keyword = (String) goodsMap.get("keyword");
		String order = (String) goodsMap.get("order");
		
		if (brandid != null && brandid != 0) {
			sql += " and g.brand_id = " + brandid + " ";
		}
		
		if("1".equals(other)){
			//商品属性为不支持打折的商品
			sql += " and g.no_discount=1";
		}
		if("2".equals(other)){
			//特殊打折商品，即单独设置了会员价的商品
			sql += " and (select count(0) from " + this.getTableName("goods_lv_price") + " glp where glp.goodsid=g.goods_id) >0";
		}
		
		if(stype!=null && keyword!=null){			
			if(stype==0){
				sql+=" and ( g.name like '%"+keyword+"%'";
				sql+=" or g.sn like '%"+keyword+"%')";
			}
		}
		
		if (name != null && !name.equals("")) {
			name = name.trim();
			String[] keys = name.split("\\s");
			for (String key : keys) {
				sql += (" and g.name like '%");
				sql += (key);
				sql += ("%'");
			}
		}

		if (sn != null && !sn.equals("")) {
			sql += "   and g.sn like '%" + sn + "%'";
		}


		if (catid != null && catid!=0) {
			Cat cat = this.goodsCatManager.getById(catid);
			sql += " and  g.cat_id in(";
			sql += "select c.cat_id from " + this.getTableName("goods_cat")
					+ " c where c.cat_path like '" + cat.getCat_path()
					+ "%')  ";
		}

		if (tagid != null && tagid.length > 0) {
			String tagidstr = StringUtil.arrayToString(tagid, ",");
			sql += " and g.goods_id in(select rel_id from "
					+ this.getTableName("tag_rel") + " where tag_id in("
					+ tagidstr + "))";
		}
		//System.out.println(sql);
		return sql;
	}

}
