package com.enation.app.b2b2c.component.plugin.cart;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.plugin.cart.ICartAddEvent;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
@Component
public class StoreCartAddPlugin extends AutoRegisterPlugin implements ICartAddEvent{
	private IDaoSupport daoSupport;
	@Override
	public void add(Cart cart) {
		
	}

	@Override
	public void afterAdd(Cart cart) {
		//购物车添加店铺Id
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String store_id= request.getParameter("store_id");
		daoSupport.execute("update es_cart set store_id=? where cart_id=?", store_id,cart.getCart_id());
	}

	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
}
