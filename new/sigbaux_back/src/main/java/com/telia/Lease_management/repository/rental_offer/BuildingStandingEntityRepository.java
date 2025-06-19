package com.telia.Lease_management.repository.rental_offer;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.BuildingStandingEntity;

public interface BuildingStandingEntityRepository extends JpaRepository<BuildingStandingEntity, Long>{

    
}
