package com.dsp.bios.repository;


import com.dsp.bios.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    
    @Query(value="""
			    		select
			    		case
			    		when house_abbr = 'LS' then lok_sabha_session_number
			    		when house_abbr = 'RS' then raj_sabha_session_number
			    		end as sessionNo,
			    		 count(id) as totalBillsIntroduced,
			count(*) filter (where status_abbr ='BILL_STATUS_PENDING') as billsPending,
			count(*) filter (where status_abbr ='BILL_STATUS_PASSED') as billsPassed,
			count(*) filter (where status_abbr ='BILL_STATUS_LAPSED') as billsLapsed
			from bills.bill
			where house_abbr=cast(:houseAbbr as varchar)
			and(
			(cast(:houseAbbr as varchar) ='LS' and lok_sabha_session_number is not null
			and (cast(:lokSabhaNo as varchar) is null or lok_sabha_number=cast(:lokSabhaNo as varchar))
			and (cast(:sessionNo as varchar) is null or lok_sabha_session_number=cast(:sessionNo as varchar)))
			or
			(cast(:houseAbbr as varchar) ='RS' and raj_sabha_session_number is not null
			and (cast(:sessionNo as varchar) is null or raj_sabha_session_number=cast(:sessionNo as varchar)))
			)
			and (cast(:billType as varchar) is null or introduced_by=cast(:billType as varchar))
			group by
			 		case
			    		when house_abbr = 'LS' then lok_sabha_session_number
			    		when house_abbr = 'RS' then raj_sabha_session_number
			    		end
    		""",nativeQuery = true)
    List<Object[]> getBillsCountByLokSabhaAndRajyaSabha(@Param("houseAbbr") String houseAbbr,@Param("billType") String billType, @Param("lokSabhaNo") String lokSabhaNo, @Param("sessionNo") String sessionNo);
    
    @Query(value="""
    		select
    		b.bill_no billNo,
    		b.lok_sabha_number lokSabhaNo,
    		b.lok_sabha_session_number lokSabhaSessionNo,
    		b.raj_sabha_session_number rajyaSabhaSessionNo,
    		b.short_title shortTitle,
    		b.bill_category billCategory,
    		b.status status,
    		b.ministry ministry,
    		b.member_name memberName,
    		b.date_of_introduction introductionDate,
    		ed.entry_data entryData
			from bills.bill b
			left join bills.entry_data ed on ed.bill_id=b.id
			where house_abbr=cast(:houseAbbr as varchar)
			and(
			(cast(:houseAbbr as varchar) ='LS' and lok_sabha_session_number is not null
			and (cast(:lokSabhaNo as varchar) is null or lok_sabha_number=cast(:lokSabhaNo as varchar))
			and (cast(:sessionNo as varchar) is null or lok_sabha_session_number=cast(:sessionNo as varchar)))
			or
			(cast(:houseAbbr as varchar) ='RS' and raj_sabha_session_number is not null
			and (cast(:sessionNo as varchar) is null or raj_sabha_session_number=cast(:sessionNo as varchar)))
			)
			and (cast(:billType as varchar) is null or introduced_by=cast(:billType as varchar))
			and (cast(:statusAbbr as varchar) is null or status_abbr=cast(:statusAbbr as varchar))

""",nativeQuery = true)
List<Object[]> getAllBillsDetails(@Param("statusAbbr") String statusAbbr,@Param("houseAbbr") String houseAbbr,@Param("billType") String billType, @Param("lokSabhaNo") String lokSabhaNo, @Param("sessionNo") String sessionNo);
    
}