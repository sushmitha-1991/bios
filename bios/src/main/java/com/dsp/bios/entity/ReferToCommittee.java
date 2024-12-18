package com.dsp.bios.entity;

import com.dsp.bios.dto.committee.CommitteeTypeDTO;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.Audited;
import org.hibernate.type.SqlTypes;

@Entity
@Data
@Table(name="refer_to_committee",schema = "bills")
@Audited
public class ReferToCommittee  extends Auditable {

    @Id
    @Column(name = "bill_id")
    private Long id;



    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "refer_to_committee", columnDefinition = "jsonb")
    private CommitteeTypeDTO CommitteeData;


}