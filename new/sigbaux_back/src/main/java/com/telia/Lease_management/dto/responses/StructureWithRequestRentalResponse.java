package com.telia.Lease_management.dto.responses;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.dto.requests.MinistryDto;
import com.telia.Lease_management.dto.requests.RentalRequestDto;
import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.common.RentalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StructureWithRequestRentalResponse {
    
    private Long id;
    private String name;
    private String domain;
    private String phone;
    private String email;
    private String manager;
    private String code;
    private MinistryDto ministry;
    private List<RentalRequestDto> availableRentalRequests;

    // public static StructureWithRequestRentalResponse fromEntity(MinisterialStructure structure) {
    //     if (structure == null) {
    //         return null;
    //     }

    //     return StructureWithRequestRentalResponse.builder()
    //             .id(structure.getId())
    //             .name(structure.getName())
    //             .domain(structure.getDomain())
    //             .phone(structure.getPhone())
    //             .email(structure.getEmail())
    //             .manager(structure.getManager())
    //             .code(structure.getCode())
    //             .ministry(MinistryDto.fromEntityMinistry(structure.getMinistry()))
    //             .availableRentalRequests(structure.getRentalRequests().stream()
    //                     .filter(request -> request.getStatus() == RentalStatus.AVAILABLE)
    //                     .map(RentalRequestDto::fromEntity)
    //                     .collect(Collectors.toList()))
    //             .build();
    // }

}
