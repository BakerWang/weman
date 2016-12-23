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
<link rel="apple-touch-icon-precomposed" sizes="57x57" href="${article.image }" />
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="${article.image }" />
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="${article.image }" />
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="${article.image }" />
<!--feature-->
<title>${article.content }</title>
<style type="text/css">
boby{
	box-sizing: border-box;
	font-family: Helvetica,Arial,"Lucida Grande",sans-serif;
}
#appDiv img {
    display: block;
    width: 100%;
    border: 0 none;
}
p {
    hyphens: auto;
    word-break: break-all;
    font-size: 100%;
    margin: 0;
    padding: 0;
}
a {
    color: inherit;
    outline: medium none;
    text-decoration: none;
}
</style>
<script src="../adminthemes/new/js/jquery-1.8.3.min.js" type="text/javascript"></script>
</head>
<body style="overflow-x: hidden;margin:0px;padding:0px;">
	<div id="secDiv" style="width:320px;margin:0 auto;position: relative;">
		<div style="width:100%;height:500px;position: relative;overflow: hidden;">
			<img width="100%" height="500px" src="http://www.weman.cc:8089/b2b2cbak/statics/${article.image}" id="bImage" style="position: absolute;z-index: 0;top:0px;left:0px;" />
			<div style="width:120px;height: 500px;float: left;background: white;opacity: 0.9;-ms-filter: progid:DXImageTransform.Microsoft.Alpha(Opacity=0.9);filter: alpha(opacity = 0.9);">
				<div style="margin-top:27px;height:180px;">
					<div style="float:left;width:3px;background: red;height:120px;"></div>
					<div style="float:left;width:117px;">
						<div style="width:50px;margin:0 auto;"><img src="http://www.weman.cc:8089/b2b2cbak/statics/${article.userPhoto }" width="50px" style="border-radius: 25px;" /></div>
						<div style="width:8px;height:1px;background: black;margin:0 auto;margin-top:15px;"></div>
						<div style="text-align: center;font-size: 16px;margin-top:10px;">${article.userName }</div>
						<div style="font-size:13px;text-align: center;margin-top:7px;">${userAttr }</div>
					</div>
				</div>
				<div style="margin-top: 160px;height:130px;margin-left:10px;width:100px;overflow: hidden;">
					<s:iterator value="#request.categoryImages" var="cimage" status="statu">
						<div style="border:1px gray solid;height:14px;margin-top:4px;padding-right:4px;width:-moz-max-content;display:inline-block; ">
							<div style="width:12px;height:12px;overflow: hidden;float:left;margin-top:1px;margin-left:2px;"><img src="${cimage }" width="12px" /></div>
							<div style="width:1px; margin-top:1px;margin-left:1px;height:12px;background: black;float:left;"></div>
							<div style="float:left;margin-left:4px;font-size:11px;line-height: 14px;">${brand[statu.index] }</div>
						</div>
					</s:iterator>
				</div>
			</div>
			<div style="position: absolute;z-index:1;bottom: 2px;right:12px;">
				<img src="../admin/api/image/articleLoveBut.png" width="40px" />
			</div>
		</div>
		<div style="width:100%;height:3px;background: red;"></div>
		<div style="width:100%;height:35px;background: rgb(215,215,215);">
			<s:iterator value="#request.tagArray" var="tag">
				<div style="float:left;margin-top:7px;margin-left:12px;background-image: url('../admin/api/image/tagbackground.png') ;background-position:right;padding-right:8px;padding-top:2px;padding-bottom: 2px;"><span style="margin-left:6px;color:white;font-size:12px;">${tag }</span></div>
			</s:iterator>
			<div style="color:black;font-size:12px;float:left;margin-left:10px;line-height: 34px;">${showCreateTime }</div>
		</div>
		<div style="padding:8px;font-size:14px;padding-left:10px;">
			${article.content }
		</div>
		<div style="width:100%;overflow: hidden;">
			<div style="float:right;margin-right:15px;">
				<img src="../admin/api/image/comment.png" style="width:12px;" width="12px"/>&nbsp;${article.commentCount }
			</div>
			<div style="float:right;margin-right:15px;">
				<img src="../admin/api/image/look.png" style="width:12px;"  width="12px"/>&nbsp;${article.viewCount }
			</div>
			<div style="float:right;margin-right:15px;">
				<img src="../admin/api/image/like.png" style="width:12px;"  width="12px"/>&nbsp;${article.loveCount }
			</div>
		</div>
<!-- 		<div style="width:100%;height:1px;background: rgb(193,193,193);margin-top:3px;"></div> -->
<!-- 		<div style="margin-top:3px;padding:8px;position: relative;"> -->
<%-- 			<s:iterator value="#request.article.loveUserList" var="loveUser" status="statu"> --%>
<%-- 				<s:if test="#statu.index==0"><img src="../admin/api/image/win.png" width="34px" style="position: absolute;z-index:2;top:-15px;left:12px;" /></s:if> --%>
<%-- 				<img src="http://www.weman.cc:8089/b2b2cbak/statics/${loveUser.photo }" width="36px" height="36px" style="border-radius: 18px;margin-left:3px;"/> --%>
<%-- 			</s:iterator> --%>
<!-- 		</div> -->
<!-- 		<div style="width:100%;height:16px;background: rgb(215,215,215);"></div> -->
<!-- 		<div style="overflow: hidden;"> -->
<%-- 			<div style="padding-top:8px;padding-left:8px;font-size: 16px;">评论（${article.commentCount }）</div> --%>
<!-- 			<div style="margin-top:10px;"> -->
<!-- 				<div style="width:100%;height:1px;background: rgb(215,215,215);"></div> -->
<%-- 				<s:iterator value="#request.commetList" var="comment"> --%>
<!-- 					<div style="padding-left:8px;padding-top:8px;height:47px;"> -->
<%-- 						<div style="width:36px;float:left;"><img width="36px" height="36px" style="border-radius: 18px;" src="${comment.photo }" /></div> --%>
<!-- 						<div style="float:left;margin-left:8px;width:85%;"> -->
<!-- 							<div style="display: block;width:100%;height:22px;"> -->
<%-- 								<div style="margin-top:1px;float:left;color:black;font-size:14px;font-weight: bold;">${comment.username }</div> --%>
<%-- 								<div style="float:right;margin-right:8px;font-size:12px;">${comment.createTimeStr }</div> --%>
<!-- 							</div> -->
<!-- 							<div style="overflow: hidden;font-size:13px;color:rgb(93,93,93);width:100%;"> -->
<%-- 								${comment.content } --%>
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<div style="width:100%;height:1px;background: rgb(215,215,215);"></div> -->
<%-- 				</s:iterator> --%>
<!-- 			</div> -->
<!-- 		</div> -->
	</div>
	<div style="margin: 0 auto;width:320px;" id="bottomDiv">
		<div style="height:60px;">
		</div>
		<header onclick="location.href='http://www.weman.cc/downloadAppStore.jsp'" id="appDiv" style="position: fixed; bottom: 0px; width: 320px; background-color: #383838;height: 60px;">
			<div style="float: left; height: 44px;left: 6px;position: absolute;top: 8px;width: 44px;"><img alt="" src="../admin/api/image/logo64.png"></div>
			<div style="color: #fff;font-size: 16px;line-height: 18px;padding: 13px 55px 0;text-align: left;">
				<p>WeMan我们</p>
				<p>1st为同志精选的型男好物平台</p>
			</div>
			<!--<a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.xymens.app" class="btn">点击下载</a>-->
	           <a style="  background-color: black;border-radius: 3px; color: white;border:1px solid rgb(234,83,67); font-size: 12px;line-height: 20px;padding: 5px 10px;position: absolute;right: 10px;text-align: center;top: 15px;" id="openApp" href="https://itunes.apple.com/cn/app/id1122061713">下载APP</a>
	           <a style="z-index: 8" class="close" href="javascript:;"></a>
		</header>
	</div>
</body>
<script type="text/javascript">
var hh=document.documentElement.clientWidth;
if(hh<1000){
	$('#secDiv').width(parseInt(hh));
	$('#appDiv').width(parseInt(hh));
	$('#bottomDiv').width(parseInt(hh));
}
$('#bImage').animate({'margin-left':"120px"},3500);
setTimeout(function(){
	$('#bImage').animate({'margin-left':"0px"},3500);
}, 3500)
setInterval(function(){
	$('#bImage').animate({'margin-left':"120px"},3500);
}, 7000);
setInterval(function(){
	$('#bImage').animate({'margin-left':"0px"},3500);
}, 7000);
</script>
</html>