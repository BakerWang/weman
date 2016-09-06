package com.enation.app.api.model;


public class PhoneBanner {

	
	private Long id;
	private String image;
	private String category;
	private String type;
	private String title;
	private String details;
	private Long start_time;
	private Long end_time;
	private String status;
	private Long create_time;
	private int click_count;
	
	public enum Type{
		Type_product("商品",1),
		Type_social("社交",2),
		Type_persion("个人主页",3),
		Type_Web("页面",4);
		
		private String name;
		private int type;
		
		private Type(String name,int type){
			this.name = name;
			this.type = type;
		}
		
		/**
		 * 根据序号获取枚举对象
		 * @param type
		 * @return
		 */
		public static Type getType(int type){
			for(Type t:Type.values()){
				if(t.type == type){
					return t;
				}
			}
			return Type_product;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public int getType() {
			return type;
		}
		public void setType(int type) {
			this.type = type;
		}
		
	}
	
	
	public int getClick_count() {
		return click_count;
	}
	public void setClick_count(int click_count) {
		this.click_count = click_count;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public Long getStart_time() {
		return start_time;
	}
	public void setStart_time(Long start_time) {
		this.start_time = start_time;
	}
	public Long getEnd_time() {
		return end_time;
	}
	public void setEnd_time(Long end_time) {
		this.end_time = end_time;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	
	
	
	
}
