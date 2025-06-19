package com.telia.Lease_management.dto.responses;

import java.time.Instant;
import java.time.LocalDateTime;

import com.telia.Lease_management.dto.requests.ContractRevisionDto;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.ContractRevision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ContractRevisionResponse {
    
    private Long id;
    private String revisionDetails;
    private LocalDateTime  revisionDate;
    private RentalStatus status;
    private Long idContract;
    
    public static ContractRevisionResponse fromEntity(ContractRevision contractRevision) {
        
        return new ContractRevisionResponse(
                contractRevision.getId(),
                contractRevision.getRevisionDetails(),
                contractRevision.getRevisionDate(),
                contractRevision.getStatus(),
                contractRevision.getContract().getId()
        );
    }
}
