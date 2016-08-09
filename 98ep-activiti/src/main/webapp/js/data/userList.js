$(function(){
	var tenantId = document.getElementById("tenantId").value;
	$('#userList').datagrid({    
		method:'GET',
		url:'identity/user/list',
		singleSelect:true,
	    columns:[[    
	        {field:'userId',title:'用户ID',width:100},    
	        {field:'--',title:'操作',width:100,formatter:function(value,row,index){
	    		return "<a href='#' onclick='toModeler("+row.modelId+")'>编辑</a> <a href='#' onclick='deleteModel("+row.modelId+")'>删除</a>";
	    	}}
	        
	    ]],
	    toolbar: [{
			iconCls: 'icon-add',
			text:'创建模板',
			handler: function(){
				$('#createModelForm').form('clear');
				$('#createModelWindow').window('open');
				if(typeof(openWindow)!='undefined'){
					openWindow();//打开窗口时要做的操作
				}
			}
		}],
		striped:true,
		pagination:true
	});
	
	$('#reset').bind('click',function(){
		$('#createModelForm').form('clear');
	});
	
	$('#createModel').bind('click',function(){
		if($('#createModelForm').form('validate') == true){
			var data = $('#createModelForm').serialize();
			
			$.ajax({
				type : 'POST',
				url : "model/create/" + tenantId,
				data : data,
				contentType:'application/x-www-form-urlencoded; charset=utf-8',
				success : function(msg) {
					if(msg.key == "id" ){
						$.messager.alert('提示', '创建成功','info',function(){
							self.location = "modeler.html?modelId="+msg.modelId;
						});
					}else if(msg.key == "error"){
						$.messager.alert('提示', '该租户已存在此类型模板，请编辑！');
					}
				}
			});
		}else{
			$.messager.alert('提示', '验证未通过，请完善信息！');
		}
	});
});

function getProDEF(modelName){
	var tenantId = document.getElementById("tenantId").value;
	$.ajax({
		type:'get',
		url:'processDefinition/list/'+encodeURI(encodeURI(modelName))+'/' + tenantId,
		success:function(data,status,XHR){
			if(status == "nocontent"){
				$.messager.alert("提示","该模板还未进行过部署，请部署后进行此操作");
			}else{
				self.location = "processDefinitionList_" + encodeURI(encodeURI(modelName)) + "_" + tenantId + "_user";
			}
		}
	});
}

function toModeler(modelId){
	self.location = "modeler.html?modelId="+modelId;
}

function deleteModel(modelId){
	$.messager.confirm("提示","确定要删除吗",function(value){
		if(value){
			$.ajax({
				type:'DELETE',
				url:'model/delete/'+modelId,
				success:function(msg){
					if(msg.key == "success"){
						$.messager.alert("提示","删除成功","info",function(){
								$('#userModel').datagrid('reload');
						});
					}else{
						$.messager.alert("提示","删除未成功");
					}
				}
			});
		}
	});
}

function deployModel(modelId){
	$.messager.confirm("提示","确定部署",function(value){
		if(value){
			$.ajax({
				type:'POST',
				url:'model/deploy/'+modelId,
				success:function(msg){
					if(msg.key == "success"){
						$.messager.alert('提示','部署成功，ID='+msg.deployId);
					}else if(msg.key == "fail"){
						$.messager.alert('提示','部署失败');
					}
				},
				error:function(msg){
					if(msg.responseText != "" ){
						$.messager.alert('提示','当前模板不完整，请点击编辑完善！');
					}
				}
			});
		}
	});
}
	