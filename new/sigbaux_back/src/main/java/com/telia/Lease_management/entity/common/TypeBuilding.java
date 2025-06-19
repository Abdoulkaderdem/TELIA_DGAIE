package com.telia.Lease_management.entity.common;

public enum TypeBuilding {
    // CATEGORIE 1 : BATIMENT EN BANCO (B)
    B1("B1", "Bâtiment à usage de bureau ou d'habitation réalisé à partir de briques en terre avec chape, les enduits en ciment, la toiture en tôles et les portes et fenêtres en persiennes."),
    
    // CATEGORIE 2 : BATIMENT EN SEMI-DUR (S)
    S1("S1", "Bâtiment à usage de bureau ou d'habitation réalisé à partir de briques en terre sur une fondation en béton avec une chape, enduite en ciment, peinture intérieure, couverture en tôles, portes et fenêtres en persiennes."),
    S2("S2", "Bâtiment ayant les mêmes caractéristiques que la variante S1 auxquelles s'ajoute un faux plafond."),
    S3("S3", "Bâtiment réalisé à partir des BTG avec structure en béton armé, avec chape, enduits en ciment, peinture intérieure, couverture en tôles ou tuiles, portes et fenêtres vitrées ou en persiennes."),
    S4("S4", "Bâtiment ayant les mêmes caractéristiques que la variante S3 auxquelles s'ajoute un faux plafond."),
    
    // CATEGORIE 3 : BATIMENT EN DUR (D)
    DM("DM", "Bâtiment à usage  commercial (magasin) réalisé à partir de matériaux définitifs, avec une chape, enduits en ciment; peinture intérieure, couverture en tôles, portes et fenêtres en métallique pleine."),
    D1("D1", "Bâtiment à usage de bureau ou d'habitation réalisé à partir de matériaux définitifs, avec une chape, enduits en ciment, peinture intérieure, couverture en tôles, portes et fenêtres en persiennes."),
    D2("D2", "Bâtiment ayant les mêmes caractéristiques que la variante D1 auxquelles s'ajoute un faux plafond."),
    D3("D3", "Bâtiment réalisé à partir de matériaux définitifs, avec chape recouverte de gerflex, enduits en ciment, peinture intérieure, couverture en tôles avec faux plafond, portes et fenêtres vitrées."),
    D4("D4", "Bâtiment ayant les mêmes caractéristiques que la variante D4 avec toutefois le sol en carreaux ou assimilés."),
    
    // CATEGORIE 4 : BATIMENT A NIVEAUX (N)
    N1("N1", "Bâtiment ayant les mêmes caractéristiques que ceux de la catégorie 3 (variantes D1 à D4) avec un niveau."),
    N2("N2", "Bâtiment ayant les mêmes caractéristiques que ceux de la catégorie 3 (variantes D1 à D4) avec plusieurs niveaux.");

    private final String code;
    private final String description;

    TypeBuilding(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return code + ": " + description;
    }
}
