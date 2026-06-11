package com.resustainability.fincorehub.service;

import java.time.LocalDateTime;
import org.apache.poi.ss.usermodel.*;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.resustainability.fincorehub.commons.Default;
import com.resustainability.fincorehub.config.SecurityUtils;
import com.resustainability.fincorehub.entity.PcMaster;
import com.resustainability.fincorehub.exception.InvalidDataException;
import com.resustainability.fincorehub.exception.ResourceNotFoundException;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.repository.PcMasterRepository;
import com.resustainability.fincorehub.request.BulkUpdatePcRequest;
import com.resustainability.fincorehub.request.addPcMasterRequest;
import com.resustainability.fincorehub.request.updatePcMasterRequest;
import com.resustainability.fincorehub.response.IPcMasterResponse;

import java.util.*;


import java.io.ByteArrayOutputStream;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



@Service
public class PcMasterService {

	
	private static final int MAX_ROW_LIMIT = 500;
    private final PcMasterRepository pcRepository;

    public PcMasterService(PcMasterRepository pcRepository) {
        this.pcRepository = pcRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public PcMaster addPc(addPcMasterRequest request) {

        boolean alreadyExists =
                pcRepository.existsByProfitCentre(
                        request.profitCentre()
                );

        if (alreadyExists) {

            throw new RuntimeException(
                    "Profit Centre already exists : "
                            + request.profitCentre()
            );
        }

        PcMaster pc = new PcMaster();

        pc.setProfitCentre(request.profitCentre());
        pc.setProfitCentreName(request.profitCentreName());
        pc.setSiteNameForMIS(request.siteNameForMIS());
        pc.setSbuFinal(request.sbuFinal());
        pc.setUnit(request.unit());
        pc.setSbu(request.sbu());
        pc.setBu(request.bu());
        pc.setPlantCode(request.plantCode());

        return pcRepository.save(pc);
    }

	    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
	    public Pager<IPcMasterResponse> list(
	            String pcCode,
	            String bu,
	            SearchCriteria searchCriteria) {

	        return Pager.of(
	            pcRepository.findAllPc(
	                pcCode,
	                bu,
	                searchCriteria.toPageRequest()
	            )
	        );
	    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public IPcMasterResponse getById(Long id) {

        return pcRepository.findByPcId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Default.ERROR_NOT_FOUND_PC + id)
                );
    }


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String updatePc(Long id, updatePcMasterRequest request) {

        pcRepository.findByPcId(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(Default.ERROR_NOT_FOUND_PC + id)
                );

        int updated = pcRepository.updatePc(
                id,
                request.profitCentre(),
                request.profitCentreName(),
                request.siteNameForMIS(),
                request.sbuFinal(),
                request.unit(),
                request.sbu(),
                request.bu(),
                request.plantCode(),
                SecurityUtils.getCurrentUser(), 
                LocalDateTime.now()               
        );

        if (updated == 0) {
            throw new InvalidDataException(Default.ERROR_FAILED);
        }

        return Default.SUCCESS_UPDATE_PC;
    }
    
    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String deletePc(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {

            throw new InvalidDataException(
                    "Please select at least one record"
            );
        }

        List<PcMaster> gls =
        		pcRepository.findAllById(ids);

        if (gls.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No PC Records Found"
            );
        }

        pcRepository.deleteAll(gls);

        return gls.size()
                + " PC Record(s) Deleted Successfully";
    }
    
    
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String uploadFromExcel(MultipartFile file) throws Exception {

    	        // ===============================
    	        // FILE VALIDATION
    	        // ===============================

    	        if (file == null || file.isEmpty()) {
    	            throw new Exception("Please upload a valid Excel file.");
    	        }

    	        String fileName = file.getOriginalFilename();

    	        if (fileName == null ||
    	                !(fileName.toLowerCase().endsWith(".xlsx")
    	                        || fileName.toLowerCase().endsWith(".xls"))) {

    	            throw new Exception(
    	                    "Only .xlsx and .xls files are allowed."
    	            );
    	        }

    	        List<PcMaster> pcList = new ArrayList<>();

    	        DataFormatter formatter = new DataFormatter();

    	        // Used for duplicate validation inside Excel
    	        Set<Long> excelProfitCentres = new HashSet<>();

    	        try (Workbook workbook =
    	                     WorkbookFactory.create(file.getInputStream())) {

    	            Sheet sheet = workbook.getSheetAt(0);

    	            int physicalRows = sheet.getPhysicalNumberOfRows();

    	            // Empty file validation
    	            if (physicalRows <= 1) {
    	                throw new Exception(
    	                        "The Excel file contains no data rows."
    	                );
    	            }

    	            int lastRowIndex = sheet.getLastRowNum();

    	            int dataRowCount = 0;

    	            // Start from row 1 because row 0 is header
    	            for (int i = 1; i <= lastRowIndex; i++) {

    	                Row row = sheet.getRow(i);

    	                // Skip empty rows
    	                if (row == null || isRowEmpty(row)) {
    	                    continue;
    	                }

    	                dataRowCount++;

    	                // Row limit validation
    	                if (dataRowCount > MAX_ROW_LIMIT) {

    	                    throw new Exception(
    	                            "Maximum "
    	                                    + MAX_ROW_LIMIT
    	                                    + " rows allowed per upload."
    	                    );
    	                }

    	                int displayRow = i + 1;

    	                PcMaster pc = new PcMaster();

    	                // ==========================================
    	                // 1. PROFIT CENTRE
    	                // ==========================================

    	                String pcStr = formatter
    	                        .formatCellValue(row.getCell(0))
    	                        .replaceAll("[^0-9]", "")
    	                        .trim();

    	                if (pcStr.isEmpty()) {

    	                    throw new Exception(
    	                            "Profit Centre is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                Long profitCentre;

    	                try {

    	                    profitCentre = Long.parseLong(pcStr);

    	                } catch (NumberFormatException e) {

    	                    throw new Exception(
    	                            "Invalid Profit Centre at row "
    	                                    + displayRow
    	                    );
    	                }

    	                // ==========================================
    	                // DUPLICATE CHECK INSIDE EXCEL
    	                // ==========================================

    	                if (excelProfitCentres.contains(profitCentre)) {

    	                    throw new Exception(
    	                            "Duplicate Profit Centre found in Excel at row "
    	                                    + displayRow
    	                                    + " : "
    	                                    + profitCentre
    	                    );
    	                }

    	                excelProfitCentres.add(profitCentre);

    	                // ==========================================
    	                // DUPLICATE CHECK IN DATABASE
    	                // ==========================================

    	                boolean alreadyExists =
    	                        pcRepository.existsByProfitCentre(profitCentre);

    	                if (alreadyExists) {

    	                    throw new Exception(
    	                            "Profit Centre already exists in database at row "
    	                                    + displayRow
    	                                    + " : "
    	                                    + profitCentre
    	                    );
    	                }

    	                pc.setProfitCentre(profitCentre);

    	                // ==========================================
    	                // 2. PROFIT CENTRE NAME
    	                // ==========================================

    	                String pcName = formatter
    	                        .formatCellValue(row.getCell(1))
    	                        .trim();

    	                if (pcName.isEmpty()) {

    	                    throw new Exception(
    	                            "Profit Centre Name is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                pc.setProfitCentreName(pcName);

    	                // ==========================================
    	                // 3. SITE NAME FOR MIS
    	                // ==========================================

    	                String siteMis = formatter
    	                        .formatCellValue(row.getCell(2))
    	                        .trim();

    	                if (siteMis.isEmpty()) {

    	                    throw new Exception(
    	                            "Site Name for MIS is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                pc.setSiteNameForMIS(siteMis);

    	                // ==========================================
    	                // 4. SBU FINAL
    	                // ==========================================

    	                String sbuFinal = formatter
    	                        .formatCellValue(row.getCell(3))
    	                        .trim();

    	                if (sbuFinal.isEmpty()) {

    	                    throw new Exception(
    	                            "SBU Final is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                pc.setSbuFinal(sbuFinal);

    	                // ==========================================
    	                // 5. UNIT
    	                // ==========================================

    	                String unit = formatter
    	                        .formatCellValue(row.getCell(4))
    	                        .trim();

    	                if (unit.isEmpty()) {

    	                    throw new Exception(
    	                            "Unit is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                pc.setUnit(unit);

    	                // ==========================================
    	                // 6. SBU
    	                // ==========================================

    	                String sbu = formatter
    	                        .formatCellValue(row.getCell(5))
    	                        .trim();

    	                if (sbu.isEmpty()) {

    	                    throw new Exception(
    	                            "SBU is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                pc.setSbu(sbu);

    	                // ==========================================
    	                // 7. BU
    	                // ==========================================

    	                String bu = formatter
    	                        .formatCellValue(row.getCell(6))
    	                        .trim();

    	                if (bu.isEmpty()) {

    	                    throw new Exception(
    	                            "BU is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                pc.setBu(bu);

    	                // ==========================================
    	                // 8. PLANT CODE
    	                // ==========================================

    	                String plantCode = formatter
    	                        .formatCellValue(row.getCell(7))
    	                        .trim();

    	                if (plantCode.isEmpty()) {

    	                    throw new Exception(
    	                            "Plant Code is missing at row "
    	                                    + displayRow
    	                    );
    	                }

    	                pc.setPlantCode(plantCode);

    	                pcList.add(pc);
    	            }

    	            // No valid rows validation
    	            if (pcList.isEmpty()) {

    	                throw new Exception(
    	                        "No valid data rows found in Excel."
    	                );
    	            }

    	            pcRepository.saveAll(pcList);
    	        }

    	        return pcList.size()
    	                + " records processed and saved successfully.";
    	    }

    	    
    	     // Check if row is empty
    	     
    	    private boolean isRowEmpty(Row row) {

    	        if (row == null) {
    	            return true;
    	        }

    	        DataFormatter formatter = new DataFormatter();

    	        for (int c = row.getFirstCellNum();
    	             c < row.getLastCellNum();
    	             c++) {

    	            Cell cell = row.getCell(c);

    	            if (cell != null) {

    	                String value =
    	                        formatter.formatCellValue(cell).trim();

    	                if (!value.isEmpty()) {
    	                    return false;
    	                }
    	            }
    	        }

    	        return true;
    	    }
    	    
    	    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    	    public String bulkUpdate(BulkUpdatePcRequest request) {

    	        if (request.ids() == null || request.ids().isEmpty()) {
    	            throw new InvalidDataException("Please select records");
    	        }

    	        int updated = pcRepository.bulkUpdateSbuBu(
    	                request.ids(),
    	                request.sbu(),
    	                request.bu(),
    	                SecurityUtils.getCurrentUser(),
    	                LocalDateTime.now()
    	        );

    	        if (updated == 0) {
    	            throw new InvalidDataException("Failed to update records");
    	        }

    	        return "Records updated successfully";
    	    }
    	    
    	    
    	    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    	    public byte[] exportToExcel(String pcCode, String bu) {

    	        List<PcMaster> pcList =
    	                pcRepository.exportPcData(pcCode, bu);

    	        try (Workbook workbook = new XSSFWorkbook();
    	             ByteArrayOutputStream out =
    	                     new ByteArrayOutputStream()) {

    	            Sheet sheet = workbook.createSheet("PC Master");

    	            // Header Style
    	            CellStyle headerStyle =
    	                    workbook.createCellStyle();

    	            Font headerFont = workbook.createFont();

    	            headerFont.setBold(true);

    	            headerStyle.setFont(headerFont);

    	            // Header Row
    	            Row headerRow = sheet.createRow(0);

    	            String[] headers = {
    	                    "Profit Centre",
    	                    "Profit Centre Name",
    	                    "Site Name For MIS",
    	                    "SBU Final",
    	                    "Unit",
    	                    "SBU",
    	                    "BU",
    	                    "Plant Code"
    	            };

    	            for (int i = 0; i < headers.length; i++) {

    	                Cell cell = headerRow.createCell(i);

    	                cell.setCellValue(headers[i]);

    	                cell.setCellStyle(headerStyle);
    	            }

    	            // Data Rows
    	            int rowNum = 1;

    	            for (PcMaster pc : pcList) {

    	                Row row = sheet.createRow(rowNum++);

    	                row.createCell(0).setCellValue(
    	                        pc.getProfitCentre() != null
    	                                ? pc.getProfitCentre()
    	                                : 0
    	                );

    	                row.createCell(1).setCellValue(
    	                        pc.getProfitCentreName() != null
    	                                ? pc.getProfitCentreName()
    	                                : ""
    	                );

    	                row.createCell(2).setCellValue(
    	                        pc.getSiteNameForMIS() != null
    	                                ? pc.getSiteNameForMIS()
    	                                : ""
    	                );

    	                row.createCell(3).setCellValue(
    	                        pc.getSbuFinal() != null
    	                                ? pc.getSbuFinal()
    	                                : ""
    	                );

    	                row.createCell(4).setCellValue(
    	                        pc.getUnit() != null
    	                                ? pc.getUnit()
    	                                : ""
    	                );

    	                row.createCell(5).setCellValue(
    	                        pc.getSbu() != null
    	                                ? pc.getSbu()
    	                                : ""
    	                );

    	                row.createCell(6).setCellValue(
    	                        pc.getBu() != null
    	                                ? pc.getBu()
    	                                : ""
    	                );

    	                row.createCell(7).setCellValue(
    	                        pc.getPlantCode() != null
    	                                ? pc.getPlantCode()
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
