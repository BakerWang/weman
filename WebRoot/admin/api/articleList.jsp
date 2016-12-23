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
<%-- 		<span style="float:left;height:28px;"> --%>
<!-- 			<a href="javascript:void(0)" class="b_fr" onclick="parent.addTab1('banner添加','/b2b2cbak/apiAdmin/AdminBannerAction_newBanner.do')">新建</a> -->
<%-- 		</span> --%>
		<form id="searchForm" action="/b2b2cbak/apiAdmin/AdminArticleAction_articleList.do" id="searchForm" method="post">
			<span style="float: right;height:28px;">
				<select id="articleFormStatus" name="adminSearchForm.status" style="height:30px;margin-right:5px;">
					<option selected="selected" value="111">所有</option>
					<option <s:if test="#request.adminSearchForm.status==0">selected="selected"</s:if> value="0">新建</option>
					<option <s:if test="#request.adminSearchForm.status==-1">selected="selected"</s:if> value="-1">删除</option>
					<option <s:if test="#request.adminSearchForm.status==1">selected="selected"</s:if> value="1">全部</option>
<!-- 					<option value="0">待审核</option> -->
<!-- 					<option value="2">通过</option> -->
<!-- 					<option value="-2">未通过</option> -->
				</select>
				<input type="hidden" value="1" id="pageSize" name="pageSize" />
				<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
				<a href="javascript:void(0)" class="b_fr" onclick="searchGoods()">搜索</a>
			</span>
		</form>
	</div>
	<div style="background: #d7d7d7 none repeat scroll 0 0;margin-top:10px;">
		<div style="width:auto;font-size: 12px; border-bottom: 1px solid #ccc;border-top: 1px solid #ccc;cursor: default;">
			<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<tr class="datagrid-header"><td style="border-left: 1px solid #ccc;">id</td><td>发文图片</td><td>详情</td><td>类别</td><td>品牌</td><td>发文标签</td><td>喜欢数</td><td>评论数</td><td>浏览数</td><td>创建时间</td><td>操作</td></tr>
				<s:iterator value="#request.page.result" var="articleObj">
					<tr class="divTr"><td style="border-left: 1px solid #ccc;width:25px;">${articleObj.id } </td><td><img src="/b2b2cbak/statics/${articleObj.image }" width="80px" height="80px"/></td>
					<td>${articleObj.content }</td><td>${articleObj.categoryName }</td><td>${articleObj.brandName }</td>
					<td>${articleObj.tagStr }</td>
					<td>${articleObj.loveCount }</td><td>${articleObj.commentCount }</td>
					<td>
						<input type="text" style="width:50px;" value="${articleObj.viewCount }" class="articleViewCount" /><button onclick="updateViewCount(this,${articleObj.id })">修改</button>
					</td>
					<td>
						<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${articleObj.createTime }"/>
					</td>
					<td>
						<select onchange="updateArticleStatusSel(${articleObj.id },this)">
							<option <s:if test="#articleObj.status==0">selected="selected"</s:if> value="0">新建</option>
							<option <s:if test="#articleObj.status==1">selected="selected"</s:if> value="1">全部</option>
							<option <s:if test="#articleObj.status==-1">selected="selected"</s:if> value="-1">删除</option>
						</select>
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
            'current_page'        :'${pageSize-1}',
            'callback'            : function(page_id,jq){
            	
//            		var type = "${type}";
           		var page = parseInt(page_id)+1;
           		$('#pageSize').val(page);
           		console.log($('#pageSize').val())
           		searchGoods();
           		//$("#searchForm").submit();
            } 
        });
	});
	function updateViewCount(tt,id){
		var viewCount = $(tt).parent().find('.articleViewCount').val();
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminArticleAction_updateArticleStatus.do',
			data:{
				'viewCount':viewCount,
				'articleId':id
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		alert("修改成功!");
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function updateArticleStatusSel(id,tt){
		var status = $(tt).val();
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminArticleAction_updateArticleStatus.do',
			data:{
				'status':status,
				'articleId':id
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		alert("修改成功!");
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function searchGoods(){
		var kw = $('#searchKeyword').val();
		var status = $('#articleFormStatus').val();
		if(kw!=null&&kw!=''){
			$('#pageSize').val('1');
		}
		var url = '/b2b2cbak/apiAdmin/AdminArticleAction_articleList.do?pageSize='+$('#pageSize').val();
		if(kw!=null&&kw!=''){
			url = url+'&adminSearchForm.title='+kw;
		}
		if(status!=113){
			url = url+'&adminSearchForm.status='+status;
		}
		url = encodeURI(encodeURI(url));
		window.location.href= url;
	}
</script>