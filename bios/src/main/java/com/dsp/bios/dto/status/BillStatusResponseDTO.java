package com.dsp.bios.dto.status;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillStatusResponseDTO {
    private Long id;
    private Long billStatusId;
    private String abbr;
    private String statusDisplayName;
    private String statusDate;
    private Long displaySequence;
}