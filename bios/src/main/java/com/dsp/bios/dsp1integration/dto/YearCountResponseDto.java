package com.dsp.bios.dsp1integration.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearCountResponseDto {

    private MetadataDto _metadata;
    private List<YearRecordDto> records;

}