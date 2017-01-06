<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta charset="utf-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no"/>
<meta name="apple-mobile-web-app-status-bar-style" content="black"/>
<meta name="apple-mobile-web-app-title" content=""/>
<meta name="apple-touch-fullscreen" content="YES" />
<meta name="apple-mobile-web-app-capable" content="yes" />
<meta name="format-detection" content="telephone=no" />
<meta name="HandheldFriendly" content="true" />
<meta http-equiv="x-rim-auto-match" content="none" />
<meta name="format-detection" content="telephone=no" />
<!-- This is to force IE into the latest version mode, overriding 'compatibility' mode which breaks everything. -->
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<link rel="apple-touch-icon-precomposed" sizes="57x57" href="${bannerObj.image }" />
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="${bannerObj.image }" />
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="${bannerObj.image }" />
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="${bannerObj.image }" />
<!--feature-->
<script type="text/javascript" src="/b2b2cbak/admin/api/js/koko.core.js"></script>
<link rel="stylesheet" href="/b2b2cbak/admin/api/js/koko.skin.css">
<title>${bannerObj.title }</title>
<style type="text/css">
boby{
	box-sizing: border-box;
	font-family: Helvetica,Arial,"Lucida Grande",sans-serif;
}
</style>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
</head>
<body style="overflow-x: hidden;margin:0px;padding:0px;">
	<div id="videoContainer" >
			<video src="${liveObj.playHls }" height="300" width="400"></video>
	</div>
</body>
<script type="text/javascript">

</script>
</html>