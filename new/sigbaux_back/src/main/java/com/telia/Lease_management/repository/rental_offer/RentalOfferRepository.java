package com.telia.Lease_management.repository.rental_offer;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;

public interface RentalOfferRepository extends JpaRepository<RentalOffer, Long>{

    List<RentalOffer> findByRentalStatus(RentalStatus status);

    //     @Query("SELECT DISTINCT ro FROM RentalOffer ro " +
    //     "JOIN ro.buildings b " +
    //     "WHERE b.region IN :criteria " +
    //     "   OR b.province IN :criteria " +
    //     "   OR b.commune IN :criteria " +
    //     "   OR b.city IN :criteria " +
    //     "   OR b.district IN :criteria ")
    // List<RentalOffer> findByBuildingsCriteria(@Param("criteria") Set<String> criteria);

    
}
