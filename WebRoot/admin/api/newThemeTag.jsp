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
</style>
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<link rel="stylesheet" type="text/css" href="/b2b2cbak/adminthemes/new/js/easy-ui/themes/gray/easyui.css"/>    
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="/b2b2cbak/adminthemes/new/js/easy-ui/easyui-lang-zh_CN.js"></script>
<div style="background-color: white; padding: 15px 0 8px 10px;margin:0px;position: relative;">
	<form id="addForm"  enctype="multipart/form-data"  method="post">
		<table style="width:98%;">
			<tr height="38px"><td class="spanFront">标签类别: </td><td>
				<select id="pSel" name="themeTag.category" >
					<option value="0">新建父标签</option>
					<s:iterator value="#request.parentTags" var="pt">
						<option value="${pt.id }">${pt.name }</option>
					</s:iterator>
				</select>
<%-- 				<select id="cSel" style="display:none;"> --%>
<%-- 					<s:iterator value="#request.childrenTags" var="ct"> --%>
<%-- 						<option value="${ct.id }">${ct.name }</option> --%>
<%-- 					</s:iterator> --%>
<%-- 				</select> --%>
			</td></tr>
			<tr height="38px"><td class="spanFront">标签名字 : </td><td>
				<input class="inputFront" name="themeTag.name" />
			</td></tr>
			<tr height="38px"><td class="spanFront">图片 : </td><td><input type="file" name="themeTagFile"/></td></tr>
		</table>
	</form>
	<div class="buttonWrap">
		<a href="javascript:;" onclick="submitForm()" class="l-btn" id="searchAdvance" group=""><span class="l-btn-left"><span class="l-btn-text">保存</span></span></a>
	</div>
</div>
<script>
	
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
	function pcChange(tt){
		if($(tt).val==0){
			$('#cSel').hide();
		}else{
			$('#cSel').show();
		}
	}
	function submitForm() {
			$.Loading.show("正在添加......");
			var options = {
				url : "/b2b2cbak/apiAdmin/AdminProductAction_addThemeTag.do",
				type : "POST",
				dataType : 'json',
				success : function(data) {
					if (data.result == "yes") {
						parent.addTab1("主题标签","/b2b2cbak/apiAdmin/AdminProductAction_themeTagList.do");
						parent.CloseTabByTitle("主题标签添加");
					}
				},
				error : function(e) {
					alert("出现错误 ，请重试"+e);
				}
			};
			$("#addForm").ajaxSubmit(options);
	}
</script>