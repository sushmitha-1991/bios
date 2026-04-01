package com.dsp.bios.dsp1integration.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class BillCountBySessionDTO{
    MetadataDto _metadata;
    List<BillsCountResponseDto> records;
}