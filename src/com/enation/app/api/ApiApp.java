package com.enation.app.api;

import org.springframework.stereotype.Component;

import com.enation.eop.resource.model.EopSite;
import com.enation.eop.sdk.App;

@Component("apiApp")
public class ApiApp extends App {

	@Override
	public void install() {
		this.doInstall("file:com/enation/app/api/api.xml");
	}

	@Override
	public void sessionDestroyed(String sessionid, EopSite site) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		return "api应用";
	}

	@Override
	public String getId() {
		return "api";
	}

	@Override
	public String getNameSpace() {
		return "/api";
	}

}
