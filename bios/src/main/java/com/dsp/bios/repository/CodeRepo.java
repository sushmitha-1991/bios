package com.dsp.bios.repository;


import com.dsp.bios.entity.Code;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CodeRepo extends JpaRepository<Code, Long> {
    Long countByAbbrInAndIsDeletedAndIsActiveAndLocale(Collection<String> abbr, Boolean isDeleted, Boolean isActive, String locale);
}
