package com.dsp.bios.controller;

import com.dsp.bios.dto.NewBillEntry.EntryDataDTO;
import com.dsp.bios.dto.NewBillEntry.SaveResponseDTO;
import com.dsp.bios.response.GenericResponse;
import com.dsp.bios.service.FormService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/form-data")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class FormDataController {


    private final FormService formService;


    @PostMapping
    public ResponseEntity<GenericResponse<SaveResponseDTO>> createBillEntry(@RequestBody @Valid EntryDataDTO formDataDetails) {

            Long id = formService.save(formDataDetails);
            SaveResponseDTO resp = SaveResponseDTO.builder().id(id).build();
            GenericResponse<SaveResponseDTO>  respBody= new GenericResponse<>();
            respBody.setSuccess(Boolean.TRUE);
            respBody.setStatusCode(HttpStatus.OK.value());
            respBody.setData(resp);
            respBody.setMessage("SUCCESS");
            return ResponseEntity.status(HttpStatus.OK).body(respBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<EntryDataDTO>> createBillEntry(@PathVariable Long id) {

            EntryDataDTO data= formService.getData(id);
            GenericResponse<EntryDataDTO>  respBody= new GenericResponse<>();
            respBody.setSuccess(Boolean.TRUE);
            respBody.setStatusCode(HttpStatus.OK.value());
            respBody.setData(data);
            respBody.setMessage("SUCCESS");
            return ResponseEntity.status(HttpStatus.OK).body(respBody);

    }


    @PostMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> createBillEntry(@PathVariable(name="id") Long id, @RequestBody @Valid EntryDataDTO formDataDetails) {
        formService.update(id,formDataDetails);
        GenericResponse<Void>  respBody= new GenericResponse<>();
        respBody.setSuccess(Boolean.TRUE);
        respBody.setStatusCode(HttpStatus.OK.value());
        respBody.setMessage("SUCCESS");
        return ResponseEntity.status(HttpStatus.OK).body(respBody);
    }

}