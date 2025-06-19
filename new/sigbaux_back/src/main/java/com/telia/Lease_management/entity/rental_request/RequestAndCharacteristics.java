package com.telia.Lease_management.entity.rental_request;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class RequestAndCharacteristics extends AbstractEntity{
   
    private Long values;
    
    @ManyToOne
    @JoinColumn(name = "characteristics_id")
    private RentalCharacteristics characteristics;

    @ManyToOne
    @JoinColumn(name = "rentalRequest_id")
    private RentalRequest rentalRequest;


    
}