package com.dsp.bios.repository;

import com.dsp.bios.entity.UploadBillText;
import com.dsp.bios.dto.uploadBillText.UploadBillTextDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LSUploadBillTextEntityRepository extends JpaRepository<UploadBillText,Long> {
    @Modifying
    @Query("update UploadBillText u set u.uploadBillTextDTO = :dto where u.id = :id")
    int update(Long id, List<UploadBillTextDTO> dto);
}