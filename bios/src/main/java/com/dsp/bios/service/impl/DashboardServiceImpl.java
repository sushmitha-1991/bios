package com.dsp.bios.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.dsp.bios.dto.BillDTO;
import com.dsp.bios.dto.DashboardBillsCountResponseDTO;
import com.dsp.bios.dto.DashboardBillsDetailsResponseDTO;
import com.dsp.bios.dto.UserDetailsDTO;
import com.dsp.bios.entity.Bill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.service.DashBoardDAOService;
import com.dsp.bios.service.DashboardService;
import com.dsp.bios.service.dao.BillDAO;
import com.dsp.bios.util.DateFormatterUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class DashboardServiceImpl implements DashboardService {

	private final String pending="BILL_STATUS_PENDING";
	private final String passed="BILL_STATUS_PASSED";
	private final String lapsed="BILL_STATUS_LAPSED";
	
	@Autowired
	private ObjectMapper objectMapper;
	
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
    public List<DashboardBillsCountResponseDTO> getMgmtDashboardBillsCount(String billType, String lokSabhaNo, String sessionNo)
    {
    	Long houseId=getHouseId();
    	String houseAbbr= (houseId==2) ? "LS" : "RS";
    	
    		List<Object[]> results = billsView.getBillsCountByLokSabhaAndRajyaSabha(houseAbbr,billType,lokSabhaNo,sessionNo);
    		
    		return results.isEmpty() ? Collections.emptyList() : results.stream()
                    .map(row -> new DashboardBillsCountResponseDTO(
                    		row[0] != null ? row[0].toString() : null, // sessionNo
                            row[1] != null ? row[1].toString() : null, // totalBillsIntroduced
                            row[2] != null ? row[2].toString() : null, // billsPending
                            row[3] != null ? row[3].toString() : null, //billsPassed		
                            row[4] != null ? row[4].toString() : null // billsLapsed
                    		))
                    .toList();
    }
    
    @Override
    public Map<String, List<DashboardBillsDetailsResponseDTO>> getMgmtDashboardBillsDetails(String billType, String lokSabhaNo, String sessionNo)
    {
    	Map<String, List<DashboardBillsDetailsResponseDTO>> response = new HashMap<>();
		List<DashboardBillsDetailsResponseDTO> dataList = new ArrayList<>();
		Long houseId = getHouseId();
		String houseAbbr = (houseId == 2) ? "LS" : "RS";

		List<Object[]> introducedResults = billsView.getAllBillsDetails(null, houseAbbr, billType, lokSabhaNo,
				sessionNo);
		List<Object[]> pendingResults = billsView.getAllBillsDetails(pending, houseAbbr, billType, lokSabhaNo,
				sessionNo);
		List<Object[]> passedResults = billsView.getAllBillsDetails(passed, houseAbbr, billType, lokSabhaNo, sessionNo);
		List<Object[]> lapsedResults = billsView.getAllBillsDetails(lapsed, houseAbbr, billType, lokSabhaNo, sessionNo);

		response.put("introducedBills",mapResponseToDto(introducedResults));
		response.put("pendingBills",mapResponseToDto(pendingResults));
		response.put("passedBills",mapResponseToDto(passedResults));
		response.put("lapsedBills",mapResponseToDto(lapsedResults));

		return response;
    }
    
    public List<DashboardBillsDetailsResponseDTO> mapResponseToDto(List<Object[]> results)
    {

		return results.isEmpty() ? Collections.emptyList() : results.stream().map(row -> {
			String actNo = null;
			String assentDate = null;
			String passedIn = null;
			String passedDate = null;

			if (row[10] != null) { // entryData JSON column
				try {
					JsonNode node = objectMapper.readTree(row[10].toString());
					actNo = node.has("actNo") ? node.get("actNo").asText() : null;
//					assentDate = node.has("dateOfAssented") ? node.get("dateOfAssented").asText() : null;
					if (node.has("dateOfAssented") && node.get("dateOfAssented").isArray()) {
			            JsonNode dateArray = node.get("dateOfAssented");
			            if (dateArray!=null && dateArray.size() == 3) {
			            	try {
			                int year = dateArray.get(0).asInt();
			                int month = dateArray.get(1).asInt();
			                int day = dateArray.get(2).asInt();
			                passedDate = String.format("%04d-%02d-%02d", year, month, day);
			            	}
			            	catch(Exception e)
			            	{
			            		log.error("Invalid assent Date", e);
			            	}
			            }
					}
					passedIn = node.has("passedIn") ? node.get("passedIn").asText() : null;
//					passedDate = node.has("passedDate") ? node.get("passedDate").asText() : null;
//					passedDate is array
					if (node.has("passedDate") && node.get("passedDate").isArray()) {
			            JsonNode dateArray = node.get("passedDate");
			            if (dateArray!=null && dateArray.size() == 3) {
			            	try {
			                int year = dateArray.get(0).asInt();
			                int month = dateArray.get(1).asInt();
			                int day = dateArray.get(2).asInt();
			                passedDate = String.format("%04d-%02d-%02d", year, month, day);
			            	}
			            	catch(Exception e)
			            	{
			            		log.error("Invalid passed Date", e);
			            	}
			            }
					}
					System.out.println("act- "+actNo+" assent date -"+assentDate+" passedin "+passedIn+" passed date -"+passedDate);
				} catch (Exception e) {
					log.error("Error parsing entryData JSON", e);
				}
			}

			return DashboardBillsDetailsResponseDTO.builder()
					.billNo(row[0] != null ? row[0].toString() : null)
					.lokSabhaNo(row[1] != null ? row[1].toString() : null)
					.lokSabhaSessionNo(row[2] != null ? row[2].toString() : null)
					.rajyaSabhaSessionNo(row[3] != null ? row[3].toString() : null)
					.shortTitle(row[4] != null ? row[4].toString() : null)
					.billCategory(row[5] != null ? row[5].toString() : null)
					.status(row[6] != null ? row[6].toString() : null)
					.ministry(row[7] != null ? row[7].toString() : null)
					.memberName(row[8] != null ? row[8].toString() : null)
					.passedIn(passedIn)
					.passedDate(passedDate != null && !"null".equalsIgnoreCase(passedDate)? DateFormatterUtil.formatDate(passedDate):null)
					.introductionDate(row[9] != null && !"null".equalsIgnoreCase(row[9].toString())? DateFormatterUtil.formatDate(row[9].toString()) : null)
					.actNo(actNo)
					.assentDate(assentDate != null && !"null".equalsIgnoreCase(assentDate)? DateFormatterUtil.formatDate(assentDate) : null)
					.build();
		}).toList();
    }
}
