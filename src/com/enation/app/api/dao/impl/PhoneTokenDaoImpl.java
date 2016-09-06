package com.enation.app.api.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Repository;

import com.enation.app.api.dao.PhoneTokenDao;
import com.enation.app.api.model.PhoneToken;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.util.StringUtil;

@Repository
public class PhoneTokenDaoImpl extends BaseSupport<PhoneToken> implements PhoneTokenDao{

	@Override
	public PhoneToken getPhoneToken(Integer memberId) {
		String sql = "select * from api_phoneToken pt where pt.member_id = ?";
		PhoneToken pt = this.baseDaoSupport.queryForObject(sql, PhoneToken.class, memberId);
		if(pt != null){
			if(this.getPostponeDate(pt.getExpireDate()).getTime()<new Date().getTime()){
				return null;
			}else{
				pt.setExpireDate(new Date().getTime());
				this.baseDaoSupport.update("api_phoneToken", pt," id = "+pt.getId());
			}
		}
		return pt;
	}

	@Override
	public PhoneToken savePhoneToken(PhoneToken phoneToken) {
		phoneToken.setExpireDate(new Date().getTime());
		phoneToken.setToken(this.getToken(phoneToken.getMember_id().toString()));
		this.baseDaoSupport.insert("es_api_phoneToken", phoneToken);
		phoneToken.setId(this.baseDaoSupport.getLastId("phoneToken"));
		return phoneToken;
	}


	private String getToken(String userId){
		String str = userId+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		return StringUtil.md5(str);
	}
	
	@Override
	public PhoneToken updatePhoneToken(PhoneToken phoneToken) {
		phoneToken.setExpireDate(new Date().getTime());
		phoneToken.setToken(this.getToken(phoneToken.getMember_id().toString()));
		this.baseDaoSupport.update("api_phoneToken", phoneToken, " id = "+phoneToken.getId());
		return phoneToken;
	}
	
	
	
	/**
	 * 获取token过期日期
	 * @param date
	 * @return
	 */
	private Date getPostponeDate(Long date){
		Calendar c = Calendar.getInstance();
		c.setTime(new Date(date));
		c.add(Calendar.MONTH, 1);
		return c.getTime();
	}

	@Override
	public int getMemberIdByAccessToken(String accessToken) {
		String sql = "select * from es_api_phoneToken apt where apt.token = ? ";
		PhoneToken pt = this.daoSupport.queryForObject(sql, PhoneToken.class, accessToken);
		if(pt==null||this.getPostponeDate(pt.getExpireDate()).getTime()<new Date().getTime()){
			return 0;
		}else{
			return pt.getMember_id();
		}
	}

}
