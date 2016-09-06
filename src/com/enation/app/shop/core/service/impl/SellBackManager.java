package com.enation.app.shop.core.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.base.core.model.MemberLv;
import com.enation.app.base.core.service.IMemberManager;
import com.enation.app.base.core.service.auth.IAdminUserManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderLog;
import com.enation.app.shop.core.model.PaymentLog;
import com.enation.app.shop.core.model.PaymentLogType;
import com.enation.app.shop.core.model.SellBackGoodsList;
import com.enation.app.shop.core.model.SellBackList;
import com.enation.app.shop.core.model.SellBackLog;
import com.enation.app.shop.core.model.SellBackStatus;
import com.enation.app.shop.core.plugin.order.OrderPluginBundle;
import com.enation.app.shop.core.service.IGoodsStoreManager;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IMemberPointManger;
import com.enation.app.shop.core.service.IOrderManager;
import com.enation.app.shop.core.service.IOrderMetaManager;
import com.enation.app.shop.core.service.ISellBackManager;
import com.enation.app.shop.core.service.OrderStatus;
import com.enation.eop.resource.model.AdminUser;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.database.Page;
import com.enation.framework.util.CurrencyUtil;
import com.enation.framework.util.DateUtil;

@SuppressWarnings({ "rawtypes", "unchecked" })
public class SellBackManager extends BaseSupport implements ISellBackManager {

	private IOrderManager orderManager;
	private IMemberManager memberManager;
	private IOrderMetaManager orderMetaManager;
	private IMemberPointManger memberPointManger;
	private IMemberLvManager memberLvManager;
	private IGoodsStoreManager goodsStoreManager;
	private OrderPluginBundle orderPluginBundle;

	/**
	 * 退货单列表
	 */
	public Page list(int page, int pageSize,Integer status) {
		String sql = "select * from sellback_list where tradestatus=? order by id desc ";
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize,status);
		return webpage;
	}
	public Page list(Integer member_id,int page, int pageSize) {
		String sql = "select * from sellback_list where member_id=? order by id desc ";
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize,member_id);
		return webpage;
	}
	@Override
	public void editStatus(Integer status, Integer id,String seller_remark) {
		String sql = "update es_sellback_list set tradestatus=?,seller_remark=? where id=?";
		this.daoSupport.execute(sql, status,seller_remark,id);
		this.saveLog(id, SellBackStatus.valueOf(status), "审核退货申请");
	}
	/**
	 * 退货搜索
	 */
	public Page search(String keyword, int page, int pageSize) {
		String sql = "select * from sellback_list";
		String where = "";
		if (keyword != "") {
			where = " where tradeno like '%" + keyword
					+ "%' or ordersn like '%" + keyword + "%' order by id desc";
		}
		sql = sql + where;
		Page webpage = this.baseDaoSupport.queryForPage(sql, page, pageSize);
		return webpage;
	}

	/**
	 * 退货单详细
	 */
	public SellBackList get(String tradeno) {
		String sql = "select * from sellback_list where tradeno=?";
		SellBackList result = (SellBackList) this.baseDaoSupport
				.queryForObject(sql, SellBackList.class, tradeno);
		return result;
	}

	public SellBackList get(Integer id) {
		String sql = "select * from sellback_list where id=?";
		SellBackList result = (SellBackList) this.baseDaoSupport.queryForObject(sql, SellBackList.class, id);
		return result;
	}

	/**
	 * 保存退货单
	 */
	@SuppressWarnings("unchecked")
	@Transactional(propagation = Propagation.REQUIRED)
	public Integer save(SellBackList data) {
		
		Integer id=0;
		if (data.getId() != null) {
			this.baseDaoSupport.update("sellback_list", data,"id=" + data.getId());
			id = data.getId();
		} else {
			this.baseDaoSupport.insert("sellback_list", data);
			id = this.baseDaoSupport.getLastId("sellback_list");
			//记录退货日志
			if(UserConext.getCurrentAdminUser()==null){
				this.saveLog(id, SellBackStatus.apply, "申请退货","会员："+UserConext.getCurrentMember().getName());
			}else{
				this.saveLog(id, SellBackStatus.apply, "申请退货",UserConext.getCurrentAdminUser().getUsername());
			}
		}
		if (data.getTradestatus() ==0){
			orderPluginBundle.onOrderSellback(data);
			Integer orderid = this.orderManager.get(data.getOrdersn()).getOrder_id();
			baseDaoSupport.execute("update order set status=? where order_id=?",OrderStatus.ORDER_RETURN_APPLY, orderid);
		}
		if (data.getTradestatus() == 1) { // 申请退货
			Integer orderid = this.orderManager.get(data.getOrdersn()).getOrder_id();
			this.log(orderid, "订单申请退货，金额[" + data.getAlltotal_pay() + "]");
		}

		if (data.getTradestatus() == 2) { // 已入库
			syncStore(data);
		}

		if (data.getTradestatus() == 4) { // 取消退货
			Integer orderid = this.orderManager.get(data.getOrdersn()).getOrder_id();
			baseDaoSupport.execute("update order set status=? where order_id=?",OrderStatus.ORDER_SHIP, orderid);
			this.log(orderid, "取消退货");
		}

		return id;
	}

	protected void updateMemberLv(Member member, int point) {
		MemberLv lv = this.memberLvManager
				.getByPoint(member.getPoint() + point);
		if (lv != null) {
			if ((member.getLv_id() == null || lv.getLv_id().intValue() < member
					.getLv_id().intValue())) {
				this.memberManager.updateLv(member.getMember_id(),
						lv.getLv_id());
			}
		}
	}

	/**
	 * 应付结算
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void closePayable(int backid, String finance_remark, String logdetail,Double alltotal_pay) {

		SellBackList data = get(backid);
		data.setTradestatus(SellBackStatus.close_payable.getValue()); // 设置为已结算
		data.setFinance_remark(finance_remark); // 设置财务备注

		// 读取当前会员
		Member member = this.memberManager.get(data.getMember_id());
		Order order = this.orderManager.get(data.getOrdersn());
		Integer orderid = order.getOrder_id();

		/**
		 * ------------------------ 添加退款单 -------------------------
		 */

		// 添加退款单
		addPayable(member, alltotal_pay, 0.0, 0.0, order);

		// 保存退货日志
		this.saveLog(data.getId(), SellBackStatus.close_payable, logdetail);

		// 更新订单状态
		daoSupport.execute("update es_order set status=?,ship_status=?,pay_status=? where order_id=?",
						OrderStatus.ORDER_CANCEL_SHIP, OrderStatus.SHIP_CANCEL,
						OrderStatus.PAY_CANCEL, orderid);
		Map map=new HashMap();
		map.put("tradestatus", 3);
		map.put("alltotal_pay", alltotal_pay);
		map.put("finance_remark", finance_remark);
		daoSupport.update("es_sellback_list", map, "id="+backid);

		this.log(orderid, "订单退货，金额[" + data.getAlltotal_pay() + "]");

	}

	/**
	 * 添加退款单
	 * 
	 * @param member
	 * @param money
	 * @param credit
	 * @param mp
	 * @param order
	 */
	private void addPayable(Member member, Double money, Double credit,
			Double mp, Order order) {
		PaymentLog paymentLog = new PaymentLog();

		paymentLog.setMember_id(member.getMember_id());
		paymentLog.setPay_user(member.getUname());
		paymentLog.setMoney(money);
		paymentLog.setCredit(credit);
		paymentLog.setMarket_point(mp);
		paymentLog.setPay_date(DateUtil.getDateline());
		paymentLog.setOrder_sn(order.getSn());
		paymentLog.setSn("");
		paymentLog.setPay_method(order.getPayment_name());
		paymentLog.setOrder_id(order.getOrder_id());
		paymentLog.setType(PaymentLogType.payable.getValue()); // 应收
		paymentLog.setStatus(1);// 已结算
		paymentLog.setCreate_time(System.currentTimeMillis());

		AdminUser adminUser = UserConext.getCurrentAdminUser();
		if (adminUser != null) {
			paymentLog.setAdmin_user(adminUser.getRealname() + "["
					+ adminUser.getUsername() + "]");
		} else if (member != null) {
			paymentLog.setAdmin_user(member.getName());
		}

		this.daoSupport.insert("es_payment_logs", paymentLog);
	}

	@Override
	public void saveLog(Integer recid, SellBackStatus status, String logdetail) {

		SellBackLog sellBackLog = new SellBackLog();

		sellBackLog.setRecid(recid);
		if ("".equals(logdetail)) {
			logdetail = status.getName();
		}
		sellBackLog.setLogdetail(logdetail);
		sellBackLog.setLogtime(DateUtil.getDateline());
		sellBackLog.setOperator(UserConext.getCurrentAdminUser().getUsername());
		this.daoSupport.insert("es_sellback_log", sellBackLog);
	}
	@Override
	public void saveLog(Integer recid, SellBackStatus status, String logdetail,String memeberName) {

		SellBackLog sellBackLog = new SellBackLog();

		sellBackLog.setRecid(recid);
		if ("".equals(logdetail)) {
			logdetail = status.getName();
		}
		sellBackLog.setLogdetail(logdetail);
		sellBackLog.setLogtime(DateUtil.getDateline());
		sellBackLog.setOperator(memeberName);
		this.daoSupport.insert("es_sellback_log", sellBackLog);
	}


	private void log(Integer order_id, String message) {
		AdminUser adminUser = UserConext.getCurrentAdminUser();
		OrderLog orderLog = new OrderLog();
		orderLog.setMessage(message);
		orderLog.setOp_id(adminUser.getUserid());
		orderLog.setOp_id(1);
		orderLog.setOp_name("test");
		orderLog.setOp_name(adminUser.getUsername());
		orderLog.setOp_time(System.currentTimeMillis());
		orderLog.setOrder_id(order_id);
		this.daoSupport.insert("es_order_log", orderLog);
	}

	/**
	 * 保存退货商品
	 */
	@SuppressWarnings("unchecked")
	public Integer saveGoodsList(SellBackGoodsList data) {
		if (data.getId() == null) {
			this.daoSupport.insert("es_sellback_goodslist", data);
		} else {
			this.daoSupport.update("es_sellback_goodslist", data,"id=" + data.getId());
		}

		Integer id = this.baseDaoSupport.getLastId("sellback_goodslist");

		return id;
	}

	/**
	 * 获取退货商品详细
	 */
	public SellBackGoodsList getSellBackGoods(Integer recid, Integer goodsid) {
		String sql = "select * from es_sellback_goodslist where recid=? and goods_id=?";
		SellBackGoodsList result = (SellBackGoodsList) this.daoSupport.queryForObject(sql, SellBackGoodsList.class, recid, goodsid);
		return result;
	}

	/**
	 * 退货商品列表
	 */
	public List getGoodsList(Integer recid, String sn) {
		String sql = "select i.*,g.return_num,g.goods_id as goodsId,storage_num,goods_remark,g.ship_num from es_order_items i " +
				"left join (select return_num,goods_id,storage_num,goods_remark,ship_num,price  from  es_sellback_goodslist where recid=?) g " +
				"on g.goods_id=i.goods_id where i.order_id in (select order_id from es_order  where sn=?) order by item_id";
		List result = this.daoSupport.queryForList(sql,recid,sn);
		return result;
	}

	public List getGoodsList(Integer recid) {
		return this.baseDaoSupport.queryForList("select s.*,g.name,g.is_pack from sellback_goodslist s inner join goods g on g.goods_id=s.goods_id where recid=?", recid);
	}

	/**
	 * 保存会员账户日志
	 * 
	 * @param log
	 */
	public void saveAccountLog(Map log) {
		this.daoSupport.insert("es_account_log", log);
	}

	/**
	 * 获取退货单id
	 */
	public Integer getRecid(String tradeno) {
		return this.daoSupport.queryForInt("select id from es_sellback_list where tradeno=?", tradeno);
	}

	/**
	 * 修改退货商品数量
	 */
	public void editGoodsNum(Map data) {
		Integer recid = (Integer) data.get("recid");
		Integer goods_id = (Integer) data.get("goods_id");
		this.daoSupport.update("es_sellback_goodslist", data, "recid=" + recid+" and goods_id=" + goods_id);

	}

	/**
	 * 修改入库货品数量
	 */
	public void editStorageNum(Integer recid, Integer goods_id, Integer num) {
		this.daoSupport.execute("update es_sellback_goodslist set storage_num=? where recid=? and goods_id=?", num,recid, goods_id);
	}

	/**
	 * 删除商品
	 */
	public void delGoods(Integer recid, Integer goodsid) {
		this.daoSupport.execute("delete from es_sellback_goodslist where recid=? and goods_id=?",recid, goodsid);
	}

	/**
	 * 操作日志
	 */
	public List sellBackLogList(Integer recid) {
		return this.daoSupport.queryForList("select * from es_sellback_log where recid=? order by id desc",recid);
	}

	
	@Override
	public void syncStore(SellBackList sellback) {
		int depotid = sellback.getDepotid();
		List<Map> goodsList = this.getGoodsList(sellback.getId());
		
		for (Map goods : goodsList) {
			Integer goodsid= (Integer) goods.get("goods_id");
			Integer storage_num = (Integer) goods.get("storage_num");
			Integer productid = (Integer) goods.get("product_id");
			goodsStoreManager.increaseStroe(goodsid, productid, depotid, storage_num);
		}
	}

	public List getProduct(int goodsid) {
		String sql = "select product_id,goods_id from es_product p where goods_id=?";
		List list = this.baseDaoSupport.queryForList(sql,goodsid);
		return list;
	}
	
	
	@Override
	public int searchSn(String sn) {
		String sql = "select count(0) from es_sellback_list where ordersn="+sn;
		int num = this.baseDaoSupport.queryForInt(sql);
		return num;
	}

	public IOrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(IOrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public IMemberManager getMemberManager() {
		return memberManager;
	}

	public void setMemberManager(IMemberManager memberManager) {
		this.memberManager = memberManager;
	}

	public IOrderMetaManager getOrderMetaManager() {
		return orderMetaManager;
	}

	public void setOrderMetaManager(IOrderMetaManager orderMetaManager) {
		this.orderMetaManager = orderMetaManager;
	}

	public IMemberPointManger getMemberPointManger() {
		return memberPointManger;
	}

	public void setMemberPointManger(IMemberPointManger memberPointManger) {
		this.memberPointManger = memberPointManger;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	public IGoodsStoreManager getGoodsStoreManager() {
		return goodsStoreManager;
	}

	public void setGoodsStoreManager(IGoodsStoreManager goodsStoreManager) {
		this.goodsStoreManager = goodsStoreManager;
	}
	public OrderPluginBundle getOrderPluginBundle() {
		return orderPluginBundle;
	}
	public void setOrderPluginBundle(OrderPluginBundle orderPluginBundle) {
		this.orderPluginBundle = orderPluginBundle;
	}
}
