package com.enation.app.api.action.live;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.action.live.qiniu.LiveUtils;
import com.enation.app.api.service.LiveService;
import com.enation.framework.database.Page;
import com.enation.framework.util.FileUtil;

import net.sf.json.JSONObject;
@Scope("prototype")
public class AdminLiveAction extends BaseAction {
	private static final long serialVersionUID = -5754071984183662887L;

	@Resource
	private LiveService liveService;
	
	public String getLiveList(){
		try {
			page.setCurrentPageNo(Long.parseLong(request.getParameter("pageNo")));
			Page resPage = liveService.getLivePalyBackList(null,page);
			request.setAttribute("page", resPage);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "getLiveListSuccess";
	}
	public String getLiveDetails(){
		try {
			Map<String,Object> resMap = liveService.getLiveById(Long.parseLong((String)request.getParameter("lid")));
			request.setAttribute("resObj", resMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "getLiveDetailsSuccess";
	}
	private File liveFile;  
    private String liveFileFileName;
    
	public void updateDetails(){
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			if(liveFile!=null){
				File image = liveFile;
				String imageName = liveFileFileName;
				if (FileUtil.isAllowUp(imageName)) {
					String saveName = uploadImage(image, imageName, "live");
					map.put("image", saveName);
				} else {
					jsonObject.put("result", "no");
					jsonObject.put("reason", "上传格式不对！");
				}
			}
			int isSave = Integer.parseInt(request.getParameter("isSave"));
			//String title = request.getParameter("title");
			long startTime = Long.parseLong(request.getParameter("startTime"));
			if(isSave==1){
				String jsonObj = LiveUtils.saveLive(request.getParameter("streamId"),startTime,new Date().getTime()/1000);
				if(jsonObj!=null){
					JSONObject obj = JSONObject.fromObject(jsonObj);
					String url = obj.getString("url");
					long saveDate = obj.getLong("duration");
					String targetUrl = obj.getString("targetUrl");
					String persistentId = obj.getString("persistentId");
					map.put("url", url);
					map.put("saveDate", saveDate);
					map.put("targetUrl", targetUrl);
					map.put("persistentId", persistentId);
				}
			}
			//map.put("title", request.getParameter("title"));
			map.put("isSave", isSave);
			map.put("status", request.getParameter("status"));
			map.put("details", request.getParameter("details"));
			liveService.updateLive(Long.parseLong(request.getParameter("id")),map);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	public File getLiveFile() {
		return liveFile;
	}
	public void setLiveFile(File liveFile) {
		this.liveFile = liveFile;
	}
	public String getLiveFileFileName() {
		return liveFileFileName;
	}
	public void setLiveFileFileName(String liveFileFileName) {
		this.liveFileFileName = liveFileFileName;
	}
	
	
	
	
}
