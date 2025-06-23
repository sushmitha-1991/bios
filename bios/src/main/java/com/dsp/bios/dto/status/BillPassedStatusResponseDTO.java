package com.dsp.bios.dto.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillPassedStatusResponseDTO {
    private Long id;
    private Long lokSabhaNumberId;
    private Long lokSabhaSessionNumberId;
    private Long rajyaSabhaSessionNumberId;
    private LocalDate passedDate;
    private String houseName;
    private String houseAbbr;
    private String lokSabhaNumber;
    private String lokSabhaSessionNumber;
    private String rajyaSabhaSessionNumber;
}
