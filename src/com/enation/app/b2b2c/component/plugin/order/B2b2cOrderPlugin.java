package com.enation.app.b2b2c.component.plugin.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.StoreBonus;
import com.enation.app.b2b2c.core.model.cart.StoreCartItem;
import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.IStoreBonusManager;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.component.bonus.service.BonusSession;
import com.enation.app.shop.component.receipt.Receipt;
import com.enation.app.shop.component.receipt.service.IReceiptManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.model.OrderLog;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.plugin.cart.ICountPriceEvent;
import com.enation.app.shop.core.plugin.order.IAfterOrderCreateEvent;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IOrderFlowManager;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.StringUtil;
/**
 * @author LiFenLong
 *b2b2c订单插件
 */
@Component
public class B2b2cOrderPlugin extends AutoRegisterPlugin implements IAfterOrderCreateEvent,ICountPriceEvent{
	private IDaoSupport daoSupport;
	private IStoreCartManager storeCartManager;
	private ICartManager cartManager;
	private IDlyTypeManager dlyTypeManager;
	private IOrderFlowManager orderFlowManager;
	private IStoreMemberManager storeMemberManager;
	private IReceiptManager receiptManager;
	private IStoreBonusManager storeBonusManager;
	private IStoreManager storeManager;
	private  final String discount_key ="bonusdiscount";
	
	@Override
	
	
	/***
	 * 主订单创建完成后添加子订单信息
	 */
	public void onAfterOrderCreate(Order order, List<CartItem> itemList,String sessionid) {

		StoreMember member = storeMemberManager.getStoreMember();
		//获取购物车列表
		List<Map> storeGoodsList=storeCartManager.storeListGoods(sessionid);
		HttpServletRequest request = ThreadContextHolder.getHttpRequest();
		StoreOrder store_order = new StoreOrder();
		Double goodsPrice=0.0; //商品价格，经过优惠过的
		Double orderprice=0.0;//订单总价，优惠过的，包含商品价格和配置费用
		Double shippingPrice=0.0; //配送费用
		Double needPayMoney=0.0; //需要支付的金额
		try {
			BeanUtils.copyProperties(store_order,order);
		} catch (Exception e) {
			e.printStackTrace();
		}
		int num=0;
		//获取配送方式数组
		String[] shippingIds=request.getParameterValues("shippingId");
		//获取优惠劵数组
		String[] bonusid=request.getParameterValues("bonusid");
			
		for (Map map : storeGoodsList) {
			int store_id = Integer.parseInt(map.get("store_id").toString());
			Integer shippingId = Integer.parseInt(shippingIds[num]);
			store_order.setBill_status(0);	//订单为未结算
			store_order.setStore_id(store_id);
			store_order.setParent_id(order.getOrder_id());
			//获取子订单的购物车货品列表以及计算子订单的订单价格
			List<StoreCartItem> list=(List) map.get("goodslist");
			
			Map storemap = new HashMap();
			storemap.put("bonusid", Integer.parseInt(bonusid[num]));
			storemap.put("storeid", store_id);
			
			/*************************计算价格，重量，积分*************/
			OrderPrice orderPrice=storeCartManager.countPrice(list,order.getRegionid()+"",shippingId,false,storemap);
			
			store_order.setGoods_amount( orderPrice.getGoodsPrice());
			store_order.setWeight(orderPrice.getWeight());		
			
			store_order.setDiscount(orderPrice.getDiscountPrice());
			store_order.setOrder_amount(orderPrice.getOrderPrice());
			store_order.setProtect_price(orderPrice.getProtectPrice());
			store_order.setShipping_amount(orderPrice.getShippingPrice());
			store_order.setGainedpoint(orderPrice.getPoint());
			store_order.setNeed_pay_money(orderPrice.getNeedPayMoney());
			
			shippingPrice+=orderPrice.getShippingPrice();
			needPayMoney+=orderPrice.getNeedPayMoney();
			orderprice+=orderPrice.getOrderPrice();
			goodsPrice+=orderPrice.getGoodsPrice();
			
			String shipName=null;
			if(shippingId!=null && shippingId!=0){
				shipName= dlyTypeManager.getDlyTypeById(shippingId).getName();
			}else{
				shipName = "";
			}
			store_order.setShipping_type(shipName);
			
			//添加子订单
			store_order.setSn(num==0?store_order.getSn()+num:store_order.getSn().substring(0, store_order.getSn().length()-1)+num);
			Store store=storeManager.getStore(store_id);
			store_order.setCommission((orderPrice.getOrderPrice()*store.getCommission())/100);
			daoSupport.insert("es_order", store_order);
			//添加子订单商品
			store_order.setOrder_id(this.daoSupport.getLastId("es_order"));
			this.saveGoodsItem(list,store_order.getOrder_id());
			
			/************ 写入订单日志 ************************/
			OrderLog log = new OrderLog();
			log.setMessage("订单创建");
			log.setOp_name(member.getName());
			log.setOrder_id(store_order.getOrder_id());
			this.addLog(log);
			//如果不为货到库款则自动确认订单
			if(!store_order.getIsCod()){
				orderFlowManager.confirmOrder(store_order.getOrder_id());
			}
			//保存发票信息
			saveReceipt(store_order);
			
			//修改为已使用,并增加已使用数量
			this.storeBonusManager.setBonusUsed(Integer.parseInt(bonusid[num]),member.getMember_id());
			
			num+=1;
		}
		this.updateOrderPrice(order.getOrder_id(), goodsPrice, orderprice, shippingPrice, needPayMoney);
	}
	/**
	 * 修改订单价格
	 */
	private void updateOrderPrice(Integer order_id,Double goodsPrice,Double orderprice,Double shippingPrice,Double needPayMoney){
		Map map=new HashMap();
		map.put("goods_amount", goodsPrice);
		map.put("order_amount", orderprice);
		map.put("shipping_amount", shippingPrice);
		map.put("need_pay_money", needPayMoney);
		this.daoSupport.update("es_order", map, "order_id="+order_id);
	}
	/***
	 * 保存订单项
	 * @param itemList
	 * @param order_id
	 */
	private void saveGoodsItem(List<StoreCartItem> itemList, Integer order_id) {
		for (int i = 0; i < itemList.size(); i++) {

			OrderItem orderItem = new OrderItem();

			CartItem cartItem = (CartItem) itemList.get(i);
			orderItem.setPrice(cartItem.getCoupPrice());
			orderItem.setName(cartItem.getName());
			orderItem.setNum(cartItem.getNum());

			orderItem.setGoods_id(cartItem.getGoods_id());
			orderItem.setShip_num(0);
			orderItem.setProduct_id(cartItem.getProduct_id());
			orderItem.setOrder_id(order_id);
			orderItem.setGainedpoint(cartItem.getPoint());
			orderItem.setAddon(cartItem.getAddon());
			
			//3.0新增的三个字段
			orderItem.setSn(cartItem.getSn());
			orderItem.setImage(cartItem.getImage_default());
			orderItem.setCat_id(cartItem.getCatid());
			
			orderItem.setUnit(cartItem.getUnit());
			this.daoSupport.insert("es_order_items", orderItem);
		}
	}
	/**
	 * 添加订单日志
	 * 
	 * @param log
	 */
	private void addLog(OrderLog log) {
		log.setOp_time(com.enation.framework.util.DateUtil.getDateline());
		this.daoSupport.insert("es_order_log", log);
	}
	/**
	 * 保存发票
	 * @param order 子订单
	 */
	private void saveReceipt(StoreOrder order){
		HttpServletRequest request  = ThreadContextHolder.getHttpRequest();
		
		String havereceipt = request.getParameter("receipt");
		if(StringUtil.isEmpty(havereceipt)) return ;
		
		
		//保存发票信息
		String appi=request.getParameter("receiptType");
		int invoice_app=0;
		if(!StringUtil.isEmpty(appi)){
			invoice_app = Integer.parseInt(appi);
		}
		if(invoice_app==1){
			String invoice_title = "个人";
			String invoice_content = request.getParameter("receiptContent");
			if(!StringUtil.isEmpty(invoice_content)){
				Receipt receipt= new Receipt();
				receipt.setOrder_id(order.getOrder_id());
				receipt.setTitle(invoice_title);
				receipt.setContent(invoice_content);
				this.receiptManager.add(receipt);
			}
		}else if(invoice_app==2){
			//单位
			String invoice_title = request.getParameter("receiptTitle");
			String invoice_content = request.getParameter("receiptContent");
			if(!StringUtil.isEmpty(invoice_title) && !StringUtil.isEmpty(invoice_content)){
				Receipt invoice= new Receipt();
				invoice.setOrder_id(order.getOrder_id());
				invoice.setTitle(invoice_title);
				invoice.setContent(invoice_content);
				this.receiptManager.add(invoice);
			}
		}
	}
	
	@Override
	public OrderPrice countPrice(OrderPrice orderprice,Map map) {
		if(map!=null && ((Integer)map.get("bonusid")).intValue()!=0){
			Integer bonusid = (Integer) map.get("bonusid");
			
			StoreBonus bonus = this.storeBonusManager.get(bonusid);
			Map<String,Object> disItems  = orderprice.getDiscountItem();
			
			//订单优惠项
			double moneyCount = bonus.getType_money();
			
			disItems.put(discount_key, moneyCount);//记录红包优惠金额
			
			//处理订单金额
			double need_pay_price =orderprice.getNeedPayMoney();
			need_pay_price= CurrencyUtil.sub(need_pay_price, moneyCount);
			
			orderprice.setDiscountPrice(CurrencyUtil.add(orderprice.getDiscountPrice(),moneyCount));//优惠总金额
			if(need_pay_price<0){
				orderprice.setNeedPayMoney(0.0);
			}else{
				orderprice.setNeedPayMoney(need_pay_price);
			}
		}
		
		return orderprice;
	}
	
	
	//set get 
	
	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}
	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}
	public ICartManager getCartManager() {
		return cartManager;
	}
	public void setCartManager(ICartManager cartManager) {
		this.cartManager = cartManager;
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}
	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}
	public IOrderFlowManager getOrderFlowManager() {
		return orderFlowManager;
	}
	public void setOrderFlowManager(IOrderFlowManager orderFlowManager) {
		this.orderFlowManager = orderFlowManager;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public IReceiptManager getReceiptManager() {
		return receiptManager;
	}
	public void setReceiptManager(IReceiptManager receiptManager) {
		this.receiptManager = receiptManager;
	}
	public IStoreBonusManager getStoreBonusManager() {
		return storeBonusManager;
	}
	public void setStoreBonusManager(IStoreBonusManager storeBonusManager) {
		this.storeBonusManager = storeBonusManager;
	}
	public IStoreManager getStoreManager() {
		return storeManager;
	}
	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
}
