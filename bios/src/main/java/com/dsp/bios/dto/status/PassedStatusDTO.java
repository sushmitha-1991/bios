package com.dsp.bios.dto.status;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PassedStatusDTO {
    private Long id;
    private String houseAbbr;
    private Long lokSabhaNumber;
    private Long lokSabhaSessionNumber;
    private Long rajyaSabhaSessionNumber;
    private LocalDate passedDate;
    private Long displaySequence;
}