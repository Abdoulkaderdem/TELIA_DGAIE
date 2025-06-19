package com.telia.Lease_management.services.rental_offer;

import com.fasterxml.jackson.databind.JsonNode;
import com.telia.Lease_management.dto.ifuDto.IfuResponse;
import com.telia.Lease_management.services.IfuService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.telia.Lease_management.dto.requests.LandLordDto;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.entity.rental_offer.LandLord;
import com.telia.Lease_management.repository.rental_offer.LandLordRepository;
import com.telia.Lease_management.utils.ValidateForms;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
@Service
public class LandLordService {
    private LandLordRepository landLordRepository;
    private IfuService ifuService;

    @Transactional(readOnly = true)
    public List<LandLordDto> getAllLandLords() {
        return landLordRepository.findAll().stream()
                .map(LandLordDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public LandLordDto getLandLordById(Long id) {
        Optional<LandLord> landLord = landLordRepository.findById(id);
        return landLord.map(LandLordDto::fromEntity).orElse(null);
    }

    @Transactional
    public LandLordDto save(LandLordDto landLordDto) {
        //Check if the building form is valid
        List<String> errors= ValidateForms.validateLandLord(landLordDto);
        if (!errors.isEmpty()){
            log.error("LandLord is not valid {}:", landLordDto);
            log.error("Liste des erreurs {}: ", errors);
            throw new IllegalArgumentException("LandLord is not valid !");
        }

        // Authenticate to get the token
        // String token = ifuService.authenticate();

        // ResponseEntity<String> ifuResponse = ifuService.checkingIfu(landLordDto.getIfu(), token);
        // log.info("**** Resutat checkingIfu 1: {}", ifuResponse);
        // log.info("**** Resutat checkingIfu 2: {}", ifuResponse.getBody());
        // ResponseEntity<?> ifuResponse2 = ifuService.verifyIfu(landLordDto.getIfu(), token);
        // log.info("**** Resutat verifyIfu 1: {}", ifuResponse2);
        // log.info("**** Resutat verifyIfu 2: {}", ifuResponse2.getBody());
        // JsonNode ifuResponse = ifuService.verifyIfu(landLordDto.getIfu(), token);
        // validateIfuResponse(ifuResponse, landLordDto);

        // Map<String, Object> verificationResult = ifuService.verifyIfu(landLordDto.getIfu(), token);
        // // Accéder au résultat avec des types appropriés
        // Map<String, Object> resultat = (Map<String, Object>) verificationResult.get("RESULTAT");

        // String state = (String) resultat.get("ETAT");
        // String companyName = (String) resultat.get("NOM_RAISON_SOCIALE");

        // if (!"ACTIF".equals(state)) {
        //     throw new IllegalStateException("Le bailleur n'est pas actif selon les informations de l'API IFU.");
        // }

        // if (!companyName.equalsIgnoreCase(landLordDto.getCompanyName())) {
        //     throw new IllegalStateException("Le nom de la société de la réponse ne correspond pas à celui du LandLordDto.");
        // }

        LandLord landLord = LandLordDto.toEntity(landLordDto);
        
        //Check LandLord Ifu from DB
        Optional<LandLord>  existingLandlord = landLordRepository.findByIfu(landLordDto.getIfu());
        if (existingLandlord.isPresent()){
            throw new IllegalStateException("An User with this IFU, already exist! This: "+ landLordDto.getIfu());
        }
        //Check LandLord Adress mail
        Optional<LandLord>  existingEmailLandlord = landLordRepository.findByEmailAdress(landLordDto.getEmailAdress());
        if (existingEmailLandlord.isPresent()){
            throw new IllegalStateException("An User with this EMAIL, already exist! This: "+ landLordDto.getEmailAdress());
        }

        landLord.setStatus(RentalStatus.ENABLE);
        LandLord savedLandLord = landLordRepository.save(landLord);
        return LandLordDto.fromEntity(savedLandLord);
    }

    // Validation de la réponse de l'API IFU
    private void validateIfuResponse(IfuResponse ifuResponse, LandLordDto landLordDto) {
        IfuResponse.Result resultat = ifuResponse.getRESULTAT();
        
        if (!resultat.getIFU().equals(landLordDto.getIfu())) {
            throw new IllegalStateException("Le IFU de la réponse ne correspond pas à celui du LandLordDto.");
        }
        if (!"ACTIF".equals(resultat.getETAT())) {
            throw new IllegalStateException("Le bailleur n'est pas actif selon les informations de l'API IFU.");
        }
        if (!resultat.getNOM_RAISON_SOCIALE().equalsIgnoreCase(landLordDto.getCompanyName())) {
            throw new IllegalStateException("Le nom de la société de la réponse ne correspond pas à celui du LandLordDto.");
        }
    }
    

    @Transactional(readOnly = true)
    public LandLordDto getLandLordByIfu(String ifu) {
        Optional<LandLord> optionalLandLord = landLordRepository.findByIfu(ifu);
        return optionalLandLord.map(LandLordDto::fromEntity).orElse(null);
    }
  

    public LandLordDto updateLandLord(Long id, LandLordDto landLordDto) {

        Optional<LandLord> optionalLandLord = landLordRepository.findById(id);

        if (optionalLandLord.isPresent()) {
            LandLord landLord = optionalLandLord.get();
            landLord.setIfu(landLordDto.getIfu());
            landLord.setTypeLandLord(landLordDto.getTypeLandLord());
            landLord.setQualityApplicant(landLordDto.getQualityApplicant());
            landLord.setFirstname(landLordDto.getFirstname());
            landLord.setLastname(landLordDto.getLastname());
            landLord.setCompanyName(landLordDto.getCompanyName());
            landLord.setBp(landLordDto.getBp());
            landLord.setPhoneNumber(landLordDto.getPhoneNumber());
            landLord.setWhatsapp(landLordDto.getWhatsapp());
            landLord.setEmailAdress(landLordDto.getEmailAdress());
            landLord.setResidencePlace(landLordDto.getResidencePlace());
            return LandLordDto.fromEntity(landLordRepository.save(landLord));
        }
        return null;
    }

    @Transactional
    public void deleteById(Long id) {
        landLordRepository.deleteById(id);
    }

    public void changeStatus(Long id, RentalStatus status) {
        Optional<LandLord> landLordOptional = landLordRepository.findById(id);
        
        if (landLordOptional.isPresent()) {
            LandLord landLord = landLordOptional.get();
            landLord.setStatus(status);
            landLordRepository.save(landLord);
        }
    }
}
