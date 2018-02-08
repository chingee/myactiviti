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
<title>${name }历史的实例流程</title>
</head>

<body>
<div id="main">
	<div class="panel panel-info">
		<div class="panel-heading text-center">
	        <h2>${name }历史的实例流程</h2>
	    </div>
	</div>
	<div class="panel panel-default">
		<div class="panel-body text-center">
	        <h3>流程图</h3>
			<img id="flowimg" class="img-thumbnail img-responsive" title="${name }实例流程图" src="<%= basePath%>processinstance/trace/${processInstanceId}"/ >
	    </div>
    </div>
	<div id="left_div" class="panel panel-default">
		<div class="panel-body ">
			<table class="table table-bordered table-striped table-hover text-center">
				<caption class="panel-title text-center">${name }的历史用户任务</caption>
				<thead>
					<tr>
						<th class="text-center">ID</th>
		    			<th class="text-center">任务的name</th>
		    			<th class="text-center">任务的assignee</th>
		    			<th class="text-center">任务的executionId</th>
		    			<th class="text-center">任务的deleteReason</th>
		    			<th class="text-center">任务的开始时间</th>
		    			<th class="text-center">任务的开结束时间</th>
		    		</tr>
				</thead>
				<tbody>
					<c:forEach items="${historictasks}" var="flow">
						<tr>
							<td>${flow.id}</td>
							<td>${flow.name}</td>
							<td>${flow.assignee}</td>
							<td>${flow.executionId}</td>
							<td>${flow.deleteReason}</td>
							<td><fmt:formatDate value="${flow.startTime}" pattern="yyyy-MM-dd HH:mm:ss SSS"/></td>
							<td><fmt:formatDate value="${flow.endTime}" pattern="yyyy-MM-dd HH:mm:ss SSS"/></td>
						</tr>
					</c:forEach>
				</tbody>	
			</table>
		</div>
	</div>
	<div id="right_div" class="panel panel-default">
		<div class="panel-body ">
			<table class="table table-bordered table-striped table-hover text-center">
				<caption class="panel-title text-center">${name }的历史节点</caption>
				<thead>
					<tr>
						<th class="text-center">ID</th>
		    			<th class="text-center">历史的activityId</th>
		    			<th class="text-center">历史的activityName</th>
		    			<th class="text-center">历史的activityType</th>
		    			<th class="text-center">历史的taskId</th>
		    			<th class="text-center">历史的assignee</th>
		    			<th class="text-center">历史的startTime</th>
		    			<th class="text-center">历史的endTime</th>
		    		</tr>
				</thead>
				<tbody>
					<c:forEach items="${historicactivitys}" var="flow">
						<tr>
							<td>${flow.id}</td>
							<td>${flow.activityId}</td>
							<td>${flow.activityName}</td>
							<td>${flow.activityType}</td>
							<td>${flow.taskId}</td>
							<td>${flow.assignee}</td>
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
