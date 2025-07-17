package com.dsp.bios.dto.status;

import jakarta.persistence.Column;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BillStatusDTO {
    private Long billStatusId;
    private LocalDate date;
    private Long displaySequence;
}