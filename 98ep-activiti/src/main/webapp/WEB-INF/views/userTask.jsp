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
<script type="text/javascript" src="js/data/userTask.js"></script>
</head>

<body>
	
	<div class="easyui-panel" style="padding:5px;">
		根据用户查询：
		<input id="userId" name="tenantIdList" class="easyui-combobox">
		<a id="userSearch" href="#" class="easyui-linkbutton">查询</a>
		<a id="searchAll" href="#" class="easyui-linkbutton">查看所有</a>
		
	</div>
	<div class="easyui-panel" style="padding:5px;">
		根据公司查询：
		<input id="tenantId" name="tenantIdList" class="easyui-combobox">
		<a id="tenantSearch" href="#" class="easyui-linkbutton">查询</a>
	</div>
	
	<table id="userTask"></table>
	<div id="claimUserWindow" class="easyui-window" title="签收"
		style="width: 242px; height: 100px" data-options="closed:true,collapsible:false">

		<form id="claimUserForm" class="easyui-form">
			<table>
				<tr>
					<td>签收人</td>
					<td><input id="claimUserId" name="userId" class="easyui-validatebox"
						data-options="required:true" missingMessage="签收人不能为空"></td>
				</tr>
				
			</table>
		</form>
		<div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" id="claim">签收</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" id="reset">重置</a>
	    </div>
	</div>
	<div id="completeWindow" class="easyui-window" title="办理"
		style="width: 280px; height: 200px" data-options="closed:true,collapsible:false">

		<form id="completeForm" class="easyui-form">
			<table>
				<tr>
					<td>办理意见</td>
					<td><input id="description" name="description" class="easyui-textbox" data-options="multiline:true" 
					style="height:120px"></td>
				</tr>
				
			</table>
		</form>
		<div style="text-align:center;padding:5px">
	    	<a href="javascript:void(0)" class="easyui-linkbutton" id="pass">同意</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" id="reject">驳回</a>
	    	<a href="javascript:void(0)" class="easyui-linkbutton" id="reset_">重置</a>
	    </div>
	    
	</div>
</body>
</html>