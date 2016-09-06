package com.enation.app.shop.component.groupbuy.tag;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.shop.component.groupbuy.service.IGroupBuyAreaManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 团购地区列表标签
 * @author kingapex
 *2015-1-5下午5:28:59
 */
@Component
@Scope("prototype")
public class GroupBuyAreaListTag extends BaseFreeMarkerTag {
	private IGroupBuyAreaManager groupBuyAreaManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		return groupBuyAreaManager.listAll();
	}
	public IGroupBuyAreaManager getGroupBuyAreaManager() {
		return groupBuyAreaManager;
	}
	public void setGroupBuyAreaManager(IGroupBuyAreaManager groupBuyAreaManager) {
		this.groupBuyAreaManager = groupBuyAreaManager;
	}

	
}

