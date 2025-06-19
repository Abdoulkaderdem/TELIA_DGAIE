package com.telia.Lease_management.entity.common;

public enum Locality {
    OUAGA("OUAGADOUGOU"),
    BOBO("BOBO DIOULASSO"),
    CHEF_LIEUX("CHEFS-LIEUX DE REGIONS"),
    AUTRES("AUTRES LOCALITES");

    private final String libelle;

    Locality(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}
