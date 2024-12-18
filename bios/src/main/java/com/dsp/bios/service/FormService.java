package com.dsp.bios.service;

import com.dsp.bios.Entity.Bill;
import com.dsp.bios.Entity.EntryData;
import com.dsp.bios.dto.NewBillEntry.EntryDataDTO;
import com.dsp.bios.mapper.NewEntryToBill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.repository.LSEntryDataEntityRepository;
import com.dsp.bios.repository.LSUploadBillTextEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FormService {

    private final LSBillsViewDetailRepository billsRepo;
    private final LSEntryDataEntityRepository entryRepo;
    private final LSUploadBillTextEntityRepository billTextRepo;
    private final NewEntryToBill mapper;



   @Transactional
    public Long save(EntryDataDTO dto) {
        Bill billView =  mapper.entryDataDTOtoBill(dto);
        billView.setIsDraft(false);
       billView= billsRepo.save(billView);
       EntryData entryData=new EntryData();
       entryData.setBillId(billView.getId());
        entryData.setEntryData(dto);
       entryData=entryRepo.save(entryData);
        return  entryData.getBillId();
    }


}