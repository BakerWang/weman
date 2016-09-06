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
		<span style="float: right;height:28px;"> 
			<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
			<a href="javascript:void(0)" class="b_fr" onclick="searchGoods()">搜索</a>
		</span>
		<select id="typeSel" style="float:right; height:30px;margin-right:15px;">
			<option value="111">所有类型</option>
			<option value="1">关注好友</option>
			<option value="2">收藏主题</option>
			<option value="3">收藏商品</option>
			<option value="4">赞社交</option>
		</select>
	</div>
	<div style="background: #d7d7d7 none repeat scroll 0 0;margin-top:10px;">
		<div style="width:auto;font-size: 12px; border-bottom: 1px solid #ccc;border-top: 1px solid #ccc;cursor: default;">
			<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<tr class="datagrid-header"><td style="border-left: 1px solid #ccc;">id</td><td>动作类型</td><td>用户名</td><td>data名字</td><td>创建时间</td>
<!-- 				<td>删除</td> -->
				</tr>
				<s:iterator value="#request.page.result" var="userActionObj">
					<tr class="divTr" style="height:100px" >
					<td style="border-left: 1px solid #ccc;">${userActionObj.id } </td>
					<td>
						<s:if test="#userActionObj.type==1">添加关注</s:if>
						<s:elseif test="#userActionObj.type==2">收藏主题</s:elseif>
						<s:elseif test="#userActionObj.type==3">收藏商品</s:elseif>
						<s:elseif test="#userActionObj.type==4">赞社交   (赞数：${userActionObj.love_count })</s:elseif>
					</td>
					<td>${userActionObj.username }</td>
					<td>${userActionObj.dataName }  (${userActionObj.data_id })</td>
					<td>
						<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${userActionObj.create_time }"/>
					</td>
<!-- 					<td> -->
<%-- 						<a style="text-decoration: none;" class="b_fr" onclick="updateTheme(this,${userActionObj.id})" href="javascript:void(0);">删除</a> --%>
<!-- 					</td> -->
					</tr>
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
	function updateTheme(tt,tid){
		var $this = $(tt);
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminArticleAction_updateUserAction.do',
			data:{
				'userActionId':tid,
				'status':'-1'
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
	function searchGoods(){
		var kw = $('#searchKeyword').val();
		var type = $('#typeSel').val();
		var url = '/b2b2cbak/apiAdmin/AdminArticleAction_userActionList.do?page.pageSize=1';
		if(kw!=null&&kw!=''){
			url = url+'&adminSearchForm.username='+kw;
		}
		if(type!='111'){
			url = url+'&adminSearchForm.title='+type;
		}
		url = encodeURI(encodeURI(url));
		window.location.href= url;
	}
</script>