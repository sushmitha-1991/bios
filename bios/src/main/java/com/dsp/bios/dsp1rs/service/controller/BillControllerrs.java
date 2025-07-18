package com.dsp.bios.dsp1rs.service.controller;

import com.dsp.bios.dsp1integration.dto.BillResponseDTO;
import com.dsp.bios.dsp1integration.service.BillService;
import com.dsp.bios.dsp1rs.service.BillServicers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api_rs/business")
public class BillControllerrs {

    @Autowired
    private BillServicers billServicers;

    @GetMapping("/bills")
    public ResponseEntity<BillResponseDTO> getBills(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,

            @RequestParam(required = false) String billNumber,
            @RequestParam(required = false) String billName,
            @RequestParam(required = false) String billType,
            @RequestParam(required = false) String billCategory,
            @RequestParam(required = false) String ministryName,
            @RequestParam(required = false) Long billYear,
            @RequestParam(required = false) String billIntroducedInHouse,
            @RequestParam(required = false) String billIntroducedBy,

            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate introductionDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate introductionDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate passedInLsDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate passedInLsDateTo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate passedInRsDateFrom,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate passedInRsDateTo,

//            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate billIntroducedDate,




            @RequestParam(required = false) String billIntroducedFile,

            @RequestParam(required = false) String billPassedInLSDate,
            @RequestParam(required = false) String billPassedInLSFile,
            @RequestParam(required = false) String billPassedInRSDate,
            @RequestParam(required = false) String billPassedInRSFile,
            @RequestParam(required = false) String billPassedInBothHousesFile,
            @RequestParam(required = false) String errataFile,
            @RequestParam(required = false) String referredToCommitteeDate,
            @RequestParam(required = false) String reportPresentedDate,
            @RequestParam(required = false) String reportFile,
            @RequestParam(required = false) String actNo,
            @RequestParam(required = false) String actYear,
            @RequestParam(required = false) String billAssentedDate,
            @RequestParam(required = false) String billGazettedFile,
            @RequestParam(required = false) String billSynopsisFile,
            @RequestParam(required = false) String mpCode,

            @RequestParam(required = false) String status
    ) {
        BillResponseDTO response = billServicers.getBills(
                page, size,
                billNumber, billName, billType, billCategory,
                ministryName, billYear, billIntroducedInHouse, billIntroducedBy,
                introductionDateFrom, introductionDateTo,
                passedInLsDateFrom, passedInLsDateTo,
                passedInRsDateFrom, passedInRsDateTo,
                status
        );

        return ResponseEntity.ok(response);
    }
}
