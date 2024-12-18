package com.dsp.bios.dto.committee;


import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


@Data
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DepartmentalStandingCommitteeDTO.class, name="DepartmentalStandingCommittee"),
        @JsonSubTypes.Type(value = JointCommitteeDTO.class, name="JointCommitteeDTO"),
        @JsonSubTypes.Type(value = LSSelectCommitteeDTO.class,name = "LSSelectCommitteeDTO"),
        @JsonSubTypes.Type(value = RSSelectCommitteeDTO.class,name = "RSSelectCommitteeDTO"),
})
public abstract class CommitteeTypeDTO {

    @NotNull
    private Long committeeTypeId;

    @NotBlank(message = "Committee type is required")
    private String committeeName;

    @Size(max = 500, message = "Remarks must be less than 500 characters")
    private String remark;


}
