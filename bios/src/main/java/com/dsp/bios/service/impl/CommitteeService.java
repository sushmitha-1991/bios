package com.dsp.bios.service.impl;

import com.dsp.bios.Entity.ReferToCommittee;
import com.dsp.bios.dto.committee.CommitteeTypeDTO;
import com.dsp.bios.repository.LSReferToCommitteeEntityRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CommitteeService {



    private final LSReferToCommitteeEntityRepository crepo;
    @Transactional
    public void save(Long id, CommitteeTypeDTO dto) {

        int i = crepo.update(id,dto);
        if(i==0){
            ReferToCommittee t = new ReferToCommittee();
            t.setId(id);
            t.setCommitteeData(dto);
            crepo.save(t);
        }
    }

    @Transactional(readOnly = true)
    public CommitteeTypeDTO getById(Long id) {
        return crepo.getReferenceById(id).getCommitteeData();
    }
}
