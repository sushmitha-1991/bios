package com.dsp.bios.dto.status;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Valid
public class PendingStatusDTO {
    private Long id;
    @NotNull
    private Long pendingStatusId;
    private LocalDate pendingStatusDate;
    private Long displaySequence;
}