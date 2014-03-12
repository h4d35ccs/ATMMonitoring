<%@include file="includes/JspImports.jsp"%>
<html>
<head>	 
	    <meta http-equiv="Cache-Control" Content="public,max-age=31536000" />
		<script type="text/javascript" src="resources/js/json2html.js"></script>
		<script type="text/javascript" src="resources/js/jquery.json2html.js"></script>
		<script type='text/javascript' src='resources/js/jquery.colorbox-min.js'></script> 
		<script type="text/javascript">
	      var strings = new Array();
	      strings['label.widget.options'] = "<spring:message code='label.widget.options' javaScriptEscape='true' />";
	      strings['label.widget.edit'] = "<spring:message code='label.widget.edit' javaScriptEscape='true' />";
	      strings['label.widget.delete'] = "<spring:message code='label.widget.delete' javaScriptEscape='true' />";
	      strings['label.widget.cancel'] = "<spring:message code='label.widget.cancel' javaScriptEscape='true' />";
	      strings['widget.delete.confirm'] = "<spring:message code='widget.delete.confirm' javaScriptEscape='true' />";
	      strings['widget.add.to.library'] = "<spring:message code='widget.add.to.library' javaScriptEscape='true' />";
	      strings['widget.remove.from.library.confirm'] = "<spring:message code='widget.remove.from.library.confirm' javaScriptEscape='true' />";
	      strings['widget.set.as.default'] = "<spring:message code='widget.set.as.default' javaScriptEscape='true' />";
	      strings['widget.set.as.default.confirm'] = "<spring:message code='widget.set.as.default.confirm' javaScriptEscape='true' />";
	      strings['widget.unset.as.default.confirm'] = "<spring:message code='widget.unset.as.default.confirm' javaScriptEscape='true' />";
	      
	      var hasPrivileges = false;
	      <sec:authorize access="hasAnyRole(${editWidgetsLibraryAllowedRoles})">
	          hasPrivileges = true;
	      </sec:authorize>
    </script>
    <!--  <script type='text/javascript' src='https://www.google.com/jsapi'></script> 
	 	<script type="text/javascript">
    		   google.load('visualization', '1', {'packages': ['corechart', 'geochart', 'table']}); 
    	</script> -->
<script type='text/javascript' src='resources/js/dashboard.js'></script>
</head>
<body>
	<div id="dashboard">
		<div id="header_g">
			<div id="components">
				<div id="my_components" class="desplegable autofold">
					<div class="txt content_hide">
						<span><spring:message code="label.widgets.mine" /></span>
					</div>
					<ul id="myCharts" class="collapsible"></ul>
				</div>
				<a class="iframe_medium cboxElement btn add"
					href="dashboard/newWidget"> <spring:message
						code="label.widget.new" />
				</a>
	
			</div>
			<div id="columns" class="columns_botonera">
				<nav>
					<ul>
						<c:forEach items="${columnOptions}" var="option">
							<li
								class="column${option.getColumns()} ${((option.getColumns() == dashboard.getColumns()) ? 'current' : '')}"
								data-columns="${option.getColumns()}"><a href="#"> <span>${option.getColumns()}
										<g:message code="label.columns" />
								</span>
							</a></li>
						</c:forEach>
					</ul>
				</nav>
			</div>
		</div>
		<ul id="sortable" class="ui-sortable column${dashboard.getColumns()}"></ul>
		<div id="noCharts" class="message left hide">
			<p>
				<spring:message code="label.dashboard.explain" />
			</p>
			<script type="text/javascript">
				$( document ).ready(function() {
					initDashboard() ;
				});
	</script>
		</div>
	</div>
</body>
</html>