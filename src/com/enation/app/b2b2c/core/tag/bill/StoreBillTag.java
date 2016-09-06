package com.enation.app.b2b2c.core.tag.bill;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.bill.IBillManager;
import com.enation.framework.taglib.BaseFreeMarkerTag;

import freemarker.template.TemplateModelException;
/**
 * 结算单详细
 * @author fenlongli
 * @date 2015-5-21 下午4:04:10
 */
@Component
public class StoreBillTag extends BaseFreeMarkerTag{
	private IBillManager billManager;
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		Integer id=Integer.parseInt(params.get("id").toString());
		
		return billManager.get_bill_detail(id);
	}
	public IBillManager getBillManager() {
		return billManager;
	}
	public void setBillManager(IBillManager billManager) {
		this.billManager = billManager;
	}
}
