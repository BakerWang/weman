<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../adminthemes/new/js/jquery.pagination.js"></script>
<link href="../adminthemes/new/css/myPagination.css" rel="stylesheet" type="text/css" />
<style type="text/css">
body{
	font-size: 12px;
}
.mr5 {
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
    line-height: 28px;
    padding: 0 20px;
}
.searchAdvanced {
    background: #fffbf4 none repeat scroll 0 0;
    border: 1px solid #d1d1d1;
    box-sizing: border-box;
    color: #7c8389;
    float: left;
    margin-top: 10px;
    padding: 10px 0 5px;
    width: 100%;
}
table tr td {
    padding: 6px;
    white-space: normal;
    word-break: break-all;
}
.datagrid-header {
    height: 50px;
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
}
.divTr td{
	border-bottom: 1px solid #ccc;
    border-right: 1px solid #ccc;
    text-align: center;
}
tr:hover td .updateThemeA{
	background-position: -26px 0px;
	cursor: pointer;
}
.divTr{
	background-color: white;
}
.divTr:hover{
	background: #d0e5f5 none repeat scroll 0 0;
	cursor: default;
}
.updateThemeA{
    background-image: url("/b2b2cbak/adminthemes/new/images/icon21.png");
    background-position: -26px -29px;
    display: inline-block;
    float: none;
    height: 23px;
    margin: 0 5px;
    text-indent: 0;
    width: 23px;
    text-decoration: none;
}
</style>
<jsp:useBean id="now" class="java.util.Date" scope="page"/>
<div class='buttonArea'>
	<div style="height:30px;margin-top:4px;">
		<span style="float:left;height:28px;">
			<a href="javascript:void(0)" class="b_fr" onclick="parent.addTab1('banner添加','/b2b2cbak/apiAdmin/AdminBannerAction_newBanner.do')">新建</a>
		</span>
		<form id="searchForm" action="/b2b2cbak/apiAdmin/AdminBannerAction_bannerList.do" id="searchForm" method="post">
			<span style="float: right;height:28px;"> 
				<input type="hidden" value="1" name="pageNo" id="goodsPage" />
			</span>
<%-- 			<span style="float: right;height:28px;">  --%>
<!-- 				<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord">  -->
<!-- 				<a href="javascript:void(0)" class="b_fr" onclick="searchGoods()">搜索</a> -->
<%-- 			</span> --%>
		</form>
	</div>
	<div style="background: #d7d7d7 none repeat scroll 0 0;margin-top:10px;">
		<div style="width:auto;font-size: 12px; border-bottom: 1px solid #ccc;border-top: 1px solid #ccc;cursor: default;">
			<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<tr class="datagrid-header">
				<td style="border-left: 1px solid #ccc;">id</td><td>banner标题</td><td>banner图片</td><td>详情</td><td>分类</td><td>类型</td><td>显示时间</td><td>创建时间</td><td>删除</td></tr>
				<s:iterator value="#request.page.result" var="bannerObj">
					<tr class="divTr" style="color:red;<c:if test="${now.getTime() > bannerObj.start_time && now.getTime()<bannerObj.end_time }">color:black;</c:if><c:if test="${now.getTime() < bannerObj.start_time }">color:royalblue;</c:if>" height="100px" >
					<td style="border-left: 1px solid #ccc;">${bannerObj.id } </td><td>${bannerObj.title }</td><td><img src="/b2b2cbak/statics/${bannerObj.image }" width="80px" height="80px"/></td>
					<td style="max-width: 260px;">${bannerObj.details }</td><td>${bannerObj.category }</td><td>${bannerObj.type }</td>
					<td>
						<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${bannerObj.start_time }"/>
						||
						<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${bannerObj.end_time }"/>
					</td>
					<td>
						<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${bannerObj.create_time }"/>
					</td>
					<td>
<%-- 						<a style="text-decoration: none;" class="b_fr" onclick="updateTheme(this,${bannerObj.id})" href="javascript:void(0);">删除</a> --%>
						<a style="text-decoration: none;" class="b_fr" href="/b2b2cbak/apiAdmin/AdminBannerAction_getBannerDetails.do?bid=${bannerObj.id }">更改</a>
					</td></tr>
				</s:iterator>
			</table>
		</div>
	</div>
</div>
<div class="pagelist">
	<div style="line-height:46px;height:46px;width:150px;float:left"><span>共 ${page.totalPageCount} 页/${page.totalCount}条记录 </span></div>
	<div id="adminUserManagePagination" class="pagination" style="float:left;margin-top:15px"></div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$("#adminUserManagePagination").pagination('${page.totalCount}', {  
            'items_per_page'      : 20,  
            'num_display_entries' : 5, 
            'ellipse_text'        : "...",
            'num_edge_entries'    : 2,  
            'prev_text'           : "上一页",  
            'next_text'           : "下一页",  
            'current_page'        :'${page.currentPageNo-1}',
            'callback'            : function(page_id,jq){
//            		var type = "${type}";
           		var page = parseInt(page_id)+1;
           		alert(page);
           		$("#goodsPage").val(page);
           		$("#searchForm").submit();
            } 
        });
	});
	function updateTheme(tt,tid){
		var $this = $(tt);
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminBannerAction_delBanner.do',
			data:{
				'bid':tid
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		$this.parent().parent().remove();
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
		
	}
// 	function searchGoods(){
// 		var kw = $('#searchKeyword').val();
// 		if(kw==null||kw==''){
// 			return;
// 		}
// 		var url = '/b2b2cbak/apiAdmin/AdminProductAction_getThemeList.do?page=1&keywords='+kw;
// 		url = encodeURI(encodeURI(url));
// 		window.location.href= url;
// 	}
</script>