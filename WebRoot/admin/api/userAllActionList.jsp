<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../adminthemes/new/js/jquery.pagination.js"></script>
<link href="../adminthemes/new/css/myPagination.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="/b2b2cbak/adminthemes/new/js/easy-ui/themes/gray/easyui.css"/>
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
		<form id="searchForm" action="/b2b2cbak/apiAdmin/AdminUserActionAction_getUserAction.do" id="searchForm" method="post">
			<span style="float: right;height:28px;"> 
				<input type="hidden" value="1" name="pageNo" id="goodsPage"/>
				<div style="float:left;">
					类型：<select name="contentStyle">
						<option <s:if test="#request.contentStyle=='topic'">selected="selected"</s:if> value="topic">首页（专题）</option>
						<option <s:if test="#request.contentStyle=='default'">selected="selected"</s:if> value="default">发现（主题）</option>
						<option <s:if test="#request.contentStyle=='product'">selected="selected"</s:if> value="product">商品详情页</option>
						<option <s:if test="#request.contentStyle=='article'">selected="selected"</s:if> value="article">社交详情页</option>
						<option <s:if test="#request.contentStyle=='banner'">selected="selected"</s:if> value="banner">banner页</option>
					</select>
				</div>
				<span style="float:left;height:28px;margin-left:15px;">
					主题上架时间 : <input class="easyui-datebox" name="startTime" value="${startTime }" style="width: 130px;height: 28px;" id="start_time" data-options="buttons:buttons" />
					<input class="easyui-datebox" name="endTime" value="${endTime }" style="width: 130px;height: 28px;" id="end_time" data-options="buttons:buttons" />
				</span>
				<input id="searchKeyword" name="keywords" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字"> 
				<a href="javascript:void(0)" class="b_fr" onclick="searchGoods()">搜索</a>
			</span>
		</form>
	</div>
	<div style="background: #d7d7d7 none repeat scroll 0 0;margin-top:10px;">
		<div style="width:auto;font-size: 12px; border-bottom: 1px solid #ccc;border-top: 1px solid #ccc;cursor: default;">
			<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
				<tr class="datagrid-header"><td style="border-left: 1px solid #ccc;">主题标题</td><td>主题图片</td><td>上架时间</td><td>创建时间</td>
				<s:if test="#request.contentStyle=='banner'"><td>类型</td></s:if>
				<td>登录用户点击次数</td><td>点击次数(未)</td></tr>
				<s:iterator value="#request.page.result" var="themeObj">
					<s:if test="#request.contentStyle=='product'">
						<tr class="divTr" height="100px" ><td style="border-left: 1px solid #ccc;">
							<a href="${themeObj.url }" style="text-decoration: none;" >${themeObj.name } </a>
						</td><td><img src="/b2b2cbak/statics/${themeObj.original }" width="80px" height="80px"/></td>
						<td>
							<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${themeObj.startTime }"/>
						</td>
						<td>
							<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${themeObj.endTime }"/>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('登录详情${themeObj.goods_id}','/b2b2cbak/apiAdmin/AdminProductAction_userThemeCount.do?themeId=${themeObj.goods_id}&contentStyle=${contentStyle }')">${themeObj.loginClickCount }</a>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('未登录详情${themeObj.goods_id}','/b2b2cbak/apiAdmin/AdminProductAction_noUserThemeCount.do?themeId=${themeObj.goods_id}&contentStyle=${contentStyle }')">${themeObj.nologinClickCount }</a>
						</td>
						</tr>
					</s:if><s:elseif test="#request.contentStyle=='banner'">
						<tr class="divTr" height="100px" ><td style="border-left: 1px solid #ccc;">
							${themeObj.title } 
						</td><td><img src="/b2b2cbak/statics/${themeObj.image }" width="80px" height="80px"/></td>
						<td>
							<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${themeObj.start_time }"/>
						</td>
						<td>
							<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${themeObj.end_time }"/>
						</td>
						<td>
							${themeObj.category }|${themeObj.type }
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('登录详情${themeObj.id}','/b2b2cbak/apiAdmin/AdminProductAction_userThemeCount.do?themeId=${themeObj.id}&contentStyle=${contentStyle }')">
								${themeObj.click_count }
							</a>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('未登录详情${themeObj.id}','/b2b2cbak/apiAdmin/AdminProductAction_noUserThemeCount.do?themeId=${themeObj.id}&contentStyle=${contentStyle }')">
								${themeObj.nologinClickCount }
							</a>
						</td>
						</tr>
					</s:elseif><s:elseif test="#request.contentStyle=='article'">
						<tr class="divTr" height="100px" ><td style="border-left: 1px solid #ccc;">
							${themeObj.content } 
						</td><td><img src="/b2b2cbak/statics/${themeObj.image }" width="80px" height="80px"/></td>
						<td>
							<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${themeObj.create_time }"/>
						</td>
						<td>
							${themeObj.comment_count }
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('登录详情${themeObj.id}','/b2b2cbak/apiAdmin/AdminProductAction_userThemeCount.do?themeId=${themeObj.id}&contentStyle=${contentStyle }')">
									${themeObj.loginClickArticle }
							</a>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('未登录详情${themeObj.id }','/b2b2cbak/apiAdmin/AdminProductAction_noUserThemeCount.do?themeId=${themeObj.id}&contentStyle=${contentStyle }')">
									${themeObj.nologinClickArticle }
							</a>
						</td>
						</tr>
					</s:elseif><s:elseif test="#request.contentStyle=='topic'||#request.contentStyle=='default'">
						<tr class="divTr" height="100px" ><td style="border-left: 1px solid #ccc;">
							<a href="javascript:void(0)" style="text-decoration: none;" onclick="parent.addTab1('主题浏览','/b2b2cbak/apiAdmin/ShareAction_getDataDetails.do?type=theme&dataId=${themeObj.theme.id}')">${themeObj.theme.title } </a>
						</td><td><img src="/b2b2cbak/statics/${themeObj.theme.image }" width="80px" height="80px"/></td>
						<td>
							<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${themeObj.theme.startTime }"/>
						</td>
						<td>
							<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${themeObj.theme.create_time }"/>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('登录详情${themeObj.theme.id}','/b2b2cbak/apiAdmin/AdminProductAction_userThemeCount.do?themeId=${themeObj.theme.id}&contentStyle=${contentStyle }')">${themeObj.theme.loginClickCount }</a>
						</td>
						<td>
							<a href="javascript:void(0)" onclick="parent.addTab1('未登录详情${themeObj.theme.id}','/b2b2cbak/apiAdmin/AdminProductAction_noUserThemeCount.do?themeId=${themeObj.theme.id}&contentStyle=${contentStyle }')">${themeObj.theme.clickCount }</a>
						</td>
						</tr>
					</s:elseif>
				</s:iterator>
			</table>
		</div>
	</div>
</div>
<div class="pagelist">
	<div style="line-height:46px;height:46px;width:180px;float:left"><span>共 ${page.totalPageCount} 页/${page.totalCount}条记录 </span></div>
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
            'current_page'        :'${pageNo-1}',
            'callback'            : function(page_id,jq){
           		var page = parseInt(page_id)+1;
            	$('#goodsPage').val(page);
           		$("#searchForm").submit();
            	//var url = '/b2b2cbak/apiAdmin/AdminProductAction_getThemeList.do?page='+;
            	//parent.addTab1('主题列表',url);
//            		var type = "${type}";
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
		 $('#end_time').datebox('setValue',"");
	}
	});
	function updateLoveCount(tt,id){
		var loveCount =$(tt).parent().find('.themeLoveCount').val();
// 		var loveCount = $('#themeLoveCount').val();
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminProductAction_updateThemeStatus.do',
			data:{
				'themeId':id,
				'loveCount':loveCount
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		alert('修改成功！');
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function updateThemeStatus(tid,tt){
		var statusStr = '';
		if($($(tt).parent('td').find('.statusArray1')[0]).attr("checked")){
			statusStr=statusStr+'1,';
		}else{
			statusStr=statusStr+'0,';
		}
		if($($(tt).parent('td').find('.statusArray2')[0]).attr("checked")){
			statusStr=statusStr+'1,';
		}else{
			statusStr=statusStr+'0,';
		}
		if($($(tt).parent('td').find('.statusArray3')[0]).attr("checked")){
			statusStr=statusStr+'1';
		}else{
			statusStr=statusStr+'0';
		}
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminProductAction_updateThemeStatusObj.do',
			data:{
				'themeId':tid,
				'status':statusStr
			},
			dataType:'json',
		    success: function(msg){
		    	if(msg.result=='yes'){
		    		alert('设置成功!');
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	}
	function updateTheme(tt,tid){
		if(confirm("你确定要删除吗？")){
			var $this = $(tt);
			$.ajax({
				type:'POST',
				url:'/b2b2cbak/apiAdmin/AdminProductAction_deleteTheme.do',
				data:{
					'themeId':tid
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
		
	}
	function tagSel(tt){
		var val=$(tt).val();
		$(tt).parent('div').children('.ccategory').find('option').hide();
		$(tt).parent('div').children('.ccategory').find('[name="'+val+'"]').show();
	}
	function searchGoods(){
		$("#searchForm").submit();
// 		var kw = $('#searchKeyword').val();
// 		if(kw==null||kw==''){
// 			return;
// 		}
// 		var url = '/b2b2cbak/apiAdmin/AdminProductAction_getThemeList.do?page=1&keywords='+kw;
// 		url = encodeURI(encodeURI(url));
// 		window.location.href= url;
	}
	
</script>