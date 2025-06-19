package com.telia.Lease_management.dto.requests;

import com.telia.Lease_management.entity.common.BuildingStanding;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.TypePropertyTitle;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.BuildingStandingEntity;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;


import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class BuildingDto {

    private Long id;
    private TypeBuilding typeBuilding;
    private TypePropertyTitle typePropertyTitle;
    private RentalStatus status;
    //private BuildingStanding buildingStanding;
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
    private int parkingCapacity;
    private int numberOfRoom;
    private int numberOfRoomMeeting;
    private int numberOfBathroom;
    private List<OfferAndCharacteristicsDto> listOfferAndCharacteristicsDto;

    private List<BuildingStanding> listBuildingStanding;


    public static BuildingDto fromEntityBuilding(Building building) {
        if (building == null) {
            return null;
        }

        return BuildingDto.builder() 
                .id(building.getId())
                .typeBuilding(building.getTypeBuilding())
                .typePropertyTitle(building.getTypePropertyTitle())
                .status(building.getStatus())
                .listBuildingStanding(building.getListBuildingStandings().stream()
                        .map(BuildingStandingEntity::getBuildingStanding)
                        .collect(Collectors.toList()))
                .buildingValue(building.getBuildingValue())
                .buildingArea(building.getBuildingArea())
                .otherInformation(building.getOtherInformation())
                .region(building.getRegion())
                .province(building.getProvince())
                .commune(building.getCommune())
                .city(building.getCity())
                .district(building.getDistrict())
                .sector(building.getSector())
                .section(building.getSection())
                .ilot(building.getIlot())
                .plot(building.getPlot())
                .street(building.getStreet())
                .doornumber(building.getDoornumber())
                .geolocation(building.getGeolocation())
                .rentPrice(building.getRentPrice())
                .code(building.getCode())
                .parkingCapacity(building.getParkingCapacity())
                .numberOfRoom(building.getNumberOfRoom())
                .numberOfRoomMeeting(building.getNumberOfRoomMeeting())
                .numberOfBathroom(building.getNumberOfBathroom())
                .listOfferAndCharacteristicsDto(
                    building.getListOfferAndCharacteristics() != null ? 
                    building.getListOfferAndCharacteristics().stream()
                        .map(OfferAndCharacteristicsDto::fromEntity)
                        .collect(Collectors.toList()) : new ArrayList<>()
                )
                .build();
    }


    public static List<BuildingDto> fromEntities(List<Building> buildings) {
        if (buildings == null) {
            return null;
        }

        return buildings.stream()
                .map(BuildingDto::fromEntityBuilding)
                .collect(Collectors.toList())
                
                ;
    }

    public static Building toEntityBuilding(BuildingDto dto) {
        if (dto == null) {
            return null;
        }

        Building building = new Building();
        building.setId(dto.getId());
        building.setTypeBuilding(dto.getTypeBuilding());
        building.setStatus(dto.getStatus());
        building.setTypePropertyTitle(dto.getTypePropertyTitle());
        building.setBuildingValue(dto.getBuildingValue());
        building.setBuildingArea(dto.getBuildingArea());
        building.setOtherInformation(dto.getOtherInformation());
        building.setRegion(dto.getRegion());
        building.setProvince(dto.getProvince());
        building.setCommune(dto.getCommune());
        building.setCity(dto.getCity());
        building.setDistrict(dto.getDistrict());
        building.setSector(dto.getSector());
        building.setSection(dto.getSection());
        building.setIlot(dto.getIlot());
        building.setPlot(dto.getPlot());
        building.setStreet(dto.getStreet());
        building.setDoornumber(dto.getDoornumber());
        building.setGeolocation(dto.getGeolocation());
        building.setRentPrice(dto.getRentPrice());
        building.setCode(dto.getCode());
        building.setParkingCapacity(dto.getParkingCapacity());
        building.setNumberOfRoom(dto.getNumberOfRoom());
        building.setNumberOfRoomMeeting(dto.getNumberOfRoomMeeting());
        building.setNumberOfBathroom(dto.getNumberOfBathroom());
        if (dto.getListOfferAndCharacteristicsDto() != null && !dto.getListOfferAndCharacteristicsDto().isEmpty()) {
            building.setListOfferAndCharacteristics(
                    dto.getListOfferAndCharacteristicsDto().stream()
                            .map(OfferAndCharacteristicsDto::toEntity)
                            .collect(Collectors.toList())
            );
        }

        List<BuildingStandingEntity> buildingStandingEntities = dto.getListBuildingStanding().stream()
                .map(standing -> {
                    BuildingStandingEntity entity = new BuildingStandingEntity();
                    entity.setBuildingStanding(standing);
                    entity.setBuilding(building);
                    return entity;
                })
                .collect(Collectors.toList());

        building.setListBuildingStandings(buildingStandingEntities);


        return building;
    }



    public static List<Building> toEntities(List<BuildingDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(BuildingDto::toEntityBuilding)
                .collect(Collectors.toList());
    }

    
    

}
