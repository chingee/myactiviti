<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Activiti流程</title>
</head>
<script type="text/javascript" src="js/jquery/jquery.js" ></script>
<body>
	<center>
		<h1><a href="<%=path %>/model/querylist">查看创建的流程</a></h1>
		<h1><a href="<%=path %>/processdefinition/querylist">查看已发布的流程</a></h1>
	</center>
</body>
</html>
