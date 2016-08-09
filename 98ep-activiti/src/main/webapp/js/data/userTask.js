var taskId = null;

$(function(){
	var url = "task/allTask";
	$('#userTask').datagrid({
		method:'get',
		url:url,
		singleSelect:true,
		rownumbers:true,
		striped:true,
		pagination:true,
		pageList:[10,20,30,40,50,60,70,80,90,100],
	    columns:[[    
	        {field:'id',title:'任务ID',width:70},    
	        {field:'name',title:'任务名称',width:100},  
	        {field:'taskDefinitionKey',title:'任务定义key',width:100},
	        {field:'processDefinitionId',title:'流程定义ID',width:130},
	        {field:'processInstanceId',title:'流程实例ID',width:70},
	        {field:'businessKey',title:'业务key',width:100},
	        {field:'modelName',title:'模型名称',width:160,formatter:function(value,row,index){
	        	var name = row.modelName.replace(".bpmn20.xml","");
	        	return name;
	        }},
	        {field:'modelKey',title:'模型Key',width:100},
	        {field:'createTime',title:'创建时间',width:122},
	        {field:'claimTime',title:'签收时间',width:122,formatter:function(value,row,index){
	        	var claimTime = row.claimTime;
	        	if(claimTime != null){
	        		var newtime = claimTime.replace(".0","");
		        	return newtime;
	        	}
	        }},
	        {field:'status',title:'状态',width:47,formatter:function(value,row,index){
	        	if(row.claimTime == null){
	        		return "未签收";
	        	}else{
	        		return "已签收";
	        	}
	        }},
	        {field:'assignee',title:'签收人',width:50},
	        {field:'tenantId',title:'所属客户',width:50},
	        {field:'authenticatedUserId',title:'发起人',width:50},
	        {field:'--',title:'操作',width:100,formatter:function(value,row,index){
	    		if(row.assignee == null){
	    			return "<a href='#' onclick='claimTask("+row.id+")'>签收</a>";
	    		}
	    		if(row.assignee != null){
	    			return "<a href='#' onclick='completeTask("+row.id+")'>办理</a>";
	    		}
	    	}}
	        
	    ]]
	});
	
	$('#userSearch').bind('click',function(){
		var userId = $('#userId').combobox('getText');
		url = "task/pageUserTask/" + userId;
		$('#tenantId').combobox('setText',"");
		$('#userTask').datagrid('reload',url);
	});
	
	$('#tenantSearch').bind('click',function(){
		var tenantId = $('#tenantId').combobox('getText');
		url = "pageTask/tenantTask/" + tenantId;
		$('#userId').combobox('setText',"");
		$('#userTask').datagrid('reload',url);
	});
	
	$('#searchAll').bind('click',function(){
		url = "task/allTask";
		$('#tenantId').combobox('setText',"");
		$('#userId').combobox('setText',"");
		$('#userTask').datagrid('reload',url);
	});
	
	$('#reset').bind('click',function(){
		$('#claimUserForm').form('clear');
	});
	
	$('#claim').bind('click',function(){
		if($('#claimUserForm').form('validate') == true){
			var userId = document.getElementById("claimUserId").value;
			
			$.ajax({
				type : 'POST',
				url : "task/claim/" + userId + "/" + taskId,
				success : function(XMLHttpRequest, textStatus) {
					
					if(textStatus == "nocontent" ){
						$.messager.alert('提示', '签收成功','info',function(){
							$('#claimUserWindow').window('close');
							$('#userTask').datagrid('reload');
						});
					}
				}
			});
		}else{
			$.messager.alert('提示', '验证未通过，请完善信息');
		}
	});
	
	$('#reset_').bind('click',function(){
		$('#completeForm').form('clear');
	});
	
	$('#pass').bind('click',function(){
		$.messager.confirm("提示","确认同意？",function(value){
			if(value){
				var description = document.getElementById("description").value;
				var data = "description="+description+"&pass=true";
				$.ajax({
					url:'task/complete/' + taskId,
					type:'POST',
					data : data,
					contentType:'application/x-www-form-urlencoded; charset=utf-8',
					success:function(XMLHttpRequest, textStatus){
						if(textStatus == "nocontent" ){
							$.messager.alert('提示', '已同意','info',function(){
								$('#completeWindow').window('close');
								$('#userTask').datagrid('reload');
							});
						}
					}
				});
			}
		});
	});
	
	$('#reject').bind('click',function(){
		$.messager.confirm("提示","确认驳回？",function(value){
			if(value){
				var description = document.getElementById("description").value;
				var data = "description="+description+"&pass=false";
				$.ajax({
					url:'task/complete/' + taskId,
					type:'POST',
					data : data,
					contentType:'application/x-www-form-urlencoded; charset=utf-8',
					success:function(XMLHttpRequest, textStatus){
						if(textStatus == "nocontent" ){
							$.messager.alert('提示', '已驳回','info',function(){
								$('#completeWindow').window('close');
								$('#userTask').datagrid('reload');
							});
						}
					}
				});
			}
		});
	});
	
});

function getTime(time) {  
    var ts = arguments[0] || 0;  
    var t,y,m,d,h,i,s;  
    t = ts ? new Date(ts*1000) : new Date();  
    y = t.getFullYear();  
    m = t.getMonth()+1;  
    d = t.getDate();  
    h = t.getHours();  
    i = t.getMinutes();  
    s = t.getSeconds();  
    // 可根据需要在这里定义时间格式  
    return y+'-'+(m<10?'0'+m:m)+'-'+(d<10?'0'+d:d)+' '+(h<10?'0'+h:h)+':'+(i<10?'0'+i:i)+':'+(s<10?'0'+s:s);  
} 

function claimTask(id){
	taskId = id;
	$('#claimUserWindow').window('open');
}

function completeTask(id){
	taskId = id;
	$('#completeWindow').window('open');
}