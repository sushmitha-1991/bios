package com.dsp.bios.controller;

import com.dsp.bios.dto.committee.*;
import com.dsp.bios.service.impl.CommitteeService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/committee")
@AllArgsConstructor
public class CommitteeController {

    private final CommitteeService committeeService;
    @PostMapping("/{id}")
    public ResponseEntity<Void> updateCommittedData(@PathVariable Long id, @Parameter(description = "Create or Update commitee", required = true,
            schema =@Schema(oneOf = {JointCommitteeDTO.class, LSSelectCommitteeDTO.class, RSSelectCommitteeDTO.class,DepartmentalStandingCommitteeDTO.class})) @RequestBody  @Valid CommitteeTypeDTO dto){
        committeeService.save(id,dto);
        return ResponseEntity.ok().build();
    }
    @GetMapping("/{id}")
    public ResponseEntity<CommitteeTypeDTO> updateCommittedData(@PathVariable Long id){
        return ResponseEntity.ok(committeeService.getById(id));

    }
}
