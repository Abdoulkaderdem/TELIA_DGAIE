package com.telia.Lease_management.repository.rental_offer;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_request.RentalRequest;


public interface BuildingRepository   extends JpaRepository<Building, Long>{
  
    @Query("SELECT DISTINCT b FROM Building b " +
          "WHERE (b.region IN :criteria " +
          "   OR b.province IN :criteria " +
          "   OR b.commune IN :criteria " +
          "   OR b.city IN :criteria " +
          "   OR b.district IN :criteria) " +
          "AND b.status = :status")
    List<Building> findBuildingsByCriteriaAndStatus(@Param("criteria") Set<String> criteria, @Param("status") RentalStatus status);


    @Query("SELECT b FROM Building b WHERE b.status = :status")
    List<Building> findBuildingsByStatus(@Param("status") RentalStatus status);


    List<Building> findByStatus(RentalStatus status);

    @Query("SELECT b FROM Building b WHERE b.rentalRequest.status = :status")
    List<Building> findByRentalRequestStatus(@Param("status") RentalStatus status);


    List<Building> findByRentalRequestIdAndStatus(Long rentalRequestId, RentalStatus status);

    List<Building> findByRentalRequestId(Long rentalRequestId);

    @Query("SELECT b FROM Building b WHERE b.rentalRequest.id = :rentalRequestId AND b.provisionalRentAmount IS NOT NULL")
    Optional<Building> findBuildingsWithNonNullProvisionalRentAmount(@Param("rentalRequestId") Long rentalRequestId);


    @Query("SELECT b FROM Building b WHERE b.rentalRequest.id = :idRentalRequest " +
    "AND b.provisionalRentAmount IS NOT NULL " +
    "AND b.status = :status")
    Optional<Building> findBuildingWithValidProvisionalRentAmount(@Param("idRentalRequest") Long idRentalRequest, @Param("status") RentalStatus status);

    
}
