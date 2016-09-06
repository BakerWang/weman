package com.enation.app.shop.component.groupbuy.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 团购分类列表标签
 * @author kingapex
 *2015-1-5下午5:28:59
 */
@Component
@Scope("prototype")
public class GroupBuyCatListTag extends BaseFreeMarkerTag {
	private IGroupBuyCatManager groupBuyCatManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		return groupBuyCatManager.listAll();
	}
	public IGroupBuyCatManager getGroupBuyCatManager() {
		return groupBuyCatManager;
	}
	public void setGroupBuyCatManager(IGroupBuyCatManager groupBuyCatManager) {
		this.groupBuyCatManager = groupBuyCatManager;
	}

	
}

