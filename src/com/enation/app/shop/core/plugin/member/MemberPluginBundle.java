package com.enation.app.shop.core.plugin.member;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberAddress;
import com.enation.app.shop.core.model.MemberComment;
import com.enation.eop.processor.core.freemarker.FreeMarkerPaser;
import com.enation.framework.plugin.AutoRegisterPluginsBundle;
import com.enation.framework.plugin.IPlugin;


/**
 * 会员插件桩 
 * @author kingapex
 * @date 2011-10-20 下午3:32:23 
 * @version V1.0
 */
public class MemberPluginBundle extends AutoRegisterPluginsBundle {
	
	/**
	 * 激发退出事件
	 * 1.b2b2c店铺会员退出插件
	 */
	public void onLogout(Member member){
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IMemberLogoutEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass()+ " onLogout 开始...");
						}
						IMemberLogoutEvent event = (IMemberLogoutEvent) plugin;
						event.onLogout(member);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onLogout 结束.");
						}
					} 
				}
			}
		}catch(RuntimeException  e){
			if(this.loger.isErrorEnabled())
			this.loger.error("调用会员插件注销事件错误", e);
			throw e;
		}
	}
	
	
	
	/**
	 * 激发登录事件
	 * 1.会员登录向Session中传入StoreMember对象
	 * 2.增加会员积分(一天只增加一次)
	 */
	public void onLogin(Member member,Long upLogintime){
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IMemberLoginEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onLogin 开始...");
						}
						IMemberLoginEvent event = (IMemberLoginEvent) plugin;
						event.onLogin(member,upLogintime);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onLogin 结束.");
						}
					} 
				}
			}
		}catch(RuntimeException  e){
			if(this.loger.isErrorEnabled())
			this.loger.error("调用会员插件登录事件错误", e);
			throw e;
		}
	}
	
	
	
	
	/**
	 * 激发注册事件
	 * 1.会员注册后标示为未申请店铺
	 * 2.响应会员注册事件，送出积分
	 * 3.发送邮件
	 */
	public void onRegister(Member member){
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IMemberRegisterEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onRegister 开始...");
						}
						IMemberRegisterEvent event = (IMemberRegisterEvent) plugin;
						event.onRegister(member);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onRegister 结束.");
						}
					} 
				}
			}
		}catch(RuntimeException  e){
			if(this.loger.isErrorEnabled())
			this.loger.error("调用会员插件注册事件错误", e);
			throw e;
		}
	}
	
	

	
	/**
	 * 激发邮件校验事件
	 * 1.增加会员积分
	 */
	public void onEmailCheck(Member member){
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IMemberEmailCheckEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onRegister 开始...");
						}
						IMemberEmailCheckEvent event = (IMemberEmailCheckEvent) plugin;
						event.onEmailCheck(member);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onRegister 结束.");
						}
					} 
				}
			}
		}catch(RuntimeException  e){
			if(this.loger.isErrorEnabled())
			this.loger.error("调用会员插件邮件验证事件错误", e);
			throw e;
		}
	}


	
	/**
	 * 激发更新密码事件
	 * 1.无
	 * @param password
	 * @param memberid
	 */
	public void onUpdatePassword(String password,int memberid){
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IMemberUpdatePasswordEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onUpdatePassword 开始...");
						}
						IMemberUpdatePasswordEvent event = (IMemberUpdatePasswordEvent) plugin;
						event.updatePassword(password, memberid);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onUpdatePassword 结束.");
						}
					} 
				}
			}
		}catch(RuntimeException  e){
			if(this.loger.isErrorEnabled())
				this.loger.error("调用会员更新密码事件错误", e);
			throw e;
		}
	}
	
	
	

	
	/**
	 * 获取会员详细页的选项卡
	 * 1.他的咨询 2.他的基本信息 3.他的评论 4.他的预存款 5.编辑会员 6.等级积分 7.消费积分 8.他的订单 9.会员备注
	 * @return
	 */
	public Map<Integer,String> getTabList(Member member){
		List<IPlugin> plugins = this.getPlugins();
		
		Map<Integer,String> tabMap = new TreeMap<Integer, String>();
		if (plugins != null) {
			
		 
			for (IPlugin plugin : plugins) {
				if(plugin instanceof IMemberTabShowEvent ){
					IMemberTabShowEvent event  = (IMemberTabShowEvent)plugin;
					/**
					 * 如果插件返回不执行，则跳过此插件
					 */
					if(!event.canBeExecute(member)){
						continue;
					}
					String name = event.getTabName(member);
					tabMap.put( event.getOrder(), name);
				}
			}
			
			 
		}
		return tabMap;
	}
	
	

	/**
	 * 获取各个插件的html
	 * 1.会员评论列表 2.他的基本信息 3.会员评论列表 4.他的预存款（指定会员的预存款日志列表） 5.编辑会员 6.等级积分 7.消费积分 8.他的订单 9.会员备注
	 */
	public Map<Integer,String>   getDetailHtml(Member member) {
		 Map<Integer,String> htmlMap = new TreeMap<Integer,String>();
		FreeMarkerPaser freeMarkerPaser =FreeMarkerPaser.getInstance();
		freeMarkerPaser.putData("member",member);
		List<IPlugin> plugins = this.getPlugins();
		
		if (plugins != null) {
			for (IPlugin plugin : plugins) {
			
				
				if (plugin instanceof IMemberTabShowEvent) {
					IMemberTabShowEvent event = (IMemberTabShowEvent) plugin;
					freeMarkerPaser.setClz(event.getClass());
						
						/**
						 * 如果插件返回不执行，则跳过此插件
						 */
						if(!event.canBeExecute(member)){
							continue;
						}
						String html =event.onShowMemberDetailHtml(member);
						htmlMap.put(event.getOrder(), html);
					 
				}
			}
		}
		
		
		return htmlMap;
	}
	
	
	/**
	 * 1.无事件
	 * @param address
	 */
	public  void onAddressAdd(MemberAddress address){
		try{
			List<IPlugin> plugins = this.getPlugins();
			
			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IMemberAddressAddEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onAddressAdd 开始...");
						}
						IMemberAddressAddEvent event = (IMemberAddressAddEvent) plugin;
						event.addressAdd(address);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " +plugin.getClass() + " onAddressAdd 结束.");
						}
					} 
				}
			}
		}catch(RuntimeException  e){
			if(this.loger.isErrorEnabled())
				this.loger.error("调用会员添加地址事件错误", e);
			throw e;
		}
	}		
	
	/**
	 * 1.无事件
	 * @param comment
	 */
	public void onComment(MemberComment comment) {
		try {
			List<IPlugin> plugins = this.getPlugins();

			if (plugins != null) {
				for (IPlugin plugin : plugins) {
					if (plugin instanceof IMemberCommentEvent) {
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass() + " onMemberComment 开始...");
						}
						IMemberCommentEvent event = (IMemberCommentEvent) plugin;
						event.onMemberComment(comment);
						if (loger.isDebugEnabled()) {
							loger.debug("调用插件 : " + plugin.getClass() + " onAddressAdd 结束.");
						}
					}
				}
			}
		} catch (RuntimeException e) {
			if (this.loger.isErrorEnabled())
				this.loger.error("调用会员添加地址事件错误", e);
			throw e;
		}
	}
	
	@Override
	public String getName() {
		return "会员插件桩";
	}
	
}
