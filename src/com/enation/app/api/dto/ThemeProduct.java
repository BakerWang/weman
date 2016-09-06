package com.enation.app.api.dto;

import java.util.List;

import com.enation.app.api.model.Theme;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Goods;

public class ThemeProduct {

	private Theme theme;
	private List<Goods> goodList;
	private List<Goods> detailsGoodList;
	private List<Member> memberList;
	
	
	public Theme getTheme() {
		return theme;
	}
	public void setTheme(Theme theme) {
		this.theme = theme;
	}
	public List<Goods> getGoodList() {
		return goodList;
	}
	public void setGoodList(List<Goods> goodList) {
		this.goodList = goodList;
	}
	public List<Member> getMemberList() {
		return memberList;
	}
	public void setMemberList(List<Member> memberList) {
		this.memberList = memberList;
	}
	public List<Goods> getDetailsGoodList() {
		return detailsGoodList;
	}
	public void setDetailsGoodList(List<Goods> detailsGoodList) {
		this.detailsGoodList = detailsGoodList;
	}
	
	
}
