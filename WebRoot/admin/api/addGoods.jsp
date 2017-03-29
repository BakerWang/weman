<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<%@taglib prefix="cc" uri="/tcardztaglib"%>
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<link rel="stylesheet" type="text/css" href="/b2b2cbak/adminthemes/new/js/easy-ui/themes/gray/easyui.css"/>    
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/easyui-lang-zh_CN.js"></script>
<style type="text/css">
body {
	font-size: 14px;
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

<div class='buttonArea'>
	<div style="background-color: white; padding: 15px 0 8px 10px; margin: 0px; position: relative; overflow: hidden;">
		<form id="addForm" enctype="multipart/form-data" method="post" style="width: 40%; float: left;">
			 <table>
			 	<tr><td>商品来源：</td><td><select name="productOrigin" onchange="orginalProduct(this)"><option value="taobao">淘宝</option><option value="tmall">天猫</option><option value="youzan">有赞</option><option value="jingdong">京东</option><option value="yamaxun">亚马逊</option><option value="wemantb">weman专场</option> </select> </td></tr>
			 	<tr class="youzan" id="youzanid"><td>有赞的外部编号：</td><td><input name="yzid" style="width:350px;"/></td></tr>
			 	<tr class="noyouzan"><td>上架时间：</td><td><input class="easyui-datebox" name="startTime" style="width: 150px;height: 28px;" value="" id="start_time" data-options="buttons:buttons" /></td></tr>
			 	<tr class="noyouzan"><td>下架时间：</td><td><input class="easyui-datebox" name="endTime" style="width: 150px;height: 28px;" value="" id="end_time" data-options="buttons:buttons" /></td></tr>
			 	<tr class="noyouzan"><td>商品名字：</td><td><input name="title" style="width:350px;" /></td></tr>
			 	<tr class="noyouzan"><td>商品标题：</td><td><input name="title2" style="width:350px;" /></td></tr>
			 	<tr class="noyouzan"><td>购买地址：</td><td><input name="url" style="width:350px;"/></td></tr>
			 	<tr class="noyouzan"><td>店铺名字：</td><td><input name="originalName" style="width:350px;" /></td></tr>
			 	<tr class="youzan"><td>是否有优惠券：</td><td>
			 		<select name="hasCoupon">
			 			<option value="-1">无</option>
			 			<option value="1">有</option>
			 		</select>
			 	</td></tr>
			 	<tr class="noyouzan"><td>商品价格：</td><td><input name="price"/>(纯数字)</td></tr>
			 	<tr class="noyouzan"><td>市场价格：</td><td><input name="mkprice" /></td></tr>
			 	<tr class="noyouzan"><td>商品描述：</td><td><textarea name="details" rows="5" cols="40"></textarea> </td></tr>
			 	<tr class="noyouzan"><td>商品图片：</td><td><input type="file" name="productImage"/></td></tr>
			 	<tr class="youzan"><td>分类和品牌：</td><td>
			 		<select id="productCategory" onchange="selCategory(this)" name="productCategory">
			 		</select>
			 		<select id="productCategory2" onchange="selBrand(this)" name="productCategory2">
			 		</select>
			 		<select id="productBrand" name="productBrand">
			 		</select>
			 	</td></tr>
			 	<tr class="noyouzan"><td>APP里是否显示市场价格：</td><td>
			 		<select name="isShowMKPrice">
						<option value="1">显示</option>
						<option value="-1">不显示</option>			 		
			 		</select>
			 	</td></tr>
			 </table>
			 
		</form>
	</div>
	<div class="buttonWrap">
		<a href="javascript:;" onclick="submitForm()" class="l-btn" id="searchAdvance" ><span class="l-btn-left"><span class="l-btn-text">保存</span></span></a>
	</div>
</div>
<script type="text/javascript">
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
function orginalProduct(tt){
	var val = $(tt).val();
	if(val=='youzan'){
		$('.noyouzan').hide();
		$('.youzan').show();
	}else{
		$('.noyouzan').show();
		$('.youzan').show();
		$('#youzanid').hide();
	}
}

	$(document).ready(function() {
		$.ajax({
			type:'POST',
			url:'/b2b2cbak/apiAdmin/AdminProductAction_addProductJson.do',
			dataType:'json',
		    success: function(msg){
		    	console.log(msg);
		    	if(msg.result=='success'){
		    		var dataList = msg.data;
		    		for(var i=0;i<dataList.length;i++){
		    			var catId = dataList[i].catId;
		    			var catName = dataList[i].catName;
		    			$('#productCategory').append('<option value="'+catId+'">'+catName+'</option>');
		    			var cdata = dataList[i].children;
		    			for(var j=0;j<cdata.length;j++){
		    				var catId2 = cdata[j].catId;
			    			var catName2 = cdata[j].catName;
			    			$('#productCategory2').append('<option parentId="'+catId+'" value="'+catId2+'">'+catName2+'</option>');
		    				var brandData = cdata[j].brandData;
		    				for(var ii=0;ii<brandData.length;ii++){
		    					var bid = brandData[ii].id;
				    			var bname = brandData[ii].name;
				    			$('#productBrand').append('<option categoryId="'+catId2+'" value="'+bid+'">'+bname+'</option>');
		    				}
		    			}
		    		}
		    	}else{
		    		alert('系统错误!');
		    	}
		    }
		})
	})
	function selCategory(tt){
		var value = $(tt).val();
		$('[parentId^=]').hide();
		$('[parentId="'+value+'"]').show();
	}
	function selBrand(tt){
		var value = $(tt).val();
		$('[categoryId^=]').hide();
		$('[categoryId="'+value+'"]').show();
	}
	function submitForm() {
			$.Loading.show("正在添加......");
			var options = {
				url : "/b2b2cbak/apiAdmin/AdminProductAction_saveProduct.do",
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if (data.result == "success") {
						parent.addTab1("商品列表","/b2b2cbak/admin/storeGoods!list.do");
						parent.CloseTabByTitle("添加商品");
					}
				},
				error : function(e) {
					alert("出现错误 ，请重试"+e);
				}
			};
			$("#addForm").ajaxSubmit(options);
	}
	
</script>