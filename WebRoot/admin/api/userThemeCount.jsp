<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<link rel="stylesheet" type="text/css" href="/b2b2cbak/adminthemes/new/js/easy-ui/themes/gray/easyui.css"/> 
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../adminthemes/new/js/jquery.pagination.js"></script>
<link href="../adminthemes/new/css/myPagination.css" rel="stylesheet" type="text/css" />
<style type="text/css">
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
.tabe td{
    padding: 6px;
    white-space: normal;
    word-break: break-all;
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

</style>
<div class='buttonArea'>
	<div style="height:30px;margin-top:4px;">
		<span style="float:left;height:28px;margin-left:15px;padding-top:3px;">
			${resPage.result[0].title }
		</span>
		<span style="float: right;height:28px;"> 
			<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
			<a href="javascript:void(0)" class="b_fr" onclick="searchGoods(${dataId})">搜索</a>
		</span>
		<span style="float:right;height:28px;margin-right:15px;">
			用户点击时间 : <input class="easyui-datebox" name="startTime" style="width: 110px;height: 28px;" id="start_time" data-options="buttons:buttons" />
			<input class="easyui-datebox" name="endTime" style="width: 110px;height: 28px;" id="end_time" data-options="buttons:e_buttons" />
		</span>
	</div>
	<div style="background: #d7d7d7 none repeat scroll 0 0;margin-top:10px;">
		<div style="width:auto;font-size: 12px; border-bottom: 1px solid #ccc;border-top: 1px solid #ccc;cursor: default;">
			<table class="tabe" style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<tr class="datagrid-header">
				<td>用户名</td><td>点击数</td></tr>
				<s:iterator value="#request.resPage.result" var="tcObj">
					<tr >
						<td style="background: white none repeat scroll 0 0;" align="center">${tcObj.username }</td>
						<td style="background: white none repeat scroll 0 0;" align="center">${tcObj.viewCount }</td>
					</tr>
				</s:iterator>
			</table>
		</div>
	</div>
</div>
<div class="pagelist">
	<div style="line-height:46px;height:46px;width:150px;float:left"><span>共 ${resPage.totalPageCount} 页/${resPage.totalCount}条记录 </span></div>
	<div id="adminUserManagePagination" class="pagination" style="float:left;margin-top:15px"></div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$("#adminUserManagePagination").pagination('${page.totalCount}', {  
            'items_per_page'      : 10,  
            'num_display_entries' : 5, 
            'ellipse_text'        : "...",
            'num_edge_entries'    : 2,  
            'prev_text'           : "上一页",  
            'next_text'           : "下一页",  
            'current_page'        :'${page.currentPageNo-1}',
            'callback'            : function(page_id,jq){
//            		var type = "${type}";
//            		var page = parseInt(page_id)+1;
//            		$("#searchForm").find("input[name='page.pages']").val(page);
//            		$("#searchForm").submit();
            } 
        });
	});
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
	function searchGoods(tid){
		var dataId= tid;
		var st = $('[name="startTime"]').val();
		if(st==null||st==''){
			st='2016-09-01';
		}
		var et =$('[name="endTime"]').val();
		if(et==null||et==''){
			et='2020-01-01';
		}
		var url = '/b2b2cbak/apiAdmin/AdminProductAction_userThemeCount.do?themeId='+dataId+'&page.currentPageNo=1&startTime='+st+'&endTime='+et;
		console.log(url);
		url = encodeURI(encodeURI(url));
		window.location.href= url;
	}
</script>