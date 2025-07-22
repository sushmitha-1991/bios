package com.dsp.bios.dto.status;


import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BillStatusDTO {
    @NotNull
    private Long billStatusId;
    private LocalDate date;
    private Long displaySequence;
}