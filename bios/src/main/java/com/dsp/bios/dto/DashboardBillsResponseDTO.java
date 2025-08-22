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
public class DashboardBillsResponseDTO {

	String sessionNo;
	String totalBillsIntroduced;
	String billsPending;
	String billsPassed;
	String billsLapsed;
	public DashboardBillsResponseDTO(String sessionNo, String totalBillsIntroduced, String billsPending,
			String billsPassed, String billsLapsed) {
		super();
		this.sessionNo = sessionNo;
		this.totalBillsIntroduced = totalBillsIntroduced;
		this.billsPending = billsPending;
		this.billsPassed = billsPassed;
		this.billsLapsed = billsLapsed;
	}
}
