package com.enation.app.shop.core.service;

import java.util.List;

import com.enation.app.shop.core.model.Cart;
import com.enation.app.shop.core.model.support.CartItem;
import com.enation.app.shop.core.model.support.OrderPrice;

/**
 * 购物车业务接口
 * @author kingapex
 * @see com.enation.test.shop.cart.CartTest#testAdd
 *2010-3-23下午03:26:12
 */
public interface ICartManager {
	
	/**
	 * 添加一个购物项
	 * @param cart
	 * @return cart_id
	 */
	public int add(Cart cart);
	
	/**
	 * 根据购物车ID来获取购物车信息
	 * @param cart_id
	 * @return 购物车
	 */
	public Cart get(int cart_id);
	
	
	/**
	 * 计算购物车中货物总数
	 * @param sessionid
	 * @return 货物总数
	 */
	public Integer countItemNum(String sessionid);
	
	/**
	 * 检测某个货品是否有购物车使用
	 * @param goodsid
	 * @return
	 */
	public boolean checkGoodsInCart(Integer goodsid);
	
	/**
	 * 根据productId和sessionId来判断购物车中是否已经存在了一个物品
	 * @param productId
	 * @param sessionid
	 * @return
	 */
	public Cart getCartByProductId(int productId, String sessionid);
	
	/**
	 * 根据productId和sessionId以及addon来判断购物车中是否已经存在了一个物品
	 * @param productId
	 * @param sessionid
	 * @param addon
	 * @return
	 */
	public Cart getCartByProductId(int productId, String sessionid, String addon);
	
	
	
	/**
	 * 读取某用户的购物车中项列表
	 * @param sessionid
	 * @return
	 */
	public List<CartItem> listGoods(String sessionid);
	
	 
	/**
	 * 清空某用户的购物车
	 * @param sessionid
	 */
	public void  clean(String sessionid);
	
 
	
	/**
	 * 更新购物数量
	 * @param sessionid
	 * @param cartid
	 */
	public void updateNum(String sessionid,Integer cartid,Integer num);
	
	
	/**
	 * 删除购物车中的一项
	 * @param cartid
	 */
	public void delete(String sessionid,Integer cartid);
	
 
	/**
	 * 计算购买商品重量，包括商品、捆绑商品、赠品
	 * @param sessionid
	 * @return
	 */
	public Double countGoodsWeight(String sessionid);
	
 
	/**
	 * 计算购物车中货物的总积分
	 * @param sessionid
	 * @return
	 */
	public  Integer countPoint(String sessionid);
	
	/**
	 * 计算购物车费用
	 * @param cart_list
	 * @param shippingid
	 * @param regionid
	 * @return 订单价格
	 * @author LiFenLong 改造计算价格 2014-12-10
	 */
	public OrderPrice countPrice(List<CartItem> cartItemList,Integer shippingid,String regionid);
	/**
	 * 计算购物商品货物总价(原始的，未处理优惠数据的)
	 * @param sessionid
	 * @return
	 */
	public Double countGoodsTotal(String sessionid);
}
