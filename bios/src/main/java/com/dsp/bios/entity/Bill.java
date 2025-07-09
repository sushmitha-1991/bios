package com.dsp.bios.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Data
@Entity
@Table(name="bill",schema = "bills")
@Audited
public class Bill extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_id_seq")
    @SequenceGenerator(schema = "bills",name = "bill_id_seq", sequenceName = "bill_id_seq", allocationSize = 1)
    private Long id;
    private String houseAbbr;
    private String billNo;
    private Long billYear;
    private String shortTitle;
    private String introducedBy;
    private String billCategory;
    private String billCategoryAbbr;
    private String ministry;
    private String ministryAbbr;
    private String memberName;
    private Long memberId;
    private LocalDate dateOfIntroduction;
    private String status;
    private String statusAbbr;
    private String lokSabhaNumber;
    private Long lokSabhaNumberId;
    private String lokSabhaSessionNumber;
    private Long lokSabhaSessionNumberId;
    private String rajSabhaSessionNumber;
    private Long rajSabhaSessionNumberId;
    private Boolean isDraft;
    private Long houseId;
}