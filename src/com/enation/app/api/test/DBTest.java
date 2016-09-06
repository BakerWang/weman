package com.enation.app.api.test;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.enation.app.api.dto.BeginArticleCat;
import com.enation.app.api.dto.ThemeProduct;
import com.enation.app.api.model.Theme;
import com.enation.app.api.service.ArticleService;
import com.enation.app.api.service.ProductService;
import com.enation.app.shop.core.model.Goods;
import com.enation.eop.sdk.utils.UploadUtil;
import com.enation.framework.database.Page;
import com.enation.framework.database.impl.JdbcDaoSupport;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration({ "classpath:spring/*.xml","classpath*:testspring/*.xml" })
public class DBTest {
	
	@Resource
	private ProductService productService;
	@Resource
	private ArticleService articleService;
	
	@Test
	@Transactional   //标明此方法需使用事务  
    @Rollback(true) 
	public void testArticle(){
		try {
			Long st = System.currentTimeMillis();
//			List<BeginArticleCat> bacs = articleService.beginArticle();
//			for(BeginArticleCat bac:bacs){
//				System.out.println(JSONObject.fromObject(bac));
//			}
			articleService.getArticleList(null, new Page());
			System.out.println(System.currentTimeMillis()-st);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	@Transactional   //标明此方法需使用事务  
    @Rollback(true) 
	public void testTheme(){
		Page page =	productService.getThemeProducts(Integer.parseInt("1"), 10 , null);
		System.out.println(page.getTotalCount());
		List<ThemeProduct> tps = (List<ThemeProduct>) page.getResult();
		System.out.println(JSONArray.fromObject(tps).toString());
		for(ThemeProduct theme:tps){
			System.out.println("主题名字:"+theme.getTheme().getTitle());
			for(Goods goods:theme.getGoodList()){
				System.out.println("             主题下的商品:"+goods.getName());
			}
		}
	}
	@Test
	@Transactional   //标明此方法需使用事务  
    @Rollback(false)
	public void createTheme(){
		//Theme theme = productService.saveTheme("第二个主题", "qwe.png", "我的第二个漂亮小屋");
		//productService.saveProductJoinTheme(90, 13 , 3);
	}
	
	public static void main(String[] args) {
		//choose("abcd",3);
		try {
			
			String path = UploadUtil.upload(new File("F:/3.jpg"),"F:/123.jpg", "theme");
			System.out.println(new File(path).exists());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static List<String> choose(String target, int m) {
	    List<String> resultList = new LinkedList<>();
	    doChoose(resultList, target, "", m, 0);
	    for(String str:resultList){
	    	System.out.println(str);
	    }
	    return resultList;
	}
	 
	private static void doChoose(List<String> resultList, String target, String resultStr, int m, int head) {
	    // 递归头
	    if (resultStr.length() == m) {
	        resultList.add(resultStr);
	        return;
	    }
	    // 递归体
	    for (int i = head; i < target.length(); i++) {
	    	System.out.println("....."+i+"||"+head);
	        doChoose(resultList, target, resultStr + target.charAt(i), m, i+1);
	    }
	}
}
