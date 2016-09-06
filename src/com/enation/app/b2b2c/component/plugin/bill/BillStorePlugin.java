package com.enation.app.b2b2c.component.plugin.bill;


import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.bill.Bill;
import com.enation.app.b2b2c.core.service.bill.IBillManager;
import com.enation.app.base.core.plugin.job.IEveryMonthExecuteEvent;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.DateUtil;
/**
 * 结算每月执行插件
 * @author fenlongli
 *
 */
@Component
public class BillStorePlugin extends AutoRegisterPlugin implements IEveryMonthExecuteEvent {
	private IDaoSupport daoSupport;
	private IBillManager billManager;
	/**
	 * 每月触发一次结算
	 * 创建结算单
	 */
	@Override
	public void everyMonth() {
		try {
			String[] time=DateUtil.getLastMonth();
			Long start_time=DateUtil.getDateline(time[0]);
			Long end_time=DateUtil.getDateline(time[1]);
			
			Bill bill=new Bill();
			bill.setName(DateUtil.getDateline()+"");
			bill.setStart_time(start_time);
			bill.setEnd_time(end_time);
			this.billManager.add_bill(bill);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}
	
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	public IBillManager getBillManager() {
		return billManager;
	}
	public void setBillManager(IBillManager billManager) {
		this.billManager = billManager;
	}
	
}
