package com.dsp.bios.dto.uploadBillText;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Valid
public class File{
    @NotNull(message = "language should not be null")
    private String language;
    @NotNull(message = "file UUID should not be null")
    private String fileUUID;
}

