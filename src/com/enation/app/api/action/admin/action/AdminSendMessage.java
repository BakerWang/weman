package com.enation.app.api.action.admin.action;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.service.SendMessageService;
import com.enation.framework.database.Page;
@Scope("prototype")
public class AdminSendMessage extends BaseAction {

	private static final long serialVersionUID = 1L;

	@Resource
	private SendMessageService sendMessageService;
	
	/**
	 * 到新增发送消息jsp
	 * @return
	 */
	public String addSendMessageJsp(){
		return "addSendMessageJspSuccess";
	}
	
	/**
	 * 推送列表
	 * @return
	 */
	public String sendMessageList(){
		try {
			page.setPageSize(20);
			Page resPage = sendMessageService.getSendMessageList(page);
			request.setAttribute("page", resPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sendMessageListSuccess";
	}
	
	
	public void saveSendMessage(){
		try {
			String pushMessage = request.getParameter("pushMessage");
			String dataId = request.getParameter("data_id");
			String type = request.getParameter("type");
			sendMessageService.saveSendMessage(pushMessage,dataId,type);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	public void testSendMessage(){
		try {
			String pushMessage = request.getParameter("pushMessage");
			String dataId = request.getParameter("data_id");
			String type = request.getParameter("type");
			String mobile = request.getParameter("mobile");
			int resint = sendMessageService.testSendMessage(pushMessage,dataId,type,mobile);
			if(resint == 0){
				jsonObject.put("reason", "手机号错误");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
}
