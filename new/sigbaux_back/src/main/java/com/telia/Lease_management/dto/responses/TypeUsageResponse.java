package com.telia.Lease_management.dto.responses;

import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_request.TypeUsage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TypeUsageResponse {

    private Long id;
    private String libCourt;
    private String libLong;
    // private Long idRequest;


       public static TypeUsageResponse fromEntity(TypeUsage typeUsage) {
        return new TypeUsageResponse(
            typeUsage.getId(),
            typeUsage.getLibCourt(),
            typeUsage.getLibLong()
            // typeUsage.getRentalRequest().getId()
        );
    }
}
