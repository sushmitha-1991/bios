package com.dsp.bios.service;

import com.dsp.bios.dto.BillHistoryResponseDTO;
import com.dsp.bios.entity.BillHistory;

import java.util.List;

public interface HistoryService {
    void create(Long billId, String formType, String actionType);

    List<BillHistoryResponseDTO> getHistory(Long billId);
}