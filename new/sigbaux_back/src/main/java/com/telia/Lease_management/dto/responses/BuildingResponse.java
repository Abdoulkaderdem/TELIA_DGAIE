package com.telia.Lease_management.dto.responses;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.dto.requests.OfferAndCharacteristicsDto;
import com.telia.Lease_management.entity.common.BuildingStanding;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.TypePropertyTitle;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.BuildingStandingEntity;
import com.telia.Lease_management.entity.rental_offer.TypeOfferAndCharacteristics;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Getter
@Setter
@Slf4j
public class BuildingResponse {
    private Long id;
    private TypeBuilding typeBuilding;
    private TypePropertyTitle typePropertyTitle;
    private RentalStatus status;
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
    private int numberOfRoom;
    private int numberOfRoomMeeting;
    private int numberOfBathroom;
    private List<OfferAndCharacteristicsResponse> listOfferAndCharacteristicsDto;
    private List<OfferAndCharacteristicsResponse> listLandlordOfferCharacteristics;

    private List<BuildingStanding> listBuildingStanding;


     // Static method to convert Building to BuildingResponse
    public static BuildingResponse fromEntity(Building building) {
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

        return new BuildingResponse(
                building.getId(),
                building.getTypeBuilding(),
                building.getTypePropertyTitle(),
                building.getStatus(),
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
                building.getNumberOfRoom(),
                building.getNumberOfRoomMeeting(),
                building.getNumberOfBathroom(),
                offerAndCharacteristicsResponses,
                landlordOfferAndCharacteristicsResponses,
                buildingStandings
        );
    }

}
