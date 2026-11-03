package com.dsp.bios.dsp1integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto {
    private Integer currentPageNumber;
    private Integer perPageSize;
    private Long totalElements;
    private Integer totalPages;
}