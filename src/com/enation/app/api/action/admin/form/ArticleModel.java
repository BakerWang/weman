package com.enation.app.api.action.admin.form;

import java.util.List;
import java.util.Map;

public class ArticleModel {

	
	private int id;
	private String content;
	private String orImage;
	private String image;
	private String categoryIds;//分类ids  以，隔开
	private String categoryName;//分类名字   以，隔开
	private String categoryImage;//分类图片 以，隔开
	private String brandIds;//品牌id  以，隔开
	private String brandName;//品牌名字 以，隔开
	private String brandLogo;//品牌logo 以，隔开
	private int viewCount;
	private int loveCount;
	private int commentCount;
	private int status;//1是通过的  0 是未通过的  -1 删除的
	private Long createTime;
	private String tagStr;
	
	private String userName;
	private String userPhoto;
	private Long userAge;
	private int userWeight;
	private int userHeight;
	private int userId;
	
	private String isFriend = null;//访问者是不是与发文者是朋友关系  null不是  '1'是
	
	private List<Map<String,Object>> loveUserList;//赞的列表
	
	
	public String getCategoryIds() {
		return categoryIds;
	}
	public void setCategoryIds(String categoryIds) {
		this.categoryIds = categoryIds;
	}
	public String getBrandIds() {
		return brandIds;
	}
	public void setBrandIds(String brandIds) {
		this.brandIds = brandIds;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public int getViewCount() {
		return viewCount;
	}
	public void setViewCount(int viewCount) {
		this.viewCount = viewCount;
	}
	public int getLoveCount() {
		return loveCount;
	}
	public void setLoveCount(int loveCount) {
		this.loveCount = loveCount;
	}
	public int getCommentCount() {
		return commentCount;
	}
	public void setCommentCount(int commentCount) {
		this.commentCount = commentCount;
	}
	public Long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getTagStr() {
		return tagStr;
	}
	public void setTagStr(String tagStr) {
		this.tagStr = tagStr;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getOrImage() {
		return orImage;
	}
	public void setOrImage(String orImage) {
		this.orImage = orImage;
	}
	public String getUserPhoto() {
		return userPhoto;
	}
	public void setUserPhoto(String userPhoto) {
		this.userPhoto = userPhoto;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getCategoryImage() {
		return categoryImage;
	}
	public void setCategoryImage(String categoryImage) {
		this.categoryImage = categoryImage;
	}
	public String getBrandLogo() {
		return brandLogo;
	}
	public void setBrandLogo(String brandLogo) {
		this.brandLogo = brandLogo;
	}
	public List<Map<String, Object>> getLoveUserList() {
		return loveUserList;
	}
	public void setLoveUserList(List<Map<String, Object>> loveUserList) {
		this.loveUserList = loveUserList;
	}
	public String getIsFriend() {
		return isFriend;
	}
	public void setIsFriend(String isFriend) {
		this.isFriend = isFriend;
	}
	public Long getUserAge() {
		return userAge;
	}
	public void setUserAge(Long userAge) {
		this.userAge = userAge;
	}
	public int getUserWeight() {
		return userWeight;
	}
	public void setUserWeight(int userWeight) {
		this.userWeight = userWeight;
	}
	public int getUserHeight() {
		return userHeight;
	}
	public void setUserHeight(int userHeight) {
		this.userHeight = userHeight;
	}
	
}
