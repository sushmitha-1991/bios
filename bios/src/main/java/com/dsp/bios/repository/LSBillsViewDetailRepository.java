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
			    		select lok_sabha_session_number as sessionNo,count(id) as totalBillsIntroduced,
			count(*) filter (where status_abbr ='BILL_STATUS_PENDING') as billsPending,
			count(*) filter (where status_abbr ='BILL_STATUS_PASSED') as billsPassed,
			count(*) filter (where status_abbr ='BILL_STATUS_LAPSED') as billsLapsed
			from bills.bill
			where house_abbr='LS' and lok_sabha_session_number is not null 
			and (cast(:billType as varchar) is null or introduced_by=cast(:billType as varchar))
			and (cast(:lokSabhaNo as varchar) is null or lok_sabha_number=cast(:lokSabhaNo as varchar))
			and (cast(:sessionNo as varchar) is null or lok_sabha_session_number=cast(:sessionNo as varchar))
			group by lok_sabha_session_number
    		
    		""",nativeQuery = true)
    List<Object[]> getBillsCountByLokSabha(@Param("billType") String billType, @Param("lokSabhaNo") String lokSabhaNo, @Param("sessionNo") String sessionNo);
    
@Query(value="""
				select raj_sabha_session_number as sessionNo,count(id) as govtIntroduced,
		count(*) filter (where status_abbr ='BILL_STATUS_PENDING') as govtPending,
		count(*) filter (where status_abbr ='BILL_STATUS_PASSED') as govtPassed,
		count(*) filter (where status_abbr ='BILL_STATUS_LAPSED') as govtLapsed
		from bills.bill
		where house_abbr='RS' and raj_sabha_session_number is not null
		and (cast(:billType as varchar) is null or introduced_by=cast(:billType as varchar))
		and (cast(:sessionNo as varchar) is null or raj_sabha_session_number=cast(:sessionNo as varchar))
		group by raj_sabha_session_number
		
    		""",nativeQuery = true)
    List<Object[]> getBillsCountByRajyaSabha(@Param("billType") String billType, @Param("sessionNo") String sessionNo);
}