package com.enation.app.api.test;

import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

public class SendEmail {

	
	public static void main(String[] args) {
		try {
			JavaMailSenderImpl javaMailSender= new JavaMailSenderImpl();
			javaMailSender.setHost("smtp.exmail.qq.com");
			javaMailSender.setUsername("574964930@qq.com");
			javaMailSender.setPassword("@jj202228");//sytcdpvhitcubfef  ///ffyxziewbrjlbajd   //rvtljhbsrkhvbcgc
			//配置文件，用于实例化java.mail.session    
	        Properties pro = System.getProperties();    
	            
	        //登录SMTP服务器,需要获得授权，网易163邮箱新近注册的邮箱均不能授权。    
	        //测试 sohu 的邮箱可以获得授权    
	        pro.put("mail.smtp.auth", "true");    
	        pro.put("mail.smtp.ssl.enable", "true");    
	        pro.put("mail.smtp.socketFactory.fallback", "false");    
	            
	        //通过文件获取信息    
	        javaMailSender.setJavaMailProperties(pro); 
			MimeMessage message = javaMailSender.createMimeMessage();	
			MimeMessageHelper helper = new MimeMessageHelper(message, true,"UTF-8");
			helper.setSubject("测试");
			helper.setTo("89987531@qq.com");
			helper.setFrom("574964930@qq.com");
			helper.setText("<body><p>Hello Html Email</p></body>", true);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
}
