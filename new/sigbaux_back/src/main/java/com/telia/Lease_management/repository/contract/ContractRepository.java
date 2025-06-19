package com.telia.Lease_management.repository.contract;

import org.springframework.data.jpa.repository.JpaRepository;

import com.telia.Lease_management.entity.contract.Contract;
import java.util.List;
import java.util.Optional;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.Building;



public interface ContractRepository extends JpaRepository<Contract, Long> {

    List<Contract> findByStatus(RentalStatus status);

    List<Contract> findByIsTerminated(boolean terminated);

    Optional<Contract> findByBuilding(Building building);

    
}