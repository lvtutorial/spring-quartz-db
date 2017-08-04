<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="ext" tagdir="/WEB-INF/tags"%>
<%@ page session="false"%>
<html>
<head>
<title>Edit Job Page</title>
</head>
<body>
<div class="ita_form">
	
	<ext:messages bean="${bean}"></ext:messages>
	<c:url var="addAction" value="/jobinfos/edit"></c:url>

	<form:form action="${addAction}" commandName="jobinfo">
	
		<div class="panel panel-default">
	    	<div class="panel-heading">
	    		<c:if test="${!empty jobinfo.entity.jobname}">
					<div class="label_title">Edit a Job</div>
				</c:if>
				<c:if test="${empty jobinfo.entity.jobname}">
					<div class="label_title">Add a Job</div>
				</c:if>
	    	</div>
	      	<div class="panel-body">
      			<div class="form-horizontal">
	      			<div class="form-group">
						<c:if test="${!empty jobinfo.entity.jobname}">
							<form:hidden path="entity.id" />
						</c:if>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2">Job Name</label>
						<div class="col-sm-6">
							<form:input class="form-control" placeholder="Job Name"	maxlength="200" path="entity.jobname" />	  				
						</div>
					</div>
					<div class="form-group">
						<label class="control-label col-sm-2">Status</label>
						<div class="col-sm-6">
							<!-- <form:checkbox path="entity.enable" value="enable" disabled="true"/> -->
							<c:if test="${!jobinfo.entity.enable}">
								Stopped 
								<button type="submit" class="btn btn-primary" name="runjob" value="${jobinfo.entity.jobname}">
									<spring:message text="Start" />
								</button>		
							</c:if>		
							<c:if test="${jobinfo.entity.enable}">
								Running 
								<button type="submit" class="btn btn-primary" name="runjob">
									<spring:message text="Stop" />
								</button>								
							</c:if>		  				
						</div>
					</div>					
					<div class="form-group">
						<label class="control-label col-sm-2">Scheduler</label>
						<div class="form-control-static col-sm-6">
							<c:if test="${empty jobinfo.entity.jobschedule}">
								NOT SET - Please set (default):  
							</c:if>	
							<div class="example-selector"></div>
							<form:hidden class="example-span" maxlength="200" path="entity.jobschedule"/>	  				
						</div>
					</div>					
					<div class="form-group">
						<div class="col-sm-offset-4 col-sm-6">
							<button type="submit" class="btn btn-primary" id="saveEdit" name="saveEdit">
								<span class="glyphicon glyphicon-floppy-disk"></span> <spring:message text="Save" />
							</button>					
							<a href="<c:url value='/jobinfos' />" class="btn btn-primary">
								<span class="glyphicon glyphicon-off"></span> <spring:message code="btn.close" />
							</a>
						</div>	
					</div>
	      		</div>		      	
      		</div>
      	</div>	    	   
				
	</form:form>	
   
<script>
	//http://shawnchin.github.io/jquery-cron/
	//http://www.openjs.com/scripts/jslibrary/demos/crontab.php
	//http://www.arnapou.net/tech-js/jquery-jqcron/
	
	$(function(){
		var cron = $('.example-span');
		if (cron.val() == "") {
			//set default
			cron.val('* 0 31 12 *');
		}
				
		$('.example-selector').jqCron({
			enabled_minute: true,
	        multiple_dom: true,
	        multiple_month: true,
	        multiple_mins: true,
	        multiple_dow: true,
	        multiple_time_hours: true,
	        multiple_time_minutes: true,
	        default_period: 'week',
	        default_value: cron.val(), //'* 0 31 12 *',
	        no_reset_button: false,
	        lang: 'en',
	        bind_to: $('.example-span'),
	        bind_method: {
	            set: function($element, value) {
	            	//$element.html(value);
	                $element.val(value);
	            }
	        }
	    });        
	    //.jqCronGetInstance().disable()
	  /*   
	    $('.a5-enable').click(function(e){
	    	cron_gen.enable();
	        e.preventDefault();
	    });
	     
	    $('.a5-disable').click(function(e){
	    	cron_gen.disable();
	        e.preventDefault();
	    });
	     
	    $('.a5-toggle').click(function(e){
	        if(cron_gen.isDisabled())
	        	cron_gen.enable();
	        else
	        	cron_gen.disable();
	        e.preventDefault();
	    });
		*/
	});

</script>
</div>
</body>
</html>