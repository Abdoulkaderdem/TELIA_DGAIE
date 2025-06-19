package com.telia.Lease_management.dto.requests;

import com.telia.Lease_management.entity.Ministry;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MinistryDto {
    
    private Long id;
    private String name;
    private String description;
    private String phone;
    private String address;
    private String manager;
    private String code;

    public static MinistryDto fromEntityMinistry (Ministry ministry){
        if (ministry == null) {
            return null;
        }

        return MinistryDto.builder()
                .id(ministry.getId())
                .name(ministry.getName())
                .description(ministry.getDescription())
                .phone(ministry.getPhone())
                .address(ministry.getAddress())
                .manager(ministry.getManager())
                .code(ministry.getCode())
                .build();
    }

    public static Ministry toEntityMinistry (MinistryDto dto){
        if (dto == null) {
            return null;
        }

        Ministry ministry= new Ministry();
        ministry.setId(dto.id);
        ministry.setName(dto.getName());
        ministry.setDescription(dto.getDescription());
        ministry.setPhone(dto.getPhone());
        ministry.setManager(dto.getManager());
        ministry.setAddress(dto.getAddress());
        ministry.setCode(dto.getCode());

        return ministry;
    }

    
}
