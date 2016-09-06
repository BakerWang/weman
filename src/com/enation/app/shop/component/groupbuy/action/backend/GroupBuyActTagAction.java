package com.enation.app.shop.component.groupbuy.action.backend;


import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.core.service.IGoodsTagManager;
import com.enation.framework.action.WWAction;
/**
 * 团购活动标签
 */
@ParentPackage("eop_default")
@Namespace("/shop/admin")
@Results({
	@Result(name="search_list",type="freemarker", location="/shop/admin/groupbuy/tag/search_list.html"),
	@Result(name="list",type="freemarker", location="/shop/admin/groupbuy/tag/tag_goods_list.html")
})

@Action("groupBuyActTag")
public class GroupBuyActTagAction extends WWAction{
	
	private Integer catid;
	private Integer tagid;
	private Integer actid;
	private IGroupBuyManager groupBuyManager;
	private IGoodsTagManager goodsTagManager;
	
	/**
	 * 
	 * @return
	 */
	public String list(){
		return "list";
	}
	
	/**
	 * 商品标签    商品列表json
	 * @param catid 商品分类Id,Integer
	 * @param tagid 标签Id,Integer
	 * @return 商品标签    商品列表json
	 */
	public String listJson(){
		
		if (catid == null || catid.intValue() == 0) {
			this.webpage = goodsTagManager.getGoodsList(tagid, this.getPage(), this.getPageSize());
		} else {
			this.webpage = goodsTagManager.getGoodsList(tagid, catid.intValue(), this.getPage(), this.getPageSize());
		}
		this.showGridJson(webpage);
		return JSON_MESSAGE;
	}
	/**
	 * 跳转至标签商品选择列表
	 * @return
	 */
	public String search() {
		return "search_list";
	}
	
	/**
	 * 团购标签商品选择列表
	 * @author LiFenLong
	 */
	public String goodsListJson() {
		
		this.webpage=this.groupBuyManager.listByActId(this.getPage(), this.getPageSize(), actid, 1);
		this.showGridJson(this.webpage);
		
		return JSON_MESSAGE;
	}

	public Integer getCatid() {
		return catid;
	}

	public void setCatid(Integer catid) {
		this.catid = catid;
	}

	public Integer getTagid() {
		return tagid;
	}

	public void setTagid(Integer tagid) {
		this.tagid = tagid;
	}

	public Integer getActid() {
		return actid;
	}

	public void setActid(Integer actid) {
		this.actid = actid;
	}

	public IGroupBuyManager getGroupBuyManager() {
		return groupBuyManager;
	}

	public void setGroupBuyManager(IGroupBuyManager groupBuyManager) {
		this.groupBuyManager = groupBuyManager;
	}

	public IGoodsTagManager getGoodsTagManager() {
		return goodsTagManager;
	}

	public void setGoodsTagManager(IGoodsTagManager goodsTagManager) {
		this.goodsTagManager = goodsTagManager;
	}
}
