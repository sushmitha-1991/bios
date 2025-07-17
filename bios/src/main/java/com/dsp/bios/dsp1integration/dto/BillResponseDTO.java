package com.dsp.bios.dsp1integration.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BillResponseDTO {

    private Metadata _metadata;
    private List<BillRecord> records;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Metadata {
        private int currentPageNumber;
        private int perPageSize;
        private long totalElements;
        private int totalPages;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BillRecord {
        private String billNumber;
        private String billName;
        private String billType;
        private String billCategory;
        private String ministryName;
        private Long billYear;
        private String billIntroducedInHouse;
        private String billIntroducedBy;
        private String billIntroducedDate;
        private String billIntroducedFile;
        private String billPassedInLSDate;
        private String billPassedInLSFile;
        private String billPassedInRSDate;
        private String billPassedInRSFile;
        private String billPassedInBothHousesFile;
        private String errataFile;
        private String referredToCommitteeDate;
        private String reportPresentedDate;
        private String reportFile;
        private String actNo;
        private String actYear;
        private String billAssentedDate;
        private String billGazettedFile;
        private String billSynopsisFile;
        private String status;
    }
}
