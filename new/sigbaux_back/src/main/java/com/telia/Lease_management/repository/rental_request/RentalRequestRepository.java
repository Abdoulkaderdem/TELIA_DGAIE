package com.telia.Lease_management.repository.rental_request;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.rental_request.RentalRequest;
import java.util.List;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.common.RentalStatus;


public interface RentalRequestRepository extends JpaRepository<RentalRequest, Long>{

     List<RentalRequest> findByStructureAndStatus(MinisterialStructure structure, RentalStatus status);

     List<RentalRequest> findByStructure(MinisterialStructure structure);

    List<RentalRequest> findByStatus(RentalStatus status);

    List<RentalRequest> findByStatusAndIsValidatedCouncilMinister(RentalStatus status, boolean isValidatedCouncilMinister);

    
}
