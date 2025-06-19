package com.telia.Lease_management.dto.responses;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.dto.requests.BuildingDto;
import com.telia.Lease_management.dto.requests.RentalCharacteristicsDto;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Data
public class OfferAndCharacteristicsResponse {
     
    private Long id;
    private Long values; 
    // TypeOfferAndCharacteristics type;
    private RentalCharacteristicsDto characteristics;
    private Long idBuilding;

     // Static method to convert OfferAndCharacteristics to OfferAndCharacteristicsResponse
    public static OfferAndCharacteristicsResponse fromEntity(OfferAndCharacteristics offerAndCharacteristics) {
        return new OfferAndCharacteristicsResponse(
                offerAndCharacteristics.getId(),
                offerAndCharacteristics.getValues(),
                // offerAndCharacteristics.getType(),
                RentalCharacteristicsDto.fromEntityRentalCharacteristics(offerAndCharacteristics.getCharacteristics()),
                offerAndCharacteristics.getBuilding() != null ? offerAndCharacteristics.getBuilding().getId() : null
        );
    }

    //Static method to convert list OfferAndCharacteristicsResponse to list  OfferAndCharacteristics
    public static List<OfferAndCharacteristics> toEntities(List<OfferAndCharacteristicsResponse> listDtosResponse) {
        if (listDtosResponse == null) {
            return null;
        }

        return listDtosResponse.stream()
                .map(dtoResponse ->{
                    OfferAndCharacteristics offerAndCharacteristics = new OfferAndCharacteristics();
                    offerAndCharacteristics.setId(dtoResponse.getId());
                    offerAndCharacteristics.setValues(dtoResponse.getValues());

                    if (dtoResponse.getCharacteristics() != null) {
                        offerAndCharacteristics.setCharacteristics(RentalCharacteristicsDto.toEntityRentalCharacteristics(dtoResponse.getCharacteristics()));
                    }

                    if (dtoResponse.getIdBuilding() != null) {
                        Building building = new Building();
                        building.setId(dtoResponse.getIdBuilding());
                        offerAndCharacteristics.setBuilding(building);
                    }

                    return offerAndCharacteristics;
                    
                })
                .collect(Collectors.toList());
    }
}
