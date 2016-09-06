package com.enation.app.shop.component.groupbuy.plugin.act;

import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;

/**
 * 添加团购活动事件
 * @author fenlongli
 *
 */
public interface IGroupBuyActAddEvent {
	/**
	 * 添加团购活动
	 * @param groupBuyActive
	 */
	public void onAddGroupBuyAct(GroupBuyActive groupBuyActive);
}
