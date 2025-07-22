package com.dsp.bios.controller;


import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.entity.Bill;
import com.dsp.bios.response.GenericResponse;
import com.dsp.bios.service.DashBoardDAOService;
import com.dsp.bios.service.DashboardService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/dashboard")
@SecurityRequirement(name = "bearerAuth")
public class Dashboard {
    private final DashboardService dashboardService;
    private final DashBoardDAOService dashBoardDAOService;
    @GetMapping("/published")
    ResponseEntity<GenericResponse<List<Bill>>> getPublished() {
        GenericResponse<List<Bill>> response =new GenericResponse<>();
        response.setData( dashboardService.getPublished() );
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/v2/published")
    ResponseEntity<GenericResponse<List<BillDTO>>> getPublishedDAO(@RequestParam(defaultValue = "en") String locale) {
        GenericResponse<List<BillDTO>> response =new GenericResponse<>();
        response.setData( dashboardService.getPublishedV2(locale) );
        response.setSuccess(Boolean.TRUE);
        response.setStatusCode(HttpStatus.OK.value());
        response.setMessage("SUCCESS");
        return ResponseEntity.ok(response);
    }
    @GetMapping("/v2/filter")
    ResponseEntity<GenericResponse<List<BillDTO>>> getPublishedDAO(@RequestParam(defaultValue = "en") String locale,
                                                                   @RequestParam(required = false) String billCategoryAbbr,
                                                                   @RequestParam(required = false) String ministryAbbr,
                                                                   @RequestParam(required = false) String statusAbbr,
                                                                   @RequestParam(required = false) Long lokSabhaId,
                                                                   @RequestParam(required = false) Long lokSabhaSessionId,
                                                                   @RequestParam(required = false) Long rajyaSabhaId,
                                                                   @RequestParam(required = false) String introducedIn,
                                                                   @RequestParam(required = false) String introducedBy,
                                                                   @RequestParam(required = false) String passedIn,
                                                                   @RequestParam(required = false) Long pendingStatusId,
                                                                   @RequestParam(required = false) Boolean isOrdinanceReplacingBill,
                                                                   @RequestParam(required = false) String referredToCommittee
                                                                   ) {
        GenericResponse<List<BillDTO>> response =new GenericResponse<>();
        response.setData( dashboardService.filterBills(locale, billCategoryAbbr, ministryAbbr, statusAbbr, lokSabhaId, lokSabhaSessionId, rajyaSabhaId, introducedIn, passedIn, pendingStatusId, isOrdinanceReplacingBill, referredToCommittee,introducedBy)
     );
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
