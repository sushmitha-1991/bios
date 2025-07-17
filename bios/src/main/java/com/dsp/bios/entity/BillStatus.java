package com.dsp.bios.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "bill_status", schema = "bills")
@Audited
public class BillStatus extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "bill_id")
    private Long billId;

    @ManyToOne
    @JoinColumn(name = "bill_id",insertable = false,updatable = false)
    private Bill bill;

    @Column(name = "bill_status_id")
    private Long billStatusId;

    @Column(name = "status_date")
    private LocalDate statusDate;

    @Column(name = "display_sequence")
    private Long displaySequence;

}