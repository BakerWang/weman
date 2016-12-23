package com.enation.app.api.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enation.app.api.service.LiveService;
import com.enation.eop.sdk.database.BaseSupport;
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
			return livelist.get(0);
		}
	}

	
	
	
}
