<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="ui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="ui/themes/icon.css">
<script type="text/javascript" src="ui/jquery.min.js"></script>
<script type="text/javascript" src="ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="ui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/data/userHisProIns.js"></script>
</head>

<body>

	<input id="userId" type="hidden" value="${a}">
	
	<div class="easyui-panel" style="padding:5px;">
		<a id="all" href="#" class="easyui-linkbutton" data-options="toggle:true,group:'g1',selected:true">全部</a>
		<a id="ongoing" href="#" class="easyui-linkbutton" data-options="toggle:true,group:'g1'">未完成</a>
		<a id="end" href="#" class="easyui-linkbutton" data-options="toggle:true,group:'g1'">已完成 </a>
	</div>
	
	<table id="allHisProIns"></table>
	
	<div id="imageWindow" class="easyui-window" title="流程图片" style="width: 900px;height: 700px" data-options="closed:true">
			<img id="processImg" src=""/>
	</div>
	
</body>
</html>