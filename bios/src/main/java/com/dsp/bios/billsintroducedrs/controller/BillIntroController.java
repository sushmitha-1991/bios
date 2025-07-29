package com.dsp.bios.billsintroducedrs.controller;

import com.dsp.bios.billsintroducedrs.dto.BillBySessionDTO;
import com.dsp.bios.billsintroducedrs.service.BillIntro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api_rs/business")
public class BillIntroController {

    @Autowired
    private BillIntro billIntro;

    /**
     * API to fetch all bills by Rajya Sabha session number.
     * No pagination is applied here.
     *
     * Example: GET /api_rs/business/bills/by-session?sessionno=260
     *
     * @param sessionno Rajya Sabha session number (required)
     * @return List of BillBySessionDTO
     */
    @GetMapping("/bills/by-session")
    public ResponseEntity<List<BillBySessionDTO>> getBillsBySession(
            @RequestParam int sessionno
    ) {
        List<BillBySessionDTO> response = billIntro.getBillsBySession(sessionno);
        return ResponseEntity.ok(response);
    }
}
