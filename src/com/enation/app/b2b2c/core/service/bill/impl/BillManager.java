package com.enation.app.b2b2c.core.service.bill.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.b2b2c.core.model.bill.Bill;
import com.enation.app.b2b2c.core.model.bill.BillDetail;
import com.enation.app.b2b2c.core.model.bill.BillStatusEnum;
import com.enation.app.b2b2c.core.model.store.Store;
import com.enation.app.b2b2c.core.service.bill.IBillManager;
import com.enation.app.b2b2c.core.service.store.IStoreManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.DateUtil;
@Component
public class BillManager implements IBillManager {
	private IDaoSupport daoSupport;
	private IStoreManager storeManager;
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#bill_list(java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page bill_list(Integer pageNo, Integer pageSize) {
		
		return this.daoSupport.queryForPage("SELECT * FROM es_bill order by bill_id desc", pageNo, pageSize);
	}

	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#bill_detail_list(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page bill_detail_list(Integer pageNo, Integer pageSize,Integer bill_id) {
		return this.daoSupport.queryForPage("SELECT * FROM es_bill_detail WHERE bill_id=? order by bill_id desc", pageNo, pageSize,bill_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#bill_detail_list(java.lang.Integer, java.lang.Integer, java.lang.Integer)
	 */
	@Override
	public Page store_bill_detail_list(Integer pageNo, Integer pageSize,Integer store_id) {
		return this.daoSupport.queryForPage("SELECT * FROM es_bill_detail WHERE store_id=? order by id desc", pageNo, pageSize,store_id);
	}
	/*
	 * (non-Javadoc)
	 * @see com.enation.app.b2b2c.core.service.bill.IBillManager#add_bill(com.enation.app.b2b2c.core.model.bill.Bill)
	 */
	@Override
	public void add_bill(Bill bill) {
		this.daoSupport.insert("es_bill", bill);
		bill.setBill_id(this.daoSupport.getLastId("es_bill"));
		this.editBill(bill);
	}
	/**
	 * 创建结算单
	 * 循环店铺表算出每家店铺的结算信息，创建出结算的详细单
	 * 然后进行相加算出此期的结算金额信息.
	 * @param bill 结算单
	 */
	public void editBill(Bill bill){
		Double pay_money=0.0;
		Double commi_price=0.0;
		Double return_price=0.0;
		Double returned_commi_price=0.0;
		Double price=0.0;
		//循环店铺列表。然后再用店铺Id去查询。
		List<Map> list=this.daoSupport.queryForList("select * from es_store  where disabled=?",1);
		
		Integer i=1;
		//结算详细单价格信息
		for (Map map:list) {
			//创建结算详细
			Store store=storeManager.getStore(Integer.parseInt(map.get("store_id").toString()));
			BillDetail billDetail=new BillDetail();
			billDetail.setStore_name(store.getStore_name());
			billDetail.setBill_id(bill.getBill_id());
			billDetail.setSn(i+"");
			billDetail.setStore_id(store.getStore_id());
			billDetail.setStatus(1);
			billDetail.setStart_time(bill.getStart_time());
			billDetail.setEnd_time(bill.getEnd_time());
			billDetail.setBill_time(DateUtil.getDateline());
			//创建结算详细单
			getBillDetail(billDetail);
			this.add_bill_detail(billDetail);
			//添加金额
			pay_money+=billDetail.getPrice();
			commi_price+=billDetail.getCommi_price();
			return_price+=billDetail.getReturned_price();
			returned_commi_price+=billDetail.getReturned_commi_price();
			price+=billDetail.getBill_price();
			i++;
		}
		bill.setCommi_price(commi_price);
		bill.setOrder_price(pay_money);
		bill.setPrice(price);
		bill.setReturned_commi_price(returned_commi_price);
		bill.setReturned_price(return_price);
		this.daoSupport.update("es_bill",bill, " bill_id="+bill.getBill_id());
	}
	/**
	 * 修改订单信息
	 * @param bill_sn 结算编号
	 * @param store_id 店铺Id
	 * @param end_time 结束时间
	 */
	private void update_order_info(String bill_sn,Integer store_id,Long end_time){
		this.daoSupport.execute("update es_order set bill_status=1,bill_sn=? where store_id=? and create_time<? and bill_status=0", bill_sn,store_id,end_time);
		this.daoSupport.execute("update es_sellback_list set bill_status=1, bill_sn=? where store_id=? and regtime<? and bill_status=0", bill_sn,store_id,end_time);
	}
	/**
	 * 添加结算详细单
	 * @param billDetail
	 */
	public void getBillDetail(BillDetail billDetail){
		Store store=storeManager.getStore(billDetail.getStore_id());
		
		Map store_order=this.daoSupport.queryForMap("select sum(need_pay_money) as pay_money,sum(commission) as commi_price from es_order where create_time<? and bill_status=0 and status=? and store_id=?", billDetail.getEnd_time(),OrderStatus.ORDER_COMPLETE,billDetail.getStore_id());
		Map store_return=this.daoSupport.queryForMap("select sum(alltotal_pay) as return_price from es_sellback_list where  regtime<? and tradestatus=? and store_id=? and bill_status=0", billDetail.getEnd_time(),3,billDetail.getStore_id());
		
		
		//结算详细单价格信息
		Double pay_money=Double.parseDouble(store_order.get("pay_money").toString());
		Double commi_price=Double.parseDouble(store_order.get("commi_price").toString());
		Double return_price=store_return.get("return_price")==null?0.0:Double.parseDouble(store_return.get("return_price").toString());
		Double returned_commi_price=return_price*store.getCommission()/100;
		//总金额=订单价格-平台佣金-退款订单+退还佣金
		Double price=pay_money-commi_price-return_price+returned_commi_price;
		
		billDetail.setPrice(pay_money);
		billDetail.setCommi_price(commi_price);
		billDetail.setReturned_price(return_price);
		billDetail.setReturned_commi_price(returned_commi_price);
		billDetail.setBill_price(price);
		//纪录订单的结算信息
		this.update_order_info(billDetail.getSn(), store.getStore_id(), billDetail.getEnd_time());
	}
	@Override
	public void add_bill_detail(BillDetail billDetail) {
		this.daoSupport.insert("es_bill_detail", billDetail);
	}
	@Override
	public BillDetail get_bill_detail(Integer id) {
		return (BillDetail) this.daoSupport.queryForObject("select * from es_bill_detail where id=?", BillDetail.class,id);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRED)  
	public void edit_billdetail_status(Integer id, Integer status) {
		String sql="update es_bill_detail set status=? where id=?";
		this.daoSupport.execute(sql, status,id);
		if(status==BillStatusEnum.PAY.getIndex()){
			this.bill(id);
		}
	}
	/**
	 * 结算订单
	 */
	@Transactional(propagation = Propagation.REQUIRED)  
	private void bill(Integer id){
		BillDetail billDetail=this.get_bill_detail(id);
	}
	/**
	 * 查询结算的订单列表
	 */
	@Override
	public Page bill_order_list(Integer pageNo,Integer pageSize,String sn) {
		String sql="select * from es_order where bill_sn=? order by order_id desc";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize, sn);
	}
	/**
	 * 获取结算的退货单列表
	 * @param pageNo
	 * @param pageSize
	 * @param sn
	 * @return
	 */
	@Override
	public Page bill_sell_back_list(Integer pageNo,Integer pageSize,String sn){
		String sql="select * from es_sellback_list where bill_sn=? order by id desc";
		return this.daoSupport.queryForPage(sql, pageNo, pageSize, sn);
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}

	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}

	public IStoreManager getStoreManager() {
		return storeManager;
	}

	public void setStoreManager(IStoreManager storeManager) {
		this.storeManager = storeManager;
	}
}
