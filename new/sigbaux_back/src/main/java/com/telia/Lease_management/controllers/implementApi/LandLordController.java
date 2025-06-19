package com.telia.Lease_management.controllers.implementApi;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.telia.Lease_management.controllers.interfaceApi.LandLordApi;
import com.telia.Lease_management.dto.ifuDto.IfuResponse;
import com.telia.Lease_management.dto.requests.LandLordDto;
import com.telia.Lease_management.entity.common.RentalStatus;
import com.telia.Lease_management.services.IfuService;
import com.telia.Lease_management.services.rental_offer.LandLordService;
import com.telia.Lease_management.utils.AppConfig;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@AllArgsConstructor
public class LandLordController implements LandLordApi{

    private LandLordService landLordService;
    private IfuService ifuService;
    private AppConfig appConfig;

    @Override
    public ResponseEntity<List<LandLordDto>> getAllLandLords() {
        return ResponseEntity.ok(landLordService.getAllLandLords());
    }

    @Override
    public ResponseEntity<LandLordDto> getLandLordById(Long id) {
        return ResponseEntity.ok(landLordService.getLandLordById(id));
    }

    @Override
    public ResponseEntity<LandLordDto> getLandLordByIfu(String ifu) {
        return ResponseEntity.ok(landLordService.getLandLordByIfu(ifu));
    }

    @Override
    public ResponseEntity<LandLordDto> createLandLord(LandLordDto landLordDto) {
        return ResponseEntity.ok(landLordService.save(landLordDto));
    }

    @Override
    public ResponseEntity<LandLordDto> updateLandLord(Long id, LandLordDto landLordDto) {
        return ResponseEntity.ok(landLordService.updateLandLord(id, landLordDto));
    }

    @Override
    public ResponseEntity<Void> changeLandLordStatus(Long id, RentalStatus status) {
        landLordService.changeStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @Override
    public Mono<ResponseEntity<IfuResponse>> verifierIfuV2(String numIfu) {
        return ifuService.authenticate()  // Authentification pour obtenir le token
            .flatMap(token -> ifuService.checkingIfu(numIfu, token))  // Vérification de l'IFU
            .map(response -> ResponseEntity.ok(response))  // Convertir la réponse en ResponseEntity avec OK
            .onErrorResume(e -> Mono.just(ResponseEntity.status(500).body(new IfuResponse())));  // Gérer les erreurs
}
 


     // Méthode pour contrôler l'IFU dans le contrôleur
    //  public Mono<ResponseEntity<?>> verifierIfuV2(String numIfu) {
    //     return authenticate()
    //             .flatMap(token -> checkingIfu(numIfu, token))
    //             .map(ResponseEntity::ok)
    //             .onErrorResume(error -> {
    //                 log.error("Erreur dans verifierIfuV2", error);
    //                 return Mono.just(ResponseEntity.status(500).body("Erreur : " + error.getMessage()));
    //             });
    // }
    // @Override
    // public ResponseEntity<?> verifierIfu(String numIfu) {
    //     try {
    //         // Authenticate to get the token
    //         String token = ifuService.authenticate();
    //         if (token != null) {
    //             // Vérification de l'IFU avec le token
    //             ResponseEntity<?>  result= ifuService.verifyIfu(numIfu, token);
    //             log.info("**** Resutat V1...Catch: {}", result);
    //             return ResponseEntity.ok(result);
    //         } else {
    //             throw new RuntimeException("Échec de l'authentification");
    //         }
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
    //     }
    // }

    // @Override
    // public ResponseEntity<?> verifierIfuV2(String numIfu) {
    //     try {
    //         // Authenticate to get the token
    //         String token = ifuService.authenticate();
            
    //         // Vérification de l'IFU
    //         ResponseEntity<?>  result= ifuService.checkingIfu(numIfu, token);
    //         log.info("**** Controller-Resutat V2...checking: {}", result);
            
    //         return ResponseEntity.ok(result);
    //     } catch (Exception e) {
    //         return ResponseEntity.status(500).body("Erreur : " + e.getMessage());
    //     }
    // }

}
