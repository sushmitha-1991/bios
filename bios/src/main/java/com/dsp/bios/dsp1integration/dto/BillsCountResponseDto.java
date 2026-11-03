package com.dsp.bios.dsp1integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillsCountResponseDto {
    private String session;
    private Integer count;
}