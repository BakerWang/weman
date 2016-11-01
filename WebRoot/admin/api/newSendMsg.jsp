<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
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
			<tr height="38px"><td class="spanFront" width="120px">推送文字: </td><td><input class="inputFront" id="pushMessage" name="pushMessage" /></td></tr>
			<tr height="38px"><td class="spanFront">推送内容 : </td><td>
				<select name="type"  onchange="BannerDetailsSel(this)" id="type">
					<option value="web">网页</option>
					<option value="themeDefault">主题详情页(默认样式)</option>
					<option value="themeTopic">主题详情页(专题样式)</option>
					<option value="product">商品详情页</option>
					<option value="systemMsg">消息列表</option>
				</select>
			</td></tr>
			<tr height="38px"><td class="spanFront">推送内容: </td>
			<td>
				<input id="bannerTypeHtml" class="inputFront bannerDetails" name="data_id" />
				<div id="bannerTypeThemeAndProduct" class="bannerDetails" style="display:none;float:left;">
					<div style="float:left;overflow: hidden;display:none;border:1px solid blue;padding:3px; margin-right: 5px;" id="productIds"></div>
					<input onclick="selectProcutButton()" type="button" value="选择详情" />
					<a style="display:none;" id="selectProduct" href="#productList">选择详情</a>
				</div>
				<span id="bannerDetailsMsg"></span>
			</td></tr>
			<tr height="38px"><td class="spanFront">测试推送用户手机号:</td><td> <input type="text" id="mobile" value="18217557671" /><input type="button" value="测试推送" onclick="testSendMessage()" /></td>
		</table>
	</form>
	<div style="display:none;">
		<div style="width:1000px;overflow: hidden;" id="productList">
			<div style="height:35px;margin:0 auto;width:100%;"> 
					<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
					<a href="javascript:void(0)" class="b_fr" style="padding: 0 20px;" onclick="searchGoods()">搜索</a>
			</div>
			<table class="prodcutTable" style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<thead><tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;">名字</td><td>出售价格/商品数量</td><td>图片</td><td>浏览数/喜欢数</td><td>操作</td></tr></thead>
				<tbody id="productAll"></tbody>
			</table>
			<div class="pagelist" style="width:100%;">
				<div style="line-height:46px;height:46px;width:150px;float:left"><span>共 <span id="totalPageSpan">${page.totalPageCount}</span> 页/<span id="totalCountSpan">${page.totalCount}</span>条记录 </span></div>
				<div id="adminUserManagePagination" class="pagination" style="float:left;margin-top:15px"></div>
			</div>
		</div>
	</div>
	<div class="buttonWrap">
		<a href="javascript:;" onclick="submitForm()" class="l-btn" id="searchAdvance" group=""><span class="l-btn-left"><span class="l-btn-text">全局推送</span></span></a>
	</div>
</div>
<script>
	
	var buttons = $.extend([], $.fn.datebox.defaults.buttons);
	buttons.splice(1, 0, {
	text: '清空',
	handler: function(target){
		 $('#start_time').datebox('setValue',"");
	}
	});
	
	var e_buttons = $.extend([], $.fn.datebox.defaults.buttons);
	e_buttons.splice(1, 0, {
	text: '清空',
	handler: function(target){
		 $('#end_time').datebox('setValue',"");
	}
	});
	function BannerDetailsSel(tt){
		var value = $(tt).val();
		$('.bannerDetails').hide();
		if(value=='web'){
			$('#bannerTypeHtml').show();
			$('#bannerDetailsMsg').html('（填写绝对路径URl）');
		}else if(value=='theme'){
			$('#bannerTypeThemeAndProduct').show();
			$('#bannerDetailsMsg').html('（选择准确的主题或商品）');
		}else if(value=='product'){
			$('#bannerTypeThemeAndProduct').show();
			$('#bannerDetailsMsg').html('（选择准确的主题或商品）');
		}else{
			$('#bannerDetailsMsg').html('跳转消息列表不用填写内容');
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
		    			divhtml=divhtml+'<td><a class="b_fr" pname="'+pd.pname+'" onclick="addProductForBanner('+pd.purl+',this)" href="javascript:void(0);">添加</a></td>';
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
		    			divhtml=divhtml+'<td><a class="b_fr" pname="'+pd.pname+'" onclick="addProductForBanner('+pd.pid+',this)" href="javascript:void(0);">添加</a></td>';
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
	function selectProcutButton(){
		var value =  $('#type').val();
		if(value=='theme'){
			catchThemeDetails(1,'');
		}else if(value=='product'){
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
	function addProductForBanner(purl,tt){
		$('#productIds').show();
		$('#productIds').html($(tt).attr('pname'));
		$('[name="data_id"]').val(purl);
		$.fancybox.close();
	}
	var textSendMessageFlag = false;
	function testSendMessage(){
		var mobile =$('#mobile').val();
		var dataId = $('[name="data_id"]').val();
		var type =$('#type').val();
		var content =$('#pushMessage').val();
		if(type!='systemMsg'&&dataId==''){
			textSendMessageFlag = false;
			alert('推送内容不能为空！！');
			return ;
		}
		if(mobile==''){
			textSendMessageFlag = false;
			alert('测试推送用户手机号不能为空！！');
			return ;
		}
		if(content==''){
			textSendMessageFlag = false;
			alert('推送消息不能为空！！');
			return ;
		}
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminSendMessage_testSendMessage.do',
			data:{
				'mobile':mobile,
				'type':type,
				'pushMessage':content,
				'data_id':dataId
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='success'){
		    		textSendMessageFlag = true;
		    		alert('测试推送成功！');
		    	}else if(msg.reason){
		    		alert("测试手机号错误！");
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function submitForm() {
		if(!textSendMessageFlag){
			alert("推送前必须测试！！！");
			return;
		}
			$.Loading.show("正在添加......");
			var options = {
				url : "/b2b2cbak/apiAdmin/AdminSendMessage_saveSendMessage.do",
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if (data.result == "success") {
						parent.addTab1("banner","/b2b2cbak/apiAdmin/AdminSendMessage_sendMessageList.do");
						parent.CloseTabByTitle("推送添加");
					}
				},
				error : function(e) {
					alert("出现错误 ，请重试"+e);
				}
			};
			$("#addForm").ajaxSubmit(options);
	}
</script>