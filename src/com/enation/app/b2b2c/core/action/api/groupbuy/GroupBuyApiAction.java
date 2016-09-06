package com.enation.app.b2b2c.core.action.api.groupbuy;

import java.io.File;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.groupbuy.StoreGroupBuy;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.shop.component.groupbuy.model.GroupBuy;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;

/**
 * 团购api
 * @author kingapex
 *2015-1-5下午7:07:19
 */
@ParentPackage("eop_default")
@Namespace("/api/b2b2c")
@Action("groupBuy")
@Component
public class GroupBuyApiAction extends WWAction {
	
	private IGroupBuyManager groupBuyManager;
	private IStoreMemberManager storeMemberManager;
	
	private int gb_id;
	private int act_id;
	private int area_id;
	private String gb_name;
	private String gb_title;
	private String goods_name;
	private int goods_id;
	private double price;
	private String img_url;
	private int goods_num;
	private int cat_id;
	private int visual_num;
	private int limit_num;
	private String remark;
	private File image;
	private String imageFileName;
	private double original_price;
	
	
	/**
	 * 添加团购
	 * @return
	 */
	public String add(){
		try {
			StoreGroupBuy groupBuy = this.putData();
			int result = groupBuyManager.add(groupBuy);
			this.showSuccessJson("团购添加成功", result);
		} catch (Exception e) {
			this.showErrorJson("团购添加失败"+e.getMessage());
			this.logger.error("团购添加失败", e);
		}
		
		
		return this.JSON_MESSAGE;
	}
	
	/**
	 * 更新团购信息
	 * 
	 * @return
	 */
	public String update(){
		
		StoreMember storeMember = storeMemberManager.getStoreMember();
		if(storeMember==null ) {
			throw new RuntimeException("尚未登陆，不能使用此API");
		}
		
		try {
			GroupBuy groupBuy = this.putData();
			groupBuy.setGb_id(gb_id);
			groupBuyManager.update(groupBuy);
			this.showSuccessJson("团购更新成功");
		} catch (Exception e) {
			this.showErrorJson("团购更新失败"+e.getMessage());
			this.logger.error("团购更新失败", e);
		}
		
		
		return this.JSON_MESSAGE;
	}
	
	
	/**
	 * 删除团购
	 * @param gb_id 要删除团购id, int 型
	 * @return 操作结果 json字串
	 */
	public String delete(){
		try {
			this.groupBuyManager.delete(gb_id);
			this.showSuccessJson("删除成功");
			
		} catch (Exception e) {
			this.showErrorJson("删除失败"+e.getMessage());
			this.logger.error("删除失败", e);
		}
		
		return this.JSON_MESSAGE;
	}
	
	
	private StoreGroupBuy  putData(){
		StoreMember storeMember = storeMemberManager.getStoreMember();
		if(storeMember==null ) {
			throw new RuntimeException("尚未登陆，不能使用此API");
		}
		
		StoreGroupBuy groupBuy = new StoreGroupBuy();
		if(image!=null){
			
			//判断文件类型
			String allowTYpe = "gif,jpg,bmp,png";
			if (!imageFileName.trim().equals("") && imageFileName.length() > 0) {
				String ex = imageFileName.substring(imageFileName.lastIndexOf(".") + 1, imageFileName.length());
				if(allowTYpe.toString().indexOf(ex.toLowerCase()) < 0){
					throw new RuntimeException("对不起,只能上传gif,jpg,bmp,png格式的图片！");
				}
			}
			
			//判断文件大小
			
			if(image.length() > 2000 * 1024){
				throw new RuntimeException("图片不能大于2MB！");
				 
			}
			
			String imgPath=	UploadUtil.upload(image, imageFileName, "groupbuy");
			groupBuy.setImg_url(imgPath);
		}
		
		groupBuy.setStore_id(storeMember.getStore_id());
		groupBuy.setAct_id(act_id);
		groupBuy.setArea_id(area_id);
		groupBuy.setCat_id(cat_id);
		groupBuy.setGb_name(gb_name);
		groupBuy.setGb_title(gb_title);
		groupBuy.setGoods_id(goods_id);
		groupBuy.setGoods_name(goods_name);
		groupBuy.setGoods_num(goods_num);
		groupBuy.setLimit_num(limit_num);
		groupBuy.setPrice(price);
		groupBuy.setOriginal_price(original_price);
		groupBuy.setRemark(remark);
		groupBuy.setVisual_num(visual_num);
		return groupBuy;
	}

	public IGroupBuyManager getGroupBuyManager() {
		return groupBuyManager;
	}

	public void setGroupBuyManager(IGroupBuyManager groupBuyManager) {
		this.groupBuyManager = groupBuyManager;
	}

	public int getGb_id() {
		return gb_id;
	}

	public void setGb_id(int gb_id) {
		this.gb_id = gb_id;
	}

	public int getAct_id() {
		return act_id;
	}

	public void setAct_id(int act_id) {
		this.act_id = act_id;
	}

	public int getArea_id() {
		return area_id;
	}

	public void setArea_id(int area_id) {
		this.area_id = area_id;
	}

	public String getGb_name() {
		return gb_name;
	}

	public void setGb_name(String gb_name) {
		this.gb_name = gb_name;
	}

	public String getGb_title() {
		return gb_title;
	}

	public void setGb_title(String gb_title) {
		this.gb_title = gb_title;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

	public int getGoods_id() {
		return goods_id;
	}

	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public int getGoods_num() {
		return goods_num;
	}

	public void setGoods_num(int goods_num) {
		this.goods_num = goods_num;
	}

	public int getCat_id() {
		return cat_id;
	}

	public void setCat_id(int cat_id) {
		this.cat_id = cat_id;
	}

	public int getVisual_num() {
		return visual_num;
	}

	public void setVisual_num(int visual_num) {
		this.visual_num = visual_num;
	}

	public int getLimit_num() {
		return limit_num;
	}

	public void setLimit_num(int limit_num) {
		this.limit_num = limit_num;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

 
	public File getImage() {
		return image;
	} 
 
	public void setImage(File image) {
		this.image = image;
	}

	public String getImageFileName() {
		return imageFileName;
	}

	public void setImageFileName(String imageFileName) {
		this.imageFileName = imageFileName;
	}

	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}

	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}

	public double getOriginal_price() {
		return original_price;
	}

	public void setOriginal_price(double original_price) {
		this.original_price = original_price;
	}
}
