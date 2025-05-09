package com.dsp.bios.service;

import com.dsp.bios.entity.Bill;
import com.dsp.bios.entity.EntryData;
import com.dsp.bios.dto.NewBillEntry.EntryDataDTO;
import com.dsp.bios.entity.EntryData;
import com.dsp.bios.mapper.NewEntryToBill;
import com.dsp.bios.repository.LSBillsViewDetailRepository;
import com.dsp.bios.repository.LSEntryDataEntityRepository;
import com.dsp.bios.repository.LSUploadBillTextEntityRepository;
import com.dsp.bios.util.UserUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@AllArgsConstructor
public class FormService {

    private final LSBillsViewDetailRepository billsRepo;
    private final LSEntryDataEntityRepository entryRepo;
    private final LSUploadBillTextEntityRepository billTextRepo;
    private final NewEntryToBill mapper;
    private final UserUtils userUtils;



   @Transactional
    public Long save(EntryDataDTO dto) {
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


    public void update(Long id, EntryDataDTO dto) {
        Bill billView =  mapper.entryDataDTOtoBill(dto);
        billView.setIsDraft(false);
        billView.setId(id);
        billView.setHouseId(userUtils.getHouseId());
        billsRepo.save(billView);


        EntryData entryData=new EntryData();
        entryData.setBillId(id);
        entryData.setEntryData(dto);
        if(Boolean.TRUE.equals(dto.getRadioC())){
            dto.setRadioA(Boolean.TRUE);
            dto.setRadioB(Boolean.TRUE);
        }
        entryRepo.save(entryData);


    }
}