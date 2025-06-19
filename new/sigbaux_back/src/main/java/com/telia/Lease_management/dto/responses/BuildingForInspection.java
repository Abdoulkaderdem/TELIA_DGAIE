package com.telia.Lease_management.dto.responses;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.entity.common.BuildingStanding;
import com.telia.Lease_management.entity.common.Locality;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.TypePropertyTitle;
import com.telia.Lease_management.entity.common.Zone;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.BuildingStandingEntity;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Data
@Slf4j
public class BuildingForInspection {
    private Long id;
    private TypePropertyTitle typePropertyTitle;
    private String buildingValue;
    private String buildingArea;
    private String otherInformation;
    private String region;
    private String province;
    private String commune;
    private String city;
    private String district;
    private String sector;
    private String section;
    private String ilot;
    private String plot;
    private String street;
    private String doornumber;
    private String geolocation;
    private String rentPrice;
    private String code;
    private List<OfferAndCharacteristicsResponse> listOfferAndCharacteristicsDto;
    private List<OfferAndCharacteristicsResponse> listLandlordOfferCharacteristics;
    private List<BuildingStanding> listBuildingStanding;
    //Attribute for inspection
    private RentalStatus status;
    private Zone zone;
    private Locality locality;
    private TypeBuilding typeBuilding;
    private String inspectionObservation;

    
    // Static method to convert Building to BuildingForInspection
    public static BuildingForInspection fromEntity(Building building) {
        List<OfferAndCharacteristicsResponse> offerAndCharacteristicsResponses = building.getListOfferAndCharacteristics().stream()
                .filter(oAndCharactik -> oAndCharactik.getType()== TypeOfferAndCharacteristics.INSPECTION)
                .map(OfferAndCharacteristicsResponse::fromEntity)
                .collect(Collectors.toList());
               
        List<OfferAndCharacteristicsResponse> landlordOfferAndCharacteristicsResponses = building.getListLandlordOfferCharacteristics().stream()
                .filter(oAndCharactik-> oAndCharactik.getType()==TypeOfferAndCharacteristics.OFFER)
                .map(OfferAndCharacteristicsResponse::fromEntity)
                .collect(Collectors.toList());
        
        List<BuildingStanding> buildingStandings = building.getListBuildingStandings().stream()
                .map(BuildingStandingEntity::getBuildingStanding) 
                .collect(Collectors.toList());

        return new BuildingForInspection(
                building.getId(),
                building.getTypePropertyTitle(),
                building.getBuildingValue(),
                building.getBuildingArea(),
                building.getOtherInformation(),
                building.getRegion(),
                building.getProvince(),
                building.getCommune(),
                building.getCity(),
                building.getDistrict(),
                building.getSector(),
                building.getSection(),
                building.getIlot(),
                building.getPlot(),
                building.getStreet(),
                building.getDoornumber(),
                building.getGeolocation(),
                building.getRentPrice(),
                building.getCode(),
                offerAndCharacteristicsResponses,
                landlordOfferAndCharacteristicsResponses,
                buildingStandings,
                building.getStatus(),
                building.getZone(),
                building.getLocality(),
                building.getTypeBuilding(),
                building.getInspectionObservation()
        );
    }

}
