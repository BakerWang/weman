package com.enation.app.api.action.admin.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.model.PhoneBanner;
import com.enation.app.api.model.Theme;
import com.enation.app.api.service.BannerService;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.database.Page;
import com.enation.framework.util.FileUtil;
@Scope("prototype")
public class AdminBannerAction extends BaseAction{

	private static final long serialVersionUID = 1L;

	@Resource
	private BannerService bannerService;
	private PhoneBanner phoneBanner;
	private AdminSearchForm adminSearchForm;
	
	
	public String newBanner(){
		try {
			//System.out.println("...............1");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "newBannerSuccess";
	}
	
	
	public String getBannerDetails(){
		try {
			String bid = request.getParameter("bid");
			PhoneBanner phoneBanner = bannerService.getBannerDetails(bid);
			request.setAttribute("phoneBanner", phoneBanner);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "getBannerSucccess";
	}
	
	public void updateBanner(){
		try {
			if(bannerFile!=null&&bannerFile.size()>1){
				File image = bannerFile.get(0);
				String imageName = bannerFileFileName.get(0);
				if (FileUtil.isAllowUp(imageName)) {
					String saveName = uploadImage(image, imageName, "banner");
					phoneBanner.setImage(saveName);
					String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+phoneBanner.getImage();
					FileUtil.delete(imagePath);
				} else {
					jsonObject.put("result", "no");
					jsonObject.put("reason", "上传格式不对！");
				}
			}
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			phoneBanner.setStart_time(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime).getTime());
			phoneBanner.setEnd_time(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime).getTime());
			bannerService.updateBanner(phoneBanner);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}
	
	public void delBanner(){
		try {
			String bid = request.getParameter("bid");
			bannerService.delBanner(Long.parseLong(bid));
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}
	
	private List<File> bannerFile;  
    private List<String> bannerFileFileName;
	public void saveBanner(){
		try {
			String startTime = request.getParameter("startTime");
			String endTime = request.getParameter("endTime");
			phoneBanner.setStart_time(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(startTime).getTime());
			phoneBanner.setEnd_time(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(endTime).getTime());
			if(phoneBanner.getStart_time()>=phoneBanner.getEnd_time()){
				jsonObject.put("result", "no");
				return;
			}
			if(bannerFile!=null&&bannerFile.size()>0){
				File image = bannerFile.get(0);
				String imageName = bannerFileFileName.get(0);
				if (FileUtil.isAllowUp(imageName)) {
					String saveName = uploadImage(image, imageName, "banner");
					phoneBanner.setImage(saveName);
				} else {
					jsonObject.put("result", "no");
					jsonObject.put("reason", "上传格式不对！");
				}
			}
			phoneBanner.setCreate_time(new Date().getTime());
			phoneBanner.setStatus("1");
			bannerService.saveBanner(phoneBanner);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}
	
	public String bannerList(){
		try {
			page.setPageSize(20);
			String pageNo = request.getParameter("pageNo");
			if (pageNo == null || "".equals(pageNo)) {
				pageNo = "1";
			}
			page.setCurrentPageNo(Long.parseLong(pageNo));
			Page pages = bannerService.getBanners(adminSearchForm, page);
			pages.setCurrentPageNo(Long.parseLong(pageNo));
			request.setAttribute("page", pages);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "bannerListSuccess";
	}

	public PhoneBanner getPhoneBanner() {
		return phoneBanner;
	}

	public void setPhoneBanner(PhoneBanner phoneBanner) {
		this.phoneBanner = phoneBanner;
	}

	public AdminSearchForm getAdminSearchForm() {
		return adminSearchForm;
	}

	public void setAdminSearchForm(AdminSearchForm adminSearchForm) {
		this.adminSearchForm = adminSearchForm;
	}


	public List<File> getBannerFile() {
		return bannerFile;
	}


	public void setBannerFile(List<File> bannerFile) {
		this.bannerFile = bannerFile;
	}


	public List<String> getBannerFileFileName() {
		return bannerFileFileName;
	}


	public void setBannerFileFileName(List<String> bannerFileFileName) {
		this.bannerFileFileName = bannerFileFileName;
	}
	
	
}
