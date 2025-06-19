package com.telia.Lease_management.entity.contract;

import java.time.Instant;
import java.time.LocalDateTime;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.RentalStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContractRevision extends AbstractEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

    private String revisionDetails; // Details of the amendment

    private LocalDateTime  revisionDate; //// Date of the amendment
    
    @Column(name = "rental_status", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)
    private RentalStatus status = RentalStatus.ENABLE;
    
}
