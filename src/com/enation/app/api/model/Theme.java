package com.enation.app.api.model;

import java.util.List;
import java.util.Map;

import com.enation.framework.database.NotDbField;

public class Theme {

	private int id;
	private String title;
	private String image;
	private String minorImage;//主题副图
	private String details;
	private String showDate;
	private int love_count;
	private int product_count;
	private String status;//1是正常  -1是删除
	private int theme_tag_id;
	private Long create_time;
	private Map<Integer,Integer> themetagList;
	private String tagsImage;//主题tag的图片列表
	private List<ThemeContent> themeContent;
	
	private int isLove = 0;//是否收藏
	
	
	
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
}