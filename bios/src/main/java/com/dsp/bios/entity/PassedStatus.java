package com.dsp.bios.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "passed_status", schema = "bills")
@Audited
public class PassedStatus extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "bill_id")
    private Long billId;

    @ManyToOne
    @JoinColumn(name = "bill_id",insertable = false,updatable = false)
    private Bill bill;

    @Column(name = "house_id")
    private Long houseId;

    @Column(name = "lok_sabha_number")
    private Long lokSabhaNumber;

    @Column(name = "lok_sabha_session_number")
    private Long lokSabhaSessionNumber;

    @Column(name = "rajya_sabha_session_number")
    private Long rajyaSabhaSessionNumber;

    @Column(name = "passed_date")
    private LocalDate passedDate;
}