package com.dsp.bios.dto.status;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PendingStatusDTO {
    private Long id;
    private Long pendingStatusId;
    private LocalDate pendingStatusDate; // Change to Long if pending_status_date is numeric
}