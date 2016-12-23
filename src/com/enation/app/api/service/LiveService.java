package com.enation.app.api.service;

import java.util.Map;

public interface LiveService {

	void saveLiveDetails(Map<String,Object> liveDetails) throws Exception;
	
	Map<String,Object> getLiveDetails(String name) throws Exception;
}
