package com.dsp.bios.service.status.impl;

import com.dsp.bios.dto.status.BillStatusDTO;
import com.dsp.bios.dto.status.PassedStatusDTO;
import com.dsp.bios.dto.status.PendingStatusDTO;
import com.dsp.bios.entity.BillStatus;
import com.dsp.bios.entity.PassedStatus;
import com.dsp.bios.entity.PendingStatus;
import com.dsp.bios.exception.BadRequest;
import com.dsp.bios.repository.*;
import com.dsp.bios.service.HistoryService;
import com.dsp.bios.service.status.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final BillStatusRepository billStatusRepository;
    private PendingStatusRepository pendingStatusRepository;
    private PassedStatusRepository passedStatusRepository;
    private CodeRepo codeRepo;
    private HouseRepository houseRepository;
    private HistoryService historyService;


    @Transactional
    public void updateBillStatuses(Long billId, List<BillStatusDTO> billStatusDTOs) {
        List<Long> requestBillStatusIds = billStatusDTOs.stream()
                .map(BillStatusDTO::getBillStatusId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Set<Long> statusIds=billStatusDTOs.stream().map(BillStatusDTO::getBillStatusId).collect(Collectors.toSet());
        if(statusIds.size()!=billStatusDTOs.size()){
            throw new BadRequest("Status are not unique");
        }


        for (BillStatusDTO dto : billStatusDTOs) {
            BillStatus billStatus = billStatusRepository.findByBillIdAndBillStatusId(billId, dto.getBillStatusId())
                    .orElse(new BillStatus());
            billStatus.setBillId(billId);
            billStatus.setBillStatusId(dto.getBillStatusId());
            billStatus.setStatusDate(dto.getDate());
            billStatus.setDisplaySequence(dto.getDisplaySequence());
            billStatus.setIsActive(Boolean.TRUE);
            billStatusRepository.save(billStatus);
        }
        billStatusRepository.deactivateBillStatusesNotInList(billId, requestBillStatusIds);
        historyService.create(billId,"Bill Status","Update");
    }

    @Transactional
    public void createOrUpdatePendingStatuses(Long billId, List<PendingStatusDTO> pendingStatusDTOs) {
        List<Long> requestPendingStatusIds = pendingStatusDTOs.stream()
                .map(PendingStatusDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Set<Long> statusIds=pendingStatusDTOs.stream().map(PendingStatusDTO::getPendingStatusId).collect(Collectors.toSet());
        if(statusIds.size()!=pendingStatusDTOs.size()){
            throw new BadRequest("Status are not unique");
        }
        for (PendingStatusDTO dto : pendingStatusDTOs) {

            PendingStatus pendingStatus ;
            if(dto.getId()==null){
                pendingStatus=new PendingStatus();
            }else {
                pendingStatus=
                pendingStatusRepository.findById(dto.getId())
                        .orElse(new PendingStatus());
            }
            pendingStatus.setBillId(billId);
            pendingStatus.setPendingStatusId(dto.getPendingStatusId());
            pendingStatus.setPendingStatusDate(dto.getPendingStatusDate());
            pendingStatus.setIsActive(Boolean.TRUE);
            pendingStatus.setDisplaySequence(dto.getDisplaySequence());
            pendingStatusRepository.save(pendingStatus);
            if(!requestPendingStatusIds.contains(pendingStatus.getId())){
                requestPendingStatusIds.add(pendingStatus.getId());
            }
        }
        pendingStatusRepository.deactivatePendingStatuses(billId, requestPendingStatusIds);
        Long passedCodeId=codeRepo.getIdByAbbr("BILL_STATUS_PENDING");
        Optional<LocalDate> maxPassedDateOpt = pendingStatusDTOs.stream()
                .map(PendingStatusDTO::getPendingStatusDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder());
        List<BillStatus> billStatuses=billStatusRepository.findByBillId(billId);
        BillStatus billStatus = billStatuses.stream()
                .filter(status -> status.getBillStatusId().equals(passedCodeId))
                .findFirst()
                .orElse(new BillStatus());
        billStatus.setBillId(billId);
        billStatus.setBillStatusId(passedCodeId);
        if(maxPassedDateOpt.isPresent()){
            billStatus.setStatusDate(maxPassedDateOpt.get());
            billStatus.setIsActive(Boolean.TRUE);
        } else {
            billStatus.setIsActive(Boolean.FALSE);
        }
        recalculateDisplaySequences(billStatus,billStatuses);
        billStatusRepository.save(billStatus);
        billStatusRepository.saveAll(billStatuses);
        historyService.create(billId,"Pending Status","Update");
    }

    @Transactional
    public void createOrUpdatePassedStatuses(Long billId, List<PassedStatusDTO> passedStatusDTOs) {

        List<Long> ids = passedStatusDTOs.stream()
                .map(PassedStatusDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        Set<String> statusIds=passedStatusDTOs.stream().map(PassedStatusDTO::getHouseAbbr).collect(Collectors.toSet());
        if(statusIds.size()!=passedStatusDTOs.size()){
            throw new BadRequest("Status are not unique");
        }
        for (PassedStatusDTO dto : passedStatusDTOs) {

            PassedStatus passedStatus;
            if( dto.getId()!=null ) {
                passedStatus = passedStatusRepository.findById(dto.getId())
                        .orElse(new PassedStatus());
            } else {
                passedStatus=new PassedStatus();
            }

            passedStatus.setBillId(billId);
            Long houseId=houseRepository.findByAbbr(dto.getHouseAbbr());
            passedStatus.setHouseId(houseId);
            passedStatus.setLokSabhaNumber(dto.getLokSabhaNumber());
            passedStatus.setLokSabhaSessionNumber(dto.getLokSabhaSessionNumber());
            passedStatus.setRajyaSabhaSessionNumber(dto.getRajyaSabhaSessionNumber());
            passedStatus.setPassedDate(dto.getPassedDate());
            passedStatus.setDisplaySequence(dto.getDisplaySequence());
            passedStatusRepository.save(passedStatus);
            if(!ids.contains(passedStatus.getId())){
                ids.add(passedStatus.getId());
            }
        }
        passedStatusRepository.deactivatePassedStatuses(billId, ids);
        Long passedCodeId=codeRepo.getIdByAbbr("BILL_STATUS_PASSED");
        Optional<LocalDate> maxPassedDateOpt = passedStatusDTOs.stream()
                .map(PassedStatusDTO::getPassedDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder());
        List<BillStatus> billStatuses=billStatusRepository.findByBillId(billId);
        BillStatus billStatus = billStatuses.stream()
                .filter(status -> status.getBillStatusId().equals(passedCodeId))
                .findFirst()
                .orElse(new BillStatus());
        billStatus.setBillId(billId);
        billStatus.setBillStatusId(passedCodeId);
        if(maxPassedDateOpt.isPresent()){
            billStatus.setStatusDate(maxPassedDateOpt.get());
            billStatus.setIsActive(Boolean.TRUE);
        } else {
            billStatus.setIsActive(Boolean.FALSE);
        }
        recalculateDisplaySequences(billStatus,billStatuses);
        billStatusRepository.save(billStatus);
        billStatusRepository.saveAll(billStatuses);
        historyService.create(billId,"Passed Status","Update");
    }

    private void recalculateDisplaySequences(BillStatus newStatus,List<BillStatus> billStatuses) {
        Long possibleSequence=null;
        for (BillStatus status : billStatuses) {
            if (status.getStatusDate().isBefore(newStatus.getStatusDate()) || status.getStatusDate().equals(newStatus.getStatusDate()) ) {
                if(possibleSequence!=null) {
                    newStatus.setDisplaySequence(status.getDisplaySequence());
                }
                status.setDisplaySequence(status.getDisplaySequence() + 1L);
            }
        }
        if(possibleSequence==null)  {
            newStatus.setDisplaySequence(1L);
        }
    }


}