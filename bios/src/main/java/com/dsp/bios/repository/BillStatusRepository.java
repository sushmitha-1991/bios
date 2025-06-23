package com.dsp.bios.repository;


import com.dsp.bios.entity.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillStatusRepository extends JpaRepository<BillStatus, Long> {

    List<BillStatus> findByBillId(Long billId);

    Optional<BillStatus> findByBillIdAndBillStatusId(Long billId, Long billStatusId);

    @Modifying
    @Query("UPDATE BillStatus bs SET bs.isActive = false WHERE bs.bill.id = :billId AND bs.billStatusId NOT IN :billStatusIds")
    void deactivateBillStatusesNotInList(Long billId, List<Long> billStatusIds);
}