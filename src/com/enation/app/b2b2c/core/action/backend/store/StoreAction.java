package com.enation.app.b2b2c.core.action.backend.store;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreLevelManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.eop.sdk.context.EopSetting;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.StringUtil;
@Component
@ParentPackage("eop_default")
@Namespace("/b2b2c/admin")
@Results({
	 @Result(name="store_list",type="freemarker", location="/b2b2c/admin/store/store_list.html"),
	 @Result(name="audit_list",type="freemarker", location="/b2b2c/admin/store/audit_list.html"),
	 @Result(name="license_list",type="freemarker", location="/b2b2c/admin/store/license_list.html"),
	 @Result(name="disStore_list",type="freemarker", location="/b2b2c/admin/store/disStore_list.html"),
	 @Result(name="edit",type="freemarker", location="/b2b2c/admin/store/store_edit.html"),
	 @Result(name="add",type="freemarker", location="/b2b2c/admin/store/store_add.html"),
	 @Result(name="opt",type="freemarker", location="/b2b2c/admin/store/opt_member.html"),
	 @Result(name="pass",type="freemarker", location="/b2b2c/admin/store/pass.html"),
	 @Result(name="auth_list",type="freemarker", location="/b2b2c/admin/store/auth_list.html")
})
@Action("store")
/**
 * 店铺管理
 * @author LiFenLong
 *
 */
public class StoreAction extends WWAction{
	private IStoreLevelManager storeLevelManager;
	private IStoreManager storeManager;
	private IStoreMemberManager storeMemberManager;
	private Map other;
	private Integer disabled; 
	private Integer storeId;
	private Store store;
	private List level_list;
	
	private Integer member_id;
	private Integer pass;
	private Integer name_auth;
	private Integer store_auth;
	private String storeName;
	
	private String uname;
	private String password;
	private Integer assign_password;
	
	private Double commission;
	/**
	 * 店铺列表
	 * @return
	 */
	public String store_list(){
		return "store_list";
	}
	/**
	 * 开店申请
	 * @return
	 */
	public String audit_list(){
		return "audit_list";
	}
	/**
	 * 店铺认证审核列表
	 * @return
	 */
	public String license_list(){
		return "license_list";
	}
	/**
	 * 禁用店铺列表
	 * @return
	 */
	public String disStore_list(){
		return "disStore_list";
	}
	/**
	 * 审核店铺
	 * @return
	 */
	public String pass(){
		store= this.storeManager.getStore(storeId);
		if(store.getName_auth()==2){
			store.setId_img(UploadUtil.replacePath(store.getId_img()));
		}
		if(store.getStore_auth()==2){
			store.setLicense_img( UploadUtil.replacePath(store.getLicense_img()));
		}
		return "pass";
	}
	public String store_listJson(){
		other=new HashMap();
		other.put("disabled", disabled);
		other.put("name", storeName);
		this.showGridJson(storeManager.store_list(other,disabled,this.getPage(),this.getPageSize()));
		return this.JSON_MESSAGE;
	}
	/**
	 * 审核通过
	 * @return
	 */
	public String audit_pass(){
		try {
			storeManager.audit_pass(member_id, storeId, pass, name_auth, store_auth,commission);
			this.showSuccessJson("操作成功");
		} catch (Exception e) {
			this.showErrorJson("审核失败");
			this.logger.error("操作失败:"+e);
		}
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 禁用店铺
	 * @return
	 */
	public String disStore(){
		if(EopSetting.IS_DEMO_SITE){
			this.showErrorJson(EopSetting.DEMO_SITE_TIP);
			return this.JSON_MESSAGE;
		}
		
		try {
			storeManager.disStore(storeId);
			this.showSuccessJson("店铺禁用成功");
		} catch (Exception e) {
			this.showErrorJson("店铺禁用失败");
			this.logger.error("店铺禁用失败:"+e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 店铺恢复使用
	 * @return
	 */
	public String useStore(){
		try {
			storeManager.useStore(storeId);
			this.showSuccessJson("店铺恢复使用成功");
		} catch (Exception e) {
			this.showErrorJson("店铺恢复使用失败");
			this.logger.error("店铺恢复使用失败"+e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 添加店铺
	 * @return
	 */
	public String save(){
		try {
			store=new Store();
			store = this.assign();
			this.storeManager.save(store);
			this.showSuccessJson("保存成功！");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("保存失败");
		}
		return JSON_MESSAGE;
	}
	
	//修改店铺
	public String edit(){
		store = this.storeManager.getStore(storeId);
		
		level_list=storeLevelManager.storeLevelList();
		
		return "edit";
	}
	/**
	 * 修改店铺信息
	 * @return
	 */
	public String saveEdit(){
		try {
			store = this.storeManager.getStore(storeId);
			Integer disable= store.getDisabled();
			store = this.assign();
			//判断店铺状态 更改店铺状态
			if(disable!=store.getDisabled()){
				if(store.getDisabled()==1){
					storeManager.useStore(storeId);
				}else{
					storeManager.disStore(storeId);
				}
			}
			this.storeManager.editStoreInfo(store);
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			this.showErrorJson("修改失败，请稍后重试！");
		}
		return JSON_MESSAGE;
	}
	/**
	 * 获取店铺信息
	 * @return
	 */
	private Store assign(){
		HttpServletRequest request = this.getRequest();

		store.setMember_name(request.getParameter("member_name"));
		store.setId_number(request.getParameter("id_number"));
		store.setStore_name(request.getParameter("store_name"));
		
		//店铺地址信息
		store.setStore_provinceid(Integer.parseInt(request.getParameter("store_province_id").toString()));	//店铺省ID
		store.setStore_cityid(Integer.parseInt(request.getParameter("store_city_id").toString()));			//店铺市ID
		store.setStore_regionid(Integer.parseInt(request.getParameter("store_region_id").toString()));		//店铺区ID
		
		store.setStore_province(request.getParameter("store_province"));	//店铺省
		store.setStore_city(request.getParameter("store_city"));			//店铺市
		store.setStore_region(request.getParameter("store_region"));		//店铺区
		store.setAttr(request.getParameter("attr"));						//店铺详细地址
		//店铺银行信息
		store.setBank_account_name(request.getParameter("bank_account_name")); 		//银行开户名   
		store.setBank_account_number(request.getParameter("bank_account_number")); 	//公司银行账号
		store.setBank_name(request.getParameter("bank_name")); 						//开户银行支行名称
		store.setBank_code(request.getParameter("bank_code")); 						//支行联行号
		
		store.setBank_provinceid(Integer.parseInt(request.getParameter("bank_province_id").toString())); //开户银行所在省Id
		store.setBank_cityid(Integer.parseInt(request.getParameter("bank_city_id").toString()));		  //开户银行所在市Id
		store.setBank_regionid(Integer.parseInt(request.getParameter("bank_region_id").toString()));    //开户银行所在区Id
		
		store.setBank_province(request.getParameter("bank_province"));	//开户银行所在省
		store.setBank_city(request.getParameter("bank_city"));			//开户银行所在市
		store.setBank_region(request.getParameter("bank_region"));		//开户银行所在区
 		
		store.setAttr(request.getParameter("attr"));
		store.setZip(request.getParameter("zip"));
		store.setTel(request.getParameter("tel"));
		
		store.setCommission(commission);
		store.setStore_level(1);
		store.setDisabled(Integer.valueOf(request.getParameter("disabled")));
		return store;
	}
	
	/**
	 * 新增店铺验证用户
	 * @return
	 */
	public String opt(){
		return "opt";
	}
	/**
	 * 验证用户 
	 * @param uname 会员名称
	 * @param password 密码
	 * @param assign_password 是否验证密码 
	 * @return
	 */
	public String optMember(){
		try {
			StoreMember storeMember= storeMemberManager.getMember(uname);
			//检测是否为新添加的会员
			if(storeMember.getIs_store()==null){
				this.showSuccessJson(uname);
				return this.JSON_MESSAGE;
			}
			//判断用户是否已经拥有店铺
			if(storeMember.getIs_store()==1){
				this.showErrorJson("会员已拥有店铺");
				return this.JSON_MESSAGE;
			}
			//验证会员密码
			if(assign_password!=null&&assign_password==1){
				if(!storeMember.getPassword().equals(StringUtil.md5(password))){
					this.showErrorJson("密码不正确");
					return this.JSON_MESSAGE;
				}
			}
			if(storeMember.getIs_store()==-1){
				this.showSuccessJson(uname);
			}else{
				this.showSuccessJson("2");
			}
		} catch (Exception e) {
			this.showErrorJson("没有此用户");
		}
		return this.JSON_MESSAGE;
		
	}
	public String add(){
		try {
			uname = java.net.URLDecoder.decode(uname,"UTF-8");
		} catch (UnsupportedEncodingException e) {
		} 
		level_list=storeLevelManager.storeLevelList();
		return "add";
	}
	/**
	 * 跳转到申请信息页面
	 * @return
	 */
	public String auth_list(){
		return "auth_list";
	}
	public String auth_listJson(){
		this.showGridJson(storeManager.auth_list(other, disabled, this.getPage(), this.getPageSize()));
		return this.JSON_MESSAGE;
	}
	/**
	 * 审核店铺认证
	 * @param storeId 店铺Id
	 * @param name_auth 店主认证
	 * @param store_auth 店铺认证
	 */
	public String auth_pass(){
		try{
			storeManager.auth_pass(storeId, name_auth, store_auth);
			this.showSuccessJson("操作成功");
		}catch(Exception e){
			this.showErrorJson("操作失败");
			this.logger.error("审核店铺认证失败:"+e);
		}
		return this.JSON_MESSAGE;
	}
	public IStoreLevelManager getStoreLevelManager() {
		return storeLevelManager;
	}
	public void setStoreLevelManager(IStoreLevelManager storeLevelManager) {
		this.storeLevelManager = storeLevelManager;
	}
	public IStoreManager getStoreManager() {
		return storeManager;
	}
	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
	public Map getOther() {
		return other;
	}
	public void setOther(Map other) {
		this.other = other;
	}
	public Integer getDisabled() {
		return disabled;
	}
	public void setDisabled(Integer disabled) {
		this.disabled = disabled;
	}
	public Integer getStoreId() {
		return storeId;
	}
	public void setStoreId(Integer storeId) {
		this.storeId = storeId;
	}
	public Store getStore() {
		return store;
	}
	public void setStore(Store store) {
		this.store = store;
	}
	public List getLevel_list() {
		return level_list;
	}
	public void setLevel_list(List level_list) {
		this.level_list = level_list;
	}
	public Integer getMember_id() {
		return member_id;
	}
	public void setMember_id(Integer member_id) {
		this.member_id = member_id;
	}
	public Integer getPass() {
		return pass;
	}
	public void setPass(Integer pass) {
		this.pass = pass;
	}
	public Integer getName_auth() {
		return name_auth;
	}
	public void setName_auth(Integer name_auth) {
		this.name_auth = name_auth;
	}
	public Integer getStore_auth() {
		return store_auth;
	}
	public void setStore_auth(Integer store_auth) {
		this.store_auth = store_auth;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public String getUname() {
		return uname;
	}
	public void setUname(String uname) {
		this.uname = uname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Integer getAssign_password() {
		return assign_password;
	}
	public void setAssign_password(Integer assign_password) {
		this.assign_password = assign_password;
	}
	public Double getCommission() {
		return commission;
	}
	public void setCommission(Double commission) {
		this.commission = commission;
	}
}
