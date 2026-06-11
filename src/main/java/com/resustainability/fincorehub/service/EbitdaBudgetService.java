
package com.resustainability.fincorehub.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.resustainability.fincorehub.commons.Default;
import com.resustainability.fincorehub.entity.EbitdaBudgetMaster;
import com.resustainability.fincorehub.exception.InvalidDataException;
import com.resustainability.fincorehub.exception.ResourceNotFoundException;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.repository.EbitdaBudgetRepository;
import com.resustainability.fincorehub.request.AddEbitdaBudgetRequest;
import com.resustainability.fincorehub.request.UpdateEbitdaBudgetRequest;
import com.resustainability.fincorehub.response.IEbitdaBudgetResponse;

import java.io.ByteArrayOutputStream;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

@Service
public class EbitdaBudgetService {

    private static final int MAX_ROW_LIMIT = 1000;

    private final EbitdaBudgetRepository budgetRepository;

    public EbitdaBudgetService(EbitdaBudgetRepository budgetRepository) {

        this.budgetRepository = budgetRepository;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public EbitdaBudgetMaster addBudget(AddEbitdaBudgetRequest request) {

        boolean alreadyExists =
                budgetRepository
                        .existsByBuIgnoreCaseAndSbuIgnoreCaseAndSiteIgnoreCaseAndFinancialYear(
                                request.bu(),
                                request.sbu(),
                                request.site(),
                                request.financialYear()
                        );

        if (alreadyExists) {

            throw new InvalidDataException(
                    "Budget already exists for BU="
                            + request.bu()
                            + ", SBU="
                            + request.sbu()
                            + ", Site="
                            + request.site()
                            + ", Financial Year="
                            + request.financialYear()
            );
        }

        EbitdaBudgetMaster budget = new EbitdaBudgetMaster();

        budget.setBu(request.bu());
        budget.setSbu(request.sbu());
        budget.setSite(request.site());
        budget.setFinancialYear(request.financialYear());

        budget.setApr(request.apr());
        budget.setMay(request.may());
        budget.setJun(request.jun());
        budget.setJul(request.jul());
        budget.setAug(request.aug());
        budget.setSep(request.sep());
        budget.setOct(request.oct());
        budget.setNov(request.nov());
        budget.setDec(request.dec());
        budget.setJan(request.jan());
        budget.setFeb(request.feb());
        budget.setMar(request.mar());

        Double total =
                safe(request.apr()) +
                safe(request.may()) +
                safe(request.jun()) +
                safe(request.jul()) +
                safe(request.aug()) +
                safe(request.sep()) +
                safe(request.oct()) +
                safe(request.nov()) +
                safe(request.dec()) +
                safe(request.jan()) +
                safe(request.feb()) +
                safe(request.mar());

        budget.setTotal(total);

        return budgetRepository.save(budget);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public Pager<IEbitdaBudgetResponse> list(
            String bu,
            String site,
            String financialYear,
            SearchCriteria searchCriteria) {

        return Pager.of(
                budgetRepository.findAllBudget(
                        bu,
                        site,
                        financialYear,
                        searchCriteria.toPageRequest()
                )
        );
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public IEbitdaBudgetResponse getById(Long id) {

        return budgetRepository.findBudgetById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Budget Record Not Found : " + id
                        )
                );
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    public String updateBudget(
            Long id,
            UpdateEbitdaBudgetRequest request) {

        budgetRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Budget Record Not Found : " + id
                        )
                );

        Double total =
                safe(request.apr()) +
                safe(request.may()) +
                safe(request.jun()) +
                safe(request.jul()) +
                safe(request.aug()) +
                safe(request.sep()) +
                safe(request.oct()) +
                safe(request.nov()) +
                safe(request.dec()) +
                safe(request.jan()) +
                safe(request.feb()) +
                safe(request.mar());

        int updated = budgetRepository.updateBudget(
                id,
                request.bu(),
                request.sbu(),
                request.site(),
                request.financialYear(),
                request.apr(),
                request.may(),
                request.jun(),
                request.jul(),
                request.aug(),
                request.sep(),
                request.oct(),
                request.nov(),
                request.dec(),
                request.jan(),
                request.feb(),
                request.mar(),
                total
        );

        if (updated == 0) {
            throw new InvalidDataException(
                    Default.ERROR_FAILED
            );
        }

        return "Budget Updated Successfully";
    }

    @Transactional(isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String deleteBudget(List<Long> ids) {

        if (ids == null || ids.isEmpty()) {

            throw new InvalidDataException(
                    "Please select at least one record"
            );
        }

        List<EbitdaBudgetMaster> budgets =
                budgetRepository.findAllById(ids);

        if (budgets.isEmpty()) {

            throw new ResourceNotFoundException(
                    "No Budget Records Found"
            );
        }

        budgetRepository.deleteAll(budgets);

        return budgets.size()
                + " Budget Record(s) Deleted Successfully";
    }

    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public String uploadFromExcel(
            MultipartFile file) throws Exception {

        if (file == null || file.isEmpty()) {

            throw new Exception(
                    "Please upload a valid Excel file."
            );
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null ||
                !(fileName.toLowerCase().endsWith(".xlsx")
                        || fileName.toLowerCase().endsWith(".xls"))) {

            throw new Exception(
                    "Only .xlsx and .xls files are allowed."
            );
        }

        List<EbitdaBudgetMaster> budgetList = new ArrayList<>();
                
        Set<String> uniqueRows = new HashSet<>();
                
        DataFormatter formatter = new DataFormatter();
                

        try (Workbook workbook =
                     WorkbookFactory.create(
                             file.getInputStream())) {

            Sheet sheet = workbook.getSheetAt(0);

            int physicalRows = sheet.getPhysicalNumberOfRows();
                    
            if (physicalRows <= 1) {

                throw new Exception(
                        "The Excel file contains no data rows."
                );
            }

            int lastRowIndex = sheet.getLastRowNum();

            int dataRowCount = 0;

            for (int i = 1;
                 i <= lastRowIndex;
                 i++) {

                Row row = sheet.getRow(i);

                if (row == null || isRowEmpty(row)) {
                    continue;
                }

                dataRowCount++;

                if (dataRowCount > MAX_ROW_LIMIT) {

                    throw new Exception(
                            "Maximum "
                                    + MAX_ROW_LIMIT
                                    + " rows allowed per upload."
                    );
                }

                int displayRow = i + 1;

                EbitdaBudgetMaster budget = new EbitdaBudgetMaster();
                        
                String bu = formatter                      
                               .formatCellValue(row.getCell(0))
                               .trim();

                if (bu.isEmpty()) {

                    throw new Exception(
                            "BU is missing at row " + displayRow
                    );
                }

                String sbu = formatter
                                .formatCellValue( row.getCell(1))
                                .trim();

                if (sbu.isEmpty()) {

                    throw new Exception(
                            "SBU is missing at row " + displayRow
                    );
                }

                String site = formatter
                                .formatCellValue(row.getCell(2))
                                .trim();

                if (site.isEmpty()) {

                    throw new Exception(
                            "Site is missing at row "+ displayRow
                    );
                }

                String financialYear = formatter
                                         .formatCellValue(row.getCell(3))
                                         .trim();

                if (financialYear.isEmpty()) {

                    throw new Exception(
                            "Financial Year is missing at row "+ displayRow
                    );
                }

                String uniqueKey =
                        bu.toLowerCase() + "|" +
                        sbu.toLowerCase() + "|" +
                        site.toLowerCase() + "|" +
                        financialYear.toLowerCase();

                if (uniqueRows.contains(uniqueKey)) {

                	
                	throw new Exception(
                            "Duplicate record found in Excel at row "
                                    + displayRow
                                    + " for BU="
                                    + bu
                                    + ", SBU="
                                    + sbu
                                    + ", Site="
                                    + site
                                    + ", Financial Year="
                                    + financialYear
                    );
                }

                uniqueRows.add(uniqueKey);

                boolean alreadyExists =
                        budgetRepository
                                .existsByBuIgnoreCaseAndSbuIgnoreCaseAndSiteIgnoreCaseAndFinancialYear(
                                        bu,
                                        sbu,
                                        site,
                                        financialYear
                                );

                if (alreadyExists) {

                	throw new Exception(
                            "Duplicate record found in Excel at row "
                                    + displayRow
                                    + " for BU="
                                    + bu
                                    + ", SBU="
                                    + sbu
                                    + ", Site="
                                    + site
                                    + ", Financial Year="
                                    + financialYear
                    );
                }

                budget.setBu(bu);
                budget.setSbu(sbu);
                budget.setSite(site);
                budget.setFinancialYear(financialYear);

                budget.setApr(getDoubleValue(row.getCell(4),formatter));
                budget.setMay(getDoubleValue(row.getCell(5),formatter));
                budget.setJun(getDoubleValue(row.getCell(6),formatter));
                budget.setJul(getDoubleValue(row.getCell(7),formatter));
                budget.setAug(getDoubleValue(row.getCell(8),formatter));
                budget.setSep(getDoubleValue(row.getCell(9),formatter));
                budget.setOct(getDoubleValue(row.getCell(10),formatter));
                budget.setNov(getDoubleValue(row.getCell(11),formatter));
                budget.setDec(getDoubleValue(row.getCell(12),formatter));
                budget.setJan(getDoubleValue(row.getCell(13),formatter));
                budget.setFeb(getDoubleValue(row.getCell(14),formatter));
                budget.setMar(getDoubleValue(row.getCell(15),formatter));
                        

                Double total =
                        safe(budget.getApr()) +
                        safe(budget.getMay()) +
                        safe(budget.getJun()) +
                        safe(budget.getJul()) +
                        safe(budget.getAug()) +
                        safe(budget.getSep()) +
                        safe(budget.getOct()) +
                        safe(budget.getNov()) +
                        safe(budget.getDec()) +
                        safe(budget.getJan()) +
                        safe(budget.getFeb()) +
                        safe(budget.getMar());

                budget.setTotal(total);

                budgetList.add(budget);
            }

            if (budgetList.isEmpty()) {

                throw new Exception(
                        "No valid data rows found in Excel."
                );
            }

            budgetRepository.saveAll(budgetList);
        }

        return budgetList.size()
                + " records processed and saved successfully.";
    }

    private boolean isRowEmpty(Row row) {

        if (row == null) {
            return true;
        }

        DataFormatter formatter =
                new DataFormatter();

        for (int c = row.getFirstCellNum();
             c < row.getLastCellNum();
             c++) {

            Cell cell = row.getCell(c);

            if (cell != null) {

                String value =
                        formatter
                                .formatCellValue(cell)
                                .trim();

                if (!value.isEmpty()) {
                    return false;
                }
            }
        }

        return true;
    }

    private Double getDoubleValue(
            Cell cell,
            DataFormatter formatter) {

        if (cell == null) {
            return 0.0;
        }

        String value =
                formatter
                        .formatCellValue(cell)
                        .trim();

        if (value.isEmpty()) {
            return 0.0;
        }

        try {

            return Double.parseDouble(
                    value.replace(",", "")
            );

        } catch (Exception e) {

            return 0.0;
        }
    }

    private Double safe(Double value) {
        return value == null ? 0.0 : value;
    }
    
    @Transactional(rollbackFor = Exception.class,isolation = Isolation.READ_COMMITTED,propagation = Propagation.REQUIRED)
    public byte[] exportBudget(
            String bu,
            String site,
            String financialYear) throws Exception {

        List<EbitdaBudgetMaster> budgetList =
                budgetRepository.exportBudgetData(
                        bu,
                        site,
                        financialYear
                );

        Workbook workbook = new XSSFWorkbook();

        Sheet sheet =
                workbook.createSheet("EBITDA Budget");

        CellStyle headerStyle =
                workbook.createCellStyle();

        Font headerFont =
                workbook.createFont();

        headerFont.setBold(true);

        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);

        String[] columns = {
                "BU",
                "SBU",
                "Site",
                "Financial Year",
                "Apr",
                "May",
                "Jun",
                "Jul",
                "Aug",
                "Sep",
                "Oct",
                "Nov",
                "Dec",
                "Jan",
                "Feb",
                "Mar",
                "Total"
        };

        for (int i = 0; i < columns.length; i++) {

            Cell cell = header.createCell(i);

            cell.setCellValue(columns[i]);

            cell.setCellStyle(headerStyle);
        }


        int rowNum = 1;

        for (EbitdaBudgetMaster b : budgetList) {

            Row row = sheet.createRow(rowNum++);

            row.createCell(0).setCellValue(
                    b.getBu() != null ? b.getBu() : ""
            );

            row.createCell(1).setCellValue(
                    b.getSbu() != null ? b.getSbu() : ""
            );

            row.createCell(2).setCellValue(
                    b.getSite() != null ? b.getSite() : ""
            );

            row.createCell(3).setCellValue(
                    b.getFinancialYear() != null
                            ? b.getFinancialYear()
                            : ""
            );

            row.createCell(4).setCellValue(safe(b.getApr()));
            row.createCell(5).setCellValue(safe(b.getMay()));
            row.createCell(6).setCellValue(safe(b.getJun()));
            row.createCell(7).setCellValue(safe(b.getJul()));
            row.createCell(8).setCellValue(safe(b.getAug()));
            row.createCell(9).setCellValue(safe(b.getSep()));
            row.createCell(10).setCellValue(safe(b.getOct()));
            row.createCell(11).setCellValue(safe(b.getNov()));
            row.createCell(12).setCellValue(safe(b.getDec()));
            row.createCell(13).setCellValue(safe(b.getJan()));
            row.createCell(14).setCellValue(safe(b.getFeb()));
            row.createCell(15).setCellValue(safe(b.getMar()));
            row.createCell(16).setCellValue(safe(b.getTotal()));
        }

        // AUTO SIZE

        for (int i = 0; i < columns.length; i++) {

            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream out =
                new ByteArrayOutputStream();

        workbook.write(out);

        workbook.close();

        return out.toByteArray();
    }
}
