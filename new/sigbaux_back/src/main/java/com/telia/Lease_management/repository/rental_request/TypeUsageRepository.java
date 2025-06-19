package com.telia.Lease_management.repository.rental_request;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.rental_request.TypeUsage;

public interface TypeUsageRepository extends JpaRepository <TypeUsage, Long>{
    
}
