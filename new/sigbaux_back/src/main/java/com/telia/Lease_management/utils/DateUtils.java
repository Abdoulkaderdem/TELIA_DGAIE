package com.telia.Lease_management.utils;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtils {

    public static Instant calculateEndDate(Instant startDate, int periodicityInMonths) {
        if (startDate == null || periodicityInMonths <= 0) {
            throw new IllegalArgumentException("Invalid start date or periodicity.");
        }

        // Convert Instant to LocalDate
        LocalDate startLocalDate = startDate.atZone(ZoneId.systemDefault()).toLocalDate();

        // Calculate end date by adding the periodicity in months
        LocalDate endLocalDate = startLocalDate.plus(Period.ofMonths(periodicityInMonths));

        // Convert LocalDate back to Instant
        ZonedDateTime endZonedDateTime = endLocalDate.atStartOfDay(ZoneId.systemDefault());
        return endZonedDateTime.toInstant();
    }


    public static String formatInstantToDate(Instant instant) {
        // Convertir Instant en LocalDate en utilisant le fuseau horaire par défaut
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        // Créer un formateur de date pour le format jj/MM/yyyy
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        // Formater le LocalDate en chaîne
        return localDate.format(formatter);
    }
}