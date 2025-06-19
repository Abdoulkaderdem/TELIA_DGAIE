package com.telia.Lease_management.utils;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import com.telia.Lease_management.entity.Users;
import com.telia.Lease_management.entity.Validation;
import com.telia.Lease_management.repository.ValidationRepository;

import java.time.Instant;
import java.util.Random;

import static java.time.temporal.ChronoUnit.MINUTES;

@AllArgsConstructor
@Service
public class ValidationService {

    private ValidationRepository validationRepository;
    //private NotificationService notificationService;

    public void enregistrer(Users user) {
        Validation validation = new Validation();
        validation.setUser(user);
        Instant creation = Instant.now();
        Instant expiration = creation.plus(10, MINUTES);
        Random random = new Random();
        int randomInteger = random.nextInt(999999);
        String code = String.format("%06d", randomInteger);

        validation.setExpiration(expiration);
        validation.setCreation(creation);
        validation.setCode(code);
        
        this.validationRepository.save(validation);
        //this.notificationService.envoyer(validation);
    }

    public Validation lireEnFonctionDuCode(String code) {
        return this.validationRepository.findByCode(code).orElseThrow(() -> new RuntimeException("Votre code est invalide"));
    }
}
