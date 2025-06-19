package com.telia.Lease_management.dto.requests;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.entity.CNOI.ValidationCommittee;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ValidationCommitteeDto {
    
    private Long id;
    private String name;
    private List<CommitteeMemberDto> members;
    private CommitteeMemberDto responsible;
    private List<RentalRequestDto> rentalRequests;

    public static ValidationCommitteeDto fromEntity(ValidationCommittee committee) {
        if (committee == null) {
            return null;
        }
        return ValidationCommitteeDto.builder()
                .id(committee.getId())
                .name(committee.getName())
                .members(committee.getMembers().stream().map(CommitteeMemberDto::fromEntity).collect(Collectors.toList()))
                .responsible(CommitteeMemberDto.fromEntity(committee.getResponsible()))
                .rentalRequests(committee.getRentalRequests() != null ? committee.getRentalRequests().stream()
                    .map(RentalRequestDto::fromEntity)
                    .collect(Collectors.toList()) : null)
                .build();
    }

    public static ValidationCommittee toEntity(ValidationCommitteeDto dto) {
        if (dto == null) {
            return null;
        }
        ValidationCommittee committee = new ValidationCommittee();
        committee.setId(dto.getId());
        committee.setName(dto.getName());
        committee.setMembers(dto.getMembers().stream().map(CommitteeMemberDto::toEntity).collect(Collectors.toList()));
        committee.setResponsible(CommitteeMemberDto.toEntity(dto.getResponsible()));
        if (dto.getRentalRequests() != null) {
            committee.setRentalRequests(dto.getRentalRequests().stream()
                .map(RentalRequestDto::toEntity)
                .collect(Collectors.toList()));
        }

        return committee;
    }
    
}
