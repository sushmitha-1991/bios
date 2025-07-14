package com.dsp.bios.service.impl;

import com.dsp.bios.dto.newbillentry.EntryDataDTO;
import com.dsp.bios.entity.Bill;
import com.dsp.bios.entity.EntryData;
import com.dsp.bios.exception.BadRequest;
import com.dsp.bios.mapper.NewEntryToBill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.repository.LSEntryDataEntityRepository;
import com.dsp.bios.service.FormService;
import com.dsp.bios.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FormServiceImpl implements FormService {

    private final LSBillsViewDetailRepository billsRepo;
    private final LSEntryDataEntityRepository entryRepo;
    private final NewEntryToBill mapper;
    private final UserUtils userUtils;
    private final JdbcTemplate jdbcTemplate;


    @Transactional
    public Long save(EntryDataDTO dto, Long draftId) {
       boolean exits=billsRepo.checkIfBillNumberAlreadyExits(dto.getBillNumber(),dto.getBillYear());
       if(exits){
           throw new BadRequest("bill number in same year already exits");
       }
        Bill billView =  mapper.entryDataDTOtoBill(dto);
        billView.setHouseId(userUtils.getHouseId());
        billView.setIsDraft(false);
       billView= billsRepo.save(billView);
       EntryData entryData=new EntryData();
       entryData.setBillId(billView.getId());
       if(Boolean.TRUE.equals(dto.getRadioC())){
           dto.setRadioA(Boolean.TRUE);
           dto.setRadioB(Boolean.TRUE);
       }
        entryData.setEntryData(dto);
       entryData=entryRepo.save(entryData);
       removeDraft(draftId);
        return  entryData.getBillId();
    }

    @Transactional(readOnly = true)
    public EntryDataDTO getData(Long id){
        EntryDataDTO dto= entryRepo.getReferenceById(id).getEntryData();

        if(Boolean.TRUE.equals(dto.getRadioC())){
            dto.setRadioA(null);
            dto.setRadioB(null);
        }
        if(Boolean.TRUE.equals(dto.getRadioB()) && Boolean.TRUE.equals(dto.getRadioA()) ) {
            dto.setRadioA(null);
            dto.setRadioB(null);
        }
        return dto;
    }

    @Transactional
    public void update(Long id, EntryDataDTO dto, Long draftId) {
        boolean exits=billsRepo.checkIfBillNumberAlreadyExitsNotForId(dto.getBillNumber(),dto.getBillYear(),id);
        if(exits){
            throw new BadRequest("bill number in same year already exits");
        }
        Bill dbEntryView = billsRepo.getReferenceById(id);
        Bill billView =  mapper.entryDataDTOtoBill(dto);
        billView.setIsDraft(false);
        billView.setId(id);
        billView.setHouseId(userUtils.getHouseId());
        copyBillView(dbEntryView,billView);
        billsRepo.save(dbEntryView);

        EntryData dbEntryData=entryRepo.getReferenceById(id);
        if(Boolean.TRUE.equals(dto.getRadioC())){
            dto.setRadioA(Boolean.TRUE);
            dto.setRadioB(Boolean.TRUE);
        }
        dbEntryData.setEntryData(dto);
        entryRepo.save(dbEntryData);
        removeDraft(draftId);
    }

    private void copyBillView(Bill dbEntry, Bill billView) {
       dbEntry.setHouseAbbr(billView.getHouseAbbr());
       dbEntry.setBillNo(billView.getBillNo());
       dbEntry.setShortTitle(billView.getShortTitle());
       dbEntry.setIntroducedBy(billView.getIntroducedBy());
       dbEntry.setBillCategory(billView.getBillCategory());
       dbEntry.setBillCategoryAbbr(billView.getBillCategoryAbbr());
       dbEntry.setMinistry(billView.getMinistry());
       dbEntry.setMinistryAbbr(billView.getMinistryAbbr());
       dbEntry.setMemberName(billView.getMemberName());
       dbEntry.setMemberId(billView.getMemberId());
       dbEntry.setDateOfIntroduction(billView.getDateOfIntroduction());
       dbEntry.setStatus(billView.getStatus());
       dbEntry.setStatusAbbr(billView.getStatusAbbr());
       dbEntry.setLokSabhaNumber(billView.getLokSabhaNumber());
       dbEntry.setLokSabhaNumberId(billView.getLokSabhaNumberId());
       dbEntry.setLokSabhaSessionNumber(billView.getLokSabhaSessionNumber());
       dbEntry.setLokSabhaSessionNumberId(billView.getLokSabhaSessionNumberId());
       dbEntry.setRajSabhaSessionNumber(billView.getRajSabhaSessionNumber());
       dbEntry.setRajSabhaSessionNumberId(billView.getRajSabhaSessionNumberId());
       dbEntry.setIsDraft(billView.getIsDraft());
       dbEntry.setHouseId(billView.getHouseId());
    }
    private void removeDraft(Long draftId) {
        if (draftId != null) {
            jdbcTemplate.update("UPDATE master.drafted_document SET is_deleted = true, is_active = false WHERE id = ?",
                    draftId);
        }
    }
}