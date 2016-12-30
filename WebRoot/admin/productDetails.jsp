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
<base href="${ctx}/themes/b2b2cv2/api/bannerActivity/">
<title>商品详情页</title>
<script type="text/javascript" src="${ctx}/themes/b2b2cv2/js/jquery.js"></script>
<style type="text/css">
body{
	font-size: 14px;
	font-family: 微软雅黑;
	color:rgb(127,127,127);
}
</style>
</head>
<body style="margin:0px;padding:0px;">
<div style="width:100%;height:auto;overflow: hidden;">
	${resObj.goods_id }
</div>
</body>
</html>