<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="base.jsp" />
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>流程模版</title>
</head>

<body>
	<div id="main">
		<div class="panel panel-info">
			<div class="panel-heading text-center">
				<h2>流程模版</h2>
			</div>
			<div id="left_div" class="panel panel-default">
				<div class="panel-body ">
					<table
						class="table table-bordered table-striped table-hover text-center">
						<caption class="panel-title text-center">创建的流程模版</caption>
						<thead>
							<tr>
								<th class="text-center">ID</th>
								<th class="text-center">流程的key</th>
								<th class="text-center">流程的name</th>
								<th class="text-center">流程的deploymentId</th>
								<th class="text-center">流程的版本</th>
								<th class="text-center">发布</th>
								<th class="text-center">删除</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${flows}" var="flow">
								<tr>
									<td><a target="model_window"
										href="<%=basePath %>modeler.html?modelId=${flow.id}">${flow.id}</a></td>
									<td>${flow.key}</td>
									<td>${flow.name}</td>
									<td>${flow.deploymentId}</td>
									<td>${flow.version}</td>
									<td>
										<button class="btn btn-default" onclick="deployModel('${flow.id}')">发布</button>
									</td>
									<td>
										<button class="btn btn-default" onclick="deleteModel('${flow.id}')">删除</button>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>
			<div id="right_div" class="panel panel-default">
				<div class="panel-body ">
					<div class="text-center">
				        <h3>流程模型</h3>
				    </div>
					<form action="<%=basePath %>/model/create" method="post" class="form-horizontal" role="form">
						<div class="form-group">
							<label for="key" class="col-sm-2 control-label">模型key</label>
							<div class="col-sm-10">
								<input id="key" name="key" type="text" class="form-control" placeholder="请输入key">
							</div>
						</div>
						<div class="form-group">
							<label for="name" class="col-sm-2 control-label">模型名称</label>
							<div class="col-sm-10">
								<input id="name" name="name" type="text" class="form-control" placeholder="请输入名称">
							</div>
						</div>
						<div class="form-group">
							<label for="description" class="col-sm-2 control-label">模型描述</label>
							<div class="col-sm-10">
								<textarea id="description" name="description" class="form-control" rows="3" placeholder="请输入描述"></textarea>
							</div>
						</div>
						<div class="form-group">
							<div class="col-sm-offset-2 col-sm-10">
								<button type="submit" class="btn btn-default">创建</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		</div>
	</div>
</body>
</html>
