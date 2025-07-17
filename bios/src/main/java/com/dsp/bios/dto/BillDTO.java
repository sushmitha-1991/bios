package com.dsp.bios.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class BillDTO {
    private Long id;
    private String billNo;
    private String shortTitle;
    private String introducedBy;
    private String billCategory;
    private String ministry;
    private String memberName;
    private LocalDate dateOfIntroduction;
    private String status;
}
