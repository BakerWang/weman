<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
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
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<link rel="stylesheet" type="text/css" href="/b2b2cbak/adminthemes/new/js/easy-ui/themes/gray/easyui.css"/>    
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="../adminthemes/new/js/jquery.pagination.js"></script>
<link href="../adminthemes/new/css/myPagination.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
var upImage = '';
function  ajaxFileUpload(i){
	var options = {
			url : "AdminUploadAction_uploadImage.do",
			type : "POST",
			dataType : 'json',
			success : function(data) {
				if (data.result == "yes") {
					$('#uploadImage'+i).attr('src','../statics/'+data.fileName)
					upImage = data.fileName;
					console.log(data.fileName+".......");
				}
				if (data.result == 0) {
					$.Loading.error(data.message);
				}
			},
			error : function(e) {
				alert("出现错误 ，请重试"+e);
			}
		};
		$("#addForm"+i).ajaxSubmit(options);
}
</script>
<link href="../adminthemes/new/css/myPagination.css" rel="stylesheet" type="text/css" />
<div style=" height: 32px;width:100%;">
	<ul style="list-style-type: none;">
			<li class="active" tabid="3" onclick="onTheme(this)">编辑主题</li>
			<li tabid="5" class="" onclick="onTheme(this)">主题内容</li>
	</ul>
</div>
<input type="hidden" value="${theme.id }" id="themeid"/>
<div id="div3" style="background-color: white; padding: 15px 0 8px 10px;margin:0px;position: relative;">
	<form id="addForm"  enctype="multipart/form-data"  method="post">
		<input type="hidden" value="${theme.id }" name="themeId"/>
		<input type="hidden" value="theme" name="type"/>
		<table style="width:98%;">
			<tr height="38px"><td class="spanFront">主题图片 : </td><td ><img src="/b2b2cbak/statics/${theme.image }" width="100px" height="100px"/><input id="themeImageFile" type="file" name="themeFile"/></td>
			<td>上架：<input class="easyui-datetimebox" name="startTime" style="width: 150px;height: 28px;" value="<cc:dateFormat format="yyyy-MM-dd HH:mm" time="${theme.startTime }"/>" id="start_time" data-options="buttons:buttons" /></td></tr>
			<tr height="38px"><td class="spanFront">主题副图 : </td><td ><img src="/b2b2cbak/statics/${theme.minorImage }" width="100px" height="100px"/><input id="themeImageFile2" type="file" name="themeFile2"/></td>
			<td>
				主题描述位置：<select name="detailsPosition"><option <s:if test="#request.theme.detailsPosition=='center'">selected="selected"</s:if> value="center">中</option><option <s:if test="#request.theme.detailsPosition=='left'">selected="selected"</s:if> value="left">左</option></select>
			</td></tr>
			<tr height="38px"><td class="spanFront">主题标题 : </td><td><input class="inputFront" value="${theme.title }" name="title" /></td>
			<td>
				商品描述位置：<select name="productPosition"><option <s:if test="#request.theme.productPosition=='center'">selected="selected"</s:if> value="center">中</option><option <s:if test="#request.theme.productPosition=='left'">selected="selected"</s:if> value="left">左</option></select>
			</td></tr>
			<tr height="38px"><td class="spanFront">主题日期 : </td><td><input class="inputFront" value="${theme.showDate }" name="showDate" /></td><td></td></tr>
			<tr height="38px"><td class="spanFront">主题描述 : </td><td><input class="inputFront" value="${theme.details }" name="details" /></td>
		</table>
	</form>
	<div class="buttonWrap">
		<a href="javascript:;" onclick="submitForm()" class="easyui-linkbutton l-btn" id="searchAdvance" group="">更改</a>
	</div>
</div>
<div id="div5" style="display:none;overflow:hidden;background-color: white; padding: 15px 0 8px 10px;margin:0px;position: relative;margin-top:1px;">
	<div style="width:48%;float:left;">
		<table style="width:98%;font-size:14px;color:black;table-layout:fixed;" id="addThemeTable">
			<s:iterator value="#request.theme.themeContent" var="tc">
				<tr height="38px"><td class="spanFront" style="width:80px;">主题内容 : </td>
				<s:if test="#tc.type=='text'">
					<td width="530px" class="themeContentTd" type="${tc.type}" position="${tc.postion}" fontSize="${tc.fontSize }" center="${tc.center }" content="${tc.content }" >文字：${tc.content }&nbsp;&nbsp;(文字大小：${tc.fontSize },文字位置：${tc.center })</td>
					<td><button onclick="updateTr(this,${tc.id})">修改</button></td>
				</s:if><s:elseif test="#tc.type=='image'">
					<td width="530px" class="themeContentTd" type="${tc.type}" image="${tc.image }" imageWidth="${tc.imagewidth}" imageHeight="${tc.imageheight}" position="${tc.postion}">图片：<img width="100px" src="/b2b2cbak/statics/${tc.image }" height="80px" />&nbsp;&nbsp;(图片宽度：${tc.imagewidth }px,图片高度：${tc.imageheight }px)</td>
					<td><button onclick="updateTr(this,${tc.id})">修改</button></td>
				</s:elseif><s:elseif test="#tc.type=='video'">
					<td width="530px" class="themeContentTd" videoUrl="${tc.content }" type="${tc.type}" image="${tc.image }" imageWidth="${tc.imagewidth}" imageHeight="${tc.imageheight}" position="${tc.postion}">视频：<img width="100px" src="/b2b2cbak/statics/${tc.image }" height="80px" />&nbsp;&nbsp;(图片宽度：${tc.imagewidth }px,图片高度：${tc.imageheight }px)</td>
					<td><button tcid="${tc.id }" onclick="updateContent('product',this)">删除</button></td>
				</s:elseif><s:elseif test="#tc.type=='product'">
					<td class="themeContentTd" type="product" productId="${tc.goods_id }" position="${tc.postion }" showType="${tc.type }">商品："${tc.productName }"</td>
					<td>
						<s:if test="#tc.isIndexShow==-1">
							<button tcid="${tc.id }" onclick="updateContentIndex(this,1)">首页显示</button>
						</s:if><s:else>
							<button tcid="${tc.id }" onclick="updateContentIndex(this,-1)">首页不显示</button>
						</s:else>
						<button tcid="${tc.id }" onclick="updateContent('product',this)">删除</button>
					</td>
				</s:elseif><s:elseif test="#tc.type=='defaultImage'">
					<td class="themeContentTd" type="defaultImage" position="${tc.postion}" >向上的箭头</td>
					<td><button tcid="${tc.id }" onclick="removeTr(this)">删除</button></td>
				</s:elseif>
			</s:iterator>
		</table>
	</div>
	<div style="width:1px;float:left;background: gray;min-height:250px"></div>
	<div style="float:left;margin-left:10px;width:48%">
		<div style="width:100%;text-align: center;font-size:16px;color:black;">
			添加主题内容&nbsp;&nbsp;||&nbsp;&nbsp;类型：<select id="selectType" onchange="selType(this)">
						<option value="1">文字</option>
						<option value="2">图片</option>
						<option value="3">商品</option>
						<option value="4">视频</option>
						<option value="5">向上箭头</option>
					</select>
		</div>
		<div style="width:100%;font-size:15px;color: black;">
			<div id="wenziDiv" style="width:100%;">
				<div style="width:100%;margin-top:15px;">
					文字大小：<select id="fontSize">
								<option value="16">16</option>
								<option value="14">14</option>
								<option value="15">15</option>
								<option value="17">17</option>
								<option value="18">18</option>
							</select>
				</div>
				<div style="width:100%;margin-top:10px;">
					文字位置：<select id="center">
								<option value="center">center</option>
								<option value="left">left</option>
								<option value="right">right</option>
							</select>
				</div>
				<div style="width:100%;margin-top:10px;">
					<span style="line-height:20px;">文字内容：</span><textarea  id="content" rows="3" cols="50"></textarea>
				</div>
				<div style="width:100%;margin-top:10px; text-align: center;">
					<input type="button" class="updateC" value="修改" style="width:60px;" tcid="0" onclick="updateContent('text',this)"/>
				</div>
			</div>
			<div id="imageDiv" style="width:100%;display:none;">
				<form id="addForm1"  enctype="multipart/form-data"  method="post">
					<div style="width:100%;overflow: hidden;">
						<div style="float:left;">图片上传:</div>
						<div style="width:150px;float:left;margin-left:20px;max-height:320px;background: gray;border: 2px darkturquoise solid;border-radius:3px;padding:5px;overflow: hidden;">
							<img id="uploadImage1" src="../admin/api/js/addPic.jpg" width="150px" style="cursor: pointer;" onclick="$('#file1').click();"/>
							<input type="file" onchange="ajaxFileUpload(1);" id="file1" name="file" style="width:100px;position: absolute;z-index:-1;top:10px;left:0px;height:320px;bottom: 10px;"/>
						</div>
					</div>
					<div style="width:100%;margin-top:10px; text-align: center;">
						<input type="button" class="updateC" value="修改" style="width:60px;" tcid="0" onclick="updateContent('image',this)"/>
					</div>
				</form>
			</div>
			<div id="productDiv" style="width:100%;display:none;">
				<div style="width:100%; text-align: center;float:left;">
					<div style="width:100%;height:30px;margin-top:15px;">
						<div style="height:26px;margin:0 auto;"> 
							<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
							<a href="javascript:void(0)" class="b_fr" style="padding: 0 20px;width:32px;" onclick="searchGoods()">搜索</a>
						</div>
					</div>
					<div style="margin-top:7px;overflow: hidden;min-height: 100px;">
						<div style="border:1px #ccc solid;margin-top:5px;">
							<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
								<thead><tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;">商品名字</td><td>出售价格</td><td>图片</td><td>浏览数</td><td>操作</td><td>操作</td></tr></thead>
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
			<div style="display:none;" id="videoDiv">
				<form id="addForm2"  enctype="multipart/form-data"  method="post">
					<div style="width:100%;overflow: hidden;">
						<div style="float:left;">图片上传:</div>
						<div style="width:150px;float:left;margin-left:20px;max-height:320px;background: gray;border: 2px darkturquoise solid;border-radius:3px;padding:5px;overflow: hidden;">
							<img id="uploadImage2" src="../admin/api/js/addPic.jpg" width="150px" style="cursor: pointer;" onclick="$('#file2').click();"/>
							<input type="file" onchange="ajaxFileUpload(2);" id="file2" name="file" style="width:100px;position: absolute;z-index:-1;top:10px;left:0px;height:320px;bottom: 10px;"/>
						</div>
					</div>
					<div style="width:100%;">
						视频URL：<input type="text" id="videoUrlInput" />
					</div>
					<div style="width:100%;margin-top:10px; text-align: center;">
						<input type="button" value="添加" style="width:60px;" onclick="addContent(3)"/>
					</div>
				</form>
			</div>
			<div style="display:none;" id="defaultImageDiv">
				<div style="width:100%;margin-top:10px; text-align: center;">
						<input type="button" value="添加" style="width:60px;" onclick="addContent(5)"/>
					</div>
			</div>
		</div>
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
function updateContentIndex(tt,status){
	var tcid = $(tt).attr('tcid');
	$.ajax({
		type:'POST',
		url:'/b2b2cbak/apiAdmin/AdminProductAction_updateThemeContent.do',
		data:{
			'tcid':tcid,
			'isIndexShow':status,
			'type':'product'
		},
		dataType:'json',
	    success: function(msg){
	    	if(msg.result=='yes'){
    			alert('设置成功！');
    			//$(tt).parent().parent().remove();
	    	}
	    }
	});
}
function updateContent(type,tt){
	var tcid = $(tt).attr('tcid');
	var fontSize=null;
	var center=null;
	var content=null;
	var image = null;
	if(type=='text'){
		fontSize= $('#fontSize').val();
		center=$('#center').val();
		content=$('#content').val();
	}else if(type=='image'){
		image = upImage;
	}
	$.ajax({
		type:'POST',
		url:'/b2b2cbak/apiAdmin/AdminProductAction_updateThemeContent.do',
		data:{
			'tcid':tcid,
			'type':type,
			'fontSize':fontSize,
			'center':center,
			'content':content,
			'image':image
		},
		dataType:'json',
	    success: function(msg){
	    	if(msg.result=='yes'){
	    		if(type=='product'){
	    			alert('删除成功！');
	    			$(tt).parent().parent().remove();
	    		}else{
	    			alert('更改成功！');
	    		}
	    	}
	    }
	});
}
function updateTr(tt,tcid){
	var $this = $($(tt).parent().parent().find('.themeContentTd')[0]);
	var type = $this.attr('type');
	if(type =='text'){
		$('#imageDiv').hide();
		$('#productDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').hide();
		$('#wenziDiv').show();
		$("#selectType").find("option[value='1']").attr("selected",true);
		$('#content').val($this.attr('content'));
		$("#fontSize").find("option[value='"+$this.attr('fontSize')+"']").attr("selected",true);
		$("#center").find("option[value='"+$this.attr('center')+"']").attr("selected",true);
		$($('#wenziDiv').find('.updateC')[0]).attr('tcid',tcid);
	}else if(type =='image'){
		$('#wenziDiv').hide();
		$('#productDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').hide();
		$('#imageDiv').show();
		$("#selectType").find("option[value='2']").attr("selected",true);
		$('#uploadImage1').attr('src',$this.attr('image'));
		$($('#imageDiv').find('.updateC')[0]).attr('tcid',tcid);
	}else if(type =='product'){
		$('#wenziDiv').hide();
		$('#imageDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').hide();
		$('#productDiv').show();
	}else if(type == 'video'){
		$('#wenziDiv').hide();
		$('#imageDiv').hide();
		$('#productDiv').hide();
		$('#defaultImageDiv').hide();
		$('#videoDiv').show();
	}else{
		$('#wenziDiv').hide();
		$('#imageDiv').hide();
		$('#productDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').show();
	}
}

function selType(tt){
	var type = $(tt).val();
	if(type ==1){
		$('#imageDiv').hide();
		$('#productDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').hide();
		$('#wenziDiv').show();
	}else if(type ==2){
		$('#wenziDiv').hide();
		$('#productDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').hide();
		$('#imageDiv').show();
	}else if(type ==3){
		$('#wenziDiv').hide();
		$('#imageDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').hide();
		$('#productDiv').show();
	}else if(type == 4){
		$('#wenziDiv').hide();
		$('#imageDiv').hide();
		$('#productDiv').hide();
		$('#defaultImageDiv').hide();
		$('#videoDiv').show();
	}else{
		$('#wenziDiv').hide();
		$('#imageDiv').hide();
		$('#productDiv').hide();
		$('#videoDiv').hide();
		$('#defaultImageDiv').show();
	}
}
	function onTheme(tt){
		var index = $(tt).attr('tabid');
		$('[id^=div]').hide();
		$('#div'+index).show();
		$(tt).parent().children('li').removeClass('active');
		$(tt).addClass('active');
	}
	function addProductForTheme(pid,tt,type){
		var position = $('.themeContentTd').length;
		var tdContent = '主题内容';
		if(position==0){
			postion=1;
		}else{
			position= position+1;
		}
		var pname = $(tt).parent('td').parent('tr').find('.pnamea').html();
		var typeStr = '显示品牌';
		if(type==2){
			typeStr='不显示品牌';
		}
		var divhtml='<tr height="38px"><td class="spanFront">'+tdContent+'</td><td class="themeContentTd" type="product" productId="'+pid+'" position="'+position+'" showType="'+typeStr+'">商品："'+pname+'"('+typeStr+')</td><button onclick="removeTr(this)">删除</button></td></tr>';
		$('#addThemeTable').append(divhtml);
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
		    			divhtml=divhtml+'<tr class="datagrid-header"><td width="280px" style="border-left: 1px solid #ccc;"><a target="_blank" class="pnamea" href="../goods-${goods.goods_id }.html">'+pd.pname+'</a></td><td>'+pd.pprice+'</td>';
		    			divhtml=divhtml+'<td><img src="'+pd.pimage+'" width="50px" height="50px"/> </td><td>'+pd.pviewCount+'</td>';
		    			divhtml=divhtml+'<td><a class="" onclick="addProductForTheme('+pd.pid+',this,1)" href="javascript:void(0);">添加(显示品牌)</a></td>';
		    			divhtml=divhtml+'<td><a class=" b_fr2" onclick="addProductForTheme('+pd.pid+',this,2)" href="javascript:void(0);">添加(不显示品牌)</a></td></tr>';
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
				url : "/b2b2cbak/apiAdmin/AdminProductAction_updateThemeContent.do",
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if (data.result == "yes") {
						alert("修改成功......");
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