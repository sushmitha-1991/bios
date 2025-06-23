package com.dsp.bios.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;

@Entity
@Data
@Table(schema = "master",name = "houses")
@AllArgsConstructor
public class House extends Auditable{
    @Id
    private Long id;
    private String abbr;
}
