package com.enation.app.api.service;

import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.framework.database.Page;

public interface InitUserService {

	@Transactional(propagation = Propagation.REQUIRED)  
	int register(Member member,String notifyType, String deviceToken, String bindType, String bindNum,String clientId);

	/**
	 * 验证是否绑定
	 * @param bnum 唯一的标示（微信，新浪，QQ）
	 * @param type（微信，新浪，QQ）
	 * @return
	 */
	Map<String,Object> login_verifybind(String bnum, String type);

	/**
	 * 用户登录
	 * @param account 账号（手机号，uname，email）
	 * @param password 密码
	 * @param loginType 设备唯一标示
	 * @param deviceToken 设备类型     ||String clientId 个推唯一标示
	 * @return
	 */
	Map<String, Object> login(String account, String password, String deviceToken, String loginType,String clientId);

	/**
	 * 初始化信息
	 * @param clientType
	 * @param version
	 * @return
	 */
	Map<String, Object> catchAppInitMessage(String clientType);

	/**
	 * 通过username获取accessToken
	 * @param username
	 * @return
	 * @throws Exception
	 */
	String getAccessTokenByUname(String username) throws Exception;

	
	/**
	 * 通过accessToken判定是否过期   获取memberId
	 * @param accessToken
	 * @return
	 */
	int getMemberIdByAccessToken(String accessToken);

	/**
	 * 绑定clientId，个推的唯一标示
	 * @param deviceToken
	 * @param clientId
	 * @throws Exception
	 */
	void bindClientId(String deviceToken, String clientId,String clientType,int memberId)throws Exception;
	
	/**
	 * 获取分享的url
	 * @param type
	 * @param dataId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> shareUrl(String type, int dataId) throws Exception;

	/**
	 * 获取用户的详细信息
	 * @param accessToken
	 * @return
	 * @throws Exception
	 */
	Member getMemberDetailsByAccessToken(String accessToken) throws Exception;

}
