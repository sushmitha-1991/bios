package com.dsp.bios.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "bill_history", schema = "bills")
public class BillHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bill_history_seq_id")
    @SequenceGenerator(name = "bill_history_seq_id", sequenceName = "bills.bill_history_seq_id", allocationSize = 1)
    private Long id;

    private Long billId;

    private String formType;

    private String actionType;

    private Long userId;

    private LocalDateTime actionTime;
}