<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="$!{basePath}/resources_support/js/document/bootstrap/bootstrap.min.css">
<link rel="stylesheet" href="$!{basePath}/resources_support/js/document/docs.css">
<title>服务接口列表</title>
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top bs-docs-nav">
		<div class="container">
			<div class="navbar-header">
				<a class="navbar-brand">系统服务文档</a>
			</div>
		</div>
	</header>


	<div class="container bs-docs-container">
		<div class="row">
			<!-- 接口列表 -->
			<div class="col-md-3">
				<div role="complementary" class="bs-sidebar hidden-print affix">
					<ul class="nav bs-sidenav">
						#foreach($item in $data)
							<li><a href="#$!{item.hrefName}">$!{item.name}</a>
						#end
					</ul>
				</div>
			</div>

			<!-- 接口名细 -->
			<div class="col-md-9" role="main">
				#foreach($item in $data)
					<div class="bs-docs-section">
						<div class="page-header">
							<h1 id="$!{item.hrefName}">$!{item.name}</h1><hr>
						</div>
						<div>
							<b>◆服务接口类名:</b>
						</div>
						<div>$!{item.className}</div>
						<div>
							<b>◆服务接口描述:</b>
						</div>
						<div>$!{item.description}</div>
						<div>
							<b>◆服务角色:</b>
						</div>
						<div>$!{item.role}</div>
						<div>
							<b>◆方法列表:</b>
						</div>
						<div>
							<table width="100%" class="table table-bordered">
								<thead>
									<tr>
										<th>序号</th>
										<th>方法名称</th>
										<th>请求URL</th>
										<th>方法描述</th>
									</tr>
								</thead>
								<tbody>
									#set ($idx=0) 
									#foreach($method in $item.methods)
										#set($idx=$idx+1) 
										<tr>
											<td>${idx}</td>
											<td><a target="_blank"
												href="$!{basePath}/document.do?method=detail&className=$!{method.className}&url=$!{method.url}">$!{method.name}</a></td>
											<td>$!{method.url}</td>
											<td>$!{method.description}</td>
										</tr>
									#end
								</tbody>
							</table>
						</div>
					</div>
				#end
			</div>
		</div>
	</div>

	<script src="$!{basePath}/resources_support/js/document/jquery-1.10.2.min.js" type="text/javascript"></script>
	<script src="$!{basePath}/resources_support/js/document/bootstrap/bootstrap.min.js"></script>
	<script src="$!{basePath}/resources_support/js/document/application.js" type="text/javascript"></script>
</body>
</html>