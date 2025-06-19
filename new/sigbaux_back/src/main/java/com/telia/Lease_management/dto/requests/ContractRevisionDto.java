package com.telia.Lease_management.dto.requests;

import java.time.Instant;
import java.time.LocalDateTime;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.contract.Contract;
import com.telia.Lease_management.entity.contract.ContractRevision;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ContractRevisionDto {

    private Long id;
    private String revisionDetails;
    private LocalDateTime  revisionDate;
    private RentalStatus status;
    

    public static ContractRevisionDto fromEntity(ContractRevision contractRevision) {
        return new ContractRevisionDto(
                contractRevision.getId(),
                contractRevision.getRevisionDetails(),
                contractRevision.getRevisionDate(),
                contractRevision.getStatus()
        );
    }


    public static ContractRevision toEntity(ContractRevisionDto dto) {
        
        ContractRevision contractRevision = new ContractRevision();
        contractRevision.setId(dto.getId());
        contractRevision.setRevisionDetails(dto.getRevisionDetails());
        contractRevision.setRevisionDate(dto.getRevisionDate());
        contractRevision.setStatus(dto.getStatus());

        return contractRevision;
        

    }
}
