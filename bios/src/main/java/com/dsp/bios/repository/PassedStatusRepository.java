package com.dsp.bios.repository;

import com.dsp.bios.entity.PassedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PassedStatusRepository extends JpaRepository<PassedStatus, Long> {

    List<PassedStatus> findByBillId(Long billId);

    Optional<PassedStatus> findByBillIdAndLokSabhaNumber(Long billId, Long lokSabhaNumber);

    @Modifying
    @Query("UPDATE PassedStatus ps SET ps.isActive = false WHERE ps.billId = :billId AND ( ps.id NOT IN :ids)")
    void deactivatePassedStatuses(Long billId, List<Long> ids);
}