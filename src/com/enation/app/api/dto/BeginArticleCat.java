package com.enation.app.api.dto;

import java.util.List;

public class BeginArticleCat {

	private int catId;//分类id
	private String catName;//分类的名字
	private String isParent;//是总类不  0是yes  
	
	private List<BeginArticleType> articleType;

	public int getCatId() {
		return catId;
	}

	public void setCatId(int catId) {
		this.catId = catId;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getIsParent() {
		return isParent;
	}

	public void setIsParent(String isParent) {
		this.isParent = isParent;
	}

	public List<BeginArticleType> getArticleType() {
		return articleType;
	}

	public void setArticleType(List<BeginArticleType> articleType) {
		this.articleType = articleType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + catId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BeginArticleCat other = (BeginArticleCat) obj;
		if (catId != other.catId)
			return false;
		return true;
	}
	
	
	
	
}
