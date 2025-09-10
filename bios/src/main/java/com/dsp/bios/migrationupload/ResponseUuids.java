package com.dsp.bios.migrationupload;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResponseUuids {
    
    private String moduleName;
    private String uploadedUuid;
    private String uploadedFileUrl;
}
