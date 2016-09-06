package com.enation.app.b2b2c.core.action.api.order;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.enation.app.b2b2c.core.model.order.StoreOrder;
import com.enation.app.b2b2c.core.model.order.StoreSellBackList;
import com.enation.app.b2b2c.core.service.OrderReport.IStoreOrderReportManager;
import com.enation.app.b2b2c.core.service.member.IStoreMemberManager;
import com.enation.app.b2b2c.core.service.order.IStoreOrderManager;
import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.SellBackGoodsList;
import com.enation.app.shop.core.model.SellBackList;
import com.enation.app.shop.core.service.ISellBackManager;
import com.enation.framework.action.WWAction;
import com.enation.framework.util.DateUtil;
/**
 * 店铺退货申请API
 * @author LiFenlong
 *
 */
@Component
@Scope("prototype")
@ParentPackage("eop_default")
@Namespace("/api/store")
@Action("storeSellBack")
public class StoreSellBackApiAction extends WWAction {

	private Integer id;
	private Integer status;
	private Integer orderId;
	
	private Integer[] goodsId;
	private Integer[] gid;
	private Integer[] goodsNum;
	private Integer[] payNum;
	private Integer[] productId;
	private Double[] price;
	
	
	private String remark;
	private String refund_way;
	private String return_account;
	private String seller_remark;
	
	private ISellBackManager sellBackManager;
	private IStoreOrderManager storeOrderManager;
	private IStoreMemberManager storeMemberManager;
	private IStoreOrderReportManager storeOrderReportManager;
	
	public String save(){
		try {
			//创建退货单
			StoreSellBackList sellBackList=new StoreSellBackList();
			//记录会员信息
			Member member =  storeMemberManager.getStoreMember();
			sellBackList.setMember_id(member.getMember_id());
			sellBackList.setSndto(member.getName());
			//订单信息
			StoreOrder order = storeOrderManager.get(orderId);
			sellBackList.setAdr(order.getShip_addr());
			sellBackList.setTradeno(com.enation.framework.util.DateUtil.toString(DateUtil.getDateline(),"yyMMddhhmmss"));//退货单号
			sellBackList.setOrdersn(order.getSn());
			sellBackList.setRegoperator("会员");
			sellBackList.setTel(order.getShip_tel());
			sellBackList.setZip(order.getShip_zip());
			sellBackList.setTradestatus(0);
			sellBackList.setRegtime(DateUtil.getDateline());
			sellBackList.setDepotid(order.getDepotid());
			sellBackList.setRemark(remark);
			sellBackList.setRefund_way(refund_way);
			sellBackList.setReturn_account(return_account);
			sellBackList.setStore_id(order.getStore_id());
			Integer sid = this.sellBackManager.save(sellBackList);
			//创建退货单结束 创建退货单商品
			for(int i=0;i<goodsId.length;i++){
				for(int j=0;j<gid.length;j++){
					if(goodsId[i].intValue()==gid[j].intValue()){
						this.saveGoodsList(goodsId[i],goodsNum[j],payNum[j],sid,remark,productId[i],price[i]);
					}
				}
			}
			this.showSuccessJson("退货单申请成功，请等待审核");
		} catch (Exception e) {
			this.showErrorJson("退货单申请失败");
			this.logger.error("退货单申请失败：",e);
		}
		return this.JSON_MESSAGE;
	}
	/**
	 * 保存退货商品
	 * @param goodsid 商品Id
	 * @param goodsnum 退货的数量
	 * @param paynum 购买数量
	 * @param id 退货单Id
	 * @param remark 备注
	 * @param productid 货品Id
	 * @param price 价格
	 * @return 退货商品ID
	 */
	public Integer saveGoodsList(Integer goodsid,Integer goodsnum,Integer paynum,Integer id,String remark,Integer productid,Double price){
		SellBackGoodsList sellBackGoods = new SellBackGoodsList();
	 
		sellBackGoods.setRecid(id);
		sellBackGoods.setPrice(price);
		sellBackGoods.setReturn_num(goodsnum);
		sellBackGoods.setShip_num(paynum);
		sellBackGoods.setGoods_id(goodsid);	
		sellBackGoods.setGoods_remark(remark);
		sellBackGoods.setProduct_id(productid);
		Integer sid = this.sellBackManager.saveGoodsList(sellBackGoods);
		return sid;
	}
	/**
	 * 审核退货申请
	 * @param status 审核状态
	 * @param id 退货单ID
	 * @param seller_remark 审核备注
	 * @return
	 */
	public String saveAuth(){
		try {
			storeOrderReportManager.saveAuth(status, id, seller_remark);
			this.showSuccessJson("操作成功");
		} catch (Exception e) {
			e.printStackTrace();
			this.showErrorJson("操作失败");
		}
		return this.JSON_MESSAGE;
	}
	//get set
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
	public Integer[] getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(Integer[] goodsId) {
		this.goodsId = goodsId;
	}
	public Integer[] getGid() {
		return gid;
	}
	public void setGid(Integer[] gid) {
		this.gid = gid;
	}
	public Integer[] getGoodsNum() {
		return goodsNum;
	}
	public void setGoodsNum(Integer[] goodsNum) {
		this.goodsNum = goodsNum;
	}
	public Integer[] getPayNum() {
		return payNum;
	}
	public void setPayNum(Integer[] payNum) {
		this.payNum = payNum;
	}
	public Integer[] getProductId() {
		return productId;
	}
	public void setProductId(Integer[] productId) {
		this.productId = productId;
	}
	public Double[] getPrice() {
		return price;
	}
	public void setPrice(Double[] price) {
		this.price = price;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getRefund_way() {
		return refund_way;
	}
	public void setRefund_way(String refund_way) {
		this.refund_way = refund_way;
	}
	public String getReturn_account() {
		return return_account;
	}
	public void setReturn_account(String return_account) {
		this.return_account = return_account;
	}
	public String getSeller_remark() {
		return seller_remark;
	}
	public void setSeller_remark(String seller_remark) {
		this.seller_remark = seller_remark;
	}
	public ISellBackManager getSellBackManager() {
		return sellBackManager;
	}
	public void setSellBackManager(ISellBackManager sellBackManager) {
		this.sellBackManager = sellBackManager;
	}
	public IStoreOrderManager getStoreOrderManager() {
		return storeOrderManager;
	}
	public void setStoreOrderManager(IStoreOrderManager storeOrderManager) {
		this.storeOrderManager = storeOrderManager;
	}
	public IStoreMemberManager getStoreMemberManager() {
		return storeMemberManager;
	}
	public void setStoreMemberManager(IStoreMemberManager storeMemberManager) {
		this.storeMemberManager = storeMemberManager;
	}
	public IStoreOrderReportManager getStoreOrderReportManager() {
		return storeOrderReportManager;
	}
	public void setStoreOrderReportManager(
			IStoreOrderReportManager storeOrderReportManager) {
		this.storeOrderReportManager = storeOrderReportManager;
	}
}
