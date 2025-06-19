package com.telia.Lease_management.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

import java.util.List;
import java.util.Optional;



public interface MinisterialStrutureRepository extends JpaRepository<MinisterialStructure, Long>{
    
    Optional<MinisterialStructure> findByName(String name);

    List<MinisterialStructure> findByActivateTrue();

    Optional<MinisterialStructure> findByIdAndActivateTrue(Long id);

    @Query("SELECT ms FROM MinisterialStructure ms JOIN FETCH ms.rentalRequests rr WHERE ms.id = :structureId AND rr.status = :status")
    Optional<MinisterialStructure> findStructureWithAvailableRentalRequests(@Param("structureId") Long structureId, @Param("status") RentalStatus status);

}
