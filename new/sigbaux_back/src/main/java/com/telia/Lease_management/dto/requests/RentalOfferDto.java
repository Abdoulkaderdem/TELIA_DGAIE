package com.telia.Lease_management.dto.requests;

import com.telia.Lease_management.entity.common.BuildingStanding;
import com.telia.Lease_management.entity.common.HouseType;
import com.telia.Lease_management.entity.common.QualityApplicant;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.TypeLandLord;
import com.telia.Lease_management.entity.common.TypePropertyTitle;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;
import com.telia.Lease_management.entity.rental_request.TypeUsage;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Builder
@Data
public class RentalOfferDto {
          
    private Long id;
    private Date dateOffer;
    private RentalStatus rentalStatus;
    private String code;
    private LandLord landLord;
    private List<BuildingDto> buildingDtos;
    private List<TypeUsageDto> listBuildingOfferUsageDto;
    private MultipartFile attestationAttributionFile; // Fichier Attestation d'Attribution
    private MultipartFile permitExploitationFile; // Fichier Permis d'Exploitation

    public static RentalOfferDto fromEntity(RentalOffer rentalOffer) {
        if (rentalOffer == null) {
            return null;
        }

        return RentalOfferDto.builder()
                .id(rentalOffer.getId())
                .dateOffer(rentalOffer.getDateOffer())
                .rentalStatus(rentalOffer.getRentalStatus())
                .code(rentalOffer.getCode())
                .landLord(rentalOffer.getLandLord())
                .buildingDtos(BuildingDto.fromEntities(rentalOffer.getBuildings()))
                .listBuildingOfferUsageDto(
                    rentalOffer.getListBuildingOfferUsage() != null ? 
                    rentalOffer.getListBuildingOfferUsage().stream()
                        .map(TypeUsageDto::fromEntity)
                        .collect(Collectors.toList()) : new ArrayList<>()
                )
                .build();
    }


    public static RentalOffer toEntity(RentalOfferDto dto) {
        if (dto == null) {
            return null;
        }

        RentalOffer rentalOffer = new RentalOffer();
        rentalOffer.setId(dto.getId());
        rentalOffer.setDateOffer(dto.getDateOffer());
        rentalOffer.setRentalStatus(dto.getRentalStatus());
        rentalOffer.setCode(dto.getCode());
        rentalOffer.setLandLord(dto.getLandLord());
        //rentalOffer.setBuildings(BuildingDto.toEntities(dto.getBuildingDtos()));
        List<Building> buildings = BuildingDto.toEntities(dto.getBuildingDtos());
        for (Building building : buildings) {
            building.setRentalOffer(rentalOffer); 
        }
        rentalOffer.setBuildings(buildings);

        if (dto.getListBuildingOfferUsageDto() != null) {
            rentalOffer.setListBuildingOfferUsage(dto.getListBuildingOfferUsageDto().stream()
                .map(TypeUsageDto::toEntity)
                .collect(Collectors.toList()));
        }
        
        return rentalOffer;
    }

}
