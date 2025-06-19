package com.telia.Lease_management.repository.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.contract.Indemnisation;

public interface IndemnisationRepository extends JpaRepository <Indemnisation, Long>{
    
}
