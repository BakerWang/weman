package com.enation.app.api.service;

import java.util.List;
import java.util.Map;

import com.enation.app.base.core.model.Member;
import com.enation.framework.database.Page;

import net.sf.json.JSONObject;

public interface PersionService {

	/**
	 * 获取用户的赞数，关注数，粉丝数
	 * @param obj
	 * @return
	 */
	JSONObject getUserActionCount(JSONObject obj,int member_id);

	/**
	 * 更新个人信息
	 * @param member
	 */
	void updatePersionDetails(Member member);

	/**
	 * 获取我的关注或者我的粉丝
	 * @param userId
	 * @param type  page
	 * @return
	 */
	Page myFriendList(int member_id,int userId, int type,Page page);

	/**
	 * 获取用户的动作列表  
	 * type 1.发文列表  2是收藏主题列表  3是收藏商品列表
	 * @param member_id
	 * @param type
	 * @param page
	 * @return
	 */
	Page fetchUserActionList(int member_id, int type, Page page);
	
	/**
	 * 意见反馈
	 * @param map
	 * @throws Exception
	 */
	void saveFAQ(Map<String, Object> map) throws Exception;

	/**
	 * 获取消息数量
	 * @param jsonObject
	 * @param member_id
	 */
	void fecthMessageCount(JSONObject jsonObject, int member_id)throws Exception;

	/**
	 * 获取通知4    被赞类别3   被评论列表2   被加关注列表1  
	 * @param type
	 * @param member_id
	 * @return
	 * @throws Exception
	 */
	Page fecthMessageList(int type, int member_id,Page page)throws Exception;

	/**
	 * 判断是不是关注过了
	 * @param member_id
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	boolean getIsFriend(int member_id, String userId)throws Exception;
	
	
	
	/**
	 * 保存用户的动作  1.loginClickTheme 2.nologinClickTheme  
	 * 3.loginClickProduct 4.nologinClickProduct
	 * 5.loginClickBanner-index-live/theme/html5 6.nologinClickBanner-index
	 * 7.loginClickBanner-find 8.nologinClickBanner-find
	 * 8.loginClickBanner-article  9.nologinClickBanner-article
	 * 10.loginClickArticle 11.nologinClickArticle
	 * @param member_id
	 * @param type
	 * @param data_id
	 * @throws Exception
	 */
	void saveUserAction(String member_id,String type,int data_id) throws Exception;

	
}
