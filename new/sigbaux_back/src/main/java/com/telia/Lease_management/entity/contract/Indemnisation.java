package com.telia.Lease_management.entity.contract;

import java.time.Instant;
import java.time.LocalDate;

import com.telia.Lease_management.entity.common.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Indemnisation  extends AbstractEntity{
    
    @ManyToOne
    @JoinColumn(name = "termination_id", nullable = false)
    private CancelledContrat resiliationContract; 

    @Column(nullable = false)
    private Instant date;

    @Column(nullable = false)
    private String justificationFilePath; // File path for the uploaded justification

    @Column(length = 500)
    private String comment;

    @Column(nullable = false)
    private boolean isJustificationUploaded;
}
