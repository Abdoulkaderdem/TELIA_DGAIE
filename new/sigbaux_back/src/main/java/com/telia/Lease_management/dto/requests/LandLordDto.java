package com.telia.Lease_management.dto.requests;

import com.telia.Lease_management.entity.common.QualityApplicant;
import com.telia.Lease_management.entity.common.TypeLandLord;
import com.telia.Lease_management.entity.rental_offer.LandLord;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LandLordDto {

    private Long id;
    private String ifu;
    private TypeLandLord typeLandLord;
    private QualityApplicant qualityApplicant;
    private String firstname;
    private String lastname;
    private String companyName;
    private String bp;
    private String phoneNumber;
    private String whatsapp;
    private String emailAdress;
    private String residencePlace;

    public static LandLordDto fromEntity(LandLord landLord) {
        if (landLord == null) {
            return null;
        }

        return LandLordDto.builder()
                .id(landLord.getId())
                .ifu(landLord.getIfu())
                .typeLandLord(landLord.getTypeLandLord())
                .qualityApplicant(landLord.getQualityApplicant())
                .firstname(landLord.getFirstname())
                .lastname(landLord.getLastname())
                .companyName(landLord.getCompanyName())
                .bp(landLord.getBp())
                .phoneNumber(landLord.getPhoneNumber())
                .whatsapp(landLord.getWhatsapp())
                .emailAdress(landLord.getEmailAdress())
                .residencePlace(landLord.getResidencePlace())
                .build();
    }

    public static LandLord toEntity(LandLordDto dto) {
        if (dto == null) {
            return null;
        }

        LandLord landLord = new LandLord();
        landLord.setId(dto.getId());
        landLord.setIfu(dto.getIfu());
        landLord.setTypeLandLord(dto.getTypeLandLord());
        landLord.setQualityApplicant(dto.getQualityApplicant());
        landLord.setFirstname(dto.getFirstname());
        landLord.setLastname(dto.getLastname());
        landLord.setCompanyName(dto.getCompanyName());
        landLord.setBp(dto.getBp());
        landLord.setPhoneNumber(dto.getPhoneNumber());
        landLord.setWhatsapp(dto.getWhatsapp());
        landLord.setEmailAdress(dto.getEmailAdress());
        landLord.setResidencePlace(dto.getResidencePlace());

        return landLord;
    }
}