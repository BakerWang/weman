<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
body{
	font-size: 12px;
	margin:0px;
	color:#7c8389;
}
.b_fr {
    text-decoration: none;
    display: block;
    margin-top: 0;
    background-color: #f7f7f7;
    border: 1px solid #d1d1d1;
    border-radius: 3px;
    color: #666666;
    cursor: pointer;
    display: inline-block;
    height: 28px;
    line-height: 30px;
    padding: 0 5px;
    width:25px;
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
.active {
    background: #fff none repeat scroll 0 0;
    border-top: 3px solid #00aeef;
    cursor: pointer;
    float: left;
    font-weight: bold;
}
 ul li {
 	float:left;
    height: 30px;
    line-height: 30px;
    padding: 0 20px;
    cursor: pointer;
}
.datagrid-header td {
    background: #f7f7f7 none repeat scroll 0 0;
    border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
    border-top: 1px dotted #fff;
    color: #333333;
    font-family: "微软雅黑","宋体";
    font-size: 12px;
    font-weight: bold;
    text-align: center;
    overflow: hidden;
    height:50px;
}
.mr5 {
	margin-right: 5px;
    display: block;
    margin-top: 0;
    border: 1px solid #d7d7d7;
    border-radius: 3px;
    display: inline-block;
    line-height: 14px;
    padding: 7px 0 7px 5px;
    width: 300px;
    outline: medium none;
}
a:link, a:visited {
    text-decoration: none;
}
a {
    color: #4287cf;
}
a:hover{
	color:#fd7742;
	text-decoration: none;
}
</style>
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../adminthemes/new/js/jquery.pagination.js"></script>
<link href="../adminthemes/new/css/myPagination.css" rel="stylesheet" type="text/css" />
<div style=" height: 32px;width:100%;">
	<ul style="list-style-type: none;">
			<li class="active" tabid="3" onclick="onTheme(this)">编辑主题</li>
			<li tabid="5" class="" onclick="onTheme(this)">商品列表</li>
	</ul>
</div>
<input type="hidden" value="${themeProduct.theme.id }" id="themeid"/>
<div id="div3" style="background-color: white; padding: 15px 0 8px 10px;margin:0px;position: relative;">
	<form id="addForm"  enctype="multipart/form-data"  method="post">
		<table style="width:98%;">
			<input type="hidden" value="${themeProduct.theme.id }" name="theme.id"/>
			<input type="hidden" value="${themeProduct.theme.image }" name="theme.image"/>
			<tr height="38px"><td class="spanFront">主题标题 : </td><td><input class="inputFront" value="${themeProduct.theme.title }" name="theme.title" /></td></tr>
			<tr height="38px"><td class="spanFront">主题描述 : </td><td><input class="inputFront" value="${themeProduct.theme.details }" name="theme.details" /></td></tr>
			<tr height="38px"><td class="spanFront">主题图片 : </td><td><img src="/b2b2cbak/statics/${themeProduct.theme.image }" width="100px" height="100px"/><input type="file" name="themeFile"/></td></tr>
		</table>
	</form>
	<div class="buttonWrap">
		<a href="javascript:;" onclick="submitForm()" class="easyui-linkbutton l-btn" id="searchAdvance" group=""><span class="l-btn-left"><span class="l-btn-text">更改</span></span></a>
	</div>
</div>
<div id="div5" style="display:none;overflow:hidden;background-color: white; padding: 15px 0 8px 10px;margin:0px;position: relative;margin-top:1px;">
	<div style="width:35%; text-align: center;float:left;margin-top:18px;">
		<div style="overflow: hidden;">
			<span style="color:cadetblue;font-weight:bold;font-size:14px;">列表页的商品</span>
			<div style="border:1px #ccc solid;margin-top:5px;">
				<table id="themeProdcutTable" style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
					<tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;">商品名字</td><td>出售价格</td><td>图片</td><td>浏览数</td><td>操作</td></tr>
					<s:iterator value="#request.themeProduct.goodList" var="goods" status="status" >
						<tr class="datagrid-header aty"><td  style="border-left: 1px solid #ccc;"><a target="_blank" href="../goods-${goods.goods_id }.html">${goods.name }</a></td><td>${goods.price }</td><td><img src="${goods.small }" width="50px" height="50px"/> </td>
						<td>${goods.view_count }</td>
						<td><a class="b_fr" onclick="addProductForTheme(${themeProduct.theme.id},${goods.goods_id },-1,${status.index+1 },this)" href="javascript:void(0);">删除</a></td></tr>
					</s:iterator>
				</table>
			</div>
		</div>
		<div style="margin-top:20px;">
			<span style="color:cadetblue;font-weight:bold;font-size:14px;margin-top:20px;">详情页的商品</span>
			<div style="border:1px #ccc solid;margin-top:5px;">
				<table id="themeProdcutTable2" style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
					<tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;">商品名字</td><td>出售价格</td><td>图片</td><td>浏览数</td><td>操作</td></tr>
					<s:iterator value="#request.themeProduct.detailsGoodList" var="dgoods" status="status" >
						<tr class="datagrid-header aty"><td  style="border-left: 1px solid #ccc;"><a target="_blank" href="../goods-${dgoods.goods_id }.html">${dgoods.name }</a></td><td>${dgoods.price }</td><td><img src="${dgoods.small }" width="50px" height="50px"/> </td>
						<td>${dgoods.view_count }</td>
						<td><a class="b_fr" onclick="addProductForTheme(${themeProduct.theme.id},${dgoods.goods_id },-1,${status.index+1 },this)" href="javascript:void(0);">删除</a></td></tr>
					</s:iterator>
				</table>
			</div>
		</div>
	</div>
	<div style="width:60%; text-align: center;float:left;margin-left:3%;">
		<div style="width:100%;height:30px;">
			<span style="color:cadetblue;font-weight:bold;font-size:14px;">添加商品</span>
			<div style="height:26px;margin:0 auto;float:right;"> 
				<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
				<a href="javascript:void(0)" class="b_fr" style="padding: 0 20px;" onclick="searchGoods()">搜索</a>
			</div>
		</div>
		<div style="margin-top:7px;overflow: hidden;min-height: 100px;">
			<div style="border:1px #ccc solid;margin-top:5px;">
				<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
					<thead><tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;">商品名字</td><td>出售价格</td><td>图片</td><td>浏览数</td><td>列表页</td><td>详情页</td></tr></thead>
					<tbody id="productAll"></tbody>
				</table>
				<div class="pagelist" style="display:none;">
					<div style="line-height:46px;height:46px;width:150px;float:left"><span>共 <span id="totalPageSpan">${page.totalPageCount}</span> 页/<span id="totalCountSpan">${page.totalCount}</span>条记录 </span></div>
					<div id="adminUserManagePagination" class="pagination" style="float:left;margin-top:15px"></div>
				</div>
			</div>
		</div>
	</div>
</div>
<script>
	function onTheme(tt){
		var index = $(tt).attr('tabid');
		$('[id^=div]').hide();
		$('#div'+index).show();
		$(tt).parent().children('li').removeClass('active');
		$(tt).addClass('active');
	}
	function addProductForTheme(themeid,productid,status,position,tt,type){
		if(status==1){
			position = $('.aty').length+1;
		}
		var $this = $(tt);
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminProductAction_addProductForTheme.do',
			data:{
				'themeid':themeid,
				'position':position,
				'productid':productid,
				'type':type,
				'status':status
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='success'){
		    		if(status==1){
		    			var tdiv = $this.parent().parent().clone();
		    			var ttis = $($(tdiv).find('.b_fr'));
		    			ttis.html('删除');
		    			$($(tdiv).find('.b_fr2')).remove();
		    			var strr = ttis.attr('onclick').replace(',1,0,',',-1,0,');
		    			ttis.attr('onclick',strr);
		    			if(type==1){
		    				$('#themeProdcutTable').append(tdiv);
			    		}else if(type==2){
			    			$('#themeProdcutTable2').append(tdiv);
			    		}
		    		}else if(status==-1){
		    			$this.parent().parent().remove();
		    		}
		    	}
		    }
		});
	}
	function searchGoods(){
		var searchKeyword=$('#searchKeyword').val();
		catchProduct(1,searchKeyword);
	}
	function catchProduct(page,namekeyword){
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
		    		var themeid=$('#themeid').val();
		    		for(var i=0;i<msg.productList.length;i++){
		    			var pd=msg.productList[i];
		    			divhtml=divhtml+'<tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;"><a target="_blank" href="../goods-${goods.goods_id }.html">'+pd.pname+'</a></td><td>'+pd.pprice+'</td>';
		    			divhtml=divhtml+'<td><img src="'+pd.pimage+'" width="50px" height="50px"/> </td><td>'+pd.pviewCount+'</td>';
		    			divhtml=divhtml+'<td><a class="b_fr" onclick="addProductForTheme('+themeid+','+pd.pid+',1,0,this,1)" href="javascript:void(0);">添加</a></td>';
		    			divhtml=divhtml+'<td><a class="b_fr b_fr2" onclick="addProductForTheme('+themeid+','+pd.pid+',1,0,this,2)" href="javascript:void(0);">添加</a></td></tr>';
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
		                	catchProduct(page,namekeyword);
		                } 
		            });
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function submitForm() {
			$.Loading.show("正在修改......");
			var options = {
				url : "/b2b2cbak/apiAdmin/AdminProductAction_updateDetials.do",
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if (data.result == "yes") {
						$.Loading.show("修改成功......");
					}
				},
				error : function(e) {
					alert("出现错误 ，请重试"+e);
				}
			};
			$("#addForm").ajaxSubmit(options);
	}
	$(document).ready(function(){
		catchProduct(1,null);
	});
</script>