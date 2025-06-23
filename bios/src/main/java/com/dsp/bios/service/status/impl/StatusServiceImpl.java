package com.dsp.bios.service.status.impl;

import com.dsp.bios.dto.status.BillStatusDTO;
import com.dsp.bios.dto.status.PassedStatusDTO;
import com.dsp.bios.dto.status.PendingStatusDTO;
import com.dsp.bios.entity.BillStatus;
import com.dsp.bios.entity.PassedStatus;
import com.dsp.bios.entity.PendingStatus;
import com.dsp.bios.repository.BillStatusRepository;
import com.dsp.bios.repository.CodeRepo;
import com.dsp.bios.repository.PassedStatusRepository;
import com.dsp.bios.repository.PendingStatusRepository;
import com.dsp.bios.service.status.StatusService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class StatusServiceImpl implements StatusService {

    private final BillStatusRepository billStatusRepository;
    private PendingStatusRepository pendingStatusRepository;
    private PassedStatusRepository passedStatusRepository;
    private CodeRepo codeRepo;


    @Transactional
    public void updateBillStatuses(Long billId, List<BillStatusDTO> billStatusDTOs) {
        List<Long> requestBillStatusIds = billStatusDTOs.stream()
                .map(BillStatusDTO::getBillStatusId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (BillStatusDTO dto : billStatusDTOs) {
            BillStatus billStatus = billStatusRepository.findByBillIdAndBillStatusId(billId, dto.getBillStatusId())
                    .orElse(new BillStatus());
            billStatus.setBillId(billId);
            billStatus.setBillStatusId(dto.getBillStatusId());
            billStatus.setStatusDate(dto.getDate());
            billStatusRepository.save(billStatus);
        }
        billStatusRepository.deactivateBillStatusesNotInList(billId, requestBillStatusIds);
    }

    @Transactional
    public void createOrUpdatePendingStatuses(Long billId, List<PendingStatusDTO> pendingStatusDTOs) {
        List<Long> requestPendingStatusIds = pendingStatusDTOs.stream()
                .map(PendingStatusDTO::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        for (PendingStatusDTO dto : pendingStatusDTOs) {

            PendingStatus pendingStatus = pendingStatusRepository.findById(dto.getId())
                    .orElse(new PendingStatus());

            pendingStatus.setBillId(billId);
            pendingStatus.setPendingStatusId(dto.getPendingStatusId());
            pendingStatus.setPendingStatusDate(dto.getPendingStatusDate());

            pendingStatusRepository.save(pendingStatus);
        }
        pendingStatusRepository.deactivatePendingStatuses(billId, requestPendingStatusIds);
        Long passedCodeId=codeRepo.getIdByAbbr("BILL_STATUS_PENDING");
        Optional<LocalDate> maxPassedDateOpt = pendingStatusDTOs.stream()
                .map(PendingStatusDTO::getPendingStatusDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder());
        BillStatus billStatus = billStatusRepository.findByBillIdAndBillStatusId(billId,passedCodeId)
                .orElse(new BillStatus());
        billStatus.setBillId(billId);
        billStatus.setBillStatusId(passedCodeId);
        if(maxPassedDateOpt.isPresent()){
            billStatus.setStatusDate(maxPassedDateOpt.get());
        } else {
            billStatus.setIsActive(Boolean.FALSE);
        }
        billStatusRepository.save(billStatus);
    }

    @Transactional
    public void createOrUpdatePassedStatuses(Long billId, List<PassedStatusDTO> passedStatusDTOs) {

        List<Long> ids = passedStatusDTOs.stream()
                .map(PassedStatusDTO::getId)
                .filter(Objects::nonNull)
                .toList();
        for (PassedStatusDTO dto : passedStatusDTOs) {
            PassedStatus passedStatus = passedStatusRepository.findById(dto.getId())
                    .orElse(new PassedStatus());

            passedStatus.setBillId(billId);
            passedStatus.setHouseId(dto.getHouseId());
            passedStatus.setLokSabhaNumber(dto.getLokSabhaNumber());
            passedStatus.setLokSabhaSessionNumber(dto.getLokSabhaSessionNumber());
            passedStatus.setRajyaSabhaSessionNumber(dto.getRajyaSabhaSessionNumber());
            passedStatus.setPassedDate(dto.getPassedDate());
            passedStatusRepository.save(passedStatus);
        }
        passedStatusRepository.deactivatePassedStatuses(billId, ids);
        Long passedCodeId=codeRepo.getIdByAbbr("BILL_STATUS_PASSED");
        Optional<LocalDate> maxPassedDateOpt = passedStatusDTOs.stream()
                .map(PassedStatusDTO::getPassedDate)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder());
        BillStatus billStatus = billStatusRepository.findByBillIdAndBillStatusId(billId,passedCodeId)
                .orElse(new BillStatus());
        billStatus.setBillId(billId);
        billStatus.setBillStatusId(passedCodeId);
        if(maxPassedDateOpt.isPresent()){
            billStatus.setStatusDate(maxPassedDateOpt.get());
        } else {
            billStatus.setIsActive(Boolean.FALSE);
        }
        billStatusRepository.save(billStatus);
    }

}