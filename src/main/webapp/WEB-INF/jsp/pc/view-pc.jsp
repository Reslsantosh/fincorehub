<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>FinCoreHub | PC Records</title>
    <%@ include file="/WEB-INF/jsp/common/head.jsp" %>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.0/font/bootstrap-icons.css">
    <style>
        body { overflow-x: hidden; background-color: #f8f9fa; }
        #content { margin-left: 250px; width: calc(100% - 250px); min-height: 100vh; padding-bottom: 60px; }
        .top-nav { background: #fff; box-shadow: 0 2px 5px rgba(0,0,0,0.1); padding: 15px 30px; }
        .rotate { transform: rotate(180deg); transition: 0.3s; }
        .pagination-footer { display: flex; justify-content: space-between; align-items: center; padding: 15px; background: #fff; border-top: 1px solid #dee2e6; }
        .table-card { border-radius: 10px; overflow: hidden; }
        .pagination .page-link { min-width: 38px; text-align: center; }
    </style>
</head>
<body>
<div class="d-flex">
    <jsp:include page="/WEB-INF/jsp/common/sidebar.jsp" />

    <div id="content">
        <div class="top-nav"><span>PC Master / Records</span></div>
        <div class="container-fluid mt-4">
            <div class="card shadow-sm border-0 table-card">
                <div class="card-header bg-white py-3 d-flex justify-content-between align-items-center">
                    <div class="d-flex align-items-center">
                        <a href="/fincorehub/" class="btn btn-outline-secondary btn-sm me-3"><i class="bi bi-arrow-left"></i> Back</a>
                        <h5 class="mb-0 text-primary fw-bold">PC Records</h5>
                    </div>
                    <div class="d-flex align-items-center gap-2">
                        <span class="small text-muted">Show</span>
                        <select id="rowLimit" class="form-select form-select-sm" style="width: 70px;">
                            <option value="10" selected>10</option>
                            <option value="25">25</option>
                            <option value="50">50</option>
                        </select>
                    </div>
                </div>
                <div class="card-body p-0">
                    <table class="table table-hover align-middle mb-0">
                        <thead class="table-light">
                            <tr>
                                <th>ID</th>
                                <th>PC Code</th>
                                <th>PC Name</th>
                                <th>Site (MIS)</th>
                                <th>Unit</th>
                                <th>SBU</th>
                                <th>BU</th>
                                <th class="text-center">Action</th>
                            </tr>
                        </thead>
                        <tbody id="pcTableBody"></tbody>
                    </table>
                </div>
                <div class="pagination-footer">
                    <div class="text-muted small" id="recordCountInfo">Showing 0 to 0 of 0 entries</div>
                    <nav><ul class="pagination pagination-sm mb-0" id="paginationNav"></ul></nav>
                </div>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editModal" tabindex="-1" aria-labelledby="editModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg modal-dialog-centered">
        <div class="modal-content border-0 shadow">            
            <div class="modal-header bg-white border-bottom">
                <h5 class="modal-title fw-bold text-primary" id="editModalLabel"><i class="bi bi-pencil-square me-2"></i>Edit PC Record</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
            </div>
            <form id="editPcForm">
                <div class="modal-body p-4">
                    <input type="hidden" id="editPcId">
                    <div class="row g-3">
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Profit Centre</label>
                            <input type="text" id="editProfitCentre" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Profit Centre Name</label>
                            <input type="text" id="editProfitCentreName" class="form-control" required>
                        </div>
                        <div class="col-12">
                            <label class="form-label fw-bold">Site Name for MIS</label>
                            <input type="text" id="editSiteNameForMIS" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">SBU Final</label>
                            <input type="text" id="editSbuFinal" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">Unit</label>
                            <input type="text" id="editUnit" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">SBU</label>
                            <input type="text" id="editSbu" class="form-control" required>
                        </div>
                        <div class="col-md-6">
                            <label class="form-label fw-bold">BU</label>
                            <input type="text" id="editBu" class="form-control" required>
                        </div>
                    </div>
                </div>
                <div class="modal-footer bg-light">
                    <button type="button" class="btn btn-secondary px-4" data-bs-dismiss="modal">Close</button>
                    <button type="submit" class="btn btn-primary px-4">Update Changes</button>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<script>
    function loadRecords(pageNumber) {
        const pageSize = parseInt($('#rowLimit').val()); 
        const tableBody = $('#pcTableBody'); 

        // Loading spinner
        tableBody.html(`
            <tr>
                <td colspan="8" class="text-center py-5">
                    <div class="spinner-border spinner-border-sm text-primary me-2" role="status"></div>
                    <span class="text-muted">Loading data...</span>
                </td>
            </tr>
        `);

        $.ajax({
            url: '${pageContext.request.contextPath}/api/pc/list',
            method: 'GET',
            data: { page: pageNumber, size: pageSize },
            success: function(res) {
                let displayData = (res.data && res.data.content) ? res.data.content : []; 
                const totalElements = res.data ? res.data.totalElements : 0; 
                const totalPages = res.data ? res.data.totalPages : 0;

                if (!displayData || displayData.length === 0) {
                    tableBody.html('<tr><td colspan="8" class="text-center py-5 text-muted">No data available</td></tr>');
                    $('#recordCountInfo').text('Showing 0 to 0 of 0 entries');
                    $('#paginationNav').empty();
                    return; 
                }

                const startLabel = (pageNumber * pageSize) + 1;
                const endLabel = (pageNumber * pageSize) + displayData.length;
                $('#recordCountInfo').text(`Showing \${startLabel} to \${endLabel} of \${totalElements} entries`);

                let html = '';
                displayData.forEach(item => {
                    const itemJson = JSON.stringify(item).replace(/"/g, '&quot;');
                    html += `<tr>
                        <td>\${item.id}</td>
                        <td>\${item.profitCentre}</td>
                        <td>\${item.profitCentreName}</td>
                        <td>\${item.siteNameForMIS}</td>
                        <td>\${item.unit}</td>
                        <td>\${item.sbu}</td>
                        <td>\${item.bu}</td>
                        <td class="text-center">
                            <button class="btn btn-sm btn-outline-primary" onclick='openEditModal(\${itemJson})'>
                                <i class="bi bi-pencil-square"></i> Edit
                            </button>
                        </td>
                    </tr>`;
                });
                
                tableBody.html(html);
                renderPagination(totalPages, pageNumber);
            },
            error: function(xhr) {
                tableBody.html(`<tr><td colspan="8" class="text-center py-4 text-danger">Error loading data (Status: \${xhr.status})</td></tr>`);
            }
        });
    }

    function renderPagination(total, active) {
        let html = '';
        
        // Previous Button
        html += `<li class="page-item \${active === 0 ? 'disabled' : ''}">
                    <a class="page-link" href="javascript:void(0)" onclick="loadRecords(\${active - 1})">Previous</a>
                 </li>`;
        
        const range = 2; // Sliding window range
        
        // Show first page and ellipsis
        if (active > range) {
            html += `<li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="loadRecords(0)">1</a></li>`;
            if (active > range + 1) {
                html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
            }
        }

        // Current range of pages
        for (let i = Math.max(0, active - range); i <= Math.min(total - 1, active + range); i++) {
            html += `<li class="page-item \${i === active ? 'active' : ''}">
                        <a class="page-link" href="javascript:void(0)" onclick="loadRecords(\${i})">\${i + 1}</a>
                     </li>`;
        }

        // Show last page and ellipsis
        if (active < total - range - 1) {
            if (active < total - range - 2) {
                html += `<li class="page-item disabled"><span class="page-link">...</span></li>`;
            }
            html += `<li class="page-item"><a class="page-link" href="javascript:void(0)" onclick="loadRecords(\${total - 1})">\${total}</a></li>`;
        }
        
        // Next Button
        html += `<li class="page-item \${active >= total - 1 || total === 0 ? 'disabled' : ''}">
                    <a class="page-link" href="javascript:void(0)" onclick="loadRecords(\${active + 1})">Next</a>
                 </li>`;
        
        $('#paginationNav').html(html);
    }

    function openEditModal(item) {
        $('#editPcId').val(item.id);
        $('#editProfitCentre').val(item.profitCentre);
        $('#editProfitCentreName').val(item.profitCentreName);
        $('#editSiteNameForMIS').val(item.siteNameForMIS);
        $('#editSbuFinal').val(item.sbuFinal);
        $('#editUnit').val(item.unit);
        $('#editSbu').val(item.sbu);
        $('#editBu').val(item.bu);
        
        const myModal = new bootstrap.Modal(document.getElementById('editModal'));
        myModal.show();
    }

    $(document).ready(function() {
        // Sidebar Toggle Logic
        $('.menu-toggle').click(function(e) {
            e.preventDefault();
            $(this).next('.sub-menu').slideToggle();
            $(this).find('.bi-chevron-down').toggleClass('rotate');
        }); 

        // Load data on start
        loadRecords(0); 

        // Row limit change handler
        $('#rowLimit').on('change', function() {
            loadRecords(0);
        });

        // Form Submission Logic
        $('#editPcForm').on('submit', function(e) {
            e.preventDefault();
            const pcId = $('#editPcId').val(); 
            const updatedData = {
                profitCentre: $('#editProfitCentre').val(),
                profitCentreName: $('#editProfitCentreName').val(),
                siteNameForMIS: $('#editSiteNameForMIS').val(),
                sbuFinal: $('#editSbuFinal').val(),
                unit: $('#editUnit').val(),
                sbu: $('#editSbu').val(),
                bu: $('#editBu').val()
            };

            $.ajax({
                url: '${pageContext.request.contextPath}/api/pc/update/' + pcId, 
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(updatedData),
                success: function(response) {
                    alert(response.message || 'PC Record updated successfully!');
                    const modalElement = document.getElementById('editModal');
                    const modalInstance = bootstrap.Modal.getInstance(modalElement);
                    modalInstance.hide();
                    loadRecords(0);
                },
                error: function(xhr) {
                    alert('Failed to update. Error: ' + xhr.status);
                }
            });
        });
    });
</script>

<jsp:include page="/WEB-INF/jsp/common/footer.jsp" />
</body>
</html>