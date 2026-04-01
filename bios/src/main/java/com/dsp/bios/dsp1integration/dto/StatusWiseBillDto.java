package com.dsp.bios.dsp1integration.dto;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatusWiseBillDto {
    private String sessionNo;
    private String noOfGovtBills;
    private String noOfPrivateBills;
}
