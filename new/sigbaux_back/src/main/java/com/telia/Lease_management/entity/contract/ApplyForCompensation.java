package com.telia.Lease_management.entity.contract;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.LandLord;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ApplyForCompensation extends AbstractEntity {

    @OneToOne
    @JoinColumn(name = "landLord_id", nullable = false)
    private LandLord landLord;
    
    @OneToOne
    @JoinColumn(name = "contract_id", nullable = false)
    private Contract contract;
    
    @Column(name = "apply_url",  length = 1024)
    private String applyUrl; // Save URL contract apply document
    
}
