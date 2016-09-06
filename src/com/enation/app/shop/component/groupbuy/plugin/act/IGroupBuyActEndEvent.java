package com.enation.app.shop.component.groupbuy.plugin.act;

/**
 * 团购活动关闭事件
 * @author fenlongli
 *
 */
public interface IGroupBuyActEndEvent {

	/**
	 * 关闭团购活动
	 */
	public void onEndGroupBuyEnd(Integer act_id);
}
