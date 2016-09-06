package com.enation.app.b2b2c.core.model.cart;

import com.enation.app.shop.core.model.support.CartItem;
/**
 * b2b2c 购物车商品
 * @author LiFenLong
 *
 */
public class StoreCartItem extends CartItem{
	private Integer store_id;	//店铺Id
	private String store_name;	//店铺名称
	public Integer getStore_id() {
		return store_id;
	}
	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public String getStore_name() {
		return store_name;
	}
	public void setStore_name(String store_name) {
		this.store_name = store_name;
	}
}
