package com.telia.Lease_management.entity.CNOI;

import java.math.BigDecimal;

import com.telia.Lease_management.entity.common.Locality;
import com.telia.Lease_management.entity.common.TypeBuilding;
import com.telia.Lease_management.entity.common.Zone;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RentPrice {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(EnumType.STRING)
    private Locality locality;

    @Enumerated(EnumType.STRING)
    private Zone zone;
        
    @Enumerated(EnumType.STRING)
    private TypeBuilding typeBuilding;
    
    private double  pricePerSquareMeter;
}
