package com.telia.Lease_management.dto.requests;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.Ministry;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MinisterialStructureDto {
    
    private Long id;
    private String name;
    private String domain;
    private String phone;
    private String email;
    private String manager;
    private String code;
    private String legalStatus;
    private Long idMinistry;

    
    public static MinisterialStructureDto fromEntityMinistryStrucure (MinisterialStructure structure){
        if (structure == null) {
            return null;
        }

        return MinisterialStructureDto.builder()
                .id(structure.getId())
                .name(structure.getName())
                .domain(structure.getDomain())
                .phone(structure.getPhone())
                .email(structure.getEmail())
                .manager(structure.getManager())
                .code(structure.getCode())
                .legalStatus(structure.getLegalStatus())
                .idMinistry(structure.getMinistry().getId())
                .build();
    }

    public static MinisterialStructure toEntityMinistryStrucure (MinisterialStructureDto dto){
        if (dto == null) {
            return null;
        }

        MinisterialStructure structure= new MinisterialStructure();
        structure.setId(dto.id);
        structure.setName(dto.getName());
        structure.setDomain(dto.getDomain());
        structure.setPhone(dto.getPhone());
        structure.setEmail(dto.getEmail());
        structure.setManager(dto.getManager());
        structure.setCode(dto.getCode());
        structure.setLegalStatus(dto.getLegalStatus());
        //structure.setMinistry(dto.getMinistry());

        return structure;
    }
}
