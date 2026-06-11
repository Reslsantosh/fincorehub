package com.resustainability.fincorehub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.resustainability.fincorehub.commons.APIResponse;
import com.resustainability.fincorehub.commons.Default;
import com.resustainability.fincorehub.entity.RevenueBudgetMaster;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.request.AddRevenueBudgetRequest;
import com.resustainability.fincorehub.request.DeleteRevenueBudgetRequest;
import com.resustainability.fincorehub.request.UpdateRevenueBudgetRequest;
import com.resustainability.fincorehub.response.IRevenueBudgetResponse;
import com.resustainability.fincorehub.service.RevenueBudgetService;

@RestController
@RequestMapping("/api/budget")
public class RevenueBudgetController {

    private final RevenueBudgetService budgetService;

    public RevenueBudgetController(RevenueBudgetService budgetService) {
        this.budgetService = budgetService;
    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse<?>> addBudget(
            @RequestBody AddRevenueBudgetRequest request) {

        try {

            RevenueBudgetMaster budget = budgetService.addBudget(request);

            return ResponseEntity.ok(
                    new APIResponse<>(
                            budget,
                            "Budget Record Added Successfully",
                            null
                    )
            );

        } catch (Exception e) {

            return ResponseEntity.badRequest().body(
                    new APIResponse<>(
                            null,
                            null,
                            e.getMessage()
                    )
            );
        }
    }

    @GetMapping("/list")
    public APIResponse<Pager<IRevenueBudgetResponse>> list(
            @RequestParam(required = false) String bu,
            @RequestParam(required = false) String site,
            @RequestParam(required = false) String financialYear,
            @ModelAttribute SearchCriteria searchCriteria) {

        return new APIResponse<>(
                budgetService.list(
                        bu,
                        site,
                        financialYear,
                        searchCriteria
                )
        );
    }

    @GetMapping("/details/{id}")
    public APIResponse<?> getById(@PathVariable Long id) {

        return new APIResponse<>(
                Default.SUCCESS,
                budgetService.getById(id)
        );
    }

    @PutMapping("/update/{id}")
    public APIResponse<?> updateBudget(
            @PathVariable Long id,
            @RequestBody UpdateRevenueBudgetRequest request) {

        return new APIResponse<>(
                budgetService.updateBudget(id, request)
        );
    }

    
    @DeleteMapping("/delete")
    public APIResponse<?> deleteBudget(
            @RequestBody DeleteRevenueBudgetRequest request) {

        return new APIResponse<>(
                budgetService.deleteBudget(request.ids())
        );
    }
    
    @PostMapping("/bulk-upload")
    public APIResponse<String> bulkUpload(
            @RequestParam("file") MultipartFile file) {

        try {

            String result = budgetService.uploadFromExcel(file);

            return new APIResponse<>(
                    result,
                    "Bulk Upload Successful",
                    null
            );

        } catch (Exception e) {

            return new APIResponse<>(
                    null,
                    null,
                    "Error processing excel : " + e.getMessage()
            );
        }
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportBudget(
            @RequestParam(required = false) String bu,
            @RequestParam(required = false) String site,
            @RequestParam(required = false) String financialYear) throws Exception {

        byte[] excelData = budgetService.exportBudget(
                bu,
                site,
                financialYear
        );

        return ResponseEntity.ok()
                .header(
                        "Content-Disposition",
                        "attachment; filename=Revenue_Budget.xlsx"
                )
                .header(
                        "Content-Type",
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
                .body(excelData);
    }
}