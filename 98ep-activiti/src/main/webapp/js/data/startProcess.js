$(function(){
	$('#start').bind('click',function(){
		var userId = document.getElementById("userId").value;
		var tenantId = document.getElementById("tenantId").value;
		var businessKey = document.getElementById("businessKey").value;
		var level = document.getElementById("level").value;
		
		if($('#startProcess').form('validate') == true){
			$.ajax({
				type : 'post',
				url : "process/start/" + userId + "/" + tenantId + "/" + businessKey + "/" +level,
				success : function(XMLHttpRequest, textStatus) {
					
					if(textStatus == "nocontent" ){
						$.messager.alert('提示', '启动成功');
						$('#startProcess').form('clear');
					}
				}
			});
		}else{
			$.messager.alert('提示', '验证未通过，请完善信息');
		}
	});
});
