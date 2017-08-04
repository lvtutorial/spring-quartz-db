<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="ext" tagdir="/WEB-INF/tags"%>
<%@ page session="false"%>
<html>
<head>
<title><spring:message code="job.search.page" /></title>

<style>
	.disabledbutton {
    	display:none !important;
	}
	
	/*disable container cron*/
	.jqCron-container {
		pointer-events: none; 
		opacity: 0.7;
	}

</style>	
</head>
<body>
<div class="ita_form">
	
	<ext:messages bean="${bean}"></ext:messages>
	<c:url var="addAction" value="/jobinfos"></c:url>

	<form:form action="${addAction}" commandName="bean" modelAttribute="bean" id="search_form">
		<div class="panel panel-default">
	    	<div class="panel-heading"><spring:message code="job.search.title" /></div>
	      	<div class="panel-body">
	      		<div class="form-horizontal">
	      			<div class="form-group">
						<label class="control-label col-sm-2">Job Name</label>
						<div class="col-sm-6">
							<div class="input-group">
								<form:input class="form-control" placeholder="Job Name"	maxlength="200" path="entity.jobname" />
								<div class="input-group-btn">
									<button type="submit" class="btn btn-primary">
										<i class="glyphicon glyphicon-search"></i>
									</button>
						      	</div>
							</div>	  				
						</div>
					</div>								 			    
	      		</div>
	      	</div>
	    </div>
	
					
		<div class="panel panel-default">
			<div class="panel-heading">Job List (${bean.total})</div>
	      	<div class="panel-body">
	      		<div id="flip-scroll">
	      			<table class="table_responsive table-striped table-bordered out-tbl table-condensed">
						<thead>
							<tr> <!-- 1170 -->
								<th>#</th>
								<th>ID</th>
								<th>Job Name</th>
								<th>Status</th>
								<th>Edit</th>
							</tr>
						</thead>
						<tbody>
						<c:if test="${bean.listResult != null}"> 
						<c:forEach var="_job" items="${bean.listResult}" varStatus="i">
							<tr>
								<td>${(bean.page-1)*bean.limit+i.index+1}</td>
								<td>${_job.id}</td>
								<td>${_job.jobname}</td>
								<td>
									<c:if test="${!_job.enable}">
										Stopped
									</c:if>		
									<c:if test="${_job.enable}">
										Running								
									</c:if>
								</td>								
								<td><a href="<c:url value='/jobinfos/edit/${_job.id}' />">Edit</a></td>													  
							</tr>
						</c:forEach>
						</c:if>
						</tbody>
					</table>
	      		</div>
	      		<div>
					<ext:pagination bean="${bean}" maxPage="10" formId="search_form"></ext:pagination>
				</div>
	      	</div>
	    </div>			
	</form:form>



</div>
</body>
</html>