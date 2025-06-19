package com.telia.Lease_management.dto.requests;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.RentalCharacteristics;
import com.telia.Lease_management.entity.rental_request.RequestAndCharacteristics;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestAndCharacteristicsDto {
     
    private Long id;
    private Long values; 
    private RentalCharacteristicsDto characteristicsDto;
    private RentalRequestDto rentalRequestDto;


    public static RequestAndCharacteristicsDto fromEntity(RequestAndCharacteristics entity) {
        if (entity == null) {
            return null;
        }
        return RequestAndCharacteristicsDto.builder()
            .id(entity.getId())
            .values(entity.getValues())
            .characteristicsDto(RentalCharacteristicsDto.fromEntityRentalCharacteristics(entity.getCharacteristics()))
            .rentalRequestDto(RentalRequestDto.fromEntity(entity.getRentalRequest()))
            .build();
    }

    public static RequestAndCharacteristics toEntity(RequestAndCharacteristicsDto dto) {
        if (dto == null) {
            return null;
        }
        RequestAndCharacteristics entity = new RequestAndCharacteristics();
        entity.setId(dto.getId());
        entity.setValues(dto.getValues());
        entity.setCharacteristics(RentalCharacteristicsDto.toEntityRentalCharacteristics(dto.getCharacteristicsDto()));
        entity.setRentalRequest(RentalRequestDto.toEntity(dto.getRentalRequestDto()));
        return entity;
    }

    public static List<RequestAndCharacteristics> toEntities(
            List<RequestAndCharacteristicsDto> listRequestAndCharacteristicsDtos) {
         if (listRequestAndCharacteristicsDtos == null) {
            return null;
        }

        return listRequestAndCharacteristicsDtos.stream()
                .map(RequestAndCharacteristicsDto::toEntity)
                .collect(Collectors.toList());
    }
}
