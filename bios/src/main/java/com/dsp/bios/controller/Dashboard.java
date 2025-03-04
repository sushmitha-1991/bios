package com.dsp.bios.controller;


import com.dsp.bios.entity.Bill;
import com.dsp.bios.response.GenericResponse;
import com.dsp.bios.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/dashboard")
@SecurityRequirement(name = "bearerAuth")
public class Dashboard {
    private final DashboardService dashboardService;
    @GetMapping("/published")
    ResponseEntity<GenericResponse<List<Bill>>> getPublished() {
        GenericResponse<List<Bill>> response =new GenericResponse<>();
        response.setData( dashboardService.getPublished() );
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/drafts")
    ResponseEntity<GenericResponse<List<Bill>>> getDrafts() {
        GenericResponse<List<Bill>> response =new GenericResponse<>();
        response.setData(  dashboardService.getDrafts());
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok(response);
    }
}
