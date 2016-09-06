package com.enation.app.api.action.admin.action;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Date;

import javax.imageio.ImageIO;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.model.Theme;
import com.enation.app.api.util.Utils;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;

@Scope("prototype")
public class AdminUploadAction extends BaseAction{

	private static final long serialVersionUID = -3888828399224384642L;
	

	private File file;
	private String fileFileName;
	
	public void uploadImage(){
		try {
			if (file != null) {
				if (FileUtil.isAllowUp(fileFileName)) {
					String saveName = uploadImage(file, fileFileName, "temp");
					//String saveName = UploadUtil.upload(file, fileFileName, "temp");
					jsonObject.put("result", "yes");
					jsonObject.put("fileName", saveName);
				}else{
					jsonObject.put("result", "no");
				}
			}else{
				jsonObject.put("result", "no");
			}
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public String getFileFileName() {
		return fileFileName;
	}

	public void setFileFileName(String fileFileName) {
		this.fileFileName = fileFileName;
	}

	
}
