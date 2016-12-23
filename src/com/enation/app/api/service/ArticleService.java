package com.enation.app.api.service;

import java.util.List;
import java.util.Map;

import com.enation.app.api.action.admin.form.ArticleModel;
import com.enation.app.api.action.admin.form.ArticleTagModel;
import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.dto.BeginArticleCat;
import com.enation.framework.database.Page;

public interface ArticleService {

	/**
	 * 保存访问
	 * @param dtoMap
	 * @throws Exception
	 */
	void saveArticle(Map<String,Object> dtoMap) throws Exception;
	
	
	
	
	/** 发文标签部分  */
	
	
	/**
	 * 保存发文标签
	 * @param dtoMap
	 * @throws Exception
	 */
	void saveArticleTag(Map<String,Object> dtoMap) throws Exception;
	
	
	/**
	 * 发文之前的接口  搜索发文标签
	 * @param searchStr
	 * @return
	 * @throws Exception
	 */
	List<String> searchTag(String searchStr) throws Exception;
	
	/**
	 * 发文之前的接口  访问品牌和分类
	 * @return
	 * @throws Exceptioin
	 */
	List<Map<String,Object>> beginArticle(int list_show) throws Exception;



	/**
	 * 发文之前 搜索品牌
	 * @param searchStr
	 * @return
	 * @throws Exception
	 */
	List<Map<String,String>> searchBrand(String searchStr) throws Exception;
	
	

	/**
	 * 后台部分
	 * 后台获取发文列表
	 * @param adminSearchForm
	 * @param page
	 * @return
	 */
	Page getArticleList(AdminSearchForm adminSearchForm, Page page) throws Exception;




	/**
	 * 更改发文的状态
	 * @param articleId
	 * @param status
	 * @throws Exception
	 */
	void updateArticleStatus(int articleId, Map<String,Object> map) throws Exception;


	/**
	 * 获取发文标签的列表
	 * @param adminSearchForm
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page getArticleTagList(AdminSearchForm adminSearchForm, Page page)throws Exception;
	
	
	/**
	 * 获取发文标签的列表
	 * @param adminSearchForm
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page getArticleTagList(Page page)throws Exception;


	/**
	 * 更改发文标签的状态
	 * @param articleTagId
	 * @param status
	 * @throws Exception
	 */
	void updateArticleTagStatus(int articleTagId, int status)throws Exception;


	/**
	 * 保存发文的评论
	 * @param commentMap
	 * @throws Exception
	 */
	void saveArticleComment(Map<String,Object> commentMap) throws Exception;

	/**
	 * 获取发文的评论列表
	 * @param adminSearchForm
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page getArticleComments(AdminSearchForm adminSearchForm,Page page) throws Exception;
	
	/**
	 * 更改发文评论的状态
	 * @param commentId
	 * @param commentMap
	 * @throws Exception
	 */
	void updateArticleComment(int commentId,Map<String,Object> commentMap) throws Exception;
	
	/**
	 * 获取某个发文的评论列表
	 * @param articleId
	 * @param memberId
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page getCommentsByArticleId(int articleId,Page page,int memberId) throws Exception;
	
	
	/**
	 * 保存用户的动作
	 * @param actionMap
	 * @throws Exception
	 */
	void saveUserAction(Map<String,Object> actionMap) throws Exception;
	
	/**
	 * 获取用户动作的列表
	 * @param adminSearchForm
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page getUserActionList(AdminSearchForm adminSearchForm,Page page) throws Exception;
	
	/**
	 * 更改用户动作
	 * @param userActionId
	 * @param userActionMap
	 * @throws Exception
	 */
	void updateUserAction(int userActionId,Map<String,Object> userActionMap) throws Exception;




	/**
	 * 获取用户的关注列表
	 * @param cmemberId
	 * @param i
	 * @return
	 * @throws Exception
	 */
	List<Integer> getFollowsByMemberId(int cmemberId, int i) throws Exception;




	/**
	 * 获取发文详情
	 * @param articleId
	 * @return
	 * @throws Exception
	 */
	ArticleModel getArtilceDetails(int articleId,int memberId) throws Exception;




	/**
	 * 更改发文标签是不是hot  type：1是hot -1不是
	 * @param articleTagId
	 * @param type
	 */
	void updateArticleTagType(int articleTagId, int type);




	/**
	 * 获取发文的赞列表
	 * @param page
	 * @return
	 */
	Page getUserActionList(int article,Page page);




	/**
	 * 取消用户动作
	 * @param actionMap
	 */
	void updateUserActioin(Map<String, Object> actionMap);




	
	
}
