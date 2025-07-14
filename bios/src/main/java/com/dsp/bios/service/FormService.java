package com.dsp.bios.service;

import com.dsp.bios.dto.newbillentry.EntryDataDTO;


public interface FormService {


    Long save(EntryDataDTO dto, Long draftId);

    EntryDataDTO getData(Long id);
    void update(Long id, EntryDataDTO dto, Long draftId) ;
}