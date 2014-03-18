<%@include file="includes/JspImports.jsp"%>
	 <div>
	  <div id="queryList">
	  <div id="header_g">
	    <nav id="breadcrumb">
	      <ul>
	        <li><a href="dashboard"><spring:message code="breadcrumb.home"/></a></li>
	        <li><a href="terminals"><spring:message code="breadcrumb.terminals"/></a></li>
	        <li><spring:message code="label.queries.mine"/></li>
	      </ul>
	    </nav>
	  </div>
	  <div class="content">
	    <h1><spring:message code="label.queries.mine"/></h1>
	    <div class="botonera">
	      <a href="queries/create" class="btn right"><spring:message code="label.query.new"/></a>
	    </div>
	    <c:if test="${success != null}">
	       <div id="notificationDiv" class="notification"><p><spring:message code="${success}"/>:)</p>
	       <script type="text/javascript">
	       		fadeNotification("#notificationDiv","notification");
	       </script>
	       </div>
	    </c:if>
	    <c:if test="${error != null}">
	      <div id="alertDiv" class="alert"><p><spring:message code="${error}"/></p></div>
	      <script type="text/javascript">
	       		fadeNotification("#alertDiv","alert");
	       </script>
	    </c:if>
	    <c:if test="${!empty pagedListHolder.pageList}">
	    <div id="pagTable">
	      <table class="link">
				<tr>
			          <th><spring:message code="label.query.name"/></th>
			          <th><spring:message code="label.creationDate"/></th>
			          <th><spring:message code="label.description"/></th>
				</tr>
				
				<c:forEach items="${pagedListHolder.pageList}" var="query">
				  <tr class="queryRow" onclick="loadInnerSection('#primary','queries/show?queryId=${query.id}')">
				    <td class="editable w33, clickeableMenu">
			              <div class="relative">
					<div class="icons_action"><a href="#" class="delete" data-query-id="${query.id}"><span><spring:message code="label.delete"/></span></a></div>
					<a onclick="loadInnerSection('#primary','queries/show?queryId=${query.id}')">${query.name}</a></div></td>
			            <td class="nowrap">${query.creationDateShort}</td>
			            <td>${query.description}</td>
			        </tr>
				</c:forEach>
			      </table>
	      <div class="pagination">
				<div class="t_number"><span class="text">${pagedListHolder.source.size()} <spring:message code="label.queries"/></span></div>
					<c:if test="${pagedListHolder.getPageCount() >1 }">
		  			<div class="p_number"><span class="text"><spring:message code="label.page"/></span><t:paging pagedListHolder="${pagedListHolder}" pagedLink="queries/list?p=~"/></div>
				</c:if>
	      		</div>
		    </c:if>
		    <c:if test="${empty pagedListHolder.pageList}">
		      <div class="message"><p> <spring:message code="label.queries.none"/> </p></div>
		    </c:if>
		</div>
	  </div> 
	  <script type="text/javascript">
	  function initPageJS() {
	        $("tr.queryRow").click(function(event) {
	            event.preventDefault();
	            var queryId = $(this).data('queryId');
	            window.location.assignWithBase("queries/show?queryId=" + queryId);
	        });
	
	        $(".delete").click(function(event) {
	            event.stopPropagation();
	            event.preventDefault();
	            var queryId= $(this).data('queryId');
	            if (confirm('�Est�s seguro que quieres borrar la consulta?')) {
	                window.location.assignWithBase("queries/delete?queryId=" + queryId);
	            }
	        });
	    }
	  </script>
	  </div>
	 </div>