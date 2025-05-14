package com.dsp.bios.service;

import com.dsp.bios.dto.committee.CommitteeTypeDTO;

public interface CommitteeService {
    void save(Long id, CommitteeTypeDTO dto);
    CommitteeTypeDTO getById(Long id);
}
