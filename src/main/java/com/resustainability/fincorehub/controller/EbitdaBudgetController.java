package com.resustainability.fincorehub.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.resustainability.fincorehub.commons.APIResponse;
import com.resustainability.fincorehub.commons.Default;
import com.resustainability.fincorehub.entity.EbitdaBudgetMaster;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.request.AddEbitdaBudgetRequest;
import com.resustainability.fincorehub.request.DeleteEbitdaBudgetRequest;
import com.resustainability.fincorehub.request.UpdateEbitdaBudgetRequest;
import com.resustainability.fincorehub.response.IEbitdaBudgetResponse;
import com.resustainability.fincorehub.service.EbitdaBudgetService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("/api/ebitda_budget")
public class EbitdaBudgetController {
	
	 private final EbitdaBudgetService ebitdabudgetService;

	    public EbitdaBudgetController(EbitdaBudgetService ebitdabudgetService) {
		super();
		this.ebitdabudgetService = ebitdabudgetService;
	}

		@PostMapping("/add")
	    public ResponseEntity<APIResponse<?>> addBudget(
	            @RequestBody AddEbitdaBudgetRequest request) {

	        try {

	        	EbitdaBudgetMaster budget = ebitdabudgetService.addBudget(request);

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
	    public APIResponse<Pager<IEbitdaBudgetResponse>> list(
	            @RequestParam(required = false) String bu,
	            @RequestParam(required = false) String site,
	            @RequestParam(required = false) String financialYear,
	            @ModelAttribute SearchCriteria searchCriteria) {

	        return new APIResponse<>(
	        		ebitdabudgetService.list(
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
	                ebitdabudgetService.getById(id)
	        );
	    }

	    @PutMapping("/update/{id}")
	    public APIResponse<?> updateBudget(
	            @PathVariable Long id,
	            @RequestBody UpdateEbitdaBudgetRequest request) {

	        return new APIResponse<>(
	        		ebitdabudgetService.updateBudget(id, request)
	        );
	    }

	    
	    @DeleteMapping("/delete")
	    public APIResponse<?> deleteBudget(
	            @RequestBody DeleteEbitdaBudgetRequest request) {

	        return new APIResponse<>(
	        		ebitdabudgetService.deleteBudget(request.ids())
	        );
	    }
	    
	    @PostMapping("/bulk-upload")
	    public APIResponse<String> bulkUpload(
	            @RequestParam("file") MultipartFile file) {

	        try {

	            String result = ebitdabudgetService.uploadFromExcel(file);

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
	            @RequestParam(required = false) String financialYear) {

	        try {

	            byte[] excelData =
	                    ebitdabudgetService.exportBudget(
	                            bu,
	                            site,
	                            financialYear
	                    );

	            return ResponseEntity.ok()
	                    .header(
	                            HttpHeaders.CONTENT_DISPOSITION,
	                            "attachment; filename=ebitda_budget.xlsx"
	                    )
	                    .contentType(
	                            MediaType.APPLICATION_OCTET_STREAM
	                    )
	                    .body(excelData);

	        } catch (Exception e) {

	            return ResponseEntity.internalServerError().build();
	        }
	    }

}
