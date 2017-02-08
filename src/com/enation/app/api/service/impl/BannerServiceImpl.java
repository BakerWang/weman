package com.enation.app.api.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	@Override
	public Map<String, Object> getBannerDetailsByTitle(String name) throws Exception {
		String sql = "select * from es_api_banner eab where eab.details = ? and type = '直播' ";
		List<Map<String, Object>> reslist = this.daoSupport.queryForList(sql, name);
		if(reslist!=null&&reslist.size()>0){
			return reslist.get(0);
		}
		return null;
	}

	@Override
	public void updateBannerClick(int bannerId,String type) throws Exception {
		if("yes".equals(type)){
			String sql = "update es_api_banner eab set eab.clickCount = eab.clickCount+1 where eab.id = ?";
			this.daoSupport.execute(sql, bannerId);
		}else{
			String sql = "update es_api_banner eab set eab.nologinClickCount = eab.nologinClickCount+1 where eab.id = ?";
			this.daoSupport.execute(sql, bannerId);
		}
	}

	@Override
	public Page getUserActionBannerList(int parseInt, int i, Map<String, Object> maps) throws Exception {
		String sql = "select at.* from es_api_banner at where 1=1 ";
		if(maps!=null){
			if(maps.containsKey("startTime")){
				sql = sql +" and at.start_time > "+ (long)maps.get("startTime");
			}
			if(maps.containsKey("endTime")){
				sql = sql +" and at.end_time > "+ (long)maps.get("endTime");
			}
			if(maps.containsKey("keywords")&&!"".equals(maps.get("keywords"))){
				sql = sql +" and at.content like '%"+maps.get("keywords")+"%' ";
			}
			if(maps.containsKey("orderBy")){
				sql = sql +" order by at."+maps.get("orderBy")+" desc ";
			}else{
				sql = sql +" order by at.start_time desc ";
			}
		}
		return this.daoSupport.queryForPage(sql, parseInt, 10);
	}


	
}
