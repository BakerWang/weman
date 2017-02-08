package com.enation.app.api.action.admin.action;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.struts2.ServletActionContext;
import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.Theme;
import com.enation.app.api.model.ThemeContent;
import com.enation.app.api.model.ThemeTag;
import com.enation.app.api.service.ArticleService;
import com.enation.app.api.service.ProductService;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.FileUtil;
import com.enation.framework.util.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
public class AdminProductAction extends BaseAction {

	private static final long serialVersionUID = -6797935445193944512L;

	@Resource
	private ProductService productService;
	@Resource
	private ArticleService articleService;

	
	/**
	 * app商品详情页  浮层
	 * @return
	 */
	public String getPrdocutDetailsApp(){
		try {
			int pid = Integer.parseInt(request.getParameter("pid"));
			Map<String,Object> map = productService.getProductDetails(pid);
			request.setAttribute("resObj", map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "getProductDetailsAppSuccess";
	}
	
	
	/**
	 * 后台概况详情
	 * @return
	 */
	public void adminIndexDetails(){
		try {
			String stime = ((request.getParameter("startTime")==null)||("".equals(request.getParameter("startTime"))))?"2016-01-01":request.getParameter("startTime");
			String etime = ((request.getParameter("endTime")==null)||("".equals(request.getParameter("endTime"))))?"2020-09-01":request.getParameter("endTime");
			Long startTime = new SimpleDateFormat("yyyy-MM-dd").parse(stime).getTime();
			Long endTime = new SimpleDateFormat("yyyy-MM-dd").parse(etime).getTime();
			Map<String,Object> resMap = productService.getAdminIndexDetails(startTime,endTime);
			jsonObject = JSONObject.fromObject(resMap);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	
	/**
	 * 未登录用户点击主题的详情
	 * @return
	 */
	public String noUserThemeCount(){
		try {
			String newtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String cnewtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-7*24*60*60*1000);
			String stime = request.getParameter("startTime")==null?cnewtime:request.getParameter("startTime");
			String etime = request.getParameter("endTime")==null?newtime:request.getParameter("endTime");
			Long startTime = new SimpleDateFormat("yyyy-MM-dd").parse(stime).getTime();
			Long endTime = new SimpleDateFormat("yyyy-MM-dd").parse(etime).getTime();
			int dataId = Integer.parseInt(request.getParameter("themeId"));
			page.setPageSize(30);
			String pageNo = request.getParameter("pageNo");
			if (pageNo == null || "".equals(pageNo)) {
				pageNo = "1";
			}
			page.setCurrentPageNo(Long.parseLong(pageNo));
			String contentType = "nologinClickTheme";
			String contentSl = request.getParameter("contentStyle");
			if("topic".equals(contentSl)||"default".equals(contentSl)){
					contentType = "nologinClickTheme";
			}else if("product".equals(contentSl)){
					contentType = "nologinClickProduct";
			}else if("article".equals(contentSl)){
					contentType = "nologinClickArticle";
			}else if("banner".equals(contentSl)){
					contentType = "nologinClickBanner-index";
			}
			Page respage = productService.noUserThemeCount(startTime,endTime,dataId,contentType,page);
			request.setAttribute("contentType", contentSl);
			request.setAttribute("resPage", respage);
			request.setAttribute("startTime", stime);
			request.setAttribute("endTime", etime);
			request.setAttribute("dataId", dataId);
			request.setAttribute("type", "nouser");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "userThemeCountSuccess";
	}
	
	
	/**
	 * 登录用户点击主题的详情
	 * @return
	 */
	public String userThemeCount(){
		try {
			String newtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
			String cnewtime = new SimpleDateFormat("yyyy-MM-dd").format(new Date().getTime()-7*24*60*60*1000);
			String stime = request.getParameter("startTime")==null?cnewtime:request.getParameter("startTime");
			String etime = request.getParameter("endTime")==null?newtime:request.getParameter("endTime");
			String usertartTime = request.getParameter("userStartTime")==null?cnewtime:request.getParameter("userStartTime");
			String userndTime = request.getParameter("userEndTime")==null?newtime:request.getParameter("userEndTime");
			Long startTime = new SimpleDateFormat("yyyy-MM-dd").parse(stime).getTime();
			Long endTime = new SimpleDateFormat("yyyy-MM-dd").parse(etime).getTime();
			Long userStartTime = new SimpleDateFormat("yyyy-MM-dd").parse(usertartTime).getTime()/1000;
			Long userEndTime =new Date().getTime();
			if(!newtime.equals(userndTime)){
				userEndTime = new SimpleDateFormat("yyyy-MM-dd").parse(userndTime).getTime()/1000;
			}
			int dataId = Integer.parseInt(request.getParameter("themeId"));
			page.setPageSize(30);
			String pageNo = request.getParameter("pageNo");
			if (pageNo == null || "".equals(pageNo)) {
				pageNo = "1";
			}
			page.setCurrentPageNo(Long.parseLong(pageNo));
			String contentType = "";
			String contentSl = request.getParameter("contentStyle");
			if("topic".equals(contentSl)||"default".equals(contentSl)){
					contentType = "loginClickTheme";
			}else if("product".equals(contentSl)){
					contentType = "loginClickProduct";
			}else if("article".equals(contentSl)){
					contentType = "loginClickArticle";
			}else if("banner".equals(contentSl)){
					contentType = "loginClickBanner-index";
			}
			Page respage = productService.userThemeCount(startTime,endTime,userStartTime,userEndTime,dataId,contentType,page);
			request.setAttribute("contentType", contentSl);
			request.setAttribute("resPage", respage);
			request.setAttribute("startTime", stime);
			request.setAttribute("endTime", etime);
			request.setAttribute("userStartTime", usertartTime);
			request.setAttribute("userEndTime", userndTime);
			request.setAttribute("dataId", dataId);
			request.setAttribute("type", "user");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "userThemeCountSuccess";
	}
	
	/**
	 * 到添加商品页面
	 * @return
	 */
	public String addProductJsp(){
		return "addProductJspSuccess";
	}
	
	private File productImage;
	private String productImageFileName;
	/**
	 * 添加商品
	 */
	public void saveProduct(){
		try {
			String title = request.getParameter("title");
			String title2 = request.getParameter("title2");
			String url = request.getParameter("url");
			String details = request.getParameter("details");
			String price = request.getParameter("price");
			String mkprice=  request.getParameter("mkprice");
			String productOrigin = request.getParameter("productOrigin");
			Long startTime = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("startTime")).getTime();
			Long endTime = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("endTime")).getTime();
			int productCategory2 = Integer.parseInt(request.getParameter("productCategory2"));
			int productBrand = Integer.parseInt(request.getParameter("productBrand"));
			int isShowMKPrice = Integer.parseInt(request.getParameter("isShowMKPrice"));
			//int hasCoupon = Integer.parseInt(request.getParameter("hasCoupon"));
			if(productImage!=null&&productImageFileName!=null&&!"".equals(productImageFileName)){
				if (FileUtil.isAllowUp(productImageFileName)) {
					String saveName = uploadImage(productImage,productImageFileName, "productImage");
					Map<String,Object> map = new HashMap<String,Object>();
					map.put("startTime", startTime);
					map.put("endTime", endTime);
					map.put("name", title);
					map.put("brief", title2);
					map.put("url", url);
					map.put("intro", details);
					map.put("cat_id", productCategory2);
					map.put("brand_id", productBrand);
					map.put("isShowMKPrice", isShowMKPrice);
					map.put("original", saveName);
					map.put("price", price);
					map.put("mktprice", mkprice);
					map.put("productOrigin", productOrigin);
					map.put("hasCoupon", -1);
					productService.addProduct(map);
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	/**
	 * 跳转到商品修改的详情页面
	 * @return
	 */
	public String goProductDetails(){
		try {
			Map<String,Object> goods = productService.getProductDetails(Integer.parseInt(request.getParameter("pid")));
			request.setAttribute("product", goods);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goProductDetailsSuccess";
	}
	/**
	 * 修改商品
	 */
	public void updateProduct(){
		try {
			String title = request.getParameter("title");
			String title2 = request.getParameter("title2");
			String url = request.getParameter("url");
			String details = request.getParameter("details");
			String price = request.getParameter("price");
			String mkprice=  request.getParameter("mkprice");
			String productOrigin = request.getParameter("productOrigin");
			Long startTime = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("startTime")).getTime();
			Long endTime = new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("endTime")).getTime();
//			int productCategory2 = Integer.parseInt(request.getParameter("productCategory2"));
//			int productBrand = Integer.parseInt(request.getParameter("productBrand"));
			int isShowMKPrice = Integer.parseInt(request.getParameter("isShowMKPrice"));
			Map<String,Object> map = new HashMap<String,Object>();
			if(productImage!=null&&productImageFileName!=null&&!"".equals(productImageFileName)){
				if (FileUtil.isAllowUp(productImageFileName)) {
					String saveName = uploadImage(productImage,productImageFileName, "productImage");
					map.put("original", saveName);
				}
			}
			map.put("startTime", startTime);
			map.put("endTime", endTime);
			map.put("name", title);
			map.put("brief", title2);
			map.put("url", url);
			map.put("intro", details);
//			map.put("cat_id", productCategory2);
//			map.put("brand_id", productBrand);
			map.put("isShowMKPrice", isShowMKPrice);
			map.put("price", price);
			map.put("mktprice", mkprice);
			map.put("productOrigin", productOrigin);
			int productId = Integer.parseInt(request.getParameter("productId"));
			productService.updateProduct(productId, map);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	
	/**
	 * 修改是否上架
	 */
	public void updateProductStatus(){
		try{
			int productId = Integer.parseInt(request.getParameter("productId"));
			int mkstatus = Integer.parseInt(request.getParameter("mkstatus"));
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("market_enable", mkstatus);
			productService.updateProduct(productId,map);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	/**
	 * 修改是否上架
	 */
	public void updateProductOrigin(){
		try{
			int productId = Integer.parseInt(request.getParameter("productId"));
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("productOrigin", request.getParameter("productOrigin"));
			productService.updateProduct(productId,map);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	
	/**
	 * 获取类别和品牌的json
	 */
	public void addProductJson(){
		try {
			List<Map<String,Object>> catbrands = articleService.beginArticle(0);
			JSONArray dataArray = new JSONArray();
			List<Integer> data = new ArrayList<Integer>();
			for(Map<String,Object> map:catbrands){
				int parentId = (int)map.get("cparentid");
				int pcid = (int)map.get("cid");
				if(parentId==0&&!data.contains(pcid)){
					data.add(pcid);
					JSONObject objJson = new JSONObject();
					objJson.put("catId", String.valueOf(pcid));
					objJson.put("catName", (String)map.get("cname"));
					JSONArray objArray = new JSONArray();
					int cid = (int)map.get("cid");
					List<Integer> cdata = new ArrayList<Integer>();
					for(Map<String,Object> cmap:catbrands){
						int cparentId = (int)cmap.get("cparentid");
						int objcatid = (int)cmap.get("cid");
						if(cparentId==cid&&!cdata.contains(objcatid)){
							cdata.add(objcatid);
							JSONObject obj = new JSONObject();
							obj.put("catId", String.valueOf(objcatid));
							obj.put("catName", (String)cmap.get("cname"));
							JSONArray brandArray = new JSONArray();
							for(Map<String,Object> bmap:catbrands){
								int brandcatid = (int)bmap.get("cid");
								if(brandcatid==objcatid){
									JSONObject brandObj = new JSONObject();
									brandObj.put("id", String.valueOf((int)bmap.get("bid")));
									brandObj.put("image", this.getImageUrl((String)bmap.get("blogo")));
									brandObj.put("name", (String)bmap.get("bname"));
									brandArray.add(brandObj);
								}
							}
							obj.put("brandData", brandArray);
							objArray.add(obj);
						}
					}
					objJson.put("children", objArray);
					dataArray.add(objJson);
				}
			}
			jsonObject.put("data", dataArray);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	
	/**
	 * 获取主题列表
	 */
	public String getThemeList() {
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
			String stime = request.getParameter("startTime")==null?"2016-09-01":request.getParameter("startTime");
			String etime = request.getParameter("endTime")==null?"2020-09-01":request.getParameter("endTime");
			Long startTime = new SimpleDateFormat("yyyy-MM-dd").parse(stime).getTime();
			Long endTime = new SimpleDateFormat("yyyy-MM-dd").parse(etime).getTime();
			maps.put("startTime", startTime);
			maps.put("endTime", endTime);
			String contentSl = request.getParameter("contentStyle");
			if(contentSl!=null&&!"0".equals(contentSl)){
				maps.put("contentStyle", contentSl);
			}
			Page page = productService.getThemeProducts(Integer.parseInt(pageNo), 10, maps);
			request.setAttribute("contentStyle", contentSl);
			request.setAttribute("startTime", stime);
			request.setAttribute("endTime", etime);
			request.setAttribute("page", page);
			request.setAttribute("pageNo", pageNo);
			List<ThemeTag> parentTags = productService.getParentThemeTagList();
			request.setAttribute("parentTags", parentTags);
			List<ThemeTag> childrenTags = productService.getChildrenThemeTagList();
			request.setAttribute("childrenTags", childrenTags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "getThemeListSuccess";
	}

	
	/**
	 * 获取主题详情
	 */
	public String getThemeDetails(){
		try {
			Theme theme = productService.getThemeDetails(Integer.parseInt(request.getParameter("themeId")), 0);
			request.setAttribute("theme", theme);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goThemeDetailsSuccess";
	}
	
	/**
	 * 跳转到新增主题页面
	 * 
	 * @return
	 */
	public String goNewTheme() {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goNewThemeSuccess";
	}

	private File themeFile;
	private String themeFileFileName;
	private File themeFile2;
	private String themeFile2FileName;
	private Theme theme;

	private String contentArray;
	/**
	 * 保存新的主题
	 * 
	 * @return
	 */
	public void saveTheme() {
		try {
			if (themeFile != null && themeFile2 != null) {
				String imageName = themeFileFileName;
				if (FileUtil.isAllowUp(imageName)) {
					String saveName = uploadImage(themeFile, imageName, "theme");
					saveName = resizeImage(saveName,500);
					String saveName2 = uploadImage(themeFile2, imageName, "theme");
					saveName2 = resizeImage(saveName2,500);
					theme.setImage(saveName);
					theme.setMinorImage(saveName2);
					theme.setStartTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(request.getParameter("startTime")).getTime());
					JSONArray ja = JSONArray.fromObject(contentArray);
					List<Map<String,Object>> contentMaps = new ArrayList<Map<String,Object>>();
					for (int i = 0; i < ja.size(); i++) {
						Map<String, Object> contentMap = new HashMap<String, Object>();
						JSONObject obj = ja.getJSONObject(i);
						String type = obj.getString("type");
						contentMap.put("position",String.valueOf(obj.getInt("position")));
						contentMap.put("type", type);
						contentMap.put("status", "1");
						contentMap.put("createtime", new Date().getTime());
						if ("text".equals(type)) {
							contentMap.put("content", obj.getString("content"));
							contentMap.put("fontsize", obj.getString("fontSize"));
							contentMap.put("center", obj.getString("center"));
							contentMap.put("imagewidth", 0);
							contentMap.put("imageheight", 0);
							contentMap.put("goods_id", 0);
						} else if ("image".equals(type)) {
							String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+obj.getString("image");
							if(new File(imagePath).exists()){
								File oldFile = new File(imagePath);
								String newFilePath = imagePath.replace("temp", "theme");
								FileUtil.createFile(oldFile, newFilePath);
								FileUtil.delete(imagePath);
								contentMap.put("image", obj.getString("image").replace("temp", "theme"));
								contentMap.put("imagewidth", Integer.parseInt(obj.getString("width")));
								contentMap.put("imageheight", Integer.parseInt(obj.getString("height")));
								contentMap.put("goods_id", 0);
							}
						} else if ("product".equals(type)) {
							contentMap.put("imagewidth", 0);
							contentMap.put("imageheight", 0);
							contentMap.put("goods_id", Integer.parseInt(obj.getString("productId")));
							int showType = Integer.parseInt(obj.getString("showType"));
							if(showType==2){
								contentMap.put("status", "2");
							}
						}else if("video".equals(type)){
							String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+obj.getString("image");
							if(new File(imagePath).exists()){
								File oldFile = new File(imagePath);
								String newFilePath = imagePath.replace("temp", "theme");
								FileUtil.createFile(oldFile, newFilePath);
								FileUtil.delete(imagePath);
								contentMap.put("image", obj.getString("image").replace("temp", "theme"));
								contentMap.put("content", obj.getString("content"));
								contentMap.put("imagewidth", Integer.parseInt(obj.getString("width")));
								contentMap.put("imageheight", Integer.parseInt(obj.getString("height")));
								contentMap.put("goods_id", 0);
							}
						}else if("defaultImage".equals(type)){
							contentMap.put("imagewidth", 0);
							contentMap.put("imageheight", 0);
							contentMap.put("goods_id", 0);
						}
						contentMaps.add(contentMap);
					}
					Theme theme2 = productService.saveTheme(theme,contentMaps);
					jsonObject.put("result", "yes");
					jsonObject.put("themeId", theme2.getId());
				} else {
					jsonObject.put("result", "no");
					jsonObject.put("reason", "上传格式不对！");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}

	/**
	 * 修改主题的内容
	 */
	public void updateThemeContent(){
		try {
			String fontSize = request.getParameter("fontSize");
			String center = request.getParameter("center");
			String content = request.getParameter("content");
			String image = request.getParameter("image");
			String type = request.getParameter("type");
			Map<String,Object> map = new HashMap<String,Object>();
			if(type!=null&&"image".equals(type)){
				int tcid = Integer.parseInt(request.getParameter("tcid"));
				String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/"+image;
				if(new File(imagePath).exists()){
					File oldFile = new File(imagePath);
					String newFilePath = imagePath.replace("temp", "theme");
					FileUtil.createFile(oldFile, newFilePath);
					FileUtil.delete(imagePath);
					map.put("image", image.replace("temp", "theme"));
				}
				productService.updateThemeContent(tcid,map);
			}else if(type!=null&&"text".equals(type)){
				int tcid = Integer.parseInt(request.getParameter("tcid"));
				map.put("fontsize", fontSize);
				map.put("center", center);
				map.put("content", content);
				productService.updateThemeContent(tcid,map);
			}else if(type!=null&&"theme".equals(type)){
				String title = request.getParameter("title");
				int themeId = Integer.parseInt(request.getParameter("themeId"));
				map.put("title", title);
				map.put("details", request.getParameter("details"));
				map.put("productPosition", request.getParameter("productPosition"));
				map.put("detailsPosition", request.getParameter("detailsPosition"));
				map.put("showDate", request.getParameter("showDate"));
				map.put("startTime", new SimpleDateFormat("yyyy-MM-dd HH:mm").parse(request.getParameter("startTime")).getTime());
				if (themeFile != null && themeFile2 != null) {
					String imageName = themeFileFileName;
					if (FileUtil.isAllowUp(imageName)) {
						String saveName = uploadImage(themeFile, imageName, "theme");
						String saveName2 = uploadImage(themeFile2, imageName, "theme");
						map.put("image", saveName);
						map.put("minorImage", saveName2);
					}
				}
				productService.updateTheme(themeId, map);
			}else{
				String isIndexShow = request.getParameter("isIndexShow");
				int tcid = Integer.parseInt(request.getParameter("tcid"));
				if(isIndexShow!=null&&!"".equals(isIndexShow)){
					map.put("isIndexShow", Integer.parseInt(isIndexShow));
					productService.updateThemeContent(tcid, map);
				}else{
					productService.deleThemeContent(tcid);
				}
			}
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	
	
	public String getContentArray() {
		return contentArray;
	}

	public void setContentArray(String contentArray) {
		this.contentArray = contentArray;
	}

	public void updateThemeStatus() {
		try {
			int themeId = Integer.parseInt(request.getParameter("themeId"));
			int loveCount = Integer.parseInt(request.getParameter("loveCount"));
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("love_count", loveCount);
			productService.updateTheme(themeId, maps);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}

	public void updateThemeTag() {
		try {
			int themeId = Integer.parseInt(request.getParameter("themeId"));
			int keyId = Integer.parseInt(request.getParameter("keyId"));
			int valueId = Integer.parseInt(request.getParameter("valueId"));
			productService.updateThemeTag(themeId, keyId, valueId);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	public String fetchThemeTag(){
		try {
			ThemeTag tt = productService.getThemeTagById(request.getParameter("ttid"));
			request.setAttribute("themeTag", tt);
			List<ThemeTag> parentTags = productService.getParentThemeTagList();
			request.setAttribute("parentTags", parentTags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "fetchThemeTagSuccess";
	}

	public void updateThemeTagObj(){
		try {
			ThemeTag tt = productService.getThemeTagById(String.valueOf(themeTag.getId()));
			if (themeTagFile != null && themeTagFile.size() > 0) {
				File image = themeTagFile.get(0);
				String imageName = themeTagFileFileName.get(0);
				if (FileUtil.isAllowUp(imageName)) {
					String trueUrl = ServletActionContext.getServletContext().getRealPath("/statics/"+tt.getImage());
					File eFile = new java.io.File(trueUrl);
					if(eFile.exists()){
						eFile.delete();
					}
					String path = UploadUtil.upload(image, imageName, "themeTag");
					tt.setImage(path);
				} else {
					jsonObject.put("result", "no");
					jsonObject.put("reason", "上传格式不对！");
				}
			}
			tt.setStatus(1);
			tt.setCreate_time(new Date().getTime());
			productService.updateThemeTag(tt);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	public String goThemeDetails() {
		try {
			String themeId = request.getParameter("themeId");
			ThemeProduct tp = productService.catchThemeDetails(Integer.parseInt(themeId));
			request.setAttribute("themeProduct", tp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "goThemeDetailsSuccess";
	}

	public void updateDetials() {
		try {
			if (themeFile != null ) {
				FileUtil.delete(theme.getImage());
				String imageName = themeFileFileName;
				if (FileUtil.isAllowUp(imageName)) {
					String path = UploadUtil.upload(themeFile, imageName, "theme");
					theme.setImage(path);
				} else {
					jsonObject.put("result", "no");
					jsonObject.put("reason", "上传格式不对！");
					return;
				}
			}
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("title", theme.getTitle());
			maps.put("image", theme.getImage());
			maps.put("details", theme.getDetails());
			productService.updateTheme(theme.getId(), maps);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}

	public void deleteTheme() {
		try {
			Theme theme = productService.getThemeDetails(Integer.parseInt(request.getParameter("themeId")), 0);
			if(theme!=null){
				String imagePath = request.getSession().getServletContext().getRealPath("/statics")+"/";
				if(new File(imagePath+theme.getImage()).exists()){
					FileUtil.delete(imagePath+theme.getImage());
				}
				if(new File(imagePath+theme.getMinorImage()).exists()){
					FileUtil.delete(imagePath+theme.getMinorImage());
				}
				for(ThemeContent tc:theme.getThemeContent()){
					if("image".equals(tc.getType())){
						FileUtil.delete(imagePath+tc.getImage());
					}
				}
				productService.deleTheme(Integer.parseInt(request.getParameter("themeId")));
				jsonObject.put("result", "yes");
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

	
	public void updateThemeStatusObj(){
		try {
			int themeId = Integer.parseInt(request.getParameter("themeId"));
			String status = request.getParameter("status");
			Map<String,Object> map = new HashMap<String,Object>();
			String[] statusArray = status.split(",");
			map.put("indexStatus", Integer.parseInt(statusArray[0]));
			map.put("findStatus", Integer.parseInt(statusArray[1]));
			map.put("recommendStatus", Integer.parseInt(statusArray[2]));
			map.put("status", "1");
			productService.updateTheme(themeId, map);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	
	/**
	 * 获取商品列表
	 */
	public void getProductList() {
		try {
			String pageNo = request.getParameter("page");
			String namekeyword = request.getParameter("namekeyword");
			String cat_id = request.getParameter("cat_id");
			String search_type = request.getParameter("search_type");
			pageNo = (pageNo == null || pageNo.equals("")) ? "1" : pageNo;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("namekeyword", namekeyword);
			map.put("cat_id", cat_id);
			map.put("disabled", 10);
			map.put("search_type", search_type);
			map.put("order", "g.goods_id");
			Page page = productService.getProductList(Integer.parseInt(pageNo), 10, map);
			jsonObject.put("totalCount", page.getTotalCount());
			JSONArray resJa = new JSONArray();
			JSONArray ja = JSONArray.fromObject(page.getResult());
			for (int i = 0; i < ja.size(); i++) {
				JSONObject resObj = new JSONObject();
				JSONObject obj = (JSONObject) ja.get(i);
				resObj.put("pid", obj.get("goods_id"));
				resObj.put("pname", obj.get("name"));
				resObj.put("pprice", obj.get("price"));
				resObj.put("pimage", obj.get("original"));
				resObj.put("purl", obj.get("url"));
				resObj.put("pcreateTime", obj.get("create_time"));
				resObj.put("pviewCount", obj.get("view_count"));
				resJa.add(resObj);
			}
			jsonObject.put("result", "yes");
			jsonObject.put("productList", resJa);
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}

	/**
	 * 获取主题列表 json
	 */
	public void getThemeJsonList() {
		try {
			String pageNo = request.getParameter("page");
			String namekeyword = request.getParameter("namekeyword");
			pageNo = (pageNo == null || pageNo.equals("")) ? "1" : pageNo;
			Map<String, String> map = new HashMap<String, String>();
			map.put("namekeyword", namekeyword);
			Page page = productService.getThemeJsonList(Integer.parseInt(pageNo), 10, map);
			jsonObject.put("totalCount", page.getTotalCount());
			JSONArray resJa = new JSONArray();
			JSONArray ja = JSONArray.fromObject(page.getResult());
			for (int i = 0; i < ja.size(); i++) {
				JSONObject resObj = new JSONObject();
				JSONObject obj = (JSONObject) ja.get(i);
				resObj.put("pid", obj.get("id"));
				resObj.put("pname", obj.get("title"));
				resObj.put("pprice", obj.get("product_count"));
				resObj.put("pimage", obj.get("image"));
				resObj.put("pcreateTime", obj.get("create_time"));
				resObj.put("pviewCount", obj.get("love_count"));
				resObj.put("pthemeContentStyle", obj.get("contentStyle"));
				resJa.add(resObj);
			}
			jsonObject.put("result", "yes");
			jsonObject.put("productList", resJa);
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}

	/**
	 * 获取主题标签列表
	 * 
	 * @return
	 */
	public String themeTagList() {
		try {
			List<ThemeTag> themeTags = productService.getThemeTagList();
			request.setAttribute("themeTagList", themeTags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "themeTagListSuccess";
	}

	public String saveThemeTag() {
		try {
			List<ThemeTag> parentTags = productService.getParentThemeTagList();
			request.setAttribute("parentTags", parentTags);
			// List<ThemeTag> childrenTags =
			// productService.getChildrenThemeTagList();
			// request.setAttribute("childrenTags", childrenTags);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "saveThemeTagSuccess";
	}

	public void deleThemeTag() {
		try {
			Long ttId = Long.parseLong(request.getParameter("ttId"));
			productService.deleThemeTag(ttId);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}

	private ThemeTag themeTag;
	private List<File> themeTagFile;
	private List<String> themeTagFileFileName;

	public void addThemeTag() {
		try {
			if (themeTagFile != null && themeTagFile.size() > 0) {
				File image = themeTagFile.get(0);
				String imageName = themeTagFileFileName.get(0);
				if (FileUtil.isAllowUp(imageName)) {
					String path = UploadUtil.upload(image, imageName, "themeTag");
					themeTag.setImage(path);
				} else {
					jsonObject.put("result", "no");
					jsonObject.put("reason", "上传格式不对！");
				}
			}
			themeTag.setStatus(1);
			themeTag.setCreate_time(new Date().getTime());
			productService.addThemeTag(themeTag);
			jsonObject.put("result", "yes");
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.write(jsonObject.toString());
		}
	}

	public void addProductForTheme() {
		try {
			String themeid = request.getParameter("themeid");
			String productid = request.getParameter("productid");
			String status = request.getParameter("status");
			String position = request.getParameter("position");
			String type = request.getParameter("type");
			productService.saveProductJoinTheme(Integer.parseInt(productid), Integer.parseInt(themeid),
					position != null ? Integer.parseInt(position) : 0, status, type);
		} catch (Exception e) {
			jsonObject.put("result", "no");
			e.printStackTrace();
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}



	public File getThemeFile() {
		return themeFile;
	}


	public void setThemeFile(File themeFile) {
		this.themeFile = themeFile;
	}


	public String getThemeFileFileName() {
		return themeFileFileName;
	}


	public void setThemeFileFileName(String themeFileFileName) {
		this.themeFileFileName = themeFileFileName;
	}


	public Theme getTheme() {
		return theme;
	}

	public void setTheme(Theme theme) {
		this.theme = theme;
	}

	public ThemeTag getThemeTag() {
		return themeTag;
	}

	public void setThemeTag(ThemeTag themeTag) {
		this.themeTag = themeTag;
	}

	public List<File> getThemeTagFile() {
		return themeTagFile;
	}

	public void setThemeTagFile(List<File> themeTagFile) {
		this.themeTagFile = themeTagFile;
	}

	public List<String> getThemeTagFileFileName() {
		return themeTagFileFileName;
	}

	public void setThemeTagFileFileName(List<String> themeTagFileFileName) {
		this.themeTagFileFileName = themeTagFileFileName;
	}


	public File getProductImage() {
		return productImage;
	}


	public void setProductImage(File productImage) {
		this.productImage = productImage;
	}


	public String getProductImageFileName() {
		return productImageFileName;
	}


	public File getThemeFile2() {
		return themeFile2;
	}


	public void setThemeFile2(File themeFile2) {
		this.themeFile2 = themeFile2;
	}


	public String getThemeFile2FileName() {
		return themeFile2FileName;
	}


	public void setThemeFile2FileName(String themeFile2FileName) {
		this.themeFile2FileName = themeFile2FileName;
	}


	public void setProductImageFileName(String productImageFileName) {
		this.productImageFileName = productImageFileName;
	}

}
