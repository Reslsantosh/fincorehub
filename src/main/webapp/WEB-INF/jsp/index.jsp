
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FinCoreHub | Home</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { overflow-x: hidden; background-color: #f8f9fa;}
        #content { margin-left: 250px; width: calc(100% - 250px); min-height: 100vh;}
        .top-nav { background: #fff; box-shadow: 0 2px 5px rgba(0,0,0,0.1); padding: 15px 30px;}
        .welcome-card { background: #fff; border-radius: 10px; padding: 60px; margin: 50px auto; max-width: 800px; text-align: center; box-shadow: 0 4px 15px rgba(0,0,0,0.05); }
        .rotate { transform: rotate(180deg); }
        .bi-chevron-down { transition: transform 0.3s; }
    </style>
</head>
<body>

<div class="d-flex">
 <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />

    <div id="content">
        <div class="top-nav"><span class="text-muted">Home</span></div>
        <div class="container-fluid">
            <div class="welcome-card">
                <h2 class="text-primary mb-3">Re FinCoreHub System</h2>
                <p class="text-muted">Select a module from the left sidebar to begin managing your records.</p>
                <img src="${pageContext.request.contextPath}/images/logo.png" alt="Re Logo" style="width: 100px;" class="mt-4">
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    $(document).ready(function() {
        $('.menu-toggle').click(function(e) {
            e.preventDefault();
            const subMenu = $(this).next('.sub-menu');
            // Toggle the one you clicked
            subMenu.slideToggle();
            $(this).find('.bi-chevron-down').toggleClass('rotate');
        });
    });
</script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>