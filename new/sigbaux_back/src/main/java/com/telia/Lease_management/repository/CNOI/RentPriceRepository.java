package com.telia.Lease_management.repository.CNOI;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.CNOI.RentPrice;
import com.telia.Lease_management.entity.common.Locality;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.Zone;

public interface RentPriceRepository extends JpaRepository<RentPrice, Long> {

    Optional<RentPrice> findByZoneAndLocalityAndTypeBuilding(Zone zone, Locality locality, TypeBuilding typeBuilding);

    
}
