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
import com.resustainability.fincorehub.entity.PcMaster;
import com.resustainability.fincorehub.pagination.Pager;
import com.resustainability.fincorehub.pagination.SearchCriteria;
import com.resustainability.fincorehub.request.BulkUpdatePcRequest;
import com.resustainability.fincorehub.request.DeletePcRequest;
import com.resustainability.fincorehub.request.addPcMasterRequest;
import com.resustainability.fincorehub.request.updatePcMasterRequest;
import com.resustainability.fincorehub.response.IPcMasterResponse;
import com.resustainability.fincorehub.service.PcMasterService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;



@RestController
@RequestMapping("/api/pc")
public class PcMasterController {

    private final PcMasterService pcService;

    public PcMasterController(PcMasterService pcService) {
        this.pcService = pcService;
    }

    @PostMapping("/add")
    public ResponseEntity<APIResponse<?>> addPc(
            @RequestBody addPcMasterRequest request) {

        try {

            PcMaster pc = pcService.addPc(request);

            return ResponseEntity.ok(
                    new APIResponse<>(
                            pc,
                            "PC Record Added Successfully",
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
    public APIResponse<Pager<IPcMasterResponse>> list(
            @RequestParam(required = false) String pcCode,
            @RequestParam(required = false) String bu,
            @ModelAttribute SearchCriteria searchCriteria) {

        return new APIResponse<>(
                pcService.list(pcCode, bu, searchCriteria)
        );
    }


    @GetMapping("/details/{id}")
    public APIResponse<?> getById(@PathVariable Long id) {

        return new APIResponse<>(
                Default.SUCCESS_GET_PC,
                pcService.getById(id)
        );
    }


    @PutMapping("/update/{id}")
    public APIResponse<?> updatePc(
            @PathVariable Long id,
            @RequestBody updatePcMasterRequest request) {

        return new APIResponse<>(
                pcService.updatePc(id, request)
        );
    }
    
    @DeleteMapping("/delete")
    public APIResponse<?> deletePc(
            @RequestBody DeletePcRequest request) {

        return new APIResponse<>(
                pcService.deletePc(request.ids())
        );
    }
    
    @PostMapping("/bulk-upload")
    public APIResponse<String> bulkUpload(@RequestParam("file") MultipartFile file) {
        try {
            String result = pcService.uploadFromExcel(file);
            return new APIResponse<String>(result, "Bulk Upload Successful", null);
        } catch (Exception e) {
            return new APIResponse<String>(null, null, "Error processing excel: " + e.getMessage());
        }
    }
    
    @PutMapping("/bulk-update")
    public APIResponse<?> bulkUpdate(
            @RequestBody BulkUpdatePcRequest request) {

        return new APIResponse<>(
                pcService.bulkUpdate(request)
        );
    }
    
    @GetMapping("/export")
    public ResponseEntity<byte[]> exportPcData(
            @RequestParam(required = false) String pcCode,
            @RequestParam(required = false) String bu
    ) {

        byte[] excelData = pcService.exportToExcel(pcCode, bu);

        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(
                MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
                )
        );

        headers.setContentDispositionFormData(
                "attachment",
                "pc_master.xlsx"
        );

        return ResponseEntity.ok()
                .headers(headers)
                .body(excelData);
    }
    

}
