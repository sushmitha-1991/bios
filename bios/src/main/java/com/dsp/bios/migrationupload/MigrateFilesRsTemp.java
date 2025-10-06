package com.dsp.bios.migrationupload;


import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "migrate_files_rs_temp", schema = "migration")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MigrateFilesRsTemp {

    @Id
    private Long id;

    @Column(name = "module_name")
    private String moduleName;

    @Column(name = "file_name")
    private String fileName;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "file_uuid", columnDefinition = "text")
    private String fileUuid;

    private String locale;

    private String house;

    @Column(name = "is_updated_uuid")
    private Boolean isUpdatedUuid;
}
