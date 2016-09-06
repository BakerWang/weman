package com.enation.app.shop.core.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.shop.core.plugin.member.MemberPluginBundle;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IMemberPointManger;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;


import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;

/**
 * 会员管理
 * 
 * @author kingapex 2010-4-30上午10:07:24
 */
@SuppressWarnings({ "rawtypes", "unchecked"})
public class MemberManager extends BaseSupport<Member> implements IMemberManager {

	protected IMemberLvManager memberLvManager;
	private IMemberPointManger memberPointManger;
	private MemberPluginBundle memberPluginBundle;

	@Transactional(propagation = Propagation.REQUIRED)
	public void logout() {
		Member member = UserConext.getCurrentMember();
		member = this.get(member.getMember_id());
		ThreadContextHolder.getSessionContext().removeAttribute(UserConext.CURRENT_MEMBER_KEY);
		this.memberPluginBundle.onLogout(member);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int register(Member member) {
		int result = add(member);
		try {
			this.memberPluginBundle.onRegister(member);
			
		} catch (Exception e) {
			System.out.println(e);
		}

		return result;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int add(Member member) {
		if (member == null)
			throw new IllegalArgumentException("member is null");
		if (member.getUname() == null)
			throw new IllegalArgumentException("member' uname is null");
//		if (member.getPassword() == null)
//			throw new IllegalArgumentException("member' password is null");
//		if (member.getEmail() == null)
//			throw new IllegalArgumentException("member'email is null");

		if (this.checkname(member.getUname()) == 1) {
			return 0;
		}
		if (this.checkMobile(member.getMobile()) == 1) {
			return 0;
		}
		
		Integer lvid = memberLvManager.getDefaultLv();
		member.setLv_id(lvid);
		if(member.getName()==null)
			member.setName(member.getUname());

		member.setPoint(0);
		member.setAdvance(0D);
		
		if(member.getRegtime()==null){
			member.setRegtime(DateUtil.getDateline());
		}
		
		member.setLastlogin(DateUtil.getDateline());
		member.setLogincount(0);
		if(member.getPassword()!=null&&!"".equals(member.getPassword())){
			member.setPassword(StringUtil.md5(member.getPassword()));
		}

		// Dawei Add
		member.setMp(0);
		//member.setFace("");
		member.setMidentity(0);
 

		this.baseDaoSupport.insert("member", member);
		int memberid = this.baseDaoSupport.getLastId("member");
		member.setMember_id(memberid);

		return 1;
	}

	public void checkEmailSuccess(Member member) {
		int memberid = member.getMember_id();

		String sql = "update member set is_cheked = 1 where member_id =  " + memberid;
		this.baseDaoSupport.execute(sql);
		this.memberPluginBundle.onEmailCheck(member);
	}

	public Member get(Integer memberId) {
		String sql = "select m.*,l.name as lvname from "
				+ this.getTableName("member") + " m left join "
				+ this.getTableName("member_lv")
				+ " l on m.lv_id = l.lv_id where member_id=?";
		Member m = this.daoSupport.queryForObject(sql, Member.class, memberId);
		return m;
	}

	public Member getMemberByUname(String uname) {
		String sql = "select * from es_member where uname=?";
		List list = this.baseDaoSupport.queryForList(sql, Member.class, uname);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	public Member getMemberByEmail(String email) {
		String sql = "select * from member where email=?";
		List list = this.baseDaoSupport.queryForList(sql, Member.class, email);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	public Member edit(Member member) {
		// 前后台用到的是一个edit方法，请在action处理好
		this.baseDaoSupport.update("member", member, "member_id=" + member.getMember_id());
		Integer memberpoint = member.getPoint();
		
		//改变会员等级
		if(memberpoint!=null ){
			MemberLv lv =  this.memberLvManager.getByPoint(memberpoint);
			if(lv!=null ){
				if((member.getLv_id()==null ||lv.getLv_id().intValue()>member.getLv_id().intValue())){
					this.updateLv(member.getMember_id(), lv.getLv_id());
				} 
			}
		}
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);
		return null;
	}

	public int checkname(String name) {
		String sql = "select count(0) from member where uname=?";
		int count = this.baseDaoSupport.queryForInt(sql, name);
		count = count > 0 ? 1 : 0;
		return count;
	}

	public int checkemail(String email) {
		String sql = "select count(0) from member where email=?";
		int count = this.baseDaoSupport.queryForInt(sql, email);
		count = count > 0 ? 1 : 0;
		return count;
	}
	
	@Override
	public int checkMobile(String mobile) {
		String sql = "select count(0) from es_member where mobile=?";
		int count = this.daoSupport.queryForInt(sql, mobile);
		count = count > 0 ? 1 : 0;
		return count;
	}

	public void delete(Integer[] id) {
		if (id == null || id.equals(""))
			return;
		String id_str = StringUtil.arrayToString(id, ",");
		String sql = "delete from member where member_id in (" + id_str + ")";
		this.baseDaoSupport.execute(sql);
	}

	public void updatePassword(String password) {
		Member member = UserConext.getCurrentMember();
		this.updatePassword(member.getMember_id(), password);
		member.setPassword(StringUtil.md5(password));
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

	}

	public void updatePassword(Integer memberid, String password) {
		String md5password = password == null ? StringUtil.md5("") : StringUtil.md5(password);
		String sql = "update member set password = ? where member_id =? ";
		this.baseDaoSupport.execute(sql, md5password, memberid);
		this.memberPluginBundle.onUpdatePassword(password, memberid);
	}

	public void updateFindCode(Integer memberid, String code) {
		String sql = "update member set find_code = ? where member_id =? ";
		this.baseDaoSupport.execute(sql, code, memberid);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public Member login(String username, String password) {
//		String sql = "select m.*,l.name as lvname from "
//				+ this.getTableName("member") + " m left join "
//				+ this.getTableName("member_lv")
//				+ " l on m.lv_id = l.lv_id where m.uname=?";
//		// 用户名中包含@，说明是用邮箱登录的
//		if (username.contains("@")) {
//			sql = "select m.*,l.name as lvname from "
//					+ this.getTableName("member") + " m left join "
//					+ this.getTableName("member_lv")
//					+ " l on m.lv_id = l.lv_id where m.email=?";
//		}
//		if(password!=null&&!"".equals(password)){
//			sql = sql+" and password = '"+com.enation.framework.util.StringUtil.md5(password)+"'";
//		}
//		List<Member> list = this.daoSupport.queryForList(sql, Member.class, username);
//		if (list == null || list.isEmpty()) {
			String sql = "select m.*,l.name as lvname from "
					+ this.getTableName("member") + " m left join "
					+ this.getTableName("member_lv")
					+ " l on m.lv_id = l.lv_id where m.mobile=? ";
			if(password!=null&&!"".equals(password)){
				sql = sql+" and password = '"+com.enation.framework.util.StringUtil.md5(password)+"'";
			}
			List<Member> list = this.daoSupport.queryForList(sql, Member.class, username);
			if (list == null || list.isEmpty()) {
				return null;
			}
//		}

		Member member = list.get(0);
		long ldate = ((long) member.getLastlogin()) * 1000;
		Date date = new Date(ldate);
		Date today = new Date();
		int logincount = member.getLogincount();
		if (DateUtil.toString(date, "yyyy-MM").equals(DateUtil.toString(today, "yyyy-MM"))) {// 与上次登录在同一月内
			logincount++;
		} else {
			logincount = 1;
		}
		Long upLogintime = member.getLastlogin();// 登录积分使用
		member.setLastlogin(DateUtil.getDateline());
		member.setLogincount(logincount);
		this.edit(member);
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

//		HttpCacheManager.sessionChange();
		this.memberPluginBundle.onLogin(member, upLogintime);

		return member;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public int loginWithCookie(String username, String password) {
		String sql = "select m.*,l.name as lvname from "
				+ this.getTableName("member") + " m left join "
				+ this.getTableName("member_lv")
				+ " l on m.lv_id = l.lv_id where m.uname=? and password=?";
		// 用户名中包含@，说明是用邮箱登录的
		if (username.contains("@")) {
			sql = "select m.*,l.name as lvname from "
					+ this.getTableName("member") + " m left join "
					+ this.getTableName("member_lv")
					+ " l on m.lv_id = l.lv_id where m.email=? and password=?";
		}
		List<Member> list = this.daoSupport.queryForList(sql, Member.class,	username, password);
		if (list == null || list.isEmpty()) {
			return 0;
		}

		Member member = list.get(0);
		long ldate = ((long) member.getLastlogin()) * 1000;
		Date date = new Date(ldate);
		Date today = new Date();
		int logincount = member.getLogincount();
		if (DateUtil.toString(date, "yyyy-MM").equals(DateUtil.toString(today, "yyyy-MM"))) {// 与上次登录在同一月内
			logincount++;
		} else {
			logincount = 1;
		}
		Long upLogintime = member.getLastlogin();// 登录积分使用
		member.setLastlogin(DateUtil.getDateline());
		member.setLogincount(logincount);
		this.edit(member);
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);

		this.memberPluginBundle.onLogin(member, upLogintime);

		return 1;
	}

	/**
	 * 系统管理员作为某个会员登录
	 */
	public int loginbysys(String username) {
		 
		if (UserConext.getCurrentAdminUser()==null) {
			throw new RuntimeException("您无权进行此操作，或者您的登录已经超时");
		}

		String sql = "select m.*,l.name as lvname from "
				+ this.getTableName("member") + " m left join "
				+ this.getTableName("member_lv")
				+ " l on m.lv_id = l.lv_id where m.uname=?";
		List<Member> list = this.daoSupport.queryForList(sql, Member.class,	username);
		if (list == null || list.isEmpty()) {
			return 0;
		}

		Member member = list.get(0);
		ThreadContextHolder.getSessionContext().setAttribute(UserConext.CURRENT_MEMBER_KEY, member);
//		HttpCacheManager.sessionChange();
		return 1;
	}


	public void addMoney(Integer memberid, Double num) {
		String sql = "update member set advance=advance+? where member_id=?";
		this.baseDaoSupport.execute(sql, num, memberid);

	}

	public void cutMoney(Integer memberid, Double num) {
		Member member = this.get(memberid);
		if (member.getAdvance() < num) {
			throw new RuntimeException("预存款不足:需要[" + num + "],剩余["
					+ member.getAdvance() + "]");
		}
		String sql = "update member set advance=advance-? where member_id=?";
		this.baseDaoSupport.execute(sql, num, memberid);
	}
	

	@Override
	public Page searchMember(Map memberMap, Integer page, Integer pageSize,String other,String order) {
		String sql = createTemlSql(memberMap);
		sql+=" order by "+other+" "+order;
//		System.out.println(sql);
		Page webpage = this.daoSupport.queryForPage(sql, page, pageSize);
		
		return webpage;
	}
	
	@Override
	public List<Member> search(Map memberMap) {
		String sql = createTemlSql(memberMap);
		return this.baseDaoSupport.queryForList(sql, Member.class);
	}
	
	public void updateLv(int memberid, int lvid) {
		String sql = "update member set lv_id=? where member_id=?";
		this.baseDaoSupport.execute(sql, lvid, memberid);
	}
	
	
	private String createTemlSql(Map memberMap){

		Integer stype = (Integer) memberMap.get("stype");
		String keyword = (String) memberMap.get("keyword");
		String uname =(String) memberMap.get("uname");
		Integer mobile = (Integer) memberMap.get("mobile");
		Integer  lv_id = (Integer) memberMap.get("lvId");
		String email = (String) memberMap.get("email");
		String start_time = (String) memberMap.get("start_time");
		String end_time = (String) memberMap.get("end_time");
		Integer sex = (Integer) memberMap.get("sex");
	
		
		Integer province_id = (Integer) memberMap.get("province_id");
		Integer city_id = (Integer) memberMap.get("city_id");
		Integer region_id = (Integer) memberMap.get("region_id");
		
		
		String sql = "select m.*,lv.name as lv_name from "
			+ this.getTableName("member") + " m left join "
			+ this.getTableName("member_lv")
			+ " lv on m.lv_id = lv.lv_id where 1=1 ";
		
		if(stype!=null && keyword!=null){			
			if(stype==0){
				sql+=" and (m.uname like '%"+keyword+"%'";
				sql+=" or m.mobile like '%"+keyword+"%')";
			}
		}
		
		if(lv_id!=null && lv_id!=0){
			sql+=" and m.lv_id="+lv_id;
		}
		
		if (uname != null && !uname.equals("")) {
			sql += " and m.name like '%" + uname + "%'";
			sql += " or m.uname like '%" + uname + "%'";
		}
		if(mobile!=null){
			sql += " and m.mobile like '%" + mobile + "%'";
		}
		
		if(email!=null && !StringUtil.isEmpty(email)){
			sql+=" and m.email = '"+email+"'";
		}
		
		if(sex!=null&&sex!=2){
			sql+=" and m.sex = "+sex;
		}
		
		if(start_time!=null&&!StringUtil.isEmpty(start_time)){			
			long stime = DateUtil.getDateline(start_time+" 00:00:00");
			sql+=" and m.regtime>"+stime;
		}
		if(end_time!=null&&!StringUtil.isEmpty(end_time)){			
			long etime = DateUtil.getDateline(end_time +" 23:59:59", "yyyy-MM-dd HH:mm:ss");
			sql+=" and m.regtime<"+etime;
		}
		if(province_id!=null&&province_id!=0){
			sql+=" and province_id="+province_id;
		}
		if(city_id!=null&&city_id!=0){
			sql+=" and city_id="+city_id;
		}
		if(region_id!=null&&region_id!=0){
			sql+=" and region_id="+region_id;
		}
		
		return sql;
	}
	
	//setter getter
	
	public IMemberPointManger getMemberPointManger() {
		return memberPointManger;
	}

	public MemberPluginBundle getMemberPluginBundle() {
		return memberPluginBundle;
	}

	public void setMemberPluginBundle(MemberPluginBundle memberPluginBundle) {
		this.memberPluginBundle = memberPluginBundle;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	public void setMemberPointManger(IMemberPointManger memberPointManger) {
		this.memberPointManger = memberPointManger;
	}

	@Override
	public Member getMemberByMobile(String mobile) {
		String sql = "select * from es_member where mobile=?";
		List list = this.baseDaoSupport.queryForList(sql, Member.class, mobile);
		Member m = null;
		if (list != null && list.size() > 0) {
			m = (Member) list.get(0);
		}
		return m;
	}

	

	
}
