package com.telia.Lease_management.entity.contract;

import com.telia.Lease_management.entity.common.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CancelledContrat extends AbstractEntity{
    
    @OneToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;

     @Column(nullable = true)
    private boolean isIndemnisation = false;

    private Double indemnityAmount;
    private String reasonForTermination;


}