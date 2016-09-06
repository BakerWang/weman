package com.enation.app.shop.component.groupbuy.service;

import java.util.List;

import com.enation.app.shop.component.groupbuy.model.GroupBuyArea;
import com.enation.framework.database.Page;

/**
 * 团购地区管理
 * @author fenlongli
 *
 */
public interface IGroupBuyAreaManager {

	/**
	 * 获取地区列表
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page list(int pageNo,int pageSize);
	
	
	/**
	 * 获取所有地区列表，不分页
	 * @return
	 */
	public List<GroupBuyArea> listAll();
	
	
	/**
	 * 获取某个团购地区
	 * @param catid
	 * @return
	 */
	public GroupBuyArea get(int catid);
	
	
	
	/**
	 * 添加团购地区
	 * @param groupBuyCat
	 */
	public void add(GroupBuyArea groupBuyArea);
	
	/**
	 * 更新团购地区
	 * @param groupBuyCat
	 */
	public void update(GroupBuyArea groupBuyArea);

	
	/**
	 * 删除团购地区
	 * @param catid
	 */
	public void delete(Integer[] areaid);
}
