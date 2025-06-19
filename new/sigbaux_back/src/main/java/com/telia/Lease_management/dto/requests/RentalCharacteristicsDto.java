package com.telia.Lease_management.dto.requests;

import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RentalCharacteristicsDto {
    
    private Long id;
    private String libCourt;
    private String libLong;
    private Double unitPrice;
    // private String type_car;

    
    public static RentalCharacteristicsDto fromEntityRentalCharacteristics (RentalCharacteristics characteristics){
         if (characteristics == null) {
            return null;
        }

        return RentalCharacteristicsDto.builder()
                    .id(characteristics.getId())
                    .libCourt(characteristics.getLibCourt())
                    .libLong(characteristics.getLibLong())
                    .unitPrice(characteristics.getUnitPrice())
                    // .type_car(characteristics.getType_car())
                    .build();

    }

      public static RentalCharacteristics toEntityRentalCharacteristics (RentalCharacteristicsDto dto){
        if (dto == null) {
            return null;
        }

        RentalCharacteristics rentalCharacteristics = new RentalCharacteristics();

        rentalCharacteristics.setId(dto.id);
        rentalCharacteristics.setLibCourt(dto.getLibCourt());
        rentalCharacteristics.setLibLong(dto.getLibLong());
        rentalCharacteristics.setUnitPrice(dto.getUnitPrice());
        // rentalCharacteristics.setType_car(dto.getType_car());

        return rentalCharacteristics;
    }


}
