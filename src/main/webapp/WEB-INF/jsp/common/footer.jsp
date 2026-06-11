<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<style>
    .footer {
        position: fixed;
        left: 0;
        bottom: 0;
        width: 100%;
        background-color: #1a252f; 
        color: #bdc3c7; 
        text-align: center;
        padding: 10px 0;
        font-size: 0.85rem;
        z-index: 1100; 
        border-top: 1px solid #34495e;
    }
</style>
</head>
<body>
<footer class="footer">
    <div class="container">
        <span>&copy; <span id="displayYear"></span>Re FinCoreHub System. All rights reserved.</span>
    </div>
</footer>

<script>
    document.getElementById('displayYear').textContent = new Date().getFullYear();
</script>

</body>
</html>