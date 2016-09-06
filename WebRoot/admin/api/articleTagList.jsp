<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="../admin/api/js/fancybox/jquery.fancybox-1.3.4.pack.js"></script>
<link rel="stylesheet" type="text/css" href="../admin/api/js/fancybox/jquery.fancybox-1.3.4.css"/>
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
<div class='buttonArea'>
	<div style="height:30px;margin-top:4px;">
		<span style="float:left;height:28px;">
			<a id="divdia" href="#divdiaDiv" class="b_fr" >新建</a>
		</span>
		<span style="float: right;height:28px;"> 
			<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
			<a href="javascript:void(0)" class="b_fr" onclick="searchGoods()">搜索</a>
		</span>
	</div>
	<div style="background: #d7d7d7 none repeat scroll 0 0;margin-top:10px;">
		<div style="width:auto;font-size: 12px; border-bottom: 1px solid #ccc;border-top: 1px solid #ccc;cursor: default;">
			<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<tr class="datagrid-header"><td style="border-left: 1px solid #ccc;">id</td><td>标签名字</td><td>创建者</td><td>发文数</td><td>创建时间</td><td>操作</td></tr>
				<s:iterator value="#request.page.result" var="articleTagObj">
					<tr class="divTr" >
					<td style="border-left: 1px solid #ccc;">${articleTagObj.id } </td>
					<td>${articleTagObj.content }</td>
					<td><s:if test="#articleTagObj.username==null"><span style="color:red;">系统创建</span></s:if><s:else>${articleTagObj.username }</s:else> </td>
					<td>${articleTagObj.articleCount }</td>
					<td>
						<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${articleTagObj.createTime }"/>
					</td>
					<td>
						<select onchange="updateArticleTagStatusSel(${articleTagObj.id },this)">
							<option <s:if test="#articleTagObj.status==-1">selected="selected"</s:if> value="-1">不能用</option>
							<option <s:if test="#articleTagObj.status==1">selected="selected"</s:if> value="1">正在用</option>
						</select>
						<select onchange="updateArticleTagTypeSel(${articleTagObj.id },this)" <s:if test="#articleTagObj.type==1">style="color:red;"</s:if>>
							<option <s:if test="#articleTagObj.type==-1">selected="selected"</s:if> value="-1">不是hot</option>
							<option <s:if test="#articleTagObj.type==1">selected="selected"</s:if> value="1">是hot</option>
						</select>
					</td></tr>
				</s:iterator>
			</table>
		</div>
	</div>
</div>
<div style="display:none;">
	<div id="divdiaDiv" style="margin:auto;text-align:center;" title="用户标签添加">
	    <div style="margin-top:5px;">
	    	<span>标签名字：</span><input type="text" id="tagname" value="" placeholder="输入添加的标签名字.." />
	    </div>
	    <div style="margin-top:10px;">
	    	<button onclick="createArticleTag()">添加</button>
	    </div>
	</div>
</div>
<div class="pagelist">
	<div style="line-height:46px;height:46px;width:150px;float:left"><span>共 ${page.totalPageCount} 页/${page.totalCount}条记录 </span></div>
	<div id="adminUserManagePagination" class="pagination" style="float:left;margin-top:15px"></div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$("#divdia").fancybox({
			'width'				: '100%',
			'height'			: '100%',
			'scrolling'         :'no',
			'autoScale'			: true
		});
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
	function createArticleTag() {
		var tagName=$('#tagname').val();
		if(tagName==null||tagName==''){
			return;
		}
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminArticleAction_saveArticleTag.do',
			data:{
				'content':tagName,
				'memberId':0
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		alert("添加成功！");
		    		$.fancybox.close();
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function updateArticleTagStatusSel(id,tt){
		var status = $(tt).val();
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminArticleAction_updateArticleTagStatus.do',
			data:{
				'status':status,
				'articleTagId':id
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
		if(kw==null||kw==''){
			return;
		}
		var url = '/b2b2cbak/apiAdmin/AdminArticleAction_articleList.do?page.pageSize=1&adminSearchForm.title='+kw;
		url = encodeURI(encodeURI(url));
		window.location.href= url;
	}
	function updateArticleTagTypeSel(id,tt){
		var type = $(tt).val();
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminArticleAction_updateArticleTagType.do',
			data:{
				'type':type,
				'articleTagId':id
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
</script>