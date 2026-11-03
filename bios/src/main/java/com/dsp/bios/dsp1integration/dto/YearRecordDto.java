package com.dsp.bios.dsp1integration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearRecordDto {

    private Integer year;
    private YearCountDto statistics;

}