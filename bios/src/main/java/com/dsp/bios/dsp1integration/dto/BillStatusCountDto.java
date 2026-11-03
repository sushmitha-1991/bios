package com.dsp.bios.dsp1integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class BillStatusCountDto {

    private String billStatus;
    private Integer count;
}
