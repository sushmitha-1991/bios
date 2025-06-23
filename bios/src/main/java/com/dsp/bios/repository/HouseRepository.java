package com.dsp.bios.repository;

import com.dsp.bios.entity.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface HouseRepository extends JpaRepository<House,Long> {
    @Query("select h.id from House h where h.abbr = :abbr and h.isActive=true ")
    Long findByAbbr(String abbr);
}
