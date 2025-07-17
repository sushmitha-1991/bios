package com.dsp.bios.controller;

import com.dsp.bios.dto.BillHistoryResponseDTO;
import com.dsp.bios.response.GenericResponse;
import com.dsp.bios.service.HistoryService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/history")
@SecurityRequirement(name = "bearerAuth")
public class HistoryController {
    private final HistoryService historyService;
    @GetMapping("/{billId}")
    public ResponseEntity<GenericResponse<List<BillHistoryResponseDTO>>> getHistory(@PathVariable Long billId){
        List<BillHistoryResponseDTO> serviceHistoryResponse= historyService.getHistory(billId);
        GenericResponse<List<BillHistoryResponseDTO>>  respBody= new GenericResponse<>();
        respBody.setSuccess(Boolean.TRUE);
        respBody.setStatusCode(HttpStatus.OK.value());
        respBody.setMessage("History fetched successfully");
        respBody.setData(serviceHistoryResponse);
        return ResponseEntity.status(HttpStatus.OK).body(respBody);

    }
}
