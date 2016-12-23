<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
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
		<input type="hidden" value="1" id="pageNo" />
		<input type="hidden" value="${dataId }" id="dataIdInput" />
		<input type="hidden" value="${type }" id="dataTypeInput" />
			<span style="float:left;height:28px;margin-left:15px;padding-top:3px;">
				${resPage.result[0].title }
			</span>
			<span style="float: right;height:28px;"> 
				<input id="searchKeyword" style="display:none;" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
				<a href="javascript:void(0)" class="b_fr" onclick="searchGoods(${dataId},'${type }')">搜索</a>
			</span>
			<s:if test="#request.type=='user'">
				<span style="float:right;height:28px;margin-right:15px;">
					用户注册时间 : <input class="easyui-datebox" name="userStartTime" value="${userStartTime }" style="width: 110px;height: 28px;" id="user_start_time" data-options="buttons:buttons" />
					<input class="easyui-datebox" name="userEndTime" value="${userEndTime }" style="width: 110px;height: 28px;" id="user_end_time" data-options="buttons:buttons" />
				</span>
			</s:if>
			<span style="float:right;height:28px;margin-right:15px;">
				用户点击时间 : <input class="easyui-datebox" name="startTime" value="${startTime }" style="width: 110px;height: 28px;" id="start_time" data-options="buttons:buttons" />
				<input class="easyui-datebox" name="endTime" value="${endTime }" style="width: 110px;height: 28px;" id="end_time" data-options="buttons:buttons" />
			</span>
	</div>
	<div style="background: #d7d7d7 none repeat scroll 0 0;margin-top:10px;">
		<div style="width:auto;font-size: 12px; border-bottom: 1px solid #ccc;border-top: 1px solid #ccc;cursor: default;">
			<table class="tabe" style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<tr class="datagrid-header">
				<s:if test="#request.type=='user'">
					<td>用户名</td><td>注册时间</td><td>点击数(<span id="totalViewCount"></span>)</td></tr>
				</s:if><s:else>
					<td>点击数</td></tr>
				</s:else>
				<s:iterator value="#request.resPage.result" var="tcObj">
					<tr >
						<s:if test="#request.type=='user'">
							<td style="background: white none repeat scroll 0 0;" align="center">${tcObj.username }</td>
							<td style="background: white none repeat scroll 0 0;" align="center"><cc:dateFormat format="yyyy-MM-dd" time="${tcObj.regtime*1000 }"/></td>
						</s:if>
						<td class="viewCount" style="background: white none repeat scroll 0 0;" align="center">
							<fmt:formatNumber  value="${tcObj.viewCount }"></fmt:formatNumber>
						</td>
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
		var totalViewCount = 0;
		for(var i=0;i<$('.viewCount').length;i++){
			totalViewCount = totalViewCount + parseInt($($('.viewCount')[i]).html());
		}
		$('#totalViewCount').html(totalViewCount);
		$("#adminUserManagePagination").pagination('${resPage.totalCount}', {  
            'items_per_page'      : 30,  
            'num_display_entries' : 5, 
            'ellipse_text'        : "...",
            'num_edge_entries'    : 2,  
            'prev_text'           : "上一页",  
            'next_text'           : "下一页",  
            'current_page'        :'${resPage.currentPageNo-1}',
            'callback'            : function(page_id,jq){
//            		var type = "${type}";
           		var page = parseInt(page_id)+1;
           		$('#pageNo').val(page);
//            		$("#searchForm").find("input[name='page.pages']").val(page);
//            		$("#searchForm").submit();
					searchGoods($('#dataIdInput').val(),$('#dataTypeInput').val());
            } 
        });
	});
	var buttons = $.extend([], $.fn.datebox.defaults.buttons);
	buttons.splice(1, 0, {
	text: '清空',
	handler: function(target){
		 $('#start_time').datebox('setValue',"");
		 $('#user_start_time').datebox('setValue',"");
		 $('#end_time').datebox('setValue',"");
		 $('#user_start_time').datebox('setValue',"");
	}
	});
	
	function searchGoods(tid,type){
		var pageNo = $('#pageNo').val();
		var dataId= tid;
		var st = $('[name="startTime"]').val();
		if(st==null||st==''){
			st='2016-09-01';
		}
		var et =$('[name="endTime"]').val();
		if(et==null||et==''){
			et='2020-01-01';
		}
		var userStartTime = $('[name="userStartTime"]').val();
		if(userStartTime==null||userStartTime==''){
			userStartTime='1910-01-01';
		}
		var userEndTime =$('[name="userEndTime"]').val();
		if(userEndTime==null||userEndTime==''){
			userEndTime='2010-01-01';
		}
		var url = '/b2b2cbak/apiAdmin/AdminProductAction_userThemeCount.do?themeId='+dataId+'&pageNo='+pageNo+'&startTime='+st+'&endTime='+et+'&userStartTime='+userStartTime+'&userEndTime='+userEndTime;
		if(type=='nouser'){
			url = '/b2b2cbak/apiAdmin/AdminProductAction_noUserThemeCount.do?themeId='+dataId+'&pageNo='+pageNo+'&startTime='+st+'&endTime='+et;
		}
		console.log(url);
		url = encodeURI(encodeURI(url));
		window.location.href= url;
	}
</script>