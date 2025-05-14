package com.dsp.bios.dto;


import com.dsp.bios.dto.newbillentry.EntryDataDTO;
import com.dsp.bios.dto.uploadBillText.UploadBillTextDTO;
import jakarta.validation.Valid;
import lombok.Data;

import java.util.List;
@Data
public class LSFormDataDTO {
    @Valid

    public EntryDataDTO entryData;
    public List<UploadBillTextDTO> uploadBillTextDTO;

}