package com.enation.app.api.action;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

import com.enation.app.api.dao.PhoneTokenDao;
import com.enation.app.api.service.InitUserService;
import com.enation.app.api.util.ImageMagicTools;
import com.enation.app.base.core.model.Member;
import com.enation.eop.sdk.utils.DateUtil;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.database.Page;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONObject;

public class BaseAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

	private static final long serialVersionUID = -5373486170251380691L;

	protected HttpServletRequest request;
	protected HttpServletResponse response;
	protected PrintWriter out;
	protected ServletContext application;
	protected HttpSession session;
	protected JSONObject paramObject;
	protected JSONObject jsonObject;
	protected Page page = new Page();
	protected String version = "1.0";
	protected String clientType = "iphone";
	protected String requestStr;
	protected String methodStr;
	
	protected final Logger logger = Logger.getLogger(getClass());

	public BaseAction() {
		try {
			out = ServletActionContext.getResponse().getWriter();
			application = ServletActionContext.getServletContext();
			request = ServletActionContext.getRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(session==null){
			session = ServletActionContext.getRequest().getSession();
		}
		
		jsonObject = new JSONObject();
		jsonObject.put("result", SUCCESS);
	}

	/**
	 * 上传图片
	 * @param file
	 * @param fileName
	 * @param subFolder
	 * @return
	 */
	public String uploadImage(File file,String fileName,String subFolder){
		String ext = FileUtil.getFileExt(fileName);
		String saveName = "attachment/"+subFolder+"/"+DateUtil.toString(new Date(), "yyyyMMddHHmmss") + StringUtil.getRandStr(4) + "." + ext;
		String filePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+saveName;
		FileUtil.createFile(file, filePath);
		return saveName;
	}
	
	public static void main(String[] args) {
		try {
			ImageMagicTools.cutImage(500,"F:/logs/201610032319367601.png","F:/logs/a-500.png");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String resizeImage(String saveName,int width) throws Exception{
		String basePath = request.getSession().getServletContext().getRealPath("/statics")+"/";
		String resizeImage = saveName.substring(0,saveName.lastIndexOf("."))+"-resize"+width+".jpg";
		String newPath = basePath+resizeImage;
		String oldPath = basePath+saveName; 
		//UploadUtil.createThumb(oldPath,newPath,width);//原来的压缩图片
		ImageMagicTools.cutImage(width,oldPath,newPath);//im4java新的压缩图片
		return resizeImage;
	}
	
	/**
	 * 获取绝对路径
	 * @param imagepath
	 * @return
	 */
	public String getImageUrl(String imagepath){
		String path = request.getContextPath();
		String bpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
		String filePath = bpath+"/statics/"+imagepath;
		String trueUrl = ServletActionContext.getServletContext().getRealPath("/statics/"+imagepath);
		if(!new java.io.File(trueUrl).exists()){
			filePath = bpath+"/statics/allDefault.png";
		}
		return filePath;
	}
	
	public String getAge(Long birthDay){
		Calendar cal = Calendar.getInstance();
        if (cal.before(birthDay)) {
            throw new IllegalArgumentException(
                "出生时间大于当前时间!");
        }
        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;//注意此处，如果不加1的话计算结果是错误的
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
        cal.setTimeInMillis(birthDay);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                } else {
                }
            } else {
                age--;
            }
        } else {
        }
        return age+"";
	}
	
	
	@Override
	public void validate() {
		super.validate();
		paramObject = (JSONObject) request.getAttribute("params");
		if(paramObject!=null){
			requestStr = paramObject.toString();
			methodStr = request.getRequestURI();
		}
		if(paramObject!=null&&paramObject.containsKey("version")&&paramObject.containsKey("appType")){
			version = paramObject.getString("version");
			clientType = paramObject.getString("appType");
		}
	}



	@Override
	public void setServletResponse(HttpServletResponse res) {
		this.response = res;
	}

	@Override
	public void setServletRequest(HttpServletRequest req) {
		this.request = req;
	}

	protected PrintWriter getPrintWriter() throws IOException {
		return response.getWriter();
	}

	protected HttpSession getSession() {
		return request.getSession();
	}

	
	public void baseLoginMsg(Map<String,Object> map,JSONObject obj){
		if(map == null||map.get("token")==null||"".equals(map.get("token"))){
			obj.put("result", "FAILED");
			obj.put("reason", "账号或密码错误！");
		}else{
			obj.put("username", map.get("username"));
			obj.put("userFace", this.getImageUrl((String)map.get("face")));
			obj.put("token", map.get("token"));
		}
	}
	
	@Resource
	private InitUserService initUserService;
	
	public int getMemberId(String accessToken) throws Exception{
		if(accessToken==null||"".equals(accessToken)){
			return 0;
		}
		//logger.error("根据accessToken查找member_id："+accessToken);
//		int memberId = initUserService.getMemberIdByAccessToken(accessToken);
//		if(memberId==0){
//			jsonObject.put("result", "login");
//			jsonObject.put("reason", "accessToken失效或者不存在!");
//			throw new Exception("accessToken失效或者不存在");
//		}
		return initUserService.getMemberIdByAccessToken(accessToken);
	}
	
	public Member getMemberDetails(String accessToken) throws Exception{
		if(accessToken==null||"".equals(accessToken)){
			return null;
		}
		return initUserService.getMemberDetailsByAccessToken(accessToken);
	}
}
