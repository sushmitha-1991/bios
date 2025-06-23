package com.dsp.bios.service.status;

import com.dsp.bios.dto.status.BillPassedStatusResponseDTO;
import com.dsp.bios.dto.status.BillStatusResponseDTO;

import java.util.List;

public interface StatusGetService {
    List<BillStatusResponseDTO> getMainStatus(Long billId, String locale);
    List<BillStatusResponseDTO> getPendingStatus(Long billId,String locale);
    List<BillPassedStatusResponseDTO> getPassedStatus(Long billId, String locale);
}
