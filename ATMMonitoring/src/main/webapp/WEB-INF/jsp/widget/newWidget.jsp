<%@include file="../includes/JspImports.jsp"%>
<%@taglib tagdir="/WEB-INF/tags/widget/" prefix="widget" %>
<!DOCTYPE HTML>
<html>
    <head>
        <script src="resources/js/jquery-1.8.3.min.js"></script>
        <script type='text/javascript' src="resources/js/jquery-ui.min.js"></script>
		<script type='text/javascript' src="resources/js/initHelp.js"></script>
		<script src="resources/js/windowLocationHenhancer.js"></script>
	</head>
<body id="iframe">
  <div id="main">
	<div id="primary1">
			<article>
				<h1><spring:message code="widget.create.title" /></h1>
				<div class="content fixed">
					<label>
						<input type="radio" name="creationType" value="dashboard/create" checked="checked">
						<spring:message code="widget.create.new" />
					</label>
					
					<label>
						<input type="radio" name="creationType" value="dashboard/createFromLibrary" >
						<spring:message code="widget.create.from.library" />
					</label>
					
					<div class="botonera">
						<input id="next" type="button" class="btn next" value="Continuar">
					</div>
				</div><!-- /content -->
			</article>
		</div>
	</div>
	<script type="text/javascript">
		$("input#next").click(function() {
			var selectedOption = $("input[type=radio]:checked");
			loadInnerSection("#widgetPrimary",selectedOption.val());
		});
	</script>
</body>
</html>