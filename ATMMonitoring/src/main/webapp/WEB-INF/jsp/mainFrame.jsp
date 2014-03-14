<%@include file="includes/JspImports.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<%@include file="includes/HtmlHead.jsp" %>
<script type="text/javascript">

 $( document ).ready(function() {
	 $("#dashboardMenu").click();
	 clock();
}); 
</script> 
</head>
<body>
<div class="loader"></div>
        <div id="main_header">
        	<input type="hidden" id="lastVisit">
            <div id="rButton" class="hide btn_close">

            </div>
            <h1>
	                <a href="dashboard"><img src="resources/images/general/logo.png"/></a>
	                <div id="headName"><span><acronym title="Hardware and Software Asset Management">HSAM</acronym></span></div>
          
            </h1>
            <div id="user_info">
               <div class="welcome"><spring:message code="label.welcomeMessage"/> ${userMsg}</div>-
               <div class="date" id="welcomeDate"></div>
                <div id="lang" class="desplegable autofold">
                    <div class="txt content_hide">
                        <span><spring:message code="language.${pageContext.response.locale}"/></span>
                    </div>
                    <ul class="collapsible">
                        <li><a href="${currentUrl}?lang=es"><spring:message code="language.es"/></a></li>
                        <li><a href="${currentUrl}?lang=en"><spring:message code="language.en"/></a></li>
                    </ul>
                </div>
                <sec:authorize access="isAuthenticated()">
                    <div id="exit">
                        <a href="logout"><span><spring:message code="label.menu.logout"/></span></a>
                    </div>
                </sec:authorize>
            </div>
        </div> 
        <div id="main_menu">
	        <nav id="main_nav" class="btn_close">
	            <ul id="nav_icons">
	                <sec:authorize access="hasAnyRole(${dashboardAccessAllowedRoles})">
		                <li class="dashboard">
		                    <a id="dashboardMenu" onclick="loadInnerSectionMenu('#'+this.id, '#primary', 'dashboard')"><span id="dashboard"><spring:message code="label.menu.dashboard"/></span></a>
		                </li>
	                </sec:authorize>
	                <sec:authorize access="hasAnyRole(${terminalsAccessAllowedRoles})">
		                <li class="terminals">
		                    <a id="terminalMenu" onclick="loadInnerSectionMenu('#'+this.id, '#primary', 'terminals')"><span><spring:message code="label.menu.terminals"/></span></a>
		                </li>
	                </sec:authorize>
	                <sec:authorize access="hasAnyRole(${reportsAccessAllowedRoles})">
		                <li class="reports">
		                    <a href="externalreports"><span><spring:message code="label.menu.externalreports"/></span></a>
		                </li>
	                </sec:authorize>
	                <sec:authorize access="hasAnyRole(${schedulesAccessAllowedRoles})">
	                <li class="schedule">
	                    <a href="terminals/schedules/list"><span><spring:message code="label.menu.scheduler"/></span></a>
	                </li>
	                </sec:authorize>
	                <sec:authorize access="hasAnyRole(${usersAccessAllowedRoles})">
		                <li class="users">
		                    <a href="users"><span><spring:message code="label.menu.users"/></span></a>
		                </li>
	                </sec:authorize>
	                <sec:authorize access="hasAnyRole(${helpAccessAllowedRoles})">
		                <li class="help">
		                    <a href="help"><span><spring:message code="label.menu.help"/></span></a>
		                </li>
	                </sec:authorize>
	            </ul>
	        </nav>
		</div>
        <div id="main" class="btn_close">
            <div id="primary">
                
            </div>
            <!-- /primary -->
        </div>
        <!-- /#main -->

    </body>
</html>