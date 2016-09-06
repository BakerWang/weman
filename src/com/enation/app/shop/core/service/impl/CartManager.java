package com.enation.app.shop.core.service.impl;

import java.util.List;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.base.core.model.Member;
import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.GoodsLvPrice;
import com.enation.app.shop.core.model.mapper.CartItemMapper;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.DiscountPrice;
import com.enation.app.shop.core.model.support.OrderPrice;
import com.enation.app.shop.core.plugin.cart.CartPluginBundle;
import com.enation.app.shop.core.service.ICartManager;
import com.enation.app.shop.core.service.IDlyTypeManager;
import com.enation.app.shop.core.service.IMemberLvManager;
import com.enation.app.shop.core.service.IPromotionManager;
import com.enation.eop.sdk.context.UserConext;
import com.enation.eop.sdk.database.BaseSupport;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.database.DoubleMapper;
import com.enation.framework.util.CurrencyUtil;

/**
 * 购物车业务实现
 * 
 * @author kingapex 2010-3-23下午03:30:50
 * edited by lzf 2011-10-08
 */

public class CartManager extends BaseSupport implements ICartManager {
	private IDlyTypeManager dlyTypeManager;

	private CartPluginBundle cartPluginBundle;
	private IMemberLvManager memberLvManager;
	private IPromotionManager promotionManager;
	
	@Transactional(propagation = Propagation.REQUIRED)

	public int add(Cart cart) {
		//HttpCacheManager.sessionChange();
		
		
		/*
		 * 触发购物车添加事件
		 */
		this.cartPluginBundle.onAdd(cart);
		
		
		
		String sql ="select count(0) from cart where  product_id=? and session_id=? and itemtype=? ";	
		
		int count = this.baseDaoSupport.queryForInt(sql, cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
		if(count>0){
			this.baseDaoSupport.execute("update cart set num=num+? where  product_id=? and session_id=? and itemtype=? ", cart.getNum(),cart.getProduct_id(),cart.getSession_id(),cart.getItemtype());
		
			return 0;
		}else{
			
			
			this.baseDaoSupport.insert("cart", cart);


			Integer cartid  = this.baseDaoSupport.getLastId("cart");
			cart.setCart_id(cartid);
			
			this.cartPluginBundle.onAfterAdd(cart);
			return cartid;
		}

	}
	
	/**
	 * 
	 */
	public Cart get(int cart_id){
		return (Cart)this.baseDaoSupport.queryForObject("SELECT * FROM cart WHERE cart_id=?", Cart.class, cart_id);
	}
	
	public Cart getCartByProductId(int productId, String sessionid){
		return (Cart)this.baseDaoSupport.queryForObject("SELECT * FROM cart WHERE product_id=? AND session_id=?", Cart.class, productId,sessionid);
	}
	
	public Cart getCartByProductId(int productId, String sessionid, String addon){
		return (Cart)this.baseDaoSupport.queryForObject("SELECT * FROM cart WHERE product_id=? AND session_id=? AND addon=?", Cart.class, productId, sessionid, addon);
	}

	public Integer countItemNum(String sessionid) {
		String sql = "select count(0) from cart where session_id =?";
		return this.baseDaoSupport.queryForInt(sql, sessionid);
	}
	
	
	public List<CartItem> listGoods(String sessionid) {
	
 
		StringBuffer sql = new StringBuffer();

		sql.append("select g.cat_id as catid,g.goods_id,g.thumbnail,c.name ,  p.sn, p.specs  ,g.mktprice,g.unit,g.point,p.product_id,c.price,c.cart_id as cart_id,c.num as num,c.itemtype,c.addon,c.weight  from "+ this.getTableName("cart") +" c,"+ this.getTableName("product") +" p,"+ this.getTableName("goods")+" g ");
		sql.append("where c.itemtype=0 and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?");
		List<CartItem>  list  =this.daoSupport.queryForList(sql.toString(), new CartItemMapper(), sessionid);
	
		cartPluginBundle.filterList(list, sessionid);
	

		return list;
	}

 
	/**
	 * 应用会员价
	 * 
	 * @param itemList
	 * @param memPriceList
	 * @param discount
	 */
	private void applyMemPrice(List<CartItem> itemList,
			List<GoodsLvPrice> memPriceList, double discount) {
		for (CartItem item : itemList) {
			double price = item.getCoupPrice()* discount;
			for (GoodsLvPrice lvPrice : memPriceList) {
				if (item.getProduct_id().intValue() == lvPrice.getProductid()) {
					price = lvPrice.getPrice();
				}
			}

		 
			item.setCoupPrice(price);
		}
 
 
	}


 
 
	
	
	
	public void  clean(String sessionid){
		String sql ="delete from cart where session_id=?";
 
		this.baseDaoSupport.execute(sql, sessionid);
//		HttpCacheManager.sessionChange();
	}

	public void clean(String sessionid, Integer userid, Integer siteid) {

			String sql = "delete from cart where session_id=?";
			this.baseDaoSupport.execute(sql, sessionid);

		if (this.logger.isDebugEnabled()) {
			this.logger.debug("clean cart sessionid[" + sessionid + "]");
		}
//		HttpCacheManager.sessionChange();
	}

	public void delete(String sessionid, Integer cartid) {
		String sql = "delete from cart where session_id=? and cart_id=?";
		this.baseDaoSupport.execute(sql, sessionid, cartid);
		this.cartPluginBundle.onDelete(sessionid, cartid);
		this.cartPluginBundle.onDelete(sessionid, cartid);
//		HttpCacheManager.sessionChange();
	}

	public void updateNum(String sessionid, Integer cartid, Integer num) {
		String sql = "update cart set num=? where session_id =? and cart_id=?";
		this.baseDaoSupport.execute(sql, num, sessionid, cartid);
	}

	public Double countGoodsTotal(String sessionid) {
		StringBuffer sql = new StringBuffer();
		sql.append("select sum( c.price * c.num ) as num from cart c ");
		sql.append("where  c.session_id=? and c.itemtype=0 ");
		Double price = (Double) this.baseDaoSupport.queryForObject(sql
				.toString(), new DoubleMapper(), sessionid);
		return price;
	}



	
	public Double  countGoodsDiscountTotal(String sessionid){
		

		List<CartItem> itemList = this.listGoods(sessionid);

		double price = 0; // 计算商品促销规则优惠后的总价
		for (CartItem item : itemList) {
			// price+=item.getSubtotal();
			price = CurrencyUtil.add(price, item.getSubtotal());
		}

		return price;
	}

	
	public Integer countPoint(String sessionid) {

//		Member member = UserServiceFactory.getUserService().getCurrentMember();
//		if (member != null) {
//			Integer memberLvId = member.getLv_id();
//			StringBuffer sql = new StringBuffer();
//			sql
//					.append("select c.*, g.goods_id, g.point from "
//							+ this.getTableName("cart")
//							+ " c,"
//							+ this.getTableName("goods")
//							+ " g, "
//							+ this.getTableName("product")
//							+ " p where p.product_id = c.product_id and g.goods_id = p.goods_id and c.session_id = ?");
//			List<Map> list = this.daoSupport.queryForList(sql.toString(),
//					sessionid);
//			Integer result = 0;
//			for (Map map : list) {
//				Integer goodsid = StringUtil.toInt(map.get("goods_id")
//						.toString());
//				List<Promotion> pmtList = new ArrayList();
//				
//				if(memberLvId!=null){
//					pmtList = promotionManager.list(goodsid, memberLvId);
//				}
//				
//				for (Promotion pmt : pmtList) {
//
//					// 查找相应插件
//					String pluginBeanId = pmt.getPmts_id();
//					IPromotionPlugin plugin = promotionManager
//							.getPlugin(pluginBeanId);
//
//					if (plugin == null) {
//						logger.error("plugin[" + pluginBeanId + "] not found ");
//						throw new ObjectNotFoundException("plugin["
//								+ pluginBeanId + "] not found ");
//					}
//
//					// 查找相应优惠方式
//					String methodBeanName = plugin.getMethods();
//					if (this.logger.isDebugEnabled()) {
//						this.logger.debug("find promotion method["
//								+ methodBeanName + "]");
//					}
//					IPromotionMethod promotionMethod = SpringContextHolder
//							.getBean(methodBeanName);
//					if (promotionMethod == null) {
//						logger.error("plugin[" + methodBeanName
//								+ "] not found ");
//						throw new ObjectNotFoundException("promotion method["
//								+ methodBeanName + "] not found ");
//					}
//
//					// 翻倍积分方式
//					if (promotionMethod instanceof ITimesPointBehavior) {
//						Integer point = StringUtil.toInt(map.get("point")
//								.toString());
//						ITimesPointBehavior timesPointBehavior = (ITimesPointBehavior) promotionMethod;
//						point = timesPointBehavior.countPoint(pmt, point);
//						result += point;
//					}
//
//				}
//			}
//			return result;
//		} else {
			StringBuffer sql = new StringBuffer();
			sql.append("select  sum(g.point * c.num) from "
					+ this.getTableName("cart") + " c,"
					+ this.getTableName("product") + " p,"
					+ this.getTableName("goods") + " g ");
			sql
					.append("where (c.itemtype=0  or c.itemtype=1)  and c.product_id=p.product_id and p.goods_id= g.goods_id and c.session_id=?");

			return this.daoSupport.queryForInt(sql.toString(), sessionid);
//		}
	}

	public Double countGoodsWeight(String sessionid) {
		StringBuffer sql = new StringBuffer(
				"select sum( c.weight * c.num )  from cart c where c.session_id=?");
		Double weight = (Double) this.baseDaoSupport.queryForObject(sql
				.toString(), new DoubleMapper(), sessionid);
		return weight;
	}
	

 
	
	
	@Override
	public OrderPrice countPrice(List<CartItem> cartItemList, Integer shippingid,String regionid) {
		 
		
		OrderPrice orderPrice = new OrderPrice();
		//计算商品重量
		Double weight=0.0;
		//计算商品原始价格
		Double originalPrice=0.0;
		//订单总价格
		Double  orderTotal = 0d;
		//配送费用
		Double dlyPrice = 0d; //如果没有计算配送信息，为0
		//优惠后的订单价格,默认为商品原始价格
		Double coupPrice =0.0; 
		//获取会员
		Member member = UserConext.getCurrentMember();

		for (CartItem cartItem : cartItemList) {
			weight=CurrencyUtil.add(weight, CurrencyUtil.mul(cartItem.getWeight(), cartItem.getNum()));
			originalPrice=CurrencyUtil.add(originalPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
			if(member!=null){
				coupPrice = CurrencyUtil.add(coupPrice, CurrencyUtil.mul(cartItem.getPrice(), cartItem.getNum()));
			}
		}
		//计算会员优惠
		if(member!=null){
			originalPrice =countGoodsDiscountTotal(ThreadContextHolder.getHttpRequest().getSession().getId()); //应用了商品优惠规则后的商品价格
		}
		int point =0;
		
		/**
		 * -------------------------------
		 * 如果传递了配送信息，计算配送费用
		 * -------------------------------
		 * 
		 */
		if(regionid!=null &&shippingid!=null ){
			if(shippingid!=0){
				//计算原始配置送费用
				Double[] priceArray = this.dlyTypeManager.countPrice(shippingid, weight, originalPrice, regionid);
				dlyPrice = priceArray[0];//费送费用
			}
			
			if(member!=null){ //计算会员优惠
				//对订单价格和积分执行优惠
				DiscountPrice discountPrice  = this.promotionManager.applyOrderPmt(coupPrice, dlyPrice,point, member.getLv_id()); 
				coupPrice=discountPrice.getOrderPrice() ; //优惠会后订单金额
				dlyPrice = discountPrice.getShipFee(); //优惠后的配送费用
				point = discountPrice.getPoint(); //优惠后的积分
			}
			
			//去除保价费用
		}
		
		
		
		/**
		 * ---------------------------------
		 * 设置订单的各种费用项
		 * ---------------------------------
		 */
		if(member==null){coupPrice=originalPrice;}
		//打折金额：原始的商品价格-优惠后的商品金额
		Double reducePrice = CurrencyUtil.sub(originalPrice , coupPrice);
		
		//订单总金额 为将优惠后的商品金额加上优惠后的配送费用
		orderTotal = CurrencyUtil.add(coupPrice, dlyPrice); 
		
		orderPrice.setDiscountPrice(reducePrice); //优惠的金额
		orderPrice.setGoodsPrice(coupPrice); //商品金额，优惠后的          whj2015-05-15修改，如果不修改，导致商品金额是0，之前的源码“coupPrice-originalPrice”，优惠金额与原始价格相同，所以相减为0,不知道对不对，请小李休闲修改
		orderPrice.setShippingPrice(dlyPrice);
		orderPrice.setPoint(point); 
		orderPrice.setOriginalPrice(originalPrice);
		orderPrice.setOrderPrice(orderTotal);
		orderPrice.setWeight(weight);
		orderPrice.setNeedPayMoney(orderTotal);// 需支付的金额默认为订单总金额
		orderPrice  = this.cartPluginBundle.coutPrice(orderPrice,null);
		return orderPrice;
		 
		
		/**
		 * --------------------------------------
		 * 废弃掉的代码在这里，有可能要改成插件的
		 * --------------------------------------
		 * 
		 * 		//计算捆绑商品的总价，并加入订单总价中
		Double pgkTotal = countPgkTotal(sessionid);
		//计算团购总价
		Double groupBuyTotal = countGroupBuyTotal(sessionid);
		
		originalPrice = CurrencyUtil.add(originalPrice,pgkTotal);
		originalPrice = CurrencyUtil.add(originalPrice,groupBuyTotal);
		 */
	}
	

	public IPromotionManager getPromotionManager() {
		return promotionManager;
	}
 

	public CartPluginBundle getCartPluginBundle() {
		return cartPluginBundle;
	}

	public void setCartPluginBundle(CartPluginBundle cartPluginBundle) {
		this.cartPluginBundle = cartPluginBundle;
	}

 
	public void setMemberLvManager(IMemberLvManager memberLvManager) {
		this.memberLvManager = memberLvManager;
	}

	public IDlyTypeManager getDlyTypeManager() {
		return dlyTypeManager;
	}

	public void setDlyTypeManager(IDlyTypeManager dlyTypeManager) {
		this.dlyTypeManager = dlyTypeManager;
	}

	public IMemberLvManager getMemberLvManager() {
		return memberLvManager;
	}

	public void setPromotionManager(IPromotionManager promotionManager) {
		this.promotionManager = promotionManager;
	}

	@Override
	public boolean checkGoodsInCart(Integer goodsid) {
		String sql ="select count(0) from cart where goods_id=?";
		return this.baseDaoSupport.queryForInt(sql, goodsid)>0;
	}


}
