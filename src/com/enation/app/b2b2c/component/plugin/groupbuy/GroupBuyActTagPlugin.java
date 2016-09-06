package com.enation.app.b2b2c.component.plugin.groupbuy;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.goods.StoreTag;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsTagManager;
import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.app.shop.component.groupbuy.plugin.act.IGroupBuyActAddEvent;
import com.enation.app.shop.component.groupbuy.plugin.act.IGroupBuyActDeleteEvent;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 团购活动标签
 * @author fenlongli
 *
 */
@Component
public class GroupBuyActTagPlugin extends AutoRegisterPlugin implements IGroupBuyActAddEvent,IGroupBuyActDeleteEvent{
	private IStoreGoodsTagManager storeGoodsTagManager;
	private IGroupBuyActiveManager groupBuyActiveManager;
	private IDaoSupport daoSupport;
	@Override
	public void onAddGroupBuyAct(GroupBuyActive groupBuyActive) {
		
		StoreTag storeTag=new StoreTag();
		storeTag.setIs_groupbuy(groupBuyActive.getAct_id());
		storeTag.setTag_name(groupBuyActive.getAct_name()+"  热门团购");
		
		storeGoodsTagManager.add(storeTag);
		//将标签id传递给团购活动
		this.daoSupport.execute("update es_groupbuy_active set act_tag_id=? where act_id=?", this.daoSupport.getLastId("es_groupbuy_active"),groupBuyActive.getAct_id());
		
	}
	@Override
	public void onDeleteGroupBuyAct(Integer act_id) {
		GroupBuyActive groupBuyActive= groupBuyActiveManager.get(act_id);
		this.daoSupport.execute("delete from es_tag_rel where tag_id=(select tag_id from es_tags where tag_name=?)",groupBuyActive.getAct_name()+"  热门团购");
		this.daoSupport.execute("delete from es_tags where where tag_name=?",groupBuyActive.getAct_name()+"  热门团购");
	}
	public IStoreGoodsTagManager getStoreGoodsTagManager() {
		return storeGoodsTagManager;
	}
	public void setStoreGoodsTagManager(IStoreGoodsTagManager storeGoodsTagManager) {
		this.storeGoodsTagManager = storeGoodsTagManager;
	}
	public IGroupBuyActiveManager getGroupBuyActiveManager() {
		return groupBuyActiveManager;
	}
	public void setGroupBuyActiveManager(
			IGroupBuyActiveManager groupBuyActiveManager) {
		this.groupBuyActiveManager = groupBuyActiveManager;
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	
}
