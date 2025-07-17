package com.dsp.bios.service.impl;

import com.dsp.bios.entity.UploadBillText;
import com.dsp.bios.dto.uploadBillText.UploadBillTextDTO;
import com.dsp.bios.exception.ResourceNotFoundException;
import com.dsp.bios.repository.CodeRepo;
import com.dsp.bios.repository.LSUploadBillTextEntityRepository;
import com.dsp.bios.service.HistoryService;
import com.dsp.bios.service.UploadBillTextService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class UploadBillTextServiceImpl implements UploadBillTextService {
    private final LSUploadBillTextEntityRepository urepo;
    private final CodeRepo codeRepo;
    private final HistoryService historyService;

    @Transactional
    public void save(Long id, List<UploadBillTextDTO> dto) {

//        Set<String> uniqueAbbrs = dto.stream()
//                .map(UploadBillTextDTO::getTextVersionAbbr)
//                .collect(Collectors.toSet());
//        Long activeAbbrCount = codeRepo.countByAbbrInAndIsDeletedAndIsActiveAndLocale(uniqueAbbrs, false, true, Contants.EN_LOCALE);
//        if (activeAbbrCount != (long) uniqueAbbrs.size()) {
//            throw new RuntimeException("Bad Request");
//        }
        int i = urepo.update(id, dto);
        if (i == 0) {
            UploadBillText t = new UploadBillText();
            t.setId(id);
            t.setUploadBillTextDTO(dto);
            urepo.save(t);
        }
        historyService.create(id,"Bill text","Update");
    }

    @Transactional(readOnly = true)
    public List<UploadBillTextDTO> getById(Long id) {
        UploadBillText dbEntry = urepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Entity not found with ID: " + id));
        return dbEntry.getUploadBillTextDTO();
    }
}
