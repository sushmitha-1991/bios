package com.dsp.bios.mapper;

import com.dsp.bios.dto.newbillentry.EntryDataDTO;
import com.dsp.bios.entity.Bill;
import org.mapstruct.Builder;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",builder = @Builder(disableBuilder = true))
public interface NewEntryToBill {

    @Mapping(source = "billIntroducedInHouseAbbr", target = "houseAbbr")

    @Mapping(source = "billNumber", target = "billNo")
    @Mapping(source = "shortTitle", target = "shortTitle")

    @Mapping(source = "billCategory", target = "billCategory")
    @Mapping(source = "billCategoryAbbr", target = "billCategoryAbbr")

    @Mapping(source = "ministryInCharge", target = "ministry")
    @Mapping(source = "ministryInChargeId", target = "ministryAbbr")

    @Mapping(source = "memberName", target = "memberName")
    @Mapping(source = "memberId", target = "memberId")

    @Mapping(source = "dateOfIntroduction", target = "dateOfIntroduction")


    @Mapping(source = "lokSabhaNumber", target = "lokSabhaNumber")
    @Mapping(source = "lokSabhaNumberId", target = "lokSabhaNumberId")

    @Mapping(source = "lokSabhaSessionNumber", target = "lokSabhaSessionNumber")
    @Mapping(source = "lokSabhaSessionNumberId", target = "lokSabhaSessionNumberId")

    @Mapping(source = "rajSabhaSession", target = "rajSabhaSessionNumber")
    @Mapping(source = "rajSabhaSessionId", target = "rajSabhaSessionNumberId")
    Bill entryDataDTOtoBill(EntryDataDTO dto);
}
