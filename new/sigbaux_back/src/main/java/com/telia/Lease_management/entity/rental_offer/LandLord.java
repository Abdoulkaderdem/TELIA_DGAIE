package com.telia.Lease_management.entity.rental_offer;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.DocumentType;
import com.telia.Lease_management.entity.common.QualityApplicant;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.TypeLandLord;
import com.telia.Lease_management.entity.common.TypeUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class LandLord extends AbstractEntity{
    
    private String ifu;
    
    @Enumerated(EnumType.STRING)
    private TypeLandLord typeLandLord;

    @Column(name = "status", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL) // Mapping avec les valeurs ordinales de l'énumération
    private RentalStatus status= RentalStatus.ENABLE;

    @Enumerated(EnumType.STRING)
    private QualityApplicant qualityApplicant;

    private String firstname;
    private String lastname;
    private String companyName;
    
    private String bp;
    private String phoneNumber;
    private String whatsapp;
    private String emailAdress;
    private String residencePlace;

}
