package com.enation.app.shop.component.groupbuy.plugin.act;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.plugin.job.IEveryDayExecuteEvent;
import com.enation.app.shop.component.groupbuy.plugin.GroupbuyPluginBundle;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.DateUtil;
/**
 * 团购活动插件
 * @author fenlongli
 *
 */
@Component
public class GroupBuyActPlugin extends AutoRegisterPlugin implements IEveryDayExecuteEvent{
	private IDaoSupport daoSupport;
	private IGroupBuyManager groupBuyManager;
	private IGroupBuyActiveManager groupBuyActiveManager;
	private GroupbuyPluginBundle groupbuyPluginBundle;
	/**
	 * 当团购达到结束时间就关闭团购
	 * 当团购达到开始时间就开启团购
	 */
	@Override
	public void everyDay() {
		//开启团购
		String sql="UPDATE es_groupbuy_active SET act_stat=1  WHERE act_status=0 AND add_time<?";
		this.daoSupport.execute(sql, DateUtil.getDateline());
		Integer actId=this.daoSupport.getLastId("es_groupbuy_active");
		groupbuyPluginBundle.onGroupBuyStart(actId);
		
		//关闭团购
		sql="UPDATE es_groupbuy_active SET act_stat=2  WHERE act_status=1 AND end_time<?";
		this.daoSupport.execute(sql, DateUtil.getDateline());
		actId=this.daoSupport.getLastId("es_groupbuy_active");
		groupbuyPluginBundle.onGroupBuyEnd(actId);
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	public IGroupBuyManager getGroupBuyManager() {
		return groupBuyManager;
	}
	public void setGroupBuyManager(IGroupBuyManager groupBuyManager) {
		this.groupBuyManager = groupBuyManager;
	}
	public IGroupBuyActiveManager getGroupBuyActiveManager() {
		return groupBuyActiveManager;
	}
	public void setGroupBuyActiveManager(
			IGroupBuyActiveManager groupBuyActiveManager) {
		this.groupBuyActiveManager = groupBuyActiveManager;
	}
}
