package com.enation.app.b2b2c.core.service.groupbuy;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.framework.database.Page;

/**
 * 团购管理类
 * @author kingapex
 *2015-1-8下午6:08:24
 */
public interface IStoreGroupBuyManager {
	
	
	/**
	 * 搜索某店铺的团购
	 * @param page 页码
	 * @param pageSize  分页数
	 * @param storeid 店铺id
	 * @param params 其它参数
	 * @return 分页结果集
	 */
	public Page listByStoreId(int page,int pageSize,int storeid,Map params);
	
	
}
