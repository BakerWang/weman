package com.enation.app.api.service;

import java.util.List;
import java.util.Map;

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

	public Map<String,Object> getBannerDetailsByTitle(String name) throws Exception;

	/**
	 * 保存banner的点击数
	 * type : 是否登陆
	 * @param bannerId
	 * @throws Exception
	 */
	public void updateBannerClick(int bannerId,String type) throws Exception;

	public Page getUserActionBannerList(int parseInt, int i, Map<String, Object> maps) throws Exception;
}
