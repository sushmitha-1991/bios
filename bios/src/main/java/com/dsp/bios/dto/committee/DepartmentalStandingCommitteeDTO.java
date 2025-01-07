package com.dsp.bios.dto.committee;


import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonTypeName("DepartmentalStandingCommittee")
public class DepartmentalStandingCommitteeDTO extends CommitteeTypeDTO {



    @NotNull(message = "department committee names is mandatory")
    String dscName;

    @NotNull(message = "department committee Id is mandatory")
    String dscAbbr;


    @NotNull(message = "Referred on date is mandatory")
    LocalDate dscReferredOnDate;

    @NotNull(message = "Report presented on date is mandatory")
    LocalDate dscReportPresentedOnDate;
}