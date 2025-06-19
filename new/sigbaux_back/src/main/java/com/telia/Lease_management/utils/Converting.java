package com.telia.Lease_management.utils;

public class Converting {

    private static final String[] chiffres = { "zéro", "un", "deux", "trois", "quatre", "cinq", "six", "sept", "huit",
    "neuf" };
    private static final String[] dizaine = { "", "dix", "vingt", "trente", "quarante", "cinquante", "soixante",
        "soixante-dix", "quatre-vingt", "quatre-vingt-dix" };
    private static final String[] autres = { "cent", "mille", "million", "milliard" };


    public static String NumberToWords(Long nombre) {
        if (nombre == 0) {
            return chiffres[0];
        }

        String enLettre = "";

        if (nombre < 10) {
            enLettre = chiffres[nombre.intValue()];
        } else if (nombre < 20) {
            switch (nombre.intValue()) {
                case 10:
                    enLettre = dizaine[1];
                    break;
                case 11:
                    enLettre = "onze";
                    break;
                case 12:
                    enLettre = "douze";
                    break;
                case 13:
                    enLettre = "treize";
                    break;
                case 14:
                    enLettre = "quatorze";
                    break;
                case 15:
                    enLettre = "quinze";
                    break;
                case 16:
                    enLettre = "seize";
                    break;
                case 17:
                    enLettre = "dix-sept";
                    break;
                case 18:
                    enLettre = "dix-huit";
                    break;
                case 19:
                    enLettre = "dix-neuf";
                    break;
            }
        } else if (nombre < 100) {
            int dizaineVal = nombre.intValue() / 10;
            int uniteVal = nombre.intValue() % 10;
            if (dizaineVal == 7 || dizaineVal == 9) {
                enLettre = dizaine[dizaineVal - 1] + "-" + NumberToWords((long) (10 + uniteVal));
            } else {
                enLettre = dizaine[dizaineVal];
                if (uniteVal == 1 && (dizaineVal == 1 || dizaineVal == 7 || dizaineVal == 9)) {
                    enLettre += "-et-" + chiffres[uniteVal];
                } else if (uniteVal > 0) {
                    enLettre += "-" + chiffres[uniteVal];
                }
            }
        } else if (nombre < 1000) {
            int centaineVal = nombre.intValue() / 100;
            int reste = nombre.intValue() % 100;
            if (centaineVal == 1) {
                enLettre = autres[0];
            } else {
                enLettre = chiffres[centaineVal] + "-" + autres[0];
            }
            if (reste > 0) {
                enLettre += "-" + NumberToWords((long) reste);
            }
        } else if (nombre < 1000000) {
            int milleVal = nombre.intValue() / 1000;
            int reste = nombre.intValue() % 1000;
            if (milleVal == 1) {
                enLettre = "mille";
            } else {
                enLettre = NumberToWords((long) milleVal) + "-" + autres[1];
            }
            if (reste > 0) {
                enLettre += "-" + NumberToWords((long) reste);
            }
        } else if (nombre < 1000000000) {
            int millionVal = nombre.intValue() / 1000000;
            int reste = nombre.intValue() % 1000000;
            if (millionVal == 1) {
                enLettre = "un-" + autres[2];
            } else {
                enLettre = NumberToWords((long) millionVal) + "-" + autres[2];
            }
            if (reste > 0) {
                enLettre += "-" + NumberToWords((long) reste);
            }
        } else {
            int milliardVal = nombre.intValue() / 1000000000;
            int reste = nombre.intValue() % 1000000000;
            if (milliardVal == 1) {
                enLettre = "un-" + autres[3];
            } else {
                enLettre = NumberToWords((long) milliardVal) + "-" + autres[3];
            }
            if (reste > 0) {
                enLettre += "-" + NumberToWords((long) reste);
            }
        }

        enLettre = enLettre.replace("-zéro", "").replace("  ", " ").replace("--", "-").trim();
        if (enLettre.endsWith("-")) {
            enLettre = enLettre.substring(0, enLettre.length() - 1);
        }

        return enLettre;

    }
    
}
