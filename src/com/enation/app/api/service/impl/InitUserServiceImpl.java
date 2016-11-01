package com.enation.app.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.api.dao.DeviceTokenDao;
import com.enation.app.api.dao.PhoneTokenDao;
import com.enation.app.api.model.DeviceToken;
import com.enation.app.api.model.PhoneToken;
import com.enation.app.api.service.ArticleService;
import com.enation.app.api.service.InitUserService;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;

@SuppressWarnings("rawtypes")
@Service
public class InitUserServiceImpl extends BaseSupport implements InitUserService{

	@Resource
	private IMemberManager memberManager;
	@Resource
	private PhoneTokenDao phoneTokenDao;
	@Resource
	private DeviceTokenDao deviceTokenDao;
	@Resource
	private ArticleService articleService;
	
	
	@Transactional(propagation = Propagation.REQUIRED)
	public int register(Member member,String notifyType,String deviceToken,String bindType, String bindNum) {
		int res = memberManager.add(member);
		if(res == 1){
			if(bindType!=null&&bindNum!=null&&!"".equals(bindType)&&!"".equals(bindNum)){//绑定账号添加
				Map<String,Object> bindAccount=new HashMap<String,Object>();
				bindAccount.put("member_id", member.getMember_id());
				bindAccount.put("bind_type", bindType);
				bindAccount.put("bind_num", bindNum);
				bindAccount.put("create_time", new Date().getTime());
				bindAccount.put("status", "1");
				this.daoSupport.insert("es_api_bind_account", bindAccount);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("is_store", 0);
			daoSupport.update("es_member",map , "member_id="+member.getMember_id());
			PhoneToken phoneToken = new PhoneToken();
			phoneToken.setMember_id(member.getMember_id());
			phoneTokenDao.savePhoneToken(phoneToken);
			
			String sql="select ead.* from es_api_devicetoken ead where ead.deviceToken = ? and ead.tokenType = ?";
			List<DeviceToken> dts = this.daoSupport.queryForList(sql, DeviceToken.class, deviceToken,notifyType);
			if(dts==null||dts.size()==0){
				String sql2="select ead.* from es_api_devicetoken ead where ead.member_id = ? and tokenType = ?";
				List<DeviceToken> dtss = this.daoSupport.queryForList(sql2, DeviceToken.class, member.getMember_id(),notifyType);
				if(dtss!=null&&dtss.size()>0){
					this.daoSupport.execute("delete from es_api_devicetoken where member_id = ? and tokenType = ?", member.getMember_id(),notifyType);
				}
				DeviceToken dt = new DeviceToken();
				dt.setMember_id(member.getMember_id());
				dt.setDeviceToken(deviceToken);
				dt.setTokenType(notifyType);
				dt.setCreate_time(new Date().getTime());
				deviceTokenDao.saveDeviceToken(dt);
			}else{
				if(dts.get(0).getMember_id()==null){
					String updatesql = "update es_api_devicetoken ead set ead.member_id = ?  where ead.deviceToken = ? and ead.tokenType = ?";
					this.daoSupport.execute(updatesql, member.getMember_id(),deviceToken,notifyType);
				}
			}
			
			memberManager.login(member.getUname(), member.getPassword());
			return 1;
		}else{
			return 0;
		}
	}


	@Override
	public Map<String, Object> login_verifybind(String bnum, String type) {
		String sql = "select am.member_id as id,am.uname as musername,am.face as mface,ap.token as ptoken "
				+ " from es_api_bind_account aba "
				+ " left join es_member am on am.member_id = aba.member_id "
				+ " left join es_api_phoneToken ap on am.member_id = ap.member_id "
				+ " where bind_num = ? and bind_type = ? and status = 1";
		List<Map> resList = (List<Map>)daoSupport.queryForList(sql, bnum , type);
		if(resList==null||resList.size()==0){
			return null;
		}else{
			Map map = resList.get(0);
			int id = (int)map.get("id");
			String updatesql = "update es_api_phoneToken ead set ead.expireDate = ? where ead.member_id= ?";
			this.daoSupport.execute(updatesql,new Date().getTime(),id);
			Map<String, Object> resMap = new HashMap<String,Object>();
			resMap.put("username",(String)map.get("musername"));
			resMap.put("face",(String)map.get("mface"));
			resMap.put("token",(String)map.get("ptoken"));
			return resMap;
		}
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public Map<String, Object> login(String account, String password,String deviceToken, String clientType,String clientId) {
		Member member = memberManager.login(account, password);
		if(member==null){
			return null;
		}else{
			Map<String, Object> resMap = new HashMap<String,Object>();
			String tsql = "select * from es_api_phoneToken where member_id = ? ";
			PhoneToken pt = (PhoneToken) this.daoSupport.queryForObject(tsql, PhoneToken.class, member.getMember_id());
			if(pt==null){
				pt = new PhoneToken();
				pt.setMember_id(member.getMember_id());
				pt = phoneTokenDao.savePhoneToken(pt);
			}else{
				pt = phoneTokenDao.updatePhoneToken(pt);
			}
			resMap.put("token", pt.getToken());
			resMap.put("username",member.getUname());
			resMap.put("face",member.getFace());
			
			String csql="select ead.* from es_api_devicetoken ead where ead.deviceToken = ? and tokenType = ?";
			List<DeviceToken> cdts = this.daoSupport.queryForList(csql, DeviceToken.class, deviceToken,clientType);
			if(cdts==null||cdts.size()==0){
				String sql="select ead.* from es_api_devicetoken ead where ead.member_id = ? and tokenType = ?";
				List<DeviceToken> dts = this.daoSupport.queryForList(sql, DeviceToken.class, member.getMember_id(),clientType);
				if(dts!=null&&dts.size()>0){
					this.daoSupport.execute("delete from es_api_devicetoken where member_id = ? and tokenType = ?", member.getMember_id(),clientType);
					this.daoSupport.execute("delete from es_api_devicetoken where clientId = ? and tokenType = ? and member_id is null", clientId,clientType);
				}
				DeviceToken dt = new DeviceToken();
				dt.setDeviceToken(deviceToken);
				dt.setTokenType(clientType);
				dt.setCreate_time(new Date().getTime());
				dt.setMember_id(member.getMember_id());
				dt.setClientId(clientId);
				deviceTokenDao.saveDeviceToken(dt);
			}else{
				String updatesql = "update es_api_devicetoken ead set ead.member_id = ? where ead.deviceToken = ?  and ead.tokenType = ?";
				this.daoSupport.execute(updatesql,member.getMember_id(),deviceToken,clientType);
			}
			return resMap;
		}
	}


	@Override
	public Map<String, Object> catchAppInitMessage(String clientType) {
		Map<String, Object> resMap = new HashMap<String,Object>();
		String sql = "select value from es_api_globalconstants ag where ag.key = ?";
		List<Map> resList = (List<Map>)daoSupport.queryForList(sql, clientType+"version");
		if(resList==null||resList.size()==0){
			return null;
		}else{
			Map map = resList.get(0);
			resMap.put("version", map.get("value"));
		}
		try {
			Page page = new Page();
			page.setCurrentPageNo(1);
			page.setPageSize(10);
			Page respage = articleService.getArticleTagList(page);
			List<Map<String,Object>> rlist = (List<Map<String, Object>>) respage.getResult();
			String tags = "";
			for(Map<String,Object> mm:rlist){
				tags = tags+mm.get("ccontent")+",";
			}
			if(!"".equals(tags)){
				tags = tags.substring(0,tags.length()-1);
				resMap.put("hotTags", tags);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resMap;
	}


	@Override
	public String getAccessTokenByUname(String username) throws Exception {
		String sql = "select member_id from es_member where uname = ?";
		int mid = this.daoSupport.queryForInt(sql, username);
		PhoneToken pt = phoneTokenDao.getPhoneToken(mid);
		if(pt==null){
			return null;
		}
		return pt.getToken();
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public int getMemberIdByAccessToken(String accessToken) {
		return phoneTokenDao.getMemberIdByAccessToken(accessToken);
	}


	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void bindClientId(String deviceToken, String clientId,String clientType,int memberId) throws Exception {
		if(memberId!=0){
			String sql="select ead.* from es_api_devicetoken ead where ead.member_id = ? and ead.tokenType = ?";
			List<DeviceToken> dts = this.daoSupport.queryForList(sql, DeviceToken.class, memberId,clientType);
			if(dts!=null&&dts.size()>0){
				String updatesql = "update es_api_devicetoken ead set ead.deviceToken = ? , ead.clientId = ?  where ead.member_id = ? and ead.tokenType = ?";
				//logger.error("参数：memberId："+memberId+"||clientId:"+clientId+"||deviceToken:"+deviceToken+"||clientType:"+clientType);
				this.daoSupport.execute(updatesql,deviceToken,clientId,memberId,clientType);
			}else{
				DeviceToken dt = new DeviceToken();
				dt.setClientId(clientId);
				dt.setCreate_time(new Date().getTime());
				dt.setDeviceToken(deviceToken);
				dt.setTokenType(clientType);
				dt.setMember_id(memberId);
				deviceTokenDao.saveDeviceToken(dt);
			}
		}else{
			String sql="select ead.* from es_api_devicetoken ead where ead.deviceToken = ? and ead.tokenType = ?";
			List<DeviceToken> dts = this.daoSupport.queryForList(sql, DeviceToken.class, deviceToken,clientType);
			if(dts==null||dts.size()==0){
				DeviceToken dt = new DeviceToken();
				dt.setClientId(clientId);
				dt.setCreate_time(new Date().getTime());
				dt.setDeviceToken(deviceToken);
				dt.setTokenType(clientType);
				deviceTokenDao.saveDeviceToken(dt);
			}else{
				String oldclientId = dts.get(0).getClientId();
				if(oldclientId!=null&&!oldclientId.equals(clientId)){
					String updatesql = "update es_api_devicetoken ead set ead.clientId = ?  where ead.deviceToken = ? and ead.tokenType = ?";
					this.daoSupport.execute(updatesql, clientId,deviceToken,clientType);
				}
			}
		}
	}

	@Override
	public Map<String, Object> shareUrl(String type, int dataId) throws Exception {
		if("theme".equals(type)){
			String sql ="select eat.id as id,eat.details as details,eat.minorImage as image,eat.title as title from es_api_theme eat where eat.id = ?";
			List<Map<String,Object>> resList = this.daoSupport.queryForList(sql, dataId);
			if(resList!=null){
				return resList.get(0);
			}
		}else {//article
			String articleSql ="select waa.id as id,waa.content as details,waa.image as image from wh_api_article waa where waa.id = ?";
			List<Map<String,Object>> resList = this.daoSupport.queryForList(articleSql, dataId);
			if(resList!=null){
				return resList.get(0);
			}
		}
		return null;
	}



}
