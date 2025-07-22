package com.dsp.bios.service.impl;

import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.entity.Bill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.service.DashBoardDAOService;
import com.dsp.bios.service.DashboardService;
import com.dsp.bios.service.dao.BillDAO;
import com.dsp.bios.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final LSBillsViewDetailRepository billsView;
    private final DashBoardDAOService dashboardDAOService;
    public List<Bill> getPublished(){
        return billsView.findByIsDraft(false).stream().sorted(Comparator.comparing(Bill::getId).reversed()).toList();
    }

    public List<Bill> getDrafts(){
        return billsView.findByIsDraft(true);
    }

    @Override
    public List<BillDTO> getPublishedV2(String locale) {
         List<BillDAO> daos = dashboardDAOService.getPublished(locale);
        return  dtoToDao(daos,locale);
    }

    @Override
    public List<BillDTO> filterBills(String locale, String billType, String ministry, String status, Long lokSabhaId, Long lokSabhaSessionId, Long rajyaSabhaId, String introducedIn, String passedIn, Long pendingStatus, Boolean isOrdinanceReplacingBill, String referredToCommittee,String introducedBy) {
        List<BillDAO> daos = dashboardDAOService.filterBills(locale, billType, ministry, status, lokSabhaId, lokSabhaSessionId, rajyaSabhaId, introducedIn, passedIn, pendingStatus, isOrdinanceReplacingBill, referredToCommittee,introducedBy);
        return  dtoToDao(daos,locale);

    }

    public List<BillDTO> dtoToDao(List<BillDAO> dtos, String locale) {
        return dtos.stream().map(dto -> {
            BillDTO dao = new BillDTO();
            dao.setId(dto.getId());
            dao.setBillNo(dto.getBillNo());
            dao.setShortTitle(dto.getShortTitle());
            dao.setIntroducedBy(dto.getIntroducedBy());
            dao.setBillCategory(dto.getBillCategory());
            dao.setMinistry(dto.getMinistry());
            if ("hi".equalsIgnoreCase(locale) && StringUtils.hasLength(dto.getHindiDisplayName())) {
                dao.setMemberName(dto.getHindiDisplayName());
            } else {
                dao.setMemberName(dto.getEnglishDisplayName());
            }
            dao.setDateOfIntroduction(dto.getDateOfIntroduction());
            dao.setStatus(dto.getStatus());

            return dao;
        }).collect(Collectors.toList());
    }
}
