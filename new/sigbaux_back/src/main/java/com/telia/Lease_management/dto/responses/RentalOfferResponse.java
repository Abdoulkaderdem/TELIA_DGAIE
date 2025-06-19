package com.telia.Lease_management.dto.responses;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;
import com.telia.Lease_management.entity.rental_request.TypeUsage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Setter 
@Slf4j
public class RentalOfferResponse {
               
    private Long id;
    private Date dateOffer;
    private RentalStatus rentalStatus;
    private String code;
    private LandLord landLord;
    private List<BuildingResponse> buildingDtos;
    private List<TypeUsageResponse> listBuildingOfferUsage;


     // Static method to convert RentalOffer to RentalOfferResponse 
    public static RentalOfferResponse fromEntity(RentalOffer rentalOffer) {
        List<TypeUsageResponse> listTypeBuildingUsageResponses = rentalOffer.getListBuildingOfferUsage().stream()
            .map(TypeUsageResponse::fromEntity)
            .collect(Collectors.toList());

        List<BuildingResponse> buildingResponses = rentalOffer.getBuildings().stream()
                .map(BuildingResponse::fromEntity)
                .collect(Collectors.toList());
        
              
        return new RentalOfferResponse(
                rentalOffer.getId(),
                rentalOffer.getDateOffer(),
                rentalOffer.getRentalStatus(),
                rentalOffer.getCode(),
                rentalOffer.getLandLord(),
                buildingResponses,
                listTypeBuildingUsageResponses
        );
    }
}
