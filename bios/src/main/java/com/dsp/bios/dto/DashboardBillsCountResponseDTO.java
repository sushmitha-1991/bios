package com.dsp.bios.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Data
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class DashboardBillsCountResponseDTO {

	String sessionNo;
	String sessionNoId;
	String lokSabhaNo;
	String lokSabhaNoId;
	String totalBillsIntroduced;
	String billsPending;
	String billsPassed;
	String billsLapsed;
}
