package com.dsp.bios.dsp1integration.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillsCountResponseDto {
    private String session;
    private Integer count;
}