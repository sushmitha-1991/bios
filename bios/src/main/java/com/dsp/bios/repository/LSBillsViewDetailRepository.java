package com.dsp.bios.repository;


import com.dsp.bios.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LSBillsViewDetailRepository extends JpaRepository<Bill, Long> {

    @Query("select b from Bill b where b.isDraft=:draft and b.houseId=:houseId")
    List<Bill> findByIsDraft(boolean draft, Long houseId);
//    List<Bill> findByIsDraft(boolean isDraft);
}