package com.enation.app.api.action.admin.action;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.action.BaseAction;
import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.Theme;
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
	 * 到添加商品页面
	 * @return
	 */
	public String addProductJsp(){
		return "addProductJspSuccess";
	}
	
	private File productImage;
	private String productImageFileName;
	public void saveProduct(){
		try {
			String title = request.getParameter("title");
			String title2 = request.getParameter("title2");
			String url = request.getParameter("url");
			String details = request.getParameter("details");
			String price = request.getParameter("price");
			String mkprice=  request.getParameter("mkprice");
			int productCategory2 = Integer.parseInt(request.getParameter("productCategory2"));
			int productBrand = Integer.parseInt(request.getParameter("productBrand"));
			int isShowMKPrice = Integer.parseInt(request.getParameter("isShowMKPrice"));
			if(productImage!=null&&productImageFileName!=null&&!"".equals(productImageFileName)){
				if (FileUtil.isAllowUp(productImageFileName)) {
					String saveName = uploadImage(productImage,productImageFileName, "productImage");
					Map<String,Object> map = new HashMap<String,Object>();
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
	 * 修改是否上架
	 */
	public void updateProductStatus(){
		try{
			int productId = Integer.parseInt(request.getParameter("productId"));
			int mkstatus = Integer.parseInt(request.getParameter("mkstatus"));
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("disabled", mkstatus);
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
			List<Map<String,Object>> catbrands = articleService.beginArticle();
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
			String pageNo = request.getParameter("page");
			if (pageNo == null || "".equals(pageNo)) {
				pageNo = "1";
			}
			String keywords = request.getParameter("keywords");
			Map<String, String> maps = new HashMap<String, String>();
			if (keywords != null) {
				keywords = java.net.URLDecoder.decode(keywords, "UTF-8");
				maps.put("keywords", keywords);
			}
			Page page = productService.getThemeProducts(Integer.parseInt(pageNo), 10, maps);
			request.setAttribute("page", page);

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
					String saveName2 = uploadImage(themeFile2, imageName, "theme");
					theme.setImage(saveName);
					theme.setMinorImage(saveName2);
					System.out.println(contentArray);
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
			productService.deleTheme(Integer.parseInt(request.getParameter("themeId")));
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
			Map<String, String> map = new HashMap<String, String>();
			map.put("namekeyword", namekeyword);
			map.put("cat_id", cat_id);
			map.put("search_type", search_type);
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
				resObj.put("pimage", obj.get("small"));
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
