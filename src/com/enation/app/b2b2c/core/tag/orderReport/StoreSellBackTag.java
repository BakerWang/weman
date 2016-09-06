package com.enation.app.b2b2c.core.tag.orderReport;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.SellBackList;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.ISellBackManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;

/**
 * 退货申请标签
 * @author fenlongli
 *
 */
@Component
public class StoreSellBackTag extends BaseFreeMarkerTag {
	private ISellBackManager sellBackManager;
	private IOrderManager orderManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Map map=new HashMap();
		Integer id=Integer.parseInt(params.get("id").toString());
		SellBackList sellBackList=this.sellBackManager.get(id);
		map.put("sellBack", sellBackList);  //退货详细
		map.put("orderinfo",orderManager.get(sellBackList.getOrdersn()));//订单详细
		map.put("goodsList",this.sellBackManager.getGoodsList(id,sellBackList.getOrdersn()));//退货商品列表
		return map;
	}
	public ISellBackManager getSellBackManager() {
		return sellBackManager;
	}
	public void setSellBackManager(ISellBackManager sellBackManager) {
		this.sellBackManager = sellBackManager;
	}
	public IOrderManager getOrderManager() {
		return orderManager;
	}
	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}
}
