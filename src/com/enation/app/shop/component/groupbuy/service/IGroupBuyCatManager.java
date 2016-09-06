package com.enation.app.shop.component.groupbuy.service;

import java.util.List;

import com.enation.app.shop.component.groupbuy.model.GroupBuyCat;
import com.enation.framework.database.Page;


/**
 * 团购分类管理
 * @author kingapex
 *2015-1-3下午2:30:58
 */
public interface IGroupBuyCatManager {
	
	/**
	 * 获取分类列表
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Page list(int pageNo,int pageSize);
	
	
	/**
	 * 获取所有分类列表，不分页
	 * @return
	 */
	public List<GroupBuyCat> listAll();
	
	
	/**
	 * 获取某个团购
	 * @param catid
	 * @return
	 */
	public GroupBuyCat get(int catid);
	
	
	
	/**
	 * 添加团购分类
	 * @param groupBuyCat
	 */
	public void add(GroupBuyCat groupBuyCat);
	
	/**
	 * 更新团购分类
	 * @param groupBuyCat
	 */
	public void update(GroupBuyCat groupBuyCat);

	
	/**
	 * 删除团购分类
	 * @param catid
	 */
	public void delete(Integer[] catid);
}
