package com.telia.Lease_management.dto.requests;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.entity.rental_request.TypeUsage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypeUsageDto {
    
    private Long id;
    private String libCourt;
    private String libLong;
    //private RentalRequest rentalRequest;


    public static TypeUsageDto fromEntity(TypeUsage typeUsage) {
        if (typeUsage == null) {
            return null;
        }

        return TypeUsageDto.builder()
                .id(typeUsage.getId())
                .libCourt(typeUsage.getLibCourt())
                .libLong(typeUsage.getLibLong())
                //.rentalRequest(typeUsage.getRentalRequest())
                .build();
    }

    public static TypeUsage toEntity(TypeUsageDto dto) {
        if (dto == null) {
            return null;
        }

        TypeUsage typeUsage = new TypeUsage();
        typeUsage.setId(dto.getId());
        typeUsage.setLibCourt(dto.getLibCourt());
        typeUsage.setLibLong(dto.getLibLong());

        return typeUsage;
    }

    
    public static List<TypeUsage> toEntities(List<TypeUsageDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(TypeUsageDto::toEntity)
                .collect(Collectors.toList());
    }
}
