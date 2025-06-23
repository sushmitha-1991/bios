package com.dsp.bios.repository;

import com.dsp.bios.entity.PendingStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PendingStatusRepository extends JpaRepository<PendingStatus, Long> {

    Optional<PendingStatus> findByBillIdAndPendingStatusId(Long billId, Long pendingStatusId);

    @Modifying
    @Query("UPDATE PendingStatus ps SET ps.isActive = false WHERE ps.billId = :billId AND  ps.id not in (:ids) ")
    void deactivatePendingStatuses(Long billId, List<Long> ids);

}