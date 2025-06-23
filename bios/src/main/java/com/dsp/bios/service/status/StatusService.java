package com.dsp.bios.service.status;

import com.dsp.bios.dto.status.BillStatusDTO;
import com.dsp.bios.dto.status.PassedStatusDTO;
import com.dsp.bios.dto.status.PendingStatusDTO;

import java.util.List;

public interface StatusService {
    void updateBillStatuses(Long billId, List<BillStatusDTO> billStatusDTOs);

    void createOrUpdatePendingStatuses(Long billId, List<PendingStatusDTO> pendingStatusDTOs);

    void createOrUpdatePassedStatuses(Long billId, List<PassedStatusDTO> passedStatusDTOs);


}
