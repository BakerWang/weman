package com.enation.app.shop.component.groupbuy.plugin.act;

/**
 * 团购活动开启事件
 * @author fenlongli
 *
 */
public interface IGroupBuyActStartEvent {

	/**
	 * 开启团购活动
	 */
	public void onGroupBuyStart(Integer act_id);
}
