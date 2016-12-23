package com.enation.app.api.action.admin.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
			page.setPageSize(10);
			String pageNo = request.getParameter("pageNo");
			if (pageNo == null || "".equals(pageNo)) {
				pageNo = "1";
			}
			page.setCurrentPageNo(Long.parseLong(pageNo));
			Page resPage = sendMessageService.getSendMessageList(page);
			resPage.setCurrentPageNo(Long.parseLong(pageNo));
			request.setAttribute("page", resPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "sendMessageListSuccess";
	}
	
	public void saveSendMessageTask(){
		try {
			String pushMessage = request.getParameter("pushMessage");
			String dataId = request.getParameter("data_id");
			String type = request.getParameter("type");
			String startTime = request.getParameter("startTime");
			sendMessageService.saveSendTimeMessage(pushMessage,dataId,type,startTime);//保存定时发送表
			//sendMessageService.saveSendMessage(pushMessage,dataId,type);//发送定时推送
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	public void saveSendMessage(){
		try {
			String pushMessage = request.getParameter("pushMessage");
			String dataId = request.getParameter("data_id");
			String type = request.getParameter("type");
			//String startTime = request.getParameter("startTime");
			//sendMessageService.saveSendTimeMessage(pushMessage,dataId,type,startTime);//保存定时发送表
			sendMessageService.saveSendMessage(pushMessage,dataId,type);//发送定时推送
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
