package com.enation.app.b2b2c.core.service.store.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.component.plugin.store.StorePluginBundle;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.b2b2c.core.service.store.IStoreSildeManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.service.IRegionsManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
@Component
public class StoreManager  extends BaseSupport implements IStoreManager{
	private IStoreMemberManager storeMemberManager;
	private IStoreSildeManager storeSildeManager;
	private IRegionsManager regionsManager;
	private StorePluginBundle storePluginBundle;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#apply(com.enation.app.b2b2c.core.model.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void apply(Store store) {
		//获取当前用户信息
		Member member=storeMemberManager.getStoreMember();
		if (member != null) {
			store.setMember_id(member.getMember_id());
			store.setMember_name(member.getUname());
		}
		this.getStoreRegions(store);
		this.daoSupport.insert("es_store", store);
		store.setStore_id(this.daoSupport.getLastId("es_store"));
		storePluginBundle.onAfterApply(store);
	}
	/**
	 * 获取店铺地址
	 * @param store
	 */
	private void getStoreRegions(Store store){
		store.setStore_province(regionsManager.get(store.getStore_provinceid()).getLocal_name());
		store.setStore_city(regionsManager.get(store.getStore_cityid()).getLocal_name());
		store.setStore_region(regionsManager.get(store.getStore_regionid()).getLocal_name());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#audit_pass(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void audit_pass(Integer member_id,Integer storeId,Integer pass,Integer name_auth,Integer store_auth,Double commission) {
		if(pass==1){
			store_auth=store_auth==null?0:store_auth;
			name_auth=name_auth==null?0:name_auth;
			this.daoSupport.execute("update es_store set create_time=?,name_auth=?,store_auth=?,commission=? where store_id=?",DateUtil.getDateline(),name_auth,store_auth,commission, storeId);
			this.editStoredis(1, storeId);
			storePluginBundle.onAfterPass(this.getStore(storeId));
		}else{
			//审核未通过
			this.daoSupport.execute("update es_store set disabled=? where store_id=?",-1,storeId);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#store_list(java.util.Map, int, int)
	 */
	@Override
	public Page store_list(Map other,Integer disabled,int pageNo,int pageSize) {
		StringBuffer sql=new StringBuffer("");
		disabled=disabled==null?1:disabled;
		String store_name=other.get("name")==null?"":other.get("name").toString();
		String searchType=other.get("searchType")==null?"":other.get("searchType").toString();
		
		//店铺状态
		if(disabled.equals(-2)){
			sql.append("select s.* from es_store s   where  disabled!="+disabled);
		}
		else{
			sql.append("select s.* from es_store s   where  disabled="+disabled);
		}
		if(!StringUtil.isEmpty(store_name)){
			sql.append( "  and s.store_name like '%" + store_name + "%'");
		}
		if(!StringUtil.isEmpty(searchType)&&!searchType.equals("default")){
			sql.append(" order by "+searchType+" desc");
		}else{
			sql.append(" order by store_id"+" desc");
		}
		//System.out.println(sql.toString());
		return this.daoSupport.queryForPage(sql.toString(),pageNo ,pageSize);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#disStore(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void disStore(Integer storeId) {
		//关闭店铺
		this.daoSupport.execute("update es_store set  end_time=? where store_id=?",DateUtil.getDateline(), storeId);
		this.editStoredis(2, storeId);
		//修改会员店铺状态
		this.daoSupport.execute("update es_member set is_store=? where member_id=?",3,this.getStore(storeId).getMember_id());
		//更高店铺商品状态
		this.daoSupport.execute("update es_goods set disabled=? where store_id=?", 1,storeId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#useStore(java.lang.Integer)
	 */
	@Override
	public void useStore(Integer storeId) {
		this.editStoredis(1, storeId);
		this.daoSupport.execute("update es_member set is_store="+1+" where member_id="+this.getStore(storeId).getMember_id());
		//更高店铺商品状态
		this.daoSupport.execute("update es_goods set disabled=? where store_id=?", 0,storeId);
	}
	/**
	 * 更改店铺状态
	 * @param disabled
	 * @param store_id
	 */
	private void editStoredis(Integer disabled,Integer store_id){
		this.daoSupport.execute("update es_store set disabled=? where store_id=?",disabled,store_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#getStore(java.lang.Integer)
	 */
	@Override
	public Store getStore(Integer storeId) {
		String sql="select * from es_store where store_id="+storeId;
		List<Store> list = this.baseDaoSupport.queryForList(sql,Store.class);
		Store store = (Store) list.get(0);
		if(store.getId_img()!=null&&!StringUtil.isEmpty(store.getId_img())){			
			store.setId_img( UploadUtil.replacePath(store.getId_img()));
		}
		if(store.getLicense_img()!=null&&!StringUtil.isEmpty(store.getLicense_img())){			
			store.setLicense_img( UploadUtil.replacePath(store.getLicense_img()));
		}
		return store;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#editStore(com.enation.app.b2b2c.core.model.store.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editStore(Store store){
		StoreMember member=storeMemberManager.getStoreMember();
		this.daoSupport.update("es_store", store, "store_id="+member.getStore_id());
		if(store.getDisabled()==1){
			this.daoSupport.execute("update  es_member set is_store=1 where member_id=?",member.getMember_id() );
		}else{
			this.daoSupport.execute("update  es_member set is_store=2 where member_id=?",member.getMember_id() );
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#editStoreInfo(com.enation.app.b2b2c.core.model.store.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editStoreInfo(Store store){
		 this.daoSupport.update("es_store", store, " store_id="+store.getStore_id());
	}
	 public void editStore(Map store) {
		 this.daoSupport.update("es_store", store, " store_id="+store.get("store_id"));
	 }
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#checkStore()
	 */
	@Override
	public boolean checkStore() {
		Member member=storeMemberManager.getStoreMember();
		String sql="select count(store_id) from es_store where member_id=?";
		int isHas= this.daoSupport.queryForInt(sql, member.getMember_id());
		if(isHas>0)
			return true;
		else
			return false;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#save(com.enation.app.b2b2c.core.model.Store)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void save(Store store) {
		store.setMember_id(this.storeMemberManager.getMember(store.getMember_name()).getMember_id());
		store.setCreate_time(DateUtil.getDateline());
		this.daoSupport.insert("es_store", store);
		store.setStore_id(this.daoSupport.getLastId("es_store"));
		
		storePluginBundle.onAfterPass(store);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#checkIdNumber(java.lang.String)
	 */
	@Override
	public Integer checkIdNumber(String idNumber) {
		String sql = "select member_id from store where id_number=?";
		List result = this.baseDaoSupport.queryForList(sql, idNumber);
		return  result.size() > 0 ? 1 : 0;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#editStoreOnekey(java.lang.String, java.lang.String)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void editStoreOnekey(String key, String value) {
		StoreMember member=storeMemberManager.getStoreMember();
		Map map=new HashMap();
		map.put(key,value);
		this.daoSupport.update("es_store", map, "store_id="+member.getStore_id());
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#collect(java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void addcollectNum(Integer storeid) {
		String sql = "update es_store set store_collect = store_collect+1 where store_id=?";
		this.baseDaoSupport.execute(sql, storeid);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#getStore(java.lang.String)
	 */
	@Override
	public boolean checkStoreName(String storeName) {
		String sql="select  count(store_id) from es_store where store_name=? and disabled=1";
		Integer count= this.daoSupport.queryForInt(sql, storeName);
		return count!=0?true:false;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#reduceCollectNum(java.lang.Integer)
	 */
	@Override
	public void reduceCollectNum(Integer storeid) {
		String sql = "update es_store set store_collect = store_collect-1 where store_id=?";
		this.baseDaoSupport.execute(sql, storeid);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#saveStoreLicense(java.lang.Integer, java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void saveStoreLicense(Integer storeid, String id_img,
			String license_img, Integer store_auth, Integer name_auth) {
		if(store_auth==2){
			this.daoSupport.execute("update es_store set store_auth=?,license_img=? where store_id=?",store_auth,license_img,storeid);
		}
		if(name_auth==2){
			this.daoSupport.execute("update es_store set name_auth=?,id_img=? where store_id=?",name_auth,id_img,storeid);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#auth_list(java.util.Map, java.lang.String, int, int)
	 */
	@Override
	public Page auth_list(Map other, Integer disabled, int pageNo, int pageSize) {
		StringBuffer sql=new StringBuffer("select s.* from es_store s   where  disabled="+disabled);
		sql.append(" and (store_auth=2 or name_auth=2)");
		return this.daoSupport.queryForPage(sql.toString(), pageNo, pageSize);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#auth_pass(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public void auth_pass(Integer store_id, Integer name_auth,
			Integer store_auth) {
		if(store_auth!=null){
				this.daoSupport.execute("update es_store set store_auth=? where store_id=?",store_auth,store_id);
		}
		if(name_auth!=null){
			this.daoSupport.execute("update es_store set name_auth=? where store_id=?",name_auth,store_id);
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#getStoreByMember(java.lang.Integer)
	 */
	@Override
	public Store getStoreByMember(Integer memberId) {
		return (Store)this.daoSupport.queryForObject("select * from es_store where member_id=?", Store.class, memberId);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.store.IStoreManager#reApply(com.enation.app.b2b2c.core.model.store.Store)
	 */
	@Override
	public void reApply(Store store) {
		//获取当前用户信息
		Member member=storeMemberManager.getStoreMember();
		if (member != null) {
			store.setMember_id(member.getMember_id());
			store.setMember_name(member.getUname());
		}
		this.daoSupport.update("es_store", store, "store_id="+store.getStore_id());
		storePluginBundle.onAfterApply(store);
	}
	public StorePluginBundle getStorePluginBundle() {
		return storePluginBundle;
	}
	public void setStorePluginBundle(StorePluginBundle storePluginBundle) {
		this.storePluginBundle = storePluginBundle;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public IStoreSildeManager getStoreSildeManager() {
		return storeSildeManager;
	}

	public void setStoreSildeManager(IStoreSildeManager storeSildeManager) {
		this.storeSildeManager = storeSildeManager;
	}
	public IRegionsManager getRegionsManager() {
		return regionsManager;
	}
	public void setRegionsManager(IRegionsManager regionsManager) {
		this.regionsManager = regionsManager;
	}
	
}
