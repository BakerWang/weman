package com.enation.app.b2b2c.core.service.cart.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.model.goods.StoreGoods;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.shop.core.model.support.DiscountPrice;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.util.CurrencyUtil;
@Component
public class StoreCartManager extends BaseSupport implements IStoreCartManager {
	private CartPluginBundle cartPluginBundle;
	private IDlyTypeManager dlyTypeManager;
	private IPromotionManager promotionManager;
	private IStoreGoodsManager storeGoodsManager;
	private IStoreMemberManager storeMemberManager;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.cart.IStoreCartManager#listGoods(java.lang.String)
	 */
	@Override
	public List<StoreCartItem> listGoods(String sessionid) {
		StringBuffer sql = new StringBuffer();

		sql.append("select s.store_id as store_id,s.store_name as store_name,p.weight AS weight,c.cart_id as id,g.goods_id,g.thumbnail as image_default,c.name ,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.addon, (c.num*c.price) as coupPrice from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ,"+this.getTableName("store")+" s ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?  AND c.store_id=s.store_id");
		//System.out.println(sql);
		List list  =this.daoSupport.queryForList(sql.toString(), StoreCartItem.class, sessionid);
		
		cartPluginBundle.filterList(list, sessionid);
	

		return list;
	}
	
	@Override
	public OrderPrice countPrice(List<StoreCartItem> storeCart,String regionid, Integer shippingid, Boolean isProtected,Map map) {
		OrderPrice orderPrice = new OrderPrice();
		//计算商品重量
		Double weight=0.0;
		//计算商品原始价格
		Double originalPrice=0.0;
		//订单总价格
		Double  orderTotal = 0d;
		//配送费用
		Double dlyPrice = 0d; //如果没有计算配送信息，为0
		//获取会员
		StoreMember member  = storeMemberManager.getStoreMember();
		
		for (StoreCartItem storeCartItem : storeCart) {
			StoreGoods goods = this.storeGoodsManager.getGoods(storeCartItem.getGoods_id());
			if(goods.getGoods_transfee_charge()==0){
				weight=CurrencyUtil.add(weight, CurrencyUtil.mul(goods.getWeight(), storeCartItem.getNum()));
			}
			//重量
			//价格
			originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
		}
		
		/**
		 * -------------------------------
		 * 如果传递了配送信息，计算配送费用
		 * -------------------------------
		 * 
		 */
		if(regionid!=null &&shippingid!=null &&isProtected!=null  ){
			if(shippingid!=0 && weight!=0d ){
				//计算原始配置送费用
				Double[] priceArray = this.dlyTypeManager.countPrice(shippingid, weight, originalPrice, regionid);
				dlyPrice = priceArray[0];//费送费用
			}
		}
		/**
		 * ---------------------------------
		 * 设置订单的各种费用项
		 * ---------------------------------
		 */
		
		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
		orderTotal = CurrencyUtil.add(originalPrice, dlyPrice); 
		
		orderPrice.setGoodsPrice(originalPrice); //商品金额，优惠后的
		orderPrice.setShippingPrice(dlyPrice);
		//积分设置为0
		orderPrice.setPoint(0); 
		orderPrice.setOriginalPrice(originalPrice);
		orderPrice.setOrderPrice(orderTotal);
		orderPrice.setWeight(weight);
		orderPrice.setNeedPayMoney(orderTotal);
		orderPrice  = this.cartPluginBundle.coutPrice(orderPrice,map);
		return orderPrice;
	}
	
//	@Override
//	public OrderPrice countPrice(List<StoreCartItem> storeCart,String regionid, String[] shippingId, Boolean isProtected,Map map) {
//		OrderPrice orderPrice = new OrderPrice();
//		//计算商品重量
//		Double weight=0.0;
//		//计算商品原始价格
//		Double originalPrice=0.0;
//		//订单总价格
//		Double  orderTotal = 0d;
//		//配送费用
//		Double dlyPrice = 0d; //如果没有计算配送信息，为0
//		//优惠后的订单价格,默认为商品原始价格
//		Double coupPrice =0.0; 
//		//获取会员
//		StoreMember member  = storeMemberManager.getStoreMember();
//		
//		if(member==null){coupPrice=originalPrice;}
//		for (StoreCartItem storeCartItem : storeCart) {
//			
//			StoreGoods goods = this.storeGoodsManager.getGoods(storeCartItem.getGoods_id());
//			
//			weight=CurrencyUtil.add(weight, CurrencyUtil.mul(goods.getWeight(), storeCartItem.getNum()));
//			originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
//			if(member!=null){
//				coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(storeCartItem.getPrice(), storeCartItem.getNum()));
//			}
//		}
//		
//		/**
//		 * -------------------------------
//		 * 如果传递了配送信息，计算配送费用
//		 * -------------------------------
//		 * 
//		 */
//		if(regionid!=null &&isProtected!=null  ){
//			if(shippingId.length>0){
//				Double dlyPriceTotal = 0.0d;
//				for(int i=0;i<shippingId.length;i++){
//					//计算原始配置送费用
//					Double[] priceArray = this.dlyTypeManager.countPrice(Integer.valueOf(shippingId[i]), weight, originalPrice, regionid);
//					dlyPrice = priceArray[0];//配送费用
//					
//					if(member!=null){ //计算会员优惠
//						//对订单价格和积分执行优惠 			积分设置为0
//						DiscountPrice discountPrice  = this.promotionManager.applyOrderPmt(coupPrice, dlyPrice,0, member.getLv_id()); 
//						coupPrice=discountPrice.getOrderPrice() ; //优惠会后订单金额
//						dlyPrice = discountPrice.getShipFee(); //优惠后的配送费用
//					}
//					dlyPriceTotal = CurrencyUtil.add(dlyPriceTotal, dlyPrice);
//				}
//				dlyPrice = dlyPriceTotal;
//			}
//			//去除计算保价费用
//		}
//		
//		/**
//		 * ---------------------------------
//		 * 设置订单的各种费用项
//		 * ---------------------------------
//		 */
//		//打折金额：原始的商品价格-优惠后的商品金额
//		Double reducePrice = CurrencyUtil.sub(originalPrice , coupPrice);
//		
//		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
//		orderTotal = CurrencyUtil.add(coupPrice, 21.00); 
//		
//		orderPrice.setDiscountPrice(reducePrice); //优惠的金额
//		orderPrice.setGoodsPrice(coupPrice); //商品金额，优惠后的
//		orderPrice.setShippingPrice(21.00);
//		//积分设置为0
//		orderPrice.setPoint(0); 
//		orderPrice.setOriginalPrice(originalPrice);
//		orderPrice.setOrderPrice(orderTotal);
//		orderPrice.setWeight(weight);
//		orderPrice.setNeedPayMoney(orderTotal);// 需支付的金额默认为订单总金额
//		orderPrice  = this.cartPluginBundle.coutPrice(orderPrice,null);
//		return null;
//	}
	
	@Override
	public List<Map> storeListGoods(String sessionid) {
		List<Map> storeGoodsList= new ArrayList<Map>();
		List<StoreCartItem> goodsList =new ArrayList();
		
		goodsList= this.listGoods(sessionid); //商品列表
		for (StoreCartItem item : goodsList) {
			findStoreMap(storeGoodsList, item);
		}
		return storeGoodsList;
	}
	/**
	 * 获取店铺商品列表
	 * @param storeGoodsList
	 * @param map
	 * @param StoreCartItem
	 * @return list<Map>
	 */
	private void findStoreMap(List<Map> storeGoodsList,StoreCartItem item){
		int is_store=0;
		if (storeGoodsList.isEmpty()){
			addGoodsList(item, storeGoodsList);
		}else{
			for (Map map: storeGoodsList) {
				if(map.containsValue(item.getStore_id())){
					List list=(List) map.get("goodslist");
					list.add(item);
					is_store=1;
				}
			}
			if(is_store==0){
				addGoodsList(item, storeGoodsList);
			}
		}
	}
	/**
	 * 添加至店铺列表
	 * @param item
	 * @param storeGoodsList
	 */
	private void addGoodsList(StoreCartItem item,List<Map> storeGoodsList){
		Map map=new HashMap();
		List list=new ArrayList();
		list.add(item);
		map.put("store_id", item.getStore_id());
		map.put("store_name", item.getStore_name());
		map.put("goodslist", list);
		storeGoodsList.add(map);
	}
	@Override
	public void  clean(String sessionid){
		String sql ="delete from cart where session_id=?";
		
		this.baseDaoSupport.execute(sql, sessionid);
	}
	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}
	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}
	public IPromotionManager getPromotionManager() {
		return promotionManager;
	}
	public void setPromotionManager(IPromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}
	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}
	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}
	public CartPluginBundle getCartPluginBundle() {
		return cartPluginBundle;
	}
	public void setCartPluginBundle(CartPluginBundle cartPluginBundle) {
		this.cartPluginBundle = cartPluginBundle;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
}
