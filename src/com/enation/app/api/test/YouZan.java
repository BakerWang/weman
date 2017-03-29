package com.enation.app.api.test;

import com.youzan.open.sdk.client.auth.Sign;
import com.youzan.open.sdk.client.auth.Token;
import com.youzan.open.sdk.client.core.DefaultKDTClient;
import com.youzan.open.sdk.client.core.KDTClient;
import com.youzan.open.sdk.gen.v1_0_0.api.KdtItemsCustomGet;
import com.youzan.open.sdk.gen.v1_0_0.api.KdtItemsInventoryGet;
import com.youzan.open.sdk.gen.v1_0_0.api.KdtItemsOnsaleGet;
import com.youzan.open.sdk.gen.v1_0_0.api.KdtTradeCartAdd;
import com.youzan.open.sdk.gen.v1_0_0.api.KdtUmpPromotionGet;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsCustomGetParams;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsCustomGetResult;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsInventoryGetParams;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsInventoryGetResult;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsInventoryGetResult.GoodsDetail;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsOnsaleGetParams;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsOnsaleGetResult;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtTradeCartAddParams;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtTradeCartAddResult;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtUmpPromotionGetParams;
import com.youzan.open.sdk.gen.v1_0_0.model.KdtUmpPromotionGetResult;

public class YouZan {

	private static String appKey = "33061573210ca6f119";
	private static String appSecret = "b5c2ee1e666a00b3b2d9ff69fc23fc80";
	
	public static void main(String[] args) {
		try {
			KDTClient client = new DefaultKDTClient(new Sign(appKey, appSecret)); //new Sign(appKey, appSecret)
//			KdtTradeCartAddParams kdtTradeCartAddParams = new KdtTradeCartAddParams();
//			kdtTradeCartAddParams.setNum(2L);
//			kdtTradeCartAddParams.setStoreId(18649521L);
//			kdtTradeCartAddParams.setGoodsId(325736789L);
//			KdtTradeCartAdd kdtTradeCartAdd = new KdtTradeCartAdd();
//			kdtTradeCartAdd.setAPIParams(kdtTradeCartAddParams);
//			KdtTradeCartAddResult result = client.invoke(kdtTradeCartAdd);
//			System.out.println(result);
			
//			KdtItemsInventoryGetParams kdtItemsInventoryGetParams = new KdtItemsInventoryGetParams();
//			KdtItemsInventoryGet kdtItemsInventoryGet = new KdtItemsInventoryGet();
//			kdtItemsInventoryGet.setAPIParams(kdtItemsInventoryGetParams);
//			KdtItemsInventoryGetResult result = client.invoke(kdtItemsInventoryGet);
//			System.out.println(result.getItems().length);
//			for(GoodsDetail gd:result.getItems()){
//				System.out.println(gd.toString());
//			}
			
			//商品在售列表
//			KdtItemsOnsaleGetParams kdtItemsOnsaleGetParams = new KdtItemsOnsaleGetParams();
//			KdtItemsOnsaleGet kdtItemsOnsaleGet = new KdtItemsOnsaleGet();
//			kdtItemsOnsaleGet.setAPIParams(kdtItemsOnsaleGetParams);
//			KdtItemsOnsaleGetResult result = client.invoke(kdtItemsOnsaleGet);
//			for(com.youzan.open.sdk.gen.v1_0_0.model.KdtItemsOnsaleGetResult.GoodsDetail gd:result.getItems()){
//				System.out.println(gd.getTitle()+"||"+gd.getNumIid()+"||"+gd.getOuterId());
//			}
			//325736789  325956453
			
			//通过外部编号活的商品详情信息
			KdtItemsCustomGetParams kdtItemsCustomGetParams = new KdtItemsCustomGetParams();
			kdtItemsCustomGetParams.setOuterId("10003");
			KdtItemsCustomGet kdtItemsCustomGet = new KdtItemsCustomGet();
			kdtItemsCustomGet.setAPIParams(kdtItemsCustomGetParams);
			KdtItemsCustomGetResult result = client.invoke(kdtItemsCustomGet);
			System.out.println(result.getItems()[0].getTitle()+"||"+result.getItems()[0].getAlias()+"||"+result.getItems()[0].getNumIid());
			
			
			KdtUmpPromotionGetParams kdtUmpPromotionGetParams = new KdtUmpPromotionGetParams();
			kdtUmpPromotionGetParams.setItemId(result.getItems()[0].getNumIid());

			KdtUmpPromotionGet kdtUmpPromotionGet = new KdtUmpPromotionGet();
			kdtUmpPromotionGet.setAPIParams(kdtUmpPromotionGetParams);
			KdtUmpPromotionGetResult result2 = client.invoke(kdtUmpPromotionGet);
			System.out.println(result2.getItemPromotion().getStartDate()+"||"+result2.getItemPromotion().getEndDate());
			client.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
