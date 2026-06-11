<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Re FinCoreHub | Login</title>
    
    <script src="https://accounts.google.com/gsi/client"></script>
    
    <link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/images/favicon.ico">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        body {
            background-color: #fcfcfc;
            height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            font-family: 'Segoe UI', Arial, sans-serif;
            color: #333;
            margin: 0;
        }
        .login-card {
            width: 100%;
            max-width: 420px;
            background: #fff;
            padding: 30px 35px;
            border-radius: 8px;
            box-shadow: 0 4px 20px rgba(0,0,0,0.03);
            text-align: center;
        }
        .logo-img { width: 80px; margin-bottom: 5px; }
        .app-title { 
            font-size: 2.5rem; 
            font-weight: 800; 
            color: #000; 
            margin-bottom: 10px; 
            letter-spacing: -1.5px; 
            line-height: 1;
        }
        hr { border-top: 1px solid #e0e0e0; opacity: 1; margin: 20px 0; }
        
        .g_id_signin {
            display: flex !important;
            justify-content: center;
            margin: 20px 0;
            min-height: 40px;
        }
        
        .info-text { 
            font-size: 0.95rem; 
            color: #70757a; 
            margin-top: 25px; 
        }
        .email-domain { 
            color: #5f6368; 
            font-weight: 700; 
            font-size: 1.05rem; 
            display: block; 
            margin-top: 2px; 
        }
        .helpdesk-link { 
            color: #1a73e8; 
            text-decoration: none; 
            font-weight: 500; 
            font-size: 0.9rem; 
        }
        .footer-text { 
            font-size: 0.85rem; 
            color: #70757a; 
            margin-top: 25px;
            border-top: 1px solid #f0f0f0; 
            padding-top: 20px; 
            line-height: 1.4;
        }
        .powered-by { 
            display: flex; 
            align-items: center; 
            justify-content: center; 
            gap: 8px; 
            margin-top: 15px; 
            color: #5f6368; 
            font-weight: 600; 
            font-size: 1rem;
        }
        .powered-by img { width: 45px; }
        .copyright-section { 
            color: #5f6368; 
            margin-top: 10px; 
            margin-bottom: 5px; 
            font-size: 0.8rem; 
        }
        .policy-links a { 
            color: #1a73e8; 
            text-decoration: none; 
            margin: 0 8px; 
            font-weight: 500; 
            font-size: 0.8rem;
        }
        .policy-links a:hover { text-decoration: underline; }
        
        .alert-container {
            margin-top: 15px;
            display: none;
        }
    </style>
</head>
<body>

<div class="login-card">
    <img src="${pageContext.request.contextPath}/images/logo.png" alt="Re Logo" class="logo-img">
    <div class="app-title">FinCoreHub</div>
    <hr>

    <div id="g_id_onload"
         data-client_id="${googleClientId}"
         data-context="signin"
         data-ux_mode="popup"
         data-callback="handleCredentialResponse"
         data-auto_prompt="false">
    </div>
    
    <div class="g_id_signin"
         data-type="standard"
         data-shape="rectangular"
         data-theme="outline"
         data-text="signin_with"
         data-size="large"
         data-logo_alignment="left"
         data-width="300">
    </div>

    <div id="alertContainer" class="alert-container alert alert-danger" role="alert"></div>

    <div class="info-text">
        <p class="mb-0">Ensure username should be your Email ID</p>
        <span class="email-domain">@resustainability.com</span>
        
        <p class="mt-3 mb-0">In case of any login issue, connect on</p>
        <a href="mailto:it.helpdesk@resustainability.com" class="helpdesk-link">it.helpdesk@resustainability.com</a>
    </div>

    <div class="footer-text">
        <p class="px-2 mb-0">Signing up in this Enterprise Portal confirms your acceptance to ReSL IT Application Usage Policy</p>
        
        <div class="powered-by">
            <span>Powered by</span>
            <img src="${pageContext.request.contextPath}/images/logo.png" alt="RE Logo Small">
        </div>

        <p class="copyright-section">
            Re Sustainability Ltd &copy; <span id="currentYear"></span>. All Rights Reserved.
        </p>
        
        <div class="policy-links">
            <a href="https://resustainability.com/privacy-policy/" target="_blank">Privacy Policy</a>
            <a href="https://resustainability.com/terms-of-service/" target="_blank">Term of service</a>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script>
    document.getElementById('currentYear').textContent = new Date().getFullYear();

    function handleCredentialResponse(response) {
        const idToken = response.credential;
        
        const signinButton = document.querySelector('.g_id_signin');
        signinButton.style.opacity = '0.5';
        signinButton.style.pointerEvents = 'none';
        
        fetch('${pageContext.request.contextPath}/api/auth/g-login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                idToken: idToken
            }),
            credentials: 'same-origin'
        })
        .then(response => {
            if (!response.ok) {
                return response.json().then(err => { throw err; });
            }
            return response.json();
        })
        .then(data => {
            window.location.href = '${pageContext.request.contextPath}/';
        })
        .catch(error => {
            console.error('Login failed:', error);
            showError(error.error || 'Login failed. Please try again.');
            
            signinButton.style.opacity = '1';
            signinButton.style.pointerEvents = 'auto';
        });
    }

    function showError(message) {
        const alertContainer = document.getElementById('alertContainer');
        alertContainer.textContent = message;
        alertContainer.style.display = 'block';
        
        setTimeout(() => {
            alertContainer.style.display = 'none';
        }, 5000);
    }
</script>

</body>
</html>
