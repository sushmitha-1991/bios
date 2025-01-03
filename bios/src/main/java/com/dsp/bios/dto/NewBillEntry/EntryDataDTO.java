package com.dsp.bios.dto.NewBillEntry;


import jakarta.validation.Valid;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Valid
@ValidConditionalFields

public class EntryDataDTO {


    private String billIntroducedInHouse;

    private String billIntroducedInHouseAbbr;

    private String lokSabhaNumber;
    private Long lokSabhaNumberId;
    private String lokSabhaSessionNumber;
    private Long lokSabhaSessionNumberId;
    private Long rajSabhaSessionId;
    private String rajSabhaSession;

    private String ministryInCharge;
    private Long ministryInChargeId;
    private String introducedBy;


    private String memberName;
    private Long memberId;
    private boolean isExMember;


    private String billCategory;

    private String billCategoryAbbr;


    private String billNumber;


    private Long billYear;

    private String shortTitle;

    private String fileNo;

    private String remarks;

    private String actNo;
    private Boolean isActNo;
    private Long actYear;

    private String caNo;
    private Boolean isCANo;
    private Long caYear;

    private Boolean radioA;
    private Boolean radioB;

    private String billStatusAbbr;

    private String billStatus;

    private String pendingStatus;
    private String pendingStatusAbbr;

    private LocalDate pendingStatusDate;
    private LocalDate dateOfWithdrawn;
    private LocalDate dateOfNegatived;
    private LocalDate dateOfAssented;
    private String passedIn;
    private LocalDate passedDate;
    private String passedLokSabhaNo;
    private String passedSessionNo;



    private LocalDate dateOfIntroduction;

    private List<LocalDate> datesOfBillTakenIntoConsideration;

    private LocalDate dateOfReturnInSecondHouse;
    private String sessionNoReturn;
    private LocalDate billReferredToCommitteeConsideredOn;
    private LocalDate consideredAndPassesInJtSit;
    private LocalDate reportOnSelectCommitteeConsideredOn;
    private LocalDate dateOfPassingIn2ndHouse;
    private LocalDate reportOfJointCommitteeConsideredOn;
    private LocalDate dateOfPresidentialAssent;
    private LocalDate amendmentMadeBy2ndHouseConsideredOn;
    private LocalDate dateOfPassingInOriginHouse;



}



