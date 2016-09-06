package com.enation.eop.sdk.tag;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.SimpleTagSupport;

public class DateFormatTag extends SimpleTagSupport{

	private String format;
	private Long time;
	
	@Override
	public void doTag() throws JspException, IOException {
		if(time!=null){
			System.out.println(time);
			String str = new SimpleDateFormat(format).format(new Date(time));
			super.getJspContext().getOut().write(str);
		}
	}
	public static void main(String[] args) {
		Long ll = 1459844913639L;
		System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(ll)));
	}
	
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}

	public Long getTime() {
		return time;
	}

	public void setTime(Long time) {
		this.time = time;
	}
	
}
