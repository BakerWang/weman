package com.enation.app.api.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;


public class Utils {

	/**
	 * 判断是否是手机
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isTelphone(String str) {
		String regExp = "^[1]\\d{10}$";
		Pattern p = Pattern.compile(regExp);
		Matcher m = p.matcher(str);
		return m.matches();
	}

	/**
	 * 手机发送注册码
	 * 
	 * @param phone
	 * @return
	 */
	public static String sendCode(String phone,int type) {
		String rd = getNumRandom(4);
		String content = "";
		if(type==2){
			content = rd+"(忘记密码验证码)【WeMan】";
		}else if(type==1){
			content = rd+"(注册验证码)【WeMan】";
		}else{
			content = "欢迎使用WeMan";
		}
		Boolean result = SmsUtils.sendSms(phone, content);
		if (result) {
			return rd;
		} else {
			return null;
		}
	}
	
	/**
	 * 获取length个数字随机数
	 * @param length
	 * @return
	 */
	public static String getNumRandom(int length) {
		String val = "";
		java.util.Random rd = new java.util.Random();
		for (int i = 0; i < length; i++) {
			val += String.valueOf(rd.nextInt(10));
		}
		return val;
	}
	
}
