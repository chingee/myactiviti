<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="base.jsp"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>${name }运行的流程实例</title>
</head>
</style>

<body>
<div id="main">
	<div class="panel panel-info">
		<div class="panel-heading text-center">
	        <h2>${name }运行的流程实例</h2>
	    </div>
	</div>
	<div id="left_div" class="panel panel-default">
		<div class="panel-body ">
			<table class="table table-bordered table-striped table-hover text-center">
				<caption class="panel-title text-center">${name }当前的流程实例</caption>
				<thead>
					<tr>
						<th class="text-center">ID</th>
		    			<th class="text-center">流程的activityId</th>
		    			<th class="text-center">流程的processDefinitionKey</th>
		    		</tr>
				</thead>
				<tbody>
					<c:forEach items="${flows}" var="flow">
						<tr>
							<td>
								<a target="processInstance_window" href="<%=basePath %>task/querylist/${flow.processInstanceId}">${flow.processInstanceId}</a>
							</td>
							<td>${flow.activityId}</td>
							<td>${flow.processDefinitionKey}</td>
						</tr>
					</c:forEach>
				</tbody>	
			</table>
		</div>
	</div>
	<div id="right_div" class="panel panel-default">
		<div class="panel-body ">
			<table class="table table-bordered table-striped table-hover text-center">
				<caption class="panel-title text-center">${name }历史的流程实例</caption>
				<tbody>
					<tr>
						<th class="text-center">ID</th>
		    			<th class="text-center">流程的startActivityId</th>
		    			<th class="text-center">流程的processDefinitionKey</th>
		    			<th class="text-center">流程的startTime</th>
		    			<th class="text-center">流程的endTime</th>
		    		</tr>
					<c:forEach items="${hisflows}" var="flow">
						<tr>
							<td>
								<a target="history_window" href="<%=basePath %>history/querylist/${flow.processInstanceId}">${flow.processInstanceId}</a>
							</td>
							<td>${flow.startActivityId}</td>
							<td>${flow.processDefinitionKey}</td>
							<td><fmt:formatDate value="${flow.startTime}" pattern="yyyy-MM-dd HH:mm:ss SSS"/></td>
							<td><fmt:formatDate value="${flow.endTime}" pattern="yyyy-MM-dd HH:mm:ss SSS"/></td>
						</tr>
					</c:forEach>
				</tbody>	
			</table>
		</div>
	</div>
</div>
</body>
</html>
