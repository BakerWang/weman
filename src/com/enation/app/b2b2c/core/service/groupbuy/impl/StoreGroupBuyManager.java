package com.enation.app.b2b2c.core.service.groupbuy.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.groupbuy.IStoreGroupBuyManager;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.StringUtil;

/**
 * 团购管理
 * @author kingapex
 *2015-1-5下午7:11:09
 */
@Component
public class StoreGroupBuyManager implements IStoreGroupBuyManager {
	
	private IDaoSupport daoSupport;
	@Override
	public Page listByStoreId(int page, int pageSize, int storeid, Map params) {
		
		String gb_name = (String)params.get("gb_name");
		String gb_status =  (String)params.get("gb_status");
		
		StringBuffer sql = new StringBuffer();
		sql.append("select g.*,a.act_name,a.start_time,a.end_time from es_groupbuy_goods g ,es_groupbuy_active a where g.store_id= ? and  g.act_id= a.act_id ");
		
		if(!StringUtil.isEmpty(gb_name)){
			sql.append(" and g.gb_name like '%"+gb_name+"%'");
		}
		if(!StringUtil.isEmpty(gb_status)){
			sql.append(" and g.gb_status="+gb_status);
		}
		sql.append(" order by add_time ");
		
		return this.daoSupport.queryForPage(sql.toString(),page,pageSize, storeid);
		
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
}
