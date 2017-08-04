 <%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>  
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<c:set var="url" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html lang="en">
<head>
  <title>Bootstrap Example</title>
  <meta charset="utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link href="${url}/resources/css/bootstrap.min.css" rel="stylesheet"/>
  <link href="${url}/resources/css/bootstrap-theme.min.css" rel="stylesheet" />

<link rel="stylesheet" type="text/css" href="${url}/resources/css/itsSpring.css"/>
<link rel="stylesheet" type="text/css" href="${url}/resources/css/flip-scroll.css"/>
<link rel="stylesheet" type="text/css" href="${url}/resources/css/signin.css"/>
<link rel="stylesheet" type="text/css" href="${url}/resources/css/menu.css"/>
<!-- angularjs for users_angularjs -->
<!--  
<script src="${url}/resources/js/angular-1.5.3.min.js"></script>
<script src="${url}/resources/js/angular-animate-1.5.3.min.js"></script>
<script src="${url}/resources/js/ui-bootstrap-tpls-1.3.3.min.js"></script>
<script src="${url}/resources/js/angularjs-modal.js"></script>
-->
<!-- jquery -->
<script src="${url}/resources/js/jquery.min.js"></script>
<!-- <script src="${url}/resources/js/jquery.maskMoney.min.js"></script>  -->
<script src="${url}/resources/js/jquery.inputmask.min.js"></script>
<script src="${url}/resources/js/bootstrap.min.js"></script>

<!-- boot -->
<script src="${url}/resources/js/bootbox.min.js"></script>

<!-- cron expression -->
<!-- <link rel="stylesheet" type="text/css" href="${url}/resources/css/jquery-cron.css"/>
<script src="${url}/resources/js/jquery-cron-min.js"></script> -->


<!-- jqcron expression -->
<link rel="stylesheet" type="text/css" href="${url}/resources/css/jqdemo.css"/>
<link rel="stylesheet" type="text/css" href="${url}/resources/css/jqreset.css"/>
<link rel="stylesheet" type="text/css" href="${url}/resources/css/jqCron.css"/>
<script src="${url}/resources/js/jqCron.js"></script>
<script src="${url}/resources/js/jqCron.en.js"></script>

<!-- its system -->
<script src="${url}/resources/js/its.js"></script>

</head>
<body>


<div class="ita_form">   
<div class="container">
  <h1>Input Group Button</h1>
  <p>chào bạnclass is a container to enhance an input by adding an icon, text or a button in front or behind it as a "help text".</p>
  <p>The .input-group-btn class attaches a button next to an input field. This is often used as a search bar:</p>
  <form action="/action_page.php">
    <div class="input-group">
      <input type="text" class="form-control" placeholder="Search" name="search">
      <div class="input-group-btn">
        <button class="btn btn-default" type="submit"><i class="glyphicon glyphicon-search"></i></button>
      </div>
    </div>
  </form>
</div>
</div>
</body>
</html>
