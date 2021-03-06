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
import net.sf.json.JSONObject;

public interface ProductService {

	Page getProductList(int pageNo,int pageSize,Map<String,Object> map);
	
	
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
	Page getThemeProducts(int pageNo,int pageSize,Map<String,Object> map);
	
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


	/**
	 * 查看用户点击主题详情
	 * @param startTime
	 * @param endTime
	 * @param dataId
	 * @param string
	 */
	Page userThemeCount(Long startTime, Long endTime,Long userStartTime, Long userEndTime, int dataId, String string,Page page);


	JSONObject getProductDetails(int productId, JSONObject jsonObject) throws Exception;


	void updateThemeContent(int tcid, Map<String, Object> map)throws Exception;


	void deleThemeContent(int tcid)throws Exception;


	/**
	 * 获取商品详情
	 * @param parseInt
	 * @return
	 * @throws Exception
	 */
	Map<String,Object> getProductDetails(int parseInt)throws Exception;


	/**
	 * 未登录用户点击主题的统计
	 * @param startTime
	 * @param endTime
	 * @param dataId
	 * @param string
	 * @param page
	 * @return
	 */
	Page noUserThemeCount(Long startTime, Long endTime, int dataId, String string, Page page);


	/**
	 * 获取后台首页的数据
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getAdminIndexDetails(Long startTime, Long endTime) throws Exception;


	/**
	 * 获取未上架的主题
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBrowseThemeList() throws Exception;

	/**
	 * app主题列表接口2.0
	 * @param pageNo
	 * @param pageSize
	 * @param map
	 * @return
	 */
	Page getThemeProductsAPPVersion2(int pageNo, int i, Map<String, String> map) throws Exception;


	/**
	 * 后台获取点击商品的统计信息
	 * @param pageNo
	 * @param i
	 * @param maps
	 * @return
	 * @throws Exception
	 */
	Page getUserActionProduct(String pageNo, int i, Map<String, Object> maps)throws Exception;


	List<Map<String, Object>> getThemeContentByThemeIds(List<Integer> themeIds) throws Exception;



}
