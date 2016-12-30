<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
<script type="text/javascript" src="/b2b2cbak/statics/js/common-min2.js"></script>
<style type="text/css">
body{
	font-size: 12px;
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
<div class='buttonArea'>
	<div style="height:30px;margin-top:4px;">
		<form id="addForm" enctype="multipart/form-data"  method="post" >
			<span style="float: right;height:28px;"> 
				<input type="hidden" value="1" name="pageNo" id="goodsPage" />
			</span>
				<input type="hidden" value="${resObj.id }" name="id" />
				<input type="hidden" value="${resObj.streamId }" name="streamId" />
				<input type="hidden" value="${resObj.createTime }" name="startTime" />
			直播名字：${resObj.title }<br/><br/>
			直播简介：<input type="text" value="${resObj.details }" name="details" /><br/><br/>
			<s:if test="#request.resObj.isSave == 1">
				回放地址：${resObj.url } <br/>map4格式：${resObj.targetUrl }<br/><br/>			
			</s:if>
			直播保存：<s:else>
				未保存
			</s:else>
			<select name="isSave">
			
				<option value="1" >保存</option>
				<option value="-1" selected="selected">未存</option>
			</select><br/><br/>
			直播回放封面：
			<s:if test="#request.resObj.image != null">
				<img src="/b2b2cbak/statics/${resObj.image }" width="80px" height="80px"/>
			</s:if>
			<input type="file" name="liveFile" /><br/><br/>
			回放列表显示：
			<select name="status">
				<option value="1" <s:if test="#request.resObj.status == 1">selected="selected"</s:if>>显示</option>
				<option value="-1" <s:if test="#request.resObj.status == -1">selected="selected"</s:if>>不显</option>
			</select><br/><br/>
		</form>
			<div class="buttonWrap">
				<a href="javascript:;" onclick="submitForm()" class="l-btn" id="searchAdvance" group=""><span class="l-btn-left"><span class="l-btn-text">保存</span></span></a>
			</div>
	</div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
	});
	function submitForm() {
		var options = {
			url : "/b2b2cbak/apiAdmin/AdminLiveAction_updateDetails.do",
			type : "POST",
			dataType : 'json',
			success : function(data) {
				if (data.result == "yes") {
					parent.addTab1("直播列表","/b2b2cbak/apiAdmin/AdminLiveAction_getLiveList.do?pageNo=1");
					parent.CloseTabByTitle("修改直播");
				}else{
					alert(data.reason);
				}
			},
			error : function(e) {
				alert("出现错误 ，请重试"+e);
			}
		};
		$("#addForm").ajaxSubmit(options);
}
</script>