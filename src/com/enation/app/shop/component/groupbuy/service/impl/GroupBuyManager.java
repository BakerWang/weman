package com.enation.app.shop.component.groupbuy.service.impl;


import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.groupbuy.model.GroupBuy;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.core.model.Goods;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
@Component
public class GroupBuyManager implements IGroupBuyManager{
	private IDaoSupport daoSupport;
	private IGroupBuyActiveManager groupBuyActiveManager;
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#add(com.enation.app.shop.component.groupbuy.model.GroupBuy)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(GroupBuy groupBuy) {
		groupBuy.setAdd_time(DateUtil.getDateline());
		this.daoSupport.insert("es_groupbuy_goods", groupBuy);
		return this.daoSupport.getLastId("es_groupbuy_goods");
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#update(com.enation.app.shop.component.groupbuy.model.GroupBuy)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(GroupBuy groupBuy) {
		this.daoSupport.update("es_groupbuy_goods", groupBuy, "gb_id="+groupBuy.getGb_id());		
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#delete(int)
	 */
	@Override
	public void delete(int gbid) {
		String sql="delete from  es_groupbuy_goods where gb_id=?";
		this.daoSupport.execute(sql, gbid);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#auth(int, int)
	 */
	@Override
	public void auth(int gbid, int status) {
		String sql="update es_groupbuy_goods set gb_status=? where gb_id=?";
		this.daoSupport.execute(sql, status,gbid);
		//获取审核的团购
		//更改商品为团购商品
		GroupBuy groupBuy=(GroupBuy)this.daoSupport.queryForObject("select * from es_groupbuy_goods where gb_id=?", GroupBuy.class, gbid);
		sql="update es_goods set is_groupbuy=1 where goods_id=?";
		this.daoSupport.execute(sql, groupBuy.getGoods_id());
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#listByActId(int, int, int, java.lang.Integer)
	 */
	@Override
	public Page listByActId(int page, int pageSize, int actid, Integer status) {
		StringBuffer sql = new StringBuffer();
		sql.append("select g.*,a.act_name,a.start_time,a.end_time from es_groupbuy_goods g ,es_groupbuy_active a ");
		sql.append(" where g.act_id= ? and  g.act_id= a.act_id");
		if(status!=null ){
			sql.append(" and g.gb_status="+status);
		}
		sql.append(" order by add_time ");
		return this.daoSupport.queryForPage(sql.toString(),page,pageSize, actid);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#search(int, int, java.lang.Integer, java.lang.Double, java.lang.Double, int, int, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page search(int page, int pageSize, Integer catid, Double minprice,
			Double maxprice, Integer sort_key, Integer sort_type, Integer search_type,Integer area_id) {
		StringBuffer sql= new StringBuffer("select * from es_groupbuy_goods where gb_status=1 ");
		if(catid!=0){
		sql.append(" and cat_id="+catid);
		}
		
		if(minprice!=null){
			sql.append(" and price>="+minprice);
		}
		
		
		if(maxprice!=null&&maxprice!=0){
			sql.append(" and price<="+maxprice);
		}
		
		if(sort_type==0){
			sql.append(" and act_id in (select act_id from es_groupbuy_active where end_time <"+DateUtil.getDateline()+")");
		}
	
		//
		if(sort_type==1&&catid==0){
			sql.append(" and act_id="+groupBuyActiveManager.get().getAct_id());
		}
		
		if(sort_type==1&&catid!=0){
			sql.append(" and act_id="+groupBuyActiveManager.get().getAct_id());
		}
		if(sort_type==2&&catid!=0){
			sql.append(" and act_id in (select act_id from es_groupbuy_active where start_time >"+DateUtil.getDateline()+")");
		}
		
		if(sort_type==2&&catid==0){
			sql.append(" and act_id in (select act_id from es_groupbuy_active where start_time >"+DateUtil.getDateline()+")");
		}
		if(area_id!=0){
			sql.append(" and area_id="+area_id);
		}
		if(sort_key==0){
			sql.append(" order by add_time ");
		}
		
		if(sort_key==1){
			sql.append(" order by price ");
		}
		
		if(sort_key==2){
			sql.append(" order by price/original_price ");
		}
		
		if(sort_key==3){
			sql.append(" order by buy_num ");
		}
		
//		System.out.println(sql.toString());
		return this.daoSupport.queryForPage(sql.toString(), page, pageSize);
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#get(int)
	 */
	@Override
	public GroupBuy get(int gbid) {
		String sql ="select * from es_groupbuy_goods where gb_id=?";
		GroupBuy groupBuy = (GroupBuy)this.daoSupport.queryForObject(sql, GroupBuy.class, gbid);
		sql="select * from es_goods where goods_id=?";
		
		Goods goods  = (Goods)this.daoSupport.queryForObject(sql, Goods.class, groupBuy.getGoods_id());
		groupBuy.setGoods(goods);
		
		return groupBuy;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.shop.component.groupbuy.service.IGroupBuyManager#getBuyGoodsId(int)
	 */
	@Override
	public GroupBuy getBuyGoodsId(int goodsId) {
		String sql="SELECT * from es_groupbuy_goods WHERE goods_id=?";
		GroupBuy groupBuy= (GroupBuy)this.daoSupport.queryForObject(sql, GroupBuy.class, goodsId);
		
		groupBuy.setGoods((Goods)this.daoSupport.queryForObject("select * from es_goods where goods_id=?", Goods.class, groupBuy.getGoods_id()));
		return  groupBuy;
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public IGroupBuyActiveManager getGroupBuyActiveManager() {
		return groupBuyActiveManager;
	}

	public void setGroupBuyActiveManager(
			IGroupBuyActiveManager groupBuyActiveManager) {
		this.groupBuyActiveManager = groupBuyActiveManager;
	}
}
