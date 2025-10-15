package com.dsp.bios.migrationupload;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MigrateFilesRsTempRepository extends JpaRepository<MigrateFilesRsTemp, Long> 
,JpaSpecificationExecutor<MigrateFilesRsTemp>{

    @Query(value = "select abbr from master.houses where id=:house",nativeQuery = true)
    String getHouseById(Long house);

    boolean existsByModuleNameAndFileNameAndFileUuidIsNotNull(String moduleName, String originalFileName);

    Optional<MigrateFilesRsTemp> findByIdAndModuleName(Long id, String moduleName);

    Optional<MigrateFilesRsTemp> findByIdAndModuleNameAndFileUuidIsNull(Long id, String moduleName);

    boolean existsByIdAndModuleNameAndFileNameAndFileUuidIsNotNull(Long id, String moduleName, String fileName);    

    Optional<MigrateFilesRsTemp> findByIdAndModuleNameAndFileTypeAndFileNameAndFileUuidIsNull(Long id,
            String moduleName, String fileName, String fileType);

    boolean existsByIdAndModuleNameAndFileTypeAndFileNameAndFileUuidIsNotNull(Long id, String moduleName,
            String fileType, String fileName);
}
