package com.telia.Lease_management.dto.responses;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.dto.requests.MinisterialStructureDto;
import com.telia.Lease_management.dto.requests.TypeUsageDto;
import com.telia.Lease_management.dto.requests.ValidationCommitteeDto;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.RentalOffer;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@AllArgsConstructor
@Getter
@Setter
public class RentalRequestResponse {

      private Long id;
    private Instant dateRequest;
    private String description;
    private RentalStatus status;
    // private boolean isValidate;
    private String motivationRequest;
    private String structureCurrentPosition;
    private String agentsNumber;
    private String managersNumber;
    private String rejectionReason; 
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

    private String leasePortfolioMinistry;
    private String buildingsOccupancyStatus;
    private List<TypeUsageResponse> listBuildingUsageDto;
    private MinisterialStructureDto structure;
    private List<RequestAndCharacteristicsResponse> listRequestAndCharacteristicsResponses;



    public static RentalRequestResponse fromEntity(RentalRequest rentalRequest) {
        List<TypeUsageResponse> typeUsageResponses = rentalRequest.getListBuildingUsage().stream()
                .map(TypeUsageResponse::fromEntity)
                .collect(Collectors.toList());

        List<RequestAndCharacteristicsResponse> requestAndCharacteristicsResponses = rentalRequest.getListRequestAndCharacteristics().stream()
                .map(RequestAndCharacteristicsResponse::fromEntity)
                .collect(Collectors.toList());

        MinisterialStructureDto ministerialStructureDto = MinisterialStructureDto.fromEntityMinistryStrucure(rentalRequest.getStructure());

        return new RentalRequestResponse(
            rentalRequest.getId(),
            rentalRequest.getDateRequest(),
            rentalRequest.getDescription(),
            rentalRequest.getStatus(),
            // rentalRequest.isValidate(),
            rentalRequest.getMotivationRequest(),
            rentalRequest.getStructureCurrentPosition(),
            rentalRequest.getAgentsNumber(),
            rentalRequest.getManagersNumber(),
            rentalRequest.getRejectionReason(),
            rentalRequest.getRegion(),
            rentalRequest.getProvince(),
            rentalRequest.getCommune(),
            rentalRequest.getCity(),
            rentalRequest.getDistrict(),
            rentalRequest.getSector(),
            rentalRequest.getRegionDesired(),
            rentalRequest.getProvinceDesired(),
            rentalRequest.getCommuneDesired(),
            rentalRequest.getCityDesired(),
            rentalRequest.getDistrictDesired(),
            rentalRequest.getSectorDesired(),
            rentalRequest.getLeasePortfolioMinistry(),
            rentalRequest.getBuildingsOccupancyStatus(),
            typeUsageResponses,
            ministerialStructureDto,
            requestAndCharacteristicsResponses
        );
     
    }
  

}
