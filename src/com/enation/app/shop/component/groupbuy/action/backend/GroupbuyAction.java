package com.enation.app.shop.component.groupbuy.action.backend;

import java.io.File;
import java.util.List;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

import com.enation.app.shop.component.groupbuy.model.GroupBuy;
import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.app.shop.component.groupbuy.model.GroupBuyArea;
import com.enation.app.shop.component.groupbuy.model.GroupBuyCat;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyActiveManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyAreaManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyCatManager;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.action.WWAction;

/**
 * 团购Action
 * @author kingapex
 *2015-1-10下午10:09:12
 *@author fenlongli
 */
@ParentPackage("eop_default")
@Namespace("/shop/admin")
@Results({
	 @Result(name="list",type="freemarker", location="/shop/admin/groupbuy/groupbuy/groupbuy_list.html"),
	 @Result(name="add",type="freemarker", location="/shop/admin/groupbuy/groupbuy/groupbuy_add.html")
})

@Action("groupBuy")
public class GroupbuyAction extends WWAction{
	
	 private IGroupBuyManager groupBuyManager;
	 private IGroupBuyActiveManager groupBuyActiveManager;
	 private IGroupBuyCatManager groupBuyCatManager;
	 private IGroupBuyAreaManager groupBuyAreaManager;
	 private List<GroupBuyArea> groupbuy_area_list;
	 private List<GroupBuyCat> groupbuy_cat_list;
	 private GroupBuyActive groupBuyActive;
	 private int actid;
	 private int gbid;
	 private Integer status;
	 
	 private GroupBuy groupBuy;
	 private File image;
	 private String imageFileName;
	/**
	 * 跳转至团购列表
	 * @return
	 */
	public String list(){
		return "list";
	}
	 /**
	  * 按活动id显示团购json
	  * @return
	  */
	 public String listJson(){
		 try {
			 
			this.webpage = this.groupBuyManager.listByActId(this.getPage(), this.getPageSize(), actid, status);
			this.showGridJson(webpage);
			
		} catch (Exception e) {
			this.logger.error("查询出错",e);
			this.showErrorJson("查询出错");
			
		}
		 return this.JSON_MESSAGE;
	 }
	 /**
	  * 审核团购
	  * @param gbid 团购Id
	  * @param status 审核状态
	  * @return
	  */
	 public String auth(){
		 
		 try {
			 this.groupBuyManager.auth(gbid, status);
			 this.showSuccessJson("操作成功");
		} catch (Exception e) {
			this.logger.error("审核操作失败",e);
			this.showErrorJson("审核操作失败"+e.getMessage());
		}
		 return this.JSON_MESSAGE;
	 }
	 /**
	  * 添加团购
	  * @return
	  */
	 public String add(){
		 groupBuyActive= groupBuyActiveManager.get(actid);
		 groupbuy_cat_list= groupBuyCatManager.listAll();
		 groupbuy_area_list= groupBuyAreaManager.listAll();
		 return "add";
	 }
	 /**
	  * 保存添加
	  * @return
	  */
	 public String saveAdd(){
		 try {
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
			groupBuyManager.add(groupBuy);
			this.showSuccessJson("添加成功");
		} catch (Exception e) {
			this.showErrorJson("添加失败");
			this.logger.error("团购添加失败："+e);
		}
		 return this.JSON_MESSAGE;
		 
	 }
	 
	 //get set
	public IGroupBuyManager getGroupBuyManager() {
		return groupBuyManager;
	}
	public void setGroupBuyManager(IGroupBuyManager groupBuyManager) {
		this.groupBuyManager = groupBuyManager;
	}
	public int getActid() {
		return actid;
	}
	public void setActid(int actid) {
		this.actid = actid;
	}
	public int getGbid() {
		return gbid;
	}
	public void setGbid(int gbid) {
		this.gbid = gbid;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
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
	public IGroupBuyCatManager getGroupBuyCatManager() {
		return groupBuyCatManager;
	}
	public void setGroupBuyCatManager(IGroupBuyCatManager groupBuyCatManager) {
		this.groupBuyCatManager = groupBuyCatManager;
	}
	public IGroupBuyAreaManager getGroupBuyAreaManager() {
		return groupBuyAreaManager;
	}
	public void setGroupBuyAreaManager(IGroupBuyAreaManager groupBuyAreaManager) {
		this.groupBuyAreaManager = groupBuyAreaManager;
	}
	public List<GroupBuyArea> getGroupbuy_area_list() {
		return groupbuy_area_list;
	}
	public void setGroupbuy_area_list(List<GroupBuyArea> groupbuy_area_list) {
		this.groupbuy_area_list = groupbuy_area_list;
	}
	public List<GroupBuyCat> getGroupbuy_cat_list() {
		return groupbuy_cat_list;
	}
	public void setGroupbuy_cat_list(List<GroupBuyCat> groupbuy_cat_list) {
		this.groupbuy_cat_list = groupbuy_cat_list;
	}
	public GroupBuy getGroupBuy() {
		return groupBuy;
	}
	public void setGroupBuy(GroupBuy groupBuy) {
		this.groupBuy = groupBuy;
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
}
