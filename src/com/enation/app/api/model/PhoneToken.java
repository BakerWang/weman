package com.enation.app.api.model;


/**
 * 手机登陆的token类
 * @author tcardz-0000
 *
 */
public class PhoneToken implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer member_id;	//会员Id
	private String token;
	private Long expireDate;
	
	
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
	
	public Long getExpireDate() {
		return expireDate;
	}
	public void setExpireDate(Long expireDate) {
		this.expireDate = expireDate;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	
	
}
