package com.dsp.bios.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Data
@Entity
@Table(name = "pending_status", schema = "bills")
@Audited
public class PendingStatus extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pending_status_id")
    private Long pendingStatusId;

    @Column(name = "pending_status_date")
    private LocalDate pendingStatusDate;


    @Column(name = "bill_id")
    private Long billId;

    @ManyToOne
    @JoinColumn(name = "bill_id",insertable = false,updatable = false)
    private Bill bill;

    @Column(name = "display_sequence")
    private Long displaySequence;
}