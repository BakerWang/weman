package com.enation.app.api.model;

import java.util.List;

public class ThemeTag {

	private Long id;
	private Long category;
	private String name;
	private String image;
	private int status;
	private Long create_time;
	private List<ThemeTag> childrenThemeTag;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCategory() {
		return category;
	}
	public void setCategory(Long category) {
		this.category = category;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getCreate_time() {
		return create_time;
	}
	public void setCreate_time(Long create_time) {
		this.create_time = create_time;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public List<ThemeTag> getChildrenThemeTag() {
		return childrenThemeTag;
	}
	public void setChildrenThemeTag(List<ThemeTag> childrenThemeTag) {
		this.childrenThemeTag = childrenThemeTag;
	}
	
	
}
