package com.dsp.bios.repository;

import com.dsp.bios.Entity.ReferToCommittee;
import com.dsp.bios.dto.committee.CommitteeTypeDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface LSReferToCommitteeEntityRepository extends JpaRepository<ReferToCommittee,Long> {

    @Modifying
    @Query("update ReferToCommittee u set u.CommitteeData = :dto where u.id = :id")
    int update(Long id, CommitteeTypeDTO dto);
}