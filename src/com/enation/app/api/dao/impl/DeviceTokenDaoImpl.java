package com.enation.app.api.dao.impl;

import org.springframework.stereotype.Repository;

import com.enation.app.api.dao.DeviceTokenDao;
import com.enation.app.api.model.DeviceToken;
import com.enation.eop.sdk.database.BaseSupport;
@Repository
public class DeviceTokenDaoImpl extends BaseSupport<DeviceToken> implements DeviceTokenDao{

	@Override
	public void saveDeviceToken(DeviceToken dt) {
		this.baseDaoSupport.insert("es_api_deviceToken", dt);
	}

}
