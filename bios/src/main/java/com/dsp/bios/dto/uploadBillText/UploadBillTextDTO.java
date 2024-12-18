package com.dsp.bios.dto.uploadBillText;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
@Valid
public class UploadBillTextDTO {
    @NotNull
    private String textVersion;
    @NotNull
    private String textVersionAbbr;
    @Valid
    @Size(min = 1,message = "At least one file object should present")
    private List<File> files;
}
