package com.dsp.bios.dto.status;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillStatusResponseDTO {
    private Long id;
    private Long billStatusId;
    private String abbr;
    private String statusDisplayName;
    private String statusDate;
}