
$(function(){
	var modelName = document.getElementById("modelName").value;
	var tenantId = document.getElementById("tenantId").value;
	var type = document.getElementById("type").value;
	$('#processDefinitionList').datagrid({    
		method:'GET',
		url:'processDefinition/list/'+encodeURI(encodeURI(modelName))+ '/' + tenantId,
		singleSelect:true,
		rownumbers:true,
	    columns:[[    
	        {field:'processDefinitionId',title:'流程定义ID',width:200},    
	        {field:'deploymentId',title:'部署ID',width:170},    
	        {field:'name',title:'流程定义名称',width:180},
	        {field:'key',title:'KEY',width:150},
	        {field:'version',title:'版本',width:150},
	        {field:'deployTime',title:'部署时间',width:300},
	        {field:'-',title:'操作',width:100,formatter:function(value,row,index){
	    		return "<a href='#' onclick='getImage(\""+row.processDefinitionId+"\")'>查看流程图</a>";
	    	}}
	    ]],
	    toolbar: [{
			iconCls: 'icon-back',
			text:'返回列表',
			handler: function(){
				if(type == "user"){
					//self.location.href = "userModel_" + tenantId;
					self.location.href = "userModel?a=" + tenantId;
				}
				if(type == "all"){
					self.location.href = "allModel";
				}
			}
		}],
		striped:true,
		pagination:true
	});
});

function getImage(processDefinitionId){
	$('#imageWindow').window('open');
	$('#processImg').attr('src','image/read/'+processDefinitionId);
}







	