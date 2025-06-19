package com.telia.Lease_management.dto.responses;

import java.time.Instant;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.rental_offer.Building;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class ContractResponse {
    
    private Long id;
    private Long buildingId;
    private Long rentAmount;
    private Instant startDate;
    private Instant endDate;
    private String terms;
    private int contractPeriodicity;
    //Signatories
    // private String president;
    // private String reporter;
    // private String contractingAuthority ;
    // private double ireAmount; 
    private RentalStatus status;
    private boolean isRevised;
    private boolean isTerminated;
    private boolean isKeyHandoverReport = false;
    private boolean isIndemnisation;

    public static ContractResponse fromEntity(Contract contract) {
        if (contract == null) {
            return null; // Or throw an exception 
        }

        return new ContractResponse(
                contract.getId(), 
                contract.getBuilding() != null ? contract.getBuilding().getId() : null,
                contract.getRentAmount(),
                contract.getStartDate(),
                contract.getEndDate(),
                contract.getTerms(),
                contract.getContractPeriodicity(),
                // contract.getPresident(),
                // contract.getReporter(),
                // contract.getContractingAuthority(),
                // contract.getIreAmount() != null ? contract.getIreAmount(): 0.0,
                contract.getStatus(),
                contract.isRevised(),
                contract.isTerminated(),
                contract.isKeyHandoverReport(),
                contract.isIndemnisation()
        );
    }
    
}
