package com.dsp.bios.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BillHistoryResponseDTO {
    private Long id;
    private String formType;
    private String actionType;
    private String userName;
    private LocalDateTime actionTime;
}
