package com.enation.app.api.action.admin.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.model.ThemeTag;
import com.enation.app.api.service.ArticleService;
import com.enation.app.api.service.BannerService;
import com.enation.app.api.service.ProductService;
import com.enation.framework.database.Page;
@Scope("prototype")
public class AdminUserActionAction extends BaseAction {

	private static final long serialVersionUID = 6460850315210635853L;

	@Resource
	private ProductService productService;
	@Resource
	private ArticleService articleService;
	@Resource
	private BannerService bannerService;
	
	public String getUserAction(){
		try {
			String pageNo = request.getParameter("pageNo");
			if (pageNo == null || "".equals(pageNo)) {
				pageNo = "1";
			}
			String keywords = request.getParameter("keywords");
			Map<String, Object> maps = new HashMap<String, Object>();
			if (keywords != null) {
				keywords = java.net.URLDecoder.decode(keywords, "UTF-8");
				maps.put("keywords", keywords);
			}
			String newtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String cnewtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-7*24*60*60*1000);
			String stime = (request.getParameter("startTime")==null||"".equals(request.getParameter("startTime")))?cnewtime:request.getParameter("startTime");
			String etime = (request.getParameter("endTime")==null||"".equals(request.getParameter("startTime")))?newtime:request.getParameter("endTime");
			Long startTime = new SimpleDateFormat("yyyy-MM-dd").parse(stime).getTime();
			Long endTime = new SimpleDateFormat("yyyy-MM-dd").parse(etime).getTime();
			maps.put("startTime", startTime);
			maps.put("endTime", endTime);
			String contentSl = request.getParameter("contentStyle");
			Page page = null;
			if(contentSl!=null&&(("topic").equals(contentSl)||("default").equals(contentSl))){
				maps.put("contentStyle", contentSl);
				page = productService.getThemeProducts(Integer.parseInt(pageNo), 10, maps);
			}else if(("product").equals(contentSl)){
				page = productService.getUserActionProduct(pageNo, 10, maps);
			}else if(("article").equals(contentSl)){
				page = articleService.getUserActionArticleList(Integer.parseInt(pageNo), 10, maps);
			}else if(("banner").equals(contentSl)){
				page = bannerService.getUserActionBannerList(Integer.parseInt(pageNo), 10, maps);
			}else{
				maps.put("contentStyle", contentSl);
				page = productService.getThemeProducts(Integer.parseInt(pageNo), 10, maps);
			}
			
			request.setAttribute("contentStyle", contentSl);
			request.setAttribute("startTime", stime);
			request.setAttribute("endTime", etime);
			request.setAttribute("page", page);
			request.setAttribute("pageNo", pageNo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "getUserActionSuccess";
	}
	
}
