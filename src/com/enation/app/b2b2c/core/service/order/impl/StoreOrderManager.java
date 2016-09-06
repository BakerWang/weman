package com.enation.app.b2b2c.core.service.order.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.member.StoreMember;
import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.service.cart.IStoreCartManager;
import com.enation.app.b2b2c.core.service.goods.IStoreGoodsManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.DlyType;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IPaymentManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
import com.enation.framework.util.StringUtil;
@Component
public class StoreOrderManager extends BaseSupport implements IStoreOrderManager{
	private IStoreCartManager storeCartManager;
	private IDlyTypeManager dlyTypeManager;
	private IPaymentManager paymentManager;
	private OrderPluginBundle orderPluginBundle;
	private IPromotionManager promotionManager;
	private IStoreGoodsManager storeGoodsManager;
	private IStoreMemberManager storeMemberManager;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#storeOrderList(java.lang.Integer, java.lang.Integer, java.util.Map)
	 */
	@Override
	public Page storeOrderList(Integer pageNo, Integer pageSize,Integer storeid,Map map) {
		String order_state=String.valueOf(map.get("order_state"));
		String keyword=String.valueOf(map.get("keyword"));
		String buyerName=String.valueOf(map.get("buyerName"));
		String startTime=String.valueOf(map.get("startTime"));
		String endTime=String.valueOf(map.get("endTime"));
		
		StringBuffer sql =new StringBuffer("select * from es_order o where store_id =" + storeid + " and disabled=0");
		if(!StringUtil.isEmpty(order_state)&&!order_state.equals("all")){
			if(order_state.equals("wait_ship") ){ //对待发货的处理
				sql.append(" and ( ( payment_type!='cod' and payment_id!=8  and  status="+OrderStatus.ORDER_PAY_CONFIRM +") ");//非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  status="+OrderStatus.ORDER_NOT_PAY +")) ");//货到付款的，新订单（已确认的）就可以发货
			}else if(order_state.equals("wait_pay") ){
				sql.append(" and ( ( payment_type!='cod' and  status="+OrderStatus.ORDER_NOT_PAY +") ");//非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_id=8 and status!="+OrderStatus.ORDER_NOT_PAY+"  and  pay_status!="+OrderStatus.PAY_CONFIRM +")" ); 
				sql.append(" or ( payment_type='cod' and  (status="+OrderStatus.ORDER_SHIP +" or status="+OrderStatus.ORDER_ROG+" )  ) )");//货到付款的要发货或收货后才能结算
			
			}else if(order_state.equals("wait_rog") ){ 
				sql.append(" and status="+OrderStatus.ORDER_SHIP  ); 
			}else{
				sql.append(" and status="+order_state);
			}
		}
		if(!StringUtil.isEmpty(keyword)&&!keyword.equals("null")){
			sql.append(" AND o.sn like '%" + keyword + "%'");
		}
		if(!StringUtil.isEmpty(buyerName)&&!buyerName.equals("null")){
			sql.append(" AND o.ship_name like '%"+buyerName+"%'");
		}
		if(!StringUtil.isEmpty(startTime)&&!startTime.equals("null")){
			sql.append(" AND o.create_time >"+DateUtil.getDateline(startTime));
		}
		if(!StringUtil.isEmpty(endTime)&&!endTime.equals("null")){
			sql.append(" AND o.create_time <"+DateUtil.getDateline(endTime));
		}
		sql.append (" order by o.create_time desc");
		//System.out.println(sql.toString());
		Page rpage = this.daoSupport.queryForPage(sql.toString(),pageNo, pageSize, Order.class);
		 
		return rpage;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#storeOrderList(java.lang.Integer)
	 */
	@Override
	public List storeOrderList(Integer parent_id) {
		StringBuffer sql=new StringBuffer("SELECT * from es_order WHERE  disabled=0 AND parent_id="+parent_id);
		return this.daoSupport.queryForList(sql.toString(), StoreOrder.class);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#get(java.lang.Integer)
	 */
	@Override
	public StoreOrder get(Integer orderId) {
		String sql = "select * from order where order_id=?";
		StoreOrder order = (StoreOrder) this.baseDaoSupport.queryForObject(sql,
				StoreOrder.class, orderId);
		return order;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#saveShipInfo(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean saveShipInfo(String remark, String ship_day,
			String ship_name, String ship_tel, String ship_mobile,
			String ship_zip, int orderid) {
		Order order = this.get(orderid);
		try {
			if(ship_day !=null && !StringUtil.isEmpty(ship_day)&&!ship_day.equals(order.getShip_day())){
				this.baseDaoSupport.execute("update order set ship_day=?  where order_id=?", ship_day,	orderid);
			}
			if(remark !=null && !StringUtil.isEmpty(remark)&&!remark.equals("undefined")&&!remark.equals(order.getRemark())){
				this.baseDaoSupport.execute("update order set remark= ?  where order_id=?",	remark,orderid);
			}
			if(ship_name !=null && !StringUtil.isEmpty(ship_name)&&!ship_name.equals(order.getShip_name())){
				this.baseDaoSupport.execute("update order set ship_name=?  where order_id=?", ship_name,	orderid);
			}
			if(ship_tel !=null && !StringUtil.isEmpty(ship_tel)&&!ship_tel.equals(order.getShip_tel())){
				this.baseDaoSupport.execute("update order set ship_tel=?  where order_id=?", ship_tel,	orderid);
			}
			if(ship_mobile !=null && !StringUtil.isEmpty(ship_mobile)&&!ship_mobile.equals(order.getShip_mobile())){
				this.baseDaoSupport.execute("update order set ship_mobile=?  where order_id=?", ship_mobile,	orderid);
			}
			if(ship_zip !=null && !StringUtil.isEmpty(ship_zip)&&!ship_zip.equals(order.getShip_zip())){
				this.baseDaoSupport.execute("update order set ship_zip=?  where order_id=?", ship_zip,	orderid);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#pageOrders(int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page pageOrders(int pageNo, int pageSize, String status,
			String keyword) {
		StoreMember member = storeMemberManager.getStoreMember();
		
		StringBuffer sql =new StringBuffer("select * from " + this.getTableName("order") + " where member_id = '" + member.getMember_id() + "' and disabled=0");
		if(!StringUtil.isEmpty(status)){
			int statusNumber = -999;
			statusNumber = StringUtil.toInt(status);
			//等待付款的订单 按付款状态查询
			if(statusNumber==0){
				sql.append(" AND status!="+OrderStatus.ORDER_CANCELLATION+" AND pay_status="+OrderStatus.PAY_NO);
			}else{
				sql.append(" AND status='" + statusNumber + "'");
			}
		}
		if(!StringUtil.isEmpty(keyword)){
			sql.append(" AND order_id in (SELECT i.order_id FROM " + this.getTableName("order_items") + " i LEFT JOIN "+this.getTableName("order")+" o ON i.order_id=o.order_id WHERE o.member_id='"+member.getMember_id()+"' AND i.name like '%" + keyword + "%')");
		}
		sql.append(" AND NOT ISNULL(parent_id) order by create_time desc");
		Page rpage = this.daoSupport.queryForPage(sql.toString(),pageNo, pageSize, Order.class);
		return rpage;
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#getStoreOrderNum(int)
	 */
	@Override
	public int getStoreOrderNum(int struts) {
		StoreMember member = storeMemberManager.getStoreMember();
		String sql ="select count(order_id) from es_order o where o.store_id ="+member.getStore_id()+" and o.disabled=0";
		if(struts!=-999){
			sql=sql+" AND o.status="+struts;
		}
		return this.daoSupport.queryForInt(sql);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#get(java.lang.String)
	 */
	@Override
	public StoreOrder get(String ordersn) {
		String sql  ="select * from es_order where sn='"+ordersn+"'";
		StoreOrder order  = (StoreOrder)this.baseDaoSupport.queryForObject(sql, StoreOrder.class);
		return order;
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.order.IStoreOrderManager#listOrder(java.util.Map, int, int, java.lang.String, java.lang.String)
	 */
	@Override
	public Page listOrder(Map map, int page, int pageSize, String other,String order) {
		
		String sql = createTempSql(map, other,order);
		Page webPage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webPage;
	}
	private String  createTempSql(Map map,String other,String order){
		
		Integer stype = (Integer) map.get("stype");
		String keyword = (String) map.get("keyword");
		String orderstate =  (String) map.get("order_state");//订单状态
		String start_time = (String) map.get("start_time");
		String end_time = (String) map.get("end_time");
		Integer status = (Integer) map.get("status");
		String sn = (String) map.get("sn");
		String ship_name = (String) map.get("ship_name");
		Integer paystatus = (Integer) map.get("paystatus");
		Integer shipstatus = (Integer) map.get("shipstatus");
		Integer shipping_type = (Integer) map.get("shipping_type");
		Integer payment_id = (Integer) map.get("payment_id");
		Integer depotid = (Integer) map.get("depotid");
		String complete = (String) map.get("complete");
		String store_name= (String) map.get("store_name");
		Integer store_id=(Integer)map.get("store_id");
		StringBuffer sql =new StringBuffer();
		sql.append("select o.*,s.store_name as store_name from order o inner join store s on o.store_id=s.store_id  where o.disabled=0 and NOT ISNULL(parent_id)");
		
		if(stype!=null && keyword!=null){			
			if(stype==0){
				sql.append(" and (sn like '%"+keyword+"%'");
				sql.append(" or ship_name like '%"+keyword+"%')");
			}
		}
		
		if(status!=null){
			sql.append("and status="+status);
		}
		
		if(sn!=null && !StringUtil.isEmpty(sn)){
			sql.append(" and sn like '%"+sn+"%'");
		}
		
		if(ship_name!=null && !StringUtil.isEmpty(ship_name)){
			sql.append(" and ship_name like '"+ship_name+"'");
		}
		
		if(paystatus!=null){
			sql.append(" and pay_status="+paystatus);
		}
		
		if(shipstatus!=null){
			sql.append(" and ship_status="+shipstatus);
		}
		
		if(shipping_type!=null){
			sql.append(" and shipping_id="+shipping_type);
		}
		
		if(payment_id!=null){
			sql.append(" and payment_id="+payment_id);
		}
		
		if (depotid!=null && depotid > 0) {
			sql.append(" and depotid=" + depotid);
		}
		
		if(start_time!=null&&!StringUtil.isEmpty(start_time)){			
			long stime = com.enation.framework.util.DateUtil.getDateline(start_time+" 00:00:00");
			sql.append(" and o.create_time>"+stime);
		}
		if(end_time!=null&&!StringUtil.isEmpty(end_time)){			
			long etime = com.enation.framework.util.DateUtil.getDateline(end_time +" 23:59:59");
			sql.append(" and o.create_time<"+etime);
		}
		if( !StringUtil.isEmpty(orderstate)){
			if(orderstate.equals("wait_ship") ){ //对待发货的处理
				sql.append(" and ( ( payment_type!='cod' and payment_id!=8  and  status="+OrderStatus.ORDER_PAY_CONFIRM +") ");//非货到付款的，要已结算才能发货
				sql.append(" or ( payment_type='cod' and  status="+OrderStatus.ORDER_NOT_PAY +")) ");//货到付款的，新订单（已确认的）就可以发货
			}else if(orderstate.equals("wait_pay") ){
				sql.append(" and ( ( payment_type!='cod' and  status="+OrderStatus.ORDER_NOT_PAY +") ");//非货到付款的，未付款状态的可以结算
				sql.append(" or ( payment_id=8 and status!="+OrderStatus.ORDER_NOT_PAY+"  and  pay_status!="+OrderStatus.PAY_CONFIRM +")" ); 
				sql.append(" or ( payment_type='cod' and  (status="+OrderStatus.ORDER_SHIP +" or status="+OrderStatus.ORDER_ROG+" )  ) )");//货到付款的要发货或收货后才能结算
			
			}else if(orderstate.equals("wait_rog") ){ 
				sql.append(" and status="+OrderStatus.ORDER_SHIP  ); 
			}else{
				sql.append(" and status="+orderstate);
			}
		
		}
		
		if(!StringUtil.isEmpty(complete)){
			sql.append(" and status="+OrderStatus.ORDER_COMPLETE);
		}
		if(!StringUtil.isEmpty(store_name)){
			sql.append(" and o.store_id in(select store_id from es_store where store_name like %'"+store_name+"'%)");
		}
		if(store_id!=null){
			sql.append(" and o.store_id="+store_id);
		}
		sql.append(" ORDER BY "+other+" "+order);
		
		//System.out.println(sql.toString());
		return sql.toString();
	}
	@Override
	public Map getStatusJson() {
		Map orderStatus = new  HashMap();
		
		orderStatus.put(""+OrderStatus.ORDER_NOT_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_PAY));
		//orderStatus.put(""+OrderStatus.ORDER_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY));
		orderStatus.put(""+OrderStatus.ORDER_NOT_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_NOT_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_PAY_CONFIRM, OrderStatus.getOrderStatusText(OrderStatus.ORDER_PAY_CONFIRM));
		orderStatus.put(""+OrderStatus.ORDER_ALLOCATION_YES, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ALLOCATION_YES));
		orderStatus.put(""+OrderStatus.ORDER_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_ROG, OrderStatus.getOrderStatusText(OrderStatus.ORDER_ROG));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_SHIP, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_SHIP));
		orderStatus.put(""+OrderStatus.ORDER_COMPLETE, OrderStatus.getOrderStatusText(OrderStatus.ORDER_COMPLETE));
		orderStatus.put(""+OrderStatus.ORDER_CANCEL_PAY, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCEL_PAY));
		orderStatus.put(""+OrderStatus.ORDER_CANCELLATION, OrderStatus.getOrderStatusText(OrderStatus.ORDER_CANCELLATION));

		return orderStatus;
	}
	@Override
	public Map getpPayStatusJson() {
		Map pmap = new HashMap();
		pmap.put(""+OrderStatus.PAY_NO, OrderStatus.getPayStatusText(OrderStatus.PAY_NO));
	//	pmap.put(""+OrderStatus.PAY_YES, OrderStatus.getPayStatusText(OrderStatus.PAY_YES));
		pmap.put(""+OrderStatus.PAY_CONFIRM, OrderStatus.getPayStatusText(OrderStatus.PAY_CONFIRM));
		pmap.put(""+OrderStatus.PAY_CANCEL, OrderStatus.getPayStatusText(OrderStatus.PAY_CANCEL));
		pmap.put(""+OrderStatus.PAY_PARTIAL_PAYED, OrderStatus.getPayStatusText(OrderStatus.PAY_PARTIAL_PAYED));

		return pmap;
	}
	@Override
	public Map getShipJson() {
		Map map = new HashMap();
		map.put(""+OrderStatus.SHIP_ALLOCATION_NO, OrderStatus.getShipStatusText(OrderStatus.SHIP_ALLOCATION_NO));
		map.put(""+OrderStatus.SHIP_ALLOCATION_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_ALLOCATION_YES));
		map.put(""+OrderStatus.SHIP_NO, OrderStatus.getShipStatusText(OrderStatus.SHIP_NO));
		map.put(""+OrderStatus.SHIP_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_YES));
		map.put(""+OrderStatus.SHIP_CANCEL, OrderStatus.getShipStatusText(OrderStatus.SHIP_CANCEL));
		map.put(""+OrderStatus.SHIP_PARTIAL_SHIPED, OrderStatus.getShipStatusText(OrderStatus.SHIP_PARTIAL_SHIPED));
		map.put(""+OrderStatus.SHIP_YES, OrderStatus.getShipStatusText(OrderStatus.SHIP_YES));
		map.put(""+OrderStatus.SHIP_CANCEL, OrderStatus.getShipStatusText(OrderStatus.SHIP_CANCEL));
		map.put(""+OrderStatus.SHIP_CHANED, OrderStatus.getShipStatusText(OrderStatus.SHIP_CHANED));
		map.put(""+OrderStatus.SHIP_ROG, OrderStatus.getShipStatusText(OrderStatus.SHIP_ROG));
		return map;
	}
	
	//set  get 
	
	public Integer orderStatusNum(Integer status) {
		StoreMember member = storeMemberManager.getStoreMember();
		String sql = "select count(0) from es_order where status =? and member_id=? and parent_id is not null";
		return this.baseDaoSupport.queryForInt(sql, status,member.getMember_id());
	}
	
	@Override
	public Integer getStoreGoodsNum(int store_id) {
		String sql = "select count(0) from goods where store_id=?";
		return this.baseDaoSupport.queryForInt(sql, store_id);
	}
	
	
	

	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IPaymentManager getPaymentManager() {
		return paymentManager;
	}

	public void setPaymentManager(IPaymentManager paymentManager) {
		this.paymentManager = paymentManager;
	}

	public OrderPluginBundle getOrderPluginBundle() {
		return orderPluginBundle;
	}

	public void setOrderPluginBundle(OrderPluginBundle orderPluginBundle) {
		this.orderPluginBundle = orderPluginBundle;
	}

	public IPromotionManager getPromotionManager() {
		return promotionManager;
	}

	public void setPromotionManager(IPromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}

	public IStoreGoodsManager getStoreGoodsManager() {
		return storeGoodsManager;
	}

	public void setStoreGoodsManager(IStoreGoodsManager storeGoodsManager) {
		this.storeGoodsManager = storeGoodsManager;
	}

	public IStoreCartManager getStoreCartManager() {
		return storeCartManager;
	}

	public void setStoreCartManager(IStoreCartManager storeCartManager) {
		this.storeCartManager = storeCartManager;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	@Override
	public void saveShipNo(Integer[] order_id, Integer logi_id,String logi_name, String shipNo) {
		Map map=new HashMap();
		map.put("ship_no", shipNo);
		map.put("logi_id", logi_id);
		map.put("logi_name", logi_name);
		this.daoSupport.update("es_order", map, "order_id="+order_id[0]);
	}
}

