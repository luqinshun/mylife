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
<script type="text/javascript" src="js/data/userModel.js"></script>
</head>

<body>

	<%-- <input id="tenantId" type="hidden" value="${variables}"> --%>

	<input id="tenantId" type="hidden" value="${a}">

	<table id="userModel"></table>
	<div id="createModelWindow" class="easyui-window" title="新建模板"
		style="width: 300px; height: 250px" data-options="closed:true,collapsible:false">

		<form id="createModelForm" class="easyui-form">
			<table>
				<tr>
					<td>模板名称</td>
					<td><input name="name" class="easyui-validatebox"
						data-options="required:true" missingMessage="模板名不能为空"></td>
				</tr>
				<tr>
					<td>模板KEY</td>
					<td><input name="key" class="easyui-validatebox"
						data-options="required:true" missingMessage="模板KEY不能为空"></td>
				</tr>
				<tr>
					<td style="vertical-align:top">模板备注</td>
					<td><input name="description" class="easyui-textbox" data-options="multiline:true"
						style="height:120px" ></td>
				</tr>
			</table>
		</form>
		<div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" id="createModel">创建</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" id="reset">重置</a>
	    </div>
	    </div>
	</div>
</body>
</html>