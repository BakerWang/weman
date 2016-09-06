package com.enation.app.api.dao;

import java.util.List;
import java.util.Map;

import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.Theme;
import com.enation.app.shop.core.model.Goods;
import com.enation.framework.database.Page;

/**
 * @author solone
 * 主题接口
 */
public interface ThemeProductDao {

	
	
	/**
	 * 商品添加到主题系列中
	 * @param good_id 商品id
	 * @param theme_id 主题id
	 * @param position  显示的位置
	 * @param type 1是列表里面的商品  2是主题详情里面的商品
	 */
	public void productJoinTheme(int good_id,int theme_id,int position,String type);
	
	
	/**
	 * 去除主题系列中的一个商品
	 * @param good_id
	 * @param theme_id
	 */
	public void themeDelProduct(int good_id, int theme_id);
	
	/**
	 * 对主题的喜欢
	 * @param member_id
	 * @param theme_id
	 */
	public void loveTheme(int member_id,int theme_id);
	
	/**
	 * 获取主题列表（包含主题信息和主题的赞）
	 * @param pageNo
	 * @param pageSize
	 * @param map  删选参数
	 * @return
	 */
	public Page themeProductList(int pageNo,int pageSize,Map<String,String> map);

	/**
	 * 获取主题详情（包含主题信息和主题的赞）
	 * @param pageNo
	 * @param pageSize
	 * @param map  删选参数
	 * @return
	 */
	public ThemeProduct catchThemeDetails(int themeId);

	/**
	 * 更新主题信息
	 * @param title
	 * @param image
	 * @param details
	 * @return
	 */
	public Theme updateTheme(int themeId,Map<String,Object> maps);

	
}
