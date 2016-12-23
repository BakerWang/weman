package com.enation.app.api.interceptor;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.StrutsStatics;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

import net.sf.json.JSONObject;


public class GlobalInterceptor implements Interceptor{


	private static final long serialVersionUID = 607670371030702213L;

	@Override
	public void destroy() {
		
	}

	@Override
	public void init() {
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
		HttpServletRequest request= (HttpServletRequest) invocation.getInvocationContext().get(StrutsStatics.HTTP_REQUEST);
	    HttpServletResponse response= (HttpServletResponse) invocation.getInvocationContext().get(StrutsStatics.HTTP_RESPONSE);
	    //Logger.getLogger(getClass()).error("访问的方法："+request.getRequestURI());
	    JSONObject jsonObject = new JSONObject();
	    if(request.getContentType().contains("form-data")||request.getContentType().indexOf("x-www-form-urlencoded")>=0){
	    	for(Object key:request.getParameterMap().keySet()){
				jsonObject.put(key.toString(),request.getParameter(key.toString()));
			}
	    	response.setCharacterEncoding("UTF-8");  
			response.setContentType("application/json; charset=utf-8");
	    	request.setAttribute("params", jsonObject);
	    	Logger.getLogger(getClass()).error("form-data参数："+jsonObject.toString());
	    	try {
//	    		String accessToken = null;
//	    		JSONObject obj = new JSONObject();
//	    		if(jsonObject.has("accessToken")&&jsonObject.get("accessToken")!=null){
//					accessToken = (String)jsonObject.get("accessToken");
//				}
//	    		ApplicationContext ac = (ApplicationContext)request.getSession().getServletContext().getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
//				InitUserService initUserService =(InitUserService)ac.getBean("initUserServiceImpl");
//				int memberId = initUserService.getMemberIdByAccessToken(accessToken);
//	    		if(memberId!=0){
//	    			jsonObject.put("userId", memberId);
	    			invocation.invoke();
//	    		}else{
//	    			obj.put("result", "login");
//	    			response.getWriter().write(obj.toString());
//	    		}
	    	} catch (Exception e) {
	    		e.printStackTrace();
	    	}
	    }
		if(request.getContentType().contains("application/json")){//application/x-www-form-urlencoded||application/json
			String requestStr = IOUtils.toString(request.getInputStream(),"UTF-8");
			jsonObject = JSONObject.fromObject(requestStr);
			request.setAttribute("params", jsonObject);
			response.setCharacterEncoding("UTF-8");  
			response.setContentType("application/json; charset=utf-8");
			String loginType = null;
			String accessToken = null; 
			Logger.getLogger(getClass()).error("application-json参数："+jsonObject.toString());
			if(jsonObject.has("loginType")){
				loginType = (String) jsonObject.get("loginType");
			}
			if(jsonObject.has("accessToken")&&jsonObject.get("accessToken")!=null){
				accessToken = (String)jsonObject.get("accessToken");
			}
			if(loginType!=null&&"global".equals(loginType)){
				invocation.invoke();
			}else{
				JSONObject obj = new JSONObject();
				if(accessToken==null||"".equals(accessToken)||"null".equalsIgnoreCase(accessToken)){
					obj.put("result", "failed");
					obj.put("reason", "accessToken为空!");
					response.getWriter().write(obj.toString());
				}else{
					try {
						//ApplicationContext ac = (ApplicationContext)request.getSession().getServletContext().getAttribute("org.springframework.web.context.WebApplicationContext.ROOT");
						//InitUserService initUserService =(InitUserService)ac.getBean("initUserServiceImpl");
						//int memberId = initUserService.getMemberIdByAccessToken(accessToken);
						//if(memberId!=0){
							//jsonObject.put("userId", memberId);
						invocation.invoke();
						//}else{
						//	obj.put("result", "login");
						//	response.getWriter().write(obj.toString());
						//}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
}
