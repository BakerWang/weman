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
<link rel="apple-touch-icon-precomposed" sizes="57x57" href="${theme.minorImage }" />
<link rel="apple-touch-icon-precomposed" sizes="72x72" href="${theme.minorImage }" />
<link rel="apple-touch-icon-precomposed" sizes="114x114" href="${theme.minorImage }" />
<link rel="apple-touch-icon-precomposed" sizes="144x144" href="${theme.minorImage }" />
<!--feature-->
<title>${theme.title }</title>
<style>
boby{
	box-sizing: border-box;
	font-family: Helvetica,Arial,"Lucida Grande",sans-serif;
}
img {
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
<body>
<body style="overflow-x: hidden;margin:0px;padding:0px;">
	<div id="secDiv" style="width:320px;margin:0 auto;">
		<div style="margin:0px;padding:0px;">
			<img src="http://183.195.242.14:8089/b2b2cbak/statics/${theme.minorImage }"/>
		</div>
		<section style="margin: 0 auto;position: relative;width:100%;" >
			<div style=" position: relative;padding-left:3%;padding-right:3%;width:94%;">
				<div style="font-weight: bold;font-size: 15px;text-align: center;margin-top:4px;">
					${theme.title }
				</div>
				<div style="font-size:13px;text-align: center;color:rgb(193,193,193);margin-top:4px;">
					${theme.showDate }
				</div>
				<div style="font-size:14px;color:rgb(113,113,113);margin-top:4px;">
					${theme.details }
				</div>
				<% int i = 1; %>
				<s:iterator value="#request.theme.themeContent" var="themeContent">
					<s:if test="#themeContent.type=='text'">
						<div style="font-size:14px;color:rgb(113,113,113);margin-top:5px;margin-bottom:5px;padding-left:2px;text-align: center;">
							${themeContent.content }
						</div>
					</s:if><s:elseif test="#themeContent.type=='image'">
						<div style="margin:0px;padding:0px;">
							<img src="http://183.195.242.14:8089/b2b2cbak/statics/${themeContent.image }"/>
						</div>
					</s:elseif><s:elseif test="#themeContent.type=='product'">
						<div style="text-align: center;margin-top:5px;height:50px;position: relative;">
							<img src="../admin/api/image/themeline.png" width="100%" />
							<span style="width:20;margin:0 auto;top:35%;margin-left:-2.4%;position: absolute;font-size:16px;">0<%=i++ %></span>
						</div>
						<div style="text-align: center;margin-top:5px;margin-bottom: 5px;color:black;font-size:15px;">${themeContent.productName }</div>
						<div style="text-align: center;margin-top:5px;margin-bottom: 5px;padding-left:4px;padding-right:4px;font-size:14px;color:rgb(113,113,113);">${themeContent.intro }</div>
						<div style="position: relative; ">
							<div style="overflow: hidden;width:100%;">
								<img src="http://183.195.242.14:8089/b2b2cbak/statics/${themeContent.productImage }" width="100%" />
							</div>
							<div style="position: absolute;bottom: 0px;">
								<img width="100%" src="../admin/api/image/goshow.png" />
							</div>
							<div style="position: absolute;bottom: 10px;color: white;font-size:18px;left:10px;font-weight: bold;">
								$ ${productPrice } 
							</div>
						</div>
					</s:elseif>
				</s:iterator>
			</div>
			<div style="margin-top:4px;margin-bottom: 4px;">
				<div style="width:100%;height:1px;background: rgb(193,193,193);"></div>
				<img src="../admin/api/image/moretheme.png" width="100%" />
				<div style="width:100%;height:1px;background: rgb(193,193,193);"></div>
			</div>
			
		</section>
	</div>
</div>
	<div style="width:320px;margin:0 auto;" id="moreThemeDiv">
		<s:iterator value="#request.pageObj.result" var="themeObj">
			<a href="../apiAdmin/ShareAction_getDataDetails.do?type=theme&dataId=${themeObj.theme.id }">
				<div style="padding-top:5px;padding-bottom:5px;height:70px;width:100%;">
					<div style="float:left;margin-left:5px;"><img src="http://183.195.242.14:8089/b2b2cbak/statics/${themeObj.theme.minorImage }" style="width:100px;height:70px"/></div>
					<div style="float:left;margin-left:3px;width:67%;">
						<div style="margin-top:7px;font-size:15px;color: black;">${themeObj.theme.title }</div>
						<div style="margin-top:4px;font-size:14px;color:rgb(193,193,193);">${themeObj.theme.details }</div>
					</div>
				</div>
			</a>
		</s:iterator>
	</div>
	<div style="margin: 0 auto;width:320px;" id="bottomDiv">
		<div style="height:60px;">
		</div>
		<header  id="appDiv" style="position: fixed; bottom: 0px; width: 320px; background-color: #383838;height: 60px;">
			<div style="float: left; height: 44px;left: 6px;position: absolute;top: 8px;width: 44px;"><img alt="" src="../admin/api/image/logo64.png"></div>
			<div style="color: #fff;font-size: 16px;line-height: 18px;padding: 13px 55px 0;text-align: left;">
				<p>第一个同志专属电商APP</p>
				<p>WeMan我们</p>
			</div>
			<!--<a href="http://a.app.qq.com/o/simple.jsp?pkgname=com.xymens.app" class="btn">点击下载</a>-->
	           <a style="  background-color: #f5a415;border-radius: 3px; color: rgb(234,83,67); font-size: 12px;line-height: 20px;padding: 5px 10px;position: absolute;right: 10px;text-align: center;top: 15px;" id="openApp" href="javascript:;">下载APP</a>
	           <a style="z-index: 8" class="close" href="javascript:;"></a>
		</header>
	</div>
</body>
<script type="text/javascript">
var hh=document.documentElement.clientWidth;
if(hh<700){
	$('#secDiv').width(parseInt(hh));
	$('#appDiv').width(parseInt(hh));
	$('#moreThemeDiv').width(parseInt(hh));
	$('#bottomDiv').width(parseInt(hh));
}
</script>
</html>