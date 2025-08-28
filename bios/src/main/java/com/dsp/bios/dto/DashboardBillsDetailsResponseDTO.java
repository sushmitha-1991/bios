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
public class DashboardBillsDetailsResponseDTO {

	String billNo;
	String lokSabhaNo;
	String lokSabhaSessionNo;
	String rajyaSabhaSessionNo;
	String shortTitle;
	String billType;
	String billCategory;
	String status;
	String ministry;
	String memberName;
	String introductionDate;
	String passedIn;
	String passedDate;
	String actNo;
	String assentDate;
	
}
