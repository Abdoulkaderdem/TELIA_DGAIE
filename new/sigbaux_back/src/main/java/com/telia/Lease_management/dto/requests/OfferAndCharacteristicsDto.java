package com.telia.Lease_management.dto.requests;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OfferAndCharacteristicsDto {
    
    private Long id;
    private Long values; 
    TypeOfferAndCharacteristics type;
    private RentalCharacteristicsDto characteristics;
    private Building building;


    public static OfferAndCharacteristicsDto fromEntity(OfferAndCharacteristics offerAndCharacteristics) {
        if (offerAndCharacteristics == null) {
            return null;
        }

        return OfferAndCharacteristicsDto.builder()
                .id(offerAndCharacteristics.getId())
                .values(offerAndCharacteristics.getValues())
                .type(offerAndCharacteristics.getType())
                .characteristics(RentalCharacteristicsDto.fromEntityRentalCharacteristics(offerAndCharacteristics.getCharacteristics()))
                .building(offerAndCharacteristics.getBuilding())
                .build();
    }


    public static OfferAndCharacteristics toEntity(OfferAndCharacteristicsDto dto) {
        if (dto == null) {
            return null;
        }

        OfferAndCharacteristics offerAndCharacteristics = new OfferAndCharacteristics();
        offerAndCharacteristics.setId(dto.getId());
        offerAndCharacteristics.setValues(dto.getValues());
        offerAndCharacteristics.setType(dto.getType());
        offerAndCharacteristics.setCharacteristics(RentalCharacteristicsDto.toEntityRentalCharacteristics(dto.getCharacteristics()));
        offerAndCharacteristics.setBuilding(dto.getBuilding());

        return offerAndCharacteristics;
    }
    
    public static List<OfferAndCharacteristics> toEntities(List<OfferAndCharacteristicsDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(OfferAndCharacteristicsDto::toEntity)
                .collect(Collectors.toList());
    }
}
