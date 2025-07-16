package com.dsp.bios.dto.committee;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonTypeName("JointCommitteeForm")
public class JointCommitteeDTO extends CommitteeTypeDTO {


    @NotNull(message = "Motion for reference to JC date is mandatory")
    LocalDate jcMotionForReferenceToJCDate;

    @NotNull(message = "Motion for concurrence date is mandatory")
    LocalDate jcMotionForConcurrenceDate;

    LocalDate jcDateOfPresentation;


}