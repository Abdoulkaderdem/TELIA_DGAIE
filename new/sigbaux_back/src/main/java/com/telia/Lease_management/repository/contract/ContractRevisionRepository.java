package com.telia.Lease_management.repository.contract;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.ContractRevision;

public interface ContractRevisionRepository  extends JpaRepository<ContractRevision, Long> {
    
    List<ContractRevision> findByContractId(Long contractId);

    List<ContractRevision> findByContractIdAndStatus(Long contractId, RentalStatus status);

    
}