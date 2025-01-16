package com.dsp.bios.controller;

import com.dsp.bios.dto.uploadBillText.UploadBillTextDTO;
import com.dsp.bios.response.GenericResponse;
import com.dsp.bios.service.UploadBillTextService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/upload-bill-text")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class UploadBillTextController {

    private final UploadBillTextService uploadBillTextService;
    @PostMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> updateUploadBillTextData(@PathVariable Long id, @RequestBody @Valid List<UploadBillTextDTO> dto){
        uploadBillTextService.save(id,dto);
        GenericResponse<Void> response =new GenericResponse<>();
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<List<UploadBillTextDTO>>> UploadBillTextData(@PathVariable Long id){
        GenericResponse<List<UploadBillTextDTO>> response =new GenericResponse<>();
        response.setData(uploadBillTextService.getById(id));
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok(response);

    }
}
