package com.dsp.bios.service;

import com.dsp.bios.service.dao.BillDAO;

import java.util.List;

public interface DashBoardDAOService {
    List<BillDAO> getPublished(String locale);

    List<BillDAO> filterBills(String locale, String billType, String ministry, String status, Long lokSabhaId, Long lokSabhaSessionId, Long rajyaSabhaId, String introducedIn, String passedIn, Long pendingStatus, Boolean isOrdinanceReplacingBill, String referredToCommittee,String introducedBy);
}
