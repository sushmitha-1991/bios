package com.dsp.bios.service.dao;

import lombok.Data;

import java.time.LocalDate;

@Data
public class BillDAO {
    private Long id;
    private String billNo;
    private String shortTitle;
    private String introducedBy;
    private String billCategory;
    private String ministry;
    private String englishDisplayName;
    private String hindiDisplayName;
    private LocalDate dateOfIntroduction;
    private String status;
}
