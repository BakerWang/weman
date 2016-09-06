package com.enation.app.shop.component.groupbuy.service;


import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.groupbuy.model.GroupBuy;
import com.enation.framework.database.Page;

public interface IGroupBuyManager {
	/**
	 * 创建团购
	 * @param groupBuy
	 * @return 创建成功返回创建的团购id
	 *         创建失败返回0
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public int add(GroupBuy groupBuy);
	/**
	 * 修改团购信息
	 * @param groupBuy
	 * @return 创建成功返回创建的团购id
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(GroupBuy groupBuy);
	
	/**
	 * 删除团购
	 * @param gbid 团购id
	 */
	public void delete(int gbid);

	/**
	 * 审核
	 * @param gbid 团购id
	 * @param status 状态
	 */
	public void auth(int gbid,int status);
	

	/**
	 * 查询某活动下的团购
	 * @param page
	 * @param pageSize
	 * @param actid 活动id
	 * @param status 状态
	 * @return
	 */
	public Page listByActId(int page ,int pageSize,int actid,Integer status);
	
	/**
	 * 前台显示团购
	 * @param page 页数
	 * @param pageSize 每页显示数量
	 * @param catid 分类Id
	 * @param minprice 最小金额
	 * @param maxprice 最大金额
	 * @param sort_key 排序类型
	 * @param sort_type 正序还是倒叙
	 * @param search_type 搜索类型
	 * @param area_id 团购地区Id
	 * @return
	 */
	public Page search(int page,int pageSize,Integer catid,Double minprice,Double maxprice,Integer sort_key,Integer sort_type,Integer search_type,Integer area_id);
	/**
	 * 获取某个团购信息
	 * @param gbid 团购id
	 * @return 团购商品
	 */
	public GroupBuy get(int gbid);
	
	/**
	 * 根据商品Id获取团购商品
	 * @param goodsId
	 * @return
	 */
	public GroupBuy getBuyGoodsId(int goodsId);
}
