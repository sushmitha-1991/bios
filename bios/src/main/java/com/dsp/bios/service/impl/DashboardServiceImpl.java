package com.dsp.bios.service.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.repository.query.Param;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.dto.DashboardBillsResponseDTO;
import com.dsp.bios.dto.UserDetailsDTO;
import com.dsp.bios.entity.Bill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.service.DashBoardDAOService;
import com.dsp.bios.service.DashboardService;
import com.dsp.bios.service.dao.BillDAO;

import lombok.AllArgsConstructor;

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
    
    private Long getHouseId() {
        Long houseId = null;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetailsDTO) {
            houseId = ((UserDetailsDTO) principal).getHouseId();
        }
        return houseId;
    }
    
    @Override
    public List<DashboardBillsResponseDTO> getMgmtDashboardBills(String billType, String lokSabhaNo, String sessionNo)
    {
    	Long houseId=getHouseId();
    	String houseAbbr= (houseId==2) ? "LS" : "RS";
    	
    		List<Object[]> results = billsView.getBillsCountByLokSabhaAndRajyaSabha(houseAbbr,billType,lokSabhaNo,sessionNo);
    		
    		return results.isEmpty() ? Collections.emptyList() : results.stream()
                    .map(row -> new DashboardBillsResponseDTO(
                    		row[0] != null ? row[0].toString() : null, // sessionNo
                            row[1] != null ? row[1].toString() : null, // totalBillsIntroduced
                            row[2] != null ? row[2].toString() : null, // billsPending
                            row[3] != null ? row[3].toString() : null, //billsPassed		
                            row[4] != null ? row[4].toString() : null // billsLapsed
                    		))
                    .toList();
    }
}
