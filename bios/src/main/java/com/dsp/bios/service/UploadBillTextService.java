package com.dsp.bios.service;

import com.dsp.bios.dto.uploadBillText.UploadBillTextDTO;

import java.util.List;

public interface UploadBillTextService {
    void save(Long id, List<UploadBillTextDTO> dto);

    List<UploadBillTextDTO> getById(Long id);
}
