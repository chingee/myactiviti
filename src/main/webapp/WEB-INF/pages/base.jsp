<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="<%=basePath%>css/mycss.css" rel="stylesheet" type="text/css"/>
<link rel="stylesheet" href="<%=basePath%>js/bootstrap/css/bootstrap.min.css">
<script type="text/javascript" src="<%=basePath%>js/jquery/jquery.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="<%=basePath%>js/myfunction.js"></script>
</head>
<script type="text/javascript">
	var basePath = "<%=basePath%>";
</script>
<body>
	<p id="errortext">${errortext }</p>
</body>
</html>