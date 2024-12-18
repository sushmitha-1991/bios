package com.dsp.bios.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@Table(schema = "master",name = "codes")
@AllArgsConstructor
public class Code extends Auditable{
    @Id
    private Long id;
    private String abbr;
    private Boolean isActive;
    private Boolean isDeleted;
    private String locale;
}
