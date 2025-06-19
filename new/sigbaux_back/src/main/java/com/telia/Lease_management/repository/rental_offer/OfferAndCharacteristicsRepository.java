package com.telia.Lease_management.repository.rental_offer;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;

import java.util.List;


public interface OfferAndCharacteristicsRepository extends JpaRepository<OfferAndCharacteristics, Long>{

    List<OfferAndCharacteristics> findByBuilding(Building building);

    List<OfferAndCharacteristics> findByBuildingAndType(Building building, TypeOfferAndCharacteristics type);

    List<OfferAndCharacteristics> findByType(TypeOfferAndCharacteristics type);

    
}
