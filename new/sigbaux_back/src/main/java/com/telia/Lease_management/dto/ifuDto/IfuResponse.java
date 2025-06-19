package com.telia.Lease_management.dto.ifuDto;

import lombok.Data;

@Data
public class IfuResponse {
    private Header ENTETE;
    private Result RESULTAT;

    @Data
    public static class Header {
        private String MESSAGE;
        private String ERREUR;
    }

    @Data
    public static class Result {
        private String IFU;
        private String NOM_RAISON_SOCIALE;
        private String ETAT;
        private String CENTRE_DES_IMPOTS;
        private String REGIME_FISCAL;
        private String NUMREGISTRE;
        private String ENCOMMERCOMERCIALE;
        private String SIEGE;
        private String FORME_JURIDIQUE;
        private String TELEPHONE;
        private String FAX;
        private String EMAIL;
        private String ADRESSE;
    }
}
