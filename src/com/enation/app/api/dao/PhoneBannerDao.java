package com.enation.app.api.dao;

import java.util.List;

import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.model.PhoneBanner;
import com.enation.framework.database.Page;

/**
 * 首页的banner
 *
 */
public interface PhoneBannerDao {

	public boolean saveBanner(PhoneBanner banner) throws Exception;
	
	public List<PhoneBanner> getCurrentBanners() throws Exception;
	
	public List<PhoneBanner> getBanners(AdminSearchForm asf, Page page) throws Exception;
	
	
}
