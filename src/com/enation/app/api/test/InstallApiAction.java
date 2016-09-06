package com.enation.app.api.test;

import org.junit.Before;
import org.junit.Test;

import com.enation.app.api.ApiApp;
import com.enation.framework.test.SpringTestSupport;

public class InstallApiAction extends SpringTestSupport{

	private ApiApp api;
	
	@Before
	public void mock(){
		api = this.getBean("apiApp");
	}
	@Test
	public void test(){
		System.out.println("........");
		api.install();
	}
}
