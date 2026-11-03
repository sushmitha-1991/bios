package com.dsp.bios.dsp1integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusWiseBillDto {
    private String sessionNo;
    private String noOfGovtBills;
    private String noOfPrivateBills;
}
