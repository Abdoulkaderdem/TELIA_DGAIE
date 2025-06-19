package com.telia.Lease_management.entity.rental_offer;

import java.util.List;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.BuildingStanding;
import com.telia.Lease_management.entity.common.Locality;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.common.RoleType;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.TypePropertyTitle;
import com.telia.Lease_management.entity.common.Zone;
import com.telia.Lease_management.entity.rental_request.RentalRequest;
import com.telia.Lease_management.entity.rental_request.TypeUsage;

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

import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Building extends AbstractEntity{
    
    @Enumerated(EnumType.STRING)
    private TypePropertyTitle typePropertyTitle;
    
    //For inspection
    @Enumerated(EnumType.STRING)
    private TypeBuilding typeBuilding;

    @Enumerated(EnumType.STRING)
    private Zone zone;
      
    @Enumerated(EnumType.STRING)
    private Locality locality;
    private String inspectionObservation;
    private String buildingArea; //Mean building surface area

    private String buildingValue;
    private String otherInformation;

    private String region;
    private String province;
    private String commune;
    private String city;
    private String district;
    private String sector;
    private String section;
    private String ilot;
    private String plot;
    private String street;
    private String doornumber;
    private String geolocation;
    
    private String rentPrice;
    private String code;
    @Column(nullable = true)
    private int parkingCapacity=0;

    //this Attributes are for matching with those in rentalreaquest
    @Column(nullable = true)
    private int numberOfRoom=0;
    @Column(nullable = true)
    private int numberOfRoomMeeting=0;
    @Column(nullable = true)
    private int numberOfBathroom=0;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isValidatedCouncilMinister = false;

    @Column(nullable = true)
    private Long provisionalRentAmount;
        
    @Column(name = "rental_status", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)
    private RentalStatus status = RentalStatus.AVAILABLE;
 
    @ManyToOne
    @JoinColumn(name = "rentOffer_id")
    private RentalOffer rentalOffer;
 
    // @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OfferAndCharacteristics> listOfferAndCharacteristics  = new ArrayList<>(); // Characteristics recorded during inspection


    // @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<OfferAndCharacteristics> listLandlordOfferCharacteristics= new ArrayList<>(); // Characteristics when the offer is made

    // @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "building", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<BuildingStandingEntity> listBuildingStandings;

    @ManyToOne
    @JoinColumn(name = "rentalRequest_id")
    private RentalRequest rentalRequest;
    
}
