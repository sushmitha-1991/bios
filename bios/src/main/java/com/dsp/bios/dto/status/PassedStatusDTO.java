package com.dsp.bios.dto.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Valid
public class PassedStatusDTO {
    private Long id;
    @NotNull
    private String houseAbbr;
    private Long lokSabhaNumber;
    private Long lokSabhaSessionNumber;
    private Long rajyaSabhaSessionNumber;
    private LocalDate passedDate;
    private Long displaySequence;
}