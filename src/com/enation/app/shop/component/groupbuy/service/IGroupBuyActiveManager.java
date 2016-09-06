package com.enation.app.shop.component.groupbuy.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.framework.database.Page;

/**
 * 团购活动Manager
 * @author fenlongli
 *
 */
public interface IGroupBuyActiveManager {
	/**
	 * 团购活动列表
	 * @param page
	 * @param pageSize
	 * @param map
	 * @return
	 */
	public Page groupBuyActive(Integer page,Integer pageSize,Map map);
	
	/**
	 * 添加团购活动
	 * @param groupBuyActive
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void add(GroupBuyActive groupBuyActive);
	
	/**
	 * 更新团购活动
	 * @param groupBuyActive
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	public void update(GroupBuyActive groupBuyActive);
	
	/**
	 * 批量删除团购活动
	 * @param ids
	 */
	public void delete(Integer[] ids);
	
	/**
	 * 删除某个团购活动
	 * @param id
	 */
	public void delete(int id);
	/**
	 * 获取某个团购活动
	 * @param id 团购活动Id
	 * @return
	 */
	public GroupBuyActive get(int id);
	/**
	 * 获取当前正在举办的团购活动
	 */
	public GroupBuyActive get();
	/**
	 * 获取团购最后结束日期
	 * @return
	 */
	public Long getLastEndTime();
	
	/**
	 * 读取可用的团购活动（未过期的）
	 * @return
	 */
	public List<GroupBuyActive> listEnable();
	/**
	 * 读取可报名的团购活动
	 * @return
	 */
	public List<GroupBuyActive> listJoinEnable();
}
