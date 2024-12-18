package com.dsp.bios.repository;

import com.dsp.bios.Entity.EntryData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LSEntryDataEntityRepository extends JpaRepository<EntryData,Long> {
    EntryData getByBillId(Long id);
}


