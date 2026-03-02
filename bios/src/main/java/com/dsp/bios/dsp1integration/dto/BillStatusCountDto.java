package com.dsp.bios.dsp1integration.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Getter
@Setter
@ToString
@NoArgsConstructor
public class BillStatusCountDto {

    private String billStatus;
    private Integer count;
}
