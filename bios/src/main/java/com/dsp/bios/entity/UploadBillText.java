package com.dsp.bios.entity;

import com.dsp.bios.dto.uploadBillText.UploadBillTextDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

import java.util.List;

@Data
@Entity
@Table(name="upload_bill_text",schema = "bills")
@Audited
public class UploadBillText  extends  Auditable {
    @Id
    @Column(name = "bill_id")
    private Long id;



    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "upload_bill_text", columnDefinition = "jsonb")
    private List<UploadBillTextDTO> uploadBillTextDTO;


}
