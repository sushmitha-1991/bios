package com.dsp.bios.controller;


import com.dsp.bios.entity.Bill;
import com.dsp.bios.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
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
    ResponseEntity<List<Bill>> getPublished() {
         List<Bill> bills= dashboardService.getPushblished();
         return ResponseEntity.ok(bills);
    }
    @GetMapping("/drafts")
    ResponseEntity<List<Bill>> getDrafts() {
        List<Bill> bills=   dashboardService.getDrafts();
        return ResponseEntity.ok(bills);
    }
}
