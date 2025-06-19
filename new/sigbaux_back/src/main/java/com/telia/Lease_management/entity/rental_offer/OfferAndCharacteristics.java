package com.telia.Lease_management.entity.rental_offer;

import com.telia.Lease_management.entity.common.AbstractEntity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class OfferAndCharacteristics extends AbstractEntity{
   
    private Long values;

    TypeOfferAndCharacteristics type;
    
    @ManyToOne
    @JoinColumn(name = "characteristics_id")
    private RentalCharacteristics characteristics;

    @ManyToOne
    @JoinColumn(name = "building_id")
    private Building building;


    
}
