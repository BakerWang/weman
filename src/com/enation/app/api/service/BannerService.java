package com.enation.app.api.service;

import java.util.List;

import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.model.PhoneBanner;
import com.enation.framework.database.Page;

public interface BannerService {

	public boolean saveBanner(PhoneBanner banner) throws Exception;
	
	public List<PhoneBanner> getCurrentBanners(String type) throws Exception;
	
	public Page getBanners(AdminSearchForm asf, Page page) throws Exception;

	public void delBanner(long parseLong) throws Exception;

	public PhoneBanner getBannerDetails(String bid) throws Exception;

	public void updateBanner(PhoneBanner phoneBanner) throws Exception;
}
