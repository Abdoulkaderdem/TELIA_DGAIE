package com.telia.Lease_management.repository.rental_offer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import java.util.List;
import java.util.Optional;


public interface RentalCharacteristicsRepository extends JpaRepository < RentalCharacteristics, Long>{
    
    Optional<RentalCharacteristics> findByLibLong(String libLong);
    Optional<RentalCharacteristics> findByLibCourt(String libCourt);
}
