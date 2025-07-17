package com.dsp.bios.repository;

import com.dsp.bios.entity.BillHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BillHistoryRepository extends JpaRepository<BillHistory, Long> {
}