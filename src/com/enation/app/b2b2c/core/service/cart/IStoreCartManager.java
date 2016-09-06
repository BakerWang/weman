package com.enation.app.b2b2c.core.service.cart;

import java.util.List;
import java.util.Map;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
/**
 * 店铺购物车管理类
 * @author LiFenLong
 *
 */
public interface IStoreCartManager {
	public static final String FILTER_KEY = "cartFilter"; 
	/**
	 * 获取购物车商品列表
	 * @param sessionid
	 * @return List<StoreCartItem>
	 */
	public List<StoreCartItem> listGoods(String sessionid);
	/**
	 * 计算订单价格(子订单)
	 * @param storeCart
	 * @param region_id
	 * @param shippingId
	 * @param isProtected
	 * @return
	 */
	public OrderPrice countPrice(List<StoreCartItem> storeCart,String region_id,Integer shippingId,Boolean isProtected,Map map);
	
//	public OrderPrice countPrice(List<StoreCartItem> storeCart,String region_id,String[] shippingId,Boolean isProtected,Map map);
	/**
	 * 获取分店铺购物车列表
	 * @param sessionid
	 * @return
	 */
	public List<Map> storeListGoods(String sessionid);
	/**
	 * 清除购物车
	 * @param sessionid
	 */
	public void  clean(String sessionid);
}
