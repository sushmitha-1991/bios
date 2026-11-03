package com.dsp.bios.dsp1integration.dto;

import java.util.List;

import com.dsp.bios.dsp1integration.dto.HouseStatisticsDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class YearStatisticsDto {

    private Integer year;
    private List<HouseStatisticsDto> statistics;

}
