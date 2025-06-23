package com.dsp.bios.dto.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MainTabData {
    private String status;
    private String statusAbbr;
    private LocalDate date;
}
