package com.dsp.bios.controller;

import com.dsp.bios.dto.uploadBillText.UploadBillTextDTO;
import com.dsp.bios.service.UploadBillTextService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/upload-bill-text")
@AllArgsConstructor
public class UploadBillTextController {

    private final UploadBillTextService uploadBillTextService;
    @PostMapping("/{id}")
    public ResponseEntity<Void> updateUploadBillTextData(@PathVariable Long id, @RequestBody @Valid List<UploadBillTextDTO> dto){
        uploadBillTextService.save(id,dto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<List<UploadBillTextDTO>> UploadBillTextData(@PathVariable Long id){
        return ResponseEntity.ok(uploadBillTextService.getById(id));

    }
}
