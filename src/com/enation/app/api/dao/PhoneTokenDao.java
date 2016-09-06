package com.enation.app.api.dao;

import com.enation.app.api.model.PhoneToken;

public interface PhoneTokenDao {

	PhoneToken getPhoneToken(Integer memberId);
	
	PhoneToken savePhoneToken(PhoneToken phoneToken);
	
	PhoneToken updatePhoneToken(PhoneToken phoneToken);
	
	int getMemberIdByAccessToken(String accessToken);
	
}
