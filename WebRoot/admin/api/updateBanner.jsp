<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib"%>
<style type="text/css">
body{
	font-size: 12px;
	color: red;
	margin:0px;
}
a {
    color: #4287cf;
}
.mr5 {
    border: 1px solid #d7d7d7;
    border-radius: 3px;
    display: inline-block;
    line-height: 14px;
    margin-right: 5px;
    margin-top: 0;
    outline: medium none;
    padding: 7px 0 7px 5px;
    width: 300px;
}
.spanFront{
 	color: #333333;
    font-family: "微软雅黑","宋体";
    font-size: 12px;
    line-height: 180%;
    text-align: left;
    font-weight: bold;
}
.inputFront{
    border: 1px solid #d7d7d7;
    border-radius: 3px;
    display: inline-block;
    font-size: 12px;
    line-height: 14px;
    padding: 7px 0 7px 5px;
    width: 300px;
}
.buttonWrap {
    border-top: 1px solid #d6d6d6;
    bottom: 0;
    box-shadow: 1px 1px 5px #d6d6d6;
    left: 0;
    background: #fff none repeat scroll 0 0;
    height: 30px;
    padding: 3px 0;
    text-align: center;
    width: 100%;
}
.l-btn-left {
    background-color: #f7f7f7;
    border: 1px solid #d1d1d1;
    border-radius: 3px;
    color: #666666;
    cursor: pointer;
    display: inline-block;
    height: 28px;
    line-height: 28px;
    margin-right: 5px;
    padding: 0 20px;
}
.b_fr {
    background-color: #f7f7f7;
    border: 1px solid #d1d1d1;
    border-radius: 3px;
    color: #666666;
    cursor: pointer;
    display: inline-block;
    height: 28px;
    line-height: 30px;
    margin-top: 0;
    padding: 0 5px;
    text-decoration: none;
    width: 25px;
}
</style>
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<link rel="stylesheet" type="text/css" href="/b2b2cbak/adminthemes/new/js/easy-ui/themes/gray/easyui.css"/>    
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="/b2b2cbak/admin/api/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="/b2b2cbak/admin/api/js/fancybox/jquery.fancybox-1.3.4.css"/>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/jquery.pagination.js"></script>
<link href="/b2b2cbak/adminthemes/new/css/myPagination.css" rel="stylesheet" type="text/css" />
<div style="background-color: white; padding: 15px 0 8px 10px;margin:0px;position: relative;">
	<form id="addForm"  enctype="multipart/form-data"  method="post">
		<table style="width:98%;">
			<tr height="38px"><td class="spanFront">banner标题 : </td><td><input value="${phoneBanner.title }" class="inputFront" name="phoneBanner.title" /></td></tr>
			<tr height="38px"><td class="spanFront">banner分类 : </td><td>
				<select name="phoneBanner.category">
					<option <s:if test="#request.phoneBanner.category=='首页banner'">selected="selected"</s:if> value="首页banner">首页banner</option>
					<option <s:if test="#request.phoneBanner.category=='首页banner2'">selected="selected"</s:if> value="首页banner2">首页下方banner</option>
					<option <s:if test="#request.phoneBanner.category=='发现banner'">selected="selected"</s:if> value="发现banner">发现banner</option>
					<option <s:if test="#request.phoneBanner.category=='社交banner'">selected="selected"</s:if> value="社交banner">社交banner</option>
				</select>
			</td></tr>
			<tr height="38px"><td class="spanFront">banner类型 : </td><td>
				<select name="phoneBanner.type" onchange="BannerDetailsSel(this)" id="typeSel">
					<option <s:if test="#request.phoneBanner.type=='跳至HTML5'">selected="selected"</s:if>  value="跳至HTML5">跳至HTML5</option>
					<option <s:if test="#request.phoneBanner.type=='主题列表页'">selected="selected"</s:if>  value="主题列表页">主题列表页</option>
					<option <s:if test="#request.phoneBanner.type=='主题详情页'">selected="selected"</s:if>  value="主题详情页">主题详情页</option>
					<option <s:if test="#request.phoneBanner.type=='商品详情页'">selected="selected"</s:if>  value="商品详情页">商品详情页</option>
					<option <s:if test="#request.phoneBanner.type=='静态图'">selected="selected"</s:if>  value="静态图">静态图</option>
					<option <s:if test="#request.phoneBanner.type=='直播'">selected="selected"</s:if>  value="直播">直播</option>
<!-- 					<option>社交详情页</option> -->
<!-- 					<option>个人详情页</option> -->
				</select>
			</td></tr>
			<tr height="38px"><td class="spanFront">主题内容: </td>
			<td>
				<input type="hidden" value="${phoneBanner.id }" name="phoneBanner.id" id="themeContentStyle"/>
				<input id="bannerTypeHtml" class="inputFront bannerDetails" value="${phoneBanner.details }" name="phoneBanner.details" />
				<div id="bannerTypeThemeAndProduct" class="bannerDetails" style="display:none;float:left;">
					<div style="float:left;overflow: hidden;display:none;border:1px solid blue;padding:3px; margin-right: 5px;" id="productIds"></div>
					<input onclick="selectProcutButton()" type="button" value="选择详情" />
					<a style="display:none;" id="selectProduct" href="#productList">选择详情</a>
				</div>
				<span id="bannerDetailsMsg"></span>
			</td></tr>
			<tr height="38px"><td class="spanFront">主题开始时间 : </td><td>
				<input class="easyui-datetimebox" value="<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${phoneBanner.start_time }"/>" name="startTime" style="width: 150px;height: 28px;" id="start_time" data-options="buttons:buttons" />
				</td></tr>
			<tr height="38px"><td class="spanFront">主题结束时间 : </td><td>
				<input class="easyui-datetimebox" value="<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${phoneBanner.end_time }"/>" name="endTime" style="width: 150px;height: 28px;" id="end_time" data-options="buttons:e_buttons" />
			</td></tr>
			<tr height="38px"><td class="spanFront">主题点击次数 : </td><td>
				<input class="inputFront" name="phoneBanner.click_count" value="${phoneBanner.click_count }" /></td></tr>
				<input type="hidden" value="${phoneBanner.image }" name="phoneBanenr.image" />
			<tr height="38px"><td class="spanFront">主题图片 : </td><td><input type="file" name="bannerFile" /></td></tr>
		</table>
	</form>
	<div style="display:none;">
		<div style="width:1000px;overflow: hidden;" id="productList">
			<div style="height:35px;margin:0 auto;width:100%;"> 
					<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
					<a href="javascript:void(0)" class="b_fr" style="padding: 0 20px;" onclick="searchGoods()">搜索</a>
			</div>
			<table class="prodcutTable" style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<thead><tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;">商品名字</td><td>出售价格/商品数量</td><td>图片</td><td>浏览数/喜欢数</td><td>操作</td></tr></thead>
				<tbody id="productAll"></tbody>
			</table>
			<div class="pagelist" style="width:100%;">
				<div style="line-height:46px;height:46px;width:150px;float:left"><span>共 <span id="totalPageSpan">${page.totalPageCount}</span> 页/<span id="totalCountSpan">${page.totalCount}</span>条记录 </span></div>
				<div id="adminUserManagePagination" class="pagination" style="float:left;margin-top:15px"></div>
			</div>
		</div>
	</div>
	<div class="buttonWrap">
		<a href="javascript:;" onclick="submitForm()" class="l-btn" id="searchAdvance" group=""><span class="l-btn-left"><span class="l-btn-text">保存</span></span></a>
	</div>
</div>
<script>
	
	var buttons = $.extend([], $.fn.datetimebox.defaults.buttons);
	buttons.splice(1, 0, {
	text: '清空',
	handler: function(target){
		 $('#start_time').datetimebox('setValue',"");
	}
	});
	
	var e_buttons = $.extend([], $.fn.datetimebox.defaults.buttons);
	e_buttons.splice(1, 0, {
	text: '清空',
	handler: function(target){
		 $('#end_time').datetimebox('setValue',"");
	}
	});
	function BannerDetailsSel(tt){
		var value = $(tt).val();
		$('.bannerDetails').hide();
		if(value=='跳至HTML5'){
			$('#bannerTypeHtml').show();
			$('#bannerDetailsMsg').html('（填写绝对路径URl）');
		}else if(value=='主题详情页'){
			$('#bannerTypeThemeAndProduct').show();
			$('#bannerDetailsMsg').html('（选择准确的主题或商品）');
		}else if(value=='商品详情页'){
			$('#bannerTypeThemeAndProduct').show();
			$('#bannerDetailsMsg').html('（选择准确的主题或商品）');
		}else if(value=='静态图'){
			
		}else if(value=='直播'){
			$('#bannerTypeHtml').show();
			$('#bannerDetailsMsg').html('（填写直播名字）');
		}
	}
	function searchGoods(){
		var searchKeyword=$('#searchKeyword').val();
		catchProductDetails(1,searchKeyword);
	}
	function catchProductDetails(page,namekeyword){
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminProductAction_getProductList.do',
			data:{
				'page':page,
				'namekeyword':namekeyword
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		var divhtml='';
		    		for(var i=0;i<msg.productList.length;i++){
		    			var pd=msg.productList[i];
		    			divhtml=divhtml+'<tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;text-aglin:left;"><a target="_blank" href="../goods-${goods.goods_id }.html">'+pd.pname+'</a></td><td>'+pd.pprice+'</td>';
		    			divhtml=divhtml+'<td><img src="'+pd.pimage+'" width="50px" height="50px"/> </td><td>'+pd.pviewCount+'</td>';
		    			divhtml=divhtml+'<td><a class="b_fr" pname="'+pd.pname+'" onclick="addProductForBanner('+pd.pid+',0,this)" href="javascript:void(0);">添加</a></td>';
		    		}
		    		$('#productAll').html('');
		    		$('#productAll').prepend(divhtml);
		    		var totalCount=msg.totalCount;
		    		$('#totalCountSpan').html(totalCount);
		    		var totalPage=((totalCount%10==0)?totalCount/10:(parseInt(totalCount/10)+1));
		    		$('#totalPageSpan').html(totalPage);
		    		$('.pagelist').show();
		    		$("#adminUserManagePagination").pagination(totalCount, {  
		                'items_per_page'      : 10,  
		                'num_display_entries' : 5, 
		                'ellipse_text'        : "...",
		                'num_edge_entries'    : 1,  
		                'prev_text'           : "上一页",  
		                'next_text'           : "下一页",  
		                'current_page'        :page-1,
		                'callback'            : function(page_id,jq){
		                	var page = parseInt(page_id)+1;
		                	catchProductDetails(page,namekeyword);
		                } 
		            });
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function catchThemeDetails(page,namekeyword){
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminProductAction_getThemeJsonList.do',
			data:{
				'page':page,
				'namekeyword':namekeyword
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		var divhtml='';
		    		for(var i=0;i<msg.productList.length;i++){
		    			var pd=msg.productList[i];
		    			divhtml=divhtml+'<tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;text-aglin:left;"><a target="_blank" href="../goods-${goods.goods_id }.html">'+pd.pname+'</a></td><td>'+pd.pprice+'</td>';
		    			divhtml=divhtml+'<td><img src="/b2b2cbak/statics/'+pd.pimage+'" width="50px" height="50px"/> </td><td>'+pd.pviewCount+'</td>';
		    			divhtml=divhtml+'<td><a class="b_fr" pname="'+pd.pname+'" onclick="addProductForBanner('+pd.pid+','+pd.pthemeContentStyle+',this)" href="javascript:void(0);">添加</a></td>';
		    		}
		    		$('#productAll').html('');
		    		$('#productAll').prepend(divhtml);
		    		var totalCount=msg.totalCount;
		    		$('#totalCountSpan').html(totalCount);
		    		var totalPage=((totalCount%10==0)?totalCount/10:(parseInt(totalCount/10)+1));
		    		$('#totalPageSpan').html(totalPage);
		    		$('.pagelist').show();
		    		$("#adminUserManagePagination").pagination(totalCount, {  
		                'items_per_page'      : 10,  
		                'num_display_entries' : 5, 
		                'ellipse_text'        : "...",
		                'num_edge_entries'    : 1,  
		                'prev_text'           : "上一页",  
		                'next_text'           : "下一页",  
		                'current_page'        :page-1,
		                'callback'            : function(page_id,jq){
		                	var page = parseInt(page_id)+1;
		                	catchThemeDetails(page,namekeyword);
		                } 
		            });
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function selectProcutButton(){
		var value =  $('#typeSel').val();
		if(value=='主题详情页'){
			catchThemeDetails(1,'');
		}else if(value=='商品详情页'){
			catchProductDetails(1,'');
		}
		setTimeout(function(){
			$('#selectProduct').click();
		}, 500);
	}
	$("#selectProduct").fancybox({
		'width'				: '100%',
		'height'			: '100%',
		'scrolling'         :'no',
		'autoScale'			: true
	});
	function addProductForBanner(pid,themeContentStyle,tt){
		$('#productIds').show();
		$('#productIds').html($(tt).attr('pname'));
		$('#bannerTypeHtml').val(pid);
		$('#themeContentStyle').val(themeContentStyle);
		$.fancybox.close();
	}
	function submitForm() {
			$.Loading.show("正在添加......");
			var options = {
				url : "/b2b2cbak/apiAdmin/AdminBannerAction_updateBanner.do",
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if (data.result == "yes") {
						parent.addTab1("banner","/b2b2cbak/apiAdmin/AdminBannerAction_bannerList.do");
						parent.CloseTabByTitle("banner添加");
					}else{
						alert(data.reason);
					}
				},
				error : function(e) {
					alert("出现错误 ，请重试"+e);
				}
			};
			$("#addForm").ajaxSubmit(options);
	}
</script>