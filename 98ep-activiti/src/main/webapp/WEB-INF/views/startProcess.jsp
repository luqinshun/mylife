<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css"
	href="ui/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="ui/themes/icon.css">
<script type="text/javascript" src="ui/jquery.min.js"></script>
<script type="text/javascript" src="ui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="ui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="js/data/startProcess.js"></script>
</head>

<body>

	<form id="startProcess" class="easyui-form">
		<table>
			<tr>
				<td>发起人</td>
				<td><input id="userId" name="userId" class="easyui-validatebox"
					data-options="required:true" missingMessage="发起人不能为空"></td>
			</tr>
			<tr>
				<td>公司</td>
				<td><input id="tenantId" name="tenantId"
					class="easyui-validatebox" data-options="required:true"
					missingMessage="公司不能为空"></td>
			</tr>
			<tr>
				<td>业务key</td>
				<td><input id="businessKey" name="businessKey"
					class="easyui-validatebox" data-options="required:true"
					missingMessage="业务key不能为空"></td>
			</tr>
			<tr>
				<td>级别</td>
				<td><input id="level" name="level" class="easyui-validatebox"
					data-options="required:true" missingMessage="级别不能为空"></td>
			</tr>
		</table>
	</form>
	<div>
	    <a href="javascript:void(0)" class="easyui-linkbutton" id="start">启动</a>
	</div>


</body>
</html>