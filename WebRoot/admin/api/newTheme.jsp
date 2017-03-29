<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<style type="text/css">
body{
	font-size: 12px;
	color: red;
	margin:0px;
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
<div style="background-color: white; padding: 15px 0 8px 10px;margin:0px;position: relative;overflow: hidden;">
	<form id="addFormTotal"  enctype="multipart/form-data"  method="post" style="width:52%;float:left;">
		<table style="width:98%;font-size:14px;color:black;table-layout:fixed;" id="addThemeTable">
			<tr height="38px"><td class="spanFront">主题图片 : </td><td width="480px"><input id="themeImageFile" type="file" name="themeFile"/></td>
			<td width="140px">上架：<input class="easyui-datetimebox" name="startTime" style="width: 90px;height: 28px;" id="start_time" data-options="buttons:buttons" /></td></tr>
			<tr height="38px"><td class="spanFront">主题副图 : </td><td width="480px"><input id="themeImageFile2" type="file" name="themeFile2"/></td><td></td></tr>
			<tr height="38px"><td class="spanFront">主题主标题 : </td><td><input class="inputFront" name="theme.title" /></td>
			<tr height="38px"><td class="spanFront">主题副标题 : </td><td><input class="inputFront" name="theme.title2" /></td>
			<td></td></tr>
			<tr height="38px"><td class="spanFront">主题作者 : </td><td>
				<select name="theme.author">
					<option value="1">路姐姐</option>
					<option value="2">路姐姐2</option>
					<option value="3">路姐姐3</option>
				</select>
			</td></tr>
			<tr height="38px"><td class="spanFront">主题日期 : </td><td><input class="inputFront" name="theme.showDate" /></td><td></td></tr>
			<tr height="38px"><td class="spanFront">主题描述 : </td><td><input class="inputFront" name="theme.details" /></td>
				<td>
					主题的风格：<select name="theme.contentStyle"><option value="default">默认</option><option value="topic">专题</option></select>
					主题描述位置：<select name="theme.detailsPosition"><option value="center">中</option><option value="left">左</option></select>
					商品描述位置：<select name="theme.productPosition"><option value="center">中</option><option value="left">左</option></select>
				</td></tr>
<!-- 			<tr height="38px"><td class="spanFront">主题内容 : </td> -->
<!-- 			<td class="themeContentTd">图片：<img width="100px" src="" height="80px" />(图片宽度：16,图片高度：center)</td></tr> -->
		</table>
	</form>
	<div style="width:1px;float:left;background: gray;min-height:250px"></div>
	<div style="float:left;margin-left:3%;width:40%">
		<div style="width:100%;text-align: center;font-size:16px;color:black;">
			添加主题内容&nbsp;&nbsp;||&nbsp;&nbsp;类型：<select onchange="selType(this)">
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
					文字是否加粗：<select id="fontWeight">
								<option value="no">正常</option>
								<option value="yes">加粗</option>
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
					<input type="button" value="添加" style="width:60px;" onclick="addContent(1)"/>
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
						<input type="button" value="添加" style="width:60px;" onclick="addContent(2)"/>
					</div>
				</form>
			</div>
			<div id="productDiv" style="width:100%;display:none;">
				<div style="width:100%; text-align: center;float:left;margin-left:3%;">
					<div style="width:100%;height:30px;margin-top:15px;">
						<div style="height:26px;margin:0 auto;"> 
							<input id="searchKeyword" class="mr5" type="text" value="" size="30"	placeholder="请输入模糊关键字" name="searchKeyWord"> 
							<a href="javascript:void(0)" class="b_fr" style="padding: 0 20px;" onclick="searchGoods()">搜索</a>
						</div>
					</div>
					<div style="margin-top:7px;overflow: hidden;min-height: 100px;">
						<div style="border:1px #ccc solid;margin-top:5px;">
							<table style="width:100%;font-size: 12px; " cellspacing="0" cellpadding="0" border="0">
								<thead><tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;">商品名字</td><td>出售价格</td><td>图片</td><td>浏览数</td><td>操作</td></tr></thead>
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
<div class="buttonWrap">
	<a href="javascript:;" onclick="submitForm()" class="easyui-linkbutton l-btn" id="searchAdvance" group="">保存</a>
</div>
<script>
var buttons = $.extend([], $.fn.datetimebox.defaults.buttons);
buttons.splice(1, 0, {
text: '清空',
handler: function(target){
	 $('#start_time').datetimebox('setValue',"");
}
});

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
	function addContent(type){
		var position = $('.themeContentTd').length;
		var tdContent = '&nbsp;';
		if(position==0){
			postion=1;
			tdContent = '主题内容';
		}else{
			position= position+1;
		}
		if(type==1){
			var fontSize= $('#fontSize').val();
			var center=$('#center').val();
			var content=$('#content').val();
			var fontWeight=$('#fontWeight').val();
			type = 'text';
			var divhtml='<tr height="38px"><td class="spanFront">'+tdContent+'</td><td style="max-width:530px;word-wrap:break-word;" class="themeContentTd" type="'+type+'" position="'+position+'" fontWeight="'+fontWeight+'" fontSize="'+fontSize+'" center="'+center+'" content="'+content+'" >文字：'+content+'&nbsp;&nbsp;(文字大小：'+fontSize+',文字位置：'+center+')</td><td><button onclick="removeTr(this)">删除</button></td></tr>';
			$('#addThemeTable').append(divhtml);
		}else if(type == 2){
			var image = '../statics/'+upImage;
			var img = new Image();
			img.src = image;
			var iwidth = img.width;
			var iheight = img.height;
			type = 'image';
			var divhtml='<tr height="38px"><td class="spanFront">'+tdContent+'</td><td class="themeContentTd" type="'+type+'" image="'+upImage+'" imageWidth="'+iwidth+'" imageHeight="'+iheight+'" position="'+position+'">图片：<img width="100px" src="'+image+'" height="80px" />&nbsp;&nbsp;(图片宽度：'+iwidth+'px,图片高度：'+iheight+'px)</td><td><button onclick="removeTr(this)">删除</button></td></tr>';
			$('#addThemeTable').append(divhtml);
		}else if(type == 3){//video
			var image = '../statics/'+upImage;
			var img = new Image();
			img.src = image;
			var iwidth = img.width;
			var iheight = img.height;
			type = 'video';
			var videoUrl = $('#videoUrlInput').val();
			var divhtml='<tr height="38px"><td class="spanFront">'+tdContent+'</td><td class="themeContentTd" videoUrl="'+videoUrl+'" type="'+type+'" image="'+upImage+'" imageWidth="'+iwidth+'" imageHeight="'+iheight+'" position="'+position+'">视频：<img width="100px" src="'+image+'" height="80px" />&nbsp;&nbsp;(图片宽度：'+iwidth+'px,图片高度：'+iheight+'px)</td><td><button onclick="removeTr(this)">删除</button></td></tr>';
			$('#addThemeTable').append(divhtml);
		}else if(type == 4){
			
		}else if(type == 5){//product
			var divhtml='<tr height="38px"><td class="spanFront">'+tdContent+'</td><td class="themeContentTd" type="defaultImage" position="'+position+'" >向上的箭头</td><td><button onclick="removeTr(this)">删除</button></td></tr>';
			$('#addThemeTable').append(divhtml);
		}
	}
	function removeTr(tt){
		$(tt).parent().parent().remove();
	}
	function addProductForTheme(pid,tt,type){
		var position = $('.themeContentTd').length;
		var tdContent = '&nbsp;';
		if(position==0){
			postion=1;
			tdContent = '主题内容';
		}else{
			position= position+1;
		}
		var pname = $(tt).parent('td').parent('tr').find('.pnamea').html();
		var typeStr = '显示品牌';
		if(type==2){
			typeStr='不显示品牌';
		}
		var divhtml='<tr height="38px"><td class="spanFront">'+tdContent+'</td><td class="themeContentTd" type="product" productId="'+pid+'" position="'+position+'" showType="'+type+'">商品："'+pname+'"('+typeStr+')</td><button onclick="removeTr(this)">删除</button></td></tr>';
		$('#addThemeTable').append(divhtml);
	}
	function submitForm() {
			if($('.themeContentTd').length==0){
				alert('主题内容不能为空！');
				return;
			}
			if($('#themeImageFile').val()==''||$('#themeImageFile2').val()==''){
				alert('主题图片不能为空！');
				return;
			}
			var contentJson = [];
			for(var i=0 ;i<$('.themeContentTd').length;i++){
				var type = $($('.themeContentTd')[i]).attr('type');
				var position = $($('.themeContentTd')[i]).attr('position');
				if(type=='text'){
					var textJson = new Object();
					textJson['type'] = type;
					textJson.position = position;
					textJson.fontSize = $($('.themeContentTd')[i]).attr('fontSize');
					textJson.fontWeight = $($('.themeContentTd')[i]).attr('fontWeight');
					textJson.center = $($('.themeContentTd')[i]).attr('center');
					textJson.content = $($('.themeContentTd')[i]).attr('content');
					contentJson.push(textJson);
				}else if(type=='image'){
					var imageJson = new Object();
					imageJson.type = type;
					imageJson.position = position;
					imageJson.image = $($('.themeContentTd')[i]).attr('image');
					imageJson.width = $($('.themeContentTd')[i]).attr('imageWidth');
					imageJson.height = $($('.themeContentTd')[i]).attr('imageHeight');
					contentJson.push(imageJson);
				}else if(type=='product'){
					var productJson = new Object();
					productJson.type = type ;
					productJson.position = position;
					productJson.showType = $($('.themeContentTd')[i]).attr('showType');
					productJson.productId  = $($('.themeContentTd')[i]).attr('productId');
					contentJson.push(productJson);
				}else if(type=='video'){
					var videoJson = new Object();
					videoJson.type=type;
					videoJson.position= position;
					videoJson.image = $($('.themeContentTd')[i]).attr('image');
					videoJson.width = $($('.themeContentTd')[i]).attr('imageWidth');
					videoJson.height = $($('.themeContentTd')[i]).attr('imageHeight');
					videoJson.content = $($('.themeContentTd')[i]).attr('videoUrl');
					contentJson.push(videoJson);
				}else if(type=='defaultImage'){
					var videoJson = new Object();
					videoJson.type=type;
					videoJson.position= position;
					contentJson.push(videoJson);
				}
			}
			var contentStr = JSON.stringify(contentJson);
			contentStr = contentStr.replace(/"/g,"'");
			console.log(contentStr.toString());
			$.Loading.show("正在添加......");
			var options = {
				url : "/b2b2cbak/apiAdmin/AdminProductAction_saveTheme.do",
				type : "POST",
				data : {
					"contentArray":contentStr.toString()
				},
				dataType : 'json',
				success : function(data) {
					if (data.result == "yes") {
						parent.addTab1("主题列表",
								"/b2b2cbak/apiAdmin/AdminProductAction_getThemeList.do");
						parent.CloseTabByTitle("主题添加");
					}else{
						alert("出现错误 ，请重试"+e);
					}
					if (data.result == 0) {
						$.Loading.error(data.message);
					}
				},
				error : function(e) {
					alert("出现错误 ，请重试"+e);
				}
			};
			$("#addFormTotal").ajaxSubmit(options);
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
		    			divhtml=divhtml+'<tr class="datagrid-header"><td  style="border-left: 1px solid #ccc;"><a target="_blank" href="../goods-'+pd.pid+'.html" class="pnamea">'+pd.pname+'</a></td><td>'+pd.pprice+'</td>';
		    			divhtml=divhtml+'<td><img src="/b2b2cbak/statics/'+pd.pimage+'" width="50px" height="50px"/> </td><td>'+pd.pviewCount+'</td>';
		    			divhtml=divhtml+'<td><a class="b_fr" onclick="addProductForTheme('+pd.pid+',this,1)" href="javascript:void(0);">添加(显示品牌)</a></td>';
		    			divhtml=divhtml+'<td><a class="b_fr" onclick="addProductForTheme('+pd.pid+',this,2)" href="javascript:void(0);">添加(不显示品牌)</a></td>';
		    			divhtml=divhtml+'</tr>';
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
	$(document).ready(function(){
		catchProduct(1,'');
	});
</script>