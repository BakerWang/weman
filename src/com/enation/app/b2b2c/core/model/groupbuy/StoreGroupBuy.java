package com.enation.app.b2b2c.core.model.groupbuy;

import com.enation.app.shop.component.groupbuy.model.GroupBuy;
/**
 * 多店铺团购
 * @author fenlongli
 *
 */
public class StoreGroupBuy extends GroupBuy{

	private Integer store_id;

	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
}
