package com.telia.Lease_management.dto.requests;

import java.util.List;
import java.util.stream.Collectors;

import com.telia.Lease_management.entity.CNOI.CommitteeMember;
import com.telia.Lease_management.entity.CNOI.ValidationCommittee;
import com.telia.Lease_management.entity.rental_request.TypeUsage;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CommitteeMemberDto {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String matricule;
    private String phoneNumber;
    private String email;
    private String function;
    // private ValidationCommittee committee;


    public static CommitteeMemberDto fromEntity(CommitteeMember member) {
        if (member == null) {
            return null;
        }
        return CommitteeMemberDto.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .matricule(member.getMatricule())
                .phoneNumber(member.getPhoneNumber())
                .email(member.getEmail())
                .function(member.getFunction())
                // .committee(member.getCommittee())
                .build();
    }

    public static CommitteeMember toEntity(CommitteeMemberDto dto) {
        if (dto == null) {
            return null;
        }
        CommitteeMember member = new CommitteeMember();
        member.setId(dto.getId());
        member.setFirstName(dto.getFirstName());
        member.setLastName(dto.getLastName());
        member.setMatricule(dto.getMatricule());
        member.setPhoneNumber(dto.getPhoneNumber());
        member.setEmail(dto.getEmail());
        member.setFunction(dto.getFunction());
        // member.setCommittee(dto.getCommittee());
        return member;
    }

        
    public static List<CommitteeMember> toEntities(List<CommitteeMemberDto> dtos) {
        if (dtos == null) {
            return null;
        }

        return dtos.stream()
                .map(CommitteeMemberDto::toEntity)
                .collect(Collectors.toList());
    }


    
}
