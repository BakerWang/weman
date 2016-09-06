package com.enation.app.shop.component.groupbuy.plugin.goods;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.shop.component.groupbuy.model.GroupBuyActive;
import com.enation.app.shop.component.groupbuy.plugin.act.IGroupBuyActAddEvent;
import com.enation.app.shop.component.groupbuy.plugin.act.IGroupBuyActDeleteEvent;
import com.enation.app.shop.component.groupbuy.plugin.act.IGroupBuyActEndEvent;
import com.enation.app.shop.component.groupbuy.plugin.act.IGroupBuyActStartEvent;
import com.enation.app.shop.component.groupbuy.service.IGroupBuyManager;
import com.enation.app.shop.core.model.Order;
import com.enation.app.shop.core.model.OrderItem;
import com.enation.app.shop.core.plugin.goods.IGoodsVisitEvent;
import com.enation.app.shop.core.plugin.order.IOrderItemSaveEvent;
import com.enation.framework.context.webcontext.ThreadContextHolder;
import com.enation.framework.context.webcontext.WebSessionContext;
import com.enation.framework.database.IDaoSupport;
import com.enation.framework.plugin.AutoRegisterPlugin;
/**
 * 团购商品插件
 * @author fenlongli
 *
 */
@Component
public class GroupBuyGoodsPlugin extends AutoRegisterPlugin implements IOrderItemSaveEvent,IGoodsVisitEvent,IGroupBuyActStartEvent,IGroupBuyActEndEvent,IGroupBuyActDeleteEvent{
	private IDaoSupport daoSupport;
	/**
	 * 当订单创建则减少团购商品的库存,添加购买数量
	 */
	@Override
	@Transactional(propagation = Propagation.REQUIRED)
	public void onItemSave(Order order, OrderItem item) {
		String sql = "select oi.* from es_order_items oi inner join es_goods g on oi.goods_id=g.goods_id  where order_id = ? and g.is_groupbuy=1";
		List<Map > itemList = this.daoSupport.queryForList(sql,order.getOrder_id());
		sql="update es_groupbuy_goods set buy_num=buy_num+?,visual_num=visual_num-? where goods_id=?";
		for (Map orderItem : itemList) {
			this.daoSupport.execute(sql, orderItem.get("num"),orderItem.get("num"),orderItem.get("goods_id"));
		}
	}
	/**
	 * 判断是否浏览过商品 如果没有浏览过商品则浏览次数加1
	 */
	@Override
	public void onVisit(Map goods) {
		WebSessionContext sessionContext = ThreadContextHolder.getSessionContext();
		List<Map> visitedGoods = (List<Map>)sessionContext.getAttribute("visitedGoods");
		if(visitedGoods==null){
			Integer goods_id=Integer.valueOf(goods.get("goods_id").toString());
			String sql="update es_groupbuy_goods set view_num=view_num+1 where goods_id=?";
			this.daoSupport.execute(sql, goods_id);
		}
		
	}
	@Override
	public void onEndGroupBuyEnd(Integer act_id) {
		String sql="update es_goods  SET is_groupbuy=0 WHERE goods_id in(SELECT goods_id from es_groupbuy_goods WHERE cat_id=?)";
		this.daoSupport.execute(sql, act_id);
	}
	@Override
	public void onGroupBuyStart(Integer act_id) {
		//团购商品开启团购
		String sql="update es_goods  SET is_groupbuy=1 WHERE goods_id in(SELECT goods_id from es_groupbuy_goods WHERE cat_id=?)";
		this.daoSupport.execute(sql, act_id);
		
	}
	@Override
	public void onDeleteGroupBuyAct(Integer act_id) {
		String sql="delete from es_groupbuy_goods WHERE cat_id=?";
		this.daoSupport.equals(sql);
	}
	public IDaoSupport getDaoSupport() {
		return daoSupport;
	}
	public void setDaoSupport(IDaoSupport daoSupport) {
		this.daoSupport = daoSupport;
	}
}
