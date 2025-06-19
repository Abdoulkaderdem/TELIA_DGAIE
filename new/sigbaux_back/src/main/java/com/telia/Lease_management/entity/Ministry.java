package com.telia.Lease_management.entity;

import com.telia.Lease_management.entity.common.AbstractEntity;
import com.telia.Lease_management.entity.common.RentalStatus;

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
public class Ministry extends AbstractEntity{
    
    private String name;
    private String description;
    private String phone;
    private String address;
    private String manager;
    private String code;
    private boolean activate= true;
    
}
