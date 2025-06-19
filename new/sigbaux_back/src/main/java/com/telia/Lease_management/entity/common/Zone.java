package com.telia.Lease_management.entity.common;

public enum Zone {
    ZH("ZONE HABITATION"),
    ZCI("ZONE COMMERCIAL ET INDUSTRIELLE"),
    ZRA("ZONE RESIDENTIELLE ET ADMINISTRATIVE");

    private final String libelle;

    Zone(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
