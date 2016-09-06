package com.enation.app.b2b2c.core.tag.cart;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.service.IStoreDlyTypeManager;
import com.enation.app.b2b2c.core.service.IStoreMemberAddressManager;
import com.enation.app.b2b2c.core.service.IStoreTemplateManager;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.taglib.BaseFreeMarkerTag;
import com.enation.framework.util.CurrencyUtil;

import freemarker.template.TemplateModelException;

/**
 * 订单总价计算标签
 * @author xulipeng
 *
 */
@Component
public class CartOrderTotlePriceTag extends BaseFreeMarkerTag {

	private IStoreCartManager storeCartManager;
	private IStoreDlyTypeManager storeDlyTypeManager;
	private IStoreMemberAddressManager storeMemberAddressManager;
	private IStoreGoodsManager storeGoodsManager;
	private IDlyTypeManager dlyTypeManager;
	private IStoreTemplateManager storeTemplateManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	protected Object exec(Map params) throws TemplateModelException {
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		Integer regionid = (Integer) params.get("regionId");//地区id
		
		//获取购物车中店铺的集合
		List<Map> storeGoodsList= new ArrayList<Map>();
		storeGoodsList=storeCartManager.storeListGoods(sessionid);
		
		OrderPrice orderPrice = null;
		
		Double totleprice=0.0d;
		Double goodsprice=0.0d;
		Double shippingprice=0.0d;
		
		for(Map map : storeGoodsList){
			
			Integer store_id=  (Integer) map.get("store_id");
			List list = (List) map.get("goodslist");
			Integer tempid = this.storeTemplateManager.getDefTempid(store_id);
			Integer type_id =0;
			if(tempid!=null){
				List<Map> dlyList = this.storeDlyTypeManager.getDlyTypeList(tempid);
				Map dlymap = dlyList.get(0);
				type_id = (Integer) dlymap.get("type_id");
			}
			orderPrice =  this.storeCartManager.countPrice(list, regionid+"", type_id, false,null);
			
			totleprice = CurrencyUtil.add(totleprice, orderPrice.getOrderPrice());
			shippingprice= CurrencyUtil.add(shippingprice, orderPrice.getShippingPrice());
			goodsprice = CurrencyUtil.add(goodsprice,orderPrice.getGoodsPrice());
			
			/*map.put("storeprice", orderPrice.getOrderPrice());
			map.put("originalPrice", orderPrice.getOriginalPrice());
			map.put("weight", orderPrice.getWeight());*/
		}
		
		orderPrice.setOrderPrice(totleprice);
		orderPrice.setShippingPrice(shippingprice);
		orderPrice.setGoodsPrice(goodsprice);
		orderPrice.setNeedPayMoney(totleprice);
		
		return orderPrice;
	}

	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}

	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}

	public IStoreDlyTypeManager getStoreDlyTypeManager() {
		return storeDlyTypeManager;
	}

	public void setStoreDlyTypeManager(IStoreDlyTypeManager storeDlyTypeManager) {
		this.storeDlyTypeManager = storeDlyTypeManager;
	}

	public IStoreMemberAddressManager getStoreMemberAddressManager() {
		return storeMemberAddressManager;
	}

	public void setStoreMemberAddressManager(
			IStoreMemberAddressManager storeMemberAddressManager) {
		this.storeMemberAddressManager = storeMemberAddressManager;
	}

	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}

	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}

	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IStoreTemplateManager getStoreTemplateManager() {
		return storeTemplateManager;
	}

	public void setStoreTemplateManager(IStoreTemplateManager storeTemplateManager) {
		this.storeTemplateManager = storeTemplateManager;
	}

}
