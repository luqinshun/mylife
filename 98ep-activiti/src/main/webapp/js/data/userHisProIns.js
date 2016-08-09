$(function(){
	var userId = document.getElementById("userId").value;
	var url = "history/userAll/" + userId + "/all";
	$('#ongoing').bind('click',function(){
		url = "history/userAll/" + userId + "/ongoing";
		$('#allHisProIns').datagrid('reload',url);
	});
	$('#end').bind('click',function(){
		url = "history/userAll/" + userId + "/end";
		$('#allHisProIns').datagrid('reload',url);
	});
	$('#all').bind('click',function(){
		url = "history/userAll/" + userId + "/all";
		$('#allHisProIns').datagrid('reload',url);
	});
	$('#allHisProIns').datagrid({
		method:'GET',
		url:url,
		singleSelect:true,
		striped:true,
		pagination:true,
		rownumbers:true,
	    columns:[[    
	        {field:'hisProInsId',title:'历史流程ID',width:200},    
	        {field:'processInstanceId',title:'流程实例ID',width:180},    
	        {field:'businessKey',title:'业务KEY',width:180},
	        {field:'processDefinitionId',title:'流程定义ID',width:150},
	        {field:'startTime',title:'创建时间',width:150},
	        {field:'endTime',title:'结束时间',width:150},
	        {field:'duration',title:'所用时间',width:150},
	        {field:'status',title:'状态',width:100},
	        {field:'-',title:'操作',width:100,formatter:function(value,row,index){
	    		return "<a href='#' onclick='showDetail(\"" + row.processInstanceId + "\",\""+ userId +"\")'>查看审核详情</a>";
	    	}},
	    	{field:'--',title:'跟踪',width:100,formatter:function(value,row,index){
	    		if(row.status == "已结束"){
	    			return "<a href='#' onclick='showImage(\"" + row.processDefinitionId + "\")'>查看流程图</a>"
	    		}
	    		if(row.status == "未结束"){
	    			return "<a href='#' onclick='showTrackImage(\"" + row.processDefinitionId + "\",\"" + row.processInstanceId + "\")'>查看流程跟踪</a>"
	    		}
	    	}}
	    ]]
	});
});

function showDetail(processInstanceId,userId){
	var userId = document.getElementById("userId").value; 
	self.location = "detailHisTaskIns?a=" + processInstanceId + "&b=" + userId + "&c=user";
}

function showImage(processDefinitionId){
	$('#imageWindow').window('open');
	$('#processImg').attr('src','image/read/' + processDefinitionId);
}

function showTrackImage(processDefinitionId,processInstanceId){
	$('#imageWindow').window('open');
	$('#processImg').attr('src','image/track/' + processDefinitionId + '/' + processInstanceId);
}