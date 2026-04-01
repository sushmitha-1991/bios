package com.dsp.bios.dsp1integration.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MetadataDto {
    private Integer currentPageNumber;
    private Integer perPageSize;
    private Long totalElements;
    private Integer totalPages;
}