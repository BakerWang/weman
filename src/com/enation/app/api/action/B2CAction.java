package com.enation.app.api.action;

import org.springframework.context.annotation.Scope;

@Scope("prototype")
public class B2CAction extends BaseAction{

	private static final long serialVersionUID = -8117414143880172190L;
	
	/**
	 * 添加购物车
	 */
	public void addCart(){
		try{
			
		}catch(Exception e){
			logger.error(e);
			logger.error(e,e);
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取版本失败", e);
			this.logger.error(e,e);
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
		}finally{
			out.print(jsonObject);
			out.close();
		}
	}

	/**
	 * 从购物车删除商品
	 */
	public void deleCart(){
		try{
			
		}catch(Exception e){
			logger.error(e);
			logger.error(e,e);
			this.logger.error("调用"+methodStr+"方法失败");
			this.logger.error("参数:"+requestStr);
			this.logger.error("获取版本失败", e);
			this.logger.error(e,e);
			if("success".equalsIgnoreCase(jsonObject.getString("result"))){
				jsonObject.put("result", "FAILED");
			}
			if(!jsonObject.containsKey("reason")){
				jsonObject.put("reason", "系统错误！");
			}
		}finally{
			out.print(jsonObject);
			out.close();
		}
	}
	
}
