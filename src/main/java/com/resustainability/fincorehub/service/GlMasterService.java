package com.resustainability.fincorehub.service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.resustainability.fincorehub.commons.Default;
import com.resustainability.fincorehub.config.SecurityUtils;
import com.resustainability.fincorehub.entity.GlMaster;
import com.resustainability.fincorehub.entity.RevenueBudgetMaster;
import com.resustainability.fincorehub.exception.InvalidDataException;
import com.resustainability.fincorehub.exception.ResourceNotFoundException;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.repository.GlMasterRepository;
import com.resustainability.fincorehub.request.addGlMasterRequest;
import com.resustainability.fincorehub.request.updateGlRequest;
import com.resustainability.fincorehub.response.IGlMasterResponse;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;



@Service
public class GlMasterService {
	
	private static final int MAX_ROW_LIMIT = 500;

  
    private final GlMasterRepository glRepository;
    
    
    public GlMasterService(GlMasterRepository glRepository) {
    	this.glRepository = glRepository;
    	
    }

    
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public GlMaster addGl(addGlMasterRequest request) {
    	
    	  GlMaster gl = new GlMaster();

    	    gl.setAccountNumber(request.accountNumber());
    	    gl.setDescription(request.description());
    	    gl.setType(request.type());
    	    gl.setConsoleGroup(request.consoleGroup());
    	    gl.setBu(request.bu());
    	    gl.setCategoryGroup(request.categoryGroup());
    	    gl.setPlHeaders(request.plHeaders());

    	    return glRepository.save(gl);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public Pager<IGlMasterResponse> list(
            String accountNumber,
            String bu,
            SearchCriteria searchCriteria) {

        return Pager.of(
            glRepository.findAllGl(
                accountNumber,
                bu,
                searchCriteria.toPageRequest()
            )
        );
    }
    

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public IGlMasterResponse getById(Long glId) {

        return glRepository.findByGlId(glId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Default.ERROR_NOT_FOUND_GL + glId)
                );
    }
    

	
    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String updateGl(Long id, updateGlRequest request) {

        glRepository.findByGlId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Default.ERROR_NOT_FOUND_GL + id)
                );

        int updated = glRepository.updateGl(
                id,
                request.accountNumber(),
                request.description(),
                request.type(),
                request.consoleGroup(),
                request.bu(),
                request.categoryGroup(),
                SecurityUtils.getCurrentUser(),   
                LocalDateTime.now(), 
                request.plHeaders()
        );

        if (updated == 0) {
            throw new InvalidDataException(Default.ERROR_FAILED);
        }

        return Default.SUCCESS_UPDATE_GL;
    }
    
    
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String deleteGl(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {

            throw new InvalidDataException(
                    "Please select at least one record"
            );
        }

        List<GlMaster> gls =
        		glRepository.findAllById(ids);

        if (gls.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No GL Records Found"
            );
        }

        glRepository.deleteAll(gls);

        return gls.size()
                + " GL Record(s) Deleted Successfully";
    }

 
    
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String uploadFromExcel(MultipartFile file) throws Exception {

        List<GlMaster> glList = new ArrayList<>();
        DataFormatter formatter = new DataFormatter();

        // ✅ Support BOTH old .xls and new .xlsx
        Workbook workbook;

        try {
            String fileName = file.getOriginalFilename();

            if (fileName == null) {
                throw new Exception("Invalid file.");
            }

            if (fileName.toLowerCase().endsWith(".xlsx")) {

                workbook = new XSSFWorkbook(file.getInputStream());

            } else if (fileName.toLowerCase().endsWith(".xls")) {

                workbook = new HSSFWorkbook(file.getInputStream());

            } else {

                throw new Exception("Only .xls and .xlsx files are allowed.");
            }

        } catch (Exception e) {
            throw new Exception("Failed to read Excel file. Please upload a valid Excel file.");
        }

        try (workbook) {

            Sheet sheet = workbook.getSheetAt(0);

            if (sheet == null) {
                throw new Exception("Excel sheet is empty.");
            }

            // =========================================
            // ✅ VALIDATE HEADER
            // =========================================
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new Exception("Excel file is missing header row.");
            }

            List<String> expectedHeaders = List.of(
                    "Account Number",
                    "Description",
                    "Type",
                    "Console Group",
                    "BU",
                    "Category Group",
                    "P&L Headers"
            );

            // Validate column count
            if (headerRow.getLastCellNum() != expectedHeaders.size()) {

                throw new Exception(
                        "Invalid number of columns in Excel. Expected "
                                + expectedHeaders.size()
                                + " columns."
                );
            }

            // Validate column order
            for (int i = 0; i < expectedHeaders.size(); i++) {

                String actualHeader =
                        formatter.formatCellValue(headerRow.getCell(i)).trim();

                if (!expectedHeaders.get(i).equalsIgnoreCase(actualHeader)) {

                    throw new Exception(
                            "Invalid column order at column "
                                    + (i + 1)
                                    + ". Expected: '"
                                    + expectedHeaders.get(i)
                                    + "' but found: '"
                                    + actualHeader
                                    + "'"
                    );
                }
            }

            // =========================================
            // ✅ VALIDATE DATA EXISTS
            // =========================================
            int physicalRows = sheet.getPhysicalNumberOfRows();

            if (physicalRows <= 1) {
                throw new Exception("The Excel file contains no data rows.");
            }

            int lastRowIndex = sheet.getLastRowNum();

            int dataRowCount = 0;

            for (int i = 1; i <= lastRowIndex; i++) {

                Row row = sheet.getRow(i);

                // Skip empty rows
                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                dataRowCount++;

                // =========================================
                // ✅ MAX LIMIT CHECK
                // =========================================
                if (dataRowCount > MAX_ROW_LIMIT) {

                    throw new Exception(
                            "Limit exceeded: Max "
                                    + MAX_ROW_LIMIT
                                    + " records allowed."
                    );
                }

                int displayRow = i + 1;

                GlMaster gl = new GlMaster();

                // =========================================
                // ✅ ACCOUNT NUMBER
                // =========================================
                String accStr = formatter
                        .formatCellValue(row.getCell(0))
                        .replaceAll("[^0-9]", "");

                if (accStr.isEmpty()) {
                    throw new Exception(
                            "Account Number is missing at row "
                                    + displayRow
                    );
                }

                try {
                    gl.setAccountNumber(Long.parseLong(accStr));
                } catch (NumberFormatException ex) {
                    throw new Exception(
                            "Invalid Account Number at row "
                                    + displayRow
                    );
                }

                // =========================================
                // ✅ DESCRIPTION
                // =========================================
                String description =
                        formatter.formatCellValue(row.getCell(1)).trim();

                if (description.isEmpty()) {
                    throw new Exception(
                            "Description is missing at row "
                                    + displayRow
                    );
                }

                gl.setDescription(description);

                // =========================================
                // ✅ TYPE
                // =========================================
                String type =
                        formatter.formatCellValue(row.getCell(2)).trim();

                if (type.isEmpty()) {
                    throw new Exception(
                            "Type is missing at row "
                                    + displayRow
                    );
                }

                gl.setType(type);

                // =========================================
                // ✅ CONSOLE GROUP
                // =========================================
                String consoleGroup =
                        formatter.formatCellValue(row.getCell(3)).trim();

                if (consoleGroup.isEmpty()) {
                    throw new Exception(
                            "Console Group is missing at row "
                                    + displayRow
                    );
                }

                gl.setConsoleGroup(consoleGroup);

                // =========================================
                // ✅ BU
                // =========================================
                String bu =
                        formatter.formatCellValue(row.getCell(4)).trim();

                if (bu.isEmpty()) {
                    throw new Exception(
                            "BU is missing at row "
                                    + displayRow
                    );
                }

                gl.setBu(bu);

                // =========================================
                // ✅ CATEGORY GROUP
                // =========================================
                String categoryGroup =
                        formatter.formatCellValue(row.getCell(5)).trim();

                if (categoryGroup.isEmpty()) {
                    throw new Exception(
                            "Category Group is missing at row "
                                    + displayRow
                    );
                }

                gl.setCategoryGroup(categoryGroup);

                // =========================================
                // ✅ P&L HEADERS
                // =========================================
                String plHeaders =
                        formatter.formatCellValue(row.getCell(6)).trim();

                if (plHeaders.isEmpty()) {
                    throw new Exception(
                            "P&L Headers is missing at row "
                                    + displayRow
                    );
                }

                gl.setPlHeaders(plHeaders);

                glList.add(gl);
            }

            // =========================================
            // ✅ NO VALID DATA
            // =========================================
            if (glList.isEmpty()) {
                throw new Exception("No valid data found in Excel.");
            }

            // =========================================
            // ✅ SAVE ALL
            // =========================================
            glRepository.saveAll(glList);
        }

        return glList.size() + " records uploaded successfully.";
    }

    private boolean isRowEmpty(Row row) {

        for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {

            Cell cell = row.getCell(c);

            if (cell != null && cell.getCellType() != CellType.BLANK) {

                String value = new DataFormatter().formatCellValue(cell);

                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }
    
    
    @Transactional(readOnly = true)
    public byte[] exportToExcel(String accountNumber, String bu) {

        List<GlMaster> glList =
                glRepository.exportGlData(accountNumber, bu);

        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("GL Master");

            // Header Style
            CellStyle headerStyle = workbook.createCellStyle();

            Font headerFont = workbook.createFont();
            headerFont.setBold(true);

            headerStyle.setFont(headerFont);

            // Header Row
            Row headerRow = sheet.createRow(0);

            String[] headers = {
                    "Account Number",
                    "Description",
                    "Type",
                    "Console Group",
                    "BU",
                    "Category Group",
                    "P&L Headers"
            };

            for (int i = 0; i < headers.length; i++) {

                Cell cell = headerRow.createCell(i);

                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data Rows
            int rowNum = 1;

            for (GlMaster gl : glList) {

                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(
                        gl.getAccountNumber() != null
                                ? gl.getAccountNumber()
                                : 0
                );

                row.createCell(1).setCellValue(
                        gl.getDescription() != null
                                ? gl.getDescription()
                                : ""
                );

                row.createCell(2).setCellValue(
                        gl.getType() != null
                                ? gl.getType()
                                : ""
                );

                row.createCell(3).setCellValue(
                        gl.getConsoleGroup() != null
                                ? gl.getConsoleGroup()
                                : ""
                );

                row.createCell(4).setCellValue(
                        gl.getBu() != null
                                ? gl.getBu()
                                : ""
                );

                row.createCell(5).setCellValue(
                        gl.getCategoryGroup() != null
                                ? gl.getCategoryGroup()
                                : ""
                );

                row.createCell(6).setCellValue(
                        gl.getPlHeaders() != null
                                ? gl.getPlHeaders()
                                : ""
                );
            }

            // Auto Size Columns
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {

            throw new RuntimeException(
                    "Failed to export Excel file.",
                    e
            );
        }
    }
    
  
}