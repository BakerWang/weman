package com.enation.app.shop.component.groupbuy.tag.act;

import java.util.Map;


import org.springframework.stereotype.Component;

import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 获取当前的活动标签
 * @author fenlongli
 *
 */
@Component
public class GroupBuyActTag extends BaseFreeMarkerTag{
	private IGroupBuyActiveManager groupBuyActiveManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		GroupBuyActive groupbuyAct;
		//如果团购活动Id为空则获取当前的团购活动
		if(params.get("act_id")==null){
			 groupbuyAct=groupBuyActiveManager.get();
		}else{
			groupbuyAct=groupBuyActiveManager.get(Integer.parseInt((params.get("act_id").toString())));
		}
		if(groupbuyAct==null){
			return "";
		}
		return groupbuyAct;
	}
	public IGroupBuyActiveManager getGroupBuyActiveManager() {
		return groupBuyActiveManager;
	}
	public void setGroupBuyActiveManager(
			IGroupBuyActiveManager groupBuyActiveManager) {
		this.groupBuyActiveManager = groupBuyActiveManager;
	}
}
