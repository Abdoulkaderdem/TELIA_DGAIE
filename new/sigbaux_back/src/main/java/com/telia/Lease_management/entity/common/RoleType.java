package com.telia.Lease_management.entity.common;

public enum RoleType {
    CPM("CPM"),
    ORDON("ORDONNATEUR"),
    DAIE("DAIE"),
    CNOI("CNOI"),
    DSI("DSI"),
    DGAHCMAH("DGAHCMAH"),
    ADMIN("ADMIN"),
    LANDLORD("BAILLEUR"),
    SUPER("SUPER");

    private final String libelle;

    RoleType(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
