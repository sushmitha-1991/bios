package com.dsp.bios.dto;

import jakarta.validation.Valid;
import lombok.Data;

@Data
public class LSBillSaveRequestDTO {
    private Boolean isDraft;
    @Valid
    private LSFormDataDTO lsFormDataDTO;
}
