package com.telia.Lease_management.entity.rental_request;

import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TypeUsage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String libCourt;
    private String libLong;
 
    @ManyToOne
    @JoinColumn(name = "request_id")
    private RentalRequest rentalRequest;
  
    @ManyToOne
    @JoinColumn(name = "offer_id")
    private RentalOffer rentalOffer;

    
}
