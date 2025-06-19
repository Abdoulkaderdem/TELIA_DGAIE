package com.telia.Lease_management.repository.rental_request;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.entity.rental_request.RequestAndCharacteristics;
import java.util.List;


public interface RequestAndCharacteristicsRepository extends JpaRepository<RequestAndCharacteristics, Long>{

    List<RequestAndCharacteristics> findByRentalRequest(RentalRequest rentalRequest);
    
}
