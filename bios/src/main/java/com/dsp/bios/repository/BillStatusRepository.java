package com.dsp.bios.repository;


import com.dsp.bios.dsp1integration.dto.BillStatusCountDto;
import com.dsp.bios.entity.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BillStatusRepository extends JpaRepository<BillStatus, Long> {

    @Query("select bs from BillStatus bs where bs.billId = :billId")
    List<BillStatus> findByBillId(Long billId);

    Optional<BillStatus> findByBillIdAndBillStatusId(Long billId, Long billStatusId);

    @Modifying
    @Query("UPDATE BillStatus bs SET bs.isActive = false WHERE bs.bill.id = :billId AND bs.billStatusId NOT IN :billStatusIds")
    void deactivateBillStatusesNotInList(Long billId, List<Long> billStatusIds);

    @Query(value = """
        select distinct c.code from bills.bill_status bs 
        inner join master.codes c on bs.bill_status_id = c.id
                """, nativeQuery = true)
    List<String> getStatusList();
     @Query(value = """
        select  c.code as billStatus, count(bs.bill_status_id) from bills.bill_status bs 
		join bills.bill b on b.id =bs.bill_id 
        join master.codes c on bs.bill_status_id = c.id
        where b.house_abbr ='LS'
        group by c.code 
                """, nativeQuery = true)
    List<BillStatusCountDto> getBillsStatusCount(@Param("house") String house);

}