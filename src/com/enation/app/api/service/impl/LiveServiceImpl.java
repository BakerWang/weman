package com.enation.app.api.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.api.service.LiveService;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
@Service
public class LiveServiceImpl extends BaseSupport implements LiveService{

	@Override
	public void saveLiveDetails(Map<String, Object> liveDetails) throws Exception {
		this.baseDaoSupport.insert("wh_api_live", liveDetails);
	}

	@Override
	public Map<String, Object> getLiveDetails(String name) throws Exception {
		String sql = "select * from wh_api_live wal where wal.title = ?";
		List<Map<String,Object>> livelist = this.daoSupport.queryForList(sql, name);
		if(livelist==null||livelist.size()==0){
			return null;
		}else{
			String updatesql="update wh_api_live wal set wal.startTime = ?  where wal.title = ?";
			this.daoSupport.execute(updatesql, new Date().getTime()/1000,name);
			return livelist.get(0);
		}
	}

	@Override
	public Page getLivePalyBackList(Map<String,Object> map,Page page) throws Exception {
		String sql = "select wal.* from wh_api_live wal where 1=1 ";
		if(map!=null){
			if(map.containsKey("status")){
				sql = sql +" and wal.status = 1 ";
			}
		}
		sql = sql +"order by createTime desc";
		List<Map<String,Object>> livelist = this.daoSupport.queryForListPage(sql, (int)(page.getCurrentPageNo()), 20);
		String countSql = "select count(*) from wh_api_live";
		int count = this.daoSupport.queryForInt(countSql);
		return new Page(0, count, 20, livelist);
	}

	@Override
	public Map<String, Object> getLiveById(long lid) throws Exception {
		String sql = "select * from wh_api_live wal where wal.id = ? ";
		List<Map<String,Object>> livelist = this.baseDaoSupport.queryForList(sql, lid);
		if(livelist!=null&&livelist.size()>0){
			return livelist.get(0);
		}
		return null;
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void updateLive(long lid,Map<String, Object> map) throws Exception {
		this.baseDaoSupport.update("wh_api_live", map, " id = "+lid);
	}

	
	
	
}
