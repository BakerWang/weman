package com.enation.app.shop.component.groupbuy.action.backend;

import java.util.HashMap;
import java.util.Map;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.DateUtil;
/**
 * 团购活动Action
 * @author fenlongli
 *
 */
@ParentPackage("eop_default")
@Namespace("/shop/admin")
@Results({
	 @Result(name="list",type="freemarker", location="/shop/admin/groupbuy/groupbuyActive/act_list.html"),
	 @Result(name="b2b2c_list",type="freemarker", location="/shop/admin/groupbuy/groupbuyActive/b2b2c_act_list.html"),
	 @Result(name="add",type="freemarker", location="/shop/admin/groupbuy/groupbuyActive/act_add.html"),
	 @Result(name="edit",type="freemarker", location="/shop/admin/groupbuy/groupbuyActive/act_edit.html")
})

@Action("groupBuyAct")
public class GroupBuyActiveAction extends WWAction{
	private IGroupBuyActiveManager groupBuyActiveManager;
	private GroupBuyActive groupBuyActive;
	private Long groupbuyActStartTime;
	private Integer act_id[];
	private String act_name;
	private String start_time;
	private String end_time;
	private String join_end_time;
	
	/**
	 * 团购活动列表
	 * @return
	 */
	public String list(){
		return "list";
	}
	/**
	 * b2b2c团购活动列表
	 * @return
	 */
	public String b2b2cList(){
		return "b2b2c_list";
	}
	/**
	 * 团购活动列表Json
	 * @return
	 */
	public String listJson(){
		Map map=new HashMap();
		this.webpage=groupBuyActiveManager.groupBuyActive(this.getPage(), this.getPageSize(), map);
		this.showGridJson(this.webpage);
		return this.JSON_MESSAGE;
	}
	/**
	 * 跳转到活动添加页
	 * @return
	 */
	public String add(){
		groupbuyActStartTime=groupBuyActiveManager.getLastEndTime();
		return "add";
	}
	/**
	 * 添加活动
	 * @param groupBuyActive 团购活动
	 * @param act_name 活动名称
	 * @param start_time 开始时间
	 * @param end_time 结束时间
	 * @param join_end_time 活动报名截止时间
	 * @return
	 */
	public String saveAdd(){
		try {
			GroupBuyActive groupBuyActive = new GroupBuyActive();
			groupBuyActive.setAct_name(act_name);
			groupBuyActive.setStart_time( DateUtil.getDateline(start_time)  );
			groupBuyActive.setEnd_time( DateUtil.getDateline(end_time)  );
			groupBuyActive.setJoin_end_time(DateUtil.getDateline(join_end_time));
			groupBuyActiveManager.add(groupBuyActive);
			this.showSuccessJson("添加活动成功");
		} catch (Exception e) {
			this.showErrorJson("添加活动失败"+e.getMessage());
			this.logger.error("添加活动失败", e);
		}
		
		return this.JSON_MESSAGE;
	}
	/**
	 * 删除某个团购
	 * @param act_id 团购活动Id数组
	 * @return
	 */
	public String delete(){
		try {
			this.groupBuyActiveManager.delete(act_id);        //如果删除团购不是要与删除多个团购配合使用，那么只需要传递一个ID就行。 如果有问题，请高手优先考虑这里。whj-2015-05-25
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson("删除失败");
		}
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 批量删除
	 * @param act_id 团购活动Id数组
	 * @return
	 */
	public String batchDelete(){
		try {
			this.groupBuyActiveManager.delete(this.act_id);
			this.showSuccessJson("删除成功");
		} catch (Exception e) {
			this.showErrorJson("删除失败");
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 跳转至团购活动修改页
	 * @param act_id 团购活动Id数组
	 * @return
	 */
	public String edit(){
		groupBuyActive=this.groupBuyActiveManager.get(act_id[0]);
		start_time=groupBuyActive.getStart_time_str();
		end_time=groupBuyActive.getEnd_time_str();
		return "edit";
	}
	/**
	 * 保存修改团购活动
	 * @param cat_id 团购活动Id数组
	 * @param act_name 团购活动名称
	 * @param start_time 团购活动开始时间
	 * @param end_time 团购活动结束时间
	 * @param join_end_time 报名截止时间 
	 * @return
	 */
	public String saveEdit(){
		try {
			GroupBuyActive groupBuyActive = new GroupBuyActive();
			groupBuyActive.setAct_id(act_id[0]);
			groupBuyActive.setAct_name(act_name);
			groupBuyActive.setStart_time( DateUtil.getDateline(start_time)  );
			groupBuyActive.setEnd_time( DateUtil.getDateline(end_time)  );
			groupBuyActiveManager.update(groupBuyActive);
			this.showSuccessJson("修改成功");
		} catch (Exception e) {
			this.showErrorJson("修改失败");
			this.logger.error("添加活动失败", e);
		}
		return this.JSON_MESSAGE;
	}
	public IGroupBuyActiveManager getGroupBuyActiveManager() {
		return groupBuyActiveManager;
	}
	public void setGroupBuyActiveManager(
			IGroupBuyActiveManager groupBuyActiveManager) {
		this.groupBuyActiveManager = groupBuyActiveManager;
	}
	public GroupBuyActive getGroupBuyActive() {
		return groupBuyActive;
	}
	public void setGroupBuyActive(GroupBuyActive groupBuyActive) {
		this.groupBuyActive = groupBuyActive;
	}
	public Long getGroupbuyActStartTime() {
		return groupbuyActStartTime;
	}
	public void setGroupbuyActStartTime(Long groupbuyActStartTime) {
		this.groupbuyActStartTime = groupbuyActStartTime;
	}
	public Integer[] getAct_id() {
		return act_id;
	}
	public void setAct_id(Integer[] act_id) {
		this.act_id = act_id;
	}
	public String getAct_name() {
		return act_name;
	}
	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}
	public String getStart_time() {
		return start_time;
	}
	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
	public String getEnd_time() {
		return end_time;
	}
	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}
	public String getJoin_end_time() {
		return join_end_time;
	}
	public void setJoin_end_time(String join_end_time) {
		this.join_end_time = join_end_time;
	}
}
