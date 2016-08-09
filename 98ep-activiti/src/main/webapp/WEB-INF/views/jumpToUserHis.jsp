<%@ page language="java" contentType="text/html; UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
<script type="text/javascript" src="js/jquery-2.2.0.min.js"></script>
<script type="text/javascript">
function getUrl(){
	var url = "userHisProIns";
	$.ajax({
		type:'get',
		url:'identity/getUserId',
		async:false,
		success:function(msg){
			if(msg.key != null){
				url = "userHisProIns?a=" + msg.key;
			}
		}
	});
	return url;
}

window.location.href = getUrl();
</script>

</head>
<body>


</body>
</html>