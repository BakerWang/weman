package com.enation.app.api.action.admin.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.action.admin.form.ArticleModel;
import com.enation.app.api.action.admin.form.ArticleTagModel;
import com.enation.app.api.action.admin.form.AdminSearchForm;
import com.enation.app.api.service.ArticleService;
import com.enation.framework.database.Page;

@Scope("prototype")
public class AdminArticleAction extends BaseAction {

	private static final long serialVersionUID = 5138516194401455918L;

	public String goArticleList() {
		return "goArticleListSuccess";
	}

	@Resource
	private ArticleService articleService;
	private AdminSearchForm adminSearchForm;

	/**
	 * 发文列表
	 * @return
	 */
	public String articleList() {
		try {
			Page page1 = articleService.getArticleList(adminSearchForm, page);
			request.setAttribute("page", page1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "articleListSuccess";
	}

	/**
	 * 修改发文  删除发文  审核发文
	 */
	public void updateArticleStatus() {
		try {
			int articleId = Integer.parseInt(request.getParameter("articleId"));
			Map<String,Object> map = new HashMap<String,Object>();
			if(request.getParameter("status")!=null){
				int status = Integer.parseInt(request.getParameter("status"));
				map.put("status", status);
			}
			if(request.getParameter("viewCount")!=null){
				int viewCount = Integer.parseInt(request.getParameter("viewCount"));
				map.put("view_count", viewCount);
			}
			articleService.updateArticleStatus(articleId,map);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}

	/**
	 * 发文标签列表
	 * @return
	 */
	public String articleTagList(){
		try {
			Page page1 = articleService.getArticleTagList(adminSearchForm, page);
			request.setAttribute("page", page1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "articleTagListSuccess";
	}
	
	/**
	 * 更新发文标签状态
	 */
	public void updateArticleTagStatus(){
		try {
			int status = Integer.parseInt(request.getParameter("status"));
			int articleTagId = Integer.parseInt(request.getParameter("articleTagId"));
			articleService.updateArticleTagStatus(articleTagId,status);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}
	
	/**
	 * 更新发文标签是不是hot  type：1是hot -1不是
	 */
	public void updateArticleTagType(){
		try {
			int type = Integer.parseInt(request.getParameter("type"));
			int articleTagId = Integer.parseInt(request.getParameter("articleTagId"));
			articleService.updateArticleTagType(articleTagId,type);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}
	
	/**
	 * 保存发文标签
	 */
	public void saveArticleTag(){
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("content", request.getParameter("content"));
			map.put("type", 1);
			map.put("member_id", Integer.parseInt(request.getParameter("memberId")));
			articleService.saveArticleTag(map);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}

	
	/**
	 * 获取发文的评论列表
	 * @return
	 */
	public String fetchArticleCommentList(){
		try {
			Page pagea =  articleService.getArticleComments(adminSearchForm, page);
			request.setAttribute("page", pagea);
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return "fetchArticleCommentListSuccess";
	}
	
	
	/**
	 * 更新发文评论
	 */
	public void updateArticleComment(){
		try {
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("status", request.getParameter("status"));
			int commentId = Integer.parseInt(request.getParameter("memberId"));
			articleService.updateArticleComment(commentId, map);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}
	
	/**
	 * 用户动作列表
	 * @return
	 */
	public String userActionList(){
		try {
			Page page1 = articleService.getUserActionList(adminSearchForm, page);
			request.setAttribute("page", page1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "userActionListSuccess";
	}
	
	/**
	 * 更新用户动作
	 */
	public void updateUserAction(){
		try {
			int status = Integer.parseInt(request.getParameter("status"));
			int userActionId = Integer.parseInt(request.getParameter("userActionId"));
			articleService.updateArticleTagStatus(userActionId,status);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}
	
	
	public AdminSearchForm getAdminSearchForm() {
		return adminSearchForm;
	}

	public void setAdminSearchForm(AdminSearchForm adminSearchForm) {
		this.adminSearchForm = adminSearchForm;
	}
	
	
	
}
