
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FinCoreHub | Add PC</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { overflow-x: hidden; background-color: #f8f9fa; }
        #content { margin-left: 250px; width: calc(100% - 250px); min-height: 100vh; }
        .top-nav { background: #fff; box-shadow: 0 2px 5px rgba(0,0,0,0.1); padding: 15px 30px; }
        .rotate { transform: rotate(180deg); }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />
    <div id="content">
        <div class="top-nav"><span class="text-muted">PC Master / Add Record</span></div>
        <div class="container-fluid mt-4">
            <div class="row g-4">
                <div class="col-md-8">
                    <div class="card shadow-sm border-0 h-100">
                        <div class="card-header bg-white py-3">
                            <h5 class="mb-0 text-primary fw-bold">Add Profit Centre Record</h5>
                        </div>
                        <div class="card-body p-4">
                            <form id="pcForm">
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">Profit Centre</label>
                                        <input placeholder="e.g. 1234567890" type="text" pattern="[0-9]*" inputmode="numeric" name="profitCentre" class="form-control" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Profit Centre Name</label>
                                        <input placeholder="e.g. REEL Corporate ofc" type="text" name="profitCentreName" class="form-control" required>
                                    </div>
                                </div>
                                <div class="mb-3">
                                    <label class="form-label">Site Name for MIS</label>
                                    <input placeholder="e.g. IES, CRM" type="text" name="siteNameForMIS" class="form-control" required>
                                </div>
                                <div class="row mb-3">
                                    <div class="col-md-6">
                                        <label class="form-label">SBU Final</label>
                                        <input placeholder="e.g. IES, CRM" type="text" name="sbuFinal" class="form-control" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">Unit</label>
                                        <input placeholder="e.g. Corporate" type="text" name="unit" class="form-control" required>
                                    </div>
                                </div>
                                <div class="row mb-4">
                                    <div class="col-md-6">
                                        <label class="form-label">SBU</label>
                                        <input placeholder="e.g. IES" type="text" name="sbu" class="form-control" required>
                                    </div>
                                    <div class="col-md-6">
                                        <label class="form-label">BU</label>
                                        <input placeholder="e.g. CRM" type="text" name="bu" class="form-control" required>
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
                                <p class="text-muted small">Upload an Excel file (.xlsx) for multiple PC records.</p>                                
                                <div class="alert alert-warning py-2 px-3 mb-4" style="font-size: 0.75rem;">
                                   <strong>Upload Rules:</strong><br>
                                    - Maximum <strong>500 rows</strong> per upload.<br>
                                    - Required Column Order: 1. PC | 2. PC Name | 3. Site MIS | 4. SBU Final | 5. Unit | 6. SBU | 7. BU
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
        $('.menu-toggle').click(function() {
            $(this).next('.sub-menu').slideToggle();
            $(this).find('.bi-chevron-down').toggleClass('rotate');
        }); 

        $('#pcForm').on('submit', function(e) {
            e.preventDefault();
            const formData = {};
            $(this).serializeArray().forEach(item => { formData[item.name] = item.value; });

            $.ajax({
                url: '${pageContext.request.contextPath}/api/pc/add',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify(formData),
                success: function(response) { 
                    alert(response.message || 'PC Record Added Successfully!'); 
                    window.location.href = '${pageContext.request.contextPath}/pc/add'; 
                },
                error: function(xhr) { 
                    const resp = xhr.responseJSON;
                    alert('Error: ' + (resp && resp.error ? resp.error : 'Failed to add record')); 
                }
            });
        });

        
     // Excel Upload Logic
        $('#uploadExcelBtn').on('click', function() {
            const fileInput = $('#excelFile')[0].files[0];
            if (!fileInput) {
                alert("Please select an Excel file.");
                return;
            }

            // Optional: Check file size as a proxy for row count before sending
            // 1MB is usually plenty for 500 rows of text data
            if (fileInput.size > 1024 * 1024) { 
                alert("The file is too large. Please limit your upload to 500 rows.");
                return;
            }

            const formData = new FormData();
            formData.append("file", fileInput);

            const $btn = $(this);
            $btn.prop('disabled', true).html('<span class="spinner-border spinner-border-sm"></span> Processing...');

            $.ajax({
                url: '${pageContext.request.contextPath}/api/pc/bulk-upload',
                type: 'POST',
                data: formData,
                processData: false,
                contentType: false,
                success: function(response) {
                    // response.error will be true if your catch block in Controller returns it
                    if (response.error) {
                        alert(response.error); // This shows the "Limit exceeded" or validation message
                        $btn.prop('disabled', false).html('<i class="bi bi-cloud-arrow-up"></i> Upload & Process');
                    } else {
                        alert(response.message + "\n" + (response.data || ""));
                        window.location.reload();
                    }
                },
                error: function(xhr) {
                    // Catches 500 status and displays the error message from the Service/Controller
                    const resp = xhr.responseJSON;
                    const errorMsg = (resp && resp.error) ? resp.error : 'Internal Server Error';
                    alert('Upload failed: ' + errorMsg);
                    $btn.prop('disabled', false).html('<i class="bi bi-cloud-arrow-up"></i> Upload & Process');
                }
            });
        });
    });
</script> 
<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>