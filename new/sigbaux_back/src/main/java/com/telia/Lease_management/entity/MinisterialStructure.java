package com.telia.Lease_management.entity;

import java.util.List;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.rental_request.RentalRequest;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class MinisterialStructure extends AbstractEntity{
    
    private String name;
    private String domain;
    private String phone;
    private String email;
    private String manager;
    private String code;
    private String legalStatus;
    private boolean activate= true;
    
    @ManyToOne
    @JoinColumn(name = "ministry_id")
    private Ministry ministry;

    // @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @OneToMany(mappedBy = "structure", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<RentalRequest> rentalRequests;
    
}
