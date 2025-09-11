package com.dsp.bios.migrationupload;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MigrateFilesLsTempRepository extends JpaRepository<MigrateFilesLsTemp, Long> 
,JpaSpecificationExecutor<MigrateFilesLsTemp>{

    @Query(value = "select abbr from master.houses where id=:house",nativeQuery = true)
    String getHouseById(Long house);

    boolean existsByModuleNameAndFileNameAndFileUuidIsNotNull(String moduleName, String originalFileName);

    Optional<MigrateFilesLsTemp> findByIdAndModuleName(Long id, String moduleName);

    Optional<MigrateFilesLsTemp> findByIdAndModuleNameAndFileUuidIsNull(Long id, String moduleName);
}
