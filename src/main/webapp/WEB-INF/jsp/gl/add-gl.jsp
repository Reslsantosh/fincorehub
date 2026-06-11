
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FinCoreHub | Add GL</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { overflow-x: hidden; background-color: #f8f9fa; }
        #content { margin-left: 250px; width: calc(100% - 250px);min-height: 100vh }
        .top-nav { background: #fff; box-shadow: 0 2px 5px rgba(0,0,0,0.1); padding: 15px 30px; }
        .rotate { transform: rotate(180deg); }
    </style>
</head>
<body>
<div class="d-flex">
  <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div id="content">
        <div class="top-nav"><span class="text-muted">GL Master / Add Record</span></div>
       <div class="container-fluid mt-4">
    <div class="row g-4">
        <div class="col-md-8">
            <div class="card shadow-sm border-0 h-100">
                <div class="card-header bg-white py-3">
                    <h5 class="mb-0 text-primary fw-bold">Add General Ledger Record</h5>
                </div>
                <div class="card-body p-4">
                    <form id="glForm">
                        <div class="row mb-3">
                            <div class="col-md-6">
                                <label class="form-label">Account Number</label>
                                <input placeholder="e.g., 1234567890" type="text" pattern="[0-9]*" inputmode="numeric" name="accountNumber" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Type</label>
                                <input placeholder="e.g., Revenue" type="text" name="type" class="form-control" required>
                            </div>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <input placeholder="e.g., State Bank Of India" type="text" name="description" class="form-control" required>
                        </div>
                        <div class="mb-3">
                            <label class="form-label">Console Group</label>
                            <input placeholder="e.g., Cash and bank balances" type="text" name="consoleGroup" class="form-control" required>
                        </div>
                        <div class="row mb-4">
                            <div class="col-md-6">
                                <label class="form-label">BU</label>
                                <input placeholder="e.g., BMW" type="text" name="bu" class="form-control" required>
                            </div>
                            <div class="col-md-6">
                                <label class="form-label">Category Group</label>
                                <input placeholder="e.g., Audit fee" type="text" name="categoryGroup" class="form-control" required>
                            </div>
                        </div>
                        <div class="d-flex gap-2 justify-content-end">
                            <a href="/fincorehub/" class="btn btn-light border px-4">Cancel</a>
                            <button type="submit" class="btn btn-primary px-5">Save Record</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <div class="col-md-4">
    <div class="card shadow-sm border-0 h-100">
        <div class="card-header bg-success text-white py-3">
            <h5 class="mb-0 fw-bold"><i class="bi bi-file-earmark-excel"></i> Bulk Upload</h5>
        </div>
        <div class="card-body p-4 d-flex flex-column justify-content-between">
            <div>
                <p class="text-muted small">Upload an Excel file (.xlsx) to add multiple records at once.</p>
 <div class="alert alert-warning py-2 px-3 mb-4" style="font-size: 0.75rem;">
    <strong>Upload Rules:</strong><br>
    - Maximum <strong>500 rows</strong> per upload.<br>
    - Required Column Order:1. Account Number | 2. Description | 3. Type | 4. Console Group | 5. BU | 6. Category Group.
</div>
                <div class="mb-4">
                    <label class="form-label fw-bold">Select Excel File</label>
                    <input type="file" id="excelFile" class="form-control" accept=".xlsx, .xls">
                </div>
            </div>
            <button type="button" id="uploadExcelBtn" class="btn btn-success w-100 py-2">
                <i class="bi bi-cloud-arrow-up"></i> Upload & Process
            </button>
        </div>
    </div>
</div>
    </div>
</div> 
        
        
    </div>
</div>
<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>


<script>
    $(document).ready(function() {
        // Toggle Sidebar Menus
        $('.menu-toggle').click(function() {
            $(this).next('.sub-menu').slideToggle();
            $(this).find('.bi-chevron-down').toggleClass('rotate');
        }); 

        // Manual Form Submit
        $('#glForm').on('submit', function(e) {
            e.preventDefault();
            const formData = {};
            $(this).serializeArray().forEach(item => formData[item.name] = item.value);
            
            $.ajax({
                url: '${pageContext.request.contextPath}/api/gl/add',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response) { 
                    // Based on your APIResponse: data, message, error
                    alert(response.message || 'Record Added Successfully!'); 
                    window.location.href = '${pageContext.request.contextPath}/gl/add'; 
                },
                error: function(xhr) { 
                    const resp = xhr.responseJSON;
                    alert('Error: ' + (resp && resp.error ? resp.error : 'Failed to add record')); 
                }
            });
        });


     // GL Master Excel Upload
        $('#uploadExcelBtn').on('click', function() {
            const fileInput = $('#excelFile')[0].files[0];
            if (!fileInput) {
                alert("Please select an Excel file.");
                return;
            }

            // Proxy check for file size (1MB limit)
            if (fileInput.size > 1024 * 1024) { 
                alert("The file is too large. Please limit your upload to 500 rows.");
                return;
            }

            const formData = new FormData();
            formData.append("file", fileInput);

            const $btn = $(this);
            // Disable button and show spinner
            $btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Processing...');

            $.ajax({
                url: '${pageContext.request.contextPath}/api/gl/bulk-upload',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    // Checks for logical errors returned with a 200 OK status
                    if (response.error) {
                        alert(response.error); 
                        $btn.prop('disabled', false).html('<i class="bi bi-cloud-arrow-up"></i> Upload & Process');
                    } else {
                        // Success case
                        alert(response.message + "\n" + (response.data || ""));
                        window.location.reload();
                    }
                },
                error: function(xhr) {
                    // Catches 500 status and prevents Whitelabel Error Page redirect
                    const resp = xhr.responseJSON;
                    const errorMsg = (resp && resp.error) ? resp.error : 'Internal Server Error';
                    
                    // Displays the alert exactly as shown in your PC Master screenshot
                    alert('Error processing excel: ' + errorMsg);
                    
                    // Reset button state
                    $btn.prop('disabled', false).html('<i class="bi bi-cloud-arrow-up"></i> Upload & Process');
                }
            });
        });
    });
</script>
<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>