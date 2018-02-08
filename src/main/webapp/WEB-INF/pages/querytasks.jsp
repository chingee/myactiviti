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
<title>当前流程实例的任务</title>
</head>

<body>
<div id="main">
	<div class="panel panel-info">
		<div class="panel-heading text-center">
	        <h2>当前流程实例</h2>
	    </div>
	</div>
	<div id="left_div" class="panel panel-default">
		<div class="panel-body ">
			<table class="table table-bordered table-hover">
				<caption class="panel-title text-center">当前流程实例的活动任务</caption>
				<tbody>
					<tr>
						<th>ID</th>
		    			<th>任务的name</th>
		    			<th>任务的assignee</th>
		    			<th>任务的executionId</th>
		    			<th>任务的createTime</th>
		    		</tr>
					<c:forEach items="${tasks}" var="flow">
						<tr>
							<td>${flow.id}</td>
							<td>${flow.name}</td>
							<td>${flow.assignee}</td>
							<td>${flow.executionId}</td>
							<td><fmt:formatDate value="${flow.createTime}" pattern="yyyy-MM-dd HH:mm:ss SSS"/></td>
						</tr>
					</c:forEach>
				</tbody>	
			</table>
			<table class="table table-bordered table-hover">
				<caption class="panel-title text-center">当前流程实例的执行节点</caption>
				<tbody>
					<tr>
						<th>ID</th>
		    			<th>执行的name</th>
		    			<th>执行的parentId</th>
		    			<th>执行的activityId</th>
		    			<th>是否结束</th>
		    			<th>执行的superExecutionId</th>
		    		</tr>
					<c:forEach items="${executions}" var="flow">
						<tr>
							<td>${flow.id}</td>
							<td>${flow.name}</td>
							<td>${flow.parentId}</td>
							<td>${flow.activityId}</td>
							<td>${flow.ended}</td>
							<td>${flow.superExecutionId}</td>
						</tr>
					</c:forEach>
				</tbody>	
			</table>
		</div>
	</div>
	<div id="right_div" class="panel panel-default">
		<div id="warning_div" class="alert alert-warning">
		</div>
		<div class="panel-body text-center">
	        <h3 style="cursor:pointer" onclick="showbiggerTrackFlow('','${processInstanceId}', false)">流程图</h3>
			<img id="flowpng" class="img-rounded img-responsive" onclick="showbiggerTrackFlow('${processDefinitionId}', '${processInstanceId}', true)" title="当前实例流程图" src="<%= basePath%>processinstance/trace/${processInstanceId}"/ >
	    </div>
	    <div class="panel-body">
			<table class="table table-bordered table-hover">
				<caption class="panel-title text-center">流程流转</caption>
				<form action="#" method="post" onSubmit="javascript:return false;">
					<tbody>
						<tr>
							<th>当前任务ID</th>
			    			<th>通过</th>
			    			<th>退回</th>
			    			<th>退回到指定节点</th>
			    			<th>指定节点ID</th>
			    		</tr>
						<tr>
							<td>
								<input name="taskId" type="text" class="form-control" placeholder="输入当前任务ID" />
							</td>
							<td>
								<button class="btn btn-default" onclick="pass(${processInstanceId})">通过</button>
							</td>
							<td>
								<button class="btn btn-default" onclick="back(${processInstanceId})">退回</button>
							</td>
							<td>
								<button class="btn btn-default" onclick="backTo(${processInstanceId})">退回到指定节点</button>
							</td>
							<td>
								<input name="toTaskId" type="text" class="form-control" placeholder="输入指定节点ID" />
							</td>
						</tr>
					</tbody>	
				</form>
			</table>
	    </div>
	    <div class="panel-body">
			<table id="paramtable" class="table table-bordered table-hover text-center">
				<caption class="panel-title text-center">启动参数</caption>
				<thead>
					<tr id="row_0">
						<th class="text-center">key</th>
						<th class="text-center">value</th>
						<th class="text-center">操作</th>
					</tr>
				</thead>
				<tbody>
					<tr id="row_1">
						<td><input type="text" class="form-control" placeholder="输入参数名" name="key"/></td>
						<td><input type="text" class="form-control" placeholder="输入参数值" name="value"/></td>
						<td>
							<button class="btn btn-default" onclick="addRow()">添加</button>
							<button class="btn btn-default" onclick="delRow()">删除</button>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<!-- 模态框（Modal） -->
<div id="biggerimg" class="modal fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div id="modelimg" onclick="hidebiggerFlow()" class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                <h4 class="modal-title" id="myModalLabel">流程图</h4>
            </div>
            <div class="modal-body" >
            	<iframe id="flowimg" frameborder="0" width="99%" height="600px" src=""/>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary btn-sm" data-toggle="modal" data-target="#biggerimg">关闭</button>
            </div>
        </div>
    </div>
</div>
</body>
</html>
