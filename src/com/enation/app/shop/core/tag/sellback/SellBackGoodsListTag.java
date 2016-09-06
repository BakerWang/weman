package com.enation.app.shop.core.tag.sellback;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.service.ISellBackManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 退货申请商品列表
 * @author fenlongli
 *
 */
@Component
public class SellBackGoodsListTag extends BaseFreeMarkerTag {
	private ISellBackManager sellBackManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer recid=Integer.parseInt(params.get("id").toString());
		String sn=params.get("sn").toString();
		List list=sellBackManager.getGoodsList(recid, sn);
		return list;
	}
	public ISellBackManager getSellBackManager() {
		return sellBackManager;
	}
	public void setSellBackManager(ISellBackManager sellBackManager) {
		this.sellBackManager = sellBackManager;
	}
}
