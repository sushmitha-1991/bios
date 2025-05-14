package com.dsp.bios.service;

import com.dsp.bios.dto.newbillentry.EntryDataDTO;


public interface FormService {


    Long save(EntryDataDTO dto);

    EntryDataDTO getData(Long id);
    void update(Long id, EntryDataDTO dto) ;
}