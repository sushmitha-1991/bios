package com.dsp.bios.controller;

import com.dsp.bios.dto.committee.*;
import com.dsp.bios.dto.newbillentry.SaveResponseDTO;
import com.dsp.bios.dto.status.*;
import com.dsp.bios.response.GenericResponse;
import com.dsp.bios.service.status.StatusGetService;
import com.dsp.bios.service.status.StatusService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/status-data")
@AllArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class StatusController {
    private final StatusService statusService;
    private final StatusGetService statusGetService;


    @PostMapping("/main/update/{billId}")
    public ResponseEntity<GenericResponse<Void>> updateBillStatuses(@PathVariable Long billId, @RequestBody List<BillStatusDTO> billStatusDTOs) {
        statusService.updateBillStatuses(billId, billStatusDTOs);
        GenericResponse<Void>  respBody= new GenericResponse<>();
        respBody.setSuccess(Boolean.TRUE);
        respBody.setStatusCode(HttpStatus.OK.value());
        respBody.setMessage("Statuses updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(respBody);
    }

    @PostMapping("/pending/update/{billId}")
    public ResponseEntity<GenericResponse<Void>> updateBillPendingStatuses(@PathVariable Long billId, @RequestBody List<PendingStatusDTO> dto) {
        statusService.createOrUpdatePendingStatuses(billId, dto);
        GenericResponse<Void>  respBody= new GenericResponse<>();
        respBody.setSuccess(Boolean.TRUE);
        respBody.setStatusCode(HttpStatus.OK.value());
        respBody.setMessage("Pending statuses updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(respBody);
    }

    @PostMapping("/passed/update/{billId}")
    public ResponseEntity<GenericResponse<Void>> updateBillPassedStatuses(@PathVariable Long billId, @RequestBody List<PassedStatusDTO> dto) {
        statusService.createOrUpdatePassedStatuses(billId, dto);
        GenericResponse<Void>  respBody= new GenericResponse<>();
        respBody.setSuccess(Boolean.TRUE);
        respBody.setStatusCode(HttpStatus.OK.value());
        respBody.setMessage("Passed statuses updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(respBody);

    }


    @GetMapping("/main/{billId}")
    public ResponseEntity<GenericResponse<List<Map<String,Object>>>> updateBillPassedStatuses(@PathVariable Long billId,@RequestParam(defaultValue = "en") String locale) {
        List<Map<String,Object>> serviceResponse=statusGetService.getMainStatus(billId,locale);
        GenericResponse<List<Map<String,Object>>>  respBody= new GenericResponse<>();
        respBody.setSuccess(Boolean.TRUE);
        respBody.setStatusCode(HttpStatus.OK.value());
        respBody.setData(serviceResponse);
        respBody.setMessage("Passed statuses updated successfully");
        return ResponseEntity.status(HttpStatus.OK).body(respBody);

    }


}
