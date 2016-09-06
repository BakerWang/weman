package com.enation.app.api.model;

import java.util.Date;

/**
 * 设备的token
 * @author tcardz-0000
 *
 */
public class DeviceToken implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Integer member_id;	//会员Id
	private String token;//苹果的设备id、安卓的设备id
	private String tokenType = TOKENTYPE_IPHONE;
	private String clientId ;//个推的唯一标示
	private Long create_time;
	
	public static String TOKENTYPE_IPHONE = "iphone";
	public static String TOKENTYPE_ANDROID = "android";
	
	
	public DeviceToken(){
	}
	
	/**
	 * 苹果设备对象构造方法
	 * @param token
	 * @param u
	 */
	public DeviceToken(String token,Integer member_id){
		this.member_id = member_id;
		this.token = token;
		this.tokenType = TOKENTYPE_IPHONE;
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMember_id() {
		return member_id;
	}

	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}

	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}

	public Long getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	
	
}
