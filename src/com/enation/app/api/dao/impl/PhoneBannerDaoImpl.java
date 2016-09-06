package com.enation.app.api.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.dao.PhoneBannerDao;
import com.enation.app.api.model.PhoneBanner;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
@Repository
public class PhoneBannerDaoImpl extends BaseSupport<PhoneBanner> implements PhoneBannerDao{

	@Override
	public boolean saveBanner(PhoneBanner banner) throws Exception {
		if(checkExistsBanner(banner)){
			return false;
		}
		this.baseDaoSupport.insert("es_api_banner", banner);
		return true;
	}

	public boolean checkExistsBanner(PhoneBanner as) throws Exception{
		List<Object> paramList = new ArrayList<Object>();
		String sql="select * from es_api_banner aas where status =1 and " +
				"((aas.startTime > ? and aas.startTime < ?) or " +
				"(aas.startTime <= ? and aas.endTime >= ?) or " +
				"(aas.startTime < ? and aas.endTime > ?) or " +
				"(aas.startTime >= ? and aas.endTime <= ?)) ";
		paramList.add(as.getStart_time());
		paramList.add(as.getEnd_time());
		paramList.add(as.getStart_time());
		paramList.add(as.getEnd_time());
		paramList.add(as.getStart_time());
		paramList.add(as.getStart_time());
		paramList.add(as.getStart_time());
		paramList.add(as.getEnd_time());
		List<PhoneBanner> pbs = this.daoSupport.queryForList(sql, PhoneBanner.class, paramList.toArray());
		if(pbs!=null&&pbs.size()>0){
			return true;
		}
		return false;
	}
	
	@Override
	public List<PhoneBanner> getCurrentBanners() throws Exception {
		return null;
	}

	@Override
	public List<PhoneBanner> getBanners(AdminSearchForm asf, Page page) throws Exception {
		String sql="select * from es_api_banner where status =1 ";
		if(asf!=null){
			if(asf.getTitle()!=null&&!"".equals(asf.getTitle())){
				sql = sql+" and (title like '%"+asf.getTitle()+"%')";
			}
			if(asf.getType()!=null&&!"".equals(asf.getType())){
				sql = sql+" and type = "+asf.getType();
			}
		}
		sql = sql+" order by create_time desc";
		return this.daoSupport.queryForList(sql, PhoneBanner.class);
	}

}
