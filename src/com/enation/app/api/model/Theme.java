package com.enation.app.api.model;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.NotDbField;

public class Theme {

	private int id;
	private String title;
	private String title2;
	private String image;
	private String minorImage;//主题副图
	private String productPosition;//主题商品描述的位置
	private String detailsPosition;//主题内容的位置
	private int author;//主题的作者
	private String details;
	private String showDate;
	private int love_count;
	private int product_count;
	private String contentStyle;//主题的风格显示
	private String status;//1是正常  -1是删除
	private int theme_tag_id;
	private Long create_time;
	private Long startTime;
	private Map<Integer,Integer> themetagList;
	private String tagsImage;//主题tag的图片列表
	private List<ThemeContent> themeContent;

	private long realClickCount;//真实点击数   不是数据库字段
	private int clickCount;//未登录用户点击数
	private int loginClickCount;//登录用户的点击数
	
	private int isLove = 0;//是否收藏
	
	private int indexStatus =0;//首页是否显示
	private int findStatus =0;//发现页是否显示
	private int recommendStatus =0;//推荐是否显示
	private int bannerStatus = 0;//banner页是否显示此主题
	
	
	
	public int getAuthor() {
		return author;
	}
	public void setAuthor(int author) {
		this.author = author;
	}
	public String getTitle2() {
		return title2;
	}
	public void setTitle2(String title2) {
		this.title2 = title2;
	}
	public String getContentStyle() {
		return contentStyle;
	}
	public void setContentStyle(String contentStyle) {
		this.contentStyle = contentStyle;
	}
	public int getLoginClickCount() {
		return loginClickCount;
	}
	public void setLoginClickCount(int loginClickCount) {
		this.loginClickCount = loginClickCount;
	}
	public int getClickCount() {
		return clickCount;
	}
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	public List<ThemeContent> getThemeContent() {
		return themeContent;
	}
	public void setThemeContent(List<ThemeContent> themeContent) {
		this.themeContent = themeContent;
	}
	public String getTagsImage() {
		return tagsImage;
	}
	public void setTagsImage(String tagsImage) {
		this.tagsImage = tagsImage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public int getLove_count() {
		return love_count;
	}
	public void setLove_count(int love_count) {
		this.love_count = love_count;
	}
	public int getProduct_count() {
		return product_count;
	}
	public void setProduct_count(int product_count) {
		this.product_count = product_count;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getTheme_tag_id() {
		return theme_tag_id;
	}
	public void setTheme_tag_id(int theme_tag_id) {
		this.theme_tag_id = theme_tag_id;
	}
	public Map<Integer, Integer> getThemetagList() {
		return themetagList;
	}
	public void setThemetagList(Map<Integer, Integer> themetagList) {
		this.themetagList = themetagList;
	}
	public String getShowDate() {
		return showDate;
	}
	public void setShowDate(String showDate) {
		this.showDate = showDate;
	}
	@NotDbField
	public int getIsLove() {
		return isLove;
	}
	public void setIsLove(int isLove) {
		this.isLove = isLove;
	}
	public String getMinorImage() {
		return minorImage;
	}
	public void setMinorImage(String minorImage) {
		this.minorImage = minorImage;
	}
	public int getIndexStatus() {
		return indexStatus;
	}
	public void setIndexStatus(int indexStatus) {
		this.indexStatus = indexStatus;
	}
	public int getFindStatus() {
		return findStatus;
	}
	public void setFindStatus(int findStatus) {
		this.findStatus = findStatus;
	}
	public int getRecommendStatus() {
		return recommendStatus;
	}
	public void setRecommendStatus(int recommendStatus) {
		this.recommendStatus = recommendStatus;
	}
	public String getDetailsPosition() {
		return detailsPosition;
	}
	public void setDetailsPosition(String detailsPosition) {
		this.detailsPosition = detailsPosition;
	}
	public String getProductPosition() {
		return productPosition;
	}
	public void setProductPosition(String productPosition) {
		this.productPosition = productPosition;
	}
	public Long getStartTime() {
		return startTime;
	}
	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}
	public int getBannerStatus() {
		return bannerStatus;
	}
	public void setBannerStatus(int bannerStatus) {
		this.bannerStatus = bannerStatus;
	}
	@NotDbField
	public long getRealClickCount() {
		return realClickCount;
	}
	public void setRealClickCount(long realClickCount) {
		this.realClickCount = realClickCount;
	}

	
}
