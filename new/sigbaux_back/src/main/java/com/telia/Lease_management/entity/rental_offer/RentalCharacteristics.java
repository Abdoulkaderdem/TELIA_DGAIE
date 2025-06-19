package com.telia.Lease_management.entity.rental_offer;

import com.telia.Lease_management.entity.common.AbstractEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RentalCharacteristics extends AbstractEntity{

    private String libCourt;
    private String libLong;
    
    @Column(nullable = false)
    private Double unitPrice;

    
}
