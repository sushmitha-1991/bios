package com.dsp.bios.dsp1integration.repo;




import com.dsp.bios.dsp1integration.dto.BillStatusCountDto;
import com.dsp.bios.dsp1integration.dto.BillsCountResponseDto;
import com.dsp.bios.dsp1integration.dto.BillCountBySessionDTO;
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

    @Query(value = """
        select count(*) from bills.bill b 
        left join members.personal_details pd on pd.id = b.member_id 
        left join master.lok_sabha_masters lsm  on b.lok_sabha_number_id = lsm.id 
        where pd.mp_code =:mpsno and lsm.lok_sabha_number = :loksabha
        """, nativeQuery = true)
    Integer getParticipation(@Param("mpsno") String mpsno,
                             @Param("lsno") String loksabha);
    @Query(value = """
        SELECT lssm.session_number as session, count(b.*) as count FROM bills.bill b 
        left join master.lok_sabha_masters lsm on lsm.id = b.lok_sabha_number_id 
        left join master.lok_sabha_session_masters lssm on lssm.id = b.lok_sabha_session_number_id 
        left join bills.bill_status bs on bs.bill_id =b.id 
        left join master.codes c on c.id = bs.bill_status_id 
        where b.introduced_by = :category 
        and c.code = :status
        and lsm.lok_sabha_number =:lsno
        group by lssm.session_number 
        limit :limit offset :offset
        """, nativeQuery = true)
    List<BillsCountResponseDto> getBillBySession(@Param("lsno") String lsno, 
                            @Param("status") String status, 
                            @Param("category") String category,
                            @Param("limit") Integer limit,
                            @Param("offset") Integer offset);
    @Query(value = """
        SELECT lssm.session_number, count(*)  FROM bills.bill b 
        left join master.lok_sabha_masters lsm on lsm.id = b.lok_sabha_number_id 
        left join master.lok_sabha_session_masters lssm on lssm.id = b.lok_sabha_session_number_id 
        left join bills.bill_status bs on bs.bill_id =b.id 
        left join master.codes c on c.id = bs.bill_status_id 
        and c.code = :status and b.lok_sabha_session_number_id is not null 
        and b.introduced_by =:billType
        group by lssm.session_number
        limit :limit offset :offset
                    """, nativeQuery = true)
    List<Map<String, Object>> getBillByStatus(@Param("status") String status,
                                              @Param("limit") Integer limit,
                                              @Param("offset") Integer offset,
                                              @Param("billType") String billType);
        @Query(value = """
        SELECT count(b.*) FROM bills.bill b 
        left join master.lok_sabha_masters lsm on lsm.id = b.lok_sabha_number_id 
        left join master.lok_sabha_session_masters lssm on lssm.id = b.lok_sabha_session_number_id 
        left join bills.bill_status bs on bs.bill_id =b.id 
        left join master.codes c on c.id = bs.bill_status_id 
        where b.introduced_by = :category 
        and c.code = :status
        and lsm.lok_sabha_number =:lsno
        group by lssm.session_number 
        """, nativeQuery = true)
    Long getCountBillBySession(@Param("lsno") String lsno, 
                            @Param("status") String status, 
                            @Param("category") String category);
    @Query(value = """
        SELECT  count(b.*)  FROM bills.bill b 
        left join master.lok_sabha_masters lsm on lsm.id = b.lok_sabha_number_id 
        left join master.lok_sabha_session_masters lssm on lssm.id = b.lok_sabha_session_number_id 
        left join bills.bill_status bs on bs.bill_id =b.id 
        left join master.codes c on c.id = bs.bill_status_id 
        and c.code = :status and b.lok_sabha_session_number_id is not null 
        group by lssm.session_number, b.introduced_by 
            """, nativeQuery = true)
    Long getCountBillByStatus(@Param("status") String status);
}