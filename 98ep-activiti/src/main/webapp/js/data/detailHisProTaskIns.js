$(function(){
	var processInstanceId = document.getElementById("processInstanceId").value;
	var userId = document.getElementById("userId").value;
	var type = document.getElementById("type").value;
	$('#detailHisProTaskIns').datagrid({
		method:'GET',
		url:'pageHistory/detail/' + processInstanceId,
		singleSelect:true,
		striped:true,
		pagination:true,
		rownumbers:true,
	    columns:[[    
	        {field:'hisTaskInsId',title:'历史任务ID',width:100},    
	        {field:'processDefinitionId',title:'流程定义ID',width:200},    
	        {field:'processInstanceId',title:'流程实例ID',width:100},
	        {field:'taskDefinitionKey',title:'任务定义',width:100},
	        {field:'taskName',title:'任务名称',width:100},
	        {field:'status',title:'任务状态',width:150},
	        {field:'description',title:'办理意见',width:150},
	        {field:'startTime',title:'任务开始时间',width:150},
	        {field:'claimTime',title:'任务签收时间',width:150},
	        {field:'endTime',title:'任务办理时间',width:150},
	        {field:'assignee',title:'签收人及办理人',width:110},
	        {field:'dutation',title:'所用时间',width:130}
	    ]],
	    toolbar: [{
			iconCls: 'icon-back',
			text:'返回列表',
			handler: function(){
				if(type == "all"){
					self.location.href = "allHisProIns";
				}
				if(type == "user"){
					self.location.href = "userHisProIns?a=" + userId;
				}
			}
		}]
	});
});