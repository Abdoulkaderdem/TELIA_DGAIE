package com.telia.Lease_management.entity.rental_request;

import com.telia.Lease_management.entity.MinisterialStructure;
import com.telia.Lease_management.entity.CNOI.ValidationCommittee;
import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.Building;
import com.telia.Lease_management.entity.rental_offer.OfferAndCharacteristics;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class RentalRequest extends AbstractEntity{

    private Instant dateRequest;
    private String description;
    
    @Column(name = "status", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL) // Mapping avec les valeurs ordinales de l'énumération
    private RentalStatus status = RentalStatus.AVAILABLE;

    //if a rental request has a at less one rental offer, validate become true
    private boolean isValidate= false;
    private String motivationRequest;
    private String structureCurrentPosition;
    private String agentsNumber;
    private String managersNumber;
    private String leasePortfolioMinistry;
    private String buildingsOccupancyStatus;

    //current geographical location
    private String region;
    private String province;
    private String commune;
    private String city;
    private String district;
    private String sector;
    //geographic location of choice
    private String regionDesired;
    private String provinceDesired;
    private String communeDesired;
    private String cityDesired;
    private String districtDesired;
    private String sectorDesired;
    //this Attributes are for matching with those in Building (for an offer)
    @Column(nullable = true)
    private int numberOfRoom=0;
    @Column(nullable = true)
    private int numberOfRoomMeeting=0;
    @Column(nullable = true)
    private int numberOfBathroom=0;
    @Column(nullable = true)
    private int parkingCapacity=0;
    
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isExaminatedCouncilMinister = false;
    
    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isValidatedCouncilMinister = false;
    
    @Column(nullable = true)
    private String rejectionReason; 

    // @OneToMany(mappedBy = "rentalRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "rentalRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TypeUsage> listBuildingUsage;

    @ManyToOne
    @JoinColumn(name = "structure_id")
    private MinisterialStructure structure;

    @ManyToOne
    @JoinColumn(name = "committee_id")
    private ValidationCommittee validationCommittee;

    
    // @OneToMany(mappedBy = "rentalRequest", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "rentalRequest", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RequestAndCharacteristics> listRequestAndCharacteristics;


    
}
