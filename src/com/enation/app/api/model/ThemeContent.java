package com.enation.app.api.model;

public class ThemeContent {

	private int id;
	private int theme_id;//主题id
	private int postion;//位置
	private String type;//text  image   product   video
	private String content;
	private String fontSize;
	private String center;
	private String image;
	private int imagewidth = 120;
	private int imageheight = 175;
	private String status;
	private Long createtime;
	private int goods_id;
	
	private String productName;
	private Double productPrice = 100.0;
	private Double productMkPrice = 100.0;
	private String productOrigin = "taobao";
	private String productBrief = "";
	private String intro;//商品的介绍
	private String productImage;
	private String productCategoryImage;//商品分类的图片
	private String productBrandName;//商品品牌的名字
	private int isIndexShow = -1;//主题里面的商品是否显示 -1 否  1 显示
	private String url;//购买地址
	
	private int isCollect = 0;//如果是商品  是否收藏 0 是没有收藏
	
	
	
	
	
	
	public int getIsIndexShow() {
		return isIndexShow;
	}
	public void setIsIndexShow(int isIndexShow) {
		this.isIndexShow = isIndexShow;
	}
	public String getProductBrief() {
		return productBrief;
	}
	public void setProductBrief(String productBrief) {
		this.productBrief = productBrief;
	}
	public String getProductOrigin() {
		return productOrigin;
	}
	public void setProductOrigin(String productOrigin) {
		this.productOrigin = productOrigin;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public Double getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(Double productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductImage() {
		return productImage;
	}
	public void setProductImage(String productImage) {
		this.productImage = productImage;
	}
	public String getProductCategoryImage() {
		return productCategoryImage;
	}
	public void setProductCategoryImage(String productCategoryImage) {
		this.productCategoryImage = productCategoryImage;
	}
	public String getProductBrandName() {
		return productBrandName;
	}
	public void setProductBrandName(String productBrandName) {
		this.productBrandName = productBrandName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getTheme_id() {
		return theme_id;
	}
	public void setTheme_id(int theme_id) {
		this.theme_id = theme_id;
	}
	public int getPostion() {
		return postion;
	}
	public void setPostion(int postion) {
		this.postion = postion;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFontSize() {
		return fontSize;
	}
	public void setFontSize(String fontSize) {
		this.fontSize = fontSize;
	}
	public String getCenter() {
		return center;
	}
	public void setCenter(String center) {
		this.center = center;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getImagewidth() {
		return imagewidth;
	}
	public void setImagewidth(int imagewidth) {
		this.imagewidth = imagewidth;
	}
	public int getImageheight() {
		return imageheight;
	}
	public void setImageheight(int imageheight) {
		this.imageheight = imageheight;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Long createtime) {
		this.createtime = createtime;
	}
	public int getGoods_id() {
		return goods_id;
	}
	public void setGoods_id(int goods_id) {
		this.goods_id = goods_id;
	}
	public int getIsCollect() {
		return isCollect;
	}
	public void setIsCollect(int isCollect) {
		this.isCollect = isCollect;
	}
	public Double getProductMkPrice() {
		return productMkPrice;
	}
	public void setProductMkPrice(Double productMkPrice) {
		this.productMkPrice = productMkPrice;
	}
	public String getIntro() {
		return intro;
	}
	public void setIntro(String intro) {
		this.intro = intro;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	
	
}
