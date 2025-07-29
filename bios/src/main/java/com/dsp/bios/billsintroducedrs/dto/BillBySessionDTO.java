package com.dsp.bios.billsintroducedrs.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillBySessionDTO {
    private String bill_no;
    private Integer bill_year;
    private String title;
    private String type;
    private String intro;
    private String ministry;
    private Long bill_id;
    private Integer house_number;
    private String as_introduced_file;
    private String as_passedrajyasabha_file;
    private String as_passedloksabha_file;
    private String as_passedbothhouses_file;
    private String errata_lsfile;
    private String house;
    private Integer sessionno;
    private Integer IntroSession;
    private Object RetSession;
    private String passing_dt;
    private String passing;
    private String H_as_introduced_file;
    private String H_as_passedrajyasabha_file;
    private String H_as_passedloksabha_file;
    private String H_as_passedbothhouses_file;
    private String H_errata_lsfile;
    private Object return_second_house;
    private String introduction;
    private String as_introduced_file_dsp;
    private String as_passedrajyasabha_file_dsp;
    private Object as_passedloksabha_file_dsp;
    private String as_passedbothhouses_file_dsp;
    private String errata_lsfile_dsp;
    private Object H_as_introduced_file_dsp;
    private Object H_as_passedrajyasabha_file_dsp;
    private Object H_as_passedloksabha_file_dsp;
    private Object H_as_passedbothhouses_file_dsp;
    private Object H_errata_lsfile_dsp;
    private String as_introduced_file_size;
    private String as_passedrajyasabha_file_size;
    private Object as_passedloksabha_file_size;
    private String as_passedbothhouses_file_size;
    private String errata_lsfile_size;
    private String H_as_introduced_file_size;
    private String H_as_passedrajyasabha_file_size;
    private Object H_as_passedloksabha_file_size;
    private Object H_as_passedbothhouses_file_size;
    private Object H_errata_lsfile_size;
}
