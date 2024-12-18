package com.dsp.bios.entity;

import com.dsp.bios.dto.NewBillEntry.EntryDataDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

@Data
@Entity
@Table(name="entry_data",schema = "bills")
@Audited
public class EntryData extends Auditable  {

    @Id
    private Long billId;


    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "entry_data", columnDefinition = "jsonb")
    private EntryDataDTO entryData;

}
