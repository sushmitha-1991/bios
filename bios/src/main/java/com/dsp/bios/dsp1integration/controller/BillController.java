package com.dsp.bios.dsp1integration.controller;

import com.dsp.bios.dsp1integration.dto.BillResponseDTO;
import com.dsp.bios.dsp1integration.dto.BillStatusCountDto;
import com.dsp.bios.dsp1integration.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api_ls/business")
public class BillController {

    @Autowired
    private BillService billService;

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
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate billIntroducedDate,
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
            @RequestParam(required = false) String status
    ) {
        BillResponseDTO response = billService.getBills(
                page, size,
                billNumber, billName, billType, billCategory,
                ministryName, billYear, billIntroducedInHouse, billIntroducedBy,
                billIntroducedDate, status
                // You can also pass the extra file/date params into service if you plan to filter by them.
        );

        return ResponseEntity.ok(response);
    }
    @GetMapping("/bill-status")
    public ResponseEntity<List<String>> getBillStatus() {
         List<String> response = billService.getBillStatus();
        return ResponseEntity.ok(response);
    }
    @GetMapping("/bill-status-count")
    public ResponseEntity<List<BillStatusCountDto>> getBills(@RequestParam String house,
                                                            @RequestParam(required = false) Long hsNo) {
         List<BillStatusCountDto> response = billService.getBillStatusCount(house, hsNo);
        return ResponseEntity.ok(response);
    }

}
