<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<jsp:include page="base.jsp"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>已经发布的流程</title>
</head>

<body>
	<div id="main">
		<div class="panel panel-info">
			<div class="panel-heading text-center">
		        <h2>已经发布的流程</h2>
		    </div>
		    <div class="panel-body text-center">
				<form class="form-inline" role="form" action="<%=basePath%>processdefinition/deploy" method="post" 
					enctype="multipart/form-data" class="form-inline" role="form">
		    		<div class="form-group">
				    	<label class="sr-only" for="inputfile">bpmn或者zip文件</label>
				    	<input id="inputfile" name="file" type="file" value="bpmn或者zip文件">
				  	</div>
				  	<button  class="btn btn-default">发布</button>
				</form>
		    </div>
		</div>
		<div id="left_div" class="panel panel-default">
			<div class="panel-body ">
				<table class="table table-bordered table-hover text-center">
					<caption class="panel-title text-center">已发布的流程</caption>
					<thead>
						<tr>
							<th class="text-center">ID</th>
							<th class="text-center">流程的key</th>
							<th class="text-center">流程的name</th>
							<th class="text-center">流程图查看</th>
							<th class="text-center">操作</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${flows}" var="flow">
							<tr>
								<td><a target="processDefinition_window"
									href="<%=basePath %>processinstance/querylist/${flow.id}">${flow.id}</a></td>
								<td>${flow.key}</td>
								<td>${flow.name}</td>
								<td>
									<button class="btn btn-default" onclick="showbiggerFlow('${flow.id}', false)">查看</button>
									<button class="btn btn-default" onclick="showbiggerFlow('${flow.id}', true)">查看</button>
								</td>
								<td>
									<button class="btn btn-default" onclick="startprocessDefinition('${flow.id}')">启动</button>
									<button class="btn btn-default" onclick="deleteprocessDefinition('${flow.id}')">删除</button>
								</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>	
		</div>
		<div id="right_div" class="panel panel-default">
			<div class="panel-body ">
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
