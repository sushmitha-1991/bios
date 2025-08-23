package com.dsp.bios.service;

import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.dto.DashboardBillsResponseDTO;
import com.dsp.bios.entity.Bill;

import java.util.List;

public interface DashboardService {
    List<Bill> getPublished();

    List<Bill> getDrafts();

    List<BillDTO> getPublishedV2(String locale);

    List<BillDTO> filterBills(String locale, String billType, String ministry, String status, Long lokSabhaId, Long lokSabhaSessionId, Long rajyaSabhaId, String introducedIn, String passedIn, Long pendingStatus, Boolean isOrdinanceReplacingBill, String referredToCommittee,String introducedBy);

    public List<DashboardBillsResponseDTO> getMgmtDashboardBills(String billType, String lokSabhaNo, String sessionNo);
}