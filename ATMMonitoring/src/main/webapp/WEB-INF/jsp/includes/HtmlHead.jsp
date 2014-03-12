        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <meta http-equiv="Cache-Control" Content="public,max-age=31536000" />
        <base href="${base}"/>
        <link rel="stylesheet" type="text/css" href="resources/css/ncr_screen.css">
         <script src="resources/js/jquery-1.8.3.min.js"></script>
			<script type="text/javascript">
					$(window).load(function() {
						$(".loader").fadeOut("slow");
					});
			</script>
	    <script type='text/javascript' src="resources/js/jquery-ui.min.js"></script>
	    <script type='text/javascript' src="resources/js/jquery.ui.touch-punch.min.js"></script>
	    <script type='text/javascript' src='resources/js/jquery.colorbox-min.js'></script>
        <script type='text/javascript' src="resources/js/windowLocationHenhancer.js"></script>
        <script type='text/javascript' src="resources/js/menu.js"></script>
        <script type='text/javascript' src="resources/js/application.js"></script>
        <script type='text/javascript' src='resources/js/jsapi.js'></script> 
	 	<script type="text/javascript">
    		   google.load('visualization', '1', {'packages': ['corechart', 'geochart', 'table']}); 
    	</script>
        <title><spring:message code="label.baseTitle"/></title>
       
        <!--[if lt IE 9]>
        <script type="text/javascript">
            document.createElement("nav");
            document.createElement("header");
            document.createElement("footer");
            document.createElement("section");
            document.createElement("article");
            document.createElement("aside");
            document.createElement("hgroup");
        </script>
        <![endif]-->