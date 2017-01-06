<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<%@taglib prefix="cc" uri="/tcardztaglib" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content="width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0" />
<meta content="telephone=no" name="format-detection">
<base href="/b2b2cbak/admin">
<title>商品详情页</title>
<script type="text/javascript" src="/b2b2cbak/themes/b2b2cv2/js/jquery.js"></script>
<style type="text/css">
body{
	font-size: 14px;
	font-family: 微软雅黑;
	color:rgb(127,127,127);
}
</style>
</head>
<body style="margin:0px;padding:0px;font-family: 微软雅黑;overflow: hidden;width:100%;">
<div id="divd" style="width:88%;height:auto;overflow: hidden;margin:0 auto;margin-top:24px;margin-right:6%;margin-left:6%;">
	<div style="width:92%;margin:0 auto;text-align: center;max-height:200px;overflow: hidden;">
		<img src="/b2b2cbak/statics/${resObj.original }" width="100%" />
	</div>
	<div style="width:100%;text-align: center;color:#313131;word-break:break-all; font-weight: bold;font-size:15px;margin-top:12px;">
		${resObj.brief }
	</div>
	<div style="width:100%;text-align: center;color:#626262;word-break:break-all; font-size:12px;margin-top:0px;">
		${resObj.name }
	</div>
	<div style="width:100%;text-align: center;color:#FF5149;font-weight: bold;font-size: 22px;margin-top:4px;">
		￥${resObj.price }
	</div>
	<div style="height:6px;width:100%;"></div>
</div>
<div id="introDiv" style="width:75%;margin:0 auto;text-align: left;word-break:break-all; color:#626262;letter-spacing: 0.1px;font-size: 12px;margin-top:6px;text-align:justify;text-justify:inter-ideograph;overflow: scroll;height:100px;">
	<div id="innerHeight">
		${resObj.intro }
	</div>
</div>
</body>
<script type="text/javascript">
document.documentElement.style.webkitTouchCallout = "none";  
document.ontouchstart=function(e){
	 e.preventDefault();
}
var inity = 0;
document.getElementById('introDiv').ontouchstart=function(e){
	e.stopPropagation();
	inity = e.touches[0].pageY;
}
document.getElementById('introDiv').ontouchmove=function(e){
	e.stopPropagation();
	$('#introDiv').css('overflow','scroll');
	var stopy = $('#introDiv').scrollTop();
	var innerHeight = $('#innerHeight').height();
	if(stopy == 0){
		var curry = e.touches[0].pageY;
		if(curry>inity){
			e.preventDefault();
		}
	}else if(stopy == (innerHeight-100)){
		var curry = e.touches[0].pageY;
		if(curry<inity){
			e.preventDefault();
		}
	}
}
</script>
</html>