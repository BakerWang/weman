package com.enation.app.api.action;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;

import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.PhoneBanner;
import com.enation.app.api.model.Theme;
import com.enation.app.api.model.ThemeContent;
import com.enation.app.api.model.ThemeTag;
import com.enation.app.api.service.ArticleService;
import com.enation.app.api.service.BannerService;
import com.enation.app.api.service.LiveService;
import com.enation.app.api.service.PersionService;
import com.enation.app.api.service.ProductService;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.gallery.model.GoodsGallery;
import com.enation.app.shop.core.model.Goods;
import com.enation.framework.database.Page;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Scope("prototype")
public class ProductAction extends BaseAction{

	private static final long serialVersionUID = -4833677871015547593L;
	
	
	@Resource
	private ProductService productService;
	
	
	@Resource
	private BannerService bannerService;
	
	
	
	/**
	 * 商品二级详情页 浮层页
	 */
	public void getProductDetailsAPP(){
		try {
			String accessToken = paramObject.has("accessToken")?paramObject.getString("accessToken"):null;
			int memberId = this.getMemberId(accessToken);
			int pid = Integer.parseInt(paramObject.getString("pid"));
			Map<String,Object> map = productService.getProductDetails(pid);
			Map<String, Object> nmap = new HashMap<String, Object>();
			int clickCount = 0;
			if(map.get("nologinClickCount")!=null){
				clickCount = (int)map.get("nologinClickCount");
			}
			if(memberId==0){
				nmap.put("nologinClickCount", clickCount+1);
			}else{
				nmap.put("loginClickCount", clickCount+1);
			}
			productService.updateProduct(pid, nmap);
			if(map!=null){
				jsonObject.put("pid", String.valueOf(pid));
				jsonObject.put("purl", String.valueOf(map.get("url")));
				jsonObject.put("hasCoupon", String.valueOf(map.get("hasCoupon")));
				jsonObject.put("h5Url", "http://www.weman.cc:8089/b2b2cbak/apiAdmin/AdminProductAction_getPrdocutDetailsApp.do?pid="+pid);
			}
			String viewUserId = null;
			String type = null;
			if(memberId==0){
				viewUserId = paramObject.has("clientId")?paramObject.getString("clientId"):null;
				type = "nologinClickProduct";
			}else{
				viewUserId = String.valueOf(memberId);
				type = "loginClickProduct";
			}
			persionService.saveUserAction(viewUserId, type, pid);
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取发现列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	
	
	/**
	 * 获取发现列表
	 */
	public void getFindList(){
		try {
			List<ThemeTag> themeTags = productService.getThemeTagList();
			JSONArray typeJa = new JSONArray();
			for(ThemeTag themetag:themeTags){
				if("身型".equals(themetag.getName())||"类型".equals(themetag.getName())){
					JSONObject typeObj = new JSONObject();
					typeObj.put("typeId", String.valueOf(themetag.getId()));
					typeObj.put("typeName", themetag.getName());
					JSONArray childrenTag = new JSONArray();
					for(ThemeTag ctag:themetag.getChildrenThemeTag()){
						JSONObject childrenTagObj = new JSONObject();
						childrenTagObj.put("id", String.valueOf(ctag.getId()));
						childrenTagObj.put("name", ctag.getName());
						if("熊".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findXiongDefault.png"));
						}else if("猴".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findHouDefault.png"));
						}else if("狒狒".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findFeiFeiDefault.png"));
						}else if("通用".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findAllDefault.png"));
						}else if("情趣".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findQingQuDefault.png"));
						}else if("护肤".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findHuFuDefault.png"));
						}else if("服装".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findFuZhuangDefault.png"));
						}else if("配饰".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findPeiShiDefault.png"));
						}else if("生活".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findShengHuoDefault.png"));
						}else{
							childrenTagObj.put("image", this.getImageUrl("123.png"));
						}
						if(ctag.getName().indexOf("双十一")<0){
							childrenTag.add(childrenTagObj);
						}
					}
					if("身型".equals(themetag.getName())){
						typeObj.put("typeStyle", "1");
						typeObj.put("typeData", childrenTag);
						typeJa.add(0,typeObj);
					}else if("类型".equals(themetag.getName())){
						typeObj.put("typeStyle", "2");
						String version = paramObject.has("version")?paramObject.getString("version"):null;
						if(version!=null&&version.startsWith("2.")){
							JSONObject childrenTagObj = new JSONObject();
							childrenTagObj.put("id", "1001");
							childrenTagObj.put("name", "直播分类");
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findLiveDefault.png"));
							childrenTag.add(3,childrenTagObj);
						}
						typeObj.put("typeData", childrenTag);
						typeJa.add(typeObj);
					}
				}
				
			}
			List<PhoneBanner> phoneBanners = bannerService.getCurrentBanners("发现banner");
			if(phoneBanners!=null&&phoneBanners.size()>0){
				JSONArray bannerJarray = new JSONArray();
				for(PhoneBanner pb:phoneBanners){
					JSONObject bannerObj = new JSONObject();
					bannerObj.put("bannerType", String.valueOf(pb.getType()));
					bannerObj.put("bannerId", String.valueOf(pb.getId()));
					bannerObj.put("bannerImage", this.getImageUrl(pb.getImage()));
					bannerObj.put("bannerData", pb.getDetails());
					bannerObj.put("bannerCategory", pb.getCategory());
					if(pb.getThemeContentStyle()!=null&&!pb.getThemeContentStyle().equals("0")){
						bannerObj.put("contentStyle", pb.getThemeContentStyle());
					}
					bannerObj.put("bannerData", pb.getDetails());
					bannerJarray.add(bannerObj);
				}
				jsonObject.put("bannerList", bannerJarray);
			}
			jsonObject.put("data", typeJa);
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取发现列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 点击banner进入主题列表接口
	 */
	@Resource
	private ArticleService articleService;
	
	public void getBannerThemeList(){
		try {
//			String clientId = paramObject.has("clientId")?paramObject.getString("clientId"):null;
//			int memberId = this.getMemberId(paramObject.has("accessToken")?paramObject.getString("accessToken"):null);
//			Map<String,Object> userView = new HashMap<String,Object>();
//			userView.put("status", "1");
//			userView.put("viewCount", "1");
//			userView.put("create_time", new Date().getTime());
//			if(memberId==0){
//				userView.put("viewUserId", Integer.parseInt(clientId));
//				userView.put("dataId", Integer.parseInt(paramObject.getString("bannerId")));
//				userView.put("type", "nologinClickBanner");
//			}else{
//				userView.put("viewUserId", memberId);
//				userView.put("dataId", Integer.parseInt(paramObject.getString("bannerId")));
//				userView.put("type", "loginClickBanner");
//			}
//			articleService.saveUserView(userView);
			int pageNo = Integer.parseInt(paramObject.getString("page"));
			Map<String, Object> maps = new HashMap<String, Object>();
			maps.put("bannerStatus", paramObject.getString("bannerId"));
			Page page =	productService.getThemeProducts(pageNo, 10 , maps);
			List<ThemeProduct> tps = (List<ThemeProduct>) page.getResult();
			JSONArray jsonArray = new JSONArray();
			for(ThemeProduct tp:tps){
				JSONObject theme = new JSONObject();
				theme.put("themeId", String.valueOf(tp.getTheme().getId()));
				theme.put("themeImage", this.getImageUrl(tp.getTheme().getMinorImage()));
				theme.put("contentStyle", tp.getTheme().getContentStyle());
//				theme.put("tags", tp.getTheme().getTagsImage());
				theme.put("themeTitle", tp.getTheme().getTitle());
				theme.put("themeDetails", tp.getTheme().getDetails());
				jsonArray.add(theme);
			}
			jsonObject.put("title", "双11专题");
			jsonObject.put("themeData", jsonArray);
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取发现列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	@Resource
	private LiveService liveService;
	
	@Resource
	private PersionService persionService;
	
	/**
	 * 获取回放列表接口
	 */
	public void getReplayList(){
		try {
			page.setCurrentPageNo(Long.parseLong(paramObject.getString("page")));
			page.setPageSize(10);
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("status", 1);
			Page resPage = liveService.getLivePalyBackList(map,page);
			List<Map<String,Object>> livelist = (List<Map<String, Object>>) resPage.getResult();
			String accessToken = paramObject.has("accessToken")?paramObject.getString("accessToken"):null;
			int member_id = this.getMemberId(accessToken);
			JSONArray jsonArray = new JSONArray();
			String path = request.getContextPath();
			String bpath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
			for(Map<String,Object> liveDetail :livelist){
				JSONObject liveObj = new JSONObject();
				liveObj.put("liveImage", this.getImageUrl((String)liveDetail.get("image")));
				liveObj.put("liveUrl", liveDetail.get("url"));
				liveObj.put("liveTargetUrl", liveDetail.get("targetUrl"));
				liveObj.put("liveId", String.valueOf(liveDetail.get("id")));
				liveObj.put("liveUserName", liveDetail.get("username"));
				liveObj.put("liveUserPhoto",bpath+"/statics/"+(String)liveDetail.get("userphoto"));
				if(accessToken==null){
					liveObj.put("isFriend", "nologin");
				}else{
					if(persionService.getIsFriend(member_id,String.valueOf((int)liveDetail.get("memberId")))){
						liveObj.put("isFriend", "yes");
					}else{
						liveObj.put("isFriend", "no");
					}
				}
				jsonArray.add(liveObj);
			}
			jsonObject.put("liveImage", this.getImageUrl("attachment/allDefaultImage/findReplayLiveDefault.png"));
			jsonObject.put("title", "直播回放接口");
			jsonObject.put("liveData", jsonArray);
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取发现列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 点击发现列表进入主题列表接口  包含首页的
	 */
	public void getFindThemeList(){
		try {
			String typeId = paramObject.getString("typeId");
			int pageNo = Integer.parseInt(paramObject.getString("page"));
			Map<String, Object> maps = new HashMap<String, Object>();
			if (typeId != null && !"0".equals(typeId)) {
				maps.put("typeId", typeId);
			}
			maps.put("findStatus", "1");
			String type = paramObject.has("type")?paramObject.getString("type"):null;
			Page page = null;
			if(type!=null&&"index".equals(type)){
				Map<String, String> map = new HashMap<String, String>();
				map.put("contentStyle", "topic");
				map.put("indexStatus", "1");
				map.put("typeId", typeId);
				page =	productService.getThemeProductsAPPVersion2(pageNo, 10 , map);
				List<Map<String,Object>> tps = (List<Map<String,Object>>) page.getResult();
				List<Integer> themeIds = new ArrayList<Integer>();
				for(Map<String,Object> tp:tps){
					themeIds.add((int)tp.get("id"));
				}
				List<Map<String,Object>> tcbts = productService.getThemeContentByThemeIds(themeIds);
				JSONArray jsonArray = new JSONArray();
				for(Map<String,Object> tp:tps){
					JSONObject theme = new JSONObject();
					int tpid = (int)tp.get("id");
					theme.put("themeId", tpid);
					theme.put("themeImage", this.getImageUrl((String)tp.get("image")));
					theme.put("contentStyle", tp.get("contentStyle"));
					theme.put("tags", this.getImageUrl((String)tp.get("tagImage")));
					JSONArray productDataJa = new JSONArray();
					for(Map<String,Object> tcbt:tcbts){
						int tid = (int)tcbt.get("tid");
						if(tid == tpid){
							JSONObject productObj = new JSONObject();
							productObj.put("pid", tcbt.get("pid"));
							//productObj.put("pname", tcbt.get("pname"));
							productObj.put("pname", tcbt.get("pbrief"));
							productObj.put("pimage", this.getImageUrl((String)tcbt.get("pimage")));
							productObj.put("pprice", String.valueOf(tcbt.get("pprice")));
							productObj.put("purl", tcbt.get("purl"));
							productObj.put("productOrigin", tcbt.get("productOrigin"));
							productDataJa.add(productObj);
						}
					}
					theme.put("productData", productDataJa);
					jsonArray.add(theme);
				}
				if(tps!=null&&tps.size()>0){
					jsonObject.put("themeData", jsonArray); 
				}
				jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeQingQuDefault.png"));
				ThemeTag ctag = productService.getThemeTagById(typeId);
				if(ctag!=null){
					jsonObject.put("title", ctag.getName()+"专题");
				}
			}else{
				page =	productService.getThemeProducts(pageNo, 10 , maps);
				List<ThemeProduct> tps = (List<ThemeProduct>) page.getResult();
				JSONArray jsonArray = new JSONArray();
				for(ThemeProduct tp:tps){
					JSONObject theme = new JSONObject();
					theme.put("themeId", String.valueOf(tp.getTheme().getId()));
					theme.put("themeImage", this.getImageUrl(tp.getTheme().getMinorImage()));
//				theme.put("tags", tp.getTheme().getTagsImage());
					theme.put("contentStyle", tp.getTheme().getContentStyle());
					theme.put("themeTitle", tp.getTheme().getTitle());
					theme.put("themeDetails", tp.getTheme().getDetails());
					jsonArray.add(theme);
				}
				ThemeTag ctag = productService.getThemeTagById(typeId);
				if(ctag!=null){
					jsonObject.put("title", ctag.getName());
					if("熊".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeXiongDefault.png"));
					}else if("猴".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeHouDefault.png"));
					}else if("狒狒".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeFeiFeiDefault.png"));
					}else if("通用".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeAllDefault.png"));
					}else if("服装".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeFuZhuangDefault.png"));
					}else if("护肤".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeHuFuDefault.png"));
					}else if("配饰".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemePeiShiDefault.png"));
					}else if("生活".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeShengHuoDefault.png"));
					}else if("情趣".equals(ctag.getName())){
						jsonObject.put("image", this.getImageUrl("attachment/allDefaultImage/findThemeQingQuDefault.png"));
					}else{
						jsonObject.put("image", this.getImageUrl("123.png"));
					}
				}else{
					jsonObject.put("image", this.getImageUrl("123.png"));
				}
				jsonObject.put("themeData", jsonArray);
			}
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取发现列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}
	
	/**
	 * 获取商品列表
	 */
	public void getProductList(){
		try {
			String pageNo = paramObject.getString("page");
			String namekeyword = paramObject.has("namekeyword")?paramObject.getString("namekeyword"):null;
			String cat_id = paramObject.has("cat_id")?paramObject.getString("cat_id"):null;
			String search_type = paramObject.has("search_type")?paramObject.getString("search_type"):"0";
			pageNo = (pageNo == null || pageNo.equals("")) ? "1" : pageNo;
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("namekeyword", namekeyword);
			map.put("cat_id", cat_id);
			map.put("search_type", search_type);
			Page page = productService.getProductList(Integer.parseInt(pageNo), 10, map);
			jsonObject.put("totalCount", page.getTotalPageCount());
			JSONArray resJa = new JSONArray();
			JSONArray ja = JSONArray.fromObject(page.getResult());
			for(int i=0;i<ja.size();i++){
				JSONObject resObj = new JSONObject();
				JSONObject obj = (JSONObject) ja.get(i);
				resObj.put("pname", obj.get("name"));
				resJa.add(resObj);
			}
			jsonObject.put("productList", resJa);
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取商品列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}

	/**
	 * 获取商品详情
	 */
	public void getProductDetails(){
		try {
			int productId = Integer.parseInt((String)paramObject.get("productId"));
			jsonObject = productService.getProductDetails(productId,jsonObject);
			String ggs = (String) jsonObject.get("productImages");
			if(ggs!=null){
				String productGallery = "";
				for(String gg:ggs.split(",")){
					productGallery = productGallery+this.getImageUrl(gg)+",";
				}
				productGallery = productGallery.substring(0, productGallery.lastIndexOf(","));
				jsonObject.put("productImages", productGallery);
			}
			jsonObject.put("productImage", this.getImageUrl((String)jsonObject.get("productImage")));
			jsonObject.put("productBrandImage", this.getImageUrl((String)jsonObject.get("productBrandImage")));
			jsonObject.put("productCatImage", this.getImageUrl((String)jsonObject.get("productCatImage")));
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取商品列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.print(jsonObject);
			out.close();
		}
	}

	
	/**
	 * 获取主题详情
	 */
	public void getThemeDetails(){
		try {
			int themeId = Integer.parseInt(paramObject.getString("themeId"));
			String accessToken = paramObject.has("accessToken")?paramObject.getString("accessToken"):null;
			int memberId = this.getMemberId(accessToken);
			Theme theme = productService.getThemeDetails(themeId,memberId);
			String viewUserId = null;
			String type = null;
			if(memberId==0){
				viewUserId = paramObject.has("clientId")?paramObject.getString("clientId"):null;
				type = "nologinClickTheme";
			}else{
				viewUserId = String.valueOf(memberId);
				type = "loginClickTheme";
			}
			persionService.saveUserAction(viewUserId, type, theme.getId());
			jsonObject.put("themeId", String.valueOf(theme.getId()));
			jsonObject.put("themeImage", this.getImageUrl(theme.getMinorImage()));
			jsonObject.put("themeTile", theme.getTitle());
			jsonObject.put("themeDate", theme.getShowDate());
			jsonObject.put("themeDetails", theme.getDetails());
			jsonObject.put("themeDetailsPosition", theme.getDetailsPosition());
			jsonObject.put("themeProductDetailsPosition", theme.getProductPosition());
			jsonObject.put("themeTitleSize", "18");
			//jsonObject.put("contentStyle", theme.getContentStyle());
			jsonObject.put("themeContentSize", "16");
			jsonObject.put("themeFontDistance", "1");//字间距
			jsonObject.put("themeCapableDistance", "8");//行间距
			jsonObject.put("themeCollectCount", theme.getLove_count());
			if(theme.getIsLove()==0){
				jsonObject.put("isLove", "no");
			}else{
				jsonObject.put("isLove", "yes");
			}
			if(theme.getThemeContent()!=null){
				JSONArray contents = new JSONArray();
				for(ThemeContent tc:theme.getThemeContent()){
					JSONObject content = new JSONObject();
					content.put("contentType", tc.getType());
					content.put("contentPosition", String.valueOf(tc.getPostion()));
					if("text".equals(tc.getType())){
						content.put("contentText", tc.getContent());
						content.put("contentFontSize", tc.getFontSize());
						content.put("contentCenter", tc.getCenter());
					}else if("image".equals(tc.getType())){
						content.put("contentImage", this.getImageUrl(tc.getImage()));
						content.put("contentWidth", String.valueOf(tc.getImagewidth()));
						content.put("contentHeight", String.valueOf(tc.getImageheight()));
					}else if("product".equals(tc.getType())){
						content.put("contentProductId", String.valueOf(tc.getGoods_id()));
						content.put("productOrigin", tc.getProductOrigin());
						content.put("contentProductName", tc.getProductBrief());//商品名字修改为商品标题
						content.put("contentProductImage", this.getImageUrl(tc.getProductImage()));
						content.put("contentProductCategoryImage", this.getImageUrl(tc.getProductCategoryImage()));
						content.put("contentProuctBrandName", tc.getProductBrandName());
						content.put("contentProductPrice", String.valueOf(tc.getProductPrice()));
						content.put("contentProductIntro", tc.getProductName());//简介修改为商品名字
						content.put("contentProductMKPrice", tc.getProductMkPrice());
						content.put("contentProductUrl", tc.getUrl());
						if("1".equals(tc.getStatus())){
							content.put("contentProductIsShowCat", "yes");
						}else{
							content.put("contentProductIsShowCat", "no");
						}
						if(tc.getIsCollect()==0){
							content.put("contentIsCollect", "no");
						}else{
							content.put("contentIsCollect", "yes");
						}
					}else if("video".equals(tc.getType())){
						content.put("contentVideoUrl", tc.getContent());
						content.put("contentVideoImage", this.getImageUrl(tc.getImage()));
						content.put("contentVideoImageWidth", String.valueOf(tc.getImagewidth()));
						content.put("contentVideoImageHeight", String.valueOf(tc.getImageheight()));
					}else if("defaultImage".equals(tc.getType())){
						content.put("defaultImage", this.getImageUrl("attachment/allDefaultImage/upArrowDefaultImage.png"));
						content.put("contentWidth", "640");
						content.put("contentHeight", "25");
					}
					contents.add(content);
				}
				jsonObject.put("themeContent", contents);
			}
			Map<String,Object> map=new HashMap<String,Object>();
			map.put("themeId", String.valueOf(themeId));
			map.put("recommendStatus", "1");
			Page page =	productService.getThemeProducts(1, 5 , map);
			List<ThemeProduct> tps = (List<ThemeProduct>) page.getResult();
			JSONArray jsonArray = new JSONArray();
			for(ThemeProduct tp:tps){
				JSONObject theme2 = new JSONObject();
				theme2.put("themeId", String.valueOf(tp.getTheme().getId()));
				theme2.put("themeImage", this.getImageUrl(tp.getTheme().getImage()));
				theme2.put("themeTitle", tp.getTheme().getTitle());
				theme2.put("themeDetails", tp.getTheme().getDetails());
				theme2.put("contentStyle", tp.getTheme().getContentStyle());
				theme2.put("tags", tp.getTheme().getTagsImage());
				jsonArray.add(theme2);
			}
			jsonObject.put("themeData", jsonArray);
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取主题详情失败",e);
			this.logger.error(e,e);
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	/**
	 * 获取主题列表  首页
	 */
	public void getThemeList(){
		try {
			int pageNo = Integer.parseInt(paramObject.getString("page"));
			String version = paramObject.getString("version");
			Map<String,String> map = new HashMap<String,String>();
			map.put("indexStatus", "1");
			if(pageNo>1&&version.startsWith("1.")){
				Map<String,Object> map2=new HashMap<String,Object>();
				map2.put("time", new Date().getTime());
				Page page = productService.getProductList(pageNo-1, 5, map2);
				jsonObject.put("totalCount", page.getTotalPageCount());
				JSONArray resJa = new JSONArray();
				JSONArray ja = JSONArray.fromObject(page.getResult());
				for(int i=0;i<ja.size();i++){
					JSONObject resObj = new JSONObject();
					JSONObject obj = (JSONObject) ja.get(i);
					resObj.put("pid", String.valueOf(obj.get("goods_id")));
					resObj.put("productOrigin", obj.get("productOrigin"));
					resObj.put("pname", obj.get("name"));
					resObj.put("pimage", this.getImageUrl((String)obj.get("original")));
					resObj.put("pprice", String.valueOf(obj.get("price")));
					resObj.put("pmktprice", String.valueOf(obj.get("mktprice")));
					resObj.put("purl", obj.get("url"));
					resObj.put("ptitle", obj.get("brief"));
					resObj.put("productOrigin", obj.get("productorigin"));
					if(obj.get("isshowmkprice")!=null&&(int)obj.get("isshowmkprice")==-1){
						resObj.put("isShowMKPrice", "no");
					}else{
						resObj.put("isShowMKPrice", "yes");
					}
					resJa.add(resObj);
				}
				jsonObject.put("productData", resJa);
				return;
			}else if(version.startsWith("1.")){
				Page page =	productService.getThemeProductsAPP(pageNo, 10 , map);
				List<Map<String,Object>> tps = (List<Map<String,Object>>) page.getResult();
				JSONArray jsonArray = new JSONArray();
				for(Map<String,Object> tp:tps){
					JSONObject theme = new JSONObject();
					theme.put("themeId", String.valueOf(tp.get("id")));
					theme.put("themeImage", this.getImageUrl((String)tp.get("image")));
					theme.put("contentStyle", tp.get("contentStyle"));
					theme.put("tags", this.getImageUrl((String)tp.get("tagImage")));
					jsonArray.add(theme);
				}
				jsonObject.put("themeData", jsonArray);
			}else if(version.startsWith("2.")){
				map.put("contentStyle", "topic");
				Page page =	productService.getThemeProductsAPPVersion2(pageNo, 10 , map);
				List<Map<String,Object>> tps = (List<Map<String,Object>>) page.getResult();
				List<Integer> themeIds = new ArrayList<Integer>();
				for(Map<String,Object> tp:tps){
					themeIds.add((int)tp.get("id"));
				}
				List<Map<String,Object>> tcbts = productService.getThemeContentByThemeIds(themeIds);
				JSONArray jsonArray = new JSONArray();
				for(Map<String,Object> tp:tps){
					JSONObject theme = new JSONObject();
					int tpid = (int)tp.get("id");
					theme.put("themeId", tpid);
					theme.put("themeImage", this.getImageUrl((String)tp.get("image")));
					theme.put("contentStyle", tp.get("contentStyle"));
					theme.put("tags", this.getImageUrl((String)tp.get("tagImage")));
					JSONArray productDataJa = new JSONArray();
					for(Map<String,Object> tcbt:tcbts){
						int tid = (int)tcbt.get("tid");
						if(tid == tpid){
							JSONObject productObj = new JSONObject();
							productObj.put("pid", tcbt.get("pid"));
							//productObj.put("pname", tcbt.get("pname"));
							productObj.put("pname", tcbt.get("pbrief"));
							productObj.put("pimage", this.getImageUrl((String)tcbt.get("pimage")));
							productObj.put("pprice", String.valueOf(tcbt.get("pprice")));
							productObj.put("purl", tcbt.get("purl"));
							productObj.put("productOrigin", tcbt.get("productOrigin"));
							productDataJa.add(productObj);
						}
					}
					theme.put("productData", productDataJa);
					jsonArray.add(theme);
				}
				if(tps!=null&&tps.size()>0){
					jsonObject.put("themeData", jsonArray); 
				}
				String accessToken = paramObject.has("accessToken")?paramObject.getString("accessToken"):null;
				Member member = this.getMemberDetails(accessToken);
				if(member!=null&&member.getCanLive()==1){
					jsonObject.put("canLive", "yes");
				}else{
					jsonObject.put("canLive", "no");
				}
			}
			//类型列表
			List<ThemeTag> themeTags = productService.getThemeTagList();
			for(ThemeTag themetag:themeTags){
				if("类型".equals(themetag.getName())){
					JSONObject typeObj = new JSONObject();
					typeObj.put("typeId", String.valueOf(themetag.getId()));
					typeObj.put("typeName", themetag.getName());
					JSONArray childrenTag = new JSONArray();
					for(ThemeTag ctag:themetag.getChildrenThemeTag()){
						JSONObject childrenTagObj = new JSONObject();
						childrenTagObj.put("id", String.valueOf(ctag.getId()));
						childrenTagObj.put("name", ctag.getName());
						if("熊".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findXiongDefault.png"));
						}else if("猴".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findHouDefault.png"));
						}else if("狒狒".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findFeiFeiDefault.png"));
						}else if("通用".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findAllDefault.png"));
						}else if("情趣".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findQingQuDefault.png"));
						}else if("护肤".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findHuFuDefault.png"));
						}else if("服装".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findFuZhuangDefault.png"));
						}else if("配饰".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findPeiShiDefault.png"));
						}else if("生活".equals(ctag.getName())){
							childrenTagObj.put("image", this.getImageUrl("attachment/allDefaultImage/findShengHuoDefault.png"));
						}else{
							childrenTagObj.put("image", this.getImageUrl("123.png"));
						}
						if(ctag.getName().indexOf("双十一")<0){
							childrenTag.add(childrenTagObj);
						}
					}
					jsonObject.put("typeData", childrenTag);
				}
			}
			//banner获取
			String accessToken = paramObject.has("accessToken")?paramObject.getString("accessToken"):null;
			int cmemberid = this.getMemberId(accessToken);
			//if(cmemberid!=0&&(cmemberid==3||cmemberid==1||cmemberid==36||cmemberid==4||cmemberid==30)){
				List<PhoneBanner> phoneBanners = bannerService.getCurrentBanners("首页banner");
				if(phoneBanners!=null&&phoneBanners.size()>0){
					JSONArray bannerJarray = new JSONArray();
					for(PhoneBanner pb:phoneBanners){
						JSONObject bannerObj = new JSONObject();
						bannerObj.put("bannerType", String.valueOf(pb.getType()));
						bannerObj.put("bannerId", String.valueOf(pb.getId()));
						bannerObj.put("bannerImage", this.getImageUrl(pb.getImage()));
						bannerObj.put("bannerData", pb.getDetails());
						bannerObj.put("bannerCategory", pb.getCategory());
						if(pb.getThemeContentStyle()!=null&&!pb.getThemeContentStyle().equals("0")){
							bannerObj.put("contentStyle", pb.getThemeContentStyle());
						}
						bannerJarray.add(bannerObj);
					}
					jsonObject.put("bannerList", bannerJarray);
				}
			//}
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取主题列表失败",e);
			this.logger.error(e,e);
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	/**
	 * 喜欢这个主题
	 */
	public void loveTheme(){
		try{
			int themeId = Integer.parseInt(paramObject.getString("themeId"));
			int memberId = Integer.parseInt(paramObject.getString("memberId"));
			productService.loveTheme(memberId, themeId);
		} catch (Exception e) {
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
			e.printStackTrace();
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("赞主题失败",e);
			this.logger.error(e,e);
		} finally {
			out.write(jsonObject.toString());
			out.close();
		}
	}
	
	
}
