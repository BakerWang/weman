package com.enation.app.api.service;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.Page;

public interface LiveService {

	void saveLiveDetails(Map<String,Object> liveDetails) throws Exception;
	
	Map<String,Object> getLiveDetails(String name,boolean isUpdateStartTime) throws Exception;

	Page getLivePalyBackList(Map<String,Object> map,Page page) throws Exception;

	Map<String, Object> getLiveById(long parseLong) throws Exception;

	void updateLive(long lid,Map<String, Object> map) throws Exception;
	
	/**
	 * 获取直播伪造用户列表
	 * @param map
	 * @throws Exception
	 */
	List<Map<String,Object>> getLiveUserList(Map<String, Object> map) throws Exception;
}
