package com.telia.Lease_management.repository.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.contract.ApplyForCompensation;
import com.telia.Lease_management.entity.contract.Contract;

import java.util.List;
import java.util.Optional;



public interface ApplyForCompensationRepository extends JpaRepository <ApplyForCompensation, Long> {

    Optional<ApplyForCompensation> findByContract(Contract contract);
    
}
