package com.telia.Lease_management.dto.requests;

import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.entity.rental_request.TypeUsage;

import lombok.Builder;
import lombok.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class RentalRequestDto {
    
    private Long id;
    private Instant dateRequest;
    private String description;
    private RentalStatus status;
    // private boolean isValidate;
    private String motivationRequest;
    private String structureCurrentPosition;
    private String agentsNumber;
    private String managersNumber;

    // Current geographical location
    private String region;
    private String province;
    private String commune;
    private String city;
    private String district;
    private String sector;

    // Geographic location of choice
    private String regionDesired;
    private String provinceDesired;
    private String communeDesired;
    private String cityDesired;
    private String districtDesired;
    private String sectorDesired;
    //this Attributes are for matching with those in Building (for an offer)
    private int numberOfRoom;
    private int numberOfRoomMeeting;
    private int numberOfBathroom;
    private int parkingCapacity;

    private String leasePortfolioMinistry;
    private String buildingsOccupancyStatus;
    private List<TypeUsageDto> listBuildingUsageDto;
    private MinisterialStructureDto structure;
    private List<RequestAndCharacteristicsDto> listRequestAndCharacteristicsDtos ;

    
    public static RentalRequestDto fromEntity(RentalRequest rentalRequest) {
        if (rentalRequest == null) {
            return null;
        }

        return RentalRequestDto.builder()
            .id(rentalRequest.getId())
            .dateRequest(rentalRequest.getDateRequest())
            .description(rentalRequest.getDescription())
            .status(rentalRequest.getStatus())
            // .isValidate(rentalRequest.isValidate())
            .motivationRequest(rentalRequest.getMotivationRequest())
            .structureCurrentPosition(rentalRequest.getStructureCurrentPosition())
            .agentsNumber(rentalRequest.getAgentsNumber())
            .managersNumber(rentalRequest.getManagersNumber())
            .region(rentalRequest.getRegion())
            .province(rentalRequest.getProvince())
            .commune(rentalRequest.getCommune())
            .city(rentalRequest.getCity())
            .district(rentalRequest.getDistrict())
            .sector(rentalRequest.getSector())
            .regionDesired(rentalRequest.getRegionDesired())
            .provinceDesired(rentalRequest.getProvinceDesired())
            .communeDesired(rentalRequest.getCommuneDesired())
            .cityDesired(rentalRequest.getCityDesired())
            .districtDesired(rentalRequest.getDistrictDesired())
            .sectorDesired(rentalRequest.getSectorDesired())
            .numberOfRoom(rentalRequest.getNumberOfRoom())
            .numberOfRoomMeeting(rentalRequest.getNumberOfRoomMeeting())
            .numberOfBathroom(rentalRequest.getNumberOfBathroom())
            .parkingCapacity(rentalRequest.getParkingCapacity())
            .leasePortfolioMinistry(rentalRequest.getLeasePortfolioMinistry())
            .buildingsOccupancyStatus(rentalRequest.getBuildingsOccupancyStatus())
            .listBuildingUsageDto(rentalRequest.getListBuildingUsage().stream()
                .map(TypeUsageDto::fromEntity)
                .collect(Collectors.toList()))
            .structure(MinisterialStructureDto.fromEntityMinistryStrucure(rentalRequest.getStructure()))
            .listRequestAndCharacteristicsDtos(rentalRequest.getListRequestAndCharacteristics().stream()
                .map(RequestAndCharacteristicsDto::fromEntity)
                .collect(Collectors.toList()))
            .build();
    }

    
    public static RentalRequest toEntity(RentalRequestDto dto) {
        if (dto == null) {
            return null;
        }

        RentalRequest rentalRequest = new RentalRequest();
        rentalRequest.setId(dto.getId());
        rentalRequest.setDateRequest(dto.getDateRequest());
        rentalRequest.setDescription(dto.getDescription());
        rentalRequest.setStatus(dto.getStatus());
        // rentalRequest.setValidate(dto.isValidate());
        rentalRequest.setMotivationRequest(dto.getMotivationRequest());
        rentalRequest.setStructureCurrentPosition(dto.getStructureCurrentPosition());
        rentalRequest.setAgentsNumber(dto.getAgentsNumber());
        rentalRequest.setManagersNumber(dto.getManagersNumber());
        rentalRequest.setRegion(dto.getRegion());
        rentalRequest.setProvince(dto.getProvince());
        rentalRequest.setCommune(dto.getCommune());
        rentalRequest.setCity(dto.getCity());
        rentalRequest.setDistrict(dto.getDistrict());
        rentalRequest.setSector(dto.getSector());
        rentalRequest.setRegionDesired(dto.getRegionDesired());
        rentalRequest.setProvinceDesired(dto.getProvinceDesired());
        rentalRequest.setCommuneDesired(dto.getCommuneDesired());
        rentalRequest.setCityDesired(dto.getCityDesired());
        rentalRequest.setDistrictDesired(dto.getDistrictDesired());
        rentalRequest.setSectorDesired(dto.getSectorDesired());
        rentalRequest.setNumberOfRoom(dto.getNumberOfRoom());
        rentalRequest.setNumberOfRoomMeeting(dto.getNumberOfRoomMeeting());
        rentalRequest.setNumberOfBathroom(dto.getNumberOfBathroom());
        rentalRequest.setLeasePortfolioMinistry(dto.getLeasePortfolioMinistry());
        rentalRequest.setParkingCapacity(dto.getParkingCapacity());
        rentalRequest.setBuildingsOccupancyStatus(dto.getBuildingsOccupancyStatus());
        if (dto.getListBuildingUsageDto() != null) {
            rentalRequest.setListBuildingUsage(dto.getListBuildingUsageDto().stream()
                .map(TypeUsageDto::toEntity)
                .collect(Collectors.toList()));
        }

        if (dto.getStructure() != null) {
            rentalRequest.setStructure(MinisterialStructureDto.toEntityMinistryStrucure(dto.getStructure()));
        }

        if (dto.getListRequestAndCharacteristicsDtos() != null) {
            rentalRequest.setListRequestAndCharacteristics(dto.getListRequestAndCharacteristicsDtos().stream()
                .map(RequestAndCharacteristicsDto::toEntity)
                .collect(Collectors.toList()));
        }

        return rentalRequest;
    }

    
}
