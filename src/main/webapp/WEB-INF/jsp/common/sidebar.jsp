<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
    #sidebar { min-width: 250px; max-width: 250px; min-height: 100vh; background: #2c3e50; color: #fff; position: fixed; z-index: 1000; }
   /*  #sidebar .sidebar-header  { padding: 15px; background: #1a252f;textalign: center }  */
   #sidebar .sidebar-header { 
    padding: 15px; 
    background: #1a252f; 
    display: flex;
    justify-content: center; 
    align-items: center;     
    width: 100%;             
}

#sidebar .sidebar-header h2 {
    margin: 0;               
    text-align: center;     
    width: 100%;
}
    #sidebar .nav-link { color: #bdc3c7; padding: 15px 20px; display: flex; align-items: center; justify-content: space-between; text-decoration: none; cursor: pointer; }
    #sidebar .nav-link:hover { background: #2c3e50; color: #fff; } 
    .sub-menu { background: #1a252f; list-style: none; padding: 0; display: none; }
    
    .sub-menu li a { 
        padding: 10px 40px; 
        display: block; 
        color: #95a5a6; 
        text-decoration: none; 
        font-size: 0.9rem; 
        background-color: #2c3e50;
        transition: 0.2s; 
    }

    .sub-menu li a:hover { 
        color: #fff !important; 
        background-color: #1a252f !important; 
    }
     
    .active-link { 
        color: #fff !important; 
        font-weight: bold; 
        background-color: #1a252f !important; 
        border-left: 4px solid #3498db;      
    }
    
    .rotate { transform: rotate(180deg); transition: 0.3s; }
</style>

<nav id="sidebar">
    <div class="sidebar-header"><h5 class="mb-0">Dashboard</h5></div>
    <ul class="nav flex-column mt-3">
        <li class="nav-item">
            <a class="nav-link menu-toggle" id="toggle-gl">
                <span><i class="bi bi-grid-fill me-2"></i> GL Master</span>
                <i class="bi bi-chevron-down small"></i>
            </a>
            <ul class="sub-menu" id="menu-gl">
                <li><a href="${pageContext.request.contextPath}/gl/add">Add New GL</a></li>
                <li><a href="${pageContext.request.contextPath}/gl/records">View GL Records</a></li>
            </ul>
        </li>
        <li class="nav-item">
            <a class="nav-link menu-toggle" id="toggle-pc">
                <span><i class="bi bi-pc-display me-2"></i> PC Master</span>
                <i class="bi bi-chevron-down small"></i>
            </a>
            <ul class="sub-menu" id="menu-pc">
                <li><a href="${pageContext.request.contextPath}/pc/add">Add New PC</a></li>
                <li><a href="${pageContext.request.contextPath}/pc/records">View PC Records</a></li>
            </ul>
        </li>
    </ul>
</nav>

<script>
    $(document).ready(function() {
        // 1. RECOVERY LOGIC
        const openMenuId = sessionStorage.getItem('activeSidebarMenu');
        if (openMenuId) {
            const $targetMenu = $('#' + openMenuId);
            if ($targetMenu.length) {
                $targetMenu.show(); 
                $targetMenu.prev('.menu-toggle').find('.bi-chevron-down').addClass('rotate');
            }
        }

        // 2. FIXED HIGHLIGHT LOGIC
        const currentPath = window.location.pathname;
        $('.sub-menu li a').removeClass('active-link'); 
        $('.sub-menu li a').each(function() {
            if ($(this).attr('href') === currentPath) {
                $(this).addClass('active-link');
                
                const $parent = $(this).closest('.sub-menu');
                $parent.show();
                $parent.prev('.menu-toggle').find('.bi-chevron-down').addClass('rotate');
                sessionStorage.setItem('activeSidebarMenu', $parent.attr('id'));
            }
        });

        // 3. TOGGLE LOGIC
        $('body').off('click', '.menu-toggle').on('click', '.menu-toggle', function(e) {
            e.preventDefault();
            const $this = $(this);
            const $subMenu = $this.next('.sub-menu');
            const menuId = $subMenu.attr('id');

            if ($subMenu.is(':visible')) {
                $subMenu.slideUp();
                $this.find('.bi-chevron-down').removeClass('rotate');
                sessionStorage.removeItem('activeSidebarMenu');
            } else {
                $('.sub-menu').not($subMenu).slideUp();
                $('.bi-chevron-down').not($this.find('.bi-chevron-down')).removeClass('rotate');

                $subMenu.slideDown();
                $this.find('.bi-chevron-down').addClass('rotate');
                sessionStorage.setItem('activeSidebarMenu', menuId);
            }
        });
    });
</script>