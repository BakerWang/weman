package com.enation.app.api.service;

import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.Theme;
import com.enation.app.api.model.ThemeTag;
import com.enation.app.shop.core.model.Goods;
import com.enation.framework.database.Page;

import net.sf.json.JSONArray;

public interface ProductService {

	Page getProductList(int pageNo,int pageSize,Map<String,String> map);
	
	
	/**
	 * 创建主题
	 * @param title 
	 * @param image
	 * @param details
	 */
	Theme saveTheme(Theme theme,List<Map<String,Object>> contentMaps);
	
	
	/**
	 * 获取主题列表（包含主题信息和主题的赞）
	 * @param pageNo
	 * @param pageSize
	 * @param map  删选参数
	 * @return
	 */
	Page getThemeProducts(int pageNo,int pageSize,Map<String,String> map);
	
	/**
	 * 商品与主题的增删
	 * @param good_id 商品id
	 * @param theme_id 主题id
	 * @param position  显示的位置   目前没用
	 * @param status 1为添加  -1是移除
	 * @param type 1是主页列表的商品  2是主题详情里面的商品
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void saveProductJoinTheme(int good_id,int theme_id,int position,String status,String type);
	
	/**
	 * 喜欢主题
	 * @param memberId
	 * @param themeId
	 */
	@Transactional(propagation = Propagation.REQUIRED)
	void loveTheme(int memberId,int themeId);

	/**
	 * 获取主题的详情
	 * @param memberId
	 * @param themeId
	 */
	ThemeProduct catchThemeDetails(int parseInt);


	/**
	 * 更新主题的信息
	 * @param title
	 * @param image
	 * @param details
	 * @return
	 */
	Theme updateTheme(int themeId,Map<String,Object> maps);


	/**
	 * 删除主题
	 * @param parseInt
	 */
	void deleTheme(int parseInt);


	/**
	 * 主题的标签列表
	 * @return
	 */
	List<ThemeTag> getThemeTagList();


	/**
	 * 保存主题标签
	 * @param themeTag
	 */
	void addThemeTag(ThemeTag themeTag);


	/**
	 * 获取父类标签
	 * @return
	 */
	List<ThemeTag> getParentThemeTagList();


	/**
	 * 获取子类标签
	 * @return
	 */
	List<ThemeTag> getChildrenThemeTagList();


	void deleThemeTag(Long ttId);


	void updateThemeTag(int themeId, int keyId, int valueId);


	Page getThemeJsonList(int parseInt, int i, Map<String, String> map);

	/**
	 * app主题列表接口
	 * @param pageNo
	 * @param pageSize
	 * @param map
	 * @return
	 */
	Page getThemeProductsAPP(int pageNo, int pageSize, Map<String, String> map) ;
	
	/**
	 * 获取主题详情
	 * @param themeId
	 * @param jsonArray
	 * @return 
	 */
	Theme getThemeDetails(int themeId,int memberId) throws Exception;


	/**
	 * 根据id获取主题的标签
	 * @param typeId
	 * @return
	 * @throws Exception
	 */
	ThemeTag getThemeTagById(String typeId) throws Exception;


	/**
	 * 添加商品
	 * @param map
	 * @throws Exception
	 */
	void addProduct(Map<String, Object> map) throws Exception;


	/**
	 * 修改商品是否上架
	 * @param productId
	 * @param map
	 * @throws Exception
	 */
	void updateProduct(int productId, Map<String, Object> map)throws Exception;


	void updateThemeTag(ThemeTag tt)throws Exception;


}
