package com.telia.Lease_management.entity.rental_offer;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.RentalStatus;
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
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "offer")
public class RentalOffer extends AbstractEntity{
    
    private Date dateOffer;
    
    @Column(name = "rental_status", columnDefinition = "smallint")
    @Enumerated(EnumType.ORDINAL)  // Mapping avec les valeurs ordinales de l'énumération
    private RentalStatus rentalStatus = RentalStatus.AVAILABLE;

    private String code;

    @Column(nullable = true)
    private String urlFileOffer;

    @Column(length = 1024)
    private String attestationAttributionPath; // Chemin pour Attestation d'Attribution
    @Column(length = 1024)
    private String permitExploitationPath; // Chemin pour Permis d'Exploitation

    @ManyToOne
    @JoinColumn(name = "landlord_id")
    private LandLord landLord;

    @OneToMany(mappedBy = "rentalOffer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Building> buildings = new ArrayList<>();
    
    @OneToMany(mappedBy = "rentalOffer", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TypeUsage> listBuildingOfferUsage = new ArrayList<>();

}



