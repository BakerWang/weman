package com.enation.app.b2b2c.core.service.member.impl;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;

@Component
public class StoreMemberManager extends BaseSupport implements IStoreMemberManager{
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberManager#edit(com.enation.app.b2b2c.core.model.member.StoreMember)
	 */
	public void edit(StoreMember member) {
		this.baseDaoSupport.update("member", member, "member_id=" + member.getMember_id());
		ThreadContextHolder.getSessionContext().setAttribute(this.CURRENT_STORE_MEMBER_KEY, member);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberManager#getMember(java.lang.Integer)
	 */
	@Override
	public StoreMember getMember(Integer member_id) {
		String sql="select * from es_member where member_id=?";
		return (StoreMember) this.daoSupport.queryForObject(sql, StoreMember.class, member_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IB2b2cMemberManager#getStoreMember()
	 */
	@Override
	public StoreMember getStoreMember() {
		StoreMember member=(StoreMember) ThreadContextHolder.getSessionContext().getAttribute(this.CURRENT_STORE_MEMBER_KEY);
		return member;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.member.IStoreMemberManager#getMember(java.lang.String)
	 */
	@Override
	public StoreMember getMember(String member_name) {
		String sql="select * from es_member where uname=?";
		return (StoreMember) this.daoSupport.queryForObject(sql, StoreMember.class,member_name );
	}
	
}

