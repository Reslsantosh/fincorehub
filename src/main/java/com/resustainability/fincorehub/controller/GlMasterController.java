package com.resustainability.fincorehub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.resustainability.fincorehub.commons.APIResponse;
import com.resustainability.fincorehub.commons.Default;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.request.DeleteGlRequest;
import com.resustainability.fincorehub.request.DeleteRevenueBudgetRequest;
import com.resustainability.fincorehub.request.addGlMasterRequest;
import com.resustainability.fincorehub.request.updateGlRequest;
import com.resustainability.fincorehub.response.IGlMasterResponse;
import com.resustainability.fincorehub.service.GlMasterService;


@RestController
@RequestMapping("/api/gl")
public class GlMasterController {
	

    private final GlMasterService glService;
    
    @Autowired
    public GlMasterController(GlMasterService glService) {
        this.glService = glService;
    }

    
    @PostMapping("/add")
    public APIResponse<?> addGl(@RequestBody addGlMasterRequest request) {

        return new APIResponse<>(
               Default.SUCCESS_ADD_GL,
                glService.addGl(request)
        );
    }

    @GetMapping("/list")
    public APIResponse<Pager<IGlMasterResponse>> list(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String bu,
            @ModelAttribute SearchCriteria searchcriteria) {

        return new APIResponse<>(
                glService.list(accountNumber, bu, searchcriteria)
        );
    }

    @GetMapping("/details/{glId}")
    public APIResponse<?> getById(@PathVariable Long glId) {

        return new APIResponse<>(
                Default.SUCCESS_GET_GL,
                glService.getById(glId)
        );
    }

    @PutMapping("/update/{id}")
    public APIResponse<?> updateGl(
            @PathVariable Long id,
            @RequestBody updateGlRequest request) {

        return new APIResponse<>(
                glService.updateGl(id, request)
        );
    }
    
    @DeleteMapping("/delete")
    public APIResponse<?> deleteGl(
            @RequestBody DeleteGlRequest request) {

        return new APIResponse<>(
                glService.deleteGl(request.ids())
        );
    }

    
    @PostMapping("/bulk-upload")
    public APIResponse<String> bulkUpload(@RequestParam("file") MultipartFile file) {
        try {
            String resultMessage = glService.uploadFromExcel(file);
            return new APIResponse<>(resultMessage, "Bulk Upload Successful", null);
        } catch (Exception e) {
           
            return new APIResponse<>(null, e.getMessage(), null);
        }
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportGlData(
            @RequestParam(required = false) String accountNumber,
            @RequestParam(required = false) String bu
    ) {

        byte[] excelData = glService.exportToExcel(accountNumber, bu);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(
                MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
        );

        headers.setContentDispositionFormData(
                "attachment",
                "gl_master.xlsx"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
    

}