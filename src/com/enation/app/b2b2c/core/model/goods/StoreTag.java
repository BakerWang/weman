package com.enation.app.b2b2c.core.model.goods;

import com.enation.app.shop.core.model.Tag;
/**
 * 店铺商品标签
 * @author LiFenLong
 *
 */
public class StoreTag extends Tag {

	private Integer store_id;	//店铺Id
	private String mark;	//店铺标签标识
	private Integer is_groupbuy;	//是否为团购商品标签
	public Integer getStore_id() {
		return store_id;
	}

	public void setStore_id(Integer store_id) {
		this.store_id = store_id;
	}
	public String getMark() {
		return mark;
	}

	public void setMark(String mark) {
		this.mark = mark;
	}

	public Integer getIs_groupbuy() {
		return is_groupbuy;
	}

	public void setIs_groupbuy(Integer is_groupbuy) {
		this.is_groupbuy = is_groupbuy;
	}
	
}
