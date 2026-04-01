package com.dsp.bios.dsp1integration.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDTO {
    private MetadataDto _metadata;
    private List<StatusWiseBillDto> records;
}
