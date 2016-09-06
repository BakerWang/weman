package com.enation.app.b2b2c.component;

import org.springframework.stereotype.Component;

import com.enation.app.base.core.service.dbsolution.DBSolutionFactory;
import com.enation.framework.component.IComponent;
@Component
/**
 * b2b2c组件
 */
public class B2b2cComponent implements IComponent{

	@Override
	public void install() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/b2b2c_install.xml", "es_");
	}

	@Override
	public void unInstall() {
		DBSolutionFactory.dbImport("file:com/enation/app/b2b2c/component/b2b2c_uninstall.xml", "es_");
	}

}
