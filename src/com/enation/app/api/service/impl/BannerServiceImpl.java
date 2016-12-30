package com.enation.app.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


import org.springframework.stereotype.Service;

import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.model.PhoneBanner;
import com.enation.app.api.service.BannerService;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
@SuppressWarnings("rawtypes")
@Service
public class BannerServiceImpl extends BaseSupport implements BannerService{

	@Override
	public boolean saveBanner(PhoneBanner banner) throws Exception {
//		if(checkExistsBanner(banner)){
//			return false;
//		}
		this.baseDaoSupport.insert("es_api_banner", banner);
		return true;
	}

	public boolean checkExistsBanner(PhoneBanner as) throws Exception{
		List<Object> paramList = new ArrayList<Object>();
		String sql="select * from es_api_banner aas where status =1 and aas.category = ? and aas.type = ? and " +
				"((aas.start_time > ? and aas.start_time < ?) or " +
				"(aas.start_time <= ? and aas.end_time >= ?) or " +
				"(aas.start_time < ? and aas.end_time > ?) or " +
				"(aas.start_time >= ? and aas.end_time <= ?)) ";
		paramList.add(as.getCategory());
		paramList.add(as.getType());
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
	public List<PhoneBanner> getCurrentBanners(String type) throws Exception {
		String sql = "select * from es_api_banner ab where ab.status=1 and ab.category = ? and ab.start_time<=UNIX_TIMESTAMP(now())*1000 and end_time>UNIX_TIMESTAMP(now())*1000 order by create_time desc";
		return this.daoSupport.queryForList(sql, PhoneBanner.class,type); 
	}

	@Override
	public Page getBanners(AdminSearchForm asf, Page page) throws Exception {
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
		String countSql = "SELECT COUNT(*) from es_api_banner where status =1";
		int totalCount = this.daoSupport.queryForInt(countSql);
		return new Page(0, totalCount, page.getPageSize(), this.daoSupport.queryForList(sql, PhoneBanner.class));
	}

	@Override
	public void delBanner(long bid) throws Exception {
		this.daoSupport.execute("delete from es_api_banner where id = ?", bid);
	}

	@Override
	public PhoneBanner getBannerDetails(String bid) throws Exception {
		return (PhoneBanner) this.daoSupport.queryForObject("select * from es_api_banner where id = ?", PhoneBanner.class, bid);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void updateBanner(PhoneBanner phoneBanner) throws Exception {
		phoneBanner.setCreate_time(new Date().getTime());
		this.daoSupport.update("es_api_banner", phoneBanner, "id = "+phoneBanner.getId());
	}


	
}
