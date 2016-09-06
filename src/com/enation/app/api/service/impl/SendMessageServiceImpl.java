package com.enation.app.api.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.enation.app.api.model.DeviceToken;
import com.enation.app.api.pushMessage.PushMessage;
import com.enation.app.api.service.SendMessageService;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
@Service
public class SendMessageServiceImpl extends BaseSupport implements SendMessageService{

	@Override
	public void saveSendMessage(String pushMessage, String dataId, String type) throws Exception {
		String sql ="select *,count(distinct clientid) from es_api_devicetoken group by clientid HAVING clientId is not null";
		List<DeviceToken> dts = this.daoSupport.queryForList(sql, DeviceToken.class);
		if(dts!=null&&dts.size()>0){
			String insertSql = "insert into es_api_push_message (content,member_id,data_id,type,status,create_time) values ";
			for(DeviceToken dt :dts){
				if(dt.getMember_id()==null){
					dt.setMember_id(100);//没有注册的用户id设置为空100
				}
				insertSql = insertSql +"('"+pushMessage+"',"+dt.getMember_id()+",'"+dataId+"','"+type+"',0,"+new Date().getTime()+"),";
			}
			insertSql = insertSql.substring(0,insertSql.lastIndexOf(","));
			this.daoSupport.execute(insertSql);
			Map<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("type", type);
			if("product".equals(type)){
				String gsql = "select eg.url as url from es_goods eg where eg.goods_id = ?";
				Map<String,Object> map = this.baseDaoSupport.queryForMap(gsql, dataId);
				if(map!=null){
					map.put("dataId", (String)map.get("url"));
				}
			}else{
				dataMap.put("dataId", dataId);
			}
			PushMessage.pushUserList(pushMessage, dataMap, dts);
		}
	}

	@Override
	public int testSendMessage(String pushMessage, String dataId, String type, String mobile) throws Exception {
		String sql = "select ead.* from es_api_devicetoken ead left join es_member em on em.member_id = ead.member_id where em.mobile = ?";
		DeviceToken dt = (DeviceToken) this.baseDaoSupport.queryForObject(sql, DeviceToken.class, mobile);
		if(dt!=null){
			Map<String,String> dataMap = new HashMap<String,String>();
			dataMap.put("type", type);
			if("product".equals(type)){
				String gsql = "select eg.url as url from es_goods eg where eg.goods_id = ?";
				List<Map<String,Object>> map = this.baseDaoSupport.queryForList(gsql, dataId);
				if(map!=null&&map.size()>0){
					dataMap.put("dataId", (String)(map.get(0).get("url")));
				}
			}else{
				dataMap.put("dataId", dataId);
			}
			PushMessage.pushSingleUser(pushMessage, dataMap, dt);
			return 1;
		}else{
			return 0;
		}
	}

	@Override
	public Page getSendMessageList(Page page) throws Exception {
		String sql ="select eapm.content,count(*) as count,data_id as dataId,eapm.type,eapm.create_time,"
				+ " (select count(*) from es_api_push_message emm where emm.content = eapm.content and emm.status = 1) as readCount, "
				+ " eat.id as themeid,eat.title as themetitle,eg.goods_id as productid,eg.name as productname "
				+ " from es_api_push_message eapm "
				+ " left join es_api_theme eat on eat.id = eapm.data_id "
				+ " left join es_goods eg on eg.goods_id = eapm.data_id "
				+ " group by eapm.create_time";
		List<Map<String,Object>> list = this.daoSupport.queryForListPage(sql, (int)page.getCurrentPageNo(), page.getPageSize());
		String listSql = "select count(distinct eapm.create_time) from es_api_push_message eapm";
		return new Page(0, this.baseDaoSupport.queryForInt(listSql), page.getPageSize(), list);
	}

	
	
}
