package com.dsp.bios.controller;

import com.dsp.bios.dto.committee.*;
import com.dsp.bios.response.GenericResponse;
import com.dsp.bios.service.CommitteeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/committee")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class CommitteeController {

    private final CommitteeService committeeService;
    @PostMapping("/{id}")
    public ResponseEntity<GenericResponse<Void>> updateCommittedData(@PathVariable Long id, @Parameter(description = "Create or Update committee", required = true,
            schema =@Schema(oneOf = {JointCommitteeDTO.class, LSSelectCommitteeDTO.class, RSSelectCommitteeDTO.class,DepartmentalStandingCommitteeDTO.class})) @RequestBody  @Valid CommitteeTypeDTO dto){
        committeeService.save(id,dto);
        GenericResponse<Void> response =new GenericResponse<>();
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{id}")
    public ResponseEntity<GenericResponse<CommitteeTypeDTO>> updateCommittedData(@PathVariable Long id){
        GenericResponse<CommitteeTypeDTO> response =new GenericResponse<>();
        response.setData( committeeService.getById(id) );
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok(response);
    }
}
