package com.dsp.bios.repository;


import com.dsp.bios.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LSBillsViewDetailRepository extends JpaRepository<Bill, Long> {

    @Query("select b from Bill b where b.isDraft=:draft")
    List<Bill> findByIsDraft(boolean draft);

    @Query("select exists (  select 1 from Bill b where trim(b.billNo)=trim(:billNumber) and b.billYear=:billYear  )")
    boolean checkIfBillNumberAlreadyExits(String billNumber,Long billYear);

    @Query("select exists (  select 1 from Bill b where trim(b.billNo)=trim(:billNumber)  and b.billYear=:billYear and b.id != :id)")
    boolean checkIfBillNumberAlreadyExitsNotForId(String billNumber, Long billYear,Long id);
}