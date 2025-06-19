package com.telia.Lease_management.repository.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.contract.CancelledContrat;
import com.telia.Lease_management.entity.contract.Contract;

import java.util.List;
import java.util.Optional;


public interface CancelledContratRepository extends JpaRepository<CancelledContrat, Long>{

    Optional<CancelledContrat> findByContract(Contract contract);
    
}
