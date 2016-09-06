package com.enation.app.shop.component.groupbuy.plugin.act;

/**
 * 团购活动删除事件
 * @author fenlongli
 *
 */
public interface IGroupBuyActDeleteEvent {
	/**
	 * 删除团购活动
	 */
	public void onDeleteGroupBuyAct(Integer act_id);
	
}
