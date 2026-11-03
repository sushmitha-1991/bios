package com.dsp.bios.dsp1integration.controller;

import com.dsp.bios.dsp1integration.dto.BillCountBySessionDTO;
import com.dsp.bios.dsp1integration.dto.BillResponseDTO;
import com.dsp.bios.dsp1integration.dto.BillStatusCountDto;
import com.dsp.bios.dsp1integration.dto.ResponseDTO;
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
    public ResponseEntity<List<String>> getBillStatus(@RequestParam(required = false) String locale) {
         List<String> response = billService.getBillStatus(locale);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/bill-status-count")
    public ResponseEntity<List<BillStatusCountDto>> getBills(@RequestParam String house,
                                                            @RequestParam(required = false) Long hsNo) {
         List<BillStatusCountDto> response = billService.getBillStatusCount(house, hsNo);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bill-categories")
    public ResponseEntity<List<String>> getCategories(@RequestParam(required = false) String locale) {
         List<String> response = billService.getCategories(locale);
        return ResponseEntity.ok(response);
    }

     @GetMapping("/bill-types")
    public ResponseEntity<List<String>> getBillTypes() {
         List<String> response = billService.getBillTypes();
        return ResponseEntity.ok(response);
    }

     @GetMapping("/bill-ministries")
    public ResponseEntity<List<String>> getMinistries() {
         List<String> response = billService.getMinistries();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/bill-participation")
    public ResponseEntity<Integer> getBillParticipation(@RequestParam("mpsno") String mpsno,
                                                        @RequestParam("loksabha") String lsno) {
        Integer response = billService.getBillParticipation(mpsno, lsno);
        return ResponseEntity.ok(response);
    }

    // @GetMapping("/billcount-sessionwise")
    // public ResponseEntity<BillCountBySessionDTO> getBillCountSessionWise(@RequestParam(required = false) String lokSabha,
    //                                                                      @RequestParam(required = false) String status,
    //                                                                      @RequestParam(required = false) String category,
    //                                                                      @RequestParam(required = false) Integer page,
    //                                                                      @RequestParam(required = false) Integer size) {
    //     BillCountBySessionDTO response = billService.getBillCountSessionWise(lokSabha, status, category, page, size);
    //     return ResponseEntity.ok(response);
    // }

    // @GetMapping("/billcount-by-status")
    // public ResponseEntity<ResponseDTO> getBillCountByStatus(@RequestParam(required = false) String status,
    //                                                     @RequestParam(required = false) Integer page,
    //                                                     @RequestParam(required = false) Integer size) {
    //     ResponseDTO response = billService.getBillCountByStatus(status, page, size);
    //     return ResponseEntity.ok(response);
    // }

    @GetMapping("/get-bills")
    ResponseEntity<BillResponseDTO> getBills(
        @RequestParam(defaultValue = "1") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String billNumber,
        @RequestParam(required = false) String billName,
        @RequestParam(required = false) String billType,
        @RequestParam(required = false) String billCategory,
        @RequestParam(required = false) String ministryName,
        @RequestParam(required = false) Integer billYear,
        @RequestParam(required = false) String billStatus,
        @RequestParam(required = false) LocalDate billIntroducedDateFrom,
        @RequestParam(required = false) LocalDate billIntroducedDateTo,
        @RequestParam(required = false) LocalDate billPassedInLSDateFrom,
        @RequestParam(required = false) LocalDate billPassedInLSDateTo,
        @RequestParam(required = false) LocalDate billPassedInRSDateFrom,
        @RequestParam(required = false) LocalDate billPassedInRSDateTo,
        @RequestParam(required = false) LocalDate startDate,
        @RequestParam(required = false) LocalDate endDate,
        @RequestParam(required = false) String locale,
        @RequestParam(required = false) String house,
        @RequestParam(required = false) String loksabha,
        @RequestParam(required = false) Integer session,
        @RequestParam(required = false) String memberName,
        @RequestParam(required = false) String mpCode,
        @RequestParam(required = false) Long billTypeId,
        @RequestParam(required = false) String sortOn,
        @RequestParam(required = false, defaultValue = "ASC") String sortBy)
    {
            // BillResponseDTO response = billService.getBillsData(page, size,billNumber, billName, billType,
            // billCategory, ministryName, billYear, billStatus, billIntroducedDateFrom, billIntroducedDateTo,
            // billPassedInLSDateFrom, billPassedInLSDateTo, billPassedInRSDateFrom, billPassedInRSDateTo, 
            // startDate, endDate, locale, house, loksabha, session, memberName, mpCode, billTypeId, sortOn, sortBy);
        // return ResponseEntity.ok(response);
        return ResponseEntity.ok(new BillResponseDTO());
    }
}
