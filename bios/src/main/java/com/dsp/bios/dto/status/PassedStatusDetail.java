package com.dsp.bios.dto.status;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassedStatusDetail {
    private String status;
    private String statusAbbr;
    private LocalDate passedDate;
    private Long sessionId;
    private Long houseId;
}