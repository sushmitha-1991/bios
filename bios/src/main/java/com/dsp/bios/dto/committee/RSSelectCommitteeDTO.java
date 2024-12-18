package com.dsp.bios.dto.committee;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@JsonTypeName("RSSelectCommittee")
public class RSSelectCommitteeDTO extends CommitteeTypeDTO {

    @NotNull(message = "Motion for reference date is mandatory")
    LocalDate rsdcMotionForReferenceDate;
}