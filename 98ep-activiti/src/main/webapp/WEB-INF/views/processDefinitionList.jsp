<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>

<script type="text/javascript" src="js/jquery-2.2.0.min.js"></script>
<link rel="stylesheet" type="text/css" href="ui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="ui/themes/icon.css">
<script type="text/javascript" src="ui/jquery.min.js"></script>
<script type="text/javascript" src="ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="ui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/data/processDefinitionList.js"></script>
</head>
<body>
	<table id="processDefinitionList"></table>
	
	<div id="imageWindow" class="easyui-window" title="流程图片" style="width: 900px;height: 700px" data-options="closed:true">
			<img id="processImg" src=""/>
	</div>
	
	<%-- <input id="modelName" type="hidden" value="${variables1}">
	<input id="tenantId" type="hidden" value="${variables2}">
	<input id="type" type="hidden" value="${variables3}"> --%>
	<input id="modelName" type="hidden" value="${a}">
	<input id="tenantId" type="hidden" value="${b}">
	<input id="type" type="hidden" value="${c}">
	
</body>
</html>