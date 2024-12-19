package com.dsp.bios.controller;

import com.dsp.bios.dto.NewBillEntry.EntryDataDTO;
import com.dsp.bios.dto.NewBillEntry.SaveResponseDTO;
import com.dsp.bios.service.FormService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/form-data")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FormDataController {


    private final FormService formService;


    @PostMapping
    public ResponseEntity<SaveResponseDTO> createBillEntry(@RequestBody @Valid EntryDataDTO formDataDetails) {
        try {

            Long id = formService.save(formDataDetails);
            SaveResponseDTO resp= SaveResponseDTO.builder().id(id).build();
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<Void> updateBillEntry(@PathVariable Long id,@RequestBody LSBillSaveRequestDTO formDataDetails) {
//        try {
//            formServiceupdateEntryData(id,formDataDetails);
//            return ResponseEntity.status(HttpStatus.OK).build();
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

//    @GetMapping("/submitted")
//    public ResponseEntity<List<Bill>> getSubmitted() {
//        try {
//
//            return ResponseEntity.status(HttpStatus.OK).body(formService.getSubmittedBills());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//    @GetMapping("/drafts")
//    public ResponseEntity<List<Bill>> getDraft() {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(formService.getDraftBills());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/billEntry/{id}")
//    public ResponseEntity<EntryDataDTO> getBillEntry(@PathVariable Long id) {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(formService.getBillEntry(id));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//
//    @GetMapping("/UploadBillText/{id}")
//    public ResponseEntity<List<UploadBillTextDTO>> getUploadBillText(@PathVariable Long id) {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(formService.getUploadBillText(id));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }
//    @GetMapping("/refCommittee/{id}")
//    public ResponseEntity<ReferToCommitteeDTO> getRefCommittee(@PathVariable Long id) {
//        try {
//            return ResponseEntity.status(HttpStatus.OK).body(formService.getRefCommittee(id));
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

}