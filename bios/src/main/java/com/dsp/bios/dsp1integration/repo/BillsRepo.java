package com.dsp.bios.dsp1integration.repo;




import com.dsp.bios.dsp1integration.dto.BillStatusCountDto;
import com.dsp.bios.entity.BillStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public interface BillsRepo extends JpaRepository<BillStatus, Long> {

    @Query(value = """
        select c.code from master.codes c 
        left join master.code_types ct on ct.id = c.code_type_id 
        where ct.abbr = :codeAbbr 
            """, nativeQuery = true)
    List<String> getCodeList(@Param("codeAbbr") String codeAbbr);
    @Query(value = """
        select c.code from master.codes c 
        left join master.code_types ct on ct.id = c.code_type_id 
        where ct.abbr = :codeAbbr and c.locale =:locale
            """, nativeQuery = true)
    List<String> getCodeListWithLocale(@Param("codeAbbr") String codeAbbr,
                            @Param("locale") String locale);

    @Query(value = """
        select distinct b.bill_category from bills.bill b where b.bill_category_abbr is not null 
            """, nativeQuery = true)
    List<String> getCategoryList();

    @Query(value = """
        select distinct b.introduced_by from bills.bill b where trim(b.introduced_by) is not null
        and trim(b.introduced_by) != ''
            """, nativeQuery = true)
    List<String> getBillTypes();


    @Query(value = """
            select distinct ministry_name from master.ministries m
            """, nativeQuery = true)
    List<String> getMinistries();
      

}