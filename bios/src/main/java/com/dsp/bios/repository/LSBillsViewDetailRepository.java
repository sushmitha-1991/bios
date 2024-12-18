package com.dsp.bios.repository;

import com.dsp.bios.Entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LSBillsViewDetailRepository extends JpaRepository<Bill, Long> {

    @Query("select b from Bill b where b.isDraft=:b")
    List<Bill> findByIsDraft(boolean b);
//    List<Bill> findByIsDraft(boolean isDraft);
}