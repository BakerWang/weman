package com.enation.app.b2b2c.core.tag.cart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.model.goods.StoreGoods;
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
 * @author LiFenLong
 *
 */
@Component
public class StoreCartGoodsTag extends BaseFreeMarkerTag{
	private IStoreCartManager storeCartManager;
	private IStoreDlyTypeManager storeDlyTypeManager;
	private IStoreMemberAddressManager storeMemberAddressManager;
	private IStoreGoodsManager storeGoodsManager;
	private IDlyTypeManager dlyTypeManager;
	private IStoreTemplateManager storeTemplateManager;
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	/**
	 * 返回购物车中的购物列表
	 * @param 无 
	 * @return 购物列表 类型List<CartItem>
	 * {@link storeGoodsList}
	 */
	protected Object exec(Map params) throws TemplateModelException {
		List<Map> storeGoodsList= new ArrayList<Map>();
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		String sessionid = request.getSession().getId();
		storeGoodsList=storeCartManager.storeListGoods(sessionid);
		
		Integer addrid = (Integer) params.get("addrid");
		Integer regionsid=0;
		if(addrid!=null && addrid!=0){
			regionsid =  this.storeMemberAddressManager.getRegionid(addrid);
		}
		
		for(Map map : storeGoodsList){
			//Double allweight=CurrencyUtil.mul(Double.valueOf(item.getWeight()), Double.valueOf(item.getNum()));
			Integer store_id=  (Integer) map.get("store_id");
			List list = (List) map.get("goodslist");
			Integer tempid = this.storeTemplateManager.getDefTempid(store_id);
			Integer type_id =0;
			if(tempid!=null){
				List<Map> dlyList = this.storeDlyTypeManager.getDlyTypeList(tempid);
				Map dlymap = dlyList.get(0);
				type_id = (Integer) dlymap.get("type_id");
			}
			OrderPrice orderPrice =  this.storeCartManager.countPrice(list, regionsid+"", type_id, false ,null);
			
			map.put("storeprice", orderPrice.getOrderPrice());
			map.put("originalPrice", orderPrice.getOriginalPrice());
			map.put("weight", orderPrice.getWeight());
			map.put("discountprice", orderPrice.getDiscountPrice());
		}
		return storeGoodsList;
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
