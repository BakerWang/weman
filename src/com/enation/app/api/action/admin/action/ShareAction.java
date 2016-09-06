package com.enation.app.api.action.admin.action;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.action.admin.form.ArticleModel;
import com.enation.app.api.dto.ArticleComment;
import com.enation.app.api.model.Theme;
import com.enation.app.api.service.ArticleService;
import com.enation.app.api.service.ProductService;
import com.enation.framework.database.Page;
@Scope("prototype")
public class ShareAction extends BaseAction{


	private static final long serialVersionUID = 7724168467598070006L;
	
	@Resource
	private ProductService productService;
	@Resource
	private ArticleService articleService;
	
	public String getDataDetails(){
		try {
			String type = request.getParameter("type");
			int dataId = Integer.parseInt(request.getParameter("dataId"));
			if("theme".equals(type)){
				Theme theme = productService.getThemeDetails(dataId, 0);
				request.setAttribute("theme", theme);
				Page page =	productService.getThemeProducts(1, 5 , null);
				request.setAttribute("pageObj", page);
				return "themeSuccess";
			}else{
				ArticleModel articleModel = articleService.getArtilceDetails(dataId);
				request.setAttribute("userAttr", articleModel.getUserHeight()+"."+articleModel.getUserWeight()+"."+this.getAge(articleModel.getUserAge()));
				request.setAttribute("article", articleModel);
				request.setAttribute("categoryImages", articleModel.getCategoryImage().split(","));
				request.setAttribute("brand", articleModel.getBrandName().split(","));
				request.setAttribute("showCreateTime", com.enation.framework.util.DateUtil.getShowDate(new Date((Long)articleModel.getCreateTime())));
				request.setAttribute("tagArray", articleModel.getTagStr().split(","));
				page.setPageSize(10);
				page.setCurrentPageNo(1);
				Page resPage = articleService.getCommentsByArticleId(articleModel.getId(),page,0);
				List<ArticleComment> acs = (List<ArticleComment>)resPage.getResult();
				for(ArticleComment ac :acs){
					ac.setPhoto(this.getImageUrl(ac.getPhoto()));
					ac.setCreateTimeStr(com.enation.framework.util.DateUtil.getShowDate(new Date((Long)ac.getCreate_time())));
				}
				request.setAttribute("commetList", acs);
			}
		} catch (Exception e) {
			this.logger.error("获取分享的详细内容失败！", e);
			this.logger.error(e,e);
		}
		return "articleSuccess";
	}
	
}
