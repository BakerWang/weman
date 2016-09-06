package com.enation.app.api.service;

import java.util.Map;

import com.enation.framework.database.Page;

public interface SendMessageService {

	/**
	 * 全局推送
	 * @param pushMessage
	 * @param dataId
	 * @param type
	 * @throws Exception
	 */
	void saveSendMessage(String pushMessage, String dataId, String type) throws Exception;

	/**
	 * 测试单独用户的推送
	 * @param pushMessage
	 * @param dataId
	 * @param type
	 * @param mobile
	 * @return
	 * @throws Exception
	 */
	int testSendMessage(String pushMessage, String dataId, String type, String mobile) throws Exception;

	/**
	 * 获取总共发了多少次推送的列表
	 * @param page
	 * @return
	 * @throws Exception
	 */
	Page getSendMessageList(Page page) throws Exception;
	

}
