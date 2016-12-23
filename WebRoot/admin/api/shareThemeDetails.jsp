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
<body style="overflow-x: hidden;margin:0px;padding:0px;font-family: 微软雅黑;">
	<div id="secDiv" style="width:320px;margin:0 auto;">
		<div style="margin:0px;padding:0px;">
			<img src="http://www.weman.cc:8089/b2b2cbak/statics/${theme.minorImage }"/>
		</div>
		<section style="margin: 0 auto;position: relative;width:100%;" >
			<div style=" position: relative;width:100%;">
				<div style="font-weight: bold;font-size: 18px;text-align: center;margin-top:4px;padding-left:3%;padding-right:3%;">
					${theme.title }
				</div>
				<div style="font-size:14px;text-align: center;color:rgb(193,193,193);margin-top:4px;padding-left:3%;padding-right:3%;">
					${theme.showDate }
				</div>
				<div style="font-size:16px;color:rgb(113,113,113);margin-top:4px;padding-left:3%;padding-right:3%;">
					${theme.details }
				</div>
				<% int i = 1; %>
				<div style="width:100%;height:auto;overflow: hidden;">
				<s:iterator value="#request.theme.themeContent" var="themeContent" >
					<s:if test="#themeContent.type=='text'">
						<div style="font-size:16px;color:rgb(113,113,113);margin-top:5px;margin-bottom:5px;padding-left:3%;padding-right:3%;text-align: center;">
							${themeContent.content }
						</div>
					</s:if><s:elseif test="#themeContent.type=='image'">
						<div style="margin:0px;padding:0px;margin-top:3%;padding-left:3%;padding-right:3%;display:inline-block;">
							<img src="http://www.weman.cc:8089/b2b2cbak/statics/${themeContent.image }"/>
						</div>
					</s:elseif>
					<s:elseif test="#themeContent.type=='product'">
							<s:if test="#request.theme.contentStyle=='default'">
								<div style="padding-left:3%;padding-right:3%;">
									<div style="text-align: center;margin:0 auto;margin-top:5px;height:50px;position: relative;width:300px;">
										<img src="../admin/api/image/themeline.png" width="100%" />
										<span style="width:20;margin:0 auto;top:26%;margin-left:-3%;position: absolute;font-size:15px;">0<%=i++ %></span>
									</div>
									<div style="text-align: center;margin-top:5px;margin-bottom: 5px;color:black;font-size:18px;">${themeContent.productName }</div>
									<div style="text-align: center;margin-top:5px;margin-bottom: 5px;padding-left:4px;padding-right:4px;font-size:16px;color:rgb(113,113,113);">${themeContent.intro }</div>
									<div style="position: relative; ">
										<div style="overflow: hidden;width:100%;">
											<img src="http://www.weman.cc:8089/b2b2cbak/statics/${themeContent.productImage }" width="100%" />
										</div>
										<div style="position: absolute;bottom: 0px;">
											<img width="100%" src="../admin/api/image/goshow.png" />
										</div>
										<div style="position: absolute;bottom: 10px;color: white;font-size:18px;left:10px;font-weight: bold;">
											¥ ${productPrice } 
										</div>
									</div>
								</div>
							</s:if><s:else>
								<div style="float:left;width:44%;height:auto;overflow: hidden;padding-top:1%;padding-left:3%;padding-right:3%;padding-bottom: 3%;background: rgb(240,240,240);">
									<a href="${themeContent.url }" style="cursor: pointer;list-style: none;">
										<div style="background: white;">
											<div style="width:100%;"><img src="http://www.weman.cc:8089/b2b2cbak/statics/${themeContent.productImage }" /></div>
											<div style="width:94%;overflow: hidden;text-align: left;height:20px;margin-top:6px;padding-left:3%;padding-right:3%;">${themeContent.productName }</div>
											<div style="width:94%;overflow: hidden;text-align: left;height:52px;margin-top:6px;font-size:12px;color:rgb(113,113,113);padding-left:3%;padding-right:3%;">${themeContent.intro }</div>
											<div style="width:94%;color:red;text-align: left;padding-left:3%;padding-right:3%;">¥ ${productPrice } </div>
										</div>
									</a>
								</div>
							</s:else>
						</s:elseif>
				</s:iterator>
				</div>
<!-- 				<div style="background: rgb(240,240,240);width:100%;height:auto;overflow: hidden;margin-top:4%;padding-bottom:4%;"> -->
<!-- 				</div> -->
			</div>
			<div style="margin-top:4px;margin-bottom: 4px;">
				<div style="width:100%;height:1px;background: rgb(193,193,193);"></div>
				<img src="../admin/api/image/moretheme.png" width="100%" />
				<div style="width:100%;height:1px;background: rgb(193,193,193);"></div>
			</div>
		</section>
	<div style="width:94%;padding-left:3%;padding-right:3%;height:auto;overflow: hidden;height:auto;padding-bottom:3%;" id="moreThemeDiv">
		<s:iterator value="#request.pageObj.result" var="themeObj">
			<a href="../apiAdmin/ShareAction_getDataDetails.do?type=theme&dataId=${themeObj.theme.id }">
				<div style="padding-top:7px;padding-bottom:7px;height:70px;width:100%;overflow: hidden;">
					<div style="float:left;width:33%;"><img src="http://www.weman.cc:8089/b2b2cbak/statics/${themeObj.theme.minorImage }"/></div>
					<div style="float:left;margin-left:2%;width:64%;">
						<div style="margin-top:7px;font-size:16px;color: black;">${themeObj.theme.title }</div>
						<div style="margin-top:4px;font-size:14px;color:rgb(193,193,193);width:100%;overflow: hidden;height:38px">${themeObj.theme.details }</div>
					</div>
				</div>
			</a>
		</s:iterator>
	</div>
	</div>
</div>
	<div style="margin: 0 auto;width:320px;" id="bottomDiv">
		<div style="height:60px;">
		</div>
		<header onclick="location.href='http://www.weman.cc/downloadAppStore.jsp?downType=2'" id="appDiv" style="position: fixed; bottom: 0px; width: 320px; background-color: #383838;height: 60px;">
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
var ww=document.documentElement.clientWidth;
if(ww<1000){
	$('#secDiv').width(parseInt(ww));
	$('#appDiv').width(parseInt(ww));
	$('#bottomDiv').width(parseInt(ww));
// 	$('#moreThemeDiv').width(parseInt(ww));
}
</script>
</html>