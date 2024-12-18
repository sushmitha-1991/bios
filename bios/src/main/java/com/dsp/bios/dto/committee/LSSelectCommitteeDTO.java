package com.dsp.bios.dto.committee;

import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
@Data
@JsonTypeName("LSSelectCommittee")
public class LSSelectCommitteeDTO extends CommitteeTypeDTO {

    @NotNull(message = "Motion for reference date is mandatory")
    LocalDate lsscMotionForReferenceDate;
}
