package com.telia.Lease_management.entity.common;

public enum RentalStatus {
    //Status for rental damand 
    NEW,
    VALIDATE,
    SEND,
    COMPLEMENT,
    APPROVAL,
    REJECTED,
    HELD,
    BATIMENT_FOUND,
    BUILDING_CONFORM,
    SATISFACTORY,
    FINALIZED,
    //Status for Building
    AVAILABLE,
    RETAINED,
    MATCHED,
    NO_CONFORM,
    RENT,
    NOT_AVAILABLE,
    //Other Status
    ENABLE,
    DISABLED,
}
