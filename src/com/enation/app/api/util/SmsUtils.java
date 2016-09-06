package com.enation.app.api.util;
import java.io.ByteArrayInputStream;
import java.net.URLEncoder;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class SmsUtils {
	private final static String ACCOUNT="dakeshi106";
	private final static String PASSWORD="dksi106129.com";
	private final static String USERID="285";
	private final static String URL = "http://www.591duanxin.com/sms.aspx";
	private final static String URLAPI="http://www.591duanxin.com/callApi.aspx";
	private final static String NEWACCOUNT="dake106";
	private final static String NEWPASSWORD="dake520";
	private final static String NEWURL="http://139.129.128.71:8086/msgHttp/json/mt";
	
	public static Boolean sendSms(String mobile,String content){
		String str = SmsClientSend.sendSms(NEWURL,
				NEWACCOUNT, NEWPASSWORD, mobile, content);
		JSONObject obj = JSONObject.fromObject(str);
		int rspcode = (int) JSONObject.fromObject(JSONArray.fromObject(obj.get("Rets")).get(0)).get("Rspcode");
		System.out.println(rspcode);
		if(rspcode==0){
			return true;
		}
		return false;
	}
	
	public static Boolean sendSms2(String mobile, String content) {
		String  str=sendSms(URL, USERID, ACCOUNT, PASSWORD, mobile, content, null,
				null, null, null, null, null, null, "POST", "UTF-8", "UTF-8");
		SAXReader saxReader = new SAXReader();
		saxReader.setEncoding("utf-8");
		Document doc = null;
		//String str = "<?xml version=\"1.0\" encoding=\"utf-8\" ?><returnsms><returnstatus>Success</returnstatus><message>你的余额不足，请充值</message><remainpoint>3</remainpoint><taskID>8922816</taskID><successCounts>2</successCounts></returnsms>";
		try {
			ByteArrayInputStream stream = new ByteArrayInputStream(str.getBytes("utf-8"));
			doc = saxReader.read(stream);
			Element rootElt = doc.getRootElement(); // 获取根节点
			String el=rootElt.element("returnstatus").getText();
			if("Success".equals(el.toString())){
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>发送信息方法--暂时私有化，这里仅仅是提供用户接口而已。其实用不了那么复杂</span><br/>
	 * <span>发送信息最终的组合形如：http://118.145.30.35/sms.aspx?action=send</span>
	 * </p>
	 * 
	 * @param url
	 *            ：必填--发送连接地址URL--比如>http://118.145.30.35/sms.aspx
	 * 
	 * @param userid
	 *            ：必填--用户ID，为数字
	 * @param account
	 *            ：必填--用户帐号
	 * @param password
	 *            ：必填--用户密码
	 * @param mobile
	 *            ：必填--发送的手机号码，多个可以用逗号隔比如>13512345678,13612345678
	 * @param content
	 *            ：必填--实际发送内容，
	 * @param action
	 *            ：选填--访问的事件，默认为send
	 * @param sendTime
	 *            ：选填--定时发送时间，不填则为立即发送，时间格式如>2011-11-11 11:11:11
	 * @param checkContent
	 *            ：选填--检查是否包含非法关键字，1--表示需要检查，0--表示不检查
	 * @param taskName
	 *            ：选填--任务名称，本次任务描述，100字内
	 * @param countNumber
	 *            ：选填--提交号码总数
	 * @param mobileNumber
	 *            ：选填--手机号码总数
	 * @param telephoneNumber
	 *            ：选填--小灵通（和）或座机总数
	 * @param sendType
	 *            ：选填--发送方式，默认为POST
	 * @param codingType
	 *            ：选填--发送内容编码方式，默认为UTF-8
	 * @param backEncodType
	 *            ：选填--返回内容编码方式，默认为UTF-8
	 * @return 返回发送之后收到的信息
	 */
	private static String sendSms(String url, String userid, String account,
			String password, String mobile, String content, String action,
			String sendTime, String checkContent, String taskName,
			String countNumber, String mobileNumber, String telephoneNumber,
			String sendType, String codingType, String backEncodType) {

		try {
			if (codingType == null || codingType.equals("")) {
				codingType = "UTF-8";
			}
			if (backEncodType == null || backEncodType.equals("")) {
				backEncodType = "UTF-8";
			}
			StringBuffer send = new StringBuffer();
			if (action != null && !action.equals("")) {
				send.append("action=").append(action);
			} else {
				send.append("action=send");
			}

			send.append("&userid=").append(userid);
			send.append("&account=").append(
					URLEncoder.encode(account, codingType));
			send.append("&password=").append(
					URLEncoder.encode(password, codingType));
			send.append("&mobile=").append(mobile);
			send.append("&content=").append(
					URLEncoder.encode(content, codingType));
			if (sendTime != null && !sendTime.equals("")) {
				send.append("&sendTime=").append(
						URLEncoder.encode(sendTime, codingType));
			}
			if (checkContent != null && !checkContent.equals("")) {
				send.append("&checkContent=").append(checkContent);
			}
			if (taskName != null && !taskName.equals("")) {
				send.append("&taskName=").append(
						URLEncoder.encode(taskName, codingType));
			}
			if (countNumber != null && !countNumber.equals("")) {
				send.append("&countNumber=").append(countNumber);
			}
			if (mobileNumber != null && !mobileNumber.equals("")) {
				send.append("&mobileNumber=").append(mobileNumber);
			}
			if (telephoneNumber != null && !telephoneNumber.equals("")) {
				send.append("&telephoneNumber=").append(telephoneNumber);
			}

			if (sendType != null && (sendType.toLowerCase()).equals("get")) {
				return SmsClientAccessTool.getInstance().doAccessHTTPGet(
						url + "?" + send.toString(), backEncodType);
			} else {
				return SmsClientAccessTool.getInstance().doAccessHTTPPost(url,
						send.toString(), backEncodType);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "未发送，编码异常";
		}
	}

	/**
	 * /**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>是否包含关键字获取方法1--必须传入必填内容</span><br/>
	 * <p>
	 * 其一：发送方式，默认为POST<br/>
	 * 其二：发送内容编码方式，默认为UTF-8
	 * </p>
	 * <br/>
	 * </p>
	 * 
	 * @param url
	 *            ：必填--发送连接地址URL--比如>http://118.145.30.35/sms.aspx
	 * @param userid
	 *            ：必填--用户ID，为数字
	 * @param account
	 *            ：必填--用户帐号
	 * @param password
	 *            ：必填--用户密码
	 * @param checkWord
	 *            ：必填--需要检查的字符串--比如：这个字符串中是否包含了屏蔽字
	 * @return 返回需要查询的字符串中是否包含关键字
	 */
	public static String queryKeyWord( String checkWord) {

		try {
			StringBuffer sendParam = new StringBuffer();
			sendParam.append("action=checkkeyword");
			sendParam.append("&userid=").append(USERID);
			sendParam.append("&account=").append(
					URLEncoder.encode(ACCOUNT, "UTF-8"));
			sendParam.append("&password=").append(
					URLEncoder.encode(PASSWORD, "UTF-8"));
			if (checkWord != null && !checkWord.equals("")) {
				sendParam.append("&content=").append(
						URLEncoder.encode(checkWord, "UTF-8"));
			} else {
				return "需要检查的字符串不能为空";
			}
			return SmsClientAccessTool.getInstance().doAccessHTTPPost(URL,
					sendParam.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "未发送，异常-->" + e.getMessage();
		}
	}

	/**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>余额获取方法1--必须传入必填内容</span><br/>
	 * <p>
	 * 其一：发送方式，默认为POST<br/>
	 * 其二：发送内容编码方式，默认为UTF-8
	 * </p>
	 * <br/>
	 * </p>
	 * 
	 * @param url
	 *            ：必填--发送连接地址URL--比如>http://118.145.30.35/sms.aspx
	 * @param userid
	 *            ：必填--用户ID，为数字
	 * @param account
	 *            ：必填--用户帐号
	 * @param password
	 *            ：必填--用户密码
	 * @return 返回余额查询字符串
	 */
	public static String queryOverage() {

		try {
			StringBuffer sendParam = new StringBuffer();
			sendParam.append("action=overage");
			sendParam.append("&userid=").append(USERID);
			sendParam.append("&account=").append(
					URLEncoder.encode(ACCOUNT, "UTF-8"));
			sendParam.append("&password=").append(
					URLEncoder.encode(PASSWORD, "UTF-8"));

			return SmsClientAccessTool.getInstance().doAccessHTTPPost(URL,
					sendParam.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "未发送，异常-->" + e.getMessage();
		}
	}

	/**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>状态报告获取方法1--必须传入必填内容</span><br/>
	 * <p>
	 * 其一：发送方式，默认为POST<br/>
	 * 其二：发送内容编码方式，默认为UTF-8
	 * </p>
	 * <br/>
	 * </p>
	 * 
	 * @param url
	 *            ：必填--发送连接地址URL--比如>http://118.145.30.35/statusApi.aspx
	 * @param userid
	 *            ：必填--用户ID，为数字
	 * @param account
	 *            ：必填--用户帐号
	 * @param password
	 *            ：必填--用户密码
	 * @return 返回状态报告
	 */
	public static String queryStatusReport() {

		try {
			StringBuffer sendParam = new StringBuffer();
			sendParam.append("action=query");
			sendParam.append("&userid=").append(USERID);
			sendParam.append("&account=").append(
					URLEncoder.encode(ACCOUNT, "UTF-8"));
			sendParam.append("&password=").append(
					URLEncoder.encode(PASSWORD, "UTF-8"));

			return SmsClientAccessTool.getInstance().doAccessHTTPPost(URLAPI,
					sendParam.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "未发送，异常-->" + e.getMessage();
		}
	}

	/**
	 * <p>
	 * <date>2012-03-01</date><br/>
	 * <span>上行（用户回复）获取方法1--必须传入必填内容</span><br/>
	 * <p>
	 * 其一：发送方式，默认为POST<br/>
	 * 其二：发送内容编码方式，默认为UTF-8
	 * </p>
	 * <br/>
	 * </p>
	 * 
	 * @param url
	 *            ：必填--发送连接地址URL--比如>http://118.145.30.35/callApi.aspx
	 * @param userid
	 *            ：必填--用户ID，为数字
	 * @param account
	 *            ：必填--用户帐号
	 * @param password
	 *            ：必填--用户密码
	 * @return 返回状态报告
	 */
	public static String queryReplay() {

		try {
			StringBuffer sendParam = new StringBuffer();
			sendParam.append("action=query");
			sendParam.append("&userid=").append(USERID);
			sendParam.append("&account=").append(
					URLEncoder.encode(ACCOUNT, "UTF-8"));
			sendParam.append("&password=").append(
					URLEncoder.encode(PASSWORD, "UTF-8"));
			
			return SmsClientAccessTool.getInstance().doAccessHTTPPost(URLAPI,
					sendParam.toString(), "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			return "未发送，异常-->" + e.getMessage();
		}
	}

}
