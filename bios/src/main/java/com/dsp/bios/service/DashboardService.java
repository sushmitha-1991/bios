package com.dsp.bios.service;

import java.util.List;
import java.util.Map;

import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.dto.DashboardBillsCountResponseDTO;
import com.dsp.bios.dto.DashboardBillsDetailsResponseDTO;
import com.dsp.bios.entity.Bill;

public interface DashboardService {
    List<Bill> getPublished();

    List<Bill> getDrafts();

    List<BillDTO> getPublishedV2(String locale);

    List<BillDTO> filterBills(String locale, String billType, String ministry, String status, Long lokSabhaId, Long lokSabhaSessionId, Long rajyaSabhaId, String introducedIn, String passedIn, Long pendingStatus, Boolean isOrdinanceReplacingBill, String referredToCommittee,String introducedBy);

    public List<DashboardBillsCountResponseDTO> getMgmtDashboardBillsCount(String billType, String lokSabhaNo, String sessionNo);
    public Map<String, List<DashboardBillsDetailsResponseDTO>> getMgmtDashboardBillsDetails(String billType, String lokSabhaNo, String sessionNo);
}