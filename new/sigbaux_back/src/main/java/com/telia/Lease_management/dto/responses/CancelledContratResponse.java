package com.telia.Lease_management.dto.responses;

import com.telia.Lease_management.entity.contract.CancelledContrat;
import com.telia.Lease_management.entity.contract.Contract;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CancelledContratResponse {
    private Long idCancellesContrat;
    private Long idContrat;
    Double indemnityAmount;

    
    public static CancelledContratResponse fromEntity(CancelledContrat cancelledContrat) {
        if (cancelledContrat == null) {
            return null; 
        }

        return new CancelledContratResponse(
            cancelledContrat.getId(), 
            cancelledContrat.getContract() != null ? cancelledContrat.getContract().getId() : null,
            cancelledContrat.getIndemnityAmount()
        );
    }
    
}
